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

import com.gabriel.smartclass.databinding.DialogAddmembersOnclassBinding;
import com.gabriel.smartclass.viewModels.CreateClassroomViewModel;
import com.google.android.material.snackbar.Snackbar;

public class DialogAddMembersOnClass extends DialogFragment {
    private DialogAddmembersOnclassBinding binding;
    private CreateClassroomViewModel viewModel;
    private boolean isStudent = false;

    public void setStudent(boolean student) {
        isStudent = student;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = DialogAddmembersOnclassBinding.inflate(inflater, container, false);
        binding.button.setOnClickListener(view->{
            this.dismiss();
        });
        if(this.isStudent){
            this.studentInitialize();
        }else{
            this.teacherInitialize();
        }
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.viewModel.getSnackbarText().setValue("");
        this.viewModel.getSnackbarText().removeObserver(this.snackbarObserve());
    }

    private Observer<? super String> snackbarObserve() {
        return text ->{
            if(text!=null && !text.equals("")){
                Snackbar.make(getContext(), binding.recyclerStudents, text, Snackbar.LENGTH_SHORT).show();
            }
        };
    }

    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void studentInitialize(){
        this.buildViewModel();
        this.buildStudentRecyclerView();
        this.viewModel.getSnackbarText().observe(getViewLifecycleOwner(),this.snackbarObserve());
    }
    public void teacherInitialize(){
        this.buildViewModel();
        this.buildTeacherRecyclerView();
        this.viewModel.getSnackbarText().observe(getViewLifecycleOwner(),this.snackbarObserve());
    }

    private void buildViewModel() {
        this.viewModel = new ViewModelProvider(requireActivity()).get(CreateClassroomViewModel.class);
    }

    private void buildStudentRecyclerView() {
        this.viewModel.loadMemberSelectionAdapter(this.isStudent);
        this.binding.recyclerStudents.setHasFixedSize(false);
        this.binding.recyclerStudents.setLayoutManager(new LinearLayoutManager(this.getContext()));
        this.binding.recyclerStudents.setAdapter(this.viewModel.getStudentsToAdd());
        this.viewModel.getStudentsToAdd().setClickListener(student -> {
            this.viewModel.addStudentOnClassroom(student);
        });
    }
    private void buildTeacherRecyclerView() {
        this.viewModel.loadMemberSelectionAdapter(this.isStudent);
        this.binding.recyclerStudents.setHasFixedSize(false);
        this.binding.recyclerStudents.setLayoutManager(new LinearLayoutManager(this.getContext()));
        this.binding.recyclerStudents.setAdapter(this.viewModel.getTeachersToadd());
        this.viewModel.getTeachersToadd().setClickListener(teacher -> {
            this.viewModel.addTeacherOnClassroom(teacher);
        });
    }
}
