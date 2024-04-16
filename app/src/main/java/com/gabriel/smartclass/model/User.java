package com.gabriel.smartclass.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.PropertyName;

import java.util.ArrayList;
import java.util.Objects;


public class User implements Parcelable {
    @PropertyName("id")

    private String id;
    private String photoUrl;
    private String name;
    private String email;
    private String phone;
    private ArrayList<DocumentReference> institutions;

    public User() {
    }

    protected User(Parcel in) {
        id = in.readString();
        photoUrl = in.readString();
        name = in.readString();
        email = in.readString();
        phone = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(photoUrl);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(phone);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(photoUrl, user.photoUrl) && Objects.equals(name, user.name) && Objects.equals(email, user.email) && Objects.equals(phone, user.phone) && Objects.equals(institutions, user.institutions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, photoUrl, name, email, phone, institutions);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public ArrayList<DocumentReference> getInstitutions() {
        return institutions;
    }

    public void setInstitutions(ArrayList<DocumentReference> institutions) {
        this.institutions = institutions;
    }
}
