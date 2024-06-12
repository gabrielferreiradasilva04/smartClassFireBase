package com.gabriel.smartclass.view.user.fragments.institutionUser;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.gabriel.smartclass.view.user.views.institutionUser.InstitutionUserMainMenu;
import com.gabriel.smartclass.viewModels.InstitutionUserMainMenuViewModel;

public class InstitutionUserProfile extends Fragment {
    private com.gabriel.smartclass.databinding.FragmentInstitutionUserProfileBinding binding;
    private InstitutionUserMainMenuViewModel viewModel;
    private EditText edtxt_username;
    private EditText edtxt_userType;
    private EditText edtxt_institutionName;
    private AppCompatImageButton imageButton;

    public InstitutionUserProfile() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = com.gabriel.smartclass.databinding.FragmentInstitutionUserProfileBinding.inflate(inflater, container, false);
        initialize();
        return binding.getRoot();
    }
    private void initialize(){
        buildMenu();
        buildViewModel();
        loadComponents();
        viewModelObservers();
    }
    public void viewModelObservers(){
        this.viewModel.getUserByInstitutionUser().observe(getViewLifecycleOwner(), user -> {
            this.edtxt_username.setText(user.getName());
        });
        this.viewModel.getUserPicture().observe(getViewLifecycleOwner(), bitmap -> {
            this.imageButton.setImageBitmap(bitmap);
        });
        this.viewModel.getUserTypeByInstitutionUser().observe(getViewLifecycleOwner(), userType -> {
            this.edtxt_userType.setText(userType.getDescription());
        });
        this.edtxt_institutionName.setText(viewModel.getCurrentInstitution().getName());
    }

    private void buildViewModel() {
        ViewModelProvider provider = new ViewModelProvider(requireActivity());
        viewModel = provider.get(InstitutionUserMainMenuViewModel.class);
    }


    public void buildMenu(){
        InstitutionUserMainMenu main = (InstitutionUserMainMenu) getActivity();
        if (main != null) {
            main.updateTitle("Perfil");
        }
    }

    private void loadComponents() {
        edtxt_username = binding.institutionUserMainmenuEdtxtUsername;
        edtxt_institutionName = binding.institutionUserMainmenuEdtxtInstitutionName;
        edtxt_userType = binding.institutionUserMainmenuEdtxtUsertype;
        imageButton = binding.institutionUserMainmenuProfilePicture;
    }

}