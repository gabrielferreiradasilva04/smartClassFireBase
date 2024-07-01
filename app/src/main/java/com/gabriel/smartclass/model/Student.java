package com.gabriel.smartclass.model;

import android.os.Parcelable;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;

public class Student extends CourseMember implements Parcelable {
    private List<StudentGrade> studentGrades = new ArrayList<>();
    private List<Frequency> frequencies = new ArrayList<>();

    public List<StudentGrade> getStudentGrades() {
        return studentGrades;
    }

    public List<Frequency> getFrequencies() {
        return frequencies;
    }

    public void setFrequencies(List<Frequency> frequencies) {
        this.frequencies = frequencies;
    }

    public void setStudentGrades(List<StudentGrade> studentGrades) {
        this.studentGrades = studentGrades;
    }

    public Student(String id, String description, DocumentReference institutionUserReference) {
        super(id, description, institutionUserReference);
    }

    public Student() {
    }
}
