package com.gabriel.smartclass.view.user.fragments.institutionUser;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gabriel.smartclass.databinding.EmptyRequestBinding;
import com.gabriel.smartclass.databinding.FragmentCoordinatorCourseClassesBinding;
import com.gabriel.smartclass.observer.EmptyRecyclerViewObserver;
import com.gabriel.smartclass.view.user.views.institutionUser.CoordinatorCourseMainMenu;
import com.gabriel.smartclass.view.user.views.institutionUser.CreateClassroom;
import com.gabriel.smartclass.viewModels.CoordinatorCourseViewModel;
import com.google.android.material.snackbar.Snackbar;

public class CoordinatorCourseClasses extends Fragment {
    private FragmentCoordinatorCourseClassesBinding binding;
    private CoordinatorCourseViewModel viewModel;

    public CoordinatorCourseClasses() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentCoordinatorCourseClassesBinding.inflate(inflater, container, false);
        this.initialize();
        return this.binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        this.emptyRequestView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.viewModel.getSnackbarText().setValue(null);
        this.viewModel.getSnackbarText().removeObserver(this.snackbarObserver());
    }

    public void initialize(){
        this.buildMenu();
        this.buttonListeners();
        this.getViewModel();
        this.viewModel.getClassrooms();
        this.buildRecyclerView();
        this.viewModel.getSnackbarText().observe(getViewLifecycleOwner(), snackbarObserver());
    }

    private Observer<? super String> snackbarObserver() {
        return text ->{
          if(text!=null && !text.equals("")){
              Snackbar.make(getContext(), binding.recyclerviewclasses, text, Snackbar.LENGTH_SHORT).show();
          }
        };
    }

    private void buildRecyclerView() {
        binding.recyclerviewclasses.setHasFixedSize(false);
        binding.recyclerviewclasses.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerviewclasses.setAdapter(viewModel.getClassroomAdapter());
    }

    private void buildMenu() {
        CoordinatorCourseMainMenu main = (CoordinatorCourseMainMenu) this.getActivity();
        main.updateTitle("Classes");
    }
    private void buttonListeners(){
        binding.buttonadd.setOnClickListener(view ->{
            Intent i = new Intent(this.getContext(), CreateClassroom.class);
            startActivity(i);
            getActivity().finish();
        });
    }
    private void getViewModel(){
        this.viewModel = new ViewModelProvider(requireActivity()).get(CoordinatorCourseViewModel.class);

    }
    @SuppressLint("NotifyDataSetChanged")
    private void emptyRequestView() {
        EmptyRequestBinding viewEmpty = binding.emptyContainerClasses;
        EmptyRecyclerViewObserver observer = new EmptyRecyclerViewObserver(binding.recyclerviewclasses, viewEmpty.getRoot());
        viewModel.getClassroomAdapter().registerAdapterDataObserver(observer);
        viewModel.getClassroomAdapter().notifyDataSetChanged();
    }

}