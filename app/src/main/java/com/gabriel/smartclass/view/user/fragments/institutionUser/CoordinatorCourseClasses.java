package com.gabriel.smartclass.view.user.fragments.institutionUser;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gabriel.smartclass.adapter.interfaces.DefaultClickListener;
import com.gabriel.smartclass.databinding.EmptyRequestBinding;
import com.gabriel.smartclass.databinding.FragmentCoordinatorCourseClassesBinding;
import com.gabriel.smartclass.model.Classroom;
import com.gabriel.smartclass.observer.EmptyRecyclerViewObserver;
import com.gabriel.smartclass.view.course.views.TimeTableView;
import com.gabriel.smartclass.view.user.views.institutionUser.CoordinatorCourseMainMenu;
import com.gabriel.smartclass.view.user.views.institutionUser.CreateClassroom;
import com.gabriel.smartclass.viewModels.CoordinatorCourseViewModel;

public class CoordinatorCourseClasses extends Fragment {
    private FragmentCoordinatorCourseClassesBinding binding;
    private CoordinatorCourseViewModel viewModel;

    public CoordinatorCourseClasses() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentCoordinatorCourseClassesBinding.inflate(inflater, container, false);
        this.initialize();
        return this.binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        this.emptyRequestView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.viewModel.getSnackbarText().setValue("");
    }

    public void initialize(){
        this.buildMenu();
        this.buttonListeners();
        this.getViewModel();
        this.buildRecyclerView();
        this.refresh();
    }

    private void buildRecyclerView() {
        binding.recyclerviewclasses.setHasFixedSize(false);
        binding.recyclerviewclasses.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerviewclasses.setAdapter(viewModel.getClassroomAdapter());
        this.viewModel.getClassroomAdapter().setClickListenerTimeTable(this.timeTableClickListener());
    }

    private DefaultClickListener<Classroom> timeTableClickListener() {
        return this::callTimeTableView;
    }

    private void callTimeTableView(Classroom classroom) {
        Intent i = new Intent(this.getActivity(), TimeTableView.class);
        i.putExtra("institution", this.viewModel.getInstitution());
        i.putExtra("course", this.viewModel.getCourse());
        i.putExtra("classroom", classroom);
        startActivity(i);
    }

    private void buildMenu() {
        CoordinatorCourseMainMenu main = (CoordinatorCourseMainMenu) this.getActivity();
        assert main != null;
        main.updateTitle("Classes");
    }
    private void buttonListeners(){
        binding.buttonadd.setOnClickListener(view ->{
            Intent i = new Intent(this.getContext(), CreateClassroom.class);
            i.putExtra("institution", this.viewModel.getInstitution());
            i.putExtra("course", this.viewModel.getCourse());
            startActivity(i);
        });
    }
    private void getViewModel(){
        this.viewModel = new ViewModelProvider(requireActivity()).get(CoordinatorCourseViewModel.class);

    }
    @SuppressLint("NotifyDataSetChanged")
    private void emptyRequestView() {
        EmptyRequestBinding viewEmpty = binding.emptyContainerClasses;
        EmptyRecyclerViewObserver observer = new EmptyRecyclerViewObserver(binding.recyclerviewclasses, viewEmpty.getRoot());
        viewModel.getClassroomAdapter().registerAdapterDataObserver(observer);
        viewModel.getClassroomAdapter().notifyDataSetChanged();
    }
    public void refresh(){
        binding.refreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.getClassrooms();
                binding.refreshlayout.setRefreshing(false);
            }
        });
    }

}