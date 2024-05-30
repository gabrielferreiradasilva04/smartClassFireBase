package com.gabriel.smartclass.viewModels.factorys;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.gabriel.smartclass.view.fragments.institutionfragments.InstitutionLinkRequestFragment;
import com.gabriel.smartclass.viewModels.InstitutionLinkRequestsFragmentViewModel;

public class InstitutionLinkRequestsViewModelFactory implements ViewModelProvider.Factory{
    private final InstitutionLinkRequestFragment view;

    public InstitutionLinkRequestsViewModelFactory(InstitutionLinkRequestFragment view){
        this.view = view;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(InstitutionLinkRequestsFragmentViewModel.class)){
            return (T) new InstitutionLinkRequestsFragmentViewModel(view);
        }else{
            throw new IllegalArgumentException("View model não reconhecido pela classe!");
        }
    }
}
