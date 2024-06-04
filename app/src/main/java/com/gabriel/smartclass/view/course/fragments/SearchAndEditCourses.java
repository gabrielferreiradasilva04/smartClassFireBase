package com.gabriel.smartclass.view.course.fragments;

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
import android.widget.EditText;
import android.widget.ImageButton;

import com.gabriel.smartclass.adapter.interfaces.DefaultClickListener;
import com.gabriel.smartclass.databinding.FragmentSearchAndEditCoursesBinding;
import com.gabriel.smartclass.model.Course;
import com.gabriel.smartclass.view.course.dialogs.EditCourseDialog;
import com.gabriel.smartclass.view.course.views.AddSubjectsOnCourse;
import com.gabriel.smartclass.viewModels.SearchAndEditCourseViewModel;
import com.gabriel.smartclass.viewModels.factorys.SearchAndEditCourseFactory;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SearchAndEditCourses extends Fragment {
    private FragmentSearchAndEditCoursesBinding binding;
    private SearchAndEditCourseViewModel viewModel;
    private ImageButton searchButton;
    private EditText title;
    private RecyclerView recyclerView;

    public EditText getTitle() {
        return title;
    }

    public void setTitle(EditText title) {
        this.title = title;
    }

    public SearchAndEditCourses() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchAndEditCoursesBinding.inflate(inflater, container, false);
        initialize();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.getSnackBarText().setValue(null);
        viewModel.getSnackBarText().removeObserver(snackbarObserve());

    }

    public void initialize() {
        loadComponents();
        SearchAndEditCourseFactory factory = new SearchAndEditCourseFactory(this);
        ViewModelProvider provider = new ViewModelProvider(requireActivity(), factory);
        viewModel = provider.get(SearchAndEditCourseViewModel.class);
        buildRecyclerView();
        searchButton.setOnClickListener(searchButtonListener());
        viewModel.getSnackBarText().observe(getViewLifecycleOwner(), snackbarObserve());
    }

    private void loadComponents() {
        this.title = binding.seacrchCoursesEdtxtTitle;
        this.recyclerView = binding.searchCourseRecyclerview;
        this.searchButton = binding.seacrchCoursesButtonsearch;
    }

    public void buildRecyclerView() {
        viewModel.getAdapter().setClickListenerEdit(openEditView());
        viewModel.getAdapter().setClickListenerRemove(removeCourse());
        viewModel.getAdapter().setClickListenerSubjects(openSubjectsDialog());
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(viewModel.getAdapter());
    }

    private Observer<? super String> snackbarObserve() {
        return text -> {
            if (text != null && !text.equals("")) {
                Snackbar.make(binding.seacrchCoursesEdtxtTitle, text, Snackbar.LENGTH_SHORT).show();
            }
        };
    }

    private View.OnClickListener searchButtonListener() {
        return view -> viewModel.searchCoursesAndPopulateAdapter(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(), title.getText().toString());
    }

    private DefaultClickListener<Course> openSubjectsDialog() {
        return this::inflateActivityAddSubjects;
    }

    private DefaultClickListener<Course> openEditView() {
        return this::inflateEditCourseDialog;
    }

    private DefaultClickListener<Course> removeCourse() {
        return course -> viewModel.deleteCourse(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(), course);
    }

    public void inflateEditCourseDialog(Course course) {
        EditCourseDialog dialog = new EditCourseDialog();
        viewModel.getSnackBarText().setValue(null);
        viewModel.setCourseEdit(course);
        dialog.show(getParentFragmentManager(), dialog.getTag());
    }

    public void inflateActivityAddSubjects(Course course) {
        Intent i = new Intent(this.getContext(), AddSubjectsOnCourse.class);
        i.putExtra("courseToEdit", course);
        startActivity(i);
    }

}