package com.gabriel.smartclass.dao;

import androidx.annotation.NonNull;

import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.InstitutionLinkRequest;
import com.gabriel.smartclass.model.LinkRequestStatus;
import com.gabriel.smartclass.viewModels.InstitutionLinkRequestFormViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.HashMap;

public class InstitutionLinkRequestDAO {
    FirebaseFirestore fb = FirebaseFirestore.getInstance();
    private final String institutionsCollection = Institution.class.getSimpleName();
    private final String COLLECTION = InstitutionLinkRequest.class.getSimpleName();

    InstitutionLinkRequestFormViewModel viewModel;

    public InstitutionLinkRequestDAO(InstitutionLinkRequestFormViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public InstitutionLinkRequestDAO() {

    }


    public void createNewLinkRequest(InstitutionLinkRequest institutionLinkRequest, DocumentReference institutionReference, OnCompleteListener onCompleteListener, OnFailureListener onFailureListener) throws RuntimeException {

        institutionReference.collection(COLLECTION).whereEqualTo("user", institutionLinkRequest.getUser()).whereEqualTo("linkRequestStatus_id", LinkRequestStatusDAO.PENDING_REFERENCE).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.getResult().isEmpty()) {
                    institutionReference.collection(COLLECTION).add(institutionLinkRequest).addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
                } else {
                    viewModel.getSnackbarText().setValue("Você já realizou uma solicitação para essa instituição recentemente");
                }
            }
        });
    }

    public void getInstitutionLinkRequests(String institutionID, DocumentReference statusReference, OnCompleteListener<QuerySnapshot> onCompleteListener, OnFailureListener onFailureListener) {
        DocumentReference institutionRef = fb.collection(institutionsCollection).document(institutionID);
        if (statusReference != null) {
                institutionRef.collection(COLLECTION).whereEqualTo("linkRequestStatus_id", statusReference).get().addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
        } else {
            institutionRef.collection(COLLECTION).get().addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
        }

    }

    public void updateInstitutionLinkRequest(String linkRequestID, String institutionID, HashMap<String, Object> updates, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        DocumentReference institutionReference = fb.collection(institutionsCollection).document(institutionID);
        DocumentReference linkRequestReference = institutionReference.collection(COLLECTION).document(linkRequestID);
        linkRequestReference.update(updates).addOnSuccessListener(onSuccessListener).addOnFailureListener(onFailureListener);
    }
    public void syncNewLinkRequestInRealTime(String institutionID, DocumentReference statusReference, EventListener<QuerySnapshot> eventListener){
        DocumentReference institutionReference = fb.collection(institutionsCollection).document(institutionID);
        institutionReference.collection(COLLECTION).addSnapshotListener(eventListener);

    }



}
