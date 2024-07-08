package com.gabriel.smartclass.view.user.fragments.institutionUser;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gabriel.smartclass.databinding.EmptyFrequencyLayoutBinding;
import com.gabriel.smartclass.databinding.FragmentFrquencyBinding;
import com.gabriel.smartclass.observer.EmptyRecyclerViewObserver;
import com.gabriel.smartclass.viewModels.ClassroomStudentMainMenuViewModel;

public class FrequencyFragment extends Fragment {

    private FragmentFrquencyBinding binding;
    private ClassroomStudentMainMenuViewModel viewModel;

    public FrequencyFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.binding = FragmentFrquencyBinding.inflate(inflater, container, false);
        this.initialize();
        return this.binding.getRoot();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void onResume() {
        super.onResume();
        EmptyFrequencyLayoutBinding viewEmpty = binding.emptyfrequencyContainer;
        EmptyRecyclerViewObserver observer = new EmptyRecyclerViewObserver(binding.rcFrequency, viewEmpty.getRoot());
        viewModel.getFrequencyAdapter().registerAdapterDataObserver(observer);
        viewModel.getFrequencyAdapter().notifyDataSetChanged();

    }

    public void initialize() {
        this.buildViewModel();
        this.buildRecyclerView();
    }

    public void buildViewModel() {
        this.viewModel = new ViewModelProvider(requireActivity()).get(ClassroomStudentMainMenuViewModel.class);
    }

    public void buildRecyclerView() {
        this.binding.rcFrequency.setHasFixedSize(false);
        this.binding.rcFrequency.setLayoutManager(new LinearLayoutManager(this.getContext()));
        this.binding.rcFrequency.setAdapter(this.viewModel.getFrequencyAdapter());
    }

}