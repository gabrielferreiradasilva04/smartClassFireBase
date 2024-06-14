package com.gabriel.smartclass.view.user.fragments.institutionUser;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gabriel.smartclass.databinding.DialogCoordinatorAddteacherBinding;
import com.gabriel.smartclass.viewModels.CoordinatorCourseViewModel;

public class CoordinatorAddTeacherDialog extends DialogFragment {
    DialogCoordinatorAddteacherBinding binding;
    private CoordinatorCourseViewModel viewModel;
    private RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogCoordinatorAddteacherBinding.inflate(inflater, container, false);
        initialize();
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }
    public void initialize(){
        loadComponents();
        this.viewModel = new ViewModelProvider(requireActivity()).get(CoordinatorCourseViewModel.class);
        buildRecyclerView();
    }
    public void buildRecyclerView(){
        this.viewModel.getAllTeachers(this.viewModel.getInstitution().getId());
        this.recyclerView.setHasFixedSize(false);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.recyclerView.setAdapter(this.viewModel.getMembersSelectionAdapter());
    }
    public void loadComponents(){
        this.recyclerView = binding.dialogAddteacherRecyclerview;
    }
}