package com.gabriel.smartclass.view.user.fragments.institutionUser;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.databinding.FragmentTeacherGradeManagementBinding;
import com.gabriel.smartclass.model.Frequency;
import com.gabriel.smartclass.model.Student;
import com.gabriel.smartclass.model.StudentGrade;
import com.gabriel.smartclass.viewModels.ClassroomTeacherManagementViewModel;

public class TeacherGradeManagement extends Fragment {
    private FragmentTeacherGradeManagementBinding binding;
    private ClassroomTeacherManagementViewModel viewModel;

    public TeacherGradeManagement() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentTeacherGradeManagementBinding.inflate(inflater, container, false);
        this.buildViewModel();
        this.initialize();
        return this.binding.getRoot();
    }

    public void initialize() {
        this.binding.buttonSaveGrade.setOnClickListener(this.saveGrade());
        this.binding.calculatebutton.setOnClickListener(this.calculate());
        this.binding.buttonLockGrades.setOnClickListener(this.lockGradesListener());
        this.viewModel.getStudent().observe(this.getViewLifecycleOwner(), this.studentObserver());
        this.loadStudentDetails();
    }

    private View.OnClickListener lockGradesListener() {
        return view ->{
            this.lockGrades();
        };
    }

    private void lockGrades() {
        if(this.viewModel.getStudent().getValue() != null){
            StudentGrade grade = null;
            Frequency frequency = null;
            if(this.viewModel.getStudent().getValue().getStudentGrades() != null && !this.viewModel.getStudent().getValue().getStudentGrades().isEmpty()){
                for (StudentGrade sGrade: this.viewModel.getStudent().getValue().getStudentGrades()) {
                    if(sGrade.getSubject().getId().equals(this.viewModel.getSubject().getId())){
                        grade = sGrade;
                    }
                }
            }
            if(this.viewModel.getStudent().getValue().getFrequencies() != null && !this.viewModel.getStudent().getValue().getFrequencies().isEmpty()){
                for (Frequency sFrequency : this.viewModel.getStudent().getValue().getFrequencies()){
                    if(sFrequency.getSubject().getId().equals(this.viewModel.getSubject().getId())){
                        frequency = sFrequency;
                    }
                }
            }
            if(grade != null && frequency != null){
                if(grade.getFinalGrade() >= this.viewModel.getSubject().getMinimum_grade() && frequency.getPercent() >= 75){
                    this.showResultDialog(true);
                    this.viewModel.lockPeriod(true);
                }else{
                    showResultDialog(false);
                    this.viewModel.lockPeriod(false);
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private Observer<? super Student> studentObserver() {
        return student -> {
            if (student != null) {
                if (student.getStudentGrades() != null && !student.getStudentGrades().isEmpty()) {
                    for (StudentGrade grade : student.getStudentGrades()) {
                        if (grade.getSubject().getId().equals(this.viewModel.getSubject().getId())) {
                            loadExistentGrade(grade);
                        }
                    }
                }
            }
        };
    }
    @SuppressLint("SetTextI18n")
    public void loadStudentDetails(){
        String userDescription = this.viewModel.getCourseMember().getDescription();
        String subjectDescription = this.viewModel.getSubject().getDescription();
        binding.textSubjectName.setText(subjectDescription);
        binding.textUsername.setText(userDescription);
        binding.textMinGrade.setText("Nota mínima: "+Double.toString(this.viewModel.getSubject().getMinimum_grade()));

    }

    private void loadExistentGrade(StudentGrade grade) {
        binding.textFinalGrade.setText(Double.toString(grade.getFinalGrade()));
        binding.textFirstGrade.setText(Double.toString(grade.getFirstGrade()));
        binding.textLastGrade.setText(Double.toString(grade.getLastGrade()));
    }

    @SuppressLint("SetTextI18n")
        private View.OnClickListener calculate () {
            return view -> calc();
        }

        private void calc () {
            double firstGrade = 0;
            double lastGrade = 0;
            double finalGrade = 0;
            if (!this.binding.textFirstGrade.getText().toString().equals("")) {
                firstGrade = Double.parseDouble(this.binding.textFirstGrade.getText().toString());
            }
            if (!this.binding.textLastGrade.getText().toString().equals("")) {
                lastGrade = Double.parseDouble(this.binding.textLastGrade.getText().toString());

            }
            if (!this.binding.textFinalGrade.getText().toString().equals("")) {
                finalGrade = Double.parseDouble(this.binding.textFinalGrade.getText().toString());
            }
            finalGrade = (firstGrade + lastGrade) / 2;
            this.binding.textFinalGrade.setText(Double.toString(finalGrade));
        }

        private View.OnClickListener saveGrade () {
            return view -> save();
        }

        private void save () {
            double firstGrade = 0;
            double lastGrade = 0;
            double finalGrade = 0;
            if (!this.binding.textFirstGrade.getText().toString().equals("")) {
                firstGrade = Double.parseDouble(this.binding.textFirstGrade.getText().toString());
            }
            if (!this.binding.textLastGrade.getText().toString().equals("")) {
                lastGrade = Double.parseDouble(this.binding.textLastGrade.getText().toString());

            }
            finalGrade = (firstGrade + lastGrade ) / 2;
            this.viewModel.updateStudentGrade(firstGrade, lastGrade, finalGrade);
        }

        public void buildViewModel () {
            this.viewModel = new ViewModelProvider(this.requireActivity()).get(ClassroomTeacherManagementViewModel.class);
        }
        public void showResultDialog(boolean positive){
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.getContext());
            dialogBuilder.setCancelable(true);
            final View popUp = getLayoutInflater().inflate(R.layout.dialog_student_result,null);
            dialogBuilder.setView(popUp);
            Dialog dialog = dialogBuilder.create();
            dialog.show();
            TextView description = dialog.findViewById(R.id.textResult);
            ImageView image = dialog.findViewById(R.id.imageresult);
            if(positive){
                description.setText("O Aluno(a) "+ this.viewModel.getStudent().getValue().getDescription()+" foi APROVADO na sua Matéria!");
                image.setImageResource(R.drawable.baseline_sentiment_very_satisfied_24);
            }else{
                description.setText("O Aluno(a)"+ this.viewModel.getStudent().getValue().getDescription()+" foi REPROVADO na sua Matéria!");
                image.setImageResource(R.drawable.ic_bad);
            }
        }
    }