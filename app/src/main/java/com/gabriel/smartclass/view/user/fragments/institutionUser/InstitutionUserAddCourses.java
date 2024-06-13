package com.gabriel.smartclass.view.user.fragments.institutionUser;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInstitutionUserAddCoursesBinding.inflate(inflater, container, false);
        initialize();
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.viewModel.getSnackbarText().setValue(null);
        this.viewModel.getSnackbarText().removeObserver(this.snackBarObserve());
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
        this.viewModel.getSnackbarText().observe(this.getViewLifecycleOwner(), this.snackBarObserve());
    }

    @NonNull
    private Observer<String> snackBarObserve() {
        return text -> {
            if (text != null && !text.equals("")) {
                Snackbar.make(getContext(), binding.useraddcoursesRecyclerview, text, Snackbar.LENGTH_SHORT).show();
            }
        };
    }
}