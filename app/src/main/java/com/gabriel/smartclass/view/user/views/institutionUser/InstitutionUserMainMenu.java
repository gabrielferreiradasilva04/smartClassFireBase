package com.gabriel.smartclass.view.user.views.institutionUser;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.databinding.ActivityInstitutionuserMainmenuBinding;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.InstitutionUser;
import com.gabriel.smartclass.view.base.BaseActivity;

public class InstitutionUserMainMenu extends BaseActivity {
    private ActivityInstitutionuserMainmenuBinding binding;
    private NavHost navHostFragment;
    private NavController navController;
    private   Institution currentInstitution;
    private  InstitutionUser currentInstitutionUser;
    public static MutableLiveData<Bitmap> institutionUserProfilePicture = new MutableLiveData<>();

    public Institution getCurrentInstitution() {
        return currentInstitution;
    }
    public InstitutionUser getCurrentInstitutionUser() {
        return currentInstitutionUser;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInstitutionuserMainmenuBinding.inflate(getLayoutInflater());
        initialize();
    }

    private void initialize() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(binding.getRoot());
        currentInstitution = getIntent().getParcelableExtra("institution");
        currentInstitutionUser = getIntent().getParcelableExtra("institutionUser");
        navigation();
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
    private void navigation(){
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_institution_user);
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.bottomActionBarInstitutionUsers, navController);
    }

}