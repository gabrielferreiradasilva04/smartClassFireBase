package com.gabriel.smartclass.model;

import android.os.Parcelable;

public class ClassroomSubject extends Subject implements Parcelable {
    private Teacher teacher;
    private String subjectTeacher; //nome da matéria + nome do professor

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public String getSubjectTeacher() {
        return subjectTeacher;
    }

    public void setSubjectTeacher(String subjectTeacher) {
        this.subjectTeacher = subjectTeacher;
    }
}
