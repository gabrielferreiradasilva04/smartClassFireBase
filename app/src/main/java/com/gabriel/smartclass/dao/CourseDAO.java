package com.gabriel.smartclass.dao;

import com.gabriel.smartclass.model.Classroom;
import com.gabriel.smartclass.model.ClassroomSubject;
import com.gabriel.smartclass.model.Course;
import com.gabriel.smartclass.model.CourseMember;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.InstitutionUser;
import com.gabriel.smartclass.model.Student;
import com.gabriel.smartclass.model.Subject;
import com.gabriel.smartclass.model.Teacher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CourseDAO {
    private final String COLLECTION = Course.class.getSimpleName();
    private final String INSTITUTIONCOLLECTION = Institution.class.getSimpleName();
    private final String CLASSROOMCOLLECTION = Classroom.class.getSimpleName();
    private final String SUBJECTSCOLLECTION = Subject.class.getSimpleName();
    private final String STUDENTSCOLLECION = Student.class.getSimpleName();
    private final String TEACHERSCOLLECTION = Teacher.class.getSimpleName();
    private final String INSTITUTIONUSERCOLLECTION = InstitutionUser.class.getSimpleName();
    private final String SEARCHAUXSTRING = "\uffff";

    public void saveNewCourse(Course course, String institutionID, OnCompleteListener<DocumentReference> onCompleteListener, OnFailureListener onFailureListener) {
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        DocumentReference institutionReference = fb.collection(INSTITUTIONCOLLECTION).document(institutionID);
        institutionReference.collection(COLLECTION).add(course).addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }

    public void updateCourse(String courseID, String institutionID, HashMap<String, Object> updates, OnCompleteListener<Void> onCompleteListener, OnFailureListener onFailureListener) {
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        fb.collection(INSTITUTIONCOLLECTION).document(institutionID).collection(COLLECTION).document(courseID).update(updates).addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }

    public void findCourseByName(String institutionID, String courseName, OnCompleteListener<QuerySnapshot> onCompleteListener, OnFailureListener onFailureListener) {
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        fb.collection(INSTITUTIONCOLLECTION).document(institutionID).collection(COLLECTION).whereEqualTo("name", courseName).get().addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }

    public void simpleSearchCourse(String institutionID, String courseTitle, OnCompleteListener<QuerySnapshot> onCompleteListener, OnFailureListener onFailureListener) {
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        fb.collection(INSTITUTIONCOLLECTION).document(institutionID).collection(COLLECTION)
                .whereGreaterThanOrEqualTo("name", courseTitle).whereLessThanOrEqualTo("name", courseTitle + SEARCHAUXSTRING).get()
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(onFailureListener);

    }

    public void deleteCourse(String institutionID, String courseID, OnCompleteListener<Void> onCompleteListener, OnFailureListener onFailureListener) {
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        fb.collection(INSTITUTIONCOLLECTION).document(institutionID).collection(COLLECTION).document(courseID)
                .delete()
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(onFailureListener);
    }

    public void addSubjectsOnCourse(String courseID, String institutionID, Subject subject, OnCompleteListener<DocumentReference> onCompleteListener, OnFailureListener onFailureListener) {
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        fb.collection(INSTITUTIONCOLLECTION).document(institutionID).collection(COLLECTION).document(courseID)
                .collection(SUBJECTSCOLLECTION)
                .add(subject)
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(onFailureListener);
    }

    public void removeSubjectFromCourse(String institutionID, String courseID, String subjectID, OnCompleteListener<Void> onCompleteListener, OnFailureListener onFailureListener) {
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        fb.collection(INSTITUTIONCOLLECTION).document(institutionID).collection(COLLECTION).document(courseID).collection(SUBJECTSCOLLECTION)
                .document(subjectID)
                .delete()
                .addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }

    public void addStudent(String institutionID, String courseID, Student student, OnCompleteListener<Void> onCompleteListener, OnFailureListener onFailureListener) {
        FirebaseFirestore.getInstance()
                .collection(INSTITUTIONCOLLECTION)
                .document(institutionID)
                .collection(COLLECTION)
                .document(courseID)
                .collection(STUDENTSCOLLECION)
                .document(student.getId())
                .set(student)
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(onFailureListener);

    }

    public void removeStudent(String institutionID, String courseID, Student student, OnCompleteListener<Void> onCompleteListener, OnFailureListener onFailureListener) {
        FirebaseFirestore.getInstance()
                .collection(INSTITUTIONCOLLECTION)
                .document(institutionID)
                .collection(COLLECTION)
                .document(courseID)
                .collection(STUDENTSCOLLECION)
                .document(student.getId()).delete()
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(onFailureListener);
    }

    public void addTeacher(String institutionID, String courseID, Teacher teacher, OnCompleteListener<Void> onCompleteListener, OnFailureListener onFailureListener) {
        FirebaseFirestore.getInstance()
                .collection(INSTITUTIONCOLLECTION)
                .document(institutionID)
                .collection(COLLECTION)
                .document(courseID)
                .collection(TEACHERSCOLLECTION)
                .document(teacher.getId())
                .set(teacher)
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(onFailureListener);
    }

    public void removeTeacher(String institutionID, String courseID, Teacher teacher, OnCompleteListener<Void> onCompleteListener, OnFailureListener onFailureListener) {
        FirebaseFirestore.getInstance()
                .collection(INSTITUTIONCOLLECTION)
                .document(institutionID)
                .collection(COLLECTION)
                .document(courseID)
                .collection(TEACHERSCOLLECTION)
                .document(teacher.getId()).delete()
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(onFailureListener);
    }

    public void getAllTeachersAndStudents(String institutionID, String courseID, final OnCompleteListener<List<DocumentReference>> onCompleteListener) {
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        Task<QuerySnapshot> teacherCollectionList = fb.collection(INSTITUTIONCOLLECTION).document(institutionID).collection(COLLECTION).document(courseID).collection(TEACHERSCOLLECTION).get();
        Task<QuerySnapshot> studentsCollectionList = fb.collection(INSTITUTIONCOLLECTION).document(institutionID).collection(COLLECTION).document(courseID).collection(STUDENTSCOLLECION).get();
        Tasks.whenAllComplete(teacherCollectionList, studentsCollectionList).addOnCompleteListener(task -> {
            List<DocumentReference> combinedList = new ArrayList<>();
            if (task.isSuccessful()) {
                QuerySnapshot teachersSnapshot = teacherCollectionList.getResult();
                QuerySnapshot studentsSnapshot = studentsCollectionList.getResult();
                if (teachersSnapshot != null) {
                    for (QueryDocumentSnapshot document : teachersSnapshot) {
                        combinedList.add(document.getReference());
                    }
                }
                if (studentsSnapshot != null) {
                    for (QueryDocumentSnapshot document : studentsSnapshot) {
                        combinedList.add(document.getReference());
                    }
                }
                onCompleteListener.onComplete(Tasks.forResult(combinedList));
            } else {
                onCompleteListener.onComplete(Tasks.forException(task.getException()));
            }
        });
    }

    public void getAllTeachersFromInstitution(String institutionID, OnCompleteListener<QuerySnapshot> onCompleteListener) {
        FirebaseFirestore.getInstance().collection(INSTITUTIONCOLLECTION).document(institutionID).collection(INSTITUTIONUSERCOLLECTION).whereEqualTo("userType_id", new UserTypeDAO().TEACHER_TYPE_REFERENCE).get().addOnCompleteListener(onCompleteListener);
    }

    public void getStudentsAsInstitutionUser(String institutionID, OnCompleteListener<List<InstitutionUser>> onCompleteListener) {
        Task<QuerySnapshot> studentsTask = FirebaseFirestore.getInstance().collection(INSTITUTIONCOLLECTION).document(institutionID).collection(INSTITUTIONUSERCOLLECTION).whereEqualTo("userType_id", new UserTypeDAO().STUDENT_TYPE_REFERENCE).get();
        Tasks.whenAllComplete(studentsTask).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<InstitutionUser> institutionUserList = new ArrayList<>();
                QuerySnapshot snapshot = studentsTask.getResult();
                if (snapshot != null) {
                    institutionUserList = snapshot.toObjects(InstitutionUser.class);
                }
                onCompleteListener.onComplete(Tasks.forResult(institutionUserList));
            } else {
                onCompleteListener.onComplete(Tasks.forException(Objects.requireNonNull(task.getException())));
            }
        });
    }

    public void getStudentsAsStudents(String institutionID, String courseID, OnCompleteListener<List<Student>> onCompleteListener) {
        Task<QuerySnapshot> studentsTask = FirebaseFirestore.getInstance().collection(INSTITUTIONCOLLECTION).document(institutionID).collection(COLLECTION).document(courseID).collection(STUDENTSCOLLECION).get();
        Tasks.whenAllComplete(studentsTask).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Student> students = new ArrayList<>();
                QuerySnapshot snapshot = studentsTask.getResult();
                if (snapshot != null) {
                    students = snapshot.toObjects(Student.class);
                }
                onCompleteListener.onComplete(Tasks.forResult(students));
            } else {
                onCompleteListener.onComplete(Tasks.forException(Objects.requireNonNull(task.getException())));
            }
        });
    }

    public void getTeachersAsTeachers(String institutionID, String courseID, OnCompleteListener<List<Teacher>> onCompleteListener) {
        Task<QuerySnapshot> teacherstask = FirebaseFirestore.getInstance().collection(INSTITUTIONCOLLECTION).document(institutionID).collection(COLLECTION).document(courseID).collection(TEACHERSCOLLECTION).get();
        Tasks.whenAllComplete(teacherstask).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Teacher> teachers = new ArrayList<>();
                QuerySnapshot snapshot = teacherstask.getResult();
                if (snapshot != null) {
                    teachers = snapshot.toObjects(Teacher.class);
                }
                onCompleteListener.onComplete(Tasks.forResult(teachers));
            } else {
                onCompleteListener.onComplete(Tasks.forException(Objects.requireNonNull(task.getException())));
            }
        });
    }

    public void getStudentByID(String institutionID, String courseID, String userid, OnCompleteListener<Student> onCompleteListener) {
        Task<DocumentSnapshot> studentTask = FirebaseFirestore.getInstance().collection(INSTITUTIONCOLLECTION).document(institutionID).collection(COLLECTION).document(courseID).collection(STUDENTSCOLLECION).document(userid).get();
        Tasks.whenAllComplete(studentTask).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Student student = studentTask.getResult().toObject(Student.class);
                onCompleteListener.onComplete(Tasks.forResult(student));
            } else {
                onCompleteListener.onComplete(Tasks.forException(Objects.requireNonNull(task.getException())));
            }
        });

    }

    public void getTeacherByID(String institutionID, String courseID, String userid, OnCompleteListener<Teacher> onCompleteListener) {
        Task<DocumentSnapshot> teacherTask = FirebaseFirestore.getInstance().collection(INSTITUTIONCOLLECTION).document(institutionID).collection(COLLECTION).document(courseID).collection(TEACHERSCOLLECTION).document(userid).get();
        Tasks.whenAllComplete(teacherTask).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Teacher teacher = teacherTask.getResult().toObject(Teacher.class);
                onCompleteListener.onComplete(Tasks.forResult(teacher));
            } else {
                onCompleteListener.onComplete(Tasks.forException(Objects.requireNonNull(task.getException())));
            }
        });
    }

    public void getCourseSubject(String institutionID, String courseID, OnCompleteListener<List<Subject>> onCompleteListener) {
        Task<QuerySnapshot> subjectTask = FirebaseFirestore.getInstance().collection(INSTITUTIONCOLLECTION).document(institutionID).collection(COLLECTION).document(courseID).collection(SUBJECTSCOLLECTION).get();
        Tasks.whenAllComplete(subjectTask).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Subject> subjects = new ArrayList<>();
                QuerySnapshot snapshot = subjectTask.getResult();
                if (snapshot != null) {
                    subjects = snapshot.toObjects(Subject.class);
                }
                onCompleteListener.onComplete(Tasks.forResult(subjects));
            } else {
                onCompleteListener.onComplete(Tasks.forException(task.getException()));
            }
        });
    }

    public void getCourseClassroomsByUser(String institutionID, String courseID, String userID, OnCompleteListener<List<Classroom>> onCompleteListener) {
        DocumentReference userReference = FirebaseFirestore.getInstance()
                .collection(INSTITUTIONCOLLECTION)
                .document(institutionID)
                .collection(COLLECTION)
                .document(courseID)
                .collection(STUDENTSCOLLECION)
                .document(userID);
        DocumentReference userReference2 = FirebaseFirestore.getInstance()
                .collection(INSTITUTIONCOLLECTION)
                .document(institutionID)
                .collection(COLLECTION)
                .document(courseID)
                .collection(TEACHERSCOLLECTION)
                .document(userID);
        Task<QuerySnapshot> courseTask = FirebaseFirestore.getInstance()
                .collection(INSTITUTIONCOLLECTION)
                .document(institutionID)
                .collection(COLLECTION)
                .document(courseID)
                .collection(CLASSROOMCOLLECTION)
                .where(
                        Filter.or(Filter.arrayContains("students_id", userReference),
                                Filter.arrayContains("teachers_id", userReference),
                                Filter.arrayContains("teachers_id", userReference2),
                                Filter.arrayContains("students_id", userReference2))
                )
                .get();
        Tasks.whenAllComplete(courseTask).addOnCompleteListener(maintask->{
           if(courseTask.isSuccessful()){
               QuerySnapshot snapshot = courseTask.getResult();
               List<Classroom> list = new ArrayList<>();
               if(snapshot != null){
                   list = snapshot.toObjects(Classroom.class);
               }
               onCompleteListener.onComplete(Tasks.forResult(list));
           }else{
               onCompleteListener.onComplete(Tasks.forException(maintask.getException()));
           }
        });
    }
    public void getAllStudents(String institutionID, String courseID, OnCompleteListener<List<CourseMember>> onCompleteListener){
        Task<QuerySnapshot> studentTask = FirebaseFirestore.getInstance().collection(INSTITUTIONCOLLECTION).document(institutionID).collection(COLLECTION).document(courseID).collection(STUDENTSCOLLECION).get();
        Tasks.whenAllComplete(studentTask).addOnCompleteListener(task ->{
            if(task.isSuccessful()){
                List<CourseMember> list = new ArrayList<>();
                QuerySnapshot snapshot = studentTask.getResult();
                if(snapshot != null){
                    list = snapshot.toObjects(CourseMember.class);
                }
                onCompleteListener.onComplete(Tasks.forResult(list));
            }else{
                onCompleteListener.onComplete(Tasks.forException(task.getException()));
            }
        });
    }
    public void getTeacherSubject(String institutionID, String courseID, String userID, String classroomID, OnCompleteListener<List<Subject>> onCompleteListener) {
        Task<QuerySnapshot> subjectTask = FirebaseFirestore.getInstance()
                .collection(INSTITUTIONCOLLECTION)
                .document(institutionID)
                .collection(COLLECTION)
                .document(courseID)
                .collection(CLASSROOMCOLLECTION)
                .document(classroomID).collection(ClassroomSubject.class.getSimpleName()).whereEqualTo("teacher.id", userID).get();
        Tasks.whenAllComplete(subjectTask).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Subject> list = new ArrayList<>();
                QuerySnapshot snapshot = subjectTask.getResult();
                if (snapshot != null) {
                    list = snapshot.toObjects(Subject.class);
                }
                onCompleteListener.onComplete(Tasks.forResult(list));
            } else {
                onCompleteListener.onComplete(Tasks.forException(task.getException()));
            }
        });
    }
    public void updateStudent(String institutionID, String courseID, String userId, HashMap<String, Object> update, OnCompleteListener<Void> onCompleteListener){
        FirebaseFirestore.getInstance()
                .collection(INSTITUTIONCOLLECTION)
                .document(institutionID)
                .collection(COLLECTION)
                .document(courseID)
                .collection(STUDENTSCOLLECION)
                .document(userId)
                .update(update).addOnCompleteListener(onCompleteListener);
    }
    public void syncStudent(String institutionID, String courseID, String userId, EventListener<DocumentSnapshot> listener){
        FirebaseFirestore.getInstance()
                .collection(INSTITUTIONCOLLECTION)
                .document(institutionID)
                .collection(COLLECTION)
                .document(courseID)
                .collection(STUDENTSCOLLECION)
                .document(userId).addSnapshotListener(listener);
    }
}
