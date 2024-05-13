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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.adapter.interfaces.ApproveLinkRequestClickListener;
import com.gabriel.smartclass.adapter.interfaces.RejectLinkRequestClickListener;
import com.gabriel.smartclass.dao.LinkRequestStatusDAO;
import com.gabriel.smartclass.databinding.EmptyRequestBinding;
import com.gabriel.smartclass.databinding.FragmentInstitutionNotificationsBinding;
import com.gabriel.smartclass.model.InstitutionLinkRequest;
import com.gabriel.smartclass.observer.EmptyRecyclerViewObserver;
import com.gabriel.smartclass.viewModels.HostUserActivityViewModel;
import com.gabriel.smartclass.viewModels.InstitutionLinkRequestsFragmentViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;

import java.util.Objects;

public class InstitutionLinkRequestFragment extends Fragment {
    private FragmentInstitutionNotificationsBinding binding;
    private InstitutionLinkRequestsFragmentViewModel primaryViewModel;
    private RecyclerView recyclerView;
    public InstitutionLinkRequestFragment() {
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInstitutionNotificationsBinding.inflate(inflater, container, false);
        initialize();
        return binding.getRoot();
    }

    private void initialize() {
        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());
        primaryViewModel = viewModelProvider.get(InstitutionLinkRequestsFragmentViewModel.class);
        primaryViewModel.getSnackBarText().observe(getViewLifecycleOwner(), observeSnackbar());
        primaryViewModel.loadInstitutionLinkRequests(LinkRequestStatusDAO.PENDING_REFERENCE);
        binding.institutionNoitificationsFilterButton.setOnClickListener(filterButtonClickListener());
        loadLinkRequests(null);
        refresh();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onStart() {
        super.onStart();
        emptyRequestView();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        primaryViewModel.getSnackBarText().removeObserver(observeSnackbar());
        primaryViewModel.getSnackBarText().setValue(null);
    }


    private void emptyRequestView() {
        EmptyRequestBinding viewEmpty = binding.emptyContainerHome;
        EmptyRecyclerViewObserver observer = new EmptyRecyclerViewObserver(recyclerView, viewEmpty.getRoot());
        primaryViewModel.getInstitutionLinkRequestsAdapter().registerAdapterDataObserver(observer);
        primaryViewModel.getInstitutionLinkRequestsAdapter().notifyDataSetChanged();
    }

    public void loadLinkRequests(SwipeRefreshLayout refreshLayout){
        primaryViewModel.getInstitutionLinkRequestsAdapter().setApproveLinkRequestClickListener(approveLinkRequestClickListener());
        primaryViewModel.getInstitutionLinkRequestsAdapter().setRejectLinkRequestClickListener(rejectLinkRequestClickListener());
        recyclerView = binding.institutionNotificationsRecyclerView;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(primaryViewModel.getInstitutionLinkRequestsAdapter());
        emptyRequestView();
        if(refreshLayout!= null && refreshLayout.isRefreshing()){
            refreshLayout.setRefreshing(false);
        }
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
        Objects.requireNonNull(primaryViewModel.getInstitutionLinkRequestsAdapter().getInstitutionLinkRequestMutableLiveData().getValue()).removeIf(institutionSelected -> institutionSelected.getId().equals(institutionLinkRequest.getId()));
        primaryViewModel.getInstitutionLinkRequestsAdapter().notifyDataSetChanged();
    }
    public View.OnClickListener filterButtonClickListener(){
        return this::openFilterOptions;
    }
    private void openFilterOptions(View v) {
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.link_requests_menu_filter, popupMenu.getMenu());
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if(id == R.id.pendingFilter){
                primaryViewModel.loadInstitutionLinkRequests(LinkRequestStatusDAO.PENDING_REFERENCE);
            } else if (id == R.id.approveFilter) {
                primaryViewModel.loadInstitutionLinkRequests(LinkRequestStatusDAO.APPROVED_REFERENCE);
            } else if (id == R.id.rejectFilter) {
                primaryViewModel.loadInstitutionLinkRequests(LinkRequestStatusDAO.REJECTED_REFERENCE);
            }
            loadLinkRequests(null);
            return true;
        });
    }
    public void refresh(){
        binding.institutionNotificationsSwiperefresh.setOnRefreshListener(() -> {
            loadLinkRequests(binding.institutionNotificationsSwiperefresh);
        });

    }



}