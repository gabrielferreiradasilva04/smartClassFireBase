package com.gabriel.smartclass.view.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.databinding.ActivityInstitutionRegisterFormBinding;
import com.gabriel.smartclass.utilities.maskListeners.MaskListenerCPNJ;
import com.gabriel.smartclass.utilities.validate.CNPJValidator;
import com.gabriel.smartclass.viewModels.LoginViewModel;
import com.gabriel.smartclass.viewModels.RegisterViewModel;
import com.google.android.material.snackbar.Snackbar;

public class InstitutionRegisterForm extends RegisterForm {
    private RegisterViewModel registerViewModel;
   private ActivityInstitutionRegisterFormBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar() !=null){
            getSupportActionBar().hide();
        }
        binding = ActivityInstitutionRegisterFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        registerViewModel = new RegisterViewModel(this);
        snackBarObserver();
        cnpjMask();
        binding.registerButtonInstitutionRegister.setOnClickListener(registerListener());
        binding.loginButtonInstitutionRegister.setOnClickListener(goToLogin());
    }

    private void snackBarObserver() {
        registerViewModel.getSnackBarText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Snackbar snackbar = Snackbar.make(binding.registerButtonInstitutionRegister, s, Snackbar.LENGTH_SHORT).setBackgroundTint(Color.BLUE);
                snackbar.show();
            }
        });
    }

    private void cnpjMask() {
        MaskListenerCPNJ listenerCPNJ = new MaskListenerCPNJ(binding.textCpnjInstitutionRegister);
            binding.textCpnjInstitutionRegister.addTextChangedListener(listenerCPNJ);
    }

    public View.OnClickListener registerListener(){
        hideKeyboard();
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cnpj = binding.textCpnjInstitutionRegister.getText().toString();
                String email = binding.textEmailInstitutionRegister.getText().toString();
                String name = binding.textNameInstitutionRegister.getText().toString();
                String password = binding.textPasswordInstitutionRegister.getText().toString();
                String confirmPassword = binding.textConfirmPasswordInstitutionRegister.getText().toString();

                registerViewModel.registerInstitution(cnpj,name,email,password, confirmPassword);
            }
        };
    }
    @NonNull
    private View.OnClickListener goToLogin() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InstitutionRegisterForm.this, LoginForm.class);
                startActivity(i);
                finish();
            }
        };
    }
}