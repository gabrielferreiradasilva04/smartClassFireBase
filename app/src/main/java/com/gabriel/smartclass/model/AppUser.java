package com.gabriel.smartclass.model;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.PropertyName;

import java.util.ArrayList;
import java.util.Objects;


public class AppUser {
    @PropertyName("id")

    private String id;
    @PropertyName("userAuth_Id")

    private String userAuth_Id;
    private ArrayList<DocumentReference> institutions;

    public AppUser() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppUser appUser = (AppUser) o;
        return Objects.equals(id, appUser.id) && Objects.equals(userAuth_Id, appUser.userAuth_Id) && Objects.equals(institutions, appUser.institutions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userAuth_Id, institutions);
    }

    public AppUser(String id, String userAuth_Id, ArrayList<DocumentReference> institutions) {
        this.id = id;
        this.userAuth_Id = userAuth_Id;
        this.institutions = institutions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserAuth_Id() {
        return userAuth_Id;
    }

    public void setUserAuth_Id(String userAuth_Id) {
        this.userAuth_Id = userAuth_Id;
    }

    public ArrayList<DocumentReference> getInstitutions() {
        return institutions;
    }

    public void setInstitutions(ArrayList<DocumentReference> institutions) {
        this.institutions = institutions;
    }
}
