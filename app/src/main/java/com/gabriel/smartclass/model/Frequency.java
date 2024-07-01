package com.gabriel.smartclass.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.gabriel.smartclass.model.baseEntitys.SimpleAuxEntity;

import java.util.Objects;

public class Frequency extends SimpleAuxEntity implements Parcelable {
    private String finalDescription;
    private double percent;
    private Subject subject;
    private Classroom classroom;

    public Frequency() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Frequency)) return false;
        Frequency frequency = (Frequency) o;
        return Double.compare(frequency.getPercent(), getPercent()) == 0 && Objects.equals(getFinalDescription(), frequency.getFinalDescription()) && Objects.equals(getSubject(), frequency.getSubject()) && Objects.equals(getClassroom(), frequency.getClassroom());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFinalDescription(), getPercent(), getSubject(), getClassroom());
    }

    public String getFinalDescription() {
        return finalDescription;
    }

    public void setFinalDescription(String finalDescription) {
        this.finalDescription = finalDescription;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    protected Frequency(Parcel in) {
        finalDescription = in.readString();
        percent = in.readDouble();
        subject = in.readParcelable(Subject.class.getClassLoader());
        classroom = in.readParcelable(Classroom.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(finalDescription);
        dest.writeDouble(percent);
        dest.writeParcelable(subject, flags);
        dest.writeParcelable(classroom, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Frequency> CREATOR = new Creator<Frequency>() {
        @Override
        public Frequency createFromParcel(Parcel in) {
            return new Frequency(in);
        }

        @Override
        public Frequency[] newArray(int size) {
            return new Frequency[size];
        }
    };
}
