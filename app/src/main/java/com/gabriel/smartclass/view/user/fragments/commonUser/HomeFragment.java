package com.gabriel.smartclass.view.user.fragments.commonUser;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gabriel.smartclass.adapter.interfaces.OnInstitutionItemClickListener;
import com.gabriel.smartclass.dao.InstitutionUserDAO;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.InstitutionUser;
import com.gabriel.smartclass.observer.EmptyRecyclerViewObserver;
import com.gabriel.smartclass.view.user.views.commonUser.CommonUserMainMenu;
import com.gabriel.smartclass.view.user.views.institution.InstitutionsSearch;
import com.gabriel.smartclass.view.user.views.institutionUser.InstitutionUserMainMenu;
import com.gabriel.smartclass.viewModels.HostUserActivityViewModel;
import com.google.firebase.auth.FirebaseAuth;


public class HomeFragment extends Fragment {
    private HostUserActivityViewModel viewModel;
    private com.gabriel.smartclass.databinding.FragmentHomeBinding binding;
    private RecyclerView recyclerViewInstitutions;
    private Institution selectedInstitution;


    public HomeFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = com.gabriel.smartclass.databinding.FragmentHomeBinding.inflate(inflater, container, false);
        initialize();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.getInstitutionUserMutableLiveData().removeObserver(institutionUserObserver());
        viewModel.getInstitutionUserMutableLiveData().setValue(null);
        viewModel.getSnackBarText().setValue("");
    }

    private void initialize() {
        CommonUserMainMenu main = (CommonUserMainMenu) getActivity();
        if (main != null) {
            main.updateTitle("Instituições");
        }
        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());
        viewModel = viewModelProvider.get(HostUserActivityViewModel.class);
        viewModel.getInstitutionUserMutableLiveData().observe(getViewLifecycleOwner(), institutionUserObserver());
        binding.buttonAddInstitutionUserInstitutions.setOnClickListener(searchForInstitutions());
        loadUserInstitutions();
        refresh();
    }
    private void loadUserInstitutions() {
        viewModel.getUserInstitutionsAdapter().setItemClickListener(institutionClickListener());
        recyclerViewInstitutions = binding.institutionsRecyclerViewHomeFragment;
        recyclerViewInstitutions.setHasFixedSize(true);
        recyclerViewInstitutions.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewInstitutions.setAdapter(viewModel.getUserInstitutionsAdapter());
        if(binding.swipeRefreshUserHome.isRefreshing()){
            binding.swipeRefreshUserHome.setRefreshing(false);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onStart() {
        super.onStart();
        com.gabriel.smartclass.databinding.EmptyRequestBinding viewEmpty = binding.emptyContainerHome;
        EmptyRecyclerViewObserver observer = new EmptyRecyclerViewObserver(recyclerViewInstitutions, viewEmpty.getRoot());
        viewModel.getUserInstitutionsAdapter().registerAdapterDataObserver(observer);
        viewModel.getUserInstitutionsAdapter().notifyDataSetChanged();
    }
    @NonNull
    private OnInstitutionItemClickListener institutionClickListener() {
        return this::verifyAccess;
    }

    private void verifyAccess(Institution institution) {
        assert FirebaseAuth.getInstance().getCurrentUser() != null;
        this.selectedInstitution = institution;
        viewModel.getInstitutionUserById(FirebaseAuth.getInstance().getCurrentUser().getUid(), institution.getId());
    }
    private Observer<? super InstitutionUser> institutionUserObserver() {
        return institutionUser -> {
            if (institutionUser != null) {
                if(InstitutionUserDAO.verifyUserAccess(institutionUser)){
                    institutionUserAccess(institutionUser);
                }else{
                    Toast.makeText(getContext(), "Algo deu errado...", Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    private void institutionUserAccess(InstitutionUser institutionUser) {
        Intent i = new Intent(requireActivity(), InstitutionUserMainMenu.class);
        Log.d("TAG", "institutionUserAccess: "+ institutionUser.getUser_id());
        i.putExtra("institution", selectedInstitution);
        i.putExtra("institutionUser", institutionUser);
        startActivity(i);
    }

    private View.OnClickListener searchForInstitutions() {
        return v -> {
            Intent i = new Intent(requireActivity(), InstitutionsSearch.class);
            startActivity(i);
        };
    }
    public void refresh(){
        binding.swipeRefreshUserHome.setOnRefreshListener(() -> {
            viewModel.getUserInstitutions();
            loadUserInstitutions();
        });
    }
}