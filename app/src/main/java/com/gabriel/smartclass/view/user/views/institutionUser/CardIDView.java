package com.gabriel.smartclass.view.user.views.institutionUser;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.gabriel.smartclass.R;
import com.gabriel.smartclass.databinding.ActivityCardIdviewBinding;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.User;
import com.gabriel.smartclass.viewModels.CardIDViewModel;

import java.util.Objects;

public class CardIDView extends AppCompatActivity {
    private ActivityCardIdviewBinding binding;
    private CardIDViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityCardIdviewBinding.inflate(getLayoutInflater());
        this.buildViewModel();
        this.buildMenu();
        setContentView(this.binding.getRoot());
    }

    public void buildViewModel() {
        this.viewModel = new ViewModelProvider(this).get(CardIDViewModel.class);
        this.viewModel.getInstitution().setValue(this.getIntent().getParcelableExtra("institution"));
        this.viewModel.getInstitutionUser().setValue(this.getIntent().getParcelableExtra("institutionUser"));
        this.viewModel.getUserByID();
        this.viewModel.getInstitution().observe(this, this.institutionObserver());
        this.viewModel.getUser().observe(this, this.userObserver());
    }

    private Observer<? super User> userObserver() {
        return user -> {
            if (user != null) {
                this.binding.idUsername.setText(user.getName());
                this.binding.idUsermail.setText(user.getEmail());
                Glide.with(this).load(user.getPhotoUrl()).placeholder(R.drawable.icone_smarclass_sem_fundo).into(binding.institutionuserPicture);
            }
        };
    }
    private void buildMenu() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Carteirinha");
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private Observer<? super Institution> institutionObserver() {
        return institution -> {
            if (institution != null) {
                this.binding.idInstitutionName.setText(institution.getName());
                this.binding.idInstitutionCnpj.setText(institution.getCnpj());
                Glide.with(this).load(institution.getPhotoUrl()).placeholder(R.drawable.icone_smarclass_sem_fundo).into(binding.institutionpicture);
            }
        };
    }
}