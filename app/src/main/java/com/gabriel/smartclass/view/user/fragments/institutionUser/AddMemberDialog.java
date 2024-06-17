package com.gabriel.smartclass.view.user.fragments.institutionUser;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gabriel.smartclass.adapter.interfaces.DefaultClickListener;
import com.gabriel.smartclass.databinding.DialogCoordinatorAddteacherBinding;
import com.gabriel.smartclass.model.InstitutionUser;
import com.gabriel.smartclass.viewModels.CoordinatorCourseViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class AddMemberDialog extends DialogFragment {
    DialogCoordinatorAddteacherBinding binding;
    private CoordinatorCourseViewModel viewModel;
    private RecyclerView recyclerView;
    private boolean isStudents = false;

    public boolean isStudents() {
        return isStudents;
    }

    public void setStudents(boolean students) {
        isStudents = students;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogCoordinatorAddteacherBinding.inflate(inflater, container, false);
        if(!this.isStudents){
            initializeTeachers();
        }else{
            initializeStudents();
        }

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.viewModel.getSnackbarText().removeObserver(this.snackbarobserver());
        this.viewModel.getMembersSelectionAdapter().getMutableLiveDataT().setValue(new ArrayList<>());
        this.viewModel.getSnackbarText().setValue(null);
    }

    public void initializeTeachers(){
        loadComponents();
        binding.title.setText("Professores");
        this.viewModel = new ViewModelProvider(requireActivity()).get(CoordinatorCourseViewModel.class);
        this.viewModel.getSnackbarText().observe(getViewLifecycleOwner(), this.snackbarobserver());
        buildRecyclerViewTeachers();
    }
    public void initializeStudents(){
        loadComponents();
        binding.title.setText("Estudantes");
        this.viewModel = new ViewModelProvider(requireActivity()).get(CoordinatorCourseViewModel.class);
        this.viewModel.getSnackbarText().observe(getViewLifecycleOwner(), this.snackbarobserver());
        buildRecyclerViewStudents();
    }

    private Observer<? super String> snackbarobserver() {
        return text ->{
            if(text != null && !text.equals("")){
                Snackbar.make(getContext(), this.recyclerView, text, Snackbar.LENGTH_SHORT).show();
            }
        };
    }

    private DefaultClickListener<InstitutionUser> addMemberListener() {
        return institutionUser -> {
            this.viewModel.addNewMemberOnCourse(institutionUser, this.isStudents);
        };
    }

    public void buildRecyclerViewTeachers(){
        this.viewModel.getAllTeachers();
        this.recyclerView.setHasFixedSize(false);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.recyclerView.setAdapter(this.viewModel.getMembersSelectionAdapter());
        this.viewModel.getMembersSelectionAdapter().setClickListener(this.addMemberListener());
    }
    public void buildRecyclerViewStudents(){
        this.viewModel.getAllStudents();
        this.recyclerView.setHasFixedSize(false);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.recyclerView.setAdapter(this.viewModel.getMembersSelectionAdapter());
        this.viewModel.getMembersSelectionAdapter().setClickListener(this.addMemberListener());
    }

    public void loadComponents(){
        this.recyclerView = binding.dialogAddteacherRecyclerview;
    }
}
