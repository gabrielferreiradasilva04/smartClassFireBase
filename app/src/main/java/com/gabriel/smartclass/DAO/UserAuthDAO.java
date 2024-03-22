package com.gabriel.smartclass.DAO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;

import com.google.firebase.auth.FirebaseAuth;

public class UserAuthDAO {
    public static FirebaseAuth auth = FirebaseAuth.getInstance();
    public void CreateNewUserByEmailAndPassword(String email, String password, OnCompleteListener onCompleteListener, OnFailureListener onFailureListener){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }

    public void signInwithEmailAndPassword(String email, String password, OnCompleteListener onCompleteListener, OnFailureListener onFailureListener){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }


}

