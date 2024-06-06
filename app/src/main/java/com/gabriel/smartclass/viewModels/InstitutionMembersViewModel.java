package com.gabriel.smartclass.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gabriel.smartclass.dao.InstitutionUserDAO;
import com.gabriel.smartclass.model.InstitutionUser;

import java.util.List;

public class InstitutionMembersViewModel extends ViewModel {
    private final MutableLiveData<List<InstitutionUser>> institutionUserMutableLiveData = new MutableLiveData<>();
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
        new InstitutionUserDAO().deleteInstitutionUser(institutionID, institutionUser.getId(),task -> {
            snackBarText.setValue("Esse usuário não faz mais parte de sua instituição");
            this.institutionUserMutableLiveData.getValue().remove(institutionUser);
        }, e -> snackBarText.setValue("Ocorreu um erro ao realizar deleção, verifique os relacionamentos desse usuário"));
    }
}
