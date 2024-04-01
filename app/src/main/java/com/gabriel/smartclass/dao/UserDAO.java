package com.gabriel.smartclass.dao;

import android.net.Uri;
import android.util.Log;

import com.gabriel.smartclass.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class UserDAO {
    private final String COLLECTION = "appUsers";
    FirebaseFirestore fb = FirebaseFirestore.getInstance();

    public void insert(User user){
        fb.collection(this.COLLECTION).add(user);
    }
    public void getUserInstitutions(OnCompleteListener<DocumentSnapshot> onCompleteListener, OnFailureListener onFailureListener){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        fb.collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }

    public void updateProfile(String displayName, OnSuccessListener onSuccessListener){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest changes = new UserProfileChangeRequest.Builder().setDisplayName(displayName).build();
        user.updateProfile(changes).addOnSuccessListener(onSuccessListener);
    }
    public void updatePassword(String newPassword, OnCompleteListener<Void> onCompleteListener, OnFailureListener onFailureListener){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updatePassword(newPassword).addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }
    public void excludeProfilePicture(OnCompleteListener onCompleteListener, OnFailureListener onFailureListener){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest update = new UserProfileChangeRequest.Builder().setPhotoUri(null).build();
        user.updateProfile(update).addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }


}
