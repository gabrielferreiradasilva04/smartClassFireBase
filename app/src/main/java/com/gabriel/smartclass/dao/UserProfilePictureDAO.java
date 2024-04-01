package com.gabriel.smartclass.dao;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class UserProfilePictureDAO {
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    StorageReference storagePictures = storageReference.child("profilePictures");


    public void uploadProfileImage(String userEmail, Bitmap imageBitmap, OnSuccessListener<UploadTask.TaskSnapshot> onSuccessListener){
        StorageReference profileStoregeReference = storagePictures.child(userEmail);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] dataBytes = baos.toByteArray();
        UploadTask uploadTask = profileStoregeReference.putBytes(dataBytes);
        uploadTask.addOnSuccessListener(onSuccessListener);
    }
    public void downloadImage(String email, OnSuccessListener<byte[]> onSuccessListener, OnFailureListener onFailureListener){
        StorageReference profilePictureRef = storagePictures.child(email);
        profilePictureRef.getBytes(3072 * 3072).addOnSuccessListener(onSuccessListener);
    }
    public void excludeImageStorage(String email, OnSuccessListener onSuccessListener, OnFailureListener onFailureListener){
        StorageReference imageRef = storagePictures.child(email);
        imageRef.delete().addOnSuccessListener(onSuccessListener).addOnFailureListener(onFailureListener);
    }
}
