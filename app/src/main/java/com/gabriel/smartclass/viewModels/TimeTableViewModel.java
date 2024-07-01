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
    private boolean showEdition = true;
    private  MutableLiveData<String> snackbarText = new MutableLiveData<>();
    private  MutableLiveData<List<String>> mondaySubjectsLiveData = new MutableLiveData<>(new ArrayList<>());
    private  MutableLiveData<List<String>> tuesdaySubjectsLiveData = new MutableLiveData<>(new ArrayList<>());
    private  MutableLiveData<List<String>> wednesdaySubjectsLiveData = new MutableLiveData<>(new ArrayList<>());
    private  MutableLiveData<List<String>> thursdaySubjectsLiveData = new MutableLiveData<>(new ArrayList<>());
    private  MutableLiveData<List<String>> fridaySubjectsLiveData = new MutableLiveData<>(new ArrayList<>());
    private  MutableLiveData<List<String>> saturdaySubjectsLiveData = new MutableLiveData<>(new ArrayList<>());
    private  MutableLiveData<List<String>> sundaySubjectsLiveData = new MutableLiveData<>(new ArrayList<>());

    public boolean isShowEdition() {
        return showEdition;
    }

    public void setShowEdition(boolean showEdition) {
        this.showEdition = showEdition;
    }

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

    public void getMondaySubjects() {
        this.mondaySubjectsLiveData.setValue(this.classroom.getTimeTable().getMondaySubjects());
    }

    public void getTuesdaySubjects() {
        this.tuesdaySubjectsLiveData.setValue(this.classroom.getTimeTable().getTuesdaySubjects());
    }

    public void getWednesdaySubjects() {
        this.wednesdaySubjectsLiveData.setValue(this.classroom.getTimeTable().getWednesdaySubjects());
    }

    public void getThursdaySubjects() {
        this.thursdaySubjectsLiveData.setValue(this.classroom.getTimeTable().getThursdaySubjects());
    }

    public void getFridaySubjects() {
        this.fridaySubjectsLiveData.setValue(this.classroom.getTimeTable().getFridaySubjects());
    }

    public void getSaturdaySubjects() {
        this.saturdaySubjectsLiveData.setValue(this.classroom.getTimeTable().getSaturdaySubjects());
    }

    public void getSundaySubjects() {
        this.sundaySubjectsLiveData.setValue(this.classroom.getTimeTable().getSundaySubjects());
    }

    public void loadAllTimeTables(){
        this.getMondaySubjects();
        this.getTuesdaySubjects();
        this.getWednesdaySubjects();
        this.getThursdaySubjects();
        this.getFridaySubjects();
        this.getSundaySubjects();
        this.getSaturdaySubjects();
    }
    public List<String> updateLists(List<String> actualList, List<String> newList, String subject, int index, boolean add){
        if(add){
            newList = actualList;
            newList.add(subject);
            return newList;
        }else{
            newList = actualList;
            newList.remove(index);
            return newList;
        }
    }
    public void addNewSubject(String subject, int day) {
        if (subject != null && !subject.equals("")) {
            TimeTable timeTable = this.classroom.getTimeTable();
            List<String> newList = new ArrayList<>();
            switch (day) {
                case 1:
                    timeTable.setMondaySubjects(updateLists(timeTable.getMondaySubjects(), newList, subject, 0, true));
                    this.mondaySubjectsLiveData.setValue(timeTable.getMondaySubjects());
                    break;
                case 2:
                    timeTable.setTuesdaySubjects(updateLists(timeTable.getTuesdaySubjects(), newList, subject, 0, true));
                    this.tuesdaySubjectsLiveData.setValue(timeTable.getTuesdaySubjects());
                    break;
                case 3:
                    timeTable.setWednesdaySubjects(updateLists(timeTable.getWednesdaySubjects(), newList, subject, 0, true));
                    this.wednesdaySubjectsLiveData.setValue(timeTable.getWednesdaySubjects());
                    break;
                case 4:
                    timeTable.setThursdaySubjects(updateLists(timeTable.getThursdaySubjects(), newList, subject, 0, true));
                    this.thursdaySubjectsLiveData.setValue(timeTable.getThursdaySubjects());
                    break;
                case 5:
                    timeTable.setFridaySubjects(updateLists(timeTable.getFridaySubjects(), newList, subject, 0, true));
                    this.fridaySubjectsLiveData.setValue(timeTable.getFridaySubjects());
                    break;
                case 6:
                    timeTable.setSaturdaySubjects(updateLists(timeTable.getSaturdaySubjects(), newList, subject, 0, true));
                    this.saturdaySubjectsLiveData.setValue(timeTable.getSaturdaySubjects());
                    break;
                case 7:
                    timeTable.setSundaySubjects(updateLists(timeTable.getSundaySubjects(), newList, subject, 0, true));
                    this.sundaySubjectsLiveData.setValue(timeTable.getSundaySubjects());
                    break;
                default:
                    snackbarText.setValue("Selecione um dia válido...");
                    break;
            }
            HashMap<String, Object> classroomUpdates = new HashMap<>();
            classroomUpdates.put("timeTable", timeTable);
            new ClassroomDAO().updateClassroom(this.getInstitution().getId(), this.course.getId(), this.classroom.getId(), classroomUpdates);
        }else{
            this.snackbarText.setValue("Preencha o campo 'matéria'...");
        }

    }

    public void removeSubject(int subjectIndex, int day) {
        TimeTable timeTable = this.getClassroom().getTimeTable();
        List<String> newList = new ArrayList<>();
        switch (day) {
            case 1:
                timeTable.setMondaySubjects(updateLists(timeTable.getMondaySubjects(), newList, null, subjectIndex, false));
                this.mondaySubjectsLiveData.setValue(timeTable.getMondaySubjects());
                break;
            case 2:
                timeTable.setTuesdaySubjects(updateLists(timeTable.getTuesdaySubjects(), newList, null, subjectIndex, false));
                this.tuesdaySubjectsLiveData.setValue(timeTable.getTuesdaySubjects());;
                break;
            case 3:
                timeTable.setWednesdaySubjects(updateLists(timeTable.getWednesdaySubjects(), newList, null, subjectIndex, false));
                this.wednesdaySubjectsLiveData.setValue(timeTable.getWednesdaySubjects());
                break;
            case 4:
                timeTable.setThursdaySubjects(updateLists(timeTable.getThursdaySubjects(), newList, null, subjectIndex, false));
                this.thursdaySubjectsLiveData.setValue(timeTable.getThursdaySubjects());
                break;
            case 5:
                timeTable.setFridaySubjects(updateLists(timeTable.getFridaySubjects(), newList, null, subjectIndex, false));
                this.fridaySubjectsLiveData.setValue(timeTable.getFridaySubjects());
                break;
            case 6:
                timeTable.setSaturdaySubjects(updateLists(timeTable.getSaturdaySubjects(), newList, null, subjectIndex, false));
                this.saturdaySubjectsLiveData.setValue(timeTable.getSaturdaySubjects());
                break;
            case 7:
                timeTable.setSundaySubjects(updateLists(timeTable.getSundaySubjects(), newList, null, subjectIndex, false));
                this.sundaySubjectsLiveData.setValue(timeTable.getSundaySubjects());
                break;
            default:
                snackbarText.setValue("Selecione um dia válido...");
                break;
        }
        HashMap<String, Object> classroomUpdates = new HashMap<>();
        classroomUpdates.put("timeTable", timeTable);
        new ClassroomDAO().updateClassroom(this.getInstitution().getId(), this.course.getId(), this.classroom.getId(), classroomUpdates);
    }
    public void liveSyncClassroom(){
        new ClassroomDAO().liveSyncClassroom(this.institution.getId(), this.course.getId(), this.classroom.getId(), (value, error) -> {
            if(error == null && value != null){
                this.classroom = value.toObject(Classroom.class);
                this.loadAllTimeTables();
            }
        });
    }
}
