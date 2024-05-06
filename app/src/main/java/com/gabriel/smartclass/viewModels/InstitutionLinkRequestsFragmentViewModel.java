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
import java.util.List;

public class InstitutionLinkRequestsFragmentViewModel extends ViewModel {
    private final InstitutionLinkRequestDAO institutionLinkRequestDAO;
    private final InstitutionUserDAO institutionUserDAO;
    private InstitutionLinkRequestsAdapter institutionLinkRequestsAdapter;
    public MutableLiveData<String> snackBarText = new MutableLiveData<>();

    public InstitutionLinkRequestsAdapter getInstitutionLinkRequestsAdapter() {
        return institutionLinkRequestsAdapter;
    }
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
    @SuppressLint("NotifyDataSetChanged")
    public void loadInstitutionLinkRequests(){
        List<InstitutionLinkRequest> institutionLinkRequests = new ArrayList<>();
        institutionLinkRequestsAdapter= new InstitutionLinkRequestsAdapter(institutionLinkRequests);
        institutionLinkRequestDAO.getAllLinkRequestsByInstitution(FirebaseAuth.getInstance().getCurrentUser().getUid(), task ->{
            if(task.isComplete() && task.isSuccessful()){
                for(QueryDocumentSnapshot snapshots : task.getResult()){
                    InstitutionLinkRequest linkRequest = snapshots.toObject(InstitutionLinkRequest.class);
                    institutionLinkRequestsAdapter.addItem(linkRequest);
                    institutionLinkRequestsAdapter.notifyDataSetChanged();
                }
            }else institutionLinkRequestsAdapter.notifyDataSetChanged();
        }, e->{
            snackBarText.setValue("Ocorreu um erro ao buscar as solicitações de vinculo: "+ e.getMessage());
        });
    }
    public void approveInstitutionLinkRequest(InstitutionLinkRequest linkRequest){
        InstitutionUser institutionUser = new InstitutionUser();
        institutionUser.setUser_id(linkRequest.getUser());
        institutionUser.setUserType_id(linkRequest.getUserType());
        institutionUser.setActive(true);
        institutionUser.setId(linkRequest.getUser().getId());
        institutionUserDAO.saveNewInstitutionUser(institutionUser, linkRequest.getInstitution_id(), task ->{
            snackBarText.setValue("Solicitação aprovada! Agora essa pessoa é um membro da sua instituição!");
        }, e->{
            snackBarText.setValue("Ops... Algo deu errado, tente novamente mais tarde: "+e.getMessage());

        });

    }


}
