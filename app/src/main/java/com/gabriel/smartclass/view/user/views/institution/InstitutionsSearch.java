package com.gabriel.smartclass.view.user.views.institution;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.adapter.interfaces.OnInstitutionItemClickListener;
import com.gabriel.smartclass.databinding.ActivitySearchInstitutionBinding;
import com.gabriel.smartclass.observer.EmptyRecyclerViewObserver;
import com.gabriel.smartclass.view.base.BaseActivity;
import com.gabriel.smartclass.view.linkRequests.InstitutionLinkRequestForm;
import com.gabriel.smartclass.viewModels.InstitutionsSearchViewModel;

@SuppressLint("NotifyDataSetChanged")

public class InstitutionsSearch extends BaseActivity {
    private ActivitySearchInstitutionBinding binding;
    private InstitutionsSearchViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchInstitutionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initialize();
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.emptyView();
    }

    public void initialize() {
        this.buildMenu();
        viewModel = new ViewModelProvider(this).get(InstitutionsSearchViewModel.class);
        binding.institutionsearchButtonsearch.setOnClickListener(searchListener());
        this.buildRecyclerView();
    }

    private void buildMenu() {
        getSupportActionBar().setTitle("Pesquisa");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode());
    }

    public void buildRecyclerView() {
        binding.recyclerViewInstitutionsFind.setHasFixedSize(false);
        binding.recyclerViewInstitutionsFind.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.recyclerViewInstitutionsFind.setAdapter(this.viewModel.getAdapter());
        this.viewModel.getAdapter().setItemClickListener(institutionClickListener());
    }

    public void emptyView() {
        EmptyRecyclerViewObserver emptyRecyclerViewObserver = new EmptyRecyclerViewObserver(binding.recyclerViewInstitutionsFind, findViewById(R.id.empty_container));
        this.viewModel.getAdapter().registerAdapterDataObserver(emptyRecyclerViewObserver);
        this.viewModel.getAdapter().notifyDataSetChanged();
    }


    private View.OnClickListener searchListener() {
        return view -> viewModel.search(binding.nameToFindInstitutionsSearch.getText().toString());
    }

    @NonNull
    private OnInstitutionItemClickListener institutionClickListener() {
        return institution -> {
            Intent i = new Intent(getApplicationContext(), InstitutionLinkRequestForm.class);
            i.putExtra("institution", institution);
            startActivity(i);
        };
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
}