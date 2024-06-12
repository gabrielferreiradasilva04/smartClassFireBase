package com.gabriel.smartclass.view.user.fragments.institutionUser;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.gabriel.smartclass.model.User;
import com.gabriel.smartclass.model.UserType;
import com.gabriel.smartclass.view.user.views.institutionUser.InstitutionUserMainMenu;
import com.gabriel.smartclass.viewModels.InstitutionUserMainMenuViewModel;
import com.google.android.material.snackbar.Snackbar;

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.viewModel.getSnackbarText().setValue(null);
        this.viewModel.getSnackbarText().removeObserver(this.snackbarObserver());
        this.viewModel.getUserPicture().removeObserver(this.profilePictureObserver());
        this.viewModel.getUserTypeByInstitutionUser().removeObserver(this.userTypeObserver());
        this.viewModel.getUserByInstitutionUser().removeObserver(this.userObserver());
        this.viewModel = null;
    }

    private void initialize(){
        buildMenu();
        buildViewModel();
        loadComponents();
        viewModelObservers();
    }
    public void viewModelObservers(){
        this.viewModel.getUserByInstitutionUser().observe(getViewLifecycleOwner(), userObserver());
        this.viewModel.getUserPicture().observe(getViewLifecycleOwner(), profilePictureObserver());
        this.viewModel.getUserTypeByInstitutionUser().observe(getViewLifecycleOwner(), userTypeObserver());
        this.edtxt_institutionName.setText(viewModel.getCurrentInstitution().getName());
        this.viewModel.getSnackbarText().observe(this.getViewLifecycleOwner(), snackbarObserver());
    }

    @NonNull
    private Observer<User> userObserver() {
        return user -> {
            this.edtxt_username.setText(user.getName());
        };
    }

    @NonNull
    private Observer<Bitmap> profilePictureObserver() {
        return bitmap -> {
            this.imageButton.setImageBitmap(bitmap);
        };
    }

    @NonNull
    private Observer<UserType> userTypeObserver() {
        return userType -> {
            this.edtxt_userType.setText(userType.getDescription());
        };
    }

    private Observer<? super String> snackbarObserver() {
        return text ->{
          if(text != null && !text.equals("")){
              Snackbar.make(getContext(), binding.institutionUserMainmenuProfilePicture, text, Snackbar.LENGTH_SHORT);
          }
        };
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