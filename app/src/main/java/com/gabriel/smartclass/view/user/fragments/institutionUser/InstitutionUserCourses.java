package com.gabriel.smartclass.view.user.fragments.institutionUser;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.databinding.FragmentInstitutionUserCoursesBinding;

public class InstitutionUserCourses extends Fragment {
    private FragmentInstitutionUserCoursesBinding binding;
    private RecyclerView recyclerView;
    private EditText courseTitle;
    private ImageButton findCourses;

    public InstitutionUserCourses() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInstitutionUserCoursesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}