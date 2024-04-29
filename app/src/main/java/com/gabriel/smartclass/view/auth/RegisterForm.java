package com.gabriel.smartclass.view.auth;

import androidx.annotation.NonNull;
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
import com.gabriel.smartclass.view.base.BaseActivity;
import com.gabriel.smartclass.viewModels.RegisterViewModel;
import com.google.android.material.snackbar.Snackbar;

public class RegisterForm extends BaseActivity {
    private Button loginButton;
    private Button registerButton;
    private RegisterViewModel registerViewModel;
    private EditText edTxtEmail;
    private EditText edTxtPassword;
    private EditText edTxtFirstAndLastName;
    private EditText editTextConfirmPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar() !=null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_register_form);
        registerViewModel = new RegisterViewModel(this);
        this.loginButton = findViewById(R.id.loginButton_register);
        this.registerButton = findViewById(R.id.registerButton_register);
        this.edTxtEmail = findViewById(R.id.edTxtEmail_register);
        this.edTxtPassword = findViewById(R.id.edTxtPassword_register);
        this.edTxtFirstAndLastName = findViewById(R.id.edTxtName_Register);
        this.editTextConfirmPassword = findViewById(R.id.edTxtConfirmPassword_register);
        this.loginButton.setOnClickListener(goToLogin());

        registerViewModel.getSnackBarText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.registerButton_register), s, Snackbar.LENGTH_LONG).setBackgroundTint(Color.BLUE);
                snackbar.show();
            }
        });
        register();
    }



    private void register() {
        this.registerButton.setOnClickListener(view ->{
            hideKeyboard();
            registerViewModel.registerUser(edTxtEmail.getText().toString(), edTxtPassword.getText().toString(), edTxtFirstAndLastName.getText().toString(),editTextConfirmPassword.getText().toString() );
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