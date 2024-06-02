package com.gabriel.smartclass.view.course.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.adapter.interfaces.DefaultClickListener;
import com.gabriel.smartclass.databinding.FragmentSearchAndEditCoursesBinding;
import com.gabriel.smartclass.model.Area;
import com.gabriel.smartclass.model.Course;
import com.gabriel.smartclass.model.InstitutionUser;
import com.gabriel.smartclass.view.course.dialogs.EditCourseDialog;
import com.gabriel.smartclass.viewModels.SearchAndEditCourseViewModel;
import com.gabriel.smartclass.viewModels.factorys.SearchAndEditCourseFactory;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

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

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public SearchAndEditCourses() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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

    public void initialize(){
        loadComponents();
        SearchAndEditCourseFactory factory = new SearchAndEditCourseFactory(this);
        ViewModelProvider provider = new ViewModelProvider(requireActivity(), factory);
        viewModel = provider.get(SearchAndEditCourseViewModel.class);
        buildRecyclerView();
        searchButton.setOnClickListener(searchButtonListener());
        viewModel.getSnackBarText().observe(getViewLifecycleOwner(), snackbarObserve());
    }
    private void loadComponents(){
        this.title = binding.seacrchCoursesEdtxtTitle;
        this.recyclerView = binding.searchCourseRecyclerview;
        this.searchButton = binding.seacrchCoursesButtonsearch;
    }
    public void buildRecyclerView(){
        viewModel.getAdapter().setClickListenerEdit(openEditView());
        viewModel.getAdapter().setClickListenerRemove(removeCourse());
        viewModel.getAdapter().setClickListenerSubjects(openSubjectsDialog());
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(viewModel.getAdapter());
    }
    private Observer<? super String> snackbarObserve() {
        return text ->{
          if(text != null && !text.equals("")){
              Snackbar.make(binding.seacrchCoursesEdtxtTitle, text, Snackbar.LENGTH_SHORT).show();
          }
        };
    }
    private View.OnClickListener searchButtonListener() {
        return view ->{
                viewModel.searchCoursesAndPopulateAdapter(FirebaseAuth.getInstance().getCurrentUser().getUid(), title.getText().toString());
        };
    }
    private DefaultClickListener<Course> openSubjectsDialog(){return this::inflateSubjectsDialog;}
    private DefaultClickListener<Course> openEditView(){return this::inflateEditCourseDialog;}
    private DefaultClickListener<Course> removeCourse(){
        return course -> {
            viewModel.deleteCourse(FirebaseAuth.getInstance().getCurrentUser().getUid(), course);
        };
    }
    public void inflateEditCourseDialog(Course course) {
        EditCourseDialog dialog = new EditCourseDialog();
        viewModel.getSnackBarText().setValue(null);
        viewModel.setCourseEdit(course);
        dialog.show(getParentFragmentManager(), dialog.getTag());
    }
    public void inflateSubjectsDialog(Course course){

    }

}