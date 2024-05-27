package com.gabriel.smartclass.dao;

import com.gabriel.smartclass.model.Area;
import com.gabriel.smartclass.model.Institution;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AreaDAO {
    private final String COLLECTION = Area.class.getSimpleName();
    private final String INSTITUTIONCOLLECTION = Institution.class.getSimpleName();

    public void addNewArea(Area area, String institutionID, OnCompleteListener<DocumentReference> onCompleteListener, OnFailureListener onFailureListener){
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        DocumentReference institutionReference = fb.collection(INSTITUTIONCOLLECTION).document(institutionID);
        institutionReference.collection(COLLECTION).add(area).addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }
}
