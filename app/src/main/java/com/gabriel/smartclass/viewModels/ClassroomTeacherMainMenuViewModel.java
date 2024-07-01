package com.gabriel.smartclass.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gabriel.smartclass.dao.CourseDAO;
import com.gabriel.smartclass.model.Classroom;
import com.gabriel.smartclass.model.Course;
import com.gabriel.smartclass.model.CourseMember;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.InstitutionUser;
import com.gabriel.smartclass.model.Subject;

import java.util.ArrayList;
import java.util.List;


public class ClassroomTeacherMainMenuViewModel extends ViewModel {
    private Classroom classroom;
    private Course course;
    private InstitutionUser institutionUser;
    private Institution institution;
    private CourseMember student;
    private MutableLiveData<List<Subject>> teachersSubjects = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<List<CourseMember>> students = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<String> snackbarText = new MutableLiveData<>();

    public CourseMember getStudent() {
        return student;
    }

    public void setStudent(CourseMember student) {
        this.student = student;
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

    public InstitutionUser getInstitutionUser() {
        return institutionUser;
    }

    public void setInstitutionUser(InstitutionUser institutionUser) {
        this.institutionUser = institutionUser;
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public MutableLiveData<List<Subject>> getTeachersSubjects() {
        return teachersSubjects;
    }

    public void setTeachersSubjects(MutableLiveData<List<Subject>> teachersSubjects) {
        this.teachersSubjects = teachersSubjects;
    }

    public MutableLiveData<List<CourseMember>> getStudents() {
        return students;
    }

    public void setStudents(MutableLiveData<List<CourseMember>> students) {
        this.students = students;
    }

    public MutableLiveData<String> getSnackbarText() {
        return snackbarText;
    }

    public void setSnackbarText(MutableLiveData<String> snackbarText) {
        this.snackbarText = snackbarText;
    }

    public void getAllStudents(){
        new CourseDAO().getAllStudents(this.institution.getId(), this.course.getId(), task ->{
            if(task.isSuccessful()){
                this.students.setValue(task.getResult());
            }
        });

    }
    public void getTeacherSubjects(){
        new CourseDAO().getTeacherSubject(this.institution.getId(), this.course.getId(), this.institutionUser.getId(), this.classroom.getId(), task -> {
            if(task.isSuccessful()){
                teachersSubjects.setValue(task.getResult());
            }else{
                this.snackbarText.setValue("Ocorreu um erro ao listar suas matérias professor...");
            }
        });
    }
}
