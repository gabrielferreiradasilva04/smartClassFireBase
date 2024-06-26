package com.gabriel.smartclass.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.gabriel.smartclass.model.baseEntitys.SimpleAuxEntity;

import java.util.Objects;

public class Subject extends SimpleAuxEntity implements Parcelable {
    private String id;
    private String description;
    private double minimum_grade;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subject)) return false;
        Subject subject = (Subject) o;
        return Objects.equals(getId(), subject.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", minimum_grade=" + minimum_grade +
                '}';
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getMinimum_grade() {
        return minimum_grade;
    }

    public void setMinimum_grade(double minimum_grade) {
        this.minimum_grade = minimum_grade;
    }

    public Subject(String id, String description, double minimum_grade) {
        this.id = id;
        this.description = description;
        this.minimum_grade = minimum_grade;
    }

    public Subject() {
    }

    protected Subject(Parcel in) {
        id = in.readString();
        description = in.readString();
        minimum_grade = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(description);
        dest.writeDouble(minimum_grade);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Subject> CREATOR = new Creator<Subject>() {
        @Override
        public Subject createFromParcel(Parcel in) {
            return new Subject(in);
        }

        @Override
        public Subject[] newArray(int size) {
            return new Subject[size];
        }
    };
}
