package com.gabriel.smartclass.viewModels;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gabriel.smartclass.adapter.spinnerAdapters.SpinnerAdapterGeneric;
import com.gabriel.smartclass.dao.AreaDAO;
import com.gabriel.smartclass.dao.CourseDAO;
import com.gabriel.smartclass.dao.InstitutionUserDAO;
import com.gabriel.smartclass.dao.UserTypeDAO;
import com.gabriel.smartclass.model.Area;
import com.gabriel.smartclass.model.Course;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.InstitutionUser;
import com.gabriel.smartclass.view.course.fragments.AddCourse;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class AddCourseViewModel extends ViewModel {
    private final AddCourse fragment;
    private final MutableLiveData<String> snackbarText = new MutableLiveData<>();
    private final MutableLiveData<List<Area>> areasMutableLiveData = new MutableLiveData<>();
    private SpinnerAdapterGeneric<Area> spinnerAreaAdapter;
    private SpinnerAdapterGeneric<InstitutionUser> spinnerInstitutionUserAdapter;
    private final MutableLiveData<List<InstitutionUser>> institutionUsersMutableLiveData = new MutableLiveData<>();

    public AddCourseViewModel(AddCourse addCourse) {
        this.fragment = addCourse;
    }

    public MutableLiveData<String> getSnackbarText() {
        return snackbarText;
    }

    public void addNewArea(String description, String institutionID) {
        Area area = new Area(description);
        AreaDAO areaDAO = new AreaDAO();
        areaDAO.findAreaByDescription(description, institutionID, task2 -> {
            if (task2.isComplete() && !task2.getResult().isEmpty()) {
                Toast.makeText(fragment.getContext(), "Já existe uma área com essa descrição cadastrada!", Toast.LENGTH_SHORT).show();
            } else {
                if (description != null && !description.equals("")) {
                    areaDAO.addNewArea(area, institutionID, task -> {
                        updateAreaAndAdapter(institutionID, area, areaDAO, task);
                        Toast.makeText(fragment.getContext(), "Nova área cadastrata com sucesso!", Toast.LENGTH_SHORT).show();
                    }, e -> Toast.makeText(fragment.getContext(), "Algo deu errado, tente novamente...", Toast.LENGTH_SHORT).show());
                } else {
                    Toast.makeText(fragment.getContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void updateAreaAndAdapter(String institutionID, Area area, AreaDAO areaDAO, Task<DocumentReference> task) {
        String id = task.getResult().getId();
        HashMap<String, Object> updates = new HashMap<>();
        updates.put("id", id);
        areaDAO.updateArea(id, institutionID, updates);
        addNewItemOnAreaAdapter(area);
    }

    public void getAllAreasAndpopulateSpinner(String institutionID) {
        AreaDAO areaDAO = new AreaDAO();
        spinnerAreaAdapter = new SpinnerAdapterGeneric<>(fragment.getContext(), this.areasMutableLiveData);
        fragment.getAreaSpinner().setAdapter(spinnerAreaAdapter);
        areaDAO.getAllAreas(institutionID, task -> {
            if (task.isComplete() && !task.getResult().isEmpty()) {
                spinnerAreaAdapter.getMutableLiveDataTList().setValue(task.getResult().toObjects(Area.class));
                spinnerAreaAdapter.notifyDataSetChanged();
            }
        });
    }

    public void getCoordinatorsAndPopulateSpinner(String institutionID) {
        spinnerInstitutionUserAdapter = new SpinnerAdapterGeneric<>(fragment.getContext(), this.institutionUsersMutableLiveData);
        fragment.getCoordinatorSpinner().setAdapter(spinnerInstitutionUserAdapter);
        new InstitutionUserDAO().getInstitutionUserByUserType(new UserTypeDAO().COORDINATOR_TYPE_REFERENCE, institutionID, task -> {
            if (task.isComplete() && task.getResult() != null) {
                spinnerInstitutionUserAdapter.getMutableLiveDataTList().setValue(task.getResult().toObjects(InstitutionUser.class));
                spinnerInstitutionUserAdapter.notifyDataSetChanged();
            }
        }, e -> {
        });

    }

    public void addNewItemOnAreaAdapter(Area area) {
        if (area != null) {
            Objects.requireNonNull(spinnerAreaAdapter.getMutableLiveDataTList().getValue()).add(area);
            spinnerAreaAdapter.notifyDataSetChanged();
        }
    }

    public void createNewCourse(String institutionID) {
        String name = fragment.getEditTextname().getText().toString().trim().toLowerCase();
        String description = fragment.getEditTextDescription().getText().toString().trim().toLowerCase();
        String durationString = fragment.getEditTextDuration().getText().toString().trim();
        String divisionString = fragment.getEditTextDivision().getText().toString().trim();
        Area area = (Area) fragment.getAreaSpinner().getSelectedItem();
        InstitutionUser coordinator = (InstitutionUser) fragment.getCoordinatorSpinner().getSelectedItem();
        if (!name.equals("") && !description.equals("") && !durationString.equals("") && !divisionString.equals("") && area != null && coordinator != null) {
            try {
                saveCourse(name, description, durationString, divisionString, area, coordinator, institutionID);
            } catch (IllegalArgumentException e) {
                snackbarText.setValue(e.getMessage());
            }
        } else {
            snackbarText.setValue("Todos os campos são obrigatórios para o cadastro!");
        }

    }

    private void saveCourse(String name, String description, String durationString, String divisionString, Area area, InstitutionUser coordinator, String institutionID) throws IllegalArgumentException {
        Course course = buildCourse(name, description, durationString, divisionString, area, coordinator, institutionID);
        CourseDAO courseDAO = new CourseDAO();
        courseDAO.findCourseByName(institutionID, name, task2 -> {
            if (task2.isComplete() && !task2.getResult().isEmpty()) {
                snackbarText.setValue("Já existe um curso cadastrado com esse nome, verifique...");
            } else {
                courseDAO.saveNewCourse(course, institutionID, task -> {
                    updateCourseID(institutionID, courseDAO, task);
                    clearViewFields();
                    snackbarText.setValue("Curso criado com sucesso!");
                }, e -> snackbarText.setValue("Erro ao criar o seu curso... Tente novamente mais tarde"));
            }
        }, e2 -> snackbarText.setValue("Erro ao criar o seu curso... Tente novamente mais tarde"));
    }

    private Course buildCourse(String name, String description, String durationString, String divisionString, Area area, InstitutionUser coordinator, String institutionID) throws IllegalArgumentException {
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        int divisionInteger = Integer.parseInt(divisionString);
        if (divisionInteger > 4 || divisionInteger <= 1) {
            throw new IllegalArgumentException("A divisão só é permitida em bimestres(4), trimestres(3) ou semestres (2)");
        }
        Course course = new Course();
        course.setName(name);
        course.setDescription(description);
        course.setDuration(Integer.parseInt(durationString));
        course.setDivision_of_the_school_year(Integer.parseInt(divisionString));
        DocumentReference areaReference = fb.collection(Institution.class.getSimpleName()).document(institutionID).collection(Area.class.getSimpleName()).document(area.getId());
        DocumentReference coordinatorReference = fb.collection(Institution.class.getSimpleName()).document(institutionID).collection(InstitutionUser.class.getSimpleName()).document(coordinator.getId());
        course.setArea_id(areaReference);
        course.setCoordinantor_id(coordinatorReference);
        return course;
    }

    private void updateCourseID(String institutionID, CourseDAO courseDAO, Task<DocumentReference> task) {
        HashMap<String, Object> updates = new HashMap<>();
        updates.put("id", task.getResult().getId());
        courseDAO.updateCourse(task.getResult().getId(), institutionID, updates);
    }

    private void clearViewFields() {
        fragment.getEditTextname().setText("");
        fragment.getEditTextDescription().setText("");
        fragment.getEditTextDivision().setText("");
        fragment.getEditTextDuration().setText("");
    }
}
