package com.gabriel.smartclass.dao;

import android.util.Log;

import com.gabriel.smartclass.model.Classroom;
import com.gabriel.smartclass.model.ClassroomSubject;
import com.gabriel.smartclass.model.Course;
import com.gabriel.smartclass.model.Institution;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClassroomDAO {
    private final String COLLECTION = Classroom.class.getSimpleName();
    private final String INSTITUTIONCOLLECTION = Institution.class.getSimpleName();
    private final String COURSECOLLECTION = Course.class.getSimpleName();

    public void saveClassroom(String institutionID, String courseID, Classroom classroom, OnCompleteListener<DocumentReference> onCompleteListener) {
        FirebaseFirestore.getInstance().collection(INSTITUTIONCOLLECTION).document(institutionID).collection(COURSECOLLECTION).document(courseID).collection(COLLECTION).add(classroom).addOnCompleteListener(onCompleteListener);
    }

    public void getAllClassrooms(String institutionID, String courseID, OnCompleteListener<List<Classroom>> onCompleteListener) {
        Task<QuerySnapshot> classroomTask = FirebaseFirestore.getInstance().collection(INSTITUTIONCOLLECTION).document(institutionID).collection(COURSECOLLECTION).document(courseID).collection(COLLECTION).get();
        Tasks.whenAllComplete(classroomTask).addOnCompleteListener(mainTask -> {
            if (mainTask.isSuccessful()) {
                List<Classroom> classrooms = new ArrayList<>();
                QuerySnapshot snapshot = classroomTask.getResult();
                if (snapshot != null) {
                    classrooms = snapshot.toObjects(Classroom.class);
                } else {
                    classrooms = new ArrayList<>();
                }
                onCompleteListener.onComplete(Tasks.forResult(classrooms));
            } else {
                onCompleteListener.onComplete(Tasks.forException(mainTask.getException()));
            }
        });
    }

    public void updateClassroom(String institutionID, String courseID, String classroomID, HashMap<String, Object> update) {
        DocumentReference classroomReference = FirebaseFirestore.getInstance().collection(INSTITUTIONCOLLECTION).document(institutionID).collection(COURSECOLLECTION).document(courseID).collection(COLLECTION).document(classroomID);
        classroomReference.update(update);
    }

    public void addSubjectsOnClassroom(String institutionID, String courseID, String classroomID, List<ClassroomSubject> list) {
        CollectionReference reference = FirebaseFirestore.getInstance().collection(INSTITUTIONCOLLECTION).document(institutionID).collection(COURSECOLLECTION).document(courseID).collection(COLLECTION).document(classroomID).collection(ClassroomSubject.class.getSimpleName());
        WriteBatch batch = FirebaseFirestore.getInstance().batch();

        for (ClassroomSubject subject:list) {
            batch.set(reference.document(), subject);
            Log.d("teste", "addSubjectsOnClassroom: chamou");
        }
        batch.commit();
    }
    public void liveSyncClassroom(String institutionID, String courseID, String classroomID, EventListener<DocumentSnapshot> eventListener){
       FirebaseFirestore.getInstance().collection(INSTITUTIONCOLLECTION).document(institutionID).collection(COURSECOLLECTION).document(courseID).collection(COLLECTION).document(classroomID).addSnapshotListener(eventListener);
    }
}
