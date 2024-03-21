package com.gabriel.smartclass.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.gabriel.smartclass.DAO.InstitutionDAO;
import com.gabriel.smartclass.R;
import com.gabriel.smartclass.model.Institution;
import com.google.firebase.Firebase;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    InstitutionDAO institutionDAO = new InstitutionDAO();
        Log.d("LISTA", "onCreate: "+ institutionDAO.listAllInstitutions());


    }
}