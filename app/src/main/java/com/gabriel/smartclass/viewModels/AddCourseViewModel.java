package com.gabriel.smartclass.viewModels;

import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gabriel.smartclass.dao.AreaDAO;
import com.gabriel.smartclass.model.Area;
import com.gabriel.smartclass.view.course.fragments.AddCourse;

import java.util.HashMap;
import java.util.List;

public class AddCourseViewModel extends ViewModel {
    private Fragment fragment;
    private MutableLiveData<String> snackbarText = new MutableLiveData<>();
    private MutableLiveData<List<Area>> areasMutableLiveData = new MutableLiveData<>();

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
                        String id = task.getResult().getId();
                        HashMap<String, Object> updates = new HashMap<>();
                        updates.put("id", id);
                        areaDAO.updateArea(id, institutionID, updates);
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
    public void getAllAreas(String institutionID){
        AreaDAO areaDAO = new AreaDAO();
        areaDAO.getAllAreas(institutionID, task->{
            if(task.isComplete() && !task.getResult().isEmpty()){
                areasMutableLiveData.setValue(task.getResult().toObjects(Area.class));
            }
        });

    }

}
