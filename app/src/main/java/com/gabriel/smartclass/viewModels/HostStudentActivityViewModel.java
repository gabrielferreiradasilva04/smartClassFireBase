package com.gabriel.smartclass.viewModels;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.gabriel.smartclass.adapter.InstitutionsAdapter;
import com.gabriel.smartclass.dao.UserDAO;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class HostStudentActivityViewModel extends ViewModel {
    private InstitutionsAdapter userInstitutionsAdapter;
    private UserDAO userDAO;
    private Institution selectedInstitution;
    /*User properties*/
    private MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private MutableLiveData<Bitmap> profilePictureLiveData = new MutableLiveData<>();
    private MutableLiveData<String> snackBarText = new MutableLiveData<>();
    /*User properties*/

    public MutableLiveData<String> getSnackBarText() {
        return snackBarText;
    }

    public MutableLiveData<Bitmap> getProfilePictureLiveData() {
        return profilePictureLiveData;
    }
    public void setSelectedInstitution(Institution selectedInstitution) {
        this.selectedInstitution = selectedInstitution;
    }

    public InstitutionsAdapter getUserInstitutionsAdapter() {
        return userInstitutionsAdapter;
    }


    public HostStudentActivityViewModel(){
        userDAO = new UserDAO();
    }
    public void listenerSnapshotChanges(){
        userDAO.listenerSnapshotChanges((value, error) -> {
            if(error == null){
                User userUpdate = value.toObject(User.class);
                userLiveData.setValue(userUpdate);
            }
        });
    }
    public void getUserInstitutions(){

        List<Institution> institutions = new ArrayList<>();
        userInstitutionsAdapter = new InstitutionsAdapter(institutions);
        userDAO.getUserInstitutions(task -> {
            User user = task.getResult().toObject(User.class);
            assert user != null;
            if(user.getInstitutions() != null){

                for (DocumentReference documentReference: user.getInstitutions()) {
                    documentReference.get().addOnSuccessListener(documentSnapshot -> {
                        Institution institution = documentSnapshot.toObject(Institution.class);
                        institution.setId(documentSnapshot.getId());

                        if(institution != null){
                            institution.setId(documentSnapshot.getId());
                            userInstitutionsAdapter.addItem(institution);
                            userInstitutionsAdapter.notifyDataSetChanged();
                        }
                    });

                }
            }else{
                userInstitutionsAdapter.notifyDataSetChanged();
            }
        }, e -> {
        });
    }
    public void loadUserDetails(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser.getPhotoUrl() != null && !currentUser.getPhotoUrl().equals(Uri.parse(""))){
            userDAO.downloadImage(currentUser.getEmail(), bytes -> {
                profilePictureLiveData.setValue(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }, e -> {

            });
        }
    }
    public void updateProfile(@NonNull String displayName,@NonNull String email,@NonNull Context context, @NonNull Bitmap profilePictureCurrent, @NonNull ProgressBar progressBar,@NonNull View viewLoading){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(displayName.equals(currentUser.getDisplayName()) && email.equals(currentUser.getEmail()) && profilePictureCurrent == profilePictureLiveData.getValue()){
            snackBarText.setValue("Não detectamos alterações a serem salvas");
        } else if (!displayName.equals("") && !email.equals("")) {
            if(profilePictureCurrent != this.profilePictureLiveData.getValue() && profilePictureCurrent!= null){
                uploadProfilePicture(email,profilePictureCurrent, progressBar, viewLoading,context);
            }
            userDAO.updateProfile(displayName, o -> {
                snackBarText.setValue("Perfil atualizado com sucesso");
            });
        }
        else{
            snackBarText.setValue("Todos os campos são obrigatórios");

        }
    }
    public void uploadProfilePicture(String email, Bitmap imageBitmap, ProgressBar progressBar, View view, Context context){
        FirebaseUser currentUserApplication = FirebaseAuth.getInstance().getCurrentUser();
        if (!email.equals("") && imageBitmap != null && imageBitmap != profilePictureLiveData.getValue()){

            view.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            userDAO.uploadProfileImage(email, imageBitmap, taskSnapshot -> {

                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                StorageReference storagePictures = storageReference.child("profilePictures");
                storagePictures.child(email).getDownloadUrl().addOnSuccessListener(uri ->  {
                    UserProfileChangeRequest changes = new UserProfileChangeRequest.Builder().setPhotoUri(uri).build();
                    currentUserApplication.updateProfile(changes);
                    profilePictureLiveData.setValue(imageBitmap);
                    progressBar.setVisibility(View.INVISIBLE);
                    view.setVisibility(View.GONE);

                }).addOnFailureListener(e -> {

                    snackBarText.setValue("Ocorreu um erro ao salvar sua foto de perfil");
                    progressBar.setVisibility(View.INVISIBLE);
                    view.setVisibility(View.GONE);

                });

            }, e -> {
                progressBar.setVisibility(View.INVISIBLE);
                view.setVisibility(View.GONE);
                snackBarText.setValue("Erro no upload");

            });

        }
    }
    public void updatePassword(String password, String newPassword, @NonNull Context context, @NonNull Dialog dialog){
        if(password.equals(newPassword)){

            userDAO.updatePassword(password, task -> {
                snackBarText.setValue("Senha atualizada com sucesso!");
                dialog.dismiss();
            }, e -> {
                String errorMessage = "";
                if(e.getClass().equals(FirebaseAuthWeakPasswordException.class)){
                    errorMessage = "A senha deve conter pelo mesnos 6 caractéres";
                }
                if(e.getClass().equals(FirebaseAuthInvalidCredentialsException.class)){
                    errorMessage = "Digite um e-mail válido";
                }
                if(e.getClass().equals(FirebaseAuthUserCollisionException.class)){
                    errorMessage = "Já existe um usuário cadastrado com esse e-mail";
                }
                if(e.getClass().equals(FirebaseNetworkException.class)){
                    errorMessage = "Sem conexão com a internet, tente novamente mais tarde";
                }
                Toast toast = Toast.makeText(context, errorMessage, Toast.LENGTH_LONG);
                toast.show();
            });

        }else{

            Toast toast = Toast.makeText(context, "Senhas não conferem", Toast.LENGTH_SHORT);
            toast.show();

        }
    }
    public void excludeProfilePicture(String email, Context context, @NonNull ProgressBar progressBar, @NonNull View view){

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest removeProfilePicture = new UserProfileChangeRequest.Builder().setPhotoUri(null).build();
        currentUser.updateProfile(removeProfilePicture).addOnSuccessListener(task -> {
            if (profilePictureLiveData.getValue()!= null){

                view.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                userDAO.excludeImageStorage(email, o -> {
                    snackBarText.setValue("Foto de perfil excluida com sucesso!");
                    profilePictureLiveData.setValue(null);
                    progressBar.setVisibility(View.INVISIBLE);
                    view.setVisibility(View.GONE);

                }, e -> {

                    snackBarText.setValue("Erro ao deletar sua foto de perfil");

                });
            }

        }).addOnFailureListener(e->{

            snackBarText.setValue("Erro ao deletar sua foto de perfil, tente novamente mais tarde!");

        });

    }



}
