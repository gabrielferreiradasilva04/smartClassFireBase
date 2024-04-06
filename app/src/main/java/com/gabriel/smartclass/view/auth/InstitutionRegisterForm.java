package com.gabriel.smartclass.view.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.gabriel.smartclass.databinding.ActivityInstitutionRegisterFormBinding;
import com.gabriel.smartclass.utilities.maskListeners.MaskListenerCPNJ;
import com.gabriel.smartclass.utilities.validate.CNPJValidator;
import com.gabriel.smartclass.viewModels.LoginViewModel;

public class InstitutionRegisterForm extends AppCompatActivity {
   private ActivityInstitutionRegisterFormBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LoginViewModel loginViewModel = new LoginViewModel();
        super.onCreate(savedInstanceState);
        binding = ActivityInstitutionRegisterFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MaskListenerCPNJ listenerCPNJ = new MaskListenerCPNJ(binding.textCpnjInstitutionRegister);
        binding.textCpnjInstitutionRegister.addTextChangedListener(listenerCPNJ);

        binding.registerButtonInstitutionRegister.setOnClickListener( view ->{
            try {

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}