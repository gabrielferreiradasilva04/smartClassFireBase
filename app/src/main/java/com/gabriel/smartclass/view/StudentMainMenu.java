package com.gabriel.smartclass.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;

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



    }
}