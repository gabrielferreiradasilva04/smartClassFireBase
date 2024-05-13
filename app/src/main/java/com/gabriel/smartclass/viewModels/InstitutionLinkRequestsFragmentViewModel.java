package com.gabriel.smartclass.viewModels;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gabriel.smartclass.adapter.InstitutionLinkRequestsAdapter;
import com.gabriel.smartclass.dao.InstitutionLinkRequestDAO;
import com.gabriel.smartclass.dao.InstitutionUserDAO;
import com.gabriel.smartclass.dao.LinkRequestStatusDAO;
import com.gabriel.smartclass.model.InstitutionLinkRequest;

import com.gabriel.smartclass.model.InstitutionUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class InstitutionLinkRequestsFragmentViewModel extends ViewModel {
    private final InstitutionLinkRequestDAO institutionLinkRequestDAO;
    private final InstitutionUserDAO institutionUserDAO;
    public MutableLiveData<String> snackBarText = new MutableLiveData<>();
    private InstitutionLinkRequestsAdapter institutionLinkRequestsAdapter;


    public MutableLiveData<String> getSnackBarText() {
        return snackBarText;
    }

    public InstitutionUserDAO getInstitutionUserDAO() {
        return institutionUserDAO;
    }

    public InstitutionLinkRequestsAdapter getInstitutionLinkRequestsAdapter() {
        return institutionLinkRequestsAdapter;
    }

    public InstitutionLinkRequestsFragmentViewModel() {
        institutionLinkRequestDAO = new InstitutionLinkRequestDAO();
        institutionUserDAO = new InstitutionUserDAO();
        institutionLinkRequestsAdapter = new InstitutionLinkRequestsAdapter();
    }

    public void approveInstitutionLinkRequest(InstitutionLinkRequest linkRequest) {
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
                snackBarText.setValue("Solicitação aprovada! Agora essa pessoa é um membro da sua instituição!");

            }, e -> {
                snackBarText.setValue("Ops... Algo deu errado, tente novamente mais tarde: " + e.getMessage());

            });
        }, e -> {
            snackBarText.setValue("Ops... Algo deu errado, tente novamente mais tarde: " + e.getMessage());

        });
    }

    public void rejectInstitutionLinkRequest(InstitutionLinkRequest linkRequest) {
        HashMap<String, Object> updateLinkRequest = new HashMap<>();
        updateLinkRequest.put("linkRequestStatus_id", LinkRequestStatusDAO.REJECTED_REFERENCE);
        institutionLinkRequestDAO.updateInstitutionLinkRequest(linkRequest.getId(), linkRequest.getInstitution_id().getId(), updateLinkRequest, task -> {
            snackBarText.setValue("Solicitação recusada, essa pessoa não faz parte da sua instituição");
        }, e -> {
            snackBarText.setValue("Ops... Algo deu errado, tente novamente mais tarde: " + e.getMessage());
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void loadInstitutionLinkRequests(DocumentReference linkRequestStatusReference) {
        InstitutionLinkRequestDAO institutionLinkRequestDAO = new InstitutionLinkRequestDAO();
        institutionLinkRequestsAdapter = new InstitutionLinkRequestsAdapter();
        institutionLinkRequestsAdapter.setInstitutionLinkRequestMutableLiveData(new MutableLiveData<>(new ArrayList<>()));
        institutionLinkRequestDAO.getInstitutionLinkRequests(FirebaseAuth.getInstance().getCurrentUser().getUid(), linkRequestStatusReference, task -> {
            if (task.isComplete()) {
                if (task.getResult().isEmpty()) {
                    institutionLinkRequestsAdapter.notifyDataSetChanged();
                } else {
                    for (QueryDocumentSnapshot snapshots : task.getResult()) {
                        InstitutionLinkRequest linkRequest = snapshots.toObject(InstitutionLinkRequest.class);
                        linkRequest.setId(snapshots.getId());
                        institutionLinkRequestsAdapter.addItem(linkRequest);
                        institutionLinkRequestsAdapter.notifyDataSetChanged();
                    }
                }
            }
        }, e -> {
            snackBarText.setValue("Ocorreu um erro ao buscar as solicitações de vinculo: " + e.getMessage());
        });
    }
    
    public void newInstitutionLinkRequestRecieve(){
        institutionLinkRequestDAO.syncNewLinkRequestInRealTime(FirebaseAuth.getInstance().getCurrentUser().getUid(), LinkRequestStatusDAO.PENDING_REFERENCE, ((value, error) -> {
            if(error !=null){
                return;
            } else if (value != null && !value.isEmpty()) {
                List<InstitutionLinkRequest> institutionLinkRequests = value.toObjects(InstitutionLinkRequest.class);
                this.institutionLinkRequestsAdapter.getInstitutionLinkRequestMutableLiveData().setValue(institutionLinkRequests);
                this.institutionLinkRequestsAdapter.notifyDataSetChanged();
            }
        }));
    }

    public static void notificationIndex(@NonNull MutableLiveData<AtomicInteger> notificationNumber){
        AtomicInteger i = new AtomicInteger(0);
        notificationNumber.setValue(new AtomicInteger(0));
        new InstitutionLinkRequestDAO().syncNewLinkRequestInRealTime(FirebaseAuth.getInstance().getCurrentUser().getUid(), LinkRequestStatusDAO.PENDING_REFERENCE, (value, error) -> {
            if(error != null){
                return;
            }
            if(value != null && !value.isEmpty()){
                List<InstitutionLinkRequest> institutionLinkRequests = value.toObjects(InstitutionLinkRequest.class);
                for (InstitutionLinkRequest linkrequest: institutionLinkRequests) {
                    if(linkrequest.getLinkRequestStatus_id().equals(LinkRequestStatusDAO.PENDING_REFERENCE)) {
                        i.getAndIncrement();
                    }
                }
                notificationNumber.setValue(i);
            }
        });
    }


}
