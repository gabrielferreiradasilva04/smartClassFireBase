package com.gabriel.smartclass.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.databinding.ActivityStudentMainMenuBinding;

public class StudentMainMenu extends AppCompatActivity {
    private ActivityStudentMainMenuBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentMainMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}