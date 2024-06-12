package com.gabriel.smartclass.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.gabriel.smartclass.model.baseEntitys.SimpleAuxEntity;
import com.google.firebase.firestore.DocumentReference;

import java.util.Objects;

public class Student extends SimpleAuxEntity implements Parcelable {
    private String id;
    private String description;
    private DocumentReference user_id;

    protected Student(Parcel in) {
        id = in.readString();
        description = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(description);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DocumentReference getUser_id() {
        return user_id;
    }

    public void setUser_id(DocumentReference user_id) {
        this.user_id = user_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;
        return Objects.equals(getId(), student.getId()) && Objects.equals(getDescription(), student.getDescription()) && Objects.equals(getUser_id(), student.getUser_id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDescription(), getUser_id());
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", username='" + description + '\'' +
                '}';
    }
}
