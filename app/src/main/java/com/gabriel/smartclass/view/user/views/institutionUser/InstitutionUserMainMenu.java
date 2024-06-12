package com.gabriel.smartclass.view.user.views.institutionUser;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.dao.UserTypeDAO;
import com.gabriel.smartclass.databinding.ActivityInstitutionuserMainmenuBinding;
import com.gabriel.smartclass.model.Course;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.InstitutionUser;
import com.gabriel.smartclass.view.base.BaseActivity;
import com.gabriel.smartclass.viewModels.InstitutionUserMainMenuViewModel;

public class InstitutionUserMainMenu extends BaseActivity {
    private ActivityInstitutionuserMainmenuBinding binding;
    private NavHost navHostFragment;
    private NavController navController;
    private InstitutionUserMainMenuViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInstitutionuserMainmenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initialize();
    }

    private void initialize() {
        buildMenu();
        buildViewModel();
        navigation();
        viewModel.loadUserByInstitutionUser();
        viewModel.getUserTypeByInstitutionUser().observe(this, userType -> {
            viewModel.loadUserCourses(userType.getDescription().equals("Coordenador"));
            this.courseClickListener();
        });
    }
    private void buildMenu() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    private void buildViewModel() {
        viewModel = new ViewModelProvider(this).get(InstitutionUserMainMenuViewModel.class);
        viewModel.setCurrentInstitution(getIntent().getParcelableExtra("institution"));
        viewModel.setCurrentInstitutionUser(getIntent().getParcelableExtra("institutionUser"));
    }
    private void courseClickListener() {
        this.viewModel.getCourseAdapter().setClickListenerAccess(course ->{
            if(this.viewModel.getCurrentInstitutionUser().getUserType_id().equals(new UserTypeDAO().COORDINATOR_TYPE_REFERENCE)){
                Log.d("click no curso", "initialize: chamou o metodo de click");
                this.callCoordinatorView(course);
            }
        });
    }

    private void callCoordinatorView(Course course) {
        Intent i = new Intent(this, CoordinatorCourseMainMenu.class);
        i.putExtra("course", course);
        i.putExtra("coordinator", this.viewModel.getCurrentInstitutionUser());
        i.putExtra("institution", this.viewModel.getCurrentInstitution());
        this.startActivity(i);
    }

    public void updateTitle(String title) {
        getSupportActionBar().setTitle(title);
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

    private void navigation() {
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_institution_user);
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.bottomActionBarInstitutionUsers, navController);
    }

}