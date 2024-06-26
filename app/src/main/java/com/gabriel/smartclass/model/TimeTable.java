package com.gabriel.smartclass.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TimeTable implements Parcelable {
    private List<String> sundaySubjects = new ArrayList<>();
    private List<String> mondaySubjects = new ArrayList<>();
    private List<String> tuesdaySubjects = new ArrayList<>();
    private List<String> wednesdaySubjects = new ArrayList<>();
    private List<String> thursdaySubjects = new ArrayList<>();
    private List<String> FridaySubjects = new ArrayList<>();
    private List<String> SaturdaySubjects = new ArrayList<>();

    protected TimeTable(Parcel in) {
        sundaySubjects = in.createStringArrayList();
        mondaySubjects = in.createStringArrayList();
        tuesdaySubjects = in.createStringArrayList();
        wednesdaySubjects = in.createStringArrayList();
        thursdaySubjects = in.createStringArrayList();
        FridaySubjects = in.createStringArrayList();
        SaturdaySubjects = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(sundaySubjects);
        dest.writeStringList(mondaySubjects);
        dest.writeStringList(tuesdaySubjects);
        dest.writeStringList(wednesdaySubjects);
        dest.writeStringList(thursdaySubjects);
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

    public List<String> getThursdaySubjects() {
        return thursdaySubjects;
    }

    public void setThursdaySubjects(List<String> thursdaySubjects) {
        this.thursdaySubjects = thursdaySubjects;
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
        return Objects.equals(getSundaySubjects(), timeTable.getSundaySubjects()) && Objects.equals(getMondaySubjects(), timeTable.getMondaySubjects()) && Objects.equals(getTuesdaySubjects(), timeTable.getTuesdaySubjects()) && Objects.equals(getWednesdaySubjects(), timeTable.getWednesdaySubjects()) && Objects.equals(getThursdaySubjects(), timeTable.getThursdaySubjects()) && Objects.equals(getFridaySubjects(), timeTable.getFridaySubjects()) && Objects.equals(getSaturdaySubjects(), timeTable.getSaturdaySubjects());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSundaySubjects(), getMondaySubjects(), getTuesdaySubjects(), getWednesdaySubjects(), getThursdaySubjects(), getFridaySubjects(), getSaturdaySubjects());
    }

    public TimeTable() {
    }

    public TimeTable(List<String> sundaySubjects, List<String> mondaySubjects, List<String> tuesdaySubjects, List<String> wednesdaySubjects, List<String> thurdaySubjects, List<String> fridaySubjects, List<String> saturdaySubjects) {
        this.sundaySubjects = sundaySubjects;
        this.mondaySubjects = mondaySubjects;
        this.tuesdaySubjects = tuesdaySubjects;
        this.wednesdaySubjects = wednesdaySubjects;
        this.thursdaySubjects = thurdaySubjects;
        FridaySubjects = fridaySubjects;
        SaturdaySubjects = saturdaySubjects;
    }
}
