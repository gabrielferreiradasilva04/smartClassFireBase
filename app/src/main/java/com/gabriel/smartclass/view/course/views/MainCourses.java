package com.gabriel.smartclass.view.course.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.databinding.ActivityMainCoursesBinding;
import com.gabriel.smartclass.view.course.fragments.CourseFragmentAdapter;
import com.google.android.material.tabs.TabLayout;

public class MainCourses extends AppCompatActivity {
    com.gabriel.smartclass.databinding.ActivityMainCoursesBinding binding;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CourseFragmentAdapter fragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = com.gabriel.smartclass.databinding.ActivityMainCoursesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initialize();
    }

    public void initialize() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cursos");
        viewPager = binding.mainCourseViewPager;
        fragmentAdapter = new CourseFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentAdapter);
        tabLayout = binding.mainCourseTabs;
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}