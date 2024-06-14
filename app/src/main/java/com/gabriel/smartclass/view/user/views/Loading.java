package com.gabriel.smartclass.view.user.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.view.auth.LoginForm;

public class Loading extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        this.getSupportActionBar().hide();
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Iniciar a próxima Activity
            Intent intent = new Intent(Loading.this, LoginForm.class);
            startActivity(intent);
            // Finalizar a LoadingActivity
            finish();
        }, 5000); // 3000 milissegundos = 3 segundos
    }
}