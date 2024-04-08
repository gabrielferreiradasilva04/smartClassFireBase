package com.gabriel.smartclass.view.linkRequests;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.adapter.SpinnerUserTypeAdapter;
import com.gabriel.smartclass.databinding.ActivityInstitutionsSearchBinding;
import com.gabriel.smartclass.databinding.ActivityLinkRequestFormBinding;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.UserType;
import com.gabriel.smartclass.viewModels.InstitutionLinkRequestViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class InstitutionLinkRequestForm extends AppCompatActivity {
    private ActivityLinkRequestFormBinding binding;
    private InstitutionLinkRequestViewModel viewModel;
    private Institution institutionSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLinkRequestFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.institutionSelected = getIntent().getParcelableExtra("institution");
        binding.textLinkInstitutionName.setText("Solicitação para: "+institutionSelected.getName());
        viewModel = new InstitutionLinkRequestViewModel(this);
        viewModel.getUserTypesAndPopulateSpinner();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode());

        binding.sendLinkRequest.setOnClickListener(sendLinkRequestToInstitution());
        viewModel.getSnackbarText().observe(this, snackbarObserve());
    }

    @NonNull
    private Observer<String> snackbarObserve() {
        return s -> {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.send_link_request), s, Snackbar.LENGTH_SHORT);
            snackbar.show();
        };
    }

    @NonNull
    private View.OnClickListener sendLinkRequestToInstitution() {
        return v -> prepareLinkRequest();
    }
    public void prepareLinkRequest(){
        UserType userType = (UserType) binding.spinnerUserTypeLink.getSelectedItem();
        String title = binding.textLinkTitle.getText().toString();
        viewModel.createNewInstitutionLinkRequest(userType,title, institutionSelected);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}