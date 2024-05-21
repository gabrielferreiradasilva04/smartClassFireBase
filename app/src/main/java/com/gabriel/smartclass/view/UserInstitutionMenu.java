package com.gabriel.smartclass.view;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.databinding.ActivityUserInstitutionMenuBinding;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.view.base.BaseActivity;

public class UserInstitutionMenu extends BaseActivity {
    private Institution currentInstitution;
    private ActivityUserInstitutionMenuBinding binding;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserInstitutionMenuBinding.inflate(getLayoutInflater());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(binding.getRoot());
        currentInstitution = getIntent().getParcelableExtra("institution");
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_action_bar, menu);
        return true;
    }

}