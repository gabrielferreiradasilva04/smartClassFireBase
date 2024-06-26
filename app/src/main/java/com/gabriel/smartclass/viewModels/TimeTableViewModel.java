package com.gabriel.smartclass.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gabriel.smartclass.dao.ClassroomDAO;
import com.gabriel.smartclass.model.Classroom;
import com.gabriel.smartclass.model.Course;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.TimeTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TimeTableViewModel extends ViewModel {
    private Classroom classroom;
    private Course course;
    private Institution institution;
    private final MutableLiveData<String> snackbarText = new MutableLiveData<>();
    private final MutableLiveData<List<String>> mondaySubjectsLiveData = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<String>> tuesdaySubjectsLiveData = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<String>> wednesdaySubjectsLiveData = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<String>> thursdaySubjectsLiveData = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<String>> fridaySubjectsLiveData = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<String>> saturdaySubjectsLiveData = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<String>> sundaySubjectsLiveData = new MutableLiveData<>(new ArrayList<>());
    public MutableLiveData<String> getSnackbarText() {
        return snackbarText;
    }
    public MutableLiveData<List<String>> getMondaySubjectsLiveData() {
        return mondaySubjectsLiveData;
    }

    public MutableLiveData<List<String>> getTuesdaySubjectsLiveData() {
        return tuesdaySubjectsLiveData;
    }

    public MutableLiveData<List<String>> getWednesdaySubjectsLiveData() {
        return wednesdaySubjectsLiveData;
    }

    public MutableLiveData<List<String>> getThursdaySubjectsLiveData() {
        return thursdaySubjectsLiveData;
    }

    public MutableLiveData<List<String>> getFridaySubjectsLiveData() {
        return fridaySubjectsLiveData;
    }

    public MutableLiveData<List<String>> getSaturdaySubjectsLiveData() {
        return saturdaySubjectsLiveData;
    }

    public MutableLiveData<List<String>> getSundaySubjectsLiveData() {
        return sundaySubjectsLiveData;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public void getMondaySubjects(){
        this.mondaySubjectsLiveData.setValue(this.classroom.getTimeTable().getMondaySubjects());
    }
    public void getTuesdaySubjects(){
        this.tuesdaySubjectsLiveData.setValue(this.classroom.getTimeTable().getTuesdaySubjects());
    }
    public void getWednesdaySubjects(){
        this.wednesdaySubjectsLiveData.setValue(this.classroom.getTimeTable().getWednesdaySubjects());
    }
    public void getThursdaySubjects(){
        this.thursdaySubjectsLiveData.setValue(this.classroom.getTimeTable().getThursdaySubjects());
    }
    public void getFridaySubjects(){
        this.fridaySubjectsLiveData.setValue(this.classroom.getTimeTable().getFridaySubjects());
    }
    public void getSaturdaySubjects(){
        this.saturdaySubjectsLiveData.setValue(this.classroom.getTimeTable().getSaturdaySubjects());
    }
    public void getSundaySubjects(){
        this.sundaySubjectsLiveData.setValue(this.classroom.getTimeTable().getSundaySubjects());
    }
    public void addNewSubject(String subject, int day){
        TimeTable timeTable = this.classroom.getTimeTable();
        switch (day){
            case 1:
                timeTable.getMondaySubjects().add(subject);
                break;
            case 2:
                timeTable.getTuesdaySubjects().add(subject);
                break;
            case 3:
                timeTable.getWednesdaySubjects().add(subject);
                break;
            case 4:
                timeTable.getThursdaySubjects().add(subject);
                break;
            case 5:
                timeTable.getFridaySubjects().add(subject);
                break;
            case 6:
                timeTable.getSaturdaySubjects().add(subject);
                break;
            case 7:
                timeTable.getSundaySubjects().add(subject);
                break;
            default: snackbarText.setValue("Selecione um dia válido...");
            break;
        }
        HashMap<String, Object> classroomUpdates = new HashMap<>();
        classroomUpdates.put("timeTable", timeTable);
        new ClassroomDAO().updateClassroom(this.getInstitution().getId(), this.course.getId(), this.classroom.getId(), classroomUpdates);
    }
    public void removeSubject(int subjectIndex, int day){
        TimeTable timeTable = this.getClassroom().getTimeTable();
        switch (day){
            case 1:
                timeTable.getMondaySubjects().remove(subjectIndex);
                break;
            case 2:
                timeTable.getTuesdaySubjects().remove(subjectIndex);
                break;
            case 3:
                timeTable.getWednesdaySubjects().remove(subjectIndex);
                break;
            case 4:
                timeTable.getThursdaySubjects().remove(subjectIndex);
                break;
            case 5:
                timeTable.getFridaySubjects().remove(subjectIndex);
                break;
            case 6:
                timeTable.getSaturdaySubjects().remove(subjectIndex);
                break;
            case 7:
                timeTable.getSundaySubjects().remove(subjectIndex);
                break;
            default: snackbarText.setValue("Selecione um dia válido...");
                break;
        }
        HashMap<String, Object> classroomUpdates = new HashMap<>();
        classroomUpdates.put("timeTable", timeTable);
        new ClassroomDAO().updateClassroom(this.getInstitution().getId(), this.course.getId(), this.classroom.getId(), classroomUpdates);
    }
}
