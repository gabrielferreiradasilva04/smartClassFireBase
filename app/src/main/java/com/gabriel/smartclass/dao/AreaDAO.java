package com.gabriel.smartclass.dao;

import android.util.Log;

import com.gabriel.smartclass.model.Area;
import com.gabriel.smartclass.model.Institution;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Objects;

public class AreaDAO {
    private final String COLLECTION = Area.class.getSimpleName();
    private final String INSTITUTIONCOLLECTION = Institution.class.getSimpleName();

    public void addNewArea(Area area, String institutionID, OnCompleteListener<DocumentReference> onCompleteListener, OnFailureListener onFailureListener){
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        DocumentReference institutionReference = fb.collection(INSTITUTIONCOLLECTION).document(institutionID);
        institutionReference.collection(COLLECTION).add(area).addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }
    public void findAreaByDescription(String description, String institutionID, OnCompleteListener<QuerySnapshot> onCompleteListener){
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        DocumentReference institutionReference = fb.collection(INSTITUTIONCOLLECTION).document(institutionID);
        institutionReference.collection(COLLECTION).whereEqualTo("description", description).get().addOnCompleteListener(onCompleteListener);
    }
    public void getAllAreas(String institutionID, OnCompleteListener<QuerySnapshot> onCompleteListener){
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        Log.d("LOG DO INSTITUTIONID", "getAllAreas: "+ institutionID);
        fb.collection(INSTITUTIONCOLLECTION).document(institutionID).collection(COLLECTION).get().addOnCompleteListener(onCompleteListener);
    }
    public void updateArea(String areaID, String institutionID, HashMap<String, Object> updates){
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        DocumentReference areaReference = fb.collection(INSTITUTIONCOLLECTION)
                .document(institutionID)
                .collection(COLLECTION)
                .document(areaID);
        areaReference.update(updates);
    }
    public DocumentReference getAreaReferenceByID(String institutionID, String areaID){
        return FirebaseFirestore.getInstance().collection(INSTITUTIONCOLLECTION).document(institutionID).collection(COLLECTION).document(areaID);
    }
}
