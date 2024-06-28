package com.gabriel.smartclass.dao;


import com.gabriel.smartclass.model.Course;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.InstitutionUser;
import com.gabriel.smartclass.model.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class InstitutionUserDAO {
    private final String COLLECTION = InstitutionUser.class.getSimpleName();
    private final String institutionsCollection = Institution.class.getSimpleName();
    private final String courseCollection = Course.class.getSimpleName();
    private final String studentCollection = Student.class.getSimpleName();

    private final FirebaseFirestore fb = FirebaseFirestore.getInstance();

    public void saveNewInstitutionUser(InstitutionUser institutionUser, DocumentReference institutionReference, OnCompleteListener<Void> onCompleteListener, OnFailureListener onFailureListener) {
        CollectionReference institutionUserCollection = fb.collection(institutionsCollection).document(institutionReference.getId()).collection(COLLECTION);
        institutionUserCollection.document(institutionUser.getId()).set(institutionUser).addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }

    public void getInstitutionUserById(String userID, String institutionID, OnCompleteListener<DocumentSnapshot> onCompleteListener, OnFailureListener onFailureListener) {
        DocumentReference institutionReference = fb.collection(institutionsCollection).document(institutionID);
        institutionReference.collection(COLLECTION)
                .document(userID).get()
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(onFailureListener);
    }

    public static boolean verifyUserAccess(InstitutionUser institutionUser) {
        return institutionUser.isActive();
    }

    public void getInstitutionUserByUserType(DocumentReference userTypeID, String institutionID, OnCompleteListener<QuerySnapshot> onCompleteListener, OnFailureListener onFailureListener) {
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        fb.collection(institutionsCollection).document(institutionID).collection(COLLECTION).whereEqualTo("userType_id", userTypeID).get().addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }

    public DocumentReference getInstitutionUserReferenceByID(String institutionID, String institutionUserID) {
        return FirebaseFirestore.getInstance().collection(institutionsCollection).document(institutionID).collection(COLLECTION).document(institutionUserID);
    }

    public void getAllInstitutionUsers(String institutionID, OnCompleteListener<QuerySnapshot> onCompleteListener, OnFailureListener onFailureListener) {
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        fb.collection(institutionsCollection).document(institutionID).collection(COLLECTION).whereEqualTo("active", true).get().addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }

    public void deleteInstitutionUser(String institutionID, String institutionUserID, OnCompleteListener<Void> onCompleteListener, OnFailureListener onFailureListener) {
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        fb.collection(institutionsCollection).document(institutionID).collection(COLLECTION).document(institutionUserID).delete().addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }

    public void updateInstitutionUser(String institutionID, String institutionUserID, HashMap<String, Object> updates, OnCompleteListener<Void> onCompleteListener, OnFailureListener onFailureListener) {
        FirebaseFirestore.getInstance().collection(institutionsCollection).document(institutionID).collection(COLLECTION).document(institutionUserID).update(updates).addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }

    public void getInstitutionUserCourses(String institutionID, String institutionUserID, String userType, OnCompleteListener<QuerySnapshot> onCompleteListener, OnFailureListener onFailureListener) {
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        DocumentReference institutionUserReference = fb.collection(institutionsCollection).document(institutionID).collection(COLLECTION).document(institutionUserID);
        DocumentReference institutionReference = fb.collection(institutionsCollection).document(institutionID);
        switch (userType) {
            case "Estudante":
                this.getStudentCourse(institutionReference, institutionUserReference, onCompleteListener, onFailureListener);
                break;
            case "Coordenador":
                getCoordinatorCourse(institutionID, onCompleteListener, onFailureListener, fb, institutionUserReference);
                break;
            case "Professor":
                this.getTeacherCourse(institutionReference, institutionUserReference, onCompleteListener, onFailureListener);
                break;
        }
    }

    private void getTeacherCourse(DocumentReference institutionReference, DocumentReference institutionUserReference,OnCompleteListener<QuerySnapshot> onCompleteListener, OnFailureListener onFailureListener) {
        institutionReference.collection(courseCollection).whereArrayContains("teachers_id", institutionUserReference.getId())
                .get()
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(onFailureListener);
    }

    private void getStudentCourse(DocumentReference institutionReference, DocumentReference institutionUserReference,OnCompleteListener<QuerySnapshot> onCompleteListener, OnFailureListener onFailureListener) {
        institutionReference.collection(courseCollection).whereArrayContains("students_id", institutionUserReference.getId())
                .get()
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(onFailureListener);
    }

    private void getCoordinatorCourse(String institutionID, OnCompleteListener<QuerySnapshot> onCompleteListener, OnFailureListener onFailureListener, FirebaseFirestore fb, DocumentReference institutionUserReference) {
        fb.collection(institutionsCollection).document(institutionID).collection(courseCollection)
                .whereEqualTo("coordinator_id", institutionUserReference)
                .get().addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(onFailureListener);
    }

    public void getInstitutionUserByID(String institutionID ,String userId, OnCompleteListener<DocumentSnapshot> onCompleteListener){
        FirebaseFirestore.getInstance().collection(institutionsCollection).document(institutionID).collection(COLLECTION).document(userId).get().addOnCompleteListener(onCompleteListener);
    }
    public void convertStudentOrTeacherToInstitutionUser(String institutionID, String teacherOrStudentID, OnCompleteListener<InstitutionUser> onCompleteListener){
        Task<DocumentSnapshot> documentSnapshotTask = FirebaseFirestore.getInstance().collection(institutionsCollection).document(institutionID).collection(COLLECTION).document(teacherOrStudentID).get();
        Tasks.whenAllComplete(documentSnapshotTask).addOnCompleteListener(task -> {
            InstitutionUser institutionUser;
            if(task.isSuccessful()){
                DocumentSnapshot snapshot = documentSnapshotTask.getResult();
                institutionUser = snapshot.toObject(InstitutionUser.class);
                onCompleteListener.onComplete(Tasks.forResult(institutionUser));
            }else{
                onCompleteListener.onComplete(Tasks.forException(task.getException()));
            }
        });

    }
}
