package com.gabriel.smartclass.viewModels;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gabriel.smartclass.adapter.CourseRCAdapter;
import com.gabriel.smartclass.dao.CourseDAO;
import com.gabriel.smartclass.model.Course;
import com.gabriel.smartclass.view.course.fragments.SearchAndEditCourses;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SearchAndEditCourseViewModel extends ViewModel {
    private final SearchAndEditCourses view;
    private CourseRCAdapter adapter = new CourseRCAdapter();
    private MutableLiveData<List<Course>> mutableLiveDataCourse = new MutableLiveData<>();
    private MutableLiveData<String> snackBarText = new MutableLiveData<>();

    public SearchAndEditCourses getView() {
        return view;
    }

    public CourseRCAdapter getAdapter() {
        return adapter;
    }

    public MutableLiveData<List<Course>> getMutableLiveDataCourse() {
        return mutableLiveDataCourse;
    }

    public MutableLiveData<String> getSnackBarText() {
        return snackBarText;
    }

    public SearchAndEditCourseViewModel(SearchAndEditCourses view){
        this.view = view;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void searchCoursesAndPopulateAdapter(String institutionID, String courseTitle){
        new CourseDAO().simpleSearchCourse(institutionID, courseTitle, task -> {
            if(task.isComplete() && task.isSuccessful()){
                adapter.getMutableliveDataCourse().setValue(task.getResult().toObjects(Course.class));
                adapter.notifyDataSetChanged();
            }else {
                adapter.getMutableliveDataCourse().setValue(new ArrayList<>());
                adapter.notifyDataSetChanged();
            }
        }, e->{
            snackBarText.setValue("Erro ao realziar pesquisa");
            adapter.getMutableliveDataCourse().setValue(new ArrayList<>());
            adapter.notifyDataSetChanged();
        });
    }
}
