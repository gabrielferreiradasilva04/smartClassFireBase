package com.gabriel.smartclass.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.gabriel.smartclass.model.baseEntitys.SimpleAuxEntity;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Course extends SimpleAuxEntity implements Parcelable {
    private String id;
    private String name;
    private String description;
    private int duration;
    private int division_of_the_school_year;
    private DocumentReference area_id;
    private DocumentReference coordinator_id;
    private List<String> students_id = new ArrayList<>();
    private List<String> teachers_id = new ArrayList<>();


    protected Course(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        duration = in.readInt();
        division_of_the_school_year = in.readInt();
        students_id = in.createStringArrayList();
        teachers_id = in.createStringArrayList();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(duration);
        dest.writeInt(division_of_the_school_year);
        dest.writeStringList(students_id);
        dest.writeStringList(teachers_id);
        dest.writeString(area_id.getPath());
        dest.writeString(coordinator_id.getPath());

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            String id = in.readString();
            String name = in.readString();
            String description = in.readString();
            int duration = in.readInt();
            int division_of_the_school_year = in.readInt();
            List<String> students_id = in.createStringArrayList();
            List<String> teachers_id = in.createStringArrayList();
            String areaReferenceString = in.readString();
            String coordinatorReferenceString = in.readString();
            DocumentReference area_id = null;
            DocumentReference coordinator_id = null;
            if (areaReferenceString != null) {
                area_id = FirebaseFirestore.getInstance().document(areaReferenceString);
            }
            if (coordinatorReferenceString != null){
                coordinator_id = FirebaseFirestore.getInstance().document(coordinatorReferenceString);
            }
            return new Course(id, name, description, duration, division_of_the_school_year, coordinator_id, area_id, students_id, teachers_id);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    public List<String> getStudents_id() {
        return students_id;
    }

    public void setStudents_id(List<String> students_id) {
        this.students_id = students_id;
    }

    public List<String> getTeachers_id() {
        return teachers_id;
    }

    public void setTeachers_id(List<String> teachers_id) {
        this.teachers_id = teachers_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public DocumentReference getArea_id() {
        return area_id;
    }

    public void setArea_id(DocumentReference area_id) {
        this.area_id = area_id;
    }

    public DocumentReference getCoordinator_id() {
        return coordinator_id;
    }

    public void setCoordinator_id(DocumentReference coordinator_id) {
        this.coordinator_id = coordinator_id;
    }

    public int getDivision_of_the_school_year() {
        return division_of_the_school_year;
    }

    public void setDivision_of_the_school_year(int division_of_the_school_year) {
        this.division_of_the_school_year = division_of_the_school_year;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        Course course = (Course) o;
        return getDuration() == course.getDuration() && getDivision_of_the_school_year() == course.getDivision_of_the_school_year() && Objects.equals(getId(), course.getId()) && Objects.equals(getName(), course.getName()) && Objects.equals(getDescription(), course.getDescription()) && Objects.equals(getArea_id(), course.getArea_id()) && Objects.equals(getCoordinator_id(), course.getCoordinator_id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), getDuration(), getDivision_of_the_school_year(), getArea_id(), getCoordinator_id());
    }

    @NonNull
    @Override
    public String toString() {
        return "Course{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", duration=" + duration +
                ", division_of_the_school_year=" + division_of_the_school_year +
                ", area_id=" + area_id +
                ", coordinator_id=" + coordinator_id +
                '}';
    }

    public Course() {
    }

    public Course(String id, String name, String description, int duration, DocumentReference area_id, DocumentReference coordinantor_id, int division_of_the_school_year) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.area_id = area_id;
        this.coordinator_id = coordinantor_id;
        this.division_of_the_school_year = division_of_the_school_year;
    }

    public Course(String id, String name, String description, int duration, int division_of_the_school_year, DocumentReference area_id, DocumentReference coordinator_id, List<String> students_id, List<String> teachers_id) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.division_of_the_school_year = division_of_the_school_year;
        this.area_id = area_id;
        this.coordinator_id = coordinator_id;
        this.students_id = students_id;
        this.teachers_id = teachers_id;
    }
}
