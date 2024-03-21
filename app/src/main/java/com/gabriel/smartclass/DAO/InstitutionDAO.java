package com.gabriel.smartclass.DAO;

import android.util.Log;

import androidx.annotation.NonNull;

import com.gabriel.smartclass.model.Institution;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InstitutionDAO {
    private Institution institution;
    private List<Institution> allInstitutions;
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
    public List<Institution> listAllInstitutions(){
        this.allInstitutions = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(COLLECTION).get().addOnCompleteListener(task ->{

                for (QueryDocumentSnapshot documentSnapshot: task.getResult()) {
                    Institution institution = new Institution();
                    institution = documentSnapshot.toObject(Institution.class);
                    institution.setId(documentSnapshot.getId());
                    allInstitutions.add(institution);
                }

        }).addOnFailureListener(task ->{
            Log.d("ERRO SEU FUDIDO", "listAllInstitutions: "+"ERRO AO LISTAR INSTITUIÇÕES");
        });
        return allInstitutions;
    }

}
