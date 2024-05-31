package com.gabriel.smartclass.dao;

import com.gabriel.smartclass.model.Course;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.Subject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class SubjectDAO {
    private final String COLLECTION = Subject.class.getSimpleName();
    private final String COURSECOLLECTION = Course.class.getSimpleName();
    private final String INSTITUTIONCOLLECTION = Institution.class.getSimpleName();

    public void addNewSubject(String institutionID, String courseID, Subject subject, OnCompleteListener<DocumentReference>onCompleteListener, OnFailureListener onFailureListener){
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        fb.collection(INSTITUTIONCOLLECTION).document(institutionID).collection(COURSECOLLECTION).document(courseID).collection(COLLECTION).add(subject)
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(onFailureListener);
    }
    public void updateSubject(String institutionID, String courseID, String subjectID, HashMap<String, Object>updates, OnCompleteListener<Void> onCompleteListener, OnFailureListener onFailureListener){
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        fb.collection(INSTITUTIONCOLLECTION).document(institutionID).collection(COURSECOLLECTION).document(courseID).collection(COLLECTION).document(subjectID).update(updates)
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(onFailureListener);
    }
    public void getAllSubjectsFromCourse(String institutionID, String courseID, OnCompleteListener<QuerySnapshot> onCompleteListener, OnFailureListener onFailureListener){
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        fb.collection(INSTITUTIONCOLLECTION).document(institutionID).collection(COURSECOLLECTION).document(courseID).collection(COLLECTION).get().addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }
    public void removeSubjectFromCourse(String institutionID, String courseID, String subjectID, OnCompleteListener<Void> onCompleteListener, OnFailureListener onFailureListener){
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        fb.collection(INSTITUTIONCOLLECTION).document(institutionID).collection(COURSECOLLECTION).document(courseID).collection(COLLECTION).document(subjectID)
                .delete()
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(onFailureListener);
    }

}
