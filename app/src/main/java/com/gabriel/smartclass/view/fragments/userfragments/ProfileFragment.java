package com.gabriel.smartclass.view.fragments.userfragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.databinding.FragmentProfileBinding;
import com.gabriel.smartclass.utilities.listeners.CapacityTextListener;
import com.gabriel.smartclass.view.InstitutionMainMenu;
import com.gabriel.smartclass.view.StudentMainMenu;
import com.gabriel.smartclass.viewModels.HostUserActivityViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private HostUserActivityViewModel hostUserActivityViewModel;
    InstitutionMainMenu institutionMain;
    private StudentMainMenu studentMain;

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
        defaultInitialize();
        if (this.getActivity().getClass().equals(StudentMainMenu.class)) {
            initializeUserProfile();
        } else if (this.getActivity().getClass().equals(InstitutionMainMenu.class)) {
            initializeInstitutionProfile();
        }
        return binding.getRoot();
    }

    private void defaultInitialize() {
        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());
        hostUserActivityViewModel = viewModelProvider.get(HostUserActivityViewModel.class);
        hostUserActivityViewModel.getSnackBarText().observe(getViewLifecycleOwner(), observeSnackbar());
        binding.changePasswordProfile.setOnClickListener(clickListenerOpenPasswordDialog());
        hostUserActivityViewModel.getProfilePictureLiveData().observe(getViewLifecycleOwner(), observeProfilePicture());
    }

    private void initializeInstitutionProfile() {
        institutionMain = (InstitutionMainMenu) getActivity();
        institutionMain.updateTitle("Perfil");
        binding.institutionEdtxtCnpj.setVisibility(View.VISIBLE);
        binding.institutionTxtviewCapacity.setVisibility(View.VISIBLE);
        binding.scrollbuttons.setVisibility(View.VISIBLE);
        binding.saveChangesProfile.setOnClickListener(clickListenerInstitutionSaveChanges());
        binding.profilePicture.setOnClickListener(clickListenerOpenInstitutionPictureOptions());
        loadInstitutionDetails();
        binding.edtxtStudentsCapacity.setOnFocusChangeListener(new CapacityTextListener(binding.edtxtStudentsCapacity));
        binding.edtxtTeachersCapacity.setOnFocusChangeListener(new CapacityTextListener(binding.edtxtTeachersCapacity));
        binding.edtxtClassroomsCapacity.setOnFocusChangeListener(new CapacityTextListener(binding.edtxtClassroomsCapacity));
        binding.edtxtCoordinatorsCapacity.setOnFocusChangeListener(new CapacityTextListener(binding.edtxtCoordinatorsCapacity));
    }

    private void initializeUserProfile() {
        studentMain = (StudentMainMenu) getActivity();
        studentMain.updateTitle("Perfil");
        loadUserDetails();
        binding.saveChangesProfile.setOnClickListener(clickListenerUserSaveChanges());
        binding.profilePicture.setOnClickListener(clickListenerOpenUserPictureOptions());
    }

    public void loadUserDetails() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            binding.edtxtEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            binding.edtxtDisplayName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        }
    }

    public void loadInstitutionDetails() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null && hostUserActivityViewModel.getInstitutionMutableLiveData().getValue() != null) {
            String name = hostUserActivityViewModel.getInstitutionMutableLiveData().getValue().getName();
            String cnpj = hostUserActivityViewModel.getInstitutionMutableLiveData().getValue().getCnpj();
            String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            binding.edtxtEmail.setText(email);
            binding.institutionEdtxtCnpj.setText(cnpj);
            binding.edtxtDisplayName.setText(name);
            binding.edtxtClassroomsCapacity.setText(Integer.toString(hostUserActivityViewModel.getInstitutionMutableLiveData().getValue().getMaxClassrooms()));
            binding.edtxtCoordinatorsCapacity.setText(Integer.toString(hostUserActivityViewModel.getInstitutionMutableLiveData().getValue().getMaxCoordinators()));
            binding.edtxtStudentsCapacity.setText(Integer.toString(hostUserActivityViewModel.getInstitutionMutableLiveData().getValue().getMaxStudents()));
            binding.edtxtTeachersCapacity.setText(Integer.toString(hostUserActivityViewModel.getInstitutionMutableLiveData().getValue().getMaxTeachers()));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hostUserActivityViewModel.getSnackBarText().removeObserver(observeSnackbar());
        hostUserActivityViewModel.getSnackBarText().setValue(null);
        hostUserActivityViewModel.getProfilePictureLiveData().removeObserver(observeProfilePicture());

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Uri uriImage = data.getData();
                    binding.profilePicture.setImageURI(uriImage);
                }
            }
        }
    }

    public void excludeUserPhoto() {
        if (binding.profilePicture.getDrawable() != null) {
            String email = binding.edtxtEmail.getText().toString();
            ProgressBar progressBar = binding.progressBarProfileChanges;
            View view = binding.viewLoading;
            hostUserActivityViewModel.excludeUserPhoto(email, progressBar, view);
        } else {
            Toast toast = Toast.makeText(getContext(), "Primeiro adicione uma foto para excluir", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void excludeInstitutionPhoto() {
        if (binding.profilePicture.getDrawable() != null) {
            String email = binding.edtxtEmail.getText().toString();
            ProgressBar progressBar = binding.progressBarProfileChanges;
            View view = binding.viewLoading;
            hostUserActivityViewModel.excludeInstitutionPhoto(email, progressBar, view);
        } else {
            Toast toast = Toast.makeText(getContext(), "Primeiro adicione uma foto para excluir", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void confirmChangePassword(String password, String passwordConfirm, Dialog dialog) {
        hostUserActivityViewModel.updatePassword(password, passwordConfirm, getContext(), dialog);
    }

    public void openChangePassword() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setCancelable(false);
        final View popUp = getLayoutInflater().inflate(R.layout.update_password_layout, null);
        dialogBuilder.setView(popUp);
        Button buttonConfirm = popUp.findViewById(R.id.button_confirm);
        Button buttonDone = popUp.findViewById(R.id.button_done);
        EditText textPassword = popUp.findViewById(R.id.text_password);
        EditText textConfirmPassword = popUp.findViewById(R.id.text_confirmPassword);
        Dialog dialog = dialogBuilder.create();
        dialog.show();
        buttonDone.setOnClickListener(v -> {
                    dialog.dismiss();
                    dialog.setOnDismissListener(dialog1 -> {
                        buttonDone.setOnClickListener(null);
                        buttonConfirm.setOnClickListener(null);
                        dialog1.cancel();
                    });
                }
        );
        buttonConfirm.setOnClickListener(v -> {
            if (!textPassword.getText().toString().equals("") && !textConfirmPassword.getText().toString().equals("")) {
                confirmChangePassword(textPassword.getText().toString(), textConfirmPassword.getText().toString(), dialog);
            } else {
                Snackbar snackbar = Snackbar.make(v, "Preencha todos os campos", Snackbar.LENGTH_SHORT).setBackgroundTint(Color.RED);
                snackbar.show();
            }

        });
    }

    public View.OnClickListener clickListenerOpenPasswordDialog() {
        return v -> openChangePassword();
    }

    private View.OnClickListener clickListenerOpenUserPictureOptions() {
        return v -> {
            PopupMenu popupMenu = new PopupMenu(getActivity().getApplication(), v);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.profile_picture_options, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.itemExcludePicture) {
                    excludeUserPhoto();
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

    private View.OnClickListener clickListenerOpenInstitutionPictureOptions() {
        return v -> {
            openMenuOptions(v);
        };
    }

    private void openMenuOptions(View v) {
        PopupMenu popupMenu = new PopupMenu(getActivity().getApplication(), v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.profile_picture_options, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.itemExcludePicture) {
                excludeInstitutionPhoto();
            }
            if (id == R.id.itemEditPicture) {
                Intent openGalaryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalaryIntent, 1000);
            }
            return true;
        });
        popupMenu.show();
    }

    public View.OnClickListener clickListenerUserSaveChanges() {
        return v -> {
            userSaveChanges();
        };
    }

    private void userSaveChanges() {
        studentMain.hideKeyboard();
        Bitmap bitmap;
        if ((binding.profilePicture.getDrawable()) != null) {
            bitmap = ((BitmapDrawable) binding.profilePicture.getDrawable()).getBitmap();
        } else {
            bitmap = null;
        }
        String displayName = binding.edtxtDisplayName.getText().toString();
        String email = binding.edtxtEmail.getText().toString();
        ProgressBar progressBar = binding.progressBarProfileChanges;
        View view = binding.viewLoading;
        hostUserActivityViewModel.updateUserProfile(displayName, email, bitmap, progressBar, view);
    }

    public View.OnClickListener clickListenerInstitutionSaveChanges() {
        return v -> {
            saveChanges();
        };
    }

    private void saveChanges() {
        institutionMain.hideKeyboard();
        Bitmap bitmap;
        if ((binding.profilePicture.getDrawable()) != null) {
            bitmap = ((BitmapDrawable) binding.profilePicture.getDrawable()).getBitmap();
        } else {
            bitmap = null;
        }
        int maxTeachers = 0;
        int maxStudents = 0;
        int maxCoordinators = 0;
        int maxClassrooms = 0;
        if (binding.edtxtTeachersCapacity.getText().toString().equals("")) {
            binding.edtxtTeachersCapacity.setText("0");
        } else {
            maxTeachers = Integer.parseInt(binding.edtxtTeachersCapacity.getText().toString());
        }
        if (binding.edtxtStudentsCapacity.getText().toString().equals("")) {
            binding.edtxtStudentsCapacity.setText("0");

        } else {
            maxStudents = Integer.parseInt(binding.edtxtStudentsCapacity.getText().toString());
        }

        if (binding.edtxtCoordinatorsCapacity.getText().toString().equals("")) {
            binding.edtxtCoordinatorsCapacity.setText("0");
        } else {
            maxCoordinators = Integer.parseInt(binding.edtxtCoordinatorsCapacity.getText().toString());
        }
        if (binding.edtxtClassroomsCapacity.getText().toString().equals("")) {
            binding.edtxtClassroomsCapacity.setText("0");
        } else {
            maxClassrooms = Integer.parseInt(binding.edtxtClassroomsCapacity.getText().toString());
        }
        String displayName = binding.edtxtDisplayName.getText().toString();
        String email = binding.edtxtEmail.getText().toString();
        ProgressBar progressBar = binding.progressBarProfileChanges;
        View view = binding.viewLoading;
        hostUserActivityViewModel.updateInstitutionProfile(displayName, email, bitmap, progressBar, view, maxTeachers, maxStudents, maxCoordinators, maxClassrooms);
    }

    @NonNull
    private Observer<String> observeSnackbar() {
        return s -> {
            if (s != null) {
                Snackbar snackbar = Snackbar.make(binding.saveChangesProfile, s, Snackbar.LENGTH_SHORT);
                snackbar.show();
            }

        };
    }

    @NonNull
    private Observer<Bitmap> observeProfilePicture() {
        return bitmap -> binding.profilePicture.setImageBitmap(bitmap);
    }


}