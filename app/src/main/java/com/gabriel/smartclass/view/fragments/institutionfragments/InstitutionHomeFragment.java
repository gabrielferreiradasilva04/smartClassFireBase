package com.gabriel.smartclass.view.fragments.institutionfragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gabriel.smartclass.databinding.FragmentInstitutionHomeBinding;
import com.gabriel.smartclass.viewModels.HostInstitutionActivityViewModel;

public class InstitutionHomeFragment extends Fragment {
    private FragmentInstitutionHomeBinding binding;
    private HostInstitutionActivityViewModel viewModel;
    public InstitutionHomeFragment(){

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInstitutionHomeBinding.inflate(inflater, container, false);
        viewModel = (HostInstitutionActivityViewModel) requireActivity().getViewModelStore().get("hostInstitutionActivityViewModel");

        return  binding.getRoot();
    }
}