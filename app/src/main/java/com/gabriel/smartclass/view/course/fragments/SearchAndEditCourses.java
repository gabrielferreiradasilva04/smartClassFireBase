package com.gabriel.smartclass.view.course.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.databinding.FragmentSearchAndEditCoursesBinding;
import com.gabriel.smartclass.viewModels.SearchAndEditCourseViewModel;
import com.gabriel.smartclass.viewModels.factorys.SearchAndEditCourseFactory;
import com.google.firebase.auth.FirebaseAuth;

public class SearchAndEditCourses extends Fragment {
    private FragmentSearchAndEditCoursesBinding binding;
    private SearchAndEditCourseViewModel viewModel;
    private ImageButton searchButton;
    private EditText title;
    private RecyclerView recyclerView;

    public ImageButton getSearchButton() {
        return searchButton;
    }

    public void setSearchButton(ImageButton searchButton) {
        this.searchButton = searchButton;
    }

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
    public void initialize(){
        loadComponents();
        SearchAndEditCourseFactory factory = new SearchAndEditCourseFactory(this);
        ViewModelProvider provider = new ViewModelProvider(requireActivity(), factory);
        viewModel = provider.get(SearchAndEditCourseViewModel.class);
        buildRecyclerView();
        searchButton.setOnClickListener(searchButtonListener());
    }

    private View.OnClickListener searchButtonListener() {
        return view ->{
            if(!title.getText().toString().equals("")){
                viewModel.searchCoursesAndPopulateAdapter(FirebaseAuth.getInstance().getCurrentUser().getUid(), title.getText().toString());
            }else{
                Toast.makeText(getContext(), "Preencha o campo de pesquisa", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void loadComponents(){
        this.title = binding.seacrchCoursesEdtxtTitle;
        this.recyclerView = binding.searchCourseRecyclerview;
        this.searchButton = binding.seacrchCoursesButtonsearch;
    }
    public void buildRecyclerView(){
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(viewModel.getAdapter());
    }

}