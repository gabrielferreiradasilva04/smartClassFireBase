package com.gabriel.smartclass.view.fragments.institutionfragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.adapter.interfaces.ApproveLinkRequestClickListener;
import com.gabriel.smartclass.adapter.interfaces.RejectLinkRequestClickListener;
import com.gabriel.smartclass.databinding.EmptyRequestBinding;
import com.gabriel.smartclass.databinding.FragmentInstitutionNotificationsBinding;
import com.gabriel.smartclass.model.InstitutionLinkRequest;
import com.gabriel.smartclass.observer.EmptyRecyclerViewObserver;
import com.gabriel.smartclass.viewModels.HostUserActivityViewModel;
import com.gabriel.smartclass.viewModels.InstitutionLinkRequestsFragmentViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class InstitutionLinkRequestFragment extends Fragment {
    private FragmentInstitutionNotificationsBinding binding;
    private InstitutionLinkRequestsFragmentViewModel primaryViewModel;
    private HostUserActivityViewModel secondaryViewModel;
    private RecyclerView recyclerView;
    public InstitutionLinkRequestFragment() {
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInstitutionNotificationsBinding.inflate(inflater, container, false);
        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());
        primaryViewModel = viewModelProvider.get(InstitutionLinkRequestsFragmentViewModel.class);
        secondaryViewModel = viewModelProvider.get(HostUserActivityViewModel.class);
        primaryViewModel.getSnackBarText().observe(getViewLifecycleOwner(), observeSnackbar());
        loadLinkRequests();
        binding.institutionNoitificationsFilterButton.setOnClickListener(filterButtonClickListener());
        return binding.getRoot();
    }
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onStart() {
        super.onStart();
        EmptyRequestBinding viewEmpty = binding.emptyContainerHome;
        EmptyRecyclerViewObserver observer = new EmptyRecyclerViewObserver(recyclerView, viewEmpty.getRoot());
        secondaryViewModel.getInstitutionLinkRequestsAdapter().registerAdapterDataObserver(observer);
        secondaryViewModel.getInstitutionLinkRequestsAdapter().notifyDataSetChanged();
    }

    public void loadLinkRequests(){
        secondaryViewModel.getInstitutionLinkRequestsAdapter().setApproveLinkRequestClickListener(approveLinkRequestClickListener());
        secondaryViewModel.getInstitutionLinkRequestsAdapter().setRejectLinkRequestClickListener(rejectLinkRequestClickListener());
        recyclerView = binding.institutionNotificationsRecyclerView;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(secondaryViewModel.getInstitutionLinkRequestsAdapter());
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
    @SuppressLint("NotifyDataSetChanged")
    @NonNull
    public ApproveLinkRequestClickListener approveLinkRequestClickListener(){
        return institutionLinkRequest -> {
            primaryViewModel.approveInstitutionLinkRequest(institutionLinkRequest);
            removeInstitutionAfterApproveOrReject(institutionLinkRequest);
        };
    }
    @NonNull
    public RejectLinkRequestClickListener rejectLinkRequestClickListener(){
        return institutionLinkRequest->{
            primaryViewModel.rejectInstitutionLinkRequest(institutionLinkRequest);
            removeInstitutionAfterApproveOrReject(institutionLinkRequest);
        };
    }
    @SuppressLint("NotifyDataSetChanged")
    public void removeInstitutionAfterApproveOrReject(InstitutionLinkRequest institutionLinkRequest){
        Objects.requireNonNull(secondaryViewModel.getInstitutionLinkRequestsAdapter().getInstitutionLinkRequestMutableLiveData().getValue()).removeIf(institutionSelected -> institutionSelected.getId().equals(institutionLinkRequest.getId()));
        secondaryViewModel.getInstitutionLinkRequestsAdapter().notifyDataSetChanged();
    }
    public View.OnClickListener filterButtonClickListener(){
        return this::openFilterOptions;
    }
    private void openFilterOptions(View v) {
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.link_requests_menu_filter, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            return true;
        });
        popupMenu.show();
    }



}