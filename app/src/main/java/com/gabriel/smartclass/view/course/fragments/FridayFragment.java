package com.gabriel.smartclass.view.course.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.gabriel.smartclass.databinding.FragmentFridaySubjectsBinding;
import com.gabriel.smartclass.viewModels.TimeTableViewModel;

public class FridayFragment extends Fragment {
    private FragmentFridaySubjectsBinding binding;
    private TimeTableViewModel viewModel;
    public FridayFragment(){

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this. binding = FragmentFridaySubjectsBinding.inflate(inflater, container, false);
        this.initialize();
        return this.binding.getRoot();
    }
    public void initialize(){
        this.buildViewModel();
    }

    private void buildViewModel() {
        this.viewModel = new ViewModelProvider(requireActivity()).get(TimeTableViewModel.class);
    }
}