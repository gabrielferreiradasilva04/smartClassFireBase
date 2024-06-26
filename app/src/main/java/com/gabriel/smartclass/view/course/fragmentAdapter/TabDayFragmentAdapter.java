package com.gabriel.smartclass.view.course.fragmentAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.gabriel.smartclass.view.course.fragments.FridayFragment;
import com.gabriel.smartclass.view.course.fragments.MondayFragment;
import com.gabriel.smartclass.view.course.fragments.SaturdayFragments;
import com.gabriel.smartclass.view.course.fragments.SundayFragment;
import com.gabriel.smartclass.view.course.fragments.ThursdayFragment;
import com.gabriel.smartclass.view.course.fragments.TuesdayFragment;
import com.gabriel.smartclass.view.course.fragments.WednesdayFragment;

public class TabDayFragmentAdapter extends FragmentPagerAdapter {
    public TabDayFragmentAdapter(FragmentManager manager){
        super(manager);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if(position == 0){
            fragment = new MondayFragment();
        }
        if(position == 1){
            fragment = new TuesdayFragment();
        }
        if(position == 2){
            fragment = new WednesdayFragment();
        }
        if(position == 3){
            fragment = new ThursdayFragment();
        }
        if(position == 4){
            fragment = new FridayFragment();
        }
        if(position == 5){
            fragment = new SaturdayFragments();
        }
        if(position == 6){
            fragment = new SundayFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 7;
    }
    public CharSequence getPageTitle(int position){
        String title = null;
        if(position == 0){
            title = "SEG";
        }
        if(position == 1){
            title = "TER";
        }
        if(position == 2){
            title = "QUA";
        }
        if(position == 3){
            title = "QUI";
        }
        if(position == 4){
            title = "SEX";
        }
        if(position == 5){
            title = "SAB";
        }
        if(position == 6){
            title = "DOM";
        }
        return title;
    }
}
