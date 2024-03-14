package com.gabriel.smartclass.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.gabriel.smartclass.view.LoginForm;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;

public class RegisterController {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private Activity registerActivity;

    public RegisterController(Activity registerActivity){
        this.registerActivity = registerActivity;
    }

    public void CreateNewUserByEmailAndPassword(String email, String password, Context context){
        if(email.isEmpty() || password.isEmpty()){
            Toast toast = Toast.makeText(context, "Preencha todos os campos!", Toast.LENGTH_LONG);
            toast.show();
        }else{
            auth = FirebaseAuth.getInstance();
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(register ->{
                if(register.isSuccessful()){
                    Toast toast = Toast.makeText(context, "Cadastro realizado com sucesso!", Toast.LENGTH_LONG);
                    toast.show();

                    Timer timer = new Timer();
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            Intent i = new Intent(context, LoginForm.class);
                            context.startActivity(i);
                        }
                    };
                    timer.schedule(task, 3000);
                }
            }).addOnFailureListener( e ->{
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
        }

    }

}
