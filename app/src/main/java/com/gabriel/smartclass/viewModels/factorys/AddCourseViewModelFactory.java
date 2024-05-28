package com.gabriel.smartclass.viewModels.factorys;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.gabriel.smartclass.view.course.fragments.AddCourse;
import com.gabriel.smartclass.viewModels.AddCourseViewModel;

public class AddCourseViewModelFactory implements ViewModelProvider.Factory{
    private final AddCourse view;

    public AddCourseViewModelFactory(AddCourse view){
        this.view = view;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(AddCourseViewModel.class)){
            return (T) new AddCourseViewModel(view);
        }else{
            throw new IllegalArgumentException("View model não reconhecido pela classe!");
        }
    }
}
