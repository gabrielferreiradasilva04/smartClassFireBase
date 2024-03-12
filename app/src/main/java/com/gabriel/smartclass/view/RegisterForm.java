package com.gabriel.smartclass.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gabriel.smartclass.R;

public class RegisterForm extends AppCompatActivity {
    private Button loginButton;
    private EditText edTxtDocument;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_form);

        this.loginButton = findViewById(R.id.loginButton_register);
        this.edTxtDocument = findViewById(R.id.edTxtDocument_Register);



        this.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterForm.this, LoginForm.class);
                startActivity(i);
                finish();
            }
        });
    }
}