package com.gabriel.smartclass.dao;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserAuthDAO {
    public static FirebaseAuth auth = FirebaseAuth.getInstance();
    public static FirebaseFirestore fb = FirebaseFirestore.getInstance();
    public void CreateNewUserByEmailAndPassword(String email, String password, OnCompleteListener onCompleteListener, OnFailureListener onFailureListener){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
//        AppUser appUser = new AppUser();
//        appUser.setUserAuth_Id(auth.getUid());
//        appUser.setInstitutions(new ArrayList<>());
//        AppUserDAO appUserDAO = new AppUserDAO();
//        appUserDAO.insert(appUser);
    }

    public void signInwithEmailAndPassword(String email, String password, OnCompleteListener onCompleteListener, OnFailureListener onFailureListener){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);

    }


}

