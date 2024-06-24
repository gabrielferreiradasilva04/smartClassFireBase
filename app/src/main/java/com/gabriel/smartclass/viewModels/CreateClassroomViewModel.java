package com.gabriel.smartclass.viewModels;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gabriel.smartclass.adapter.ClassroomSubjectsAdapter;
import com.gabriel.smartclass.adapter.SimpleAdapter;
import com.gabriel.smartclass.adapter.spinnerAdapters.SpinnerAdapterGeneric;
import com.gabriel.smartclass.dao.ClassroomDAO;
import com.gabriel.smartclass.dao.CourseDAO;
import com.gabriel.smartclass.model.Classroom;
import com.gabriel.smartclass.model.ClassroomSubject;
import com.gabriel.smartclass.model.Course;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.Student;
import com.gabriel.smartclass.model.Subject;
import com.gabriel.smartclass.model.Teacher;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("NotifyDataSetChanged")

public class CreateClassroomViewModel extends ViewModel {
    private Institution institution;
    private Course course;
    private SimpleAdapter<Teacher> teachersToadd = new SimpleAdapter();
    private SimpleAdapter<Student> studentsToAdd = new SimpleAdapter();
    private SimpleAdapter<Teacher> teachersOnClass = new SimpleAdapter<>();
    private SimpleAdapter<Student> studentsOnClass = new SimpleAdapter<>();
    private MutableLiveData<String> snackbarText = new MutableLiveData<>();
    private SpinnerAdapterGeneric<Subject> subjectSpinnerAdapter;
    private SpinnerAdapterGeneric<Teacher> teachersSpinnerAdapter;
    private ClassroomSubjectsAdapter subjectsAdapter = new ClassroomSubjectsAdapter();

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

    public void setTeachersToadd(SimpleAdapter<Teacher> teachersToadd) {
        this.teachersToadd = teachersToadd;
    }

    public void setStudentsToAdd(SimpleAdapter<Student> studentsToAdd) {
        this.studentsToAdd = studentsToAdd;
    }

    public SimpleAdapter<Teacher> getTeachersOnClass() {
        return teachersOnClass;
    }

    public void setTeachersOnClass(SimpleAdapter<Teacher> teachersOnClass) {
        this.teachersOnClass = teachersOnClass;
    }

    public SimpleAdapter<Student> getStudentsOnClass() {
        return studentsOnClass;
    }

    public void setStudentsOnClass(SimpleAdapter<Student> studentsOnClass) {
        this.studentsOnClass = studentsOnClass;
    }

    public MutableLiveData<String> getSnackbarText() {
        return snackbarText;
    }

    public void setSnackbarText(MutableLiveData<String> snackbarText) {
        this.snackbarText = snackbarText;
    }

    public SpinnerAdapterGeneric<Subject> getSubjectSpinnerAdapter() {
        return subjectSpinnerAdapter;
    }

    public void setSubjectSpinnerAdapter(SpinnerAdapterGeneric<Subject> subjectSpinnerAdapter) {
        this.subjectSpinnerAdapter = subjectSpinnerAdapter;
    }

    public SpinnerAdapterGeneric<Teacher> getTeachersSpinnerAdapter() {
        return teachersSpinnerAdapter;
    }

    public void setTeachersSpinnerAdapter(SpinnerAdapterGeneric<Teacher> teachersSpinnerAdapter) {
        this.teachersSpinnerAdapter = teachersSpinnerAdapter;
    }

    public ClassroomSubjectsAdapter getSubjectsAdapter() {
        return subjectsAdapter;
    }

    public void setSubjectsAdapter(ClassroomSubjectsAdapter subjectsAdapter) {
        this.subjectsAdapter = subjectsAdapter;
    }

    public SimpleAdapter<Student> getStudentsToAdd() {
        return studentsToAdd;
    }

    public SimpleAdapter<Teacher> getTeachersToadd() {
        return teachersToadd;
    }

    public void createClassroom(String description, int period) {
        if (!this.subjectsAdapter.getClassroomSubjects().getValue().isEmpty() && !this.teachersOnClass.getMutableLiveDataT().getValue().isEmpty() && !this.studentsOnClass.getMutableLiveDataT().getValue().isEmpty()
                && !description.equals("") && period != 0) {
            List<DocumentReference> students_id = new ArrayList<>();
            for (Student student : this.studentsOnClass.getMutableLiveDataT().getValue()) {
                DocumentReference studentReference = FirebaseFirestore.getInstance().collection(Institution.class.getSimpleName())
                        .document(institution.getId())
                        .collection(Course.class.getSimpleName())
                        .document(course.getId())
                        .collection(Student.class.getSimpleName())
                        .document(student.getId());
                students_id.add(studentReference);
            }
            List<DocumentReference> teachers_id = new ArrayList<>();
            for (Teacher teacher : this.teachersOnClass.getMutableLiveDataT().getValue()) {
                DocumentReference teacherReference = FirebaseFirestore.getInstance().collection(Institution.class.getSimpleName())
                        .document(institution.getId())
                        .collection(Course.class.getSimpleName())
                        .document(course.getId())
                        .collection(Teacher.class.getSimpleName())
                        .document(teacher.getId());
                teachers_id.add(teacherReference);
            }
            List<DocumentReference> subjects_id = new ArrayList<>();
            for (ClassroomSubject classroomSubject : this.subjectsAdapter.getClassroomSubjects().getValue()) {
                DocumentReference classroomSubjectReference = FirebaseFirestore.getInstance().collection(Institution.class.getSimpleName())
                        .document(this.institution.getId())
                        .collection(Course.class.getSimpleName())
                        .document(this.course.getId())
                        .collection(ClassroomSubject.class.getSimpleName())
                        .document(classroomSubject.getId());
                subjects_id.add(classroomSubjectReference);
            }
            Classroom classroom = new Classroom();
            classroom.setDescription(description);
            classroom.setPeriod(period);
            classroom.setStudents_id(students_id);
            classroom.setTeachers_id(teachers_id);
            classroom.setSubjects_id(subjects_id);
            new ClassroomDAO().saveClassroom(this.institution.getId(), this.course.getId(), classroom, task -> {
                snackbarText.setValue("Classe adicionada com sucesso...");
            });
        } else {
            snackbarText.setValue("Certifique-se de que preencheu todos os campos, todos eles são obrigatórios...");
        }

    }

    public void getCourseStudents() {
        new CourseDAO().getStudentsAsStudents(this.institution.getId(), this.course.getId(), task -> {
            if (task.isSuccessful()) {
                this.studentsToAdd.getMutableLiveDataT().setValue(task.getResult());
                this.studentsToAdd.notifyDataSetChanged();
            } else {
                snackbarText.setValue("Erro ao carregar estudantes do curso");
            }
        });
    }

    public void getCourseTeachers() {
        new CourseDAO().getTeachersAsTeachers(this.institution.getId(), this.course.getId(), task -> {
            if (task.isSuccessful()) {
                this.teachersToadd.getMutableLiveDataT().setValue(task.getResult());
                this.teachersToadd.notifyDataSetChanged();
            } else {
                snackbarText.setValue("Erro ao carregar estudantes do curso");
            }
        });
    }

    public void loadMemberSelectionAdapter(boolean isStudent) {
        if (isStudent) {
            this.getCourseStudents();
        } else {
            this.getCourseTeachers();
        }
    }

    public void addTeacherOnClassroom(Teacher teacher) {
        this.teachersOnClass.addItem(teacher);
    }

    public void addStudentOnClassroom(Student student) {
        this.studentsOnClass.addItem(student);
    }
    public void removeTeacherFromClassroom(Teacher teacher){
        if(this.teachersOnClass.getMutableLiveDataT().getValue().contains(teacher)){
            this.teachersOnClass.removeItem(teacher);
        }
    }
    public void removeStudentFromClassroom(Student student){
        if(this.studentsOnClass.getMutableLiveDataT().getValue().contains(student)){
            this.studentsOnClass.removeItem(student);
        }
    }

    public void getCourseSubjects(Context context) {
        this.subjectSpinnerAdapter = new SpinnerAdapterGeneric<Subject>(context, new MutableLiveData<>(new ArrayList<>()));
        new CourseDAO().getCourseSubject(this.institution.getId(), this.course.getId(), subjectTask -> {
            this.subjectSpinnerAdapter.getMutableLiveDataTList().setValue(subjectTask.getResult());
            this.subjectSpinnerAdapter.notifyDataSetChanged();
        });
    }

    public void getCourseTeachers(Context context) {
        this.teachersSpinnerAdapter = new SpinnerAdapterGeneric<>(context, new MutableLiveData<>(new ArrayList<>()));
        new CourseDAO().getTeachersAsTeachers(this.institution.getId(), this.course.getId(), task -> {
            if (task.isSuccessful()) {
                this.teachersSpinnerAdapter.getMutableLiveDataTList().setValue(task.getResult());
                this.teachersSpinnerAdapter.notifyDataSetChanged();
            }
        });
    }

    public void createClassroomSubject(@NonNull Subject subject, @NonNull Teacher teacher) {
        ClassroomSubject classroomSubject = new ClassroomSubject();
        classroomSubject.setDescription(subject.getDescription());
        classroomSubject.setId(subject.getId());
        classroomSubject.setMinimum_grade(subject.getMinimum_grade());
        classroomSubject.setTeacher(teacher);
        this.subjectsAdapter.addItem(classroomSubject);
    }
}
