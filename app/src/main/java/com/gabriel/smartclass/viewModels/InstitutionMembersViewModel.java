package com.gabriel.smartclass.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gabriel.smartclass.dao.InstitutionUserDAO;
import com.gabriel.smartclass.model.InstitutionUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class InstitutionMembersViewModel extends ViewModel {
    private final MutableLiveData<List<InstitutionUser>> institutionUserMutableLiveData = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<String> snackBarText = new MutableLiveData<>();

    public MutableLiveData<List<InstitutionUser>> getInstitutionUserMutableLiveData() {
        return institutionUserMutableLiveData;
    }

    public MutableLiveData<String> getSnackBarText() {
        return snackBarText;
    }

    public void getAllInstitutionUsers(String institutionID){
        new InstitutionUserDAO().getAllInstitutionUsers(institutionID, task -> {
            if(task.isComplete() && task.isSuccessful()){
                this.institutionUserMutableLiveData.setValue(task.getResult().toObjects(InstitutionUser.class));
            }
        }, e-> snackBarText.setValue("Erro ao realizar busca de membros"));
    }
    public void removeMember(String institutionID, InstitutionUser institutionUser){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("active", false);
        new InstitutionUserDAO().updateInstitutionUser(institutionID, institutionUser.getId(), hashMap, task -> {
            if(task.isComplete() && task.isSuccessful()){
               this.institutionUserMutableLiveData.getValue().removeIf(object -> object.getId().equals(institutionUser.getId()));
               this.institutionUserMutableLiveData.setValue(this.institutionUserMutableLiveData.getValue());
                snackBarText.setValue("Esse usuário não é mais um membro da sua instituição");
            }
        }, e -> snackBarText.setValue("Erro ao deletar usuário"));
    }
}
