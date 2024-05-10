package com.gabriel.smartclass.dao;


import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.InstitutionUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class InstitutionUserDAO {
    private final String COLLECTION = InstitutionUser.class.getSimpleName();
    private final String institutionsCollection = Institution.class.getSimpleName();

    private final FirebaseFirestore fb = FirebaseFirestore.getInstance();

    public void saveNewInstitutionUser(InstitutionUser institutionUser, DocumentReference institutionReference, OnCompleteListener<DocumentReference> onCompleteListener, OnFailureListener onFailureListener){
        fb.collection(institutionsCollection).document(institutionReference.getId()).collection(COLLECTION).add(institutionUser).addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }
}
