package com.gabriel.smartclass.viewModels;

import android.annotation.SuppressLint;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gabriel.smartclass.adapter.CourseRCAdapter;
import com.gabriel.smartclass.dao.CourseDAO;
import com.gabriel.smartclass.dao.InstitutionUserDAO;
import com.gabriel.smartclass.dao.UserDAO;
import com.gabriel.smartclass.dao.UserTypeDAO;
import com.gabriel.smartclass.model.Course;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.InstitutionUser;
import com.gabriel.smartclass.model.User;
import com.gabriel.smartclass.model.UserType;

import java.util.ArrayList;

public class InstitutionUserMainMenuViewModel extends ViewModel {
    private InstitutionUser currentInstitutionUser;
    private final MutableLiveData<User> userByInstitutionUser = new MutableLiveData<>();
    private final MutableLiveData<UserType> userTypeByInstitutionUser = new MutableLiveData<>();
    private Institution currentInstitution;
    private CourseRCAdapter courseAdapter;
    private final MutableLiveData<String> snackbarText = new MutableLiveData<>();
    private final CourseRCAdapter searchCoursesAdapter = new CourseRCAdapter();

    public InstitutionUser getCurrentInstitutionUser() {
        return currentInstitutionUser;
    }

    public CourseRCAdapter getSearchCoursesAdapter() {
        return searchCoursesAdapter;
    }

    public void setCurrentInstitutionUser(InstitutionUser currentInstitutionUser) {
        this.currentInstitutionUser = currentInstitutionUser;
    }

    public Institution getCurrentInstitution() {
        return currentInstitution;
    }

    public void setCurrentInstitution(Institution currentInstitution) {
        this.currentInstitution = currentInstitution;
    }

    public MutableLiveData<User> getUserByInstitutionUser() {
        return userByInstitutionUser;
    }

    public MutableLiveData<UserType> getUserTypeByInstitutionUser() {
        return userTypeByInstitutionUser;
    }


    public CourseRCAdapter getCourseAdapter() {
        return courseAdapter;
    }

    public MutableLiveData<String> getSnackbarText() {
        return snackbarText;
    }

    public void loadUserByInstitutionUser() {
        new UserDAO().getUserByDocumentReference(currentInstitutionUser.getUser_id(), task -> new UserTypeDAO().getUserTypeByDocumentReference(currentInstitutionUser.getUserType_id(), task1 -> {
            if (task.isComplete() && task.isSuccessful() && task1.isComplete() && task1.isSuccessful()) {
                this.userByInstitutionUser.setValue(task.getResult().toObject(User.class));
                this.userTypeByInstitutionUser.setValue(task1.getResult().toObject(UserType.class));
            }
        }, e -> snackbarText.setValue("Erro ao recuperar dados do usuário")), e -> snackbarText.setValue("Erro ao recuperar dados do usuário"));
    }

    @SuppressLint("NotifyDataSetChanged")
    public void loadUserCourses(String userType) {
        if(userType.equals("Coordenador")){
            this.courseAdapter = new CourseRCAdapter();
            this.courseAdapter.setShowOnlyCourseView(true);
            this.courseAdapter.setEnableCourseAccess(true);
            new InstitutionUserDAO().getInstitutionUserCourses(this.currentInstitution.getId(), this.currentInstitutionUser.getId(), userType, task -> {
                if (task.isComplete() && task.isSuccessful()) {
                    this.courseAdapter.getMutableliveDataCourse().setValue(task.getResult().toObjects(Course.class));
                    this.courseAdapter.notifyDataSetChanged();
                }
            }, e -> snackbarText.setValue("Erro ao recuperar seus cursos, tente novamente mais tarde..."));
        }else{
            this.courseAdapter = new CourseRCAdapter();
            this.courseAdapter.setShowOnlyCourseView(false);
            this.courseAdapter.setEnableCourseAccess(true);
            new InstitutionUserDAO().getInstitutionUserCourses(this.currentInstitution.getId(), this.currentInstitutionUser.getId(), userType, task -> {
                if (task.isComplete() && task.isSuccessful()) {
                    this.courseAdapter.getMutableliveDataCourse().setValue(task.getResult().toObjects(Course.class));
                    this.courseAdapter.notifyDataSetChanged();
                }
            }, e -> snackbarText.setValue("Erro ao recuperar seus cursos, tente novamente mais tarde..."));
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    public void searchCourses(String institutionID, String title) {
        if (!institutionID.equals("")) {
            this.searchCoursesAdapter.setShowOnlyCourseView(true);
            new CourseDAO().simpleSearchCourse(institutionID, title, task -> {
                if (task.isComplete() && task.isSuccessful()) {
                    searchCoursesAdapter.getMutableliveDataCourse().setValue(task.getResult().toObjects(Course.class));
                    searchCoursesAdapter.notifyDataSetChanged();
                } else {
                    searchCoursesAdapter.getMutableliveDataCourse().setValue(new ArrayList<>());
                    searchCoursesAdapter.notifyDataSetChanged();
                }
            }, e -> {
                snackbarText.setValue("Não foi encontrada nenhuma instituição com esses parâmetros...");
                searchCoursesAdapter.getMutableliveDataCourse().setValue(new ArrayList<>());
                searchCoursesAdapter.notifyDataSetChanged();
            });
        } else {
            snackbarText.setValue("Erro ao realizar a sua busca, tente novamente mais tarde ");
        }

    }
}
