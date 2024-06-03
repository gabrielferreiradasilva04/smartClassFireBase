package com.gabriel.smartclass.viewModels.factorys;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.gabriel.smartclass.view.course.views.AddSubjectsOnCourse;
import com.gabriel.smartclass.viewModels.AddSubjectsOnCourseViewModel;

public class AddSubjectsOnCourseViewModelFactory implements ViewModelProvider.Factory {
    private final AddSubjectsOnCourse view;
    public AddSubjectsOnCourseViewModelFactory(AddSubjectsOnCourse view){
        this.view = view;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(AddSubjectsOnCourseViewModel.class)){
            return (T) new AddSubjectsOnCourseViewModel(view);
        }else{
            throw new IllegalArgumentException("View model não reconhecido pela classe!");
        }
    }
}
