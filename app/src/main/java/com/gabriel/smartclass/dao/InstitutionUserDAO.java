package com.gabriel.smartclass.dao;


import android.util.Log;

import com.gabriel.smartclass.model.Course;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.InstitutionUser;
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

    public void getInstitutionUserCourses(String institutionID, String institutionUserID, boolean iscoordinator, OnCompleteListener<QuerySnapshot> onCompleteListener, OnFailureListener onFailureListener) {
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        DocumentReference institutionUserReference = fb.collection(institutionsCollection).document(institutionID).collection(COLLECTION).document(institutionUserID);
        if (!iscoordinator) {
            fb.collection(institutionsCollection).document(institutionID).collection(courseCollection)
                    .whereArrayContains("students_id", institutionUserReference)
                    .get().addOnCompleteListener(onCompleteListener)
                    .addOnFailureListener(onFailureListener);
        } else{
            fb.collection(institutionsCollection).document(institutionID).collection(courseCollection)
                    .whereEqualTo("coordinator_id", institutionUserReference)
                    .get().addOnCompleteListener(onCompleteListener)
                    .addOnFailureListener(onFailureListener);
        }
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
