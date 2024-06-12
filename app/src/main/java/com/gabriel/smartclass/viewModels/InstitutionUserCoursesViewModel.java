package com.gabriel.smartclass.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gabriel.smartclass.adapter.CourseRCAdapter;
import com.gabriel.smartclass.adapter.SimpleDefaultAdapter;
import com.gabriel.smartclass.dao.InstitutionUserDAO;
import com.gabriel.smartclass.model.Course;

public class InstitutionUserCoursesViewModel extends ViewModel {
    private CourseRCAdapter adapter;
    private MutableLiveData<String> snackbarText = new MutableLiveData<>();


    public CourseRCAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(CourseRCAdapter adapter) {
        this.adapter = adapter;
    }

    public MutableLiveData<String> getSnackbarText() {
        return snackbarText;
    }

    public void setSnackbarText(MutableLiveData<String> snackbarText) {
        this.snackbarText = snackbarText;
    }

    public void getUserCourses(String institutionID, String institutionUserID, boolean iscoordinator){
        adapter = new CourseRCAdapter();
        adapter.setShowOnlyCourseView(true);
        new InstitutionUserDAO().getInstitutionUserCourses(institutionID, institutionUserID, iscoordinator, task -> {
            if(task.isComplete() && task.isSuccessful()){
                adapter.getMutableliveDataCourse().setValue(task.getResult().toObjects(Course.class));
                adapter.notifyDataSetChanged();
            }
        }, e->snackbarText.setValue("Ops... Ocorreu um erro ao encontrar seus cursos"));
    }
}
