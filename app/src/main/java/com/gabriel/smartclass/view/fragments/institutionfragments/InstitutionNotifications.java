package com.gabriel.smartclass.view.fragments.institutionfragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gabriel.smartclass.databinding.EmptyRequestBinding;
import com.gabriel.smartclass.databinding.FragmentInstitutionNotificationsBinding;
import com.gabriel.smartclass.observer.EmptyRecyclerViewObserver;
import com.gabriel.smartclass.viewModels.InstitutionLinkRequestsFragmentViewModel;
import com.google.android.material.snackbar.Snackbar;

public class InstitutionNotifications extends Fragment {
    private FragmentInstitutionNotificationsBinding binding;
    private InstitutionLinkRequestsFragmentViewModel viewModel;
    private RecyclerView recyclerView;
    public InstitutionNotifications() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentInstitutionNotificationsBinding.inflate(inflater, container, false);
        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());
        viewModel = viewModelProvider.get(InstitutionLinkRequestsFragmentViewModel.class);
        viewModel.getSnackBarText().observe(getViewLifecycleOwner(), observeSnackbar());
        viewModel.loadInstitutionLinkRequests();
        loadLinkRequests();
        return binding.getRoot();
    }
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onStart() {
        super.onStart();
        EmptyRequestBinding viewEmpty = binding.emptyContainerHome;
        EmptyRecyclerViewObserver observer = new EmptyRecyclerViewObserver(recyclerView, viewEmpty.getRoot());
        viewModel.getInstitutionLinkRequestsAdapter().registerAdapterDataObserver(observer);
        viewModel.getInstitutionLinkRequestsAdapter().notifyDataSetChanged();
    }

    public void loadLinkRequests(){
        recyclerView = binding.institutionNotificationsRecyclerView;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(viewModel.getInstitutionLinkRequestsAdapter());
    }

    @NonNull
    private Observer<String> observeSnackbar() {
        return s -> {
            if (s != null) {
                Snackbar snackbar = Snackbar.make(binding.institutionNotificationsRecyclerView, s, Snackbar.LENGTH_SHORT);
                snackbar.show();
            }

        };
    }

}