package com.gabriel.smartclass.dao;

import com.gabriel.smartclass.model.Classroom;
import com.gabriel.smartclass.model.Course;
import com.gabriel.smartclass.model.Institution;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ClassroomDAO {
    private final String COLLECTION = Classroom.class.getSimpleName();
    private final String INSTITUTIONCOLLECTION = Institution.class.getSimpleName();
    private final String COURSECOLLECTION = Course.class.getSimpleName();

    public void saveClassroom(String institutionID, String courseID, Classroom classroom, OnCompleteListener<DocumentReference> onCompleteListener){
        FirebaseFirestore.getInstance().collection(INSTITUTIONCOLLECTION).document(institutionID).collection(COURSECOLLECTION).document(courseID).collection(COLLECTION).add(classroom).addOnCompleteListener(onCompleteListener);
    }
}
