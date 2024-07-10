package com.gabriel.smartclass.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gabriel.smartclass.dao.UserDAO;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.InstitutionUser;
import com.gabriel.smartclass.model.User;

public class CardIDViewModel extends ViewModel {
    private MutableLiveData<Institution> institution = new MutableLiveData<>();
    private MutableLiveData<InstitutionUser> institutionUser = new MutableLiveData<>();
    private MutableLiveData<User> user = new MutableLiveData<>();

    public MutableLiveData<User> getUser() {
        return user;
    }

    public void setUser(MutableLiveData<User> user) {
        this.user = user;
    }

    public MutableLiveData<Institution> getInstitution() {
        return institution;
    }

    public void setInstitution(MutableLiveData<Institution> institution) {
        this.institution = institution;
    }

    public MutableLiveData<InstitutionUser> getInstitutionUser() {
        return institutionUser;
    }

    public void setInstitutionUser(MutableLiveData<InstitutionUser> institutionUser) {
        this.institutionUser = institutionUser;
    }
    public void getUserByID(){
        new UserDAO().getUserByDocumentReference(this.institutionUser.getValue().getUser_id(), task -> {
            this.user.setValue(task.getResult().toObject(User.class));
        }, e ->{

        });
    }
}
