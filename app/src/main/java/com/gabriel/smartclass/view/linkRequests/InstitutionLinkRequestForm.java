package com.gabriel.smartclass.view.linkRequests;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.databinding.ActivityLinkRequestFormBinding;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.UserType;
import com.gabriel.smartclass.viewModels.InstitutionLinkRequestFormViewModel;
import com.google.android.material.snackbar.Snackbar;

public class InstitutionLinkRequestForm extends AppCompatActivity {
    private ActivityLinkRequestFormBinding binding;
    private InstitutionLinkRequestFormViewModel viewModel;
    private Institution institutionSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLinkRequestFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.institutionSelected = getIntent().getParcelableExtra("institution");
        binding.textLinkInstitutionName.setText("Solicitação para: " + institutionSelected.getName());
        viewModel = new InstitutionLinkRequestFormViewModel(this);
        viewModel.getUserTypesAndPopulateSpinner();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Solicitação");
        binding.sendLinkRequest.setOnClickListener(clickListenerSendRequestToInstitution());
        viewModel.getSnackbarText().observe(this, snackbarObserve());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.viewModel.getSnackbarText().setValue("");
        this.viewModel.getSnackbarText().removeObserver(this.snackbarObserve());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_action_bar, menu);
        return true;
    }

    @NonNull
    private Observer<String> snackbarObserve() {
        return s -> {
            if (s != null && !s.equals("")) {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.send_link_request), s, Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        };
    }

    @NonNull
    private View.OnClickListener clickListenerSendRequestToInstitution() {
        return v -> sendLinkRequest();
    }

    public void sendLinkRequest() {
        UserType userType = (UserType) binding.spinnerUserTypeLink.getSelectedItem();
        String title = binding.textLinkTitle.getText().toString();
        viewModel.createNewInstitutionLinkRequest(userType, title, institutionSelected, this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}