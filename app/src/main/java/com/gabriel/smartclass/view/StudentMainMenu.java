package com.gabriel.smartclass.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.databinding.ActivityStudentMainMenuBinding;
import com.gabriel.smartclass.viewModels.HostStudentActivityViewModel;

public class StudentMainMenu extends AppCompatActivity {
    private ActivityStudentMainMenuBinding binding;
    private NavHost navHostFragment;
    private NavController navController;
    private HostStudentActivityViewModel hostStudentActivityViewModel;

    @SuppressLint({"RestrictedApi", "NotifyDataSetChanged"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentMainMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        hostStudentActivityViewModel = new ViewModelProvider(this).get(HostStudentActivityViewModel.class);
        getViewModelStore().put("hostStudentActivityViewModel", hostStudentActivityViewModel);

        hostStudentActivityViewModel.getUserInstitutions();
        hostStudentActivityViewModel.loadUserDetails();
        hostStudentActivityViewModel.listenerSnapshotChanges();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode());
        navigation();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
    private void navigation(){
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_student);
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.bottomActionBarStudents, navController);
    }
}