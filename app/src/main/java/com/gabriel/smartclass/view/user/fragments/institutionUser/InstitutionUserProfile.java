package com.gabriel.smartclass.view.user.fragments.institutionUser;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.gabriel.smartclass.R;
import com.gabriel.smartclass.model.User;
import com.gabriel.smartclass.model.UserType;
import com.gabriel.smartclass.view.user.views.institutionUser.CardIDView;
import com.gabriel.smartclass.view.user.views.institutionUser.InstitutionUserMainMenu;
import com.gabriel.smartclass.viewModels.InstitutionUserMainMenuViewModel;

import java.util.Objects;

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
        this.viewModel.getSnackbarText().setValue("");
        this.viewModel.getUserTypeByInstitutionUser().removeObserver(this.userTypeObserver());
        this.viewModel.getUserByInstitutionUser().removeObserver(this.userObserver());
    }

    private void initialize(){
        buildMenu();
        buildViewModel();
        loadComponents();
        viewModelObservers();
        this.binding.buttonCardId.setOnClickListener(this.cardViewListener());
    }

    private View.OnClickListener cardViewListener() {
        return view ->{
          this.openCardIDView();
        };
    }

    private void openCardIDView() {
        Intent i = new Intent(this.getContext(), CardIDView.class);
        i.putExtra("institution", this.viewModel.getCurrentInstitution());
        i.putExtra("institutionUser", this.viewModel.getCurrentInstitutionUser());
        startActivity(i);
    }

    public void viewModelObservers(){
        this.viewModel.getUserByInstitutionUser().observe(getViewLifecycleOwner(), userObserver());
        this.viewModel.getUserTypeByInstitutionUser().observe(getViewLifecycleOwner(), userTypeObserver());
        this.edtxt_institutionName.setText(viewModel.getCurrentInstitution().getName());
    }

    @NonNull
    private Observer<User> userObserver() {
        return user -> {
            if(user != null){
                this.edtxt_username.setText(user.getName());
                Glide.with(this.requireContext()).load(Objects.requireNonNull(viewModel.getUserByInstitutionUser().getValue()).getPhotoUrl()).error(R.drawable.icone_smarclass_sem_fundo).placeholder(R.drawable.icone_smarclass_sem_fundo).into(this.imageButton);
            }
        };
    }

    @NonNull
    private Observer<UserType> userTypeObserver() {
        return userType -> this.edtxt_userType.setText(userType.getDescription());
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