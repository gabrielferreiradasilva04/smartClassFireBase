package com.gabriel.smartclass.view.user.fragments.institutionUser;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.gabriel.smartclass.databinding.FragmentTeacherFrequencyMenagementBinding;
import com.gabriel.smartclass.model.Frequency;
import com.gabriel.smartclass.model.Student;
import com.gabriel.smartclass.viewModels.ClassroomTeacherManagementViewModel;

public class TeacherFrequencyManagement extends Fragment {
    private FragmentTeacherFrequencyMenagementBinding binding;
    private ClassroomTeacherManagementViewModel viewModel;

    public TeacherFrequencyManagement(){

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentTeacherFrequencyMenagementBinding.inflate(inflater, container, false);
        initialize();
        return this.binding.getRoot();
    }
    public void initialize(){
        this.buildViewModel();
        this.binding.buttonsave.setOnClickListener(this.buttonSaveClickListener());
    }

    private View.OnClickListener buttonSaveClickListener() {
        return view -> this.saveFrequency();
    }

    private void saveFrequency() {
        double frequency = 100;
        if(!this.binding.edtxtFrquencyPercent.getText().toString().equals("")){
            frequency = Double.parseDouble(this.binding.edtxtFrquencyPercent.getText().toString());
        }
        this.viewModel.updateStudentFrequency(frequency);
    }

    public void buildViewModel(){
        this.viewModel = new ViewModelProvider(this.requireActivity()).get(ClassroomTeacherManagementViewModel.class);
        this.viewModel.getStudent().observe(this.getViewLifecycleOwner(), this.studentObserver());
    }

    private Observer<? super Student> studentObserver() {
        return student -> {
            if(student != null){
                loadDetails(student);
            }
        };

    }

    private void loadDetails(Student student) {
        binding.textStudentName.setText(student.getDescription());
        binding.textSubjectTitle.setText(this.viewModel.getSubject().getDescription());
        if(student.getFrequencies() != null){
            loadExistentFrequency(student);
        }
    }

    @SuppressLint("SetTextI18n")
    private void loadExistentFrequency(Student student) {
        for (Frequency frequency : student.getFrequencies()) {
            if(frequency.getSubject().getId().equals(this.viewModel.getSubject().getId())){
                binding.edtxtFrquencyPercent.setText(Double.toString(frequency.getPercent()));
            }
        }
    }
}