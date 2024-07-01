package com.gabriel.smartclass.view.user.views.institutionUser;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.databinding.ActivityInstitutionuserMainmenuBinding;
import com.gabriel.smartclass.model.Course;
import com.gabriel.smartclass.view.base.BaseActivity;
import com.gabriel.smartclass.view.user.fragments.institutionUser.ChooseClassDialog;
import com.gabriel.smartclass.viewModels.InstitutionUserMainMenuViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class InstitutionUserMainMenu extends BaseActivity {
    private ActivityInstitutionuserMainmenuBinding binding;
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
            viewModel.loadUserCourses(userType.getDescription());
            this.courseClickListener(userType.getDescription());
        });
        viewModel.getSnackbarText().observe(this, this.snackbarObserver());
    }

    private Observer<? super String> snackbarObserver() {
        return text -> {
            if (text != null && !text.equals("")) {
                Snackbar.make(binding.bottomActionBarInstitutionUsers, text, Snackbar.LENGTH_SHORT).show();
            }
        };
    }

    private void buildMenu() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    private void buildViewModel() {
        viewModel = new ViewModelProvider(this).get(InstitutionUserMainMenuViewModel.class);
        viewModel.setCurrentInstitution(getIntent().getParcelableExtra("institution"));
        viewModel.setCurrentInstitutionUser(getIntent().getParcelableExtra("institutionUser"));
    }

    private void courseClickListener(String userType) {
        this.viewModel.getCourseAdapter().setClickListenerAccess(course -> {
            switch (userType) {
                case "Coordenador":
                    this.callCoordinatorView(course);
                    break;
                case "Professor":
                case "Estudante":
                    this.inflateClassDialog(course);
                    break;
                default:
                    Snackbar.make(binding.bottomActionBarInstitutionUsers, "Selecione uma opção válida", Snackbar.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    private void inflateClassDialog(Course course) {
        this.viewModel.setSelected_course(course);
        this.viewModel.loadClassroomsUser();
        ChooseClassDialog dialog = new ChooseClassDialog();
        dialog.show(this.getSupportFragmentManager(), dialog.getTag());
    }

    private void callCoordinatorView(Course course) {
        Intent i = new Intent(this, CoordinatorCourseMainMenu.class);
        i.putExtra("course", course);
        i.putExtra("coordinator", this.viewModel.getCurrentInstitutionUser());
        i.putExtra("institution", this.viewModel.getCurrentInstitution());
        this.startActivity(i);
    }


    public void updateTitle(String title) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



    private void navigation() {
        NavHost navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_institution_user);
        NavController navController = null;
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        }
        if (navController != null) {
            NavigationUI.setupWithNavController(binding.bottomActionBarInstitutionUsers, navController);
        }
    }

}