package com.gabriel.smartclass.viewModels;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.gabriel.smartclass.dao.UserAuthDAO;
import com.gabriel.smartclass.view.LoginForm;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class RegisterViewModel extends ViewModel {
    private UserAuthDAO userAuthDAO;

    public RegisterViewModel(){
        this.userAuthDAO = new UserAuthDAO();
    }

    public void registerUser(String email, String password, Context context){
        this.userAuthDAO.CreateNewUserByEmailAndPassword(email, password, new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    Toast toast = Toast.makeText(context, "Cadastro realizado!", Toast.LENGTH_LONG);
                    toast.show();
                    Intent i = new Intent(context, LoginForm.class);
                    context.startActivity(i);
                }
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
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
            }
        });
    }




}
