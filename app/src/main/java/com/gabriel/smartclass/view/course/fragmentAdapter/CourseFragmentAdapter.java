package com.gabriel.smartclass.view.course.fragmentAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.gabriel.smartclass.view.course.fragments.AddCourse;
import com.gabriel.smartclass.view.course.fragments.SearchAndEditCourses;

public class CourseFragmentAdapter extends FragmentPagerAdapter {

    public CourseFragmentAdapter(FragmentManager manager){
        super(manager);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if(position == 0){
            fragment = new AddCourse();
        }
        if(position == 1){
            fragment = new SearchAndEditCourses();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
    public CharSequence getPageTitle(int position){
        String title = null;
        if(position == 0){
            title = "Adicionar";
        }
        if(position == 1){
            title = "Consultar";
        }
        return title;
    }
}
