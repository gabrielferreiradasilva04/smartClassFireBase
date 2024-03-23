package com.gabriel.smartclass.dao;

import com.gabriel.smartclass.model.Institution;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class InstitutionDAO {
    private Institution institution;
    private final String COLLECTION = "Institutions";

    public void insert(Institution institution) throws Exception{
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(COLLECTION).add(institution);
    }
    public void update(Map<String, Object> updateData) throws Exception{
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference instRef = db.collection(COLLECTION).document(institution.getId());
        instRef.update(updateData);
    }
    public void listAllInstitutions(OnSuccessListener<QuerySnapshot> onSuccessListener){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(COLLECTION).get().addOnSuccessListener(onSuccessListener);
    }

}
