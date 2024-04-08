package com.gabriel.smartclass.viewModels;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gabriel.smartclass.view.InstitutionMainMenu;

public class HostInstitutionActivityViewModel extends ViewModel {
    private MutableLiveData<Integer> notifications = new MutableLiveData<>();
    private InstitutionMainMenu institutionMainMenu;


    public MutableLiveData<Integer> getNotifications() {
        return notifications;
    }

    public void setNotifications(MutableLiveData<Integer> notifications) {
        this.notifications = notifications;
    }

    public HostInstitutionActivityViewModel(InstitutionMainMenu institutionMainMenu){
        this.institutionMainMenu = institutionMainMenu;
        this.notifications.setValue(10);
    }
}
