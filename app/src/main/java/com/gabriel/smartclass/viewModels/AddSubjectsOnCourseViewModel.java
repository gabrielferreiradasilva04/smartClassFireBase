package com.gabriel.smartclass.viewModels;
import androidx.lifecycle.ViewModel;

import com.gabriel.smartclass.model.Course;
import com.gabriel.smartclass.view.course.views.AddSubjectsOnCourse;

public class AddSubjectsOnCourseViewModel extends ViewModel {
    private final AddSubjectsOnCourse view;

    public AddSubjectsOnCourseViewModel(AddSubjectsOnCourse view){
        this.view = view;
    }
}
