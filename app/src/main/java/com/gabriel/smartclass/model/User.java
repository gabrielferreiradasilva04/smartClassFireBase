package com.gabriel.smartclass.model;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.PropertyName;

import java.util.ArrayList;
import java.util.Objects;


public class User {
    @PropertyName("id")

    private String id;
    private ArrayList<DocumentReference> institutions;

    public User() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(institutions, user.institutions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, institutions);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public ArrayList<DocumentReference> getInstitutions() {
        return institutions;
    }

    public void setInstitutions(ArrayList<DocumentReference> institutions) {
        this.institutions = institutions;
    }
}
