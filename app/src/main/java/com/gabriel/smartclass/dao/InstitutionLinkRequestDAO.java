package com.gabriel.smartclass.dao;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.InstitutionLinkRequest;
import com.gabriel.smartclass.viewModels.InstitutionLinkRequestViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class InstitutionLinkRequestDAO {
    FirebaseFirestore fb = FirebaseFirestore.getInstance();
    InstitutionLinkRequestViewModel viewModel;

    public InstitutionLinkRequestDAO(InstitutionLinkRequestViewModel viewModel){
        this.viewModel = viewModel;
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


}
