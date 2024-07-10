package com.gabriel.smartclass.view.user.views.institutionUser;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.databinding.ActivityClassroomStudentMainMenuBinding;
import com.gabriel.smartclass.view.course.views.TimeTableView;
import com.gabriel.smartclass.viewModels.ClassroomStudentMainMenuViewModel;

import java.util.Objects;

public class ClassroomStudentMainMenu extends AppCompatActivity {
    private ActivityClassroomStudentMainMenuBinding binding;
    private ClassroomStudentMainMenuViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClassroomStudentMainMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        navigation();
        initialize();
    }

    public void initialize() {
        buildMenu();
        buildViewModel();
        this.studentInitialize();
        this.binding.buttonTimeTable.setOnClickListener(view -> {
            this.openTimeTable();
        });
    }

    public void studentInitialize() {
        this.viewModel.getStudentByID();
        this.viewModel.buildRank();
    }

    public void buildViewModel() {
        this.viewModel = new ViewModelProvider(this).get(ClassroomStudentMainMenuViewModel.class);
        this.viewModel.setInstitutionUser(this.getIntent().getParcelableExtra("user"));
        this.viewModel.setInstitution(this.getIntent().getParcelableExtra("institution"));
        this.viewModel.setClassroom(this.getIntent().getParcelableExtra("classroom"));
        this.viewModel.setCourse(this.getIntent().getParcelableExtra("course"));
    }
    private void buildMenu() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sala de aula");
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void navigation() {
        NavHost navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_classroom);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.bottomactionbarclassroom, navController);
    }

    public void openTimeTable() {
        Intent i = new Intent(this, TimeTableView.class);
        i.putExtra("institution", this.viewModel.getInstitution());
        i.putExtra("course", this.viewModel.getCourse());
        i.putExtra("classroom", this.viewModel.getClassroom());
        i.putExtra("courseMember", false);
        this.startActivity(i);
    }
}