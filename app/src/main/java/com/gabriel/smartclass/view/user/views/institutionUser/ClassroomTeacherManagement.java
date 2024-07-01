package com.gabriel.smartclass.view.user.views.institutionUser;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.gabriel.smartclass.databinding.ActivityClassroomTeacherManagementBinding;
import com.gabriel.smartclass.view.user.fragments.fragmentAdapter.TeacherManagementAdapter;
import com.gabriel.smartclass.viewModels.ClassroomTeacherManagementViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

public class ClassroomTeacherManagement extends AppCompatActivity {
    private ActivityClassroomTeacherManagementBinding binding;
    private ViewPager viewPager;
    private TeacherManagementAdapter tabTeacherManagement;
    private TabLayout tabLayout;
    private ClassroomTeacherManagementViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClassroomTeacherManagementBinding.inflate(getLayoutInflater());
        navigation();
        buildMenu();
        initialize();
        setContentView(binding.getRoot());
    }

    private void buildMenu() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.viewModel.getSnackbartext().setValue("");
        this.viewModel.getSnackbartext().removeObserver(this.snackbarobserver());
    }

    public void initialize(){
        this.buildViewModel();
    }
    public void buildViewModel(){
        this.viewModel = new ViewModelProvider(this).get(ClassroomTeacherManagementViewModel.class);
        this.viewModel.setCourse(this.getIntent().getParcelableExtra("course"));
        this.viewModel.setSubject(this.getIntent().getParcelableExtra("subject"));
        this.viewModel.setTeacher(this.getIntent().getParcelableExtra("teacher"));
        this.viewModel.setInstitution(this.getIntent().getParcelableExtra("institution"));
        this.viewModel.setCourseMember(this.getIntent().getParcelableExtra("student"));
        this.viewModel.setClassroom(this.getIntent().getParcelableExtra("classroom"));
        this.viewModel.getStudentByCourseMember();
        this.viewModel.getSnackbartext().observe(this, this.snackbarobserver());
        this.viewModel.syncStudent();

        Log.d("Outro recebimento", "buildViewModel: "+ this.viewModel.getTeacher().getDescription()+"\n"+this.viewModel.getClassroom().getDescription()+"\n"+this.viewModel.getSubject().getDescription()+"\n"+this.viewModel.getInstitution().getName()+"\n"+this.viewModel.getCourseMember().getDescription());

    }

    private Observer<? super String> snackbarobserver() {
        return text ->{
            if(text != null && !text.equals("")){
                Snackbar.make(binding.tabManagementPager, text, Snackbar.LENGTH_SHORT).show();
            }
        };
    }

    private void navigation() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Propriedades");
        this.viewPager = binding.tabManagementPager;
        this.tabTeacherManagement = new TeacherManagementAdapter(getSupportFragmentManager());
        this.viewPager.setAdapter(this.tabTeacherManagement);
        this.tabLayout = this.binding.tabManagement;
        this.tabLayout.setupWithViewPager(this.viewPager);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}