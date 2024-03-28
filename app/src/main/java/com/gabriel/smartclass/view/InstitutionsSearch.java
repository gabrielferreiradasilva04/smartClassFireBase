package com.gabriel.smartclass.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.adapter.InstitutionsAdapter;
import com.gabriel.smartclass.databinding.ActivityInstitutionsSearchBinding;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.observer.EmptyRecyclerViewObserver;
import com.gabriel.smartclass.view.linkRequests.InstitutionLinkRequestForm;
import com.gabriel.smartclass.viewModels.InstitutionsSearchViewModel;

public class InstitutionsSearch extends AppCompatActivity {
    private ActivityInstitutionsSearchBinding binding;
    private InstitutionsSearchViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInstitutionsSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EmptyRecyclerViewObserver emptyRecyclerViewObserver = new EmptyRecyclerViewObserver(binding.recyclerViewInstitutionsFind, findViewById(R.id.empty_container));


        viewModel = new InstitutionsSearchViewModel(this, emptyRecyclerViewObserver);
        binding.nameToFindInstitutionsSearch.addTextChangedListener(search());


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
                viewModel.search(binding.recyclerViewInstitutionsFind, s.toString(), new InstitutionsAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(Institution institution) {
                        Intent i = new Intent(getApplicationContext(), InstitutionLinkRequestForm.class);
                        startActivity(i);
                    }
                });
                viewModel.getAdapter().notifyDataSetChanged();
            }
        };
    }
}