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
import androidx.lifecycle.MutableLiveData;
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
import com.gabriel.smartclass.view.BaseNotification;
import com.gabriel.smartclass.viewModels.HostUserActivityViewModel;
import com.gabriel.smartclass.viewModels.InstitutionLinkRequestsFragmentViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InstitutionLinkRequestFragment extends Fragment {
    private FragmentInstitutionNotificationsBinding binding;
    private InstitutionLinkRequestsFragmentViewModel primaryViewModel;
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
        primaryViewModel.getLinkRequests(LinkRequestStatusDAO.PENDING_REFERENCE);
        initializeRecyclerView();
        primaryViewModel.getInstitutionLinkRequestsAdapter().getInstitutionLinkRequestMutableLiveData().observe(getViewLifecycleOwner(), linkRequestsObserve());
        binding.institutionNoitificationsFilterButton.setOnClickListener(filterButtonClickListener());
        primaryViewModel.getInstitutionLinkRequestsAdapter().setApproveLinkRequestClickListener(approveLinkRequest());
        primaryViewModel.getInstitutionLinkRequestsAdapter().setRejectLinkRequestClickListener(rejectLinkRequest());
    }

    private RejectLinkRequestClickListener rejectLinkRequest() {
        return linkRequest -> {
            primaryViewModel.approveOrRejectInstitutionLinkRequest(linkRequest, false);
            removeItemFromAdapter(linkRequest);
        };
    }

    private void removeItemFromAdapter(InstitutionLinkRequest linkRequest) {
        int indexRemoved = 0;
        for(int i = 0; i< primaryViewModel.getInstitutionLinkRequestsAdapter().getItemCount(); i++){
            if(primaryViewModel.getInstitutionLinkRequestsAdapter().getInstitutionLinkRequestMutableLiveData().getValue().get(i).equals(linkRequest)){
                indexRemoved = i;
                break;
            }
        }
        primaryViewModel.getInstitutionLinkRequestsAdapter().getInstitutionLinkRequestMutableLiveData().getValue().removeIf(object -> object.equals(linkRequest));
        primaryViewModel.getInstitutionLinkRequestsAdapter().notifyItemRemoved(indexRemoved);
    }

    private ApproveLinkRequestClickListener approveLinkRequest() {
        return linkRequest -> {
            primaryViewModel.approveOrRejectInstitutionLinkRequest(linkRequest, true);
            removeItemFromAdapter(linkRequest);
        };
    }

    private Observer<? super List<InstitutionLinkRequest>> linkRequestsObserve() {
        return (Observer<List<InstitutionLinkRequest>>) institutionLinkRequests -> initializeRecyclerView();
    }

    public void initializeRecyclerView(){
        binding.institutionNotificationsRecyclerView.setHasFixedSize(true);
        binding.institutionNotificationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.institutionNotificationsRecyclerView.setAdapter(primaryViewModel.getInstitutionLinkRequestsAdapter());
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
        primaryViewModel.getInstitutionLinkRequestsAdapter().setInstitutionLinkRequestMutableLiveData(new MutableLiveData<>(new ArrayList<>()));
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void emptyRequestView() {
        EmptyRequestBinding viewEmpty = binding.emptyContainerHome;
        EmptyRecyclerViewObserver observer = new EmptyRecyclerViewObserver(binding.institutionNotificationsRecyclerView, viewEmpty.getRoot());
        primaryViewModel.getInstitutionLinkRequestsAdapter().registerAdapterDataObserver(observer);
        primaryViewModel.getInstitutionLinkRequestsAdapter().notifyDataSetChanged();
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
                primaryViewModel.getLinkRequests(LinkRequestStatusDAO.PENDING_REFERENCE);
            } else if (id == R.id.approveFilter) {
                primaryViewModel.getLinkRequests(LinkRequestStatusDAO.APPROVED_REFERENCE);
            } else if (id == R.id.rejectFilter) {
                primaryViewModel.getLinkRequests(LinkRequestStatusDAO.REJECTED_REFERENCE);
            }
            return true;
        });
    }

    public void refresh(){
        binding.institutionNotificationsSwiperefresh.setOnRefreshListener(() -> {
        });
    }




}