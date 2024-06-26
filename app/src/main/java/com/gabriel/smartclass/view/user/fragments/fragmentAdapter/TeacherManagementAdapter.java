package com.gabriel.smartclass.view.user.fragments.fragmentAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.gabriel.smartclass.view.user.fragments.institutionUser.TeacherFrequencyManagement;
import com.gabriel.smartclass.view.user.fragments.institutionUser.TeacherGradeManagement;

public class TeacherManagementAdapter extends FragmentPagerAdapter {
    public TeacherManagementAdapter(FragmentManager manager){
        super(manager);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if(position == 0){
            fragment = new TeacherGradeManagement();
        }
        if(position == 1){
            fragment = new TeacherFrequencyManagement();
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
            title = "Notas";
        }
        if(position == 1){
            title = "Frequência";
        }
        return title;
    }
}
