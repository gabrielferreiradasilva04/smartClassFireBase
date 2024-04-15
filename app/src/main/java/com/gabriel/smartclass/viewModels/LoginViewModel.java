package com.gabriel.smartclass.viewModels;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.dao.UserAuthDAO;
import com.gabriel.smartclass.dao.UserDAO;
import com.gabriel.smartclass.view.InstitutionMainMenu;
import com.gabriel.smartclass.view.StudentMainMenu;
import com.gabriel.smartclass.view.auth.LoginForm;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

public class LoginViewModel extends ViewModel {
    FirebaseUser userAuth;
    UserAuthDAO userAuthDAO;
    private LoginForm loginForm;
    private MutableLiveData<String> snackBarText = new MutableLiveData<>();

    public LoginViewModel(LoginForm loginForm){
        userAuthDAO = new UserAuthDAO();
        this.loginForm = loginForm;
    }

    public MutableLiveData<String> getSnackBarText() {
        return snackBarText;
    }

    public void loginUser(String email, String password){
        ProgressBar progressBar = loginForm.findViewById(R.id.progressbar_login);
        progressBar.setVisibility(View.VISIBLE);

        if(!email.equals("") && !password.equals("")){
            userAuthDAO.signInwithEmailAndPassword(email, password, new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful() && UserAuthDAO.auth != null ){
                        userAuthDAO.identifyUserOrInstitution(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                //encontrou usuário comum
                                if(task.getResult().exists()){
                                    Intent i = new Intent(loginForm.getApplicationContext(), InstitutionMainMenu.class);
                                    loginForm.startActivity(i);
                                    progressBar.setVisibility(View.GONE);
                                    loginForm.finish();
                                }else{
                                    progressBar.setVisibility(View.GONE);
                                    Intent i = new Intent(loginForm.getApplicationContext(), StudentMainMenu.class);
                                    loginForm.startActivity(i);
                                    loginForm.finish();
                                }
                            }
                        });
                    }
                }
            }, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String errorMessage = "";
                    if(e.getClass().equals(FirebaseAuthInvalidCredentialsException.class)){
                        snackBarText.setValue("Credenciais inválidas");
                        progressBar.setVisibility(View.GONE);
                    }
                    if(e.getClass().equals(FirebaseNetworkException.class)){
                        snackBarText.setValue("Sem conexão com a internet, tente novamente mais tarde");
                        progressBar.setVisibility(View.GONE);
                    }

                }
            });
        }else{
            snackBarText.setValue("Preencha todos os campos");
            progressBar.setVisibility(View.GONE);
        }
    }

}
