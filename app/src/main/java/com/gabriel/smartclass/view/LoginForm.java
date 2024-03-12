package com.gabriel.smartclass.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.dao.UserTypeDao;
import com.gabriel.smartclass.model.UserType;

public class LoginForm extends AppCompatActivity {
    private Button registerButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);

        this.registerButton = findViewById(R.id.registerButton_login);
        /*
        UserType userType = new UserType("Admin");
        UserTypeDao userTypeDao = new UserTypeDao();
        userTypeDao.insert(userType);
        */

        UserTypeDao userTypeDao = new UserTypeDao();
        userTypeDao.findById("mpo6mhhxZDc1IUV9lr3L");


        this.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginForm.this, RegisterForm.class);
                startActivity(i);
                finish();
            }
        });
    }
}