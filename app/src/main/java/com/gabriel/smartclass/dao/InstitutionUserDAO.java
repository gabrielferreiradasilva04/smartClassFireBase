package com.gabriel.smartclass.dao;


import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.InstitutionUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

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
    public void getInstitutionUserByUserType(DocumentReference userTypeID, String institutionID, OnCompleteListener<QuerySnapshot> onCompleteListener, OnFailureListener onFailureListener){
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        fb.collection(institutionsCollection).document(institutionID).collection(COLLECTION).whereEqualTo("userType_id", userTypeID).get().addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }
    public DocumentReference getInstitutionUserReferenceByID(String institutionID, String institutionUserID){
        return FirebaseFirestore.getInstance().collection(institutionsCollection).document(institutionID).collection(COLLECTION).document(institutionUserID);
    }
    public void getAllInstitutionUsers(String institutionID, OnCompleteListener<QuerySnapshot> onCompleteListener, OnFailureListener onFailureListener){
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        fb.collection(institutionsCollection).document(institutionID).collection(COLLECTION).get().addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }
    public void deleteInstitutionUser(String institutionID, String institutionUserID, OnCompleteListener<Void> onCompleteListener, OnFailureListener onFailureListener){
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        fb.collection(institutionsCollection).document(institutionID).collection(COLLECTION).document(institutionUserID).delete().addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }
}
