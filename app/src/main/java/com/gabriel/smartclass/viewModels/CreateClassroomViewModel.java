package com.gabriel.smartclass.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gabriel.smartclass.dao.ClassroomDAO;
import com.gabriel.smartclass.model.Classroom;
import com.gabriel.smartclass.model.ClassroomSubject;
import com.gabriel.smartclass.model.Course;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.InstitutionUser;
import com.gabriel.smartclass.model.Location;
import com.gabriel.smartclass.model.Student;
import com.gabriel.smartclass.model.Subject;
import com.gabriel.smartclass.model.Teacher;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CreateClassroomViewModel extends ViewModel {
    private Institution institution;
    private InstitutionUser coordinator;
    private Course course;
    private MutableLiveData<String> snackbarText = new MutableLiveData<>();
    public void createClassroom(String description, int period, List<Student> students, List<Teacher> teachers, List<ClassroomSubject> subjects){
        List<DocumentReference> students_id = new ArrayList<>();
        for (Student student: students) {
            DocumentReference studentReference = FirebaseFirestore.getInstance().collection(Institution.class.getSimpleName())
                    .document(institution.getId())
                    .collection(Course.class.getSimpleName())
                    .document(course.getId())
                    .collection(Student.class.getSimpleName())
                    .document(student.getId());
            students_id.add(studentReference);
        }
        List<DocumentReference> teachers_id = new ArrayList<>();
        for (Teacher teacher: teachers) {
            DocumentReference teacherReference = FirebaseFirestore.getInstance().collection(Institution.class.getSimpleName())
                    .document(institution.getId())
                    .collection(Course.class.getSimpleName())
                    .document(course.getId())
                    .collection(Teacher.class.getSimpleName())
                    .document(teacher.getId());
            teachers_id.add(teacherReference);
        }
        List<DocumentReference> subjects_id = new ArrayList<>();
        for (ClassroomSubject classroomSubject: subjects) {
            DocumentReference classroomSubjectReference = FirebaseFirestore.getInstance().collection(Institution.class.getSimpleName())
                    .document(institution.getId())
                    .collection(Course.class.getSimpleName())
                    .document(course.getId())
                    .collection(ClassroomSubject.class.getSimpleName())
                    .document(classroomSubject.getId());
            subjects_id.add(classroomSubjectReference);
        }
        Classroom classroom = new Classroom();
        classroom.setDescription(description);
        classroom.setPeriod(period);
        classroom.setStudents_id(students_id);
        classroom.setTeachers_id(teachers_id);
        new ClassroomDAO().saveClassroom(this.institution.getId(), this.course.getId(), classroom, task -> {
            snackbarText.setValue("Classe adicionada com sucesso...");
        });

    }
}
