package com.gabriel.smartclass.view.auth;

import androidx.lifecycle.Observer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.view.base.BaseActivity;
import com.gabriel.smartclass.viewModels.LoginViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginForm extends BaseActivity {
    private Button registerButton;
    private Button loginButton;
    private Button institutionRegisterButton;
    private EditText edTxtEmail;
    private EditText edTxtPassword;

    private LoginViewModel loginViewModel;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseUser currentUserApplication = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUserApplication!=null){
            FirebaseAuth.getInstance().signOut();
        }
        if(getSupportActionBar() !=null){
            getSupportActionBar().hide();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);
        this.loginButton = findViewById(R.id.loginButton_login);
        this.registerButton = findViewById(R.id.registerButton_login);
        this.edTxtEmail = findViewById(R.id.edTxtEmail_login);
        this.edTxtPassword = findViewById(R.id.edTxtPassword_login);
        this.institutionRegisterButton = findViewById(R.id.institutionRegisterButton_login);
        this.loginViewModel = new LoginViewModel(this);
        loginViewModel.getSnackBarText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.loginButton_login), s, Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });



        this.registerButton.setOnClickListener(view -> {
                Intent i = new Intent(LoginForm.this, RegisterForm.class);
                startActivity(i);
            overridePendingTransition(R.anim.to_left,
                    R.anim.fade_in
            );
        });
        this.institutionRegisterButton.setOnClickListener(view ->{
            Intent i = new Intent(LoginForm.this, InstitutionRegisterForm.class);
            startActivity(i);
            overridePendingTransition(R.anim.to_left,
                    R.anim.fade_in);
        });

        this.loginButton.setOnClickListener(view ->{
            hideKeyboard();
            loginViewModel.loginUser(this.edTxtEmail.getText().toString(), this.edTxtPassword.getText().toString());
        });
    }
}