package com.gabriel.smartclass.view.fragments.userfragments;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gabriel.smartclass.adapter.InstitutionsAdapter;
import com.gabriel.smartclass.databinding.EmptyRequestBinding;
import com.gabriel.smartclass.databinding.FragmentHomeBinding;
import com.gabriel.smartclass.observer.EmptyRecyclerViewObserver;
import com.gabriel.smartclass.view.InstitutionsSearch;
import com.gabriel.smartclass.view.StudentMainMenu;
import com.gabriel.smartclass.view.UserInstitutionMenu;
import com.gabriel.smartclass.viewModels.HostUserActivityViewModel;
import com.gabriel.smartclass.adapter.interfaces.OnInstitutionItemClickListener;


public class HomeFragment extends Fragment {
    private HostUserActivityViewModel hostUserActivityViewModel;
    private FragmentHomeBinding binding;
    private EmptyRecyclerViewObserver observer;
    private RecyclerView recyclerViewInstitutions;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        StudentMainMenu main = (StudentMainMenu) getActivity();
        if (main != null) {
            main.updateTitle("Instituições");
        }
        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());
        hostUserActivityViewModel = viewModelProvider.get(HostUserActivityViewModel.class);
        loadUserInstitutions();
        refresh();
        binding.buttonAddInstitutionUserInstitutions.setOnClickListener(onInstitutionClick());
        return binding.getRoot();
    }

    private void loadUserInstitutions() {
        hostUserActivityViewModel.getUserInstitutionsAdapter().setItemClickListener(institutionClickListener());
        recyclerViewInstitutions = binding.institutionsRecyclerViewHomeFragment;
        recyclerViewInstitutions.setHasFixedSize(true);
        recyclerViewInstitutions.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewInstitutions.setAdapter(hostUserActivityViewModel.getUserInstitutionsAdapter());
        if(binding.swipeRefreshUserHome.isRefreshing()){
            binding.swipeRefreshUserHome.setRefreshing(false);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onStart() {
        super.onStart();
        EmptyRequestBinding viewEmpty = binding.emptyContainerHome;
        observer = new EmptyRecyclerViewObserver(recyclerViewInstitutions, viewEmpty.getRoot());
        hostUserActivityViewModel.getUserInstitutionsAdapter().registerAdapterDataObserver(observer);
        hostUserActivityViewModel.getUserInstitutionsAdapter().notifyDataSetChanged();
    }
    @NonNull
    private OnInstitutionItemClickListener institutionClickListener() {
        return institution -> {
            Intent i = new Intent(requireActivity(), UserInstitutionMenu.class);
            i.putExtra("institution", institution);
            startActivity(i);
        };
    }

    private View.OnClickListener onInstitutionClick() {
        return v -> {
            Intent i = new Intent(requireActivity(), InstitutionsSearch.class);
            startActivity(i);
        };
    }
    public void refresh(){
        binding.swipeRefreshUserHome.setOnRefreshListener(() -> {
            hostUserActivityViewModel.getUserInstitutions();
            loadUserInstitutions();
        });

    }
}