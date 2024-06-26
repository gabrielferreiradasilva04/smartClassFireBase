package com.gabriel.smartclass.view.course.views;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.gabriel.smartclass.databinding.ActivityTimeTableViewBinding;
import com.gabriel.smartclass.view.course.fragmentAdapter.TabDayFragmentAdapter;
import com.google.android.material.tabs.TabLayout;

public class TimeTableView extends AppCompatActivity {
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