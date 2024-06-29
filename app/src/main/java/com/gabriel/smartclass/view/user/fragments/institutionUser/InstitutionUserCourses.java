package com.gabriel.smartclass.view.user.fragments.institutionUser;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gabriel.smartclass.databinding.EmptyRequestBinding;
import com.gabriel.smartclass.databinding.FragmentInstitutionUserCoursesBinding;
import com.gabriel.smartclass.observer.EmptyRecyclerViewObserver;
import com.gabriel.smartclass.view.user.views.institutionUser.InstitutionUserMainMenu;
import com.gabriel.smartclass.viewModels.InstitutionUserMainMenuViewModel;

@SuppressLint("NotifyDataSetChanged")
public class InstitutionUserCourses extends Fragment {
    private FragmentInstitutionUserCoursesBinding binding;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private InstitutionUserMainMenuViewModel viewModel;

    public InstitutionUserCourses() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInstitutionUserCoursesBinding.inflate(inflater, container, false);
        this.initialize();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        this.emptyview();
    }

    private void emptyview() {
        EmptyRequestBinding viewEmpty = binding.institutionuserCoursesEmptyview;
        EmptyRecyclerViewObserver observer = new EmptyRecyclerViewObserver(binding.institutionuserCoursesRecyclerview, viewEmpty.getRoot());
        this.viewModel.getCourseAdapter().registerAdapterDataObserver(observer);
        this.viewModel.getCourseAdapter().notifyDataSetChanged();
    }

    public void initialize(){
        this.buildMenu();
        this.loadComponents();
        this.viewModel = new ViewModelProvider(requireActivity()).get(InstitutionUserMainMenuViewModel.class);
        this.buildRecyclerView();
        refresh();
    }

    private void refresh() {
        this.refreshLayout.setOnRefreshListener(() -> {
            viewModel.loadUserCourses(viewModel.getUserTypeByInstitutionUser().getValue().getDescription());
            if(this.refreshLayout.isRefreshing()){
                this.refreshLayout.setRefreshing(false);
            }
        });
    }

    public void buildMenu(){
        InstitutionUserMainMenu main = (InstitutionUserMainMenu) getActivity();
        if (main != null) {
            main.updateTitle("Meus Cursos");
        }
    }
    public void buildRecyclerView(){
        this.recyclerView.setHasFixedSize(false);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.recyclerView.setAdapter(viewModel.getCourseAdapter());
    }

    public void loadComponents(){
        this.recyclerView = binding.institutionuserCoursesRecyclerview;
        this.refreshLayout = binding.institutionuserCoursesRefreshlayout;
    }
}