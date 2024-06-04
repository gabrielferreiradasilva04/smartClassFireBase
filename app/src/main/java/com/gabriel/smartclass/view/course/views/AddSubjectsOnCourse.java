package com.gabriel.smartclass.view.course.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.adapter.interfaces.DefaultClickListener;
import com.gabriel.smartclass.databinding.ActivityAddSubjectsOnCourseBinding;
import com.gabriel.smartclass.model.Course;
import com.gabriel.smartclass.model.Subject;
import com.gabriel.smartclass.viewModels.AddSubjectsOnCourseViewModel;
import com.gabriel.smartclass.viewModels.factorys.AddSubjectsOnCourseViewModelFactory;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class AddSubjectsOnCourse extends AppCompatActivity {
    private ActivityAddSubjectsOnCourseBinding binding;
    private AddSubjectsOnCourseViewModel viewModel;
    private EditText textDescription;
    private EditText minimumGrade;
    private ImageButton saveButton;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddSubjectsOnCourseBinding.inflate(getLayoutInflater());
        initialize();
        setContentView(binding.getRoot());
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.getSnackBarText().removeObserver(snackbarObserve());
        this.recyclerView.setAdapter(null);
        this.saveButton.setOnClickListener(null);
        viewModel.getSnackBarText().setValue(null);
    }
    public void loadComponents() {
        this.textDescription = binding.addsubjectsOncourseDescription;
        this.minimumGrade = binding.addsubjectsOncourseMinimumgrade;
        this.saveButton = binding.addsubjectsOncourseSaveButton;
        this.recyclerView = binding.addsubjectsOncourseRecyclerview;
    }
    public void initialize(){
        buildMenu();
        loadComponents();
        buildViewModel();
        viewModel.initSubjectAdapter(FirebaseAuth.getInstance().getCurrentUser().getUid());
        buildRecyclerview();
        viewModel.getSnackBarText().observe(this, snackbarObserve());
        this.saveButton.setOnClickListener(saveButtonClickListener());
    }

    private void buildMenu() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Matérias");
    }

    private void buildViewModel() {
        AddSubjectsOnCourseViewModelFactory factory = new AddSubjectsOnCourseViewModelFactory(this);
        ViewModelProvider provider = new ViewModelProvider(this, factory);
        this.viewModel = provider.get(AddSubjectsOnCourseViewModel.class);
        viewModel.setCourseEdit(getIntent().getParcelableExtra("courseToEdit"));
    }

    private void addNewSubject() {
        String descriptiontxt = this.textDescription.getText().toString();
        String minimumgradetxt = this.minimumGrade.getText().toString();
        viewModel.addNewSubjectOnCourse(FirebaseAuth.getInstance().getCurrentUser().getUid(), descriptiontxt, minimumgradetxt);
    }

    public void buildRecyclerview(){
        viewModel.getAdapter().setClickListener(adapterClickListener());
        this.recyclerView.setHasFixedSize(false);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        this.recyclerView.setAdapter(viewModel.getAdapter());
        Log.d("RETORNO NO RC", "buildRecyclerview: "+ recyclerView.getAdapter().getItemCount());
    }

    private Observer<? super String> snackbarObserve() {
        return text ->{
            if(text != null && !text.equals("")){
                Snackbar.make(binding.addsubjectsOncourseRecyclerview, text, Snackbar.LENGTH_SHORT).show();
            }
        };
    }
    private DefaultClickListener<Subject> adapterClickListener() {
        return subject ->{
          viewModel.removeSubjectFromCourse(FirebaseAuth.getInstance().getCurrentUser().getUid(), viewModel.getCourseEdit().getId(), subject);
        };
    }
    private View.OnClickListener saveButtonClickListener() {
        return view ->{addNewSubject();};
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_action_bar, menu);
        return true;
    }
}