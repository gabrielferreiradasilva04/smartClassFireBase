package com.gabriel.smartclass.view.user.views.institutionUser;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.databinding.ActivityCreateClassroomBinding;
import com.gabriel.smartclass.view.user.fragments.institutionUser.DialogAddMembersOnClass;
import com.gabriel.smartclass.viewModels.CreateClassroomViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class CreateClassroom extends AppCompatActivity {
    private ActivityCreateClassroomBinding binding;
    private CreateClassroomViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateClassroomBinding.inflate(getLayoutInflater());
        this.initialize();
        setContentView(binding.getRoot());
    }
    public void initialize(){
        this.buildMenu();
        this.buildViewModel();
        this.buttonListeners();
    }
    public void buildViewModel(){
        this.viewModel = new ViewModelProvider(this).get(CreateClassroomViewModel.class);
        this.viewModel.setInstitution(getIntent().getParcelableExtra("institution"));
        this.viewModel.setCourse(getIntent().getParcelableExtra("course"));
    }
    public void buttonListeners(){
        binding.buttonstudents.setOnClickListener(view ->{
            this.inflateMemberDialog(true);
        });
        binding.buttonteachers.setOnClickListener(view ->{
            this.inflateMemberDialog(false);
        });
    }

    private Observer<? super String> snackbarObserver() {
        return text ->{
            if(text != null && !text.equals("")){
                Snackbar.make(binding.getRoot(), text, Snackbar.LENGTH_SHORT).show();
            }
        };
    }

    private void inflateMemberDialog(boolean isStudent) {
        DialogAddMembersOnClass dialog = new DialogAddMembersOnClass();
        dialog.setStudent(isStudent);
        dialog.show(this.getSupportFragmentManager(), dialog.getTag());
    }

    public void updateTitle(String title){
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
        buildMenu();
    }
    private void buildMenu() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().setTitle("Criar Classe");
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
}