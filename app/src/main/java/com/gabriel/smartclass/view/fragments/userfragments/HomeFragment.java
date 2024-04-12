package com.gabriel.smartclass.view.fragments.userfragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gabriel.smartclass.adapter.InstitutionsAdapter;
import com.gabriel.smartclass.databinding.EmptyRequestBinding;
import com.gabriel.smartclass.databinding.FragmentHomeBinding;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.observer.EmptyRecyclerViewObserver;
import com.gabriel.smartclass.view.InstitutionsSearch;
import com.gabriel.smartclass.view.StudentMainMenu;
import com.gabriel.smartclass.view.UserInstitutionMenu;
import com.gabriel.smartclass.viewModels.HostStudentActivityViewModel;

public class HomeFragment extends Fragment {
    private HostStudentActivityViewModel hostStudentActivityViewModel;
    private FragmentHomeBinding binding;
    private EmptyRecyclerViewObserver observer;
    private RecyclerView recyclerViewInstitutions;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        StudentMainMenu main = (StudentMainMenu) getActivity();
        main.updateTitle("Instituições");
        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());
        hostStudentActivityViewModel = viewModelProvider.get(HostStudentActivityViewModel.class);
        hostStudentActivityViewModel.getUserInstitutionsAdapter().setItemClickListener(institutionClickListener());
        recyclerViewInstitutions = binding.institutionsRecyclerViewHomeFragment;
        recyclerViewInstitutions.setHasFixedSize(true);
        recyclerViewInstitutions.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewInstitutions.setAdapter(hostStudentActivityViewModel.getUserInstitutionsAdapter());
        binding.buttonAddInstitutionUserInstitutions.setOnClickListener(onInstitutionClick());
        return binding.getRoot();
    }
    @Override
    public void onStart() {
        super.onStart();
        EmptyRequestBinding viewEmpty = binding.emptyContainerHome;
        observer = new EmptyRecyclerViewObserver(recyclerViewInstitutions, viewEmpty.getRoot());
        hostStudentActivityViewModel.getUserInstitutionsAdapter().registerAdapterDataObserver(observer);
        hostStudentActivityViewModel.getUserInstitutionsAdapter().notifyDataSetChanged();
    }
    @NonNull
    private InstitutionsAdapter.ItemClickListener institutionClickListener() {
        return new InstitutionsAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Institution institution) {
                hostStudentActivityViewModel.setSelectedInstitution(institution);
                Intent i = new Intent(getContext(), UserInstitutionMenu.class);
                i.putExtra("institution", institution);
                startActivity(i);
            }
        };
    }

    private View.OnClickListener onInstitutionClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), InstitutionsSearch.class);
                startActivity(i);
            }
        };
    }
}