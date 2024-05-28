package com.gabriel.smartclass.viewModels;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gabriel.smartclass.adapter.SpinnerAdapterGeneric;
import com.gabriel.smartclass.dao.AreaDAO;
import com.gabriel.smartclass.model.Area;
import com.gabriel.smartclass.view.course.fragments.AddCourse;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.List;

public class AddCourseViewModel extends ViewModel {
    private AddCourse fragment;
    private MutableLiveData<String> snackbarText = new MutableLiveData<>();
    private MutableLiveData<List<Area>> areasMutableLiveData = new MutableLiveData<>();
    private SpinnerAdapterGeneric<Area> spinnerAreaAdapter;

    public AddCourseViewModel(AddCourse addCourse) {
        this.fragment = addCourse;
    }

    public MutableLiveData<List<Area>> getAreasMutableLiveData() {
        return areasMutableLiveData;
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
                    }, e -> {
                        Toast.makeText(fragment.getContext(), "Algo deu errado, tente novamente...", Toast.LENGTH_SHORT).show();
                    });
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

    public void getAllAreasAndpopulateSpinner(String institutionID){
        AreaDAO areaDAO = new AreaDAO();
        spinnerAreaAdapter = new SpinnerAdapterGeneric<>(fragment.getContext(), this.areasMutableLiveData);
        fragment.getAreaSpinner().setAdapter(spinnerAreaAdapter);
        areaDAO.getAllAreas(institutionID, task->{
            if(task.isComplete() && !task.getResult().isEmpty()){
                spinnerAreaAdapter.getMutableLiveDataTList().setValue(task.getResult().toObjects(Area.class));
                spinnerAreaAdapter.notifyDataSetChanged();
            }
        });
    }
    public void addNewItemOnAreaAdapter(Area area){
        spinnerAreaAdapter.getMutableLiveDataTList().getValue().add(area);
        spinnerAreaAdapter.notifyDataSetChanged();
    }
    public void removeItemFromAreaAdapter(Area area){
        spinnerAreaAdapter.getMutableLiveDataTList().getValue().remove(area);
        spinnerAreaAdapter.notifyDataSetChanged();
    }



}
