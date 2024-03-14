package com.gabriel.smartclass.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.databinding.ActivityStudentMainMenuBinding;

public class StudentMainMenu extends AppCompatActivity {
    private ActivityStudentMainMenuBinding binding;
    private NavHost navHostFragment;
    private NavController navController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentMainMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        navigation();
    }

    private void navigation(){
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_student);
        navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(binding.bottomActionBarStudents, navController);
    }
}