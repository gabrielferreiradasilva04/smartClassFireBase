package com.gabriel.smartclass.dao;

import com.gabriel.smartclass.model.AppUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class AppUserDAO {
    private final String COLLECTION = "appUsers";
    FirebaseFirestore fb = FirebaseFirestore.getInstance();

    public void insert(AppUser appUser){
        fb.collection(this.COLLECTION).add(appUser);
    }
    public void getUserInstitutions(OnCompleteListener<QuerySnapshot> onCompleteListener, OnFailureListener onFailureListener){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        fb.collection("appUsers").whereEqualTo("userAuth_Id", auth.getCurrentUser().getUid()).get().addOnCompleteListener(onCompleteListener);


    }


}
