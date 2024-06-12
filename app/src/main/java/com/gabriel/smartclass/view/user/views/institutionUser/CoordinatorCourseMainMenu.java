package com.gabriel.smartclass.view.user.views.institutionUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;


import android.os.Bundle;
import android.view.Menu;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.databinding.ActivityCoordinatorCourseMainMenuBinding;
import com.gabriel.smartclass.viewModels.CoordinatorCourseViewModel;

import java.util.Objects;

public class CoordinatorCourseMainMenu extends AppCompatActivity {
    private ActivityCoordinatorCourseMainMenuBinding binding;
    private CoordinatorCourseViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCoordinatorCourseMainMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        navigation();
        initialize();
    }
    public void initialize(){
        buildMenu();
        this.viewModel = new ViewModelProvider(this).get(CoordinatorCourseViewModel.class);
        this.viewModel.setCoordinator(getIntent().getParcelableExtra("coordinator"));
        this.viewModel.setCourse(getIntent().getParcelableExtra("course"));
        this.viewModel.setInstitution(getIntent().getParcelableExtra("institution"));
    }
    public void updateTitle(String title){
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
    }
    private void buildMenu() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
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
        NavHost navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_usercourse);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.bottomActionBarUserCourse, navController);
    }
}