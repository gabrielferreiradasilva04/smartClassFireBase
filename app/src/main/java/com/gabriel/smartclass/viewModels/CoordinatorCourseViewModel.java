package com.gabriel.smartclass.viewModels;


import android.annotation.SuppressLint;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gabriel.smartclass.adapter.ClassroomAdapter;
import com.gabriel.smartclass.adapter.InstitutionUserAdapter;
import com.gabriel.smartclass.adapter.SimpleDefaultAdapter;
import com.gabriel.smartclass.dao.ClassroomDAO;
import com.gabriel.smartclass.dao.CourseDAO;
import com.gabriel.smartclass.dao.InstitutionUserDAO;
import com.gabriel.smartclass.dao.UserTypeDAO;
import com.gabriel.smartclass.model.Course;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.InstitutionUser;
import com.gabriel.smartclass.model.Student;
import com.gabriel.smartclass.model.Teacher;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressLint("NotifyDataSetChanged")
public class CoordinatorCourseViewModel extends ViewModel {
    private InstitutionUser coordinator;
    private Course course;
    private Institution institution;
    private InstitutionUserAdapter membersAdapter = new InstitutionUserAdapter();
    private final SimpleDefaultAdapter<InstitutionUser> membersSelectionAdapter = new SimpleDefaultAdapter<>();
    private final ClassroomAdapter classroomAdapter = new ClassroomAdapter();
    private MutableLiveData<String> snackbarText = new MutableLiveData<>();

    public void setSnackbarText(MutableLiveData<String> snackbarText) {
        this.snackbarText = snackbarText;
    }

    public ClassroomAdapter getClassroomAdapter() {
        return classroomAdapter;
    }

    public MutableLiveData<String> getSnackbarText() {
        return snackbarText;
    }

    public SimpleDefaultAdapter<InstitutionUser> getMembersSelectionAdapter() {
        return membersSelectionAdapter;
    }



    public InstitutionUserAdapter getMembersAdapter() {
        return membersAdapter;
    }

    public void setMembersAdapter(InstitutionUserAdapter membersAdapter) {
        this.membersAdapter = membersAdapter;
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

    public void setCoordinator(InstitutionUser coordinator) {
        this.coordinator = coordinator;
    }

    public void getCourseMembers(String institutionID, String courseID) {
        MutableLiveData<List<InstitutionUser>> institutionUsers = new MutableLiveData<>();
        List<InstitutionUser> tempList = new ArrayList<>();
        new CourseDAO().getAllTeachersAndStudents(institutionID, courseID, task -> {
            if (task.isSuccessful()) {
                List<DocumentReference> documentReferences = task.getResult();
                AtomicInteger pendingTasks = new AtomicInteger(documentReferences.size());
                for (DocumentReference documentReference : documentReferences) {
                    new InstitutionUserDAO().getInstitutionUserByID(institutionID, documentReference.getId(), task1 -> {
                        if (task1.isSuccessful()) {
                            InstitutionUser institutionUser = task1.getResult().toObject(InstitutionUser.class);
                            tempList.add(institutionUser);
                        }
                        if (pendingTasks.decrementAndGet() == 0) {
                            institutionUsers.setValue(tempList);
                            this.membersAdapter.getInstitutionUsers().setValue(tempList);
                            this.membersAdapter.notifyDataSetChanged();
                        }
                    });
                }
            } else {
                snackbarText.setValue("Erro ao carregar membros de instituição");
            }
        });
    }

    public void getAllTeachers() {
        this.membersSelectionAdapter.setAddAdapter(true);
        new CourseDAO().getAllTeachersFromInstitution(this.institution.getId(), task -> {
            this.membersSelectionAdapter.getMutableLiveDataT().setValue(task.getResult().toObjects(InstitutionUser.class));
            this.membersSelectionAdapter.notifyDataSetChanged();
        });
    }

    public void addNewMemberOnCourse(InstitutionUser institutionUser, boolean isStudent) {
        if (!this.membersAdapter.getInstitutionUsers().getValue().contains(institutionUser)) {
            if (!isStudent) {
                CourseDAO courseDAO = new CourseDAO();
                Teacher teacher = new Teacher();
                teacher.setId(institutionUser.getId());
                teacher.setDescription(institutionUser.getDescription());
                teacher.setMainUserReference(institutionUser.getUser_id());
                courseDAO.addTeacher(this.institution.getId(), this.course.getId(), teacher, unused -> {
                    if (unused.isSuccessful()) {
                        updateTeachersList(institutionUser, courseDAO, true );
                        this.membersAdapter.addItem(institutionUser);

                    } else {
                        snackbarText.setValue("Ocorreu um erro ao adicionar o professor ao curso");
                    }
                }, e -> {
                    snackbarText.setValue("Ocorreu um erro inesperado, tente novamente mais tarde");
                });
            } else {
                CourseDAO courseDAO = new CourseDAO();
                Student student = new Student();
                student.setId(institutionUser.getId());
                student.setDescription(institutionUser.getDescription());
                student.setMainUserReference(institutionUser.getUser_id());
                courseDAO.addStudent(this.institution.getId(), this.course.getId(), student, unused -> {
                    if (unused.isSuccessful()) {
                        this.membersAdapter.addItem(institutionUser);
                        this.updateStudentList(institutionUser, courseDAO, true);
                    } else {
                        snackbarText.setValue("Ocorreu um erro ao adicionar o estudante ao curso");
                    }
                }, e -> snackbarText.setValue("Ocorreu um erro inesperado, tente novamente mais tarde"));
            }
        } else {
            snackbarText.setValue("Esse usuário já está vinculado ao curso...");
        }

    }

    private void updateStudentList(InstitutionUser institutionUser, CourseDAO courseDAO, boolean add) {
        HashMap<String, Object> updateCourse = new HashMap<>();
        List<String> students_id = this.course.getStudents_id();
        if(add){
            students_id.add(institutionUser.getId());
        }else{
            students_id.removeIf(object -> object.equals(institutionUser.getId()));
        }
        updateCourse.put("students_id", students_id);
        courseDAO.updateCourse(this.course.getId(), this.institution.getId(), updateCourse, complete ->{}, fail ->{});

    }

    private void updateTeachersList(InstitutionUser institutionUser, CourseDAO courseDAO, boolean add) {
        HashMap<String, Object> updateCourse = new HashMap<>();
        List<String> teachers_id = this.course.getTeachers_id();
        if(add){
            teachers_id.add(institutionUser.getId());
        }else{
            teachers_id.removeIf(object -> object.equals(institutionUser.getId()));
        }
        updateCourse.put("teachers_id", teachers_id);
        courseDAO.updateCourse(this.course.getId(), this.institution.getId(), updateCourse, complete ->{}, fail ->{});
    }

    public void removeMemberFromCourse(InstitutionUser institutionUser) {
        CourseDAO courseDAO = new CourseDAO();
        if (this.membersAdapter.getInstitutionUsers().getValue().contains(institutionUser)) {
            if (institutionUser.getUserType_id().equals(new UserTypeDAO().STUDENT_TYPE_REFERENCE)) {
                removeStudent(institutionUser, courseDAO);
            }
            removeTeacher(institutionUser, courseDAO);
        }
    }

    private void removeTeacher(InstitutionUser institutionUser, CourseDAO courseDAO) {
        courseDAO.getTeacherByID(institution.getId(), course.getId(), institutionUser.getId(), task -> {
            if (task.isComplete() && task.isSuccessful()) {
                Teacher teacher = task.getResult();
                if (teacher != null) {
                    courseDAO.removeTeacher(this.institution.getId(), this.course.getId(), teacher, taskRemoveTeacher -> {
                        this.updateTeachersList(institutionUser, courseDAO, false);
                        snackbarText.setValue("Professor removido do curso");
                        this.membersAdapter.removeItem(institutionUser);
                    }, eRemoveTeacher -> snackbarText.setValue("Ocorreu um erro ao remover o membro do curso, tente novamente mais tarde.."));
                }
            } else {
                snackbarText.setValue("Ocorreu um erro inesperado, mas já estamos trabalhando nisso...");
            }
        });
    }
    private void removeStudent(InstitutionUser institutionUser, CourseDAO courseDAO) {
        courseDAO.getStudentByID(institution.getId(), course.getId(), institutionUser.getId(), task -> {
            if (task.isComplete() && task.isSuccessful()) {
                Student student = task.getResult();
                if (student != null) {
                    courseDAO.removeStudent(this.institution.getId(), this.course.getId(), student, taskRemoveStudent -> {
                        if (task.isSuccessful()) {
                            this.updateStudentList(institutionUser, courseDAO, false);
                            snackbarText.setValue("Estudante Removido do Curso");
                            this.membersAdapter.removeItem(institutionUser);
                        } else {
                            snackbarText.setValue("Ocorreu um erro ao remover o membro do curso, tente novamente mais tarde...");
                        }
                    }, eRemoveStudent -> snackbarText.setValue("Ocorreu um erro inesperado, não sabemos o que aconteceu, mas estamos trabalhando nisso..."));
                }
            }
        });
    }

    public void getAllStudents() {
        this.membersSelectionAdapter.setAddAdapter(true);
        new CourseDAO().getStudentsAsInstitutionUser(this.institution.getId(), task -> {
            if (task.isSuccessful()) {
                this.membersSelectionAdapter.getMutableLiveDataT().setValue(task.getResult());
                this.membersSelectionAdapter.notifyDataSetChanged();
            } else {
                snackbarText.setValue("Ocorreu um erro ao buscar os alunos deste curso. Tente novamente mais tarde!");
            }
        });
    }
    public void getClassrooms(){
        new ClassroomDAO().getAllClassrooms(this.institution.getId(), this.course.getId(), courseTask ->{
            if(courseTask.isSuccessful()){
                this.classroomAdapter.getClassrooms().setValue(courseTask.getResult());
                this.classroomAdapter.notifyDataSetChanged();
            }else{
                snackbarText.setValue("Ops, ocorreu um erro inesperado, tente novamente mais tarde");
            }
        });
    }

}
