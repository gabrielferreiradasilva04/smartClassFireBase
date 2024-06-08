package com.gabriel.smartclass.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gabriel.smartclass.adapter.SimpleDefaultAdapter;
import com.gabriel.smartclass.model.Course;

import java.util.List;

public class InstitutionUserCoursesViewModel extends ViewModel {
    private SimpleDefaultAdapter<Course>  courseAdapter;
    private MutableLiveData<List<Course>> mutableLiveDataCourse = new MutableLiveData<>();

    public void getUserCourses(){

    }
}
