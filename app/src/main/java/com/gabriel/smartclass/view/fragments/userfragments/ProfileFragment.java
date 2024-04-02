package com.gabriel.smartclass.view.fragments.userfragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.databinding.FragmentProfileBinding;
import com.gabriel.smartclass.viewModels.HostStudentActivityViewModel;

public class ProfileFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private FragmentProfileBinding binding;
    private HostStudentActivityViewModel hostStudentActivityViewModel;
    public ProfileFragment() {
        // Required empty public constructor
    }
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        hostStudentActivityViewModel = (HostStudentActivityViewModel) requireActivity().getViewModelStore().get("hostStudentActivityViewModel");
        loadUserDetails();
        binding.chooseprofilepicturebutton.setOnClickListener(openChooseProfilePicture());
        binding.saveChangesProfile.setOnClickListener(buttonListenerSaveChanges());
        binding.changePasswordProfile.setOnClickListener(openPasswordDialog());
        binding.excludeProfilePictureButton.setOnClickListener(excludeProfilePictureButton());
        refresh();
        return binding.getRoot();
    }
    public View.OnClickListener buttonListenerSaveChanges(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

            }
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
            binding.profilePicture.setImageBitmap(null);
        }else{
            Toast toast = Toast.makeText(getContext(), "Erro ao deletar foto", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /***
     * Carrega os dados do usuário com base no viewModel da tela principal (Host)
     */
    public void loadUserDetails(){
        binding.edtxtDisplayName.setText(hostStudentActivityViewModel.getDisplayName());
        binding.edtxtEmail.setText(hostStudentActivityViewModel.getEmail());
        if(hostStudentActivityViewModel.getPictureProfileBitmap() != null){
            binding.profilePicture.setImageBitmap(hostStudentActivityViewModel.getPictureProfileBitmap());
        }
        if(binding.refreshProfileFragment.isRefreshing()){
            binding.refreshProfileFragment.setRefreshing(false);
        }
    }

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
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmChangePassword(textPassword.getText().toString(), textConfirmPassword.getText().toString(), dialog);
            }
        });
    }
    private View.OnClickListener excludeProfilePictureButton() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                excludeProfilePicture();
            }
        };
    }

    private View.OnClickListener openPasswordDialog() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChangePassword();
            }
        };
    }
    public View.OnClickListener openChooseProfilePicture(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalaryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalaryIntent, 1000);
            }
        };
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
    public void refresh(){
        binding.refreshProfileFragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                hostStudentActivityViewModel.loadUserDetails(getContext());
                loadUserDetails();
            }
        });
    }

}