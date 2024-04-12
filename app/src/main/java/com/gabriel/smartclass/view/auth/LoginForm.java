package com.gabriel.smartclass.view.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.dao.UserDAO;
import com.gabriel.smartclass.model.User;
import com.gabriel.smartclass.viewModels.LoginViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginForm extends AppCompatActivity {
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
                finish();
        });
        this.institutionRegisterButton.setOnClickListener(view ->{
            Intent i = new Intent(LoginForm.this, InstitutionRegisterForm.class);
            startActivity(i);
            finish();
        });

        this.loginButton.setOnClickListener(view ->{
            loginViewModel.loginUser(this.edTxtEmail.getText().toString(), this.edTxtPassword.getText().toString());
        });
    }
}