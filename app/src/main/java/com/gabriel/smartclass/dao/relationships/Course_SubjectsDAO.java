package com.gabriel.smartclass.dao.relationships;

import com.gabriel.smartclass.model.Course;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.relationships.Course_Subject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

public class Course_SubjectsDAO {
    private final String COLLECTION = Course_Subject.class.getSimpleName();
    private final String INSTITUTIONCOLLECTION = Institution.class.getSimpleName();
    private final String COURSECOLLECTION = Course.class.getSimpleName();

    public void addNewSubjectOnCourse(String institutionID, Course_Subject relationship, OnCompleteListener<DocumentReference> onCompleteListener, OnFailureListener onFailureListener) {
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        DocumentReference institutionReference = fb.collection(INSTITUTIONCOLLECTION).document(institutionID);
        institutionReference.collection(COLLECTION).add(relationship).addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }
    public void getSubjectByCourse(String courseID, String institutionID, OnCompleteListener<QuerySnapshot> onCompleteListener, OnFailureListener onFailureListener){
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        DocumentReference institutionReference = fb.collection(INSTITUTIONCOLLECTION).document(institutionID);
        DocumentReference courseReference = institutionReference.collection(COURSECOLLECTION).document(courseID);
        institutionReference.collection(COLLECTION).whereEqualTo("course_reference", courseReference).get().addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }

}
