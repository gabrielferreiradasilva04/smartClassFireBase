package com.gabriel.smartclass.viewModels;

import android.annotation.SuppressLint;
import android.util.Log;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gabriel.smartclass.adapter.CourseRCAdapter;
import com.gabriel.smartclass.adapter.SimpleDefaultAdapter;
import com.gabriel.smartclass.adapter.spinnerAdapters.SpinnerAdapterGeneric;
import com.gabriel.smartclass.dao.AreaDAO;
import com.gabriel.smartclass.dao.CourseDAO;
import com.gabriel.smartclass.dao.InstitutionUserDAO;
import com.gabriel.smartclass.dao.SubjectDAO;
import com.gabriel.smartclass.dao.UserTypeDAO;
import com.gabriel.smartclass.model.Area;
import com.gabriel.smartclass.model.Course;
import com.gabriel.smartclass.model.InstitutionUser;
import com.gabriel.smartclass.model.Subject;
import com.gabriel.smartclass.view.course.dialogs.AddSubjectsOnCourseDialog;
import com.gabriel.smartclass.view.course.fragments.SearchAndEditCourses;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchAndEditCourseViewModel extends ViewModel {
    private final SearchAndEditCourses view;
    private final CourseRCAdapter adapter = new CourseRCAdapter();
    private final MutableLiveData<List<Course>> mutableLiveDataCourse = new MutableLiveData<>();
    private final MutableLiveData<String> snackBarText = new MutableLiveData<>();
    //editDialog fields and components
    private SpinnerAdapterGeneric<Area> spinnerAreaAdapter;
    private final MutableLiveData<List<Area>> areaMutableLiveData = new MutableLiveData<>(new ArrayList<>());
    private SpinnerAdapterGeneric<InstitutionUser> spinnerCoordinatorAdapter;
    private final MutableLiveData<List<InstitutionUser>> coordinatorMutableLiveData = new MutableLiveData<>(new ArrayList<>());
    private Course courseEdit; //atributo utilizado pelos dialog fragments para a edição dos dados do curso
    //addSubjectsOnCourse filds and components
    private SimpleDefaultAdapter<Subject> subjectRCAdapter;

    public SimpleDefaultAdapter<Subject> getSubjectRCAdapter() {
        return subjectRCAdapter;
    }

    public Course getCourseEdit() {
        return courseEdit;
    }

    public void setCourseEdit(Course courseEdit) {
        this.courseEdit = courseEdit;
    }

    public SpinnerAdapterGeneric<Area> getSpinnerAreaAdapter() {
        return spinnerAreaAdapter;
    }

    public SpinnerAdapterGeneric<InstitutionUser> getSpinnerCoordinatorAdapter() {
        return spinnerCoordinatorAdapter;
    }

    public SearchAndEditCourses getView() {
        return view;
    }

    public CourseRCAdapter getAdapter() {
        return adapter;
    }

    public MutableLiveData<List<Course>> getMutableLiveDataCourse() {
        return mutableLiveDataCourse;
    }

    public MutableLiveData<String> getSnackBarText() {
        return snackBarText;
    }

    public SearchAndEditCourseViewModel(SearchAndEditCourses view) {
        this.view = view;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void searchCoursesAndPopulateAdapter(String institutionID, String courseTitle) {
        new CourseDAO().simpleSearchCourse(institutionID, courseTitle, task -> {
            if (task.isComplete() && task.isSuccessful()) {
                adapter.getMutableliveDataCourse().setValue(task.getResult().toObjects(Course.class));
                adapter.notifyDataSetChanged();
            } else {
                adapter.getMutableliveDataCourse().setValue(new ArrayList<>());
                adapter.notifyDataSetChanged();
            }
        }, e -> {
            snackBarText.setValue("Erro ao realziar pesquisa");
            adapter.getMutableliveDataCourse().setValue(new ArrayList<>());
            adapter.notifyDataSetChanged();
        });
    }

    public void deleteCourse(String institutionID, Course course) {
        new CourseDAO().deleteCourse(institutionID, course.getId(), task -> {
            if (task.isSuccessful()) {
                snackBarText.setValue("Curso deletado com sucesso!");
                adapter.removeItem(course);
            }
        }, e -> snackBarText.setValue("Erro ao deletar curso"));
    }
// edit dialog methods
    public void getAreasAndPopulateSpinnerOnEditDialog(String institutionID, Spinner spinner, Course course) {
        spinnerAreaAdapter = new SpinnerAdapterGeneric<>(view.getContext(), areaMutableLiveData);
        spinner.setAdapter(spinnerAreaAdapter);
        new AreaDAO().getAllAreas(institutionID, task -> {
            if (task.isComplete() && task.isSuccessful()) {
                spinnerAreaAdapter.getMutableLiveDataTList().setValue(task.getResult().toObjects(Area.class));
                for (Area area : spinnerAreaAdapter.getMutableLiveDataTList().getValue()) {
                    if (area.getId().equals(course.getArea_id().getId())) {
                        spinner.setSelection(spinnerAreaAdapter.getMutableLiveDataTList().getValue().indexOf(area));
                    }
                }
                spinnerAreaAdapter.notifyDataSetChanged();
            } else {
                snackBarText.setValue("Erro ao carregar areas de curso");
            }
        });
    }

    public void getCoordinatorsAndPopulateSpinnerOnEditDialog(String institutionID, Spinner spinner, Course course) {
        spinnerCoordinatorAdapter = new SpinnerAdapterGeneric<>(view.getContext(), coordinatorMutableLiveData);
        spinner.setAdapter(spinnerCoordinatorAdapter);
        new InstitutionUserDAO().getInstitutionUserByUserType(new UserTypeDAO().COORDINATOR_TYPE_REFERENCE, institutionID, task -> {
            if (task.isComplete() && task.isSuccessful()) {
                Log.d("Retorno", "getCoordinatorsAndPopulateSpinnerOnEditDialog: " + task.getResult().toObjects(InstitutionUser.class));
                spinnerCoordinatorAdapter.getMutableLiveDataTList().setValue(task.getResult().toObjects(InstitutionUser.class));
                for (InstitutionUser user : spinnerCoordinatorAdapter.getMutableLiveDataTList().getValue()) {
                    if (user.getId().equals(course.getCoordinator_id().getId())) {
                        spinner.setSelection(spinnerCoordinatorAdapter.getMutableLiveDataTList().getValue().indexOf(user));
                    }
                }
                spinnerCoordinatorAdapter.notifyDataSetChanged();
            }
        }, e -> {
        });
    }

    public void initEditCourseDialog(String institutionID, Course course, Spinner areaSpinner, Spinner coordinatorSpinner) {
        getCoordinatorsAndPopulateSpinnerOnEditDialog(institutionID, coordinatorSpinner, course);
        getAreasAndPopulateSpinnerOnEditDialog(institutionID, areaSpinner, course);
    }

    public void saveCourseChanges(String description, String duration, String division, InstitutionUser coordinator, Area area, Course course, String institutionID, DialogFragment dialogFragment) {
        try {
            int durationInt;
            int divisionInt;
            durationInt = Integer.parseInt(duration);
            divisionInt = Integer.parseInt(division);
            if (durationInt <= 1) {
                throw new RuntimeException("Insira uma duração maior que 1 ano");
            } else if (divisionInt <= 1 || divisionInt > 4) {
                throw new RuntimeException("A divisão do curso deve ser feita em semestres (2) bimestres (4) ou trimestres (3)");

            } else {
                int position  = adapter.getMutableliveDataCourse().getValue().indexOf(course);
                Course courseChanged = adapter.getMutableliveDataCourse().getValue().get(position);
                HashMap<String, Object> updates = new HashMap<>();
                if (!description.equalsIgnoreCase(course.getDescription())) {
                    updates.put("description", description);
                    courseChanged.setDescription(description);
                }
                if (durationInt != course.getDuration()) {
                    updates.put("duration", durationInt);
                    courseChanged.setDuration(durationInt);
                }
                if (divisionInt != course.getDivision_of_the_school_year()) {
                    updates.put("division_of_the_school_year", divisionInt);
                    courseChanged.setDivision_of_the_school_year(divisionInt);
                }
                if (!coordinator.getId().equals(course.getCoordinator_id().getId())) {
                    DocumentReference coordinatorReference = new InstitutionUserDAO().getInstitutionUserReferenceByID(institutionID, coordinator.getId());
                    updates.put("coordinator_id", coordinatorReference);
                    courseChanged.setCoordinator_id(coordinatorReference);
                }
                if (!area.getId().equals(course.getArea_id().getId())) {
                    DocumentReference areaReference = new AreaDAO().getAreaReferenceByID(institutionID, area.getId());
                    updates.put("area_id", areaReference);
                    courseChanged.setArea_id(areaReference);
                }
                if(!updates.isEmpty()){
                    new CourseDAO().updateCourse(course.getId(), institutionID, updates, task -> {
                        adapter.notifyItemChanged(position);
                        Toast.makeText(dialogFragment.getContext(),"Curso atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                        dialogFragment.dismiss();
                    }, e-> snackBarText.setValue("Ocorreu um erro durante a atualização, tente novamente mais tarde!"));
                }
                else{
                    throw new RuntimeException("Não foram realizadas alterações");
                }
            }
        } catch (RuntimeException e) {
            snackBarText.setValue(e.getMessage());
        }
    }
    // add subjects dialog methods
    @SuppressLint("NotifyDataSetChanged")
    public void initSubjectAdapter(String institutionID){
        new SubjectDAO().getAllSubjectsFromCourse(institutionID, courseEdit.getId(), task -> {
            if(task.isComplete() && task.isSuccessful()){
                this.subjectRCAdapter.getMutableLiveDataT().setValue(task.getResult().toObjects(Subject.class));
                this.subjectRCAdapter.notifyDataSetChanged();
            }
        }, e->{});
        this.subjectRCAdapter = new SimpleDefaultAdapter<>();
    }

    public void addNewSubjectOnCourse(String institutionID, String description, String minimumGrade){
        if(!description.isEmpty() && !minimumGrade.isEmpty()){
            double minimumGradeInt = Double.parseDouble(minimumGrade);
            Subject subject = new Subject();
            subject.setMinimum_grade(minimumGradeInt);
            subject.setDescription(description.toLowerCase());
            new CourseDAO().addSubjectsOnCourse(this.courseEdit.getId(), institutionID, subject, task -> {
                subject.setId(task.getResult().getId());
                updateSubjectID(institutionID, task);
                this.subjectRCAdapter.addItem(subject);
            }, e -> {
            });
        }else{
            snackBarText.setValue("Preencha todos os campos");
        }
    }

    private void updateSubjectID(String institutionID, Task<DocumentReference> task) {
        HashMap<String, Object> update = new HashMap<>();
        update.put("id", task.getResult().getId());
        new SubjectDAO().updateSubject(institutionID, courseEdit.getId(), task.getResult().getId(), update, unused ->{}, e -> {});
    }

    public void removeSubjectFromCourse(String institutionID, String courseID, Subject subject){
        new CourseDAO().removeSubjectFromCourse(institutionID,courseID,subject.getId(), task->{
            subjectRCAdapter.removeItem(subject);
        }, e -> {
            snackBarText.setValue("Erro ao deltar matéria");
        });

    }


}