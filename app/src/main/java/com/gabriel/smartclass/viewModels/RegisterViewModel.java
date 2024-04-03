package com.gabriel.smartclass.viewModels;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.gabriel.smartclass.dao.UserAuthDAO;
import com.gabriel.smartclass.model.User;
import com.gabriel.smartclass.view.auth.LoginForm;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;

public class RegisterViewModel extends ViewModel {
    private UserAuthDAO userAuthDAO;

    public RegisterViewModel(){
        this.userAuthDAO = new UserAuthDAO();
    }

    public void registerUser(String email, String password, String firstAndLastName, Context context){
        if(!email.equals("") && !password.equals("") && !firstAndLastName.equals("")){
            this.userAuthDAO.CreateNewUserByEmailAndPassword(email, password, new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        FirebaseUser userAuth = UserAuthDAO.auth.getCurrentUser();
                        if(userAuth != null){
                            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(firstAndLastName).build();
                            userAuth.updateProfile(profileUpdate);
                            DocumentReference userRef = UserAuthDAO.fb.collection("users").document(UserAuthDAO.auth.getUid());
                            User user = new User();
                            userRef.set(user);
                            Toast toast = Toast.makeText(context, "Cadastro realizado!", Toast.LENGTH_LONG);
                            toast.show();
                            Intent i = new Intent(context, LoginForm.class);
                            context.startActivity(i);
                        }

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
        }else{
            Toast toast = Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_LONG);
            toast.show();
        }

    }




}
