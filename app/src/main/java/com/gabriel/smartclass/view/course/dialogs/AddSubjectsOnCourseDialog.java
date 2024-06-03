package com.gabriel.smartclass.view.course.dialogs;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gabriel.smartclass.adapter.interfaces.DefaultClickListener;
import com.gabriel.smartclass.databinding.DialogAddsubjetsCourseBinding;
import com.gabriel.smartclass.model.Subject;
import com.gabriel.smartclass.viewModels.SearchAndEditCourseViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class AddSubjectsOnCourseDialog extends DialogFragment {
    private DialogAddsubjetsCourseBinding binding;
    private SearchAndEditCourseViewModel viewModel;
    private EditText textDescription;
    private EditText textMinimumGrade;
    private ImageButton addSubjectButton;
    private ImageButton backButton;
    private RecyclerView recyclerView;

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = DialogAddsubjetsCourseBinding.inflate(inflater, container, false);
        initialize();
        return this.binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.getSnackBarText().removeObserver(snackbarObserve());
        viewModel.getSnackBarText().setValue(null);
        recyclerView.setAdapter(null);
        this.addSubjectButton.setOnClickListener(null);
        this.backButton.setOnClickListener(null);
        this.viewModel = null;
    }

    public void loadComponents(){
        this.textDescription = binding.addCourseAddSubjectEdtxtDescription;
        this.textMinimumGrade = binding.addCourseAddSubjectEdtxtMinimumGrade;
        this.addSubjectButton = binding.addCourseAddSubjectButton;
        this.backButton = binding.addCourseAddSubjectCancel;
        this.recyclerView = binding.recyclerViewCourseSubjects;
    }
    public void initialize(){
        loadComponents();
        ViewModelProvider provider = new ViewModelProvider(requireActivity());
        this.viewModel = provider.get(SearchAndEditCourseViewModel.class);
        viewModel.initSubjectAdapter(FirebaseAuth.getInstance().getCurrentUser().getUid());
        buildRecyclerView();
        viewModel.getSnackBarText().observe(getViewLifecycleOwner(), snackbarObserve());
        addSubjectButton.setOnClickListener(addSubjectButtonListener());
        backButton.setOnClickListener(backButtonListener());

    }
    private View.OnClickListener backButtonListener() {
        return view ->{
            this.dismiss();
        };
    }
    public void buildRecyclerView(){
        this.recyclerView.setHasFixedSize(false);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.recyclerView.setAdapter(viewModel.getSubjectRCAdapter());
        viewModel.getSubjectRCAdapter().setClickListener(removeSubjectListener());
    }
    private DefaultClickListener<Subject> removeSubjectListener() {
        return subject ->{
            viewModel.removeSubjectFromCourse(FirebaseAuth.getInstance().getCurrentUser().getUid(), viewModel.getCourseEdit().getId(), subject);
        };
    }
    private View.OnClickListener addSubjectButtonListener() {
        return view ->{
            String description  = textDescription.getText().toString();
            String minimumGrade = textMinimumGrade.getText().toString();
            viewModel.addNewSubjectOnCourse(FirebaseAuth.getInstance().getCurrentUser().getUid(),description, minimumGrade);
        };
    }
    private Observer<? super String> snackbarObserve() {
        return text ->{
            if(text != null && !text.equals("")) {
                Snackbar.make(addSubjectButton, text, Snackbar.LENGTH_SHORT).show();
            }
        };
    }
}
