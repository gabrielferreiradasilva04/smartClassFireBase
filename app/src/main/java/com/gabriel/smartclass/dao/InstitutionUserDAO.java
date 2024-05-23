package com.gabriel.smartclass.dao;


import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.InstitutionUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class InstitutionUserDAO {
    private final String COLLECTION = InstitutionUser.class.getSimpleName();
    private final String institutionsCollection = Institution.class.getSimpleName();

    private final FirebaseFirestore fb = FirebaseFirestore.getInstance();

    public void saveNewInstitutionUser(InstitutionUser institutionUser, DocumentReference institutionReference, OnCompleteListener<Void> onCompleteListener, OnFailureListener onFailureListener){
        CollectionReference institutionUserCollection = fb.collection(institutionsCollection).document(institutionReference.getId()).collection(COLLECTION);
        institutionUserCollection.document(institutionUser.getId()).set(institutionUser).addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }
    public void getInstitutionUserById(String userID, String institutionID, OnCompleteListener<DocumentSnapshot> onCompleteListener, OnFailureListener onFailureListener){
       DocumentReference institutionReference = fb.collection(institutionsCollection).document(institutionID);
           institutionReference.collection(COLLECTION)
                   .document(userID).get()
                   .addOnCompleteListener(onCompleteListener)
                   .addOnFailureListener(onFailureListener);
    }
    public static boolean verifyUserAccess(InstitutionUser institutionUser){
        return institutionUser.isActive();
    }
}
