package com.gabriel.smartclass.viewModels;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gabriel.smartclass.adapter.FrequencyAdapter;
import com.gabriel.smartclass.adapter.GradesAdapter;
import com.gabriel.smartclass.dao.CourseDAO;
import com.gabriel.smartclass.model.Classroom;
import com.gabriel.smartclass.model.Course;
import com.gabriel.smartclass.model.Frequency;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.InstitutionUser;
import com.gabriel.smartclass.model.Student;
import com.gabriel.smartclass.model.StudentGrade;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClassroomStudentMainMenuViewModel extends ViewModel {
    private Classroom classroom;
    private Course course;
    private InstitutionUser institutionUser;
    private Institution institution;
    private final MutableLiveData<Student> mldStudent = new MutableLiveData<>();
    private MutableLiveData<String> snackbarText = new MutableLiveData<>();
    private final GradesAdapter gradeAdapter = new GradesAdapter();
    private final FrequencyAdapter frequencyAdapter = new FrequencyAdapter();

    public FrequencyAdapter getFrequencyAdapter() {
        return frequencyAdapter;
    }

    public GradesAdapter getGradeAdapter() {
        return gradeAdapter;
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

    public MutableLiveData<Student> getMldStudent() {
        return mldStudent;
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

    @SuppressLint("NotifyDataSetChanged")
    public void getStudentByID() {
        new CourseDAO().getStudentByID(this.institution.getId(), this.course.getId(), this.institutionUser.getId(), task -> {
            if (task.getResult() != null) {
                this.mldStudent.setValue(task.getResult());
                if (this.mldStudent.getValue() != null) {
                    if (this.mldStudent.getValue().getStudentGrades() != null) {
                        Stream<StudentGrade> gradeStream = mldStudent.getValue().getStudentGrades().stream().filter(object -> object.getClassroom().getId().equals(this.classroom.getId()));
                        this.gradeAdapter.getList().setValue(gradeStream.collect(Collectors.toList()));
                        this.gradeAdapter.notifyDataSetChanged();
                    }
                    if (this.mldStudent.getValue().getFrequencies() != null) {
                        Log.d("TAG", "getStudentByID: "+ mldStudent.getValue().getFrequencies());
                        Stream<Frequency> frequencyStream = mldStudent.getValue().getFrequencies().stream().filter(object -> object.getClassroom().getId().equals(this.classroom.getId()));
                        this.frequencyAdapter.getList().setValue(frequencyStream.collect(Collectors.toList()));
                        this.frequencyAdapter.notifyDataSetChanged();
                    }
                }
            } else
                this.snackbarText.setValue("Erro ao encontrar o estudante correspondente ao usuario");
        });
    }


}
