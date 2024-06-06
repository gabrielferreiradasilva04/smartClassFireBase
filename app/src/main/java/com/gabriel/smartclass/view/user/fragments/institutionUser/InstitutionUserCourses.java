package com.gabriel.smartclass.view.user.fragments.institutionUser;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.gabriel.smartclass.databinding.FragmentInstitutionUserCoursesBinding;
import com.gabriel.smartclass.model.InstitutionUser;
import com.gabriel.smartclass.view.user.views.commonUser.CommonUserMainMenu;
import com.gabriel.smartclass.view.user.views.institutionUser.InstitutionUserMainMenu;

public class InstitutionUserCourses extends Fragment {
    private FragmentInstitutionUserCoursesBinding binding;
    private RecyclerView recyclerView;
    private EditText courseTitle;
    private Button findCourses;

    public InstitutionUserCourses() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInstitutionUserCoursesBinding.inflate(inflater, container, false);
        initialize();
        return binding.getRoot();
    }
    public void initialize(){
        buildMenu();
    }
    public void buildMenu(){
        InstitutionUserMainMenu main = (InstitutionUserMainMenu) getActivity();
        if (main != null) {
            main.updateTitle("Meus Cursos");
        }

    }
    public void buildRecyclerView(){

    }

    public void loadComponents(){
        this.recyclerView = binding.institutionuserCoursesRecyclerview;
        this.courseTitle = binding.institutionuserCoursesEdtxtTitle;
        this.findCourses = binding.institutionuserCoursesFindbutton;
    }
}