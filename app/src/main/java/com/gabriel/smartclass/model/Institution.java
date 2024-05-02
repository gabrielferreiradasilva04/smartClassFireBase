package com.gabriel.smartclass.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class Institution extends User implements Parcelable {
    private String id;
    private String cnpj;
    private int maxTeachers;
    private int maxStudents;
    private int maxCoordinators;
    private int maxClassrooms;
    //getters and setters

    public int getMaxTeachers() {
        return maxTeachers;
    }

    public void setMaxTeachers(int maxTeachers) {
        this.maxTeachers = maxTeachers;
    }

    public int getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(int maxStudents) {
        this.maxStudents = maxStudents;
    }

    public int getMaxCoordinators() {
        return maxCoordinators;
    }

    public void setMaxCoordinators(int maxCoordinators) {
        this.maxCoordinators = maxCoordinators;
    }

    public int getMaxClassrooms() {
        return maxClassrooms;
    }

    public void setMaxClassrooms(int maxClassrooms) {
        this.maxClassrooms = maxClassrooms;
    }

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
//parcelable

    protected Institution(Parcel in) {
        id = in.readString();
        cnpj = in.readString();
        maxTeachers = in.readInt();
        maxStudents = in.readInt();
        maxCoordinators = in.readInt();
        maxClassrooms = in.readInt();
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
        dest.writeInt(maxTeachers);
        dest.writeInt(maxStudents);
        dest.writeInt(maxCoordinators);
        dest.writeInt(maxClassrooms);
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

}
