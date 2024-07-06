package com.gabriel.smartclass.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gabriel.smartclass.dao.CourseDAO;
import com.gabriel.smartclass.model.Classroom;
import com.gabriel.smartclass.model.Course;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.InstitutionUser;
import com.gabriel.smartclass.model.Student;

public class ClassroomStudentMainMenuViewModel extends ViewModel{
    private Classroom classroom;
    private Course course;
    private InstitutionUser institutionUser;
    private Institution institution;
    private final MutableLiveData<Student> mldStudent = new MutableLiveData<>();
    private MutableLiveData<String> snackbarText = new MutableLiveData<>();

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

    public MutableLiveData<String> getSnackbarText() {
        return snackbarText;
    }

    public void setSnackbarText(MutableLiveData<String> snackbarText) {
        this.snackbarText = snackbarText;
    }

    public void getStudent(){
        new CourseDAO().getStudentByID(this.institution.getId(), this.course.getId(), this.institutionUser.getId(), task -> {
            if(task.getResult() != null) this.mldStudent.setValue(task.getResult());
            else this.snackbarText.setValue("Erro ao encontrar o estudante correspondente ao usuario");
        });
    }


}
