package com.gabriel.smartclass.viewModels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gabriel.smartclass.DAO.InstitutionDAO;
import com.gabriel.smartclass.model.Institution;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragmentViewModel extends ViewModel {
    private InstitutionDAO institutionDAO;
    private MutableLiveData<List<Institution>> institutionsLiveData;

    public HomeFragmentViewModel(){
        institutionDAO = new InstitutionDAO();
        this.institutionsLiveData = new MutableLiveData<>();
    }
    public void getAllInstitutions(){
        institutionDAO.listAllInstitutions(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Institution> institutions = new ArrayList<>();
                for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots.getDocuments()) {
                    institutions.add(documentSnapshot.toObject(Institution.class));
                    Log.d("FUNCIONOU!!!!!!", "onSuccess: "+ institutions);
                }
                institutionsLiveData.setValue(institutions);
            }
        });
    }
}
