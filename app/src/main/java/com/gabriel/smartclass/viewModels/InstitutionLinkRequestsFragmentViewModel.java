package com.gabriel.smartclass.viewModels;

import android.annotation.SuppressLint;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gabriel.smartclass.adapter.InstitutionLinkRequestsAdapter;
import com.gabriel.smartclass.dao.InstitutionLinkRequestDAO;
import com.gabriel.smartclass.dao.InstitutionUserDAO;
import com.gabriel.smartclass.dao.LinkRequestStatusDAO;
import com.gabriel.smartclass.dao.UserDAO;
import com.gabriel.smartclass.model.InstitutionLinkRequest;
import com.gabriel.smartclass.model.InstitutionUser;
import com.gabriel.smartclass.model.User;
import com.gabriel.smartclass.view.user.fragments.institution.InstitutionLinkRequestFragment;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class InstitutionLinkRequestsFragmentViewModel extends ViewModel {
    private final InstitutionLinkRequestFragment fragment;
    private MutableLiveData<String> snackBarText = new MutableLiveData<>();
    private MutableLiveData<List<InstitutionLinkRequest>> institutionLinkRquests;
    private InstitutionLinkRequestsAdapter adapter;

    public MutableLiveData<String> getSnackBarText() {
        return snackBarText;
    }

    public MutableLiveData<List<InstitutionLinkRequest>> getInstitutionLinkRquests() {
        return institutionLinkRquests;
    }

    public InstitutionLinkRequestsFragmentViewModel(InstitutionLinkRequestFragment fragment){
        this.fragment = fragment;
    }

    public InstitutionLinkRequestsAdapter getAdapter() {
        return adapter;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void getInstitutionLinkRequests(String institutionID, DocumentReference statusReference){
        adapter = new InstitutionLinkRequestsAdapter();
        institutionLinkRquests = new MutableLiveData<>(new ArrayList<>());
        adapter.setInstitutionLinkRequestMutableLiveData(institutionLinkRquests);
        new InstitutionLinkRequestDAO().getInstitutionLinkRequests(institutionID, statusReference, task -> {
            if(task.isComplete() && task.isSuccessful()){
                Objects.requireNonNull(adapter.getInstitutionLinkRequestMutableLiveData().getValue()).addAll(task.getResult().toObjects(InstitutionLinkRequest.class));
                adapter.notifyDataSetChanged();
            }else{
                this.snackBarText.setValue("Erro ao carregar solicitações... Tente novamente mais tarde");
            }
        }, e -> this.snackBarText.setValue("Ops... algo deu errado, tente novamente mais tarde"));
    }
    public void approveOrRejectInstitutionLinkRequest(InstitutionLinkRequest linkRequest, boolean approve) {
        if (approve) {
            new UserDAO().getUserByDocumentReference(linkRequest.getUser(), task -> {
                if (task.isComplete() && task.isSuccessful() && task.getResult().exists()) {
                    createNewInstitutionUser(linkRequest, task);
                }
            }, e -> {
            });
        } else {
            rejectInstitutionRequest(linkRequest);
        }
    }
    private void rejectInstitutionRequest(InstitutionLinkRequest linkRequest) {
        HashMap<String, Object> updateLinkRequest = new HashMap<>();
        updateLinkRequest.put("linkRequestStatus_id", LinkRequestStatusDAO.REJECTED_REFERENCE);
        new InstitutionLinkRequestDAO().updateInstitutionLinkRequest(linkRequest.getId(), linkRequest.getInstitution_id().getId(), updateLinkRequest, task -> snackBarText.setValue("Solicitação recusada, essa pessoa não faz parte da sua instituição"), e -> snackBarText.setValue("Ops... Algo deu errado, tente novamente mais tarde: " + e.getMessage()));
    }

    private void createNewInstitutionUser(InstitutionLinkRequest linkRequest, Task<DocumentSnapshot> task) {
        User user = task.getResult().toObject(User.class);
        InstitutionUser institutionUser = new InstitutionUser();
        institutionUser.setId(linkRequest.getUser().getId());
        institutionUser.setUser_id(linkRequest.getUser());
        institutionUser.setUserType_id(linkRequest.getUserType());
        institutionUser.setActive(true);
        institutionUser.setId(linkRequest.getUser().getId());
        institutionUser.setDescription(user.getName());
        HashMap<String, Object> updateLinkRequest = new HashMap<>();
        updateLinkRequest.put("linkRequestStatus_id", LinkRequestStatusDAO.APPROVED_REFERENCE);
        new InstitutionLinkRequestDAO().updateInstitutionLinkRequest(linkRequest.getId(), linkRequest.getInstitution_id().getId(), updateLinkRequest, unused -> new InstitutionUserDAO().saveNewInstitutionUser(institutionUser, linkRequest.getInstitution_id(), task2 -> {
            if (task2.isComplete()) {
                List<DocumentReference> institutionsList = new ArrayList<>();
                institutionsList.add(linkRequest.getInstitution_id());
                new UserDAO().updateInstitutionsList(institutionsList, true, linkRequest.getUser(), task3 -> snackBarText.setValue("Solicitação aprovada! Agora essa pessoa é um membro da sua instituição!"));
            }
        }, e -> snackBarText.setValue("Ops... Algo deu errado, tente novamente mais tarde: " + e.getMessage())), e -> snackBarText.setValue("Ops... Algo deu errado, tente novamente mais tarde: " + e.getMessage()));
    }

    public void syncNewLinkRequests(MutableLiveData<List<InstitutionLinkRequest>> institutionLinkRequests) {
        new InstitutionLinkRequestDAO().syncNewLinkRequestInRealTime(FirebaseAuth.getInstance().getCurrentUser().getUid(), LinkRequestStatusDAO.PENDING_REFERENCE, (value, error) -> {
            if (error != null) {
                snackBarText.setValue("Ocorreu um erro ao sincronizar suas notificações");
            } else {
                if (value != null && !value.isEmpty()) {
                    List<InstitutionLinkRequest> allInstitutionLinkRequests = value.toObjects(InstitutionLinkRequest.class);
                    Stream<InstitutionLinkRequest> pendingInstitutionLinkRequests = allInstitutionLinkRequests.stream().filter(object -> object.getLinkRequestStatus_id().equals(LinkRequestStatusDAO.PENDING_REFERENCE));
                    Stream<InstitutionLinkRequest> actualPendingInstitutionLinkRequests = institutionLinkRequests.getValue().stream().filter(object -> object.getLinkRequestStatus_id().equals(LinkRequestStatusDAO.PENDING_REFERENCE));
                    if (!actualPendingInstitutionLinkRequests.collect(Collectors.toList()).equals(pendingInstitutionLinkRequests.collect(Collectors.toList()))) {
                        snackBarText.setValue("Existem novas solicitações de vínculo, atualize o conteúdo para vê-las");
                    } else {
                        snackBarText.setValue("Não existem novas notificações");
                    }
                }
            }
        });

    }

}
