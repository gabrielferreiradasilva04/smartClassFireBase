package com.gabriel.smartclass.dao;

import android.graphics.Bitmap;
import android.net.Uri;

import com.gabriel.smartclass.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Objects;


public class UserDAO {
    private FirebaseUser currentUserAplication = FirebaseAuth.getInstance().getCurrentUser();
    private final String COLLECTION = User.class.getSimpleName();
    private FirebaseFirestore fb = FirebaseFirestore.getInstance();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private StorageReference storagePictures = storageReference.child("profilePictures");



    public void insert(User user){
        fb.collection(this.COLLECTION).add(user);
    }
    public void getUserInstitutions(OnCompleteListener<DocumentSnapshot> onCompleteListener, OnFailureListener onFailureListener){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        fb.collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }

    public void updateProfile(String displayName, OnSuccessListener onSuccessListener){
        UserProfileChangeRequest changes = new UserProfileChangeRequest.Builder().setDisplayName(displayName).build();
        currentUserAplication.updateProfile(changes).addOnSuccessListener(onSuccessListener);
    }
    public void updatePassword(String newPassword, OnCompleteListener<Void> onCompleteListener, OnFailureListener onFailureListener){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.updatePassword(newPassword).addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
        }
    }
    public void excludeProfilePicture(OnCompleteListener onCompleteListener, OnFailureListener onFailureListener){
        UserProfileChangeRequest update = new UserProfileChangeRequest.Builder().setPhotoUri(null).build();
        currentUserAplication.updateProfile(update).addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }
    public void uploadProfileImage(String userEmail, Bitmap imageBitmap, OnSuccessListener<UploadTask.TaskSnapshot> onSuccessListener, OnFailureListener onFailureListener){
        StorageReference profileStoregeReference = storagePictures.child(userEmail);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] dataBytes = baos.toByteArray();
        UploadTask uploadTask = profileStoregeReference.putBytes(dataBytes);
        uploadTask.addOnSuccessListener(onSuccessListener).addOnFailureListener(onFailureListener);
    }
    public void downloadImage(String email, OnSuccessListener<byte[]> onSuccessListener, OnFailureListener onFailureListener){
        StorageReference profilePictureRef = storagePictures.child(email);
        profilePictureRef.getBytes(3072 * 3072).addOnSuccessListener(onSuccessListener);
    }
    public void excludeImageStorage(String email, OnSuccessListener onSuccessListener, OnFailureListener onFailureListener){
        StorageReference imageRef = storagePictures.child(email);
        imageRef.delete().addOnSuccessListener(onSuccessListener).addOnFailureListener(onFailureListener);
    }
    public void listenerSnapshotChanges(EventListener<DocumentSnapshot> enventListener){
        fb.collection(COLLECTION).document(currentUserAplication.getUid()).addSnapshotListener(enventListener);
    }
    public void getUserByUserAuthId(OnCompleteListener<DocumentSnapshot> onCompleteListener){
        fb.collection(COLLECTION).document(currentUserAplication.getUid()).get().addOnCompleteListener(onCompleteListener);
    }
    public void getUserPictureByCloudStorage(String email,OnCompleteListener<Uri> onCompleteListener, OnFailureListener onFailureListener){
        storagePictures.child(email).getDownloadUrl().addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }
    public void getUserById(String id, OnSuccessListener<DocumentSnapshot> onSuccessListener, OnFailureListener onFailureListener){
        fb.collection(COLLECTION).document(id).get().addOnSuccessListener(onSuccessListener).addOnFailureListener(onFailureListener);
    }
    public void updateApplicationUser(HashMap<String, Object> updates, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener){
        fb.collection(COLLECTION).document(currentUserAplication.getUid()).update(updates).addOnSuccessListener(onSuccessListener).addOnFailureListener(onFailureListener);
    }
    public void getUserByDocumentReference(DocumentReference userReference, OnCompleteListener<DocumentSnapshot> onCompleteListener, OnFailureListener onFailureListener){
        fb.collection(COLLECTION).document(userReference.getId()).get().addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }


}
