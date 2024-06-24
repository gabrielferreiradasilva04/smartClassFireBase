package com.gabriel.smartclass.view.user.views.commonUser;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.databinding.ActivityCommonuserMainmenuBinding;
import com.gabriel.smartclass.view.base.BaseActivity;
import com.gabriel.smartclass.viewModels.HostUserActivityViewModel;
import com.google.android.material.snackbar.Snackbar;

public class CommonUserMainMenu extends BaseActivity {
    private ActivityCommonuserMainmenuBinding binding;
    private NavHost navHostFragment;
    private NavController navController;
    private HostUserActivityViewModel hostUserActivityViewModel;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommonuserMainmenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initialize();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.hostUserActivityViewModel.getSnackBarText().removeObserver(snackbarObserver());
    }

    private void initialize() {
        ViewModelProvider viewModelProvider = new ViewModelProvider(this);
        hostUserActivityViewModel = viewModelProvider.get(HostUserActivityViewModel.class);
        hostUserActivityViewModel.getUserInstitutions();
        hostUserActivityViewModel.loadUserPicture();
        hostUserActivityViewModel.getSnackBarText().observe(this, snackbarObserver());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigation();
    }

    private Observer<? super String> snackbarObserver() {
        return text ->{
            if(text != null && !text.equals("")){
                Snackbar.make(binding.bottomActionBarStudents, text, Snackbar.LENGTH_SHORT).show();
            }
        };
    }

    public void updateTitle(String title){
        getSupportActionBar().setTitle(title);
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
    @Override
    protected void onStart() {
        super.onStart();
    }
    private void navigation(){
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_student);
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.bottomActionBarStudents, navController);
    }
}