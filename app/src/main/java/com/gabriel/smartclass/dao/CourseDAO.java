package com.gabriel.smartclass.dao;

import android.util.Log;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.model.Course;
import com.gabriel.smartclass.model.Institution;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.Query;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;

public class CourseDAO {
    private final String COLLECTION = Course.class.getSimpleName();
    private final String INSTITUTIONCOLLECTION = Institution.class.getSimpleName();
    private final String SEARCHAUXSTRING = "\uffff";

    public void saveNewCourse(Course course, String institutionID, OnCompleteListener<DocumentReference> onCompleteListener, OnFailureListener onFailureListener){
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        DocumentReference institutionReference = fb.collection(INSTITUTIONCOLLECTION).document(institutionID);
        institutionReference.collection(COLLECTION).add(course).addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }
    public void updateCourse(String courseID, String institutionID, HashMap<String, Object> updates, OnCompleteListener<Void> onCompleteListener, OnFailureListener onFailureListener){
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        fb.collection(INSTITUTIONCOLLECTION).document(institutionID).collection(COLLECTION).document(courseID).update(updates).addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }
    public void findCourseByName(String institutionID, String courseName, OnCompleteListener<QuerySnapshot> onCompleteListener, OnFailureListener onFailureListener){
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        fb.collection(INSTITUTIONCOLLECTION).document(institutionID).collection(COLLECTION).whereEqualTo("name", courseName).get().addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }
    public void simpleSearchCourse(String institutionID, String courseTitle, OnCompleteListener<QuerySnapshot> onCompleteListener, OnFailureListener onFailureListener){
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        fb.collection(INSTITUTIONCOLLECTION).document(institutionID).collection(COLLECTION)
                .whereGreaterThanOrEqualTo("name", courseTitle).whereLessThanOrEqualTo("name", courseTitle+SEARCHAUXSTRING).get()
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(onFailureListener);

    }
    public void deleteCourse(String institutionID, String courseID, OnCompleteListener<Void>onCompleteListener, OnFailureListener onFailureListener){
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        fb.collection(INSTITUTIONCOLLECTION).document(institutionID).collection(COLLECTION).document(courseID)
                .delete()
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(onFailureListener);
    }



}
