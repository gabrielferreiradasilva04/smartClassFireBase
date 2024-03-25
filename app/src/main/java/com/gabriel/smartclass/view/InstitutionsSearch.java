package com.gabriel.smartclass.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.adapter.InstitutionsAdapter;
import com.gabriel.smartclass.databinding.ActivityInstitutionsSearchBinding;
import com.gabriel.smartclass.databinding.ActivityStudentMainMenuBinding;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.viewModels.InstitutionsSearchViewModel;

public class InstitutionsSearch extends AppCompatActivity {
    private ActivityInstitutionsSearchBinding binding;
    private InstitutionsSearchViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInstitutionsSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new InstitutionsSearchViewModel(this);
        binding.nameToFindInstitutionsSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

                viewModel.search(binding.recyclerView, s.toString(), new InstitutionsAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(Institution institution) {

                    }
                });
            }
        });


    }
}