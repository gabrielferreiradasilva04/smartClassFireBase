package com.gabriel.smartclass.viewModels;

import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.dao.UserAuthDAO;
import com.gabriel.smartclass.view.InstitutionMainMenu;
import com.gabriel.smartclass.view.StudentMainMenu;
import com.gabriel.smartclass.view.auth.LoginForm;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;

public class LoginViewModel extends ViewModel {
    private UserAuthDAO userAuthDAO;
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
            userAuthDAO.signInwithEmailAndPassword(email, password, task -> {
                if(task.isSuccessful() && UserAuthDAO.auth != null ){
                    userAuthDAO.identifyUserOrInstitution(o -> {
                            if(o.getResult().exists()){
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
                    });
                }
            }, e -> {
                String errorMessage = "";
                if(e.getClass().equals(FirebaseAuthInvalidCredentialsException.class)){
                    snackBarText.setValue("Credenciais inválidas");
                    progressBar.setVisibility(View.GONE);
                }
                if(e.getClass().equals(FirebaseNetworkException.class)){
                    snackBarText.setValue("Sem conexão com a internet, tente novamente mais tarde");
                    progressBar.setVisibility(View.GONE);
                }

            });
        }else{
            snackBarText.setValue("Preencha todos os campos");
            progressBar.setVisibility(View.GONE);
        }
    }

}
