package com.gabriel.smartclass.dao;
import com.gabriel.smartclass.model.Institution;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class InstitutionDAO {
    private FirebaseFirestore db ;
    private Institution institution;
    public InstitutionDAO(){
        db = FirebaseFirestore.getInstance();
    }
    private final String COLLECTION = "Institutions";

    public void insert(Institution institution) throws Exception{
        db.collection(COLLECTION).add(institution);
    }
    public void update(Map<String, Object> updateData) throws Exception{
        DocumentReference instRef = db.collection(COLLECTION).document(institution.getId());
        instRef.update(updateData);
    }
    public void listAllInstitutions(OnSuccessListener<QuerySnapshot> onSuccessListener){
        db.collection(COLLECTION).get().addOnSuccessListener(onSuccessListener);
    }

    public void getInstitutionById(String id, OnSuccessListener onSuccessListener, OnFailureListener onFailureListener){
        db.collection(COLLECTION).document(id).get().addOnSuccessListener(onSuccessListener).addOnFailureListener(onFailureListener);
    }
    public void getInstitutionByName(String name, OnCompleteListener<QuerySnapshot> onCompleteListener, OnFailureListener onFailureListener){
        db.collection(COLLECTION).whereEqualTo("name", name).get().addOnCompleteListener(onCompleteListener);

    }

}
