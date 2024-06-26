package com.gabriel.smartclass.dao;

import com.gabriel.smartclass.model.Course;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.InstitutionUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class InstitutionDAO {
    private final FirebaseFirestore db ;
    private final String SEARCHAUXSTRING = "\uffff";
    private final String COLLECTION = Institution.class.getSimpleName();
    private Institution institution;
    public InstitutionDAO(){
        db = FirebaseFirestore.getInstance();
    }

    public void insert(Institution institution) throws Exception{
        db.collection(COLLECTION).add(institution);
    }
    public void update(HashMap<String, Object> updateData,String institutionId, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener){
        DocumentReference instRef = db.collection(COLLECTION).document(institutionId);
        instRef.update(updateData).addOnSuccessListener(onSuccessListener).addOnFailureListener(onFailureListener);
    }
    public void listAllInstitutions(OnSuccessListener<QuerySnapshot> onSuccessListener){
        db.collection(COLLECTION).get().addOnSuccessListener(onSuccessListener);
    }

    public void getInstitutionById(String id, OnSuccessListener onSuccessListener, OnFailureListener onFailureListener){
        db.collection(COLLECTION).document(id).get().addOnSuccessListener(onSuccessListener).addOnFailureListener(onFailureListener);
    }
    public void getInstitutionByName(String name, OnCompleteListener<QuerySnapshot> onCompleteListener, OnFailureListener onFailureListener){
        db.collection(COLLECTION)
                .whereGreaterThanOrEqualTo("name", name).whereLessThanOrEqualTo("name", name+SEARCHAUXSTRING).get()
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(onFailureListener);

    }
    public void getInstitutionByCNPJ(String cnpj,OnCompleteListener<QuerySnapshot> onCompleteListener, OnFailureListener onFailureListener){
        db.collection(COLLECTION).whereEqualTo("cnpj", cnpj).get().addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }
    public void getInstitutionByCurrentUser(OnCompleteListener<DocumentSnapshot> onCompleteListener, OnFailureListener onFailureListener){
        db.collection(COLLECTION).document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }
    public void syncChangesInRealTime(String institutionID, EventListener<DocumentSnapshot> eventListener){
        DocumentReference institutionRef = db.collection(COLLECTION).document(institutionID);
        institutionRef.addSnapshotListener(eventListener);
    }
    public void getInstitutionUsers(String institutionID, OnCompleteListener<QuerySnapshot> onCompleteListener, OnFailureListener onFailureListener){
        DocumentReference institutionRef = db.collection(COLLECTION).document(institutionID);
        institutionRef.collection(InstitutionUser.class.getSimpleName()).get().addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }
    public void getInstitutionCourses(String institutionID, OnCompleteListener<QuerySnapshot> onCompleteListener, OnFailureListener onFailureListener){
        DocumentReference institutionRef = db.collection(COLLECTION).document(institutionID);
        institutionRef.collection(Course.class.getSimpleName()).get().addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }



}
