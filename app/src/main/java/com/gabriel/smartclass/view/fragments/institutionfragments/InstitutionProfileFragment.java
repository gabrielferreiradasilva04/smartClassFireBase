package com.gabriel.smartclass.view.fragments.institutionfragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.databinding.FragmentInstitutionProfileBinding;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.viewModels.HostInstitutionActivityViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class InstitutionProfileFragment extends Fragment {
    private FragmentInstitutionProfileBinding binding;
    private HostInstitutionActivityViewModel viewModel;
    public InstitutionProfileFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInstitutionProfileBinding.inflate(inflater, container, false);
        ViewModelProvider viewModelProvider = new ViewModelProvider(this);
        viewModel = viewModelProvider.get(HostInstitutionActivityViewModel.class);
        if (viewModel.getInstitutionMutableLiveData() != null) {
            viewModel.getInstitutionMutableLiveData().observe(getViewLifecycleOwner(), observeInstitution());
        }
        binding.institutionProfilePicture.setOnClickListener(clickOpenPictureOptions());
        return binding.getRoot();
    }

    @NonNull
    private Observer<Institution> observeInstitution() {
        return institution ->  {
                binding.institutionEdtxtDisplayName.setText(institution.getName());
                binding.institutionEdtxtCnpj.setText(institution.getCnpj());
                binding.institutionEdtxtEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        };
    }
    public void openChangePassword(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setCancelable(false);
        final View popUp = getLayoutInflater().inflate(R.layout.update_password_layout,null);
        dialogBuilder.setView(popUp);
        Button buttonConfirm = popUp.findViewById(R.id.button_confirm);
        Button buttonDone = popUp.findViewById(R.id.button_done);
        EditText textPassword = popUp.findViewById(R.id.text_password);
        EditText textConfirmPassword = popUp.findViewById(R.id.text_confirmPassword);
        Dialog dialog = dialogBuilder.create();
        dialog.show();
        buttonDone.setOnClickListener(v -> dialog.dismiss());
        buttonConfirm.setOnClickListener(v -> {
            if(!textPassword.getText().toString().equals("") && !textConfirmPassword.getText().toString().equals("")){
                viewModel.updatePassword(textPassword.getText().toString(), textConfirmPassword.getText().toString(), getContext(), dialog);
            }
            else{
                Snackbar snackbar = Snackbar.make(v, "Preencha todos os campos",Snackbar.LENGTH_SHORT).setBackgroundTint(Color.RED);
                snackbar.show();
            }
        });
    }
    private View.OnClickListener clickOpenPictureOptions() {
        return v -> {
            PopupMenu popupMenu = new PopupMenu(getActivity().getApplication(), v);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.profile_picture_options, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if(id == R.id.itemExcludePicture){
                    excludeProfilePicture();
                }
                if (id == R.id.itemEditPicture) {
                    Intent openGalaryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(openGalaryIntent, 1000);
                }
                return true;
            });
            popupMenu.show();
        };
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK && data != null){
                Uri uriImage = data.getData();
                binding.institutionProfilePicture.setImageURI(uriImage);
            }
        }
    }
    public void excludeProfilePicture() {
        if(binding.institutionProfilePicture.getDrawable() != null){
            String email = binding.institutionEdtxtEmail.getText().toString();
            ProgressBar progressBar = binding.progressBarProfileChanges;
            View view = binding.viewLoading;
            viewModel.excludeProfilePicture(email,getContext(),progressBar, view);
        }else{
            Snackbar snackbar = Snackbar.make(binding.institutionSaveChangesProfile, "Primeiro adicione uma foto para excluir...", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }
}