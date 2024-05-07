package com.gabriel.smartclass.dao;

import androidx.annotation.NonNull;

import com.gabriel.smartclass.model.InstitutionLinkRequest;
import com.gabriel.smartclass.viewModels.InstitutionLinkRequestFormViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class InstitutionLinkRequestDAO {
    FirebaseFirestore fb = FirebaseFirestore.getInstance();
    private final String institutionsCollection = "Institutions";

    InstitutionLinkRequestFormViewModel viewModel;

    public InstitutionLinkRequestDAO(InstitutionLinkRequestFormViewModel viewModel){
        this.viewModel = viewModel;
    }
    public InstitutionLinkRequestDAO(){

    }
    private final String COLLECTION = "linkRequests";

    public void createNewLinkRequest(InstitutionLinkRequest institutionLinkRequest, DocumentReference institutionReference, OnCompleteListener onCompleteListener, OnFailureListener onFailureListener) throws RuntimeException{

        institutionReference.collection(COLLECTION).whereEqualTo("user", institutionLinkRequest.getUser()).whereEqualTo("approved", false).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.getResult().isEmpty()) {
                    institutionReference.collection(COLLECTION).add(institutionLinkRequest).addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
                }else{
                    viewModel.getSnackbarText().setValue("Você já possui uma solicitação em aberto para essa instituição");
                }
            }
        });
    }
    public void getAllLinkRequestsByInstitution(String institutionID, OnCompleteListener<QuerySnapshot> onCompleteListener, OnFailureListener onFailureListener){
        DocumentReference institutionRef = fb.collection(institutionsCollection).document(institutionID);
        institutionRef.collection(COLLECTION).get().addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }
    public void updateInstitutionLinkRequest(String linkRequestID, String institutionID, HashMap<String, Object> updates, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener){
        DocumentReference institutionReference = fb.collection(institutionsCollection).document(institutionID);
        DocumentReference linkRequestReference = institutionReference.collection(COLLECTION).document(linkRequestID);
        linkRequestReference.update(updates).addOnSuccessListener(onSuccessListener).addOnFailureListener(onFailureListener);
    }


}
