package com.gabriel.smartclass.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.gabriel.smartclass.model.baseEntitys.SimpleAuxEntity;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Classroom extends SimpleAuxEntity implements Parcelable {
    private String id;
    private String description;
    private int period;
    private List<DocumentReference> students_id;
    private List<DocumentReference> teachers_id;
    private List<DocumentReference> subjects_id;
    private TimeTable timeTable;


    protected Classroom(Parcel in) {
        id = in.readString();
        description = in.readString();
        period = in.readInt();
        timeTable = in.readParcelable(TimeTable.class.getClassLoader());

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(description);
        dest.writeInt(period);
        dest.writeParcelable(timeTable, flags);

        dest.writeInt(students_id.size()); // tamanho da lista de alunos
        for (DocumentReference students_id : students_id) { // lista de alunos/estudantes
            dest.writeString(students_id.getPath());
        }
        dest.writeInt(teachers_id.size()); // tamnho da lista de professores
        for (DocumentReference teachers_id : teachers_id) { //lista de professores
            dest.writeString(teachers_id.getPath());
        }
        dest.writeInt(subjects_id.size()); // tamanho da lista de materias
        for (DocumentReference subjects_id : subjects_id) { //lista de materias
            dest.writeString(subjects_id.getPath());
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Classroom> CREATOR = new Creator<Classroom>() {
        @Override
        public Classroom createFromParcel(Parcel in) {
            String id = in.readString();
            String description = in.readString();
            int period = in.readInt();
            TimeTable timeTable = in.readParcelable(TimeTable.class.getClassLoader());
            int studentsSize = in.readInt(); //encontrar lista de estudantes
            List<DocumentReference> students_id = new ArrayList<>();
            for (int i = 0; i < studentsSize; i++) {
                String studentPath = in.readString();
                if (studentPath != null) {
                    DocumentReference studentReference = FirebaseFirestore.getInstance().document(studentPath);
                    students_id.add(studentReference);
                }
            }

            int teacherSize = in.readInt();
            List<DocumentReference> teachers_id = new ArrayList<>();
            for (int j = 0; j < teacherSize; j++) {
                String teacherPath = in.readString();
                if (teacherPath != null) {
                    DocumentReference teacherReference = FirebaseFirestore.getInstance().document(teacherPath);
                    teachers_id.add(teacherReference);
                }
            }

            int subjectSize = in.readInt();
            List<DocumentReference> subjects_id = new ArrayList<>();
            for (int k = 0; k < subjectSize; k++) {
                String subjectPath = in.readString();
                if (subjectPath != null) {
                    DocumentReference subjectReference = FirebaseFirestore.getInstance().document(subjectPath);
                    subjects_id.add(subjectReference);
                }
            }
            return new Classroom(id, description, period, students_id, teachers_id, subjects_id, timeTable);
        }

        @Override
        public Classroom[] newArray(int size) {
            return new Classroom[size];
        }
    };

    public TimeTable getTimeTable() {
        return timeTable;
    }

    public void setTimeTable(TimeTable timeTable) {
        this.timeTable = timeTable;
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

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public List<DocumentReference> getStudents_id() {
        return students_id;
    }

    public void setStudents_id(List<DocumentReference> students_id) {
        this.students_id = students_id;
    }

    public List<DocumentReference> getTeachers_id() {
        return teachers_id;
    }

    public void setTeachers_id(List<DocumentReference> teachers_id) {
        this.teachers_id = teachers_id;
    }

    public List<DocumentReference> getSubjects_id() {
        return subjects_id;
    }

    public void setSubjects_id(List<DocumentReference> subjects_id) {
        this.subjects_id = subjects_id;
    }


    @Override
    public String toString() {
        return "Classroom{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", period=" + period +
                ", students_id=" + students_id +
                ", teachers_id=" + teachers_id +
                ", subjects_id=" + subjects_id +
                ", timeTable=" + timeTable +
                '}';
    }

    public Classroom(String id, String description, int period, List<DocumentReference> students_id, List<DocumentReference> teachers_id, List<DocumentReference> subjects_id, TimeTable timeTable) {
        this.id = id;
        this.description = description;
        this.period = period;
        this.students_id = students_id;
        this.teachers_id = teachers_id;
        this.subjects_id = subjects_id;
        this.timeTable = timeTable;
    }

    public Classroom() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Classroom)) return false;
        Classroom classroom = (Classroom) o;
        return getPeriod() == classroom.getPeriod() && Objects.equals(getId(), classroom.getId()) && Objects.equals(getDescription(), classroom.getDescription()) && Objects.equals(getStudents_id(), classroom.getStudents_id()) && Objects.equals(getTeachers_id(), classroom.getTeachers_id()) && Objects.equals(getSubjects_id(), classroom.getSubjects_id()) && Objects.equals(getTimeTable(), classroom.getTimeTable());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDescription(), getPeriod(), getStudents_id(), getTeachers_id(), getSubjects_id(), getTimeTable());
    }

}
