package com.gabriel.smartclass.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.gabriel.smartclass.model.baseEntitys.SimpleAuxEntity;
import com.google.firebase.firestore.DocumentReference;

import java.util.Objects;

public class Student extends CourseMember {

    public Student(String id, String description, DocumentReference institutionUserReference) {
        super(id, description, institutionUserReference);
    }

    public Student() {
    }
}
