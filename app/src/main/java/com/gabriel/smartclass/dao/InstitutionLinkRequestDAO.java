package com.gabriel.smartclass.dao;

import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.InstitutionLinkRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class InstitutionLinkRequestDAO {
    FirebaseFirestore fb = FirebaseFirestore.getInstance();
    private final String COLLECTION = "LinkRequests";

    public void createNewLinkRequest(InstitutionLinkRequest institutionLinkRequest, Institution institution, OnCompleteListener onCompleteListener, OnFailureListener onFailureListener){
        DocumentReference documentReference = fb.collection("institution").document(institution.getId());
        documentReference.collection(COLLECTION).add(institutionLinkRequest).addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);

    }


}
