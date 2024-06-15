package com.gabriel.smartclass.viewModels;


import android.annotation.SuppressLint;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gabriel.smartclass.adapter.InstitutionUserAdapter;
import com.gabriel.smartclass.adapter.SimpleDefaultAdapter;
import com.gabriel.smartclass.dao.CourseDAO;
import com.gabriel.smartclass.dao.InstitutionUserDAO;
import com.gabriel.smartclass.model.Course;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.InstitutionUser;
import com.gabriel.smartclass.model.Student;
import com.gabriel.smartclass.model.Teacher;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressLint("NotifyDataSetChanged")
public class CoordinatorCourseViewModel extends ViewModel {
    private InstitutionUser coordinator;
    private Course course;
    private Institution institution;
    private InstitutionUserAdapter membersAdapter = new InstitutionUserAdapter();
    private final SimpleDefaultAdapter<InstitutionUser> membersSelectionAdapter = new SimpleDefaultAdapter<>();

    public MutableLiveData<String> getSnackbarText() {
        return snackbarText;
    }

    public SimpleDefaultAdapter<InstitutionUser> getMembersSelectionAdapter() {
        return membersSelectionAdapter;
    }

    private MutableLiveData<String> snackbarText = new MutableLiveData<>();

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

    public InstitutionUser getCoordinator() {
        return coordinator;
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
                Teacher teacher = new Teacher();
                teacher.setId(institutionUser.getId());
                teacher.setDescription(institutionUser.getDescription());
                teacher.setInstitutionUserReference(institutionUser.getUser_id());
                new CourseDAO().addTeacher(this.institution.getId(), this.course.getId(), teacher, unused -> {
                    if(unused.isSuccessful()){
                        this.membersAdapter.addItem(institutionUser);

                    }else{
                        snackbarText.setValue("Ocorreu um erro ao adicionar o professor ao curso");
                    }
                }, e -> {
                    snackbarText.setValue("Ocorreu um erro inesperado, tente novamente mais tarde");
                });
            }else{
                Student student = new Student();
                student.setId(institutionUser.getId());
                student.setDescription(institutionUser.getDescription());
                student.setInstitutionUserReference(institutionUser.getUser_id());
                new CourseDAO().addStudent(this.institution.getId(), this.course.getId(), student, unused ->{
                    if(unused.isSuccessful()){
                        this.membersAdapter.addItem(institutionUser);
                    }else{
                        snackbarText.setValue("Ocorreu um erro ao adicionar o estudante ao curso");
                    }
                }, e -> snackbarText.setValue("Ocorreu um erro inesperado, tente novamente mais tarde"));
            }
        } else {
            snackbarText.setValue("Esse usuário já está vinculado ao curso...");
        }

    }

    public void getAllStudents() {
        new CourseDAO().getAllStudents(this.institution.getId(), task -> {
            if(task.isSuccessful()){
                this.membersSelectionAdapter.getMutableLiveDataT().setValue(task.getResult());
                this.membersSelectionAdapter.notifyDataSetChanged();
            }else{
                snackbarText.setValue("Ocorreu um erro ao buscar os alunos deste curso. Tente novamente mais tarde!");
            }
        });
    }
}
