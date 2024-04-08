package com.gabriel.smartclass.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.databinding.ActivityInstitutionMainMenuBinding;
import com.gabriel.smartclass.viewModels.HostInstitutionActivityViewModel;
import com.google.android.material.badge.BadgeDrawable;

public class InstitutionMainMenu extends AppCompatActivity {
    private NavHost navHost;
    private NavController navController;
    private ActivityInstitutionMainMenuBinding binding;
    private HostInstitutionActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInstitutionMainMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode());
        viewModel = new HostInstitutionActivityViewModel(this);
        navigation();
        viewModel.getNotifications().observe(this, notificationsObserve());
    }



    private void navigation(){
        navHost = (NavHost) getSupportFragmentManager().findFragmentById(R.id.fragment_container_institution);
        assert navHost != null;
        navController =navHost.getNavController();
        NavigationUI.setupWithNavController(binding.bottomActionBarInstitutions, navController);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public void updateNotificationsNumber(Integer number){
        BadgeDrawable badgeDrawable = binding.bottomActionBarInstitutions.getOrCreateBadge(R.id.institution_menu_notifications);
        badgeDrawable.setVisible(true);
        badgeDrawable.setNumber(number);

    }
    @NonNull
    private Observer<Integer> notificationsObserve() {
        return new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                updateNotificationsNumber(integer);
            }
        };
    }

}