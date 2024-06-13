package com.gabriel.smartclass.viewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.gabriel.smartclass.adapter.InstitutionsAdapter;
import com.gabriel.smartclass.dao.InstitutionDAO;
import com.gabriel.smartclass.model.Institution;
import com.google.android.gms.tasks.OnFailureListener;

public class InstitutionsSearchViewModel extends ViewModel {

    private InstitutionsAdapter adapter = new InstitutionsAdapter();
    /*getters and setters*/
    public InstitutionsAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(InstitutionsAdapter adapter) {
        this.adapter = adapter;
    }

    /*constructors*/

    public void search(String institutionName){
        new InstitutionDAO().getInstitutionByName(institutionName, task -> {
            if (task.isSuccessful()){
                adapter.getInstitutionsMutableLiveData().setValue(task.getResult().toObjects(Institution.class));
                adapter.notifyDataSetChanged();
            }else{
                adapter.getInstitutionsMutableLiveData().getValue().clear();
                adapter.notifyDataSetChanged();
            }
        }, e -> {});


    }


}
