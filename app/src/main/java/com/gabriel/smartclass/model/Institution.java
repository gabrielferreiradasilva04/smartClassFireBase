package com.gabriel.smartclass.model;

import android.os.Parcel;
import android.os.Parcelable;


import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.PropertyName;

import java.util.Objects;

public class Institution extends User implements Parcelable {
    private String id;
    private String cnpj;
    //getters and setters

    protected Institution(Parcel in) {
        id = in.readString();
        cnpj = in.readString();
        String name = in.readString();
        String email = in.readString();
        String photoUrl = in.readString();
        String phone = in.readString();
        setName(name);
        setEmail(email);
        setPhotoUrl(photoUrl);
        setPhone(phone);
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(cnpj);
        dest.writeString(getName());
        dest.writeString(getEmail());
        dest.writeString(getPhotoUrl());
        dest.writeString(getPhone());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Institution> CREATOR = new Creator<Institution>() {
        @Override
        public Institution createFromParcel(Parcel in) {

            return new Institution(in);
        }

        @Override
        public Institution[] newArray(int size) {
            return new Institution[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }


    public Institution(String cnpj){
        this.cnpj = cnpj;
    }

    public Institution(String id, String cnpj) {
        this.id = id;
        this.cnpj = cnpj;
    }

    public Institution() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Institution that = (Institution) o;
        return Objects.equals(id, that.id) && Objects.equals(cnpj, that.cnpj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, cnpj);
    }

    @Override
    public String toString() {
        return "Institution{" +
                "id='" + id + '\'' +
                ", cnpj='" + cnpj + '\'' +
                '}';
    }


}
