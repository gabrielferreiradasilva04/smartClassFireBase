package com.gabriel.smartclass.view.fragments.userfragments;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gabriel.smartclass.dao.InstitutionUserDAO;
import com.gabriel.smartclass.databinding.EmptyRequestBinding;
import com.gabriel.smartclass.databinding.FragmentHomeBinding;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.InstitutionUser;
import com.gabriel.smartclass.observer.EmptyRecyclerViewObserver;
import com.gabriel.smartclass.view.InstitutionsSearch;
import com.gabriel.smartclass.view.StudentMainMenu;
import com.gabriel.smartclass.view.UserInstitutionMenu;
import com.gabriel.smartclass.viewModels.HostUserActivityViewModel;
import com.gabriel.smartclass.adapter.interfaces.OnInstitutionItemClickListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;


public class HomeFragment extends Fragment {
    private HostUserActivityViewModel viewModel;
    private FragmentHomeBinding binding;
    private RecyclerView recyclerViewInstitutions;
    private Institution selectedInstitution;


    public HomeFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        initialize();
        binding.buttonAddInstitutionUserInstitutions.setOnClickListener(searchForInstitutions());
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.getInstitutionUserMutableLiveData().removeObserver(institutionUserObserver());
        viewModel.getInstitutionUserMutableLiveData().setValue(null);
        viewModel.getSnackBarText().removeObserver(snackbarObserver());
        viewModel.getSnackBarText().setValue(null);
    }

    private void initialize() {
        StudentMainMenu main = (StudentMainMenu) getActivity();
        if (main != null) {
            main.updateTitle("Instituições");
        }
        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());
        viewModel = viewModelProvider.get(HostUserActivityViewModel.class);
        viewModel.getInstitutionUserMutableLiveData().observe(getViewLifecycleOwner(), institutionUserObserver());
        viewModel.getSnackBarText().observe(getViewLifecycleOwner(), snackbarObserver());
        loadUserInstitutions();
        refresh();
    }

    private Observer<? super String> snackbarObserver() {
        return text ->{
            if(text != null){
                Snackbar.make(binding.institutionsRecyclerViewHomeFragment, text, Snackbar.LENGTH_SHORT).show();
            }
        };
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
        EmptyRequestBinding viewEmpty = binding.emptyContainerHome;
        EmptyRecyclerViewObserver observer = new EmptyRecyclerViewObserver(recyclerViewInstitutions, viewEmpty.getRoot());
        viewModel.getUserInstitutionsAdapter().registerAdapterDataObserver(observer);
        viewModel.getUserInstitutionsAdapter().notifyDataSetChanged();
    }
    @NonNull
    private OnInstitutionItemClickListener institutionClickListener() {
        return this::verirfyUserAccess;
    }

    private void verirfyUserAccess(Institution institution) {
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
        Intent i = new Intent(requireActivity(), UserInstitutionMenu.class);
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