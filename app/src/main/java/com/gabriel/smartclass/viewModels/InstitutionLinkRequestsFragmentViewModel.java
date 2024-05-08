package com.gabriel.smartclass.viewModels;

import android.annotation.SuppressLint;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gabriel.smartclass.adapter.InstitutionLinkRequestsAdapter;
import com.gabriel.smartclass.dao.InstitutionLinkRequestDAO;
import com.gabriel.smartclass.dao.InstitutionUserDAO;
import com.gabriel.smartclass.model.InstitutionLinkRequest;

import com.gabriel.smartclass.model.InstitutionUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InstitutionLinkRequestsFragmentViewModel extends ViewModel {
    private final InstitutionLinkRequestDAO institutionLinkRequestDAO;
    private final InstitutionUserDAO institutionUserDAO;
    public MutableLiveData<String> snackBarText = new MutableLiveData<>();

    public MutableLiveData<String> getSnackBarText() {
        return snackBarText;
    }
    public InstitutionUserDAO getInstitutionUserDAO() {
        return institutionUserDAO;
    }
    public InstitutionLinkRequestsFragmentViewModel(){
        institutionLinkRequestDAO = new InstitutionLinkRequestDAO();
        institutionUserDAO = new InstitutionUserDAO();
    }
    public void approveInstitutionLinkRequest(InstitutionLinkRequest linkRequest){
        InstitutionUser institutionUser = new InstitutionUser();
        institutionUser.setId(linkRequest.getUser().getId());
        institutionUser.setUser_id(linkRequest.getUser());
        institutionUser.setUserType_id(linkRequest.getUserType());
        institutionUser.setActive(true);
        institutionUser.setId(linkRequest.getUser().getId());

        HashMap<String, Object> updateLinkRequest = new HashMap<>();
        updateLinkRequest.put("approved", true);
        institutionLinkRequestDAO.updateInstitutionLinkRequest(linkRequest.getId(), linkRequest.getInstitution_id().getId(), updateLinkRequest, unused -> {

            institutionUserDAO.saveNewInstitutionUser(institutionUser, linkRequest.getInstitution_id(), task ->{
                snackBarText.setValue("Solicitação aprovada! Agora essa pessoa é um membro da sua instituição!");

            }, e->{
                snackBarText.setValue("Ops... Algo deu errado, tente novamente mais tarde: "+e.getMessage());

            });
        }, e -> {
            snackBarText.setValue("Ops... Algo deu errado, tente novamente mais tarde: "+e.getMessage());

        });
    }

    public void rejectInstitutionLinkRequest(InstitutionLinkRequest linkRequest){
        HashMap<String, Object> updateLinkRequest = new HashMap<>();
        updateLinkRequest.put("approved", false);
        institutionLinkRequestDAO.updateInstitutionLinkRequest(linkRequest.getId(), linkRequest.getInstitution_id().getId(), updateLinkRequest, task ->{
            snackBarText.setValue("Solicitação recusada, essa pessoa não faz parte da sua instituição");
        }, e -> {
            snackBarText.setValue("Ops... Algo deu errado, tente novamente mais tarde: "+e.getMessage());
        });


    }


}
