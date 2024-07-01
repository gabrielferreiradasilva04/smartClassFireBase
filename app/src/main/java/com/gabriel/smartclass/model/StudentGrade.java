package com.gabriel.smartclass.model;

import android.os.Parcel;
import android.os.Parcelable;

public class StudentGrade implements Parcelable {
    private Subject subject;
    private Classroom classroom;
    private double firstGrade;
    private double lastGrade;
    private double finalGrade; // = first + last / 2
    private String finalResult;

    protected StudentGrade(Parcel in) {
        subject = in.readParcelable(Subject.class.getClassLoader());
        classroom = in.readParcelable(Classroom.class.getClassLoader());
        firstGrade = in.readDouble();
        lastGrade = in.readDouble();
        finalGrade = in.readDouble();
        finalResult = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(subject, flags);
        dest.writeParcelable(classroom, flags);
        dest.writeDouble(firstGrade);
        dest.writeDouble(lastGrade);
        dest.writeDouble(finalGrade);
        dest.writeString(finalResult);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<StudentGrade> CREATOR = new Creator<StudentGrade>() {
        @Override
        public StudentGrade createFromParcel(Parcel in) {
            return new StudentGrade(in);
        }

        @Override
        public StudentGrade[] newArray(int size) {
            return new StudentGrade[size];
        }
    };

    public String getFinalResult() {
        return finalResult;
    }

    public void setFinalResult(String finalResult) {
        this.finalResult = finalResult;
    }

    public StudentGrade(Subject subject, Classroom classroom, double firstGrade, double lastGrade, double finalGrade) {
        this.subject = subject;
        this.classroom = classroom;
        this.firstGrade = firstGrade;
        this.lastGrade = lastGrade;
        this.finalGrade = finalGrade;
    }

    public StudentGrade() {
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

    public double getFirstGrade() {
        return firstGrade;
    }

    public void setFirstGrade(double firstGrade) {
        this.firstGrade = firstGrade;
    }

    public double getLastGrade() {
        return lastGrade;
    }

    public void setLastGrade(double lastGrade) {
        this.lastGrade = lastGrade;
    }

    public double getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(double finalGrade) {
        this.finalGrade = finalGrade;
    }

}
