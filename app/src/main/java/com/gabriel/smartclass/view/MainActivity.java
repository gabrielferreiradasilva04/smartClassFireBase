package com.gabriel.smartclass.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.view.base.BaseActivity;

public class MainActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}