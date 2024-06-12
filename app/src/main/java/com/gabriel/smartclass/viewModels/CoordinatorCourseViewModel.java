package com.gabriel.smartclass.viewModels;

import androidx.lifecycle.ViewModel;

import com.gabriel.smartclass.model.Course;
import com.gabriel.smartclass.model.InstitutionUser;

public class CoordinatorCourseMainMenuViewModel extends ViewModel {
    private InstitutionUser coordinator;
    private Course course;

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public InstitutionUser getCoordinator() {
        return coordinator;
    }

    public void setCoordinator(InstitutionUser coordinator) {
        this.coordinator = coordinator;
    }
}
