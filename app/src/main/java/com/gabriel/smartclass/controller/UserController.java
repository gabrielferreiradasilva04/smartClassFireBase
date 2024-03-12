package com.gabriel.smartclass.controller;

import android.util.Log;

import androidx.annotation.NonNull;

import com.gabriel.smartclass.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserController {
    private FirebaseAuth auth;
    private FirebaseUser authUser;
    private FirebaseUser currentUser;
   public void createNewUserWithEmailAndPassword(String email, String password){
       auth = FirebaseAuth.getInstance();

       auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
               FirebaseUser newUser = auth.getCurrentUser();
               currentUser = auth.getCurrentUser();
               Log.d("Criado", "Usuário Criado! ");
           }
       });
   }
   public void updateUser(){
       UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName("Gabriel")
               .build();

        currentUser.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.d("Atualizado", "atualizado");
                }
            }
        });
   }
}
