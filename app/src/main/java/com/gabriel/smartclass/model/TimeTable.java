package com.gabriel.smartclass.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.DocumentReference;

import java.util.List;
import java.util.Objects;

public class TimeTable implements Parcelable {
    private List<String> sundaySubjects;
    private List<String> mondaySubjects;
    private List<String> tuesdaySubjects;
    private List<String> wednesdaySubjects;
    private List<String> thurdaySubjects;
    private List<String> FridaySubjects;
    private List<String> SaturdaySubjects;

    protected TimeTable(Parcel in) {
        sundaySubjects = in.createStringArrayList();
        mondaySubjects = in.createStringArrayList();
        tuesdaySubjects = in.createStringArrayList();
        wednesdaySubjects = in.createStringArrayList();
        thurdaySubjects = in.createStringArrayList();
        FridaySubjects = in.createStringArrayList();
        SaturdaySubjects = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(sundaySubjects);
        dest.writeStringList(mondaySubjects);
        dest.writeStringList(tuesdaySubjects);
        dest.writeStringList(wednesdaySubjects);
        dest.writeStringList(thurdaySubjects);
        dest.writeStringList(FridaySubjects);
        dest.writeStringList(SaturdaySubjects);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TimeTable> CREATOR = new Creator<TimeTable>() {
        @Override
        public TimeTable createFromParcel(Parcel in) {
            return new TimeTable(in);
        }

        @Override
        public TimeTable[] newArray(int size) {
            return new TimeTable[size];
        }
    };

    public List<String> getSundaySubjects() {
        return sundaySubjects;
    }

    public void setSundaySubjects(List<String> sundaySubjects) {
        this.sundaySubjects = sundaySubjects;
    }

    public List<String> getMondaySubjects() {
        return mondaySubjects;
    }

    public void setMondaySubjects(List<String> mondaySubjects) {
        this.mondaySubjects = mondaySubjects;
    }

    public List<String> getTuesdaySubjects() {
        return tuesdaySubjects;
    }

    public void setTuesdaySubjects(List<String> tuesdaySubjects) {
        this.tuesdaySubjects = tuesdaySubjects;
    }

    public List<String> getWednesdaySubjects() {
        return wednesdaySubjects;
    }

    public void setWednesdaySubjects(List<String> wednesdaySubjects) {
        this.wednesdaySubjects = wednesdaySubjects;
    }

    public List<String> getThurdaySubjects() {
        return thurdaySubjects;
    }

    public void setThurdaySubjects(List<String> thurdaySubjects) {
        this.thurdaySubjects = thurdaySubjects;
    }

    public List<String> getFridaySubjects() {
        return FridaySubjects;
    }

    public void setFridaySubjects(List<String> fridaySubjects) {
        FridaySubjects = fridaySubjects;
    }

    public List<String> getSaturdaySubjects() {
        return SaturdaySubjects;
    }

    public void setSaturdaySubjects(List<String> saturdaySubjects) {
        SaturdaySubjects = saturdaySubjects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeTable)) return false;
        TimeTable timeTable = (TimeTable) o;
        return Objects.equals(getSundaySubjects(), timeTable.getSundaySubjects()) && Objects.equals(getMondaySubjects(), timeTable.getMondaySubjects()) && Objects.equals(getTuesdaySubjects(), timeTable.getTuesdaySubjects()) && Objects.equals(getWednesdaySubjects(), timeTable.getWednesdaySubjects()) && Objects.equals(getThurdaySubjects(), timeTable.getThurdaySubjects()) && Objects.equals(getFridaySubjects(), timeTable.getFridaySubjects()) && Objects.equals(getSaturdaySubjects(), timeTable.getSaturdaySubjects());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSundaySubjects(), getMondaySubjects(), getTuesdaySubjects(), getWednesdaySubjects(), getThurdaySubjects(), getFridaySubjects(), getSaturdaySubjects());
    }

    public TimeTable() {
    }

    public TimeTable(List<String> sundaySubjects, List<String> mondaySubjects, List<String> tuesdaySubjects, List<String> wednesdaySubjects, List<String> thurdaySubjects, List<String> fridaySubjects, List<String> saturdaySubjects) {
        this.sundaySubjects = sundaySubjects;
        this.mondaySubjects = mondaySubjects;
        this.tuesdaySubjects = tuesdaySubjects;
        this.wednesdaySubjects = wednesdaySubjects;
        this.thurdaySubjects = thurdaySubjects;
        FridaySubjects = fridaySubjects;
        SaturdaySubjects = saturdaySubjects;
    }
}
