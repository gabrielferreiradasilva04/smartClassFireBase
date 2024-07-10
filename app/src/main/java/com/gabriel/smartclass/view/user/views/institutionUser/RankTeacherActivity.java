package com.gabriel.smartclass.view.user.views.institutionUser;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gabriel.smartclass.databinding.ActivityRankTeacherBinding;
import com.gabriel.smartclass.viewModels.RankTeacherViewModel;

import java.util.Objects;

public class RankTeacherActivity extends AppCompatActivity {
    private ActivityRankTeacherBinding binding;
    private RankTeacherViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityRankTeacherBinding.inflate(getLayoutInflater());
        this.buildViewModel();
        this.buildMenu();
        this.buildRecyclerView();
        setContentView(this.binding.getRoot());
    }
    public void buildViewModel(){
        this.viewModel = new ViewModelProvider(this).get(RankTeacherViewModel.class);
        this.viewModel.setInstitution(this.getIntent().getParcelableExtra("institution"));
        this.viewModel.setClassroom(this.getIntent().getParcelableExtra("classroom"));
        this.viewModel.setInstitutionUser(this.getIntent().getParcelableExtra("institutionUser"));
        this.viewModel.setCourse(this.getIntent().getParcelableExtra("course"));
        this.viewModel.buildRank();
    }
    public void buildRecyclerView(){
        this.binding.rcTeacherRanking.setHasFixedSize(false);
        this.binding.rcTeacherRanking.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        this.binding.rcTeacherRanking.setAdapter(this.viewModel.getRankAdapter());
    }
    private void buildMenu() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Ranking");
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}