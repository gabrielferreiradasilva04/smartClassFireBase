package com.gabriel.smartclass.view.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.gabriel.smartclass.databinding.ActivityInstitutionRegisterFormBinding;
import com.gabriel.smartclass.utilities.maskListeners.MaskListenerCPNJ;

public class InstitutionRegisterForm extends AppCompatActivity {
   private ActivityInstitutionRegisterFormBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInstitutionRegisterFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MaskListenerCPNJ listenerCPNJ = new MaskListenerCPNJ(binding.textCpnjInstitutionRegister);
        binding.textCpnjInstitutionRegister.addTextChangedListener(listenerCPNJ);
    }
}