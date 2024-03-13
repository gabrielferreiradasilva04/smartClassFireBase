package com.gabriel.smartclass.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.controller.RegisterController;

import java.util.Timer;
import java.util.TimerTask;

public class RegisterForm extends AppCompatActivity {
    private Button loginButton;
    private Button registerButton;
    private RegisterController controller;
    private EditText edTxtEmail;
    private EditText edTxtPassword;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_form);


        this.loginButton = findViewById(R.id.loginButton_register);
        this.registerButton = findViewById(R.id.registerButton_register);
        this.edTxtEmail = findViewById(R.id.edTxtEmail_register);
        this.edTxtPassword = findViewById(R.id.edTxtPassword_register);


        this.controller = new RegisterController(this);



        this.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterForm.this, LoginForm.class);
                startActivity(i);
                finish();
            }
        });
        this.registerButton.setOnClickListener(view ->{
            controller.CreateNewUserByEmailAndPassword(edTxtEmail.getText().toString(), edTxtPassword.getText().toString(),this);
        });
    }
}