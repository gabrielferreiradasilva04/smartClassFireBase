package com.gabriel.smartclass.view.fragments.institutionfragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.databinding.FragmentInstitutionHomeBinding;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.view.InstitutionMainMenu;

import com.gabriel.smartclass.viewModels.HostUserActivityViewModel;

import java.util.ArrayList;
import java.util.HashMap;

public class InstitutionHomeFragment extends Fragment {
    private FragmentInstitutionHomeBinding binding;
    private HostUserActivityViewModel viewModel;

    public InstitutionHomeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInstitutionHomeBinding.inflate(inflater, container, false);
        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());
        viewModel = viewModelProvider.get(HostUserActivityViewModel.class);
        InstitutionMainMenu main = (InstitutionMainMenu) getActivity();
        if (main != null) {
            main.updateTitle("Visão geral");
        }
        viewModel.getInstitutionStatistics();
        viewModel.getInstitutionMutableLiveData().observe(getViewLifecycleOwner(), institutionObserver());
        viewModel.getInstitutionStatisticsLiveData().observe(getViewLifecycleOwner(), institutionStatisticsObserver());
        binding.institutionSwipeRefreshLayout.setOnRefreshListener(getOnRefreshListener());
        binding.institutionMenuButton.setOnClickListener(menuButtonClickListener());
        return binding.getRoot();
    }

    @NonNull
    private SwipeRefreshLayout.OnRefreshListener getOnRefreshListener() {
        return () -> {
            viewModel.getInstitutionStatistics();
        };
    }

    public Observer<Institution> institutionObserver() {
        return institution -> {
            if (institution != null) {
                loadCapacity(institution);
            }
        };
    }

    public Observer<HashMap<String, Integer>> institutionStatisticsObserver() {
        return stringLongHashMap -> {
            loadStatistics(stringLongHashMap, binding.institutionSwipeRefreshLayout);
        };
    }

    public void loadCapacity(Institution institution) {
        binding.studentsBody1Number1.setText(Integer.toString(institution.getMaxStudents()));
        binding.teachersBody1Number1.setText(Integer.toString(institution.getMaxTeachers()));
        binding.coordinatorsBody1Number1.setText(Integer.toString(institution.getMaxCoordinators()));
        binding.classroomsBody1Number1.setText(Integer.toString(institution.getMaxClassrooms()));
    }

    public void loadStatistics(HashMap<String, Integer> statistics, SwipeRefreshLayout swipeRefreshLayout) {
        binding.studentsBody2Number2.setText(Integer.toString(statistics.get("students")));
        binding.teachersBody2Number2.setText(Integer.toString(statistics.get("teachers")));
        binding.coordinatorsBody2Number2.setText(Integer.toString(statistics.get("coordinators")));
        binding.numberOfCourses.setText(Integer.toString(statistics.get("courses")));
        progressToProgressBar();
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }

    }

    public void progressToProgressBar() {
        ArrayList<TextView> textViewsCapacity = new ArrayList<>();
        ArrayList<TextView> textViewsStatistics = new ArrayList<>();
        ArrayList<ProgressBar> progressBars = new ArrayList<>();
        ArrayList<ImageView> imageViewsWarning = new ArrayList<>();

        textViewsCapacity.add(binding.studentsBody1Number1);
        textViewsCapacity.add(binding.teachersBody1Number1);
        textViewsCapacity.add(binding.coordinatorsBody1Number1);
        textViewsCapacity.add(binding.classroomsBody1Number1);

        textViewsStatistics.add(binding.studentsBody2Number2);
        textViewsStatistics.add(binding.teachersBody2Number2);
        textViewsStatistics.add(binding.coordinatorsBody2Number2);
        textViewsStatistics.add(binding.classroomsBody2Number2);

        progressBars.add(binding.progressbarStudents);
        progressBars.add(binding.progressbarTeachers);
        progressBars.add(binding.progressbarCoordinators);
        progressBars.add(binding.progressbarClassrooms);

        imageViewsWarning.add(binding.warningProgressBarStudents);
        imageViewsWarning.add(binding.warningProgressBarTeachers);
        imageViewsWarning.add(binding.warningProgressBarCoordinators);
        imageViewsWarning.add(binding.warningProgressBarClassrooms);

        for (int i = 0; i < 4; i++) {
            int capacity = Integer.parseInt(textViewsCapacity.get(i).getText().toString());
            int statistic = Integer.parseInt(textViewsStatistics.get(i).getText().toString());
            if (capacity <= statistic) {
                progressBars.get(i).setMax(100);
                progressBars.get(i).setProgress(100);
                imageViewsWarning.get(i).setVisibility(View.VISIBLE);

            } else {
                progressBars.get(i).setMax(capacity);
                progressBars.get(i).setProgress(statistic, true);
            }
        }
    }
    public View.OnClickListener menuButtonClickListener(){
        return this::openMenuOptions;
    }
    private void openMenuOptions(View v) {
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.institution_main_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            return true;
        });
        popupMenu.show();
    }

}