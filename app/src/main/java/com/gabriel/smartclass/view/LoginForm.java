package com.gabriel.smartclass.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.controller.LoginController;
import com.gabriel.smartclass.controller.RegisterController;


public class LoginForm extends AppCompatActivity {
    private Button registerButton;
    private Button loginButton;
    private EditText edTxtEmail;
    private EditText edTxtPassword;

    private LoginController controller;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);

        this.loginButton = findViewById(R.id.loginButton_login);
        this.registerButton = findViewById(R.id.registerButton_login);
        this.edTxtEmail = findViewById(R.id.edTxtEmail_login);
        this.edTxtPassword = findViewById(R.id.edTxtPassword_login);
        this.controller = new LoginController(this);

        this.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginForm.this, RegisterForm.class);
                startActivity(i);
                finish();
            }
        });

        this.loginButton.setOnClickListener(view ->{
            controller.signInwithEmailAndPassword(this.edTxtEmail.getText().toString(), this.edTxtPassword.getText().toString(), this);
        });
    }
}