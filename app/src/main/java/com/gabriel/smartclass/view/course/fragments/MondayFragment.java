package com.gabriel.smartclass.view.course.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gabriel.smartclass.adapter.TimeTableItemsRCA;
import com.gabriel.smartclass.databinding.EmptyTimetableBinding;
import com.gabriel.smartclass.databinding.FragmentMondayBinding;
import com.gabriel.smartclass.observer.EmptyRecyclerViewObserver;
import com.gabriel.smartclass.viewModels.TimeTableViewModel;

import java.util.List;
import java.util.Objects;

@SuppressLint("NotifyDataSetChanged")

public class MondayFragment extends Fragment {
    private FragmentMondayBinding binding;
    private final TimeTableItemsRCA adapter = new TimeTableItemsRCA();
    private TimeTableViewModel viewModel;
    public static final int DAY = 1;

    public MondayFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = FragmentMondayBinding.inflate(inflater, container, false);
        this.initialize();
        return this.binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.emptyView();
    }

    public void initialize() {
        this.buildRecyclerView();
        this.buildViewModel();
        this.subjectsObserver();
        this.binding.addMondayButton.setOnClickListener(this.addSubjectListener());
        if(!this.viewModel.isShowEdition()){
            binding.mondaysubject.setVisibility(View.GONE);
            binding.addMondayButton.setVisibility(View.GONE);
        }
    }

    private View.OnClickListener addSubjectListener() {
        return view -> addSubject();
    }

    private void addSubject() {
        String subject = Objects.requireNonNull(this.binding.mondaysubject.getText()).toString();
        if(!subject.equals("")){
            this.viewModel.addNewSubject(subject, DAY);
        }
    }

    private void subjectsObserver() {
        this.viewModel.getMondaySubjectsLiveData().observe(this.getViewLifecycleOwner(), observerList());
    }

    private Observer<? super List<String>> observerList() {
        return list -> {
            this.adapter.getSubjects().setValue(list);
            this.adapter.notifyDataSetChanged();
        };
    }

    private void buildViewModel() {
        this.viewModel = new ViewModelProvider(this.requireActivity()).get(TimeTableViewModel.class);
    }

    private void buildRecyclerView() {
        this.binding.rcMonday.setHasFixedSize(true);
        this.binding.rcMonday.setLayoutManager(new LinearLayoutManager(this.getContext()));
        this.binding.rcMonday.setAdapter(adapter);
        this.adapter.setRemoveSubject(index -> this.viewModel.removeSubject(index, DAY));
    }

    private void emptyView() {
        EmptyTimetableBinding emptyBinding = binding.emptyMonday;
        EmptyRecyclerViewObserver emptyRecyclerViewObserver = new EmptyRecyclerViewObserver(binding.rcMonday, emptyBinding.getRoot());
        this.adapter.registerAdapterDataObserver(emptyRecyclerViewObserver);
        this.adapter.notifyDataSetChanged();
    }
}