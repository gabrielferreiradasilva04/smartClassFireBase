package com.gabriel.smartclass.view.course.views;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.gabriel.smartclass.databinding.ActivityTimeTableViewBinding;
import com.gabriel.smartclass.view.course.fragmentAdapter.TabDayFragmentAdapter;
import com.gabriel.smartclass.viewModels.TimeTableViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

public class TimeTableView extends AppCompatActivity {
    private TimeTableViewModel viewModel;
    private ActivityTimeTableViewBinding binding;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabDayFragmentAdapter tabDayFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTimeTableViewBinding.inflate(getLayoutInflater());
        this.initialize();
        setContentView(binding.getRoot());
    }

    private void initialize() {
        navigation();
        buildViewModel();
    }

    private void buildViewModel() {
        this.viewModel = new ViewModelProvider(this).get(TimeTableViewModel.class);
        this.viewModel.setInstitution(getIntent().getParcelableExtra("institution"));
        this.viewModel.setCourse(getIntent().getParcelableExtra("course"));
        this.viewModel.setClassroom(getIntent().getParcelableExtra("classroom"));
        this.viewModel.getSnackbarText().observe(this, this.snackbarObserver());
    }

    private Observer<? super String> snackbarObserver() {
        return text ->{
            if(text != null && !text.equals("")){
                Snackbar.make(binding.tabDaysPager, text, Snackbar.LENGTH_SHORT).show();
            }
        };
    }

    private void navigation() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Horário");
        this.viewPager = binding.tabDaysPager;
        this.tabDayFragmentAdapter = new TabDayFragmentAdapter(getSupportFragmentManager());
        this.viewPager.setAdapter(this.tabDayFragmentAdapter);
        this.tabLayout = this.binding.tabDays;
        this.tabLayout.setupWithViewPager(this.viewPager);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}