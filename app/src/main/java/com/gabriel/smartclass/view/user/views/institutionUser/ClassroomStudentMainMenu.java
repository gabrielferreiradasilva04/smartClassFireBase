package com.gabriel.smartclass.view.user.views.institutionUser;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.databinding.ActivityClassroomStudentMainMenuBinding;
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
    public void initialize(){
        buildMenu();
        buildViewModel();
        this.studentInitialize();
    }

    public void studentInitialize(){
        this.viewModel.getStudentByID();
    }

    private void buildMenu() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sala de aula");
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
    public void buildViewModel(){
        this.viewModel = new ViewModelProvider(this).get(ClassroomStudentMainMenuViewModel.class);
        this.viewModel.setInstitutionUser(this.getIntent().getParcelableExtra("user"));
        this.viewModel.setInstitution(this.getIntent().getParcelableExtra("institution"));
        this.viewModel.setClassroom(this.getIntent().getParcelableExtra("classroom"));
        this.viewModel.setCourse(this.getIntent().getParcelableExtra("course"));
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private void navigation(){
        NavHost navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_classroom);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.bottomactionbarclassroom, navController);
    }
}