package com.gabriel.smartclass.view.fragments.institutionusersfragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.gabriel.smartclass.databinding.FragmentInstitutionUserProfileBinding;
import com.gabriel.smartclass.view.UserInstitutionMenu;
import com.gabriel.smartclass.viewModels.InstitutionUserProfileViewModel;
import com.gabriel.smartclass.viewModels.factorys.InstitutionUserProfileViewModelFactory;

public class InstitutionUserProfile extends Fragment {
    private UserInstitutionMenu parentView;
    private FragmentInstitutionUserProfileBinding binding;
    private  InstitutionUserProfileViewModel viewModel;
    private EditText edtxt_username;
    private EditText edtxt_userType;
    private EditText edtxt_institutionName;
    private AppCompatImageButton imageButton;

    public EditText getEdtxt_username() {
        return edtxt_username;
    }

    public EditText getEdtxt_userType() {
        return edtxt_userType;
    }

    public EditText getEdtxt_institutionName() {
        return edtxt_institutionName;
    }

    public AppCompatImageButton getImageButton() {
        return imageButton;
    }

    public InstitutionUserProfile() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInstitutionUserProfileBinding.inflate(inflater, container, false);
        initialize();
        return binding.getRoot();
    }
    private void initialize(){
        InstitutionUserProfileViewModelFactory factory = new InstitutionUserProfileViewModelFactory(this);
        ViewModelProvider provider = new ViewModelProvider(requireActivity(), factory);
        viewModel = provider.get(InstitutionUserProfileViewModel.class);
        parentView = (UserInstitutionMenu) getActivity();
        loadComponents();
        viewModel.loadUserDetails(parentView.getCurrentInstitutionUser(), parentView.getCurrentInstitution());
        UserInstitutionMenu.institutionUserProfilePicture.observe(getViewLifecycleOwner(), profilePictureObserver());
    }

    private Observer<? super Bitmap> profilePictureObserver() {
        return this::loadProfilePicture;
    }

    private void loadProfilePicture(Bitmap bitmap) {
        if(bitmap!=null){
            imageButton.setImageBitmap(bitmap);
        }
    }

    private void loadComponents() {
        edtxt_username = binding.institutionUserMainmenuEdtxtUsername;
        edtxt_institutionName = binding.institutionUserMainmenuEdtxtInstitutionName;
        edtxt_userType = binding.institutionUserMainmenuEdtxtUsertype;
        imageButton = binding.institutionUserMainmenuProfilePicture;
    }

}