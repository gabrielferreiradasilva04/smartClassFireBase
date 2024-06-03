package com.gabriel.smartclass.viewModels.factorys;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.gabriel.smartclass.view.user.fragments.institutionUser.InstitutionUserProfile;
import com.gabriel.smartclass.viewModels.InstitutionUserProfileViewModel;

public class InstitutionUserProfileViewModelFactory implements ViewModelProvider.Factory {
    private final InstitutionUserProfile view;
    public InstitutionUserProfileViewModelFactory(InstitutionUserProfile view){
        this.view = view;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(InstitutionUserProfileViewModel.class)){
            return (T) new InstitutionUserProfileViewModel(view);
        }else{
            throw new IllegalArgumentException("View model não reconhecido pela classe!");
        }
    }
}
