package com.gabriel.smartclass.view.user.fragments.institutionUser;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;


import com.gabriel.smartclass.databinding.EmptyRequestBinding;
import com.gabriel.smartclass.databinding.FragmentInstitutionUserAddCoursesBinding;
import com.gabriel.smartclass.observer.EmptyRecyclerViewObserver;
import com.gabriel.smartclass.viewModels.InstitutionUserMainMenuViewModel;
import com.google.android.material.snackbar.Snackbar;

public class InstitutionUserAddCourses extends Fragment {
    private FragmentInstitutionUserAddCoursesBinding binding;
    private InstitutionUserMainMenuViewModel viewModel;
    private ImageButton searchButton;
    private EditText title;
    private RecyclerView recyclerView;

    public InstitutionUserAddCourses() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInstitutionUserAddCoursesBinding.inflate(inflater, container, false);
        initialize();
        return binding.getRoot();
    }

    public void initialize() {
        buildViewModel();
        loadComponents();
        componentsListener();
        buildRecyclerView();
    }

    public void buildRecyclerView() {
        EmptyRequestBinding viewEmpty = binding.emptyContainerUseraddcourses;
        EmptyRecyclerViewObserver observer = new EmptyRecyclerViewObserver(binding.useraddcoursesRecyclerview, viewEmpty.getRoot());
        viewModel.getSearchCoursesAdapter().registerAdapterDataObserver(observer);
        this.recyclerView.setHasFixedSize(false);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.recyclerView.setAdapter(this.viewModel.getSearchCoursesAdapter());
    }

    public void loadComponents() {
        this.searchButton = binding.useraddcoursesSearchbutton;
        this.title = binding.useraddcoursesTitle;
        this.recyclerView = binding.useraddcoursesRecyclerview;
    }

    public void buildViewModel() {
        this.viewModel = new ViewModelProvider(requireActivity()).get(InstitutionUserMainMenuViewModel.class);
    }

    public void componentsListener() {
        this.searchButton.setOnClickListener(view -> {
            this.viewModel.searchCourses(viewModel.getCurrentInstitution().getId(), this.title.getText().toString());
        });
        this.viewModel.getSnackbarText().observe(getViewLifecycleOwner(), text -> {
            if (text != null && !text.equals("")) {
                Snackbar.make(getContext(), binding.useraddcoursesRecyclerview, text, Snackbar.LENGTH_SHORT).show();
            }
        });
    }
    private void emptyview() {
        EmptyRequestBinding viewEmpty = binding.emptyContainerUseraddcourses;
        EmptyRecyclerViewObserver observer = new EmptyRecyclerViewObserver(binding.useraddcoursesRecyclerview, viewEmpty.getRoot());
        viewModel.getSearchCoursesAdapter().registerAdapterDataObserver(observer);
    }
}