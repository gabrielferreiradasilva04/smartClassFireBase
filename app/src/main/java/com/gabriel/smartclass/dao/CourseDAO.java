package com.gabriel.smartclass.dao;

import com.gabriel.smartclass.model.Course;
import com.gabriel.smartclass.model.Institution;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

public class CourseDAO {
    private final String COLLECTION = Course.class.getSimpleName();
    private final String INSTITUTIONCOLLECTION = Institution.class.getSimpleName();

    public void saveNewCourse(Course course, String institutionID, OnCompleteListener<DocumentReference> onCompleteListener, OnFailureListener onFailureListener){
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        DocumentReference institutionReference = fb.collection(INSTITUTIONCOLLECTION).document(institutionID);
        institutionReference.collection(COLLECTION).add(course).addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }


}
