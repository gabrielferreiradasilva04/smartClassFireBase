package com.gabriel.smartclass.view.user.views.institutionUser;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.databinding.ActivityCreateClassroomBinding;
import com.gabriel.smartclass.view.user.fragments.institutionUser.AddSubjectOnClassDialog;
import com.gabriel.smartclass.view.user.fragments.institutionUser.DialogAddMembersOnClass;
import com.gabriel.smartclass.viewModels.CreateClassroomViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Objects;
@SuppressLint("NotifyDataSetChanged")
public class CreateClassroom extends AppCompatActivity {
    private ActivityCreateClassroomBinding binding;
    private CreateClassroomViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateClassroomBinding.inflate(getLayoutInflater());
        this.initialize();
        setContentView(binding.getRoot());
    }
    public void initialize(){
        this.buildMenu();
        this.buildViewModel();
        this.buttonListeners();
        this.viewModel.getSnackbarText().observe(this, this.snackbarObserver());
        this.buildRCs();
    }
    public void buildRCs(){
        this.buildRCStudents();
        this.buildRCSubjects();
        this.buildRCTeachers();
    }

    public void buildRCStudents(){
        binding.rcStudents.setHasFixedSize(true);
        binding.rcStudents.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.rcStudents.setAdapter(this.viewModel.getStudentsOnClass());
    }
    public void buildRCTeachers(){
        binding.recyclerviewteachers.setHasFixedSize(true);
        binding.recyclerviewteachers.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.recyclerviewteachers.setAdapter(this.viewModel.getTeachersOnClass());
    }
    public void buildRCSubjects(){
        binding.recyclerViewCourseSubjects.setHasFixedSize(true);
        binding.recyclerViewCourseSubjects.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.recyclerViewCourseSubjects.setAdapter(this.viewModel.getSubjectsAdapter());
    }
    public void buildViewModel(){
        this.viewModel = new ViewModelProvider(this).get(CreateClassroomViewModel.class);
        this.viewModel.setInstitution(getIntent().getParcelableExtra("institution"));
        this.viewModel.setCourse(getIntent().getParcelableExtra("course"));
    }
    public void buttonListeners(){
        binding.buttonstudents.setOnClickListener(view ->{
            this.inflateMemberDialog(true);
        });
        binding.buttonteachers.setOnClickListener(view ->{
            this.inflateMemberDialog(false);
        });
        binding.buttonsubjects.setOnClickListener(view ->{
            this.inflateSubjectDialog();
        });
        binding.buttoncreate.setOnClickListener(view ->{
            this.createClassroom();
        });

    }

    private void createClassroom() {
        String description = binding.edDescription.getText().toString();
        String period = binding.edPeriod.getText().toString();
        if (!period.equals("") && !description.equals("")){
            int periodInt = Integer.parseInt(period);
            this.viewModel.createClassroom(description, periodInt);
            this.clearAllComponents();
        }else{
            Snackbar.make(binding.buttoncreate, "Preencha todos os ccampos", Snackbar.LENGTH_SHORT).show();
        }
    }


    private void clearAllComponents() {
        this.viewModel.getStudentsOnClass().getMutableLiveDataT().setValue(new ArrayList<>());
        this.viewModel.getTeachersOnClass().getMutableLiveDataT().setValue(new ArrayList<>());
        this.viewModel.getSubjectsAdapter().getClassroomSubjects().setValue(new ArrayList<>());
        this.viewModel.getStudentsOnClass().notifyDataSetChanged();
        this.viewModel.getTeachersOnClass().notifyDataSetChanged();
        this.viewModel.getSubjectsAdapter().notifyDataSetChanged();
        this.binding.edDescription.setText("");
        this.binding.edPeriod.setText("");
    }

    private void inflateSubjectDialog() {
        this.viewModel.getSnackbarText().setValue("");
        AddSubjectOnClassDialog dialog = new AddSubjectOnClassDialog();
        dialog.show(getSupportFragmentManager(), dialog.getTag());
    }

    private Observer<? super String> snackbarObserver() {
        return text ->{
            if(text != null && !text.equals("")){
                Snackbar.make(binding.getRoot(), text, Snackbar.LENGTH_SHORT).show();
            }
        };
    }

    private void inflateMemberDialog(boolean isStudent) {
        this.viewModel.getSnackbarText().setValue("");
        DialogAddMembersOnClass dialog = new DialogAddMembersOnClass();
        dialog.setStudent(isStudent);
        dialog.show(this.getSupportFragmentManager(), dialog.getTag());
    }

    public void updateTitle(String title){
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
        buildMenu();
    }
    private void buildMenu() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().setTitle("Criar Classe");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_action_bar, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}