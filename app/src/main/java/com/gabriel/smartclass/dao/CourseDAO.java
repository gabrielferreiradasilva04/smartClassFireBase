package com.gabriel.smartclass.dao;

import com.gabriel.smartclass.model.Course;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.Student;
import com.gabriel.smartclass.model.Subject;
import com.gabriel.smartclass.model.Teacher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CourseDAO {
    private final String COLLECTION = Course.class.getSimpleName();
    private final String INSTITUTIONCOLLECTION = Institution.class.getSimpleName();
    private final String SUBJECTSCOLLECTION = Subject.class.getSimpleName();
    private final String STUDENTSCOLLECION = Student.class.getSimpleName();
    private final String TEACHERSCOLLECTION = Teacher.class.getSimpleName();
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
    public void addSubjectsOnCourse(String courseID, String institutionID, Subject subject, OnCompleteListener<DocumentReference> onCompleteListener, OnFailureListener onFailureListener){
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        fb.collection(INSTITUTIONCOLLECTION).document(institutionID).collection(COLLECTION).document(courseID)
                .collection(SUBJECTSCOLLECTION)
                .add(subject)
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(onFailureListener);
    }
    public void removeSubjectFromCourse(String institutionID, String courseID, String subjectID, OnCompleteListener<Void> onCompleteListener, OnFailureListener onFailureListener){
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        fb.collection(INSTITUTIONCOLLECTION).document(institutionID).collection(COLLECTION).document(courseID).collection(SUBJECTSCOLLECTION)
                .document(subjectID)
                .delete()
                .addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }
    public void addStudent(String institutionID, String courseID, Student student, OnCompleteListener<Void> onCompleteListener, OnFailureListener onFailureListener){
        FirebaseFirestore.getInstance()
                .collection(INSTITUTIONCOLLECTION)
                .document(institutionID)
                .collection(COLLECTION)
                .document(courseID)
                .collection(STUDENTSCOLLECION)
                .document(student.getId())
                .set(student)
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(onFailureListener);

    }
    public void removeStudent(String institutionID, String courseID, Student student, OnCompleteListener<Void> onCompleteListener, OnFailureListener onFailureListener){
        FirebaseFirestore.getInstance()
                .collection(INSTITUTIONCOLLECTION)
                .document(institutionID)
                .collection(COLLECTION)
                .document(courseID)
                .collection(STUDENTSCOLLECION)
                .document(student.getId()).delete()
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(onFailureListener);
    }
    public void addTeacher(String institutionID, String courseID, Teacher teacher, OnCompleteListener<Void> onCompleteListener, OnFailureListener onFailureListener){
        FirebaseFirestore.getInstance()
                .collection(INSTITUTIONCOLLECTION)
                .document(institutionID)
                .collection(COLLECTION)
                .document(courseID)
                .collection(TEACHERSCOLLECTION)
                .document(teacher.getId())
                .set(teacher)
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(onFailureListener);
    }
    public void removeTeacher(String institutionID, String courseID, Teacher teacher, OnCompleteListener<Void> onCompleteListener, OnFailureListener onFailureListener){
        FirebaseFirestore.getInstance()
                .collection(INSTITUTIONCOLLECTION)
                .document(institutionID)
                .collection(COLLECTION)
                .document(courseID)
                .collection(TEACHERSCOLLECTION)
                .document(teacher.getId()).delete()
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(onFailureListener);
    }
    public void getAllTeachersAndStudents(String institutionID, String courseID, final OnCompleteListener<List<DocumentReference>> onCompleteListener){
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        Task<QuerySnapshot> teacherCollectionList = fb.collection(INSTITUTIONCOLLECTION).document(institutionID).collection(COLLECTION).document(courseID).collection(TEACHERSCOLLECTION).get();
        Task<QuerySnapshot> studentsCollectionList = fb.collection(INSTITUTIONCOLLECTION).document(institutionID).collection(COLLECTION).document(courseID).collection(STUDENTSCOLLECION).get();
        Tasks.whenAllComplete(teacherCollectionList, studentsCollectionList).addOnCompleteListener(task -> {
                    List<DocumentReference> combinedList = new ArrayList<>();
                    if (task.isSuccessful()) {
                        QuerySnapshot teachersSnapshot = teacherCollectionList.getResult();
                        QuerySnapshot studentsSnapshot = studentsCollectionList.getResult();
                        if (teachersSnapshot != null) {
                            for (QueryDocumentSnapshot document : teachersSnapshot) {
                                combinedList.add(document.getReference());
                            }
                        }
                        if (studentsSnapshot != null) {
                            for (QueryDocumentSnapshot document : studentsSnapshot) {
                                combinedList.add(document.getReference());
                            }
                        }
                        onCompleteListener.onComplete(Tasks.forResult(combinedList));
                    } else {
                        onCompleteListener.onComplete(Tasks.forException(task.getException()));
                    }
                });
    }
}
