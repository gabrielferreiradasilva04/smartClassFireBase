package com.gabriel.smartclass.viewModels;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gabriel.smartclass.dao.InstitutionDAO;
import com.gabriel.smartclass.dao.UserDAO;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.view.InstitutionMainMenu;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class HostInstitutionActivityViewModel extends ViewModel {
    private MutableLiveData<Integer> notifications = new MutableLiveData<>();
    private MutableLiveData<Institution> institutionMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> snackbarText = new MutableLiveData<>();
    private MutableLiveData<Bitmap> profilePictureLiveData = new MutableLiveData<>();
    private InstitutionDAO institutionDAO;
    private UserDAO userDAO;

    public MutableLiveData<Institution> getInstitutionMutableLiveData() {
        return institutionMutableLiveData;
    }



    public MutableLiveData<Integer> getNotifications() {
        return notifications;
    }

    public void setNotifications(MutableLiveData<Integer> notifications) {
        this.notifications = notifications;
    }

    public HostInstitutionActivityViewModel(InstitutionMainMenu institutionMainMenu){
        this.institutionDAO = new InstitutionDAO();
        this.userDAO = new UserDAO();
    }
    private HostInstitutionActivityViewModel(){
        this.institutionDAO = new InstitutionDAO();
        this.userDAO = new UserDAO();
    }
    public void getInstitutionByCurrentUser(){
        institutionDAO.getInstitutionByCurrentUser(task ->{
            if(task.exists()){
                Institution institution = task.toObject(Institution.class);
                institutionMutableLiveData.setValue(institution);
            }
        }, e -> {
            snackbarText.setValue("Algo deu errado, tente novamente mais tarde");
        });
    }
    public void updatePassword(String password, String newPassword, Context context, Dialog dialog){
        if(password.equals(newPassword)){
            userDAO.updatePassword(password, task -> {
                Toast toast = Toast.makeText(context, "Senha atualizada", Toast.LENGTH_SHORT);
                toast.show();
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
    public void excludeProfilePicture(String email, Context context, ProgressBar progressBar, View view){
        if (profilePictureLiveData.getValue()!= null){
            view.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            userDAO.excludeImageStorage(email, o -> {
                Toast toast = Toast.makeText(context, "Foto de perfil excluída!", Toast.LENGTH_SHORT);
                toast.show();
                profilePictureLiveData.setValue(null);
                progressBar.setVisibility(View.INVISIBLE);
                view.setVisibility(View.GONE);
            }, e -> {
            });
            userDAO.excludeProfilePicture(task -> {
            }, e -> {

            })  ;
        }
    }
}
