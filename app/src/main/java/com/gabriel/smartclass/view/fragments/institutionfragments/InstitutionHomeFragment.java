package com.gabriel.smartclass.view.fragments.institutionfragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gabriel.smartclass.databinding.FragmentInstitutionHomeBinding;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.view.InstitutionMainMenu;

import com.gabriel.smartclass.viewModels.HostUserActivityViewModel;

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
        viewModel.getInstitutionMutableLiveData().observe(getViewLifecycleOwner(),institutionObserver());
        viewModel.getInstitutionStatisticsLiveData().observe(getViewLifecycleOwner(), institutionStatisticsObserver());
        return binding.getRoot();
    }

    public Observer<Institution> institutionObserver(){
        return institution -> {
          loadcCapacity();
        };
    }
    public Observer<HashMap<String, Long>> institutionStatisticsObserver(){
        return this::loadStatistics;
    }
    public void loadcCapacity() {
        if (viewModel.getInstitutionMutableLiveData().getValue() != null) {
            Institution institution = viewModel.getInstitutionMutableLiveData().getValue();
            binding.studentsBody1Number1.setText(Integer.toString(institution.getMaxStudents()));
            binding.teachersBody1Number1.setText(Integer.toString(institution.getMaxTeachers()));
            binding.coordinatorsBody1Number1.setText(Integer.toString(institution.getMaxCoordinators()));
            binding.classroomsBody1Number1.setText(Integer.toString(institution.getMaxClassrooms()));
        }
    }
    public void loadStatistics(HashMap<String, Long> statistics){
        if(statistics!=null){
            binding.studentsBody2Number2.setText(Long.toString(statistics.get("students")));
            binding.teachersBody2Number2.setText(Long.toString(statistics.get("teachers")));
            binding.coordinatorsBody2Number2.setText(Long.toString(statistics.get("coordinators")));
            binding.numberOfCourses.setText(Long.toString(statistics.get("courses")));
        }

    }

    public void progressToProgressBar(){
    }
}