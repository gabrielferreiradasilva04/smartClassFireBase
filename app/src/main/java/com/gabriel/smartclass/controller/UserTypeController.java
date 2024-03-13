package com.gabriel.smartclass.controller;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserTypeController {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void saveNewUserType(String description) {
        CollectionReference ref = db.collection("userTypes");



    }
}
