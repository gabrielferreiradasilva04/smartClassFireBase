package com.gabriel.smartclass.viewModels.factorys;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.gabriel.smartclass.view.course.fragments.SearchAndEditCourses;
import com.gabriel.smartclass.viewModels.SearchAndEditCourseViewModel;

public class SearchAndEditCourseFactory implements ViewModelProvider.Factory {
    private final SearchAndEditCourses view;

    public SearchAndEditCourseFactory(SearchAndEditCourses view){
        this.view = view;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(SearchAndEditCourseViewModel.class)){
            return (T) new SearchAndEditCourseViewModel(view);

        }else{
            throw new IllegalArgumentException("View model nao reconhecido pela classe!");
        }
    }
}
