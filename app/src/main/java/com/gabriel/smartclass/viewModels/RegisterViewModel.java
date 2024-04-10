package com.gabriel.smartclass.viewModels;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.dao.InstitutionDAO;
import com.gabriel.smartclass.dao.UserAuthDAO;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.User;
import com.gabriel.smartclass.utilities.validate.CNPJValidator;
import com.gabriel.smartclass.view.auth.InstitutionRegisterForm;
import com.gabriel.smartclass.view.auth.LoginForm;
import com.gabriel.smartclass.view.auth.RegisterForm;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class RegisterViewModel extends ViewModel {
    private UserAuthDAO userAuthDAO;
    private MutableLiveData<String> snackBarText = new MutableLiveData<>();
    private RegisterForm registerForm;

    public RegisterViewModel(RegisterForm registerForm){
        this.userAuthDAO = new UserAuthDAO();
        this.registerForm = registerForm;
    }

    public MutableLiveData<String> getSnackBarText() {
        return snackBarText;
    }

    public void registerUser(String email, String password, String firstAndLastName, String confirmPassword){
        ProgressBar progressBar = registerForm.findViewById(R.id.progressbar_register);
        progressBar.setVisibility(View.VISIBLE);
        if(!email.equals("") && !password.equals("") && !firstAndLastName.equals("")){
            email = email.trim();
            password = password.trim();
            firstAndLastName = firstAndLastName.trim();
            confirmPassword = confirmPassword.trim();
            String finalFirstAndLastName = firstAndLastName;
            if(password.equals(confirmPassword)){
                this.userAuthDAO.CreateNewUserByEmailAndPassword(email, password, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser userAuth = UserAuthDAO.auth.getCurrentUser();
                        if (userAuth != null) {
                            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(finalFirstAndLastName).build();
                            userAuth.updateProfile(profileUpdate);
                            User user = new User();
                            user.setId(userAuth.getUid());
                            user.setInstitutions(new ArrayList<>());
                            FirebaseFirestore.getInstance().collection("users").document(userAuth.getUid()).set(user).addOnSuccessListener(task1 -> {
                                progressBar.setVisibility(View.GONE);
                                snackBarText.setValue("Cadastro realizado com sucesso!");
                                Intent i = new Intent(registerForm.getApplicationContext(), LoginForm.class);
                                registerForm.startActivity(i);
                            }).addOnFailureListener(e1 -> {
                                snackBarText.setValue("Ops.. algo deu errado, tente novamente mais tarde: " + e1.getMessage());
                                userAuth.delete();
                                progressBar.setVisibility(View.GONE);
                            });
                        }
                    }else{
                        snackBarText.setValue("Ops.. algo deu errado, tente novamente mais tarde");
                        progressBar.setVisibility(View.GONE);
                    }
                }, e-> {
                        if(e.getClass().equals(FirebaseAuthWeakPasswordException.class)){
                            snackBarText.setValue("A senha deve conter pelo menos 6 caractéres!");
                            progressBar.setVisibility(View.GONE);
                        }
                        else if (e.getClass() == FirebaseAuthEmailException.class){
                            snackBarText.setValue("Verifique o e-mail informado");
                            progressBar.setVisibility(View.GONE);
                        }
                        else if (e.getClass().equals(FirebaseAuthUserCollisionException.class)) {
                            snackBarText.setValue("Já existe um usuário com esse e-mail");
                            progressBar.setVisibility(View.GONE);
                        } else {
                            snackBarText.setValue("Ops... Algo deu errado, tente novamente mais tarde!");
                            progressBar.setVisibility(View.GONE);
                        }
                });
            }else {
                snackBarText.setValue("As senhas não conferem");
                progressBar.setVisibility(View.GONE);
            }
        }else{
            snackBarText.setValue("Preencha todos os campos");
            progressBar.setVisibility(View.GONE);
        }

    }
    public void registerInstitution(String cnpj, String name, String email, String password, String confirmPassword){
        String finalName = name.trim();
        String finalEmail = email.trim();
        String finalCnpj = cnpj.trim();
        String finalPassword = password.trim();
        String finalConfirmPassword = confirmPassword.trim();
        InstitutionDAO institutionDAO = new InstitutionDAO();
        ProgressBar progressBar = registerForm.findViewById(R.id.progressBar_InstitutionRegister);
        progressBar.setVisibility(View.VISIBLE);
            institutionDAO.getInstitutionByCNPJ(cnpj, task -> {
                if(!task.getResult().isEmpty()){
                    snackBarText.setValue("Já existe uma instituição de ensino com esse CNPJ");
                    progressBar.setVisibility(View.GONE);
                }else{
                    if(!finalEmail.equals("") && !finalName.equals("") && !finalPassword.equals("") && !finalCnpj.equals("")){
                        if(finalPassword.equals(finalConfirmPassword)) {
                            try {
                                if (!CNPJValidator.validateCNPJ(finalCnpj)) {
                                    snackBarText.setValue("CNPJ Inválido, verifique e tente novamente");
                                    progressBar.setVisibility(View.GONE);
                                } else {
                                    userAuthDAO.CreateNewUserByEmailAndPassword(finalEmail, finalPassword, task1 -> {
                                        if (task1.isSuccessful()) {
                                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                            UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder().setDisplayName(finalName).build();
                                            assert currentUser != null;
                                            currentUser.updateProfile(changeRequest);
                                            DocumentReference institutionRef = UserAuthDAO.fb.collection("Institutions").document(currentUser.getUid());
                                            Institution institution;
                                            institution = new Institution();
                                            institution.setId(currentUser.getUid());
                                            institution.setCnpj(finalCnpj);
                                            institution.setName(finalName);
                                            institutionRef.set(institution).addOnSuccessListener(unused -> {
                                                snackBarText.setValue("Cadastro realizado com sucesso!");
                                                progressBar.setVisibility(View.GONE);
                                                Intent i = new Intent(registerForm.getApplicationContext(), LoginForm.class);
                                                registerForm.startActivity(i);
                                            }).addOnFailureListener(e -> {
                                                snackBarText.setValue("Erro ao finalizar cadastro");
                                                currentUser.delete();
                                                progressBar.setVisibility(View.GONE);
                                            });
                                        }

                                    }, e -> {
                                        if (e.getClass().equals(FirebaseAuthEmailException.class)) {
                                            snackBarText.setValue("Verifique o e-mail informado");
                                            progressBar.setVisibility(View.GONE);
                                        }
                                        if (e.getClass().equals(FirebaseAuthWeakPasswordException.class)) {
                                            snackBarText.setValue("A senha deve conter ao menos 6 caractéres");
                                            progressBar.setVisibility(View.GONE);
                                        }
                                        if (e.getClass().equals(FirebaseAuthUserCollisionException.class)) {
                                            snackBarText.setValue("Já existe uma instituição cadastrada com esse e-mail");
                                            progressBar.setVisibility(View.GONE);
                                        }
                                            snackBarText.setValue("Algo deu errado, tente novamente mais tarde");
                                            progressBar.setVisibility(View.GONE);
                                    });
                                }
                            } catch (Exception eCNPJ) {
                                snackBarText.setValue("CNPJ Inválido, verifique e tente novamente");
                                progressBar.setVisibility(View.GONE);
                            }
                        }else{
                            snackBarText.setValue("As senhas não conferem");
                            progressBar.setVisibility(View.GONE);
                        }
                    }else{
                        snackBarText.setValue("Todos os campos são obrigatórios");
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }, e -> {
                snackBarText.setValue("Algo deu errado, tente novamente mais tarde!");
                progressBar.setVisibility(View.GONE);
            });

    }
}