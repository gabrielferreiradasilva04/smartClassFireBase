package com.gabriel.smartclass.view.user.fragments.institutionUser;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.adapter.interfaces.DefaultClickListener;
import com.gabriel.smartclass.databinding.EmptyRequestBinding;
import com.gabriel.smartclass.databinding.FragmentCoordinatorCourseMembersBinding;
import com.gabriel.smartclass.model.InstitutionUser;
import com.gabriel.smartclass.observer.EmptyRecyclerViewObserver;
import com.gabriel.smartclass.view.user.views.institutionUser.CoordinatorCourseMainMenu;
import com.gabriel.smartclass.viewModels.CoordinatorCourseViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

@SuppressLint("NotifyDataSetChanged")

public class CoordinatorCourseMembers extends Fragment {
    private FragmentCoordinatorCourseMembersBinding binding;
    private CoordinatorCourseViewModel viewModel;
    private RecyclerView recyclerView;
    private FloatingActionButton menuButton;

    public CoordinatorCourseMembers() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.binding = FragmentCoordinatorCourseMembersBinding.inflate(inflater, container, false);
        initialize();
        return this.binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        emptyview();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.viewModel.getSnackbarText().setValue(null);
        this.viewModel.getSnackbarText().removeObserver(this.snackbarobserve());
    }

    private void initialize() {
        CoordinatorCourseMainMenu parentView = (CoordinatorCourseMainMenu) getActivity();
        parentView.updateTitle("Membros");
        this.loadComponents();
        this.viewModel = new ViewModelProvider(requireActivity()).get(CoordinatorCourseViewModel.class);
        buildRecyclerView();
        this.viewModel.getSnackbarText().observe(this.getViewLifecycleOwner(), this.snackbarobserve());
        this.menuButton.setOnClickListener(this::openMenuOptions);
    }

    private Observer<? super String> snackbarobserve() {
        return text ->{
          if(text != null && !text.equals(""))  {
              Snackbar.make(this.getContext(), binding.coursemembersRecyclerview, text, Snackbar.LENGTH_SHORT).show();
          }
        };
    }

    private void buildRecyclerView() {
        this.viewModel.getMembersAdapter().setClickListenerRemove(this.removeMemberListener());
        this.recyclerView.setHasFixedSize(false);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.recyclerView.setAdapter(this.viewModel.getMembersAdapter());
    }

    private DefaultClickListener<InstitutionUser> removeMemberListener() {
        return institutionUser -> {
          this.viewModel.removeMemberFromCourse(institutionUser);
        };
    }

    private void loadComponents() {
        this.recyclerView = binding.coursemembersRecyclerview;
        this.menuButton = binding.coursemembersButton;
    }

    private void emptyview() {
        EmptyRequestBinding viewEmpty = binding.emptyContainerCoursemembers;
        EmptyRecyclerViewObserver observer = new EmptyRecyclerViewObserver(binding.coursemembersRecyclerview, viewEmpty.getRoot());
        this.viewModel.getMembersAdapter().registerAdapterDataObserver(observer);
        this.viewModel.getMembersAdapter().notifyDataSetChanged();
    }

    private void openMenuOptions(View v) {
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.coordinator_course_addmembers, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            if(item.getItemId() == R.id.addmemebersmenu_teacher){
                AddMemberDialog dialog = new AddMemberDialog();
                this.viewModel.getSnackbarText().setValue(null);
                dialog.show(getParentFragmentManager(), dialog.getTag());
            }
            if(item.getItemId() == R.id.addmemebersmenu_student){
                AddMemberDialog dialog = new AddMemberDialog();
                dialog.setStudents(true);
                this.viewModel.getSnackbarText().setValue(null);
                dialog.show(getParentFragmentManager(), dialog.getTag());
            }
            return true;
        });
        popupMenu.show();
    }
}