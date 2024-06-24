package com.gabriel.smartclass.view.user.fragments.institutionUser;

import android.database.DataSetObserver;
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

import com.gabriel.smartclass.databinding.DialogAddsubjectonclassBinding;
import com.gabriel.smartclass.model.Subject;
import com.gabriel.smartclass.model.Teacher;
import com.gabriel.smartclass.viewModels.CreateClassroomViewModel;
import com.google.android.material.snackbar.Snackbar;

public class AddSubjectOnClassDialog extends DialogFragment {
    private CreateClassroomViewModel viewModel;
    private DialogAddsubjectonclassBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = DialogAddsubjectonclassBinding.inflate(inflater, container, false);
        this.initialize();
        return binding.getRoot();
    }
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }
    public void initialize(){
        buildViewModel();
        viewModel.getCourseSubjects(this.getContext());
        viewModel.getCourseTeachers(this.getContext());
        loadSpinners();
        binding.buttoncreateSubject.setOnClickListener(view ->{
            this.createSubject();
        });
    }

    private void createSubject() {
        Teacher teacher = (Teacher) binding.spinnerTeacher.getSelectedItem();
        Subject subject = (Subject) binding.spinnerSubject.getSelectedItem();
        viewModel.createClassroomSubject(subject, teacher);
    }


    public void buildViewModel(){
        this.viewModel = new ViewModelProvider(requireActivity()).get(CreateClassroomViewModel.class);
        this.viewModel.getSnackbarText().observe(getViewLifecycleOwner(), this.snackbarObserver());
    }

    private Observer<? super String> snackbarObserver() {
        return text ->{
            if(text != null && !text.equals("")){
                Snackbar.make(binding.buttoncreateSubject, text, Snackbar.LENGTH_SHORT).show();
            }
        };
    }

    public void loadSpinners(){
        this.viewModel.getSubjectSpinnerAdapter().registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                binding.spinnerSubject.setAdapter(viewModel.getSubjectSpinnerAdapter());
            }
        });
        this.viewModel.getTeachersSpinnerAdapter().registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                binding.spinnerTeacher.setAdapter(viewModel.getTeachersSpinnerAdapter());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
