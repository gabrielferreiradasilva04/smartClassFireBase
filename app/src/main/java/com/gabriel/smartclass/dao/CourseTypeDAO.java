package com.gabriel.smartclass.dao;

import com.gabriel.smartclass.model.CourseType;
import com.gabriel.smartclass.model.Institution;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CourseTypeDAO {
    private final String COLLECTION = CourseType.class.getSimpleName();
    private final String INSTITUTIONCOLLECTION = Institution.class.getSimpleName();

    public void addNewCourseType(CourseType courseType, String institutionID, OnCompleteListener<DocumentReference> onCompleteListener, OnFailureListener onFailureListener){
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        DocumentReference institutionReference = fb.collection(INSTITUTIONCOLLECTION).document(institutionID);
        institutionReference.collection(COLLECTION).add(courseType).addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }
}
