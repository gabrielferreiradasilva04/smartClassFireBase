package com.gabriel.smartclass.view.user.fragments.institution;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.gabriel.smartclass.viewModels.InstitutionLinkRequestsFragmentViewModel;
import com.gabriel.smartclass.viewModels.factorys.InstitutionLinkRequestsViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;
import java.util.stream.Stream;

public class InstitutionLinkRequestFragment extends Fragment {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton actionButton;
    private InstitutionLinkRequestsFragmentViewModel viewModel;
    private FragmentInstitutionNotificationsBinding binding;
    public static int actualPendingNotifications;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInstitutionNotificationsBinding.inflate(inflater, container, false);
        initialize();
        return binding.getRoot();
    }
    @Override
    public void onStart() {
        super.onStart();
        emptyRequestView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.getSnackBarText().setValue("");
    }

    private void initialize(){
        loadComponents();
        InstitutionLinkRequestsViewModelFactory factory = new InstitutionLinkRequestsViewModelFactory(this);
        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity(), factory);
        viewModel = viewModelProvider.get(InstitutionLinkRequestsFragmentViewModel.class);
        viewModel.getInstitutionLinkRequests(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(), LinkRequestStatusDAO.PENDING_REFERENCE);
        buildRecyclerView();
        refresh();
        actionButton.setOnClickListener(openMenu());
    }

    private void loadComponents(){
        recyclerView = binding.institutionNotificationsRecyclerView;
        swipeRefreshLayout = binding.institutionNotificationsSwiperefresh;
        actionButton = binding.institutionNoitificationsFilterButton;
    }
    private void buildRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(viewModel.getAdapter());
        viewModel.getAdapter().setApproveLinkRequestClickListener(approveLinkRequest());
        viewModel.getAdapter().setRejectLinkRequestClickListener(rejectLinkRequest());
        verifyRefresh();
    }
    public void refresh(){
        swipeRefreshLayout.setOnRefreshListener(()->{
            viewModel.getInstitutionLinkRequests(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(), LinkRequestStatusDAO.PENDING_REFERENCE);
            buildRecyclerView();
        });
    }
    public void verifyRefresh(){
        if(swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }
    }
    private void openFilterOptions(View v) {
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.link_requests_menu_filter, popupMenu.getMenu());
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if(id == R.id.pendingFilter){
                viewModel.getInstitutionLinkRequests(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(), LinkRequestStatusDAO.PENDING_REFERENCE);
                buildRecyclerView();
                emptyRequestView();
            } else if (id == R.id.approveFilter) {
                viewModel.getInstitutionLinkRequests(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(), LinkRequestStatusDAO.APPROVED_REFERENCE);
                buildRecyclerView();
                emptyRequestView();
            } else if (id == R.id.rejectFilter) {
                viewModel.getInstitutionLinkRequests(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(), LinkRequestStatusDAO.REJECTED_REFERENCE);
                buildRecyclerView();
                emptyRequestView();
            }
            return true;
        });
    }
    private View.OnClickListener openMenu() {
        return this::openFilterOptions;
    }
    @SuppressLint("NotifyDataSetChanged")
    private void emptyRequestView() {
            EmptyRequestBinding viewEmpty = binding.emptyContainerHome;
            EmptyRecyclerViewObserver observer = new EmptyRecyclerViewObserver(binding.institutionNotificationsRecyclerView, viewEmpty.getRoot());
            viewModel.getAdapter().registerAdapterDataObserver(observer);
            viewModel.getAdapter().notifyDataSetChanged();
    }
    private void getPendingNotificationsIndex() {
        Stream<InstitutionLinkRequest> pendingNotifications = viewModel.getAdapter().getInstitutionLinkRequestMutableLiveData().getValue()
                .stream().filter(object->object.getLinkRequestStatus_id().equals(LinkRequestStatusDAO.PENDING_REFERENCE));
        actualPendingNotifications = (int) pendingNotifications.count();
    }
    private ApproveLinkRequestClickListener approveLinkRequest() {
        return linkRequest -> {
            viewModel.approveOrRejectInstitutionLinkRequest(linkRequest, true);
            removeItemFromAdapter(linkRequest);
        };
    }
    private void removeItemFromAdapter(InstitutionLinkRequest linkRequest) {
        for(int i = 0; i < viewModel.getAdapter().getItemCount(); i++){
            if(viewModel.getAdapter().getInstitutionLinkRequestMutableLiveData().getValue().get(i).equals(linkRequest)){
                viewModel.getAdapter().removeItem(linkRequest);
                getPendingNotificationsIndex();
                break;
            }
        }

    }
    private RejectLinkRequestClickListener rejectLinkRequest() {
        return linkRequest -> {
            viewModel.approveOrRejectInstitutionLinkRequest(linkRequest, false);
            removeItemFromAdapter(linkRequest);
        };
    }



}