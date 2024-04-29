package com.gabriel.smartclass.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.databinding.ActivityStudentMainMenuBinding;
import com.gabriel.smartclass.view.base.BaseActivity;
import com.gabriel.smartclass.viewModels.HostUserActivityViewModel;

public class StudentMainMenu extends BaseActivity {
    private ActivityStudentMainMenuBinding binding;
    private NavHost navHostFragment;
    private NavController navController;
    private HostUserActivityViewModel hostUserActivityViewModel;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentMainMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewModelProvider viewModelProvider = new ViewModelProvider(this);
        hostUserActivityViewModel = viewModelProvider.get(HostUserActivityViewModel.class);
        hostUserActivityViewModel.getUserInstitutions();
        hostUserActivityViewModel.loadUserPicture();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigation();
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