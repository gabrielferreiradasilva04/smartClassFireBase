package com.gabriel.smartclass.viewModels;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gabriel.smartclass.dao.CourseDAO;
import com.gabriel.smartclass.model.Classroom;
import com.gabriel.smartclass.model.Course;
import com.gabriel.smartclass.model.CourseMember;
import com.gabriel.smartclass.model.Frequency;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.InstitutionUser;
import com.gabriel.smartclass.model.Student;
import com.gabriel.smartclass.model.StudentGrade;
import com.gabriel.smartclass.model.Subject;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ClassroomTeacherManagementViewModel extends ViewModel {
    private String id;

    private InstitutionUser teacher;
    private CourseMember courseMember;
    private MutableLiveData<Student> student = new MutableLiveData<>();
    private Subject subject;
    private Course course;
    private Institution institution;
    private StudentGrade grade;
    private Classroom classroom;
    private MutableLiveData<String> snackbartext = new MutableLiveData<>();

    public MutableLiveData<Student> getStudent() {
        return student;
    }

    public void setStudent(MutableLiveData<Student> student) {
        this.student = student;
    }

    public MutableLiveData<String> getSnackbartext() {
        return snackbartext;
    }

    public void setSnackbartext(MutableLiveData<String> snackbartext) {
        this.snackbartext = snackbartext;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCourseMember(CourseMember courseMember) {
        this.courseMember = courseMember;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public InstitutionUser getTeacher() {
        return teacher;
    }

    public void setTeacher(InstitutionUser teacher) {
        this.teacher = teacher;
    }

    public CourseMember getCourseMember() {
        return courseMember;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public StudentGrade getGrade() {
        return grade;
    }

    public void setGrade(StudentGrade grade) {
        this.grade = grade;
    }

    public void updateStudentGrade(double firstGrade, double lastGrade, double finalGrade) {
        new CourseDAO().getStudentByID(this.institution.getId(), this.course.getId(), this.courseMember.getId(), task -> {
            if (task.isSuccessful() && task.isComplete()) {
                Student student = task.getResult();
                if (student != null) {
                    Classroom classroom = this.classroom;
                    Subject subject = this.subject;
                    List<StudentGrade> studentGradeList = new ArrayList<>();
                    if (student.getStudentGrades() != null) {
                        studentGradeList = student.getStudentGrades();
                    }
                    StudentGrade studentGrade = new StudentGrade(subject, classroom, firstGrade, lastGrade, finalGrade);
                    studentGradeList.removeIf(object -> object.getSubject().getId().equals(subject.getId()));
                    studentGradeList.add(studentGrade);
                    HashMap<String, Object> update = new HashMap<>();
                    update.put("studentGrades", studentGradeList);
                    new CourseDAO().updateStudent(this.institution.getId(), this.course.getId(), student.getId(), update, task1 -> {
                        if (task1.isSuccessful() && task1.isComplete()) {
                            snackbartext.setValue("Nota atualizada com suceso!");
                        } else {
                            snackbartext.setValue("Ocorreu um erro ao atualizar a nota do aluno, tente novamente mais tarde.");
                        }
                    });
                }
            }
        });
    }

    public void getStudentByCourseMember() {
        new CourseDAO().getStudentByID(this.institution.getId(), this.course.getId(), this.courseMember.getId(), task -> {
            if (task.isSuccessful()) {
                this.student.setValue(task.getResult());
            }
        });
    }

    public void updateStudentFrequency(double frequencyPercent) {
        if (student != null) {
            HashMap<String, Object> update = new HashMap<>();
            List<Frequency> frequencies = new ArrayList<>();
            Frequency frequency = new Frequency();
            frequency.setSubject(this.getSubject());
            frequency.setClassroom(this.getClassroom());
            frequency.setPercent(frequencyPercent);
            if (frequencyPercent < 75) {
                frequency.setFinalDescription("Reprovando por Faltas");
            } else {
                frequency.setFinalDescription("Presenças OK");
            }
            if (Objects.requireNonNull(student.getValue()).getFrequencies() != null) {
                frequencies = student.getValue().getFrequencies();
            }
            frequencies.removeIf(object -> object.getSubject().getId().equals(this.subject.getId()));
            frequencies.add(frequency);
            update.put("frequencies", frequencies);
            new CourseDAO().updateStudent(this.institution.getId(), this.course.getId(), this.courseMember.getId(), update, task -> {
                if (task.isSuccessful()) {
                    this.snackbartext.setValue("Frequência do aluno atualizada");
                } else {
                    snackbartext.setValue("Erro ao atualizar frequência do aluno");
                }
            });
        }
    }

    public void syncStudent() {
        new CourseDAO().syncStudent(this.institution.getId(), this.course.getId(), this.courseMember.getId(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null && value != null && value.exists()) {
                    student.setValue(value.toObject(Student.class));
                }
            }
        });
    }

    public void lockPeriod(boolean positive) {
        if (positive) {
            if (this.student.getValue() != null && !this.student.getValue().getStudentGrades().isEmpty() && !this.student.getValue().getFrequencies().isEmpty()) {
                StudentGrade grade = null;
                HashMap<String, Object> update = new HashMap<>();
                for (StudentGrade sGrade : this.student.getValue().getStudentGrades()) {
                    if (sGrade.getSubject().getId().equals(this.subject.getId())) {
                        grade = sGrade;
                    }
                }
                List<StudentGrade> grades = this.student.getValue().getStudentGrades();
                if (grade != null) {
                    grade.setFinalResult("Aprovado");
                    grades.removeIf(object -> object.getSubject().getId().equals(this.subject.getId()));
                    grades.add(grade);
                    update.put("studentGrades", grades);
                    new CourseDAO().updateStudent(this.institution.getId(), this.course.getId(), this.courseMember.getId(), update, task -> {});
                }
            }
        }else{
            if (this.student.getValue() != null && !this.student.getValue().getStudentGrades().isEmpty() && !this.student.getValue().getFrequencies().isEmpty()) {
                StudentGrade grade = null;
                HashMap<String, Object> update = new HashMap<>();
                for (StudentGrade sGrade : this.student.getValue().getStudentGrades()) {
                    if (sGrade.getSubject().getId().equals(this.subject.getId())) {
                        grade = sGrade;
                    }
                }
                List<StudentGrade> grades = this.student.getValue().getStudentGrades();
                if (grade != null) {
                    grade.setFinalResult("Reprovado");
                    grades.removeIf(object -> object.getSubject().getId().equals(this.subject.getId()));
                    grades.add(grade);
                    update.put("studentGrades", grades);
                    new CourseDAO().updateStudent(this.institution.getId(), this.course.getId(), this.courseMember.getId(), update, task -> {});
                }
            }
        }
    }

}
