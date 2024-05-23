package com.gabriel.smartclass.viewModels;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InstitutionLinkRequestsFragmentViewModel extends ViewModel {
    private final InstitutionLinkRequestDAO institutionLinkRequestDAO;
    private final InstitutionUserDAO institutionUserDAO;
    public MutableLiveData<String> snackBarText = new MutableLiveData<>();
    private final InstitutionLinkRequestsAdapter institutionLinkRequestsAdapter;


    public MutableLiveData<String> getSnackBarText() {
        return snackBarText;
    }


    public InstitutionLinkRequestsAdapter getInstitutionLinkRequestsAdapter() {
        return institutionLinkRequestsAdapter;
    }

    public InstitutionLinkRequestsFragmentViewModel() {
        institutionLinkRequestDAO = new InstitutionLinkRequestDAO();
        institutionUserDAO = new InstitutionUserDAO();
        institutionLinkRequestsAdapter = new InstitutionLinkRequestsAdapter();
    }

    public void getLinkRequests(DocumentReference statusReference){
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            institutionLinkRequestDAO.getInstitutionLinkRequests(FirebaseAuth.getInstance().getCurrentUser().getUid(), statusReference, task ->{
                List<InstitutionLinkRequest> institutionLinkRequests = new ArrayList<>();
                if(task.isComplete() && !task.getResult().isEmpty()){
                    for(QueryDocumentSnapshot snapshots : task.getResult()){
                        InstitutionLinkRequest linkRequest = snapshots.toObject(InstitutionLinkRequest.class);
                        linkRequest.setId(snapshots.getId());
                        institutionLinkRequests.add(linkRequest);
                    }
                    institutionLinkRequestsAdapter.getInstitutionLinkRequestMutableLiveData().setValue(institutionLinkRequests);
                    institutionLinkRequestsAdapter.notifyDataSetChanged();
                }else{
                    institutionLinkRequestsAdapter.getInstitutionLinkRequestMutableLiveData().setValue(new ArrayList<>());
                    institutionLinkRequestsAdapter.notifyDataSetChanged();
                }
            }, e->{
                institutionLinkRequestsAdapter.getInstitutionLinkRequestMutableLiveData().setValue(new ArrayList<>());
                institutionLinkRequestsAdapter.notifyDataSetChanged();
            });
        }
    }


    public void approveOrRejectInstitutionLinkRequest(InstitutionLinkRequest linkRequest, boolean approve) {
        if(approve){
            InstitutionUser institutionUser = new InstitutionUser();
            institutionUser.setId(linkRequest.getUser().getId());
            institutionUser.setUser_id(linkRequest.getUser());
            institutionUser.setUserType_id(linkRequest.getUserType());
            institutionUser.setActive(true);
            institutionUser.setId(linkRequest.getUser().getId());

            HashMap<String, Object> updateLinkRequest = new HashMap<>();
            updateLinkRequest.put("linkRequestStatus_id", LinkRequestStatusDAO.APPROVED_REFERENCE);
            institutionLinkRequestDAO.updateInstitutionLinkRequest(linkRequest.getId(), linkRequest.getInstitution_id().getId(), updateLinkRequest, unused -> {
                institutionUserDAO.saveNewInstitutionUser(institutionUser, linkRequest.getInstitution_id(), task -> {
                    if(task.isComplete()){
                        List<DocumentReference> institutionsList = new ArrayList<>();
                        institutionsList.add(linkRequest.getInstitution_id());
                        new UserDAO().updateInstitutionsList(institutionsList, true, linkRequest.getUser(), task2->{
                            snackBarText.setValue("Solicitação aprovada! Agora essa pessoa é um membro da sua instituição!");
                        });
                    }
                }, e -> {snackBarText.setValue("Ops... Algo deu errado, tente novamente mais tarde: " + e.getMessage());});
            }, e -> {snackBarText.setValue("Ops... Algo deu errado, tente novamente mais tarde: " + e.getMessage());});
        }else{
            HashMap<String, Object> updateLinkRequest = new HashMap<>();
            updateLinkRequest.put("linkRequestStatus_id", LinkRequestStatusDAO.REJECTED_REFERENCE);
            institutionLinkRequestDAO.updateInstitutionLinkRequest(linkRequest.getId(), linkRequest.getInstitution_id().getId(), updateLinkRequest, task -> {
                snackBarText.setValue("Solicitação recusada, essa pessoa não faz parte da sua instituição");
            }, e -> {
                snackBarText.setValue("Ops... Algo deu errado, tente novamente mais tarde: " + e.getMessage());
            });
        }
    }
    public void syncNewLinkRequests(MutableLiveData<List<InstitutionLinkRequest>> institutionLinkRequests){
        new InstitutionLinkRequestDAO().syncNewLinkRequestInRealTime(FirebaseAuth.getInstance().getCurrentUser().getUid(), LinkRequestStatusDAO.PENDING_REFERENCE, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    return;
                }else{
                    if(value!= null && !value.isEmpty()){
                        List<InstitutionLinkRequest> allInstitutionLinkRequests = value.toObjects(InstitutionLinkRequest.class);
                        Stream<InstitutionLinkRequest> pendingInstitutionLinkRequests = allInstitutionLinkRequests.stream().filter(object -> object.getLinkRequestStatus_id().equals(LinkRequestStatusDAO.PENDING_REFERENCE));
                        Stream<InstitutionLinkRequest> actualPendingInstitutionLinkRequests = institutionLinkRequests.getValue().stream().filter(object -> object.getLinkRequestStatus_id().equals(LinkRequestStatusDAO.PENDING_REFERENCE));
                        if(!actualPendingInstitutionLinkRequests.collect(Collectors.toList()).equals(pendingInstitutionLinkRequests.collect(Collectors.toList()))){
                            snackBarText.setValue("Existem novas solicitações de vínculo, atualize o conteúdo para vê-las");
                        }else{
                            snackBarText.setValue("Não existem novas notificações");
                        }
                    }
                }
            }
        });

    }

}
