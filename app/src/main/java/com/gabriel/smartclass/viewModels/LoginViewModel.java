package com.gabriel.smartclass.viewModels;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.gabriel.smartclass.DAO.UserAuthDAO;
import com.gabriel.smartclass.view.LoginForm;
import com.gabriel.smartclass.view.StudentMainMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginViewModel extends ViewModel {
    FirebaseUser userAuth;
    UserAuthDAO userAuthDAO;

    public LoginViewModel(){
        userAuthDAO = new UserAuthDAO();
    }

    public void loginUser(String email, String password, Context context){
        userAuthDAO.signInwithEmailAndPassword(email, password, new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful() && UserAuthDAO.auth != null ){
                    Intent i = new Intent(context, StudentMainMenu.class);
                    context.startActivity(i);
                }

            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String errorMessage = "";
                if(e.getClass().equals(FirebaseAuthInvalidCredentialsException.class)){
                    errorMessage = "Credenciais inválidas";
                }
                if(e.getClass().equals(FirebaseNetworkException.class)){
                    errorMessage = "Sem conexão com a internet, tente novamente mais tarde";
                }
                Toast toast = Toast.makeText(context, errorMessage, Toast.LENGTH_LONG);
                toast.show();

            }
        });
    }



}
