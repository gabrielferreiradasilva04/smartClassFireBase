package com.gabriel.smartclass.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.viewModels.RegisterViewModel;

public class RegisterForm extends AppCompatActivity {
    private Button loginButton;
    private Button registerButton;
    private RegisterViewModel registerViewModel;
    private EditText edTxtEmail;
    private EditText edTxtPassword;
    private EditText edTxtFirstAndLastName;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_form);
        this.loginButton = findViewById(R.id.loginButton_register);
        this.registerButton = findViewById(R.id.registerButton_register);
        this.edTxtEmail = findViewById(R.id.edTxtEmail_register);
        this.edTxtPassword = findViewById(R.id.edTxtPassword_register);
        this.registerViewModel = new RegisterViewModel();
        this.edTxtFirstAndLastName = findViewById(R.id.edTxtName_Register);
        this.loginButton.setOnClickListener(goToLogin());
        register();
    }



    private void register() {
        this.registerButton.setOnClickListener(view ->{
            registerViewModel.registerUser(edTxtEmail.getText().toString(), edTxtPassword.getText().toString(), edTxtFirstAndLastName.getText().toString() ,this);
        });
    }

    @NonNull
    private View.OnClickListener goToLogin() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterForm.this, LoginForm.class);
                startActivity(i);
                finish();
            }
        };
    }
}