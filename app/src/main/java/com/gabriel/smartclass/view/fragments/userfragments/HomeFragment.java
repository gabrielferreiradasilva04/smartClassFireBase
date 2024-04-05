package com.gabriel.smartclass.view.fragments.userfragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gabriel.smartclass.adapter.InstitutionsAdapter;
import com.gabriel.smartclass.databinding.EmptyRequestBinding;
import com.gabriel.smartclass.databinding.FragmentHomeBinding;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.observer.EmptyRecyclerViewObserver;
import com.gabriel.smartclass.view.auth.InstitutionsSearch;
import com.gabriel.smartclass.view.UserInstitutionMenu;
import com.gabriel.smartclass.viewModels.HostStudentActivityViewModel;

public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private HostStudentActivityViewModel hostStudentActivityViewModel;
    private FragmentHomeBinding binding;
    private EmptyRecyclerViewObserver observer;
    private RecyclerView recyclerViewInstitutions;

    public HomeFragment() {
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        hostStudentActivityViewModel = (HostStudentActivityViewModel) requireActivity().getViewModelStore().get("hostStudentActivityViewModel");
        hostStudentActivityViewModel.getUserInstitutionsAdapter().setItemClickListener(institutionClickListener());
        recyclerViewInstitutions = binding.institutionsRecyclerViewHomeFragment;
        recyclerViewInstitutions.setHasFixedSize(true);
        recyclerViewInstitutions.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewInstitutions.setAdapter(hostStudentActivityViewModel.getUserInstitutionsAdapter());
        binding.buttonAddInstitutionUserInstitutions.setOnClickListener(onInstitutionClick());
        refresh();
        return binding.getRoot();
    }
    @SuppressLint({"RestrictedApi", "NotifyDataSetChanged"})
    @Override
    public void onStart() {
        super.onStart();
        EmptyRequestBinding viewEmpty = binding.emptyContainerHome;
        observer = new EmptyRecyclerViewObserver(recyclerViewInstitutions, viewEmpty.getRoot());
        hostStudentActivityViewModel.getUserInstitutionsAdapter().registerAdapterDataObserver(observer);
        hostStudentActivityViewModel.getUserInstitutionsAdapter().notifyDataSetChanged();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @SuppressLint("NotifyDataSetChanged")
    private void update() {
            hostStudentActivityViewModel.getUserInstitutions();
            hostStudentActivityViewModel.getUserInstitutionsAdapter().setItemClickListener(institutionClickListener());
            hostStudentActivityViewModel.getUserInstitutionsAdapter().notifyDataSetChanged();
            recyclerViewInstitutions.setAdapter(hostStudentActivityViewModel.getUserInstitutionsAdapter());
            recyclerViewInstitutions.getAdapter().registerAdapterDataObserver(observer);
            if(binding.swipeRefreshHomeFragment.isRefreshing()){
                binding.swipeRefreshHomeFragment.setRefreshing(false);
            }
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

    public void refresh(){
        binding.swipeRefreshHomeFragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onRefresh() {
                update();
            }
        });
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