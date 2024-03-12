package com.gabriel.smartclass.dao;

import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.gabriel.smartclass.model.UserType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserTypeDao {
    private final String COLLNAME = "userTypes";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void insert(UserType userType) {
        db.collection(this.COLLNAME).add(userType).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("Sucesso!", "Document save with success! - " + documentReference.getId());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ERRO!", "onFailure: " + e);
            }
        });
    }

    public void findById(String id) {
        db.collection(this.COLLNAME).whereEqualTo("description", "Student").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("CONSULTA", document.getId() + " => " + document.getData());
                    }
                }
            }
        });
    }

}
