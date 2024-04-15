package com.gabriel.smartclass.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.ui.NavigationUI;
import android.os.Bundle;
import android.view.Menu;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.databinding.ActivityInstitutionMainMenuBinding;
import com.gabriel.smartclass.viewModels.HostUserActivityViewModel;
import com.google.android.material.badge.BadgeDrawable;

import java.util.Objects;

/** @noinspection ALL*/
public class InstitutionMainMenu extends AppCompatActivity {
    private  HostUserActivityViewModel viewModel;
    private ActivityInstitutionMainMenuBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInstitutionMainMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigation();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        ViewModelProvider viewModelProvider = new ViewModelProvider(this);
        viewModel = viewModelProvider.get(HostUserActivityViewModel.class);
        viewModel.getInstitutionByCurrentUser();
        viewModel.loadUserPicture();
        viewModel.getNotifications().observe(this, notificationsObserve());
    }

    private void navigation(){
        NavHost navHost = (NavHost) getSupportFragmentManager().findFragmentById(R.id.fragment_container_institution);
        assert navHost != null;
        NavController navController = navHost.getNavController();
        NavigationUI.setupWithNavController(binding.bottomActionBarInstitutions, navController);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_action_bar, menu);
        return true;
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

    public void updateTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}