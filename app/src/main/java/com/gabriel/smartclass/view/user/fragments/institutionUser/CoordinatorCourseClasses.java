package com.gabriel.smartclass.view.user.fragments.institutionUser;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.databinding.FragmentCoordinatorCourseClassesBinding;
import com.gabriel.smartclass.view.user.views.institutionUser.CoordinatorCourseMainMenu;

public class CoordinatorCourseClasses extends Fragment {
    private FragmentCoordinatorCourseClassesBinding binding;

    public CoordinatorCourseClasses() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentCoordinatorCourseClassesBinding.inflate(inflater, container, false);

        return this.binding.getRoot();
    }
    public void initialize(){
        this.buildMenu();
    }

    private void buildMenu() {
        CoordinatorCourseMainMenu main = (CoordinatorCourseMainMenu) this.getActivity();
        main.updateTitle("Classes");
    }
}