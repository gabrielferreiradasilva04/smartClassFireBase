package com.gabriel.smartclass.view.user.views.institution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.adapter.InstitutionsAdapter;
import com.gabriel.smartclass.adapter.interfaces.OnInstitutionItemClickListener;
import com.gabriel.smartclass.databinding.ActivitySearchInstitutionBinding;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.observer.EmptyRecyclerViewObserver;
import com.gabriel.smartclass.view.base.BaseActivity;
import com.gabriel.smartclass.view.linkRequests.InstitutionLinkRequestForm;
import com.gabriel.smartclass.viewModels.InstitutionsSearchViewModel;

public class InstitutionsSearch extends BaseActivity {
    private ActivitySearchInstitutionBinding binding;
    private InstitutionsSearchViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchInstitutionBinding.inflate(getLayoutInflater());
        getSupportActionBar().setTitle("Pesquisa");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode());
        EmptyRecyclerViewObserver emptyRecyclerViewObserver = new EmptyRecyclerViewObserver(binding.recyclerViewInstitutionsFind, findViewById(R.id.empty_container));
        viewModel = new InstitutionsSearchViewModel(this, emptyRecyclerViewObserver);
        binding.nameToFindInstitutionsSearch.addTextChangedListener(search());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_action_bar, menu);
        return true;
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @NonNull
    private TextWatcher search() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void afterTextChanged(Editable s) {
                viewModel.search(binding.recyclerViewInstitutionsFind, s.toString(), new OnInstitutionItemClickListener() {
                    @Override
                    public void onItemClick(Institution institution) {
                        Intent i = new Intent(getApplicationContext(), InstitutionLinkRequestForm.class);
                        i.putExtra("institution", institution);
                        startActivity(i);
                    }
                });
                viewModel.getAdapter().notifyDataSetChanged();
            }
        };
    }
}