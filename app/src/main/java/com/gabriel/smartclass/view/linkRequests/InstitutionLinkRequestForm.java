package com.gabriel.smartclass.view.linkRequests;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.widget.Spinner;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.adapter.SpinnerUserTypeAdapter;
import com.gabriel.smartclass.databinding.ActivityInstitutionsSearchBinding;
import com.gabriel.smartclass.databinding.ActivityLinkRequestFormBinding;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.UserType;
import com.gabriel.smartclass.viewModels.InstitutionLinkRequestViewModel;

import java.util.ArrayList;
import java.util.List;

public class InstitutionLinkRequestForm extends AppCompatActivity {
    private ActivityLinkRequestFormBinding binding;
    private Spinner spinnerUserType;
    private InstitutionLinkRequestViewModel viewModel;
    private Institution institutionSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLinkRequestFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.institutionSelected = getIntent().getParcelableExtra("institution");
        binding.textLinkInstitutionName.setText("Solicitação para: "+institutionSelected.getName());
        viewModel = new InstitutionLinkRequestViewModel();
        spinnerUserType = binding.spinnerUserTypeLink;
        viewModel.getUserTypesAndPopulateSpinner(spinnerUserType, this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode());
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}