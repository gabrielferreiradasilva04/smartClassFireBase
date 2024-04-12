package com.gabriel.smartclass.view.fragments.userfragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsetsController;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gabriel.smartclass.R;
import com.gabriel.smartclass.databinding.FragmentProfileBinding;
import com.gabriel.smartclass.view.StudentMainMenu;
import com.gabriel.smartclass.viewModels.HostStudentActivityViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private HostStudentActivityViewModel hostStudentActivityViewModel;
    public ProfileFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @SuppressLint({"WrongConstant"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        StudentMainMenu main = (StudentMainMenu) getActivity();
        main.updateTitle("Perfil");
        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());
        hostStudentActivityViewModel = viewModelProvider.get(HostStudentActivityViewModel.class);
        hostStudentActivityViewModel.getProfilePictureLiveData().observe(getViewLifecycleOwner(), observeProfilePictureStudentMainMenu());
        binding.profilePicture.setOnClickListener(clickOpenPictureOptions());
        binding.saveChangesProfile.setOnClickListener(buttonListenerSaveChanges());
        hostStudentActivityViewModel.getSnackBarText().observe(getViewLifecycleOwner(), observeSnackbar());
        binding.changePasswordProfile.setOnClickListener(openPasswordDialog());
        setUserDetails();
        return binding.getRoot();
    }
    public void setUserDetails(){
        binding.edtxtEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        binding.edtxtDisplayName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hostStudentActivityViewModel.getSnackBarText().removeObserver(observeSnackbar());
        hostStudentActivityViewModel.getSnackBarText().setValue(null);
    }

    @NonNull
    private Observer<String> observeSnackbar() {
        return s-> {
            if(s != null){
                Snackbar snackbar = Snackbar.make(binding.saveChangesProfile, s, Snackbar.LENGTH_SHORT);
                snackbar.show();
            }

        };
    }

    @NonNull
    private Observer<Bitmap> observeProfilePictureStudentMainMenu() {
        return bitmap -> binding.profilePicture.setImageBitmap(bitmap);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri uriImage = data.getData();
                binding.profilePicture.setImageURI(uriImage);
            }
        }
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

    public View.OnClickListener buttonListenerSaveChanges(){
        return v -> {
            Bitmap bitmap;
            if(( binding.profilePicture.getDrawable())!=null){
                bitmap = ((BitmapDrawable) binding.profilePicture.getDrawable()).getBitmap();
            }else{
                bitmap = null;
            }
            String displayName = binding.edtxtDisplayName.getText().toString();
            String email = binding.edtxtEmail.getText().toString();
            Context context = getContext();
            ProgressBar progressBar = binding.progressBarProfileChanges;
            View view = binding.viewLoading;
            hostStudentActivityViewModel.updateProfile(displayName,email,context,bitmap,progressBar,view);
        };
    }

    /***
     * Exclui a foto de perfil do usuário setando como nula no viewModel da host e excluindo do repositório na nuvem através da classe usuarioDAO
     */
    public void excludeProfilePicture() {
        if(binding.profilePicture.getDrawable() != null){
            String email = binding.edtxtEmail.getText().toString();
            ProgressBar progressBar = binding.progressBarProfileChanges;
            View view = binding.viewLoading;
            hostStudentActivityViewModel.excludeProfilePicture(email,getContext(),progressBar, view);
        }else{
            Toast toast = Toast.makeText(getContext(), "Primeiro adicione uma foto para excluir", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /***
     * Carrega os dados do usuário com base no viewModel da tela principal (Host)
     */


    /***
     * Realiza a validação das senhas e a alteração por meio da viweModel da host utilizando a usuarioDAO
     * @param password senha
     * @param passwordConfirm confirmação de senha
     * @param dialog dialog a ser fechado na conclusão do método
     */
    public void confirmChangePassword(String password, String passwordConfirm, Dialog dialog){
        hostStudentActivityViewModel.updatePassword(password, passwordConfirm, getContext(), dialog);
    }

    /***
     * realiza o processo de criação e abertura de uma dialog para realizar a troca da senha do usuário através da validação do
     * metodo confirmChangePassowrd
     */
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
                confirmChangePassword(textPassword.getText().toString(), textConfirmPassword.getText().toString(), dialog);
            }
            else{
                Snackbar snackbar = Snackbar.make(v, "Preencha todos os campos",Snackbar.LENGTH_SHORT).setBackgroundTint(Color.RED);
                snackbar.show();
            }

        });
    }

    public View.OnClickListener excludeProfilePictureButton() {
        return v -> excludeProfilePicture();
    }

    public View.OnClickListener openPasswordDialog() {
        return v -> openChangePassword();
    }

}