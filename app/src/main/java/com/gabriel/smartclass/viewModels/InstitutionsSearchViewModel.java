package com.gabriel.smartclass.viewModels;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.gabriel.smartclass.adapter.InstitutionsAdapter;
import com.gabriel.smartclass.model.Institution;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.gabriel.smartclass.dao.InstitutionDAO;
import java.util.ArrayList;
import java.util.List;

public class InstitutionsSearchViewModel extends ViewModel {
    private RecyclerView recyclerView;
    private InstitutionsAdapter adapter;
    private List<Institution> institutions;
    private InstitutionDAO dao;
    private Context context;
    private Institution selectedInstitution;

    public InstitutionsAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(InstitutionsAdapter adapter) {
        this.adapter = adapter;
    }

    public InstitutionsSearchViewModel(Context context){
        this.context = context;
        this.dao = new InstitutionDAO();
        this.institutions = new ArrayList<>();

    }
    public void search(RecyclerView recyclerView, String institutionName, InstitutionsAdapter.ItemClickListener itemClickListener){
        adapter = new InstitutionsAdapter(institutions,itemClickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        dao.getInstitutionByName(institutionName, new OnCompleteListener<QuerySnapshot>() {


            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    institutions = new ArrayList<>();
                    for (DocumentSnapshot document: task.getResult()){
                        Institution institution = document.toObject(Institution.class);
                        institution.setId(document.getId());
                        institutions.add(institution);
                        adapter.getInstitutionsMutableLiveData().setValue(institutions);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }


}
