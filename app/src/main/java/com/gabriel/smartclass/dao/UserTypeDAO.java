package com.gabriel.smartclass.dao;

import android.util.Log;

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


public class UserTypeDAO {
    private final String COLLECTION = UserType.class.getSimpleName();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public  final DocumentReference STUDENT_TYPE_REFERENCE = db.collection(this.COLLECTION).document("0kpiVHFO4bp3qvoQa5f8");
    public final DocumentReference TEACHER_TYPE_REFERENCE = db.collection(this.COLLECTION).document("7d2e9taUjSBFu4CkmfIX");
    public final DocumentReference COORDINATOR_TYPE_REFERENCE = db.collection(this.COLLECTION).document("VozV7BKIaUn7dyXQN84y");


    public void insert(UserType userType) {
        db.collection(this.COLLECTION).add(userType).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
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
        db.collection(this.COLLECTION).whereEqualTo("description", "Student").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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

    public void getAllUserTypes(OnCompleteListener<QuerySnapshot> onCompleteListener, OnFailureListener onFailureListener){
        db.collection(COLLECTION).get().addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }

    public void getUserTypeByDocumentReference(DocumentReference userTypeReference, OnCompleteListener<DocumentSnapshot> onCompleteListener, OnFailureListener onFailureListener){
        db.collection(COLLECTION).document(userTypeReference.getId()).get().addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }

}
