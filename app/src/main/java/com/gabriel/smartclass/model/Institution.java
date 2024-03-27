package com.gabriel.smartclass.model;

import android.os.Parcel;
import android.os.Parcelable;


import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.PropertyName;

import java.util.Objects;

public class Institution implements Parcelable {
    private String id;
    @PropertyName("cnpj")

    private String cnpj;
    @PropertyName("name")

    private String name;
    @PropertyName("reponsable_id")

    private DocumentReference responsable_id;

    //getters and setters




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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DocumentReference getResponsable_id() {
        return responsable_id;
    }

    public void setResponsable_id(DocumentReference responsable_id) {
        this.responsable_id = responsable_id;
    }



    public Institution(String cnpj, String name, DocumentReference responsable_id){
        this.cnpj = cnpj;
        this.name = name;
        this.responsable_id = responsable_id;
    }

    public Institution(String id, String cnpj, String name, DocumentReference responsable_id) {
        this.id = id;
        this.cnpj = cnpj;
        this.name = name;
        this.responsable_id = responsable_id;
    }

    public Institution() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Institution institution = (Institution) o;
        return Objects.equals(id, institution.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Institution{" +
                "id='" + id + '\'' +
                ", cnpj='" + cnpj + '\'' +
                ", name='" + name + '\'' +
                ", responsable_id=" + responsable_id +
                '}';
    }
    protected Institution(Parcel in) {
        id = in.readString();
        cnpj = in.readString();
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(cnpj);
        dest.writeString(name);
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





}
