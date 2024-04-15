package com.gabriel.smartclass.view.fragments.userfragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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
import com.gabriel.smartclass.databinding.FragmentProfileBinding;
import com.gabriel.smartclass.view.InstitutionMainMenu;
import com.gabriel.smartclass.view.StudentMainMenu;
import com.gabriel.smartclass.viewModels.HostUserActivityViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private HostUserActivityViewModel hostUserActivityViewModel;
    public ProfileFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());
        hostUserActivityViewModel = viewModelProvider.get(HostUserActivityViewModel.class);
        hostUserActivityViewModel.getSnackBarText().observe(getViewLifecycleOwner(), observeSnackbar());
        binding.profilePicture.setOnClickListener(clickOpenPictureOptions());
        binding.changePasswordProfile.setOnClickListener(openPasswordDialog());
        hostUserActivityViewModel.getProfilePictureLiveData().observe(getViewLifecycleOwner(), observeProfilePicture());
        if(this.getActivity().getClass().equals(StudentMainMenu.class)){
            StudentMainMenu main = (StudentMainMenu) getActivity();
            main.updateTitle("Perfil");
            loadUserDetails();
            binding.saveChangesProfile.setOnClickListener(buttonListenerSaveChanges());
        } else if (this.getActivity().getClass().equals(InstitutionMainMenu.class)) {
            InstitutionMainMenu main = (InstitutionMainMenu) getActivity();
            main.updateTitle("Perfil");
            binding.institutionEdtxtCnpj.setVisibility(View.VISIBLE);
            binding.saveChangesProfile.setOnClickListener(buttonListenerSaveInstitutionChanges());
            loadInstitutionDetails();
        }

        return binding.getRoot();
    }
    public void loadUserDetails(){
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            binding.edtxtEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            binding.edtxtDisplayName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hostUserActivityViewModel.getSnackBarText().removeObserver(observeSnackbar());
        hostUserActivityViewModel.getSnackBarText().setValue(null);
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
    private Observer<Bitmap> observeProfilePicture() {
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
                if(data != null){
                    Uri uriImage = data.getData();
                    binding.profilePicture.setImageURI(uriImage);
                }
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
            ProgressBar progressBar = binding.progressBarProfileChanges;
            View view = binding.viewLoading;
            hostUserActivityViewModel.updateProfile(displayName,email, bitmap,progressBar,view);
        };
    }
    public View.OnClickListener buttonListenerSaveInstitutionChanges(){
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
            hostUserActivityViewModel.updateProfileInstitutionProfile(displayName,email, bitmap,progressBar,view);
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
            hostUserActivityViewModel.excludeProfilePicture(email,progressBar, view);
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
        hostUserActivityViewModel.updatePassword(password, passwordConfirm, getContext(), dialog);
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
        buttonDone.setOnClickListener(v -> {
                    dialog.dismiss();
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            buttonDone.setOnClickListener(null);
                            buttonConfirm.setOnClickListener(null);
                            dialog.cancel();
                        }
                    });
                }


        );
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

    public void loadInstitutionDetails(){
        if(FirebaseAuth.getInstance().getCurrentUser() != null && hostUserActivityViewModel.getInstitutionMutableLiveData().getValue() != null){
            String name = hostUserActivityViewModel.getInstitutionMutableLiveData().getValue().getName();
            String cnpj = hostUserActivityViewModel.getInstitutionMutableLiveData().getValue().getCnpj();
            String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            binding.edtxtEmail.setText(email);
            binding.institutionEdtxtCnpj.setText(cnpj);
            binding.edtxtDisplayName.setText(name);
        }
    }


}