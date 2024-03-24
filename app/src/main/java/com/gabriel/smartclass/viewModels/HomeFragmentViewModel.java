package com.gabriel.smartclass.viewModels;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gabriel.smartclass.adapter.InstitutionsAdapter;
import com.gabriel.smartclass.dao.AppUserDAO;
import com.gabriel.smartclass.dao.InstitutionDAO;
import com.gabriel.smartclass.model.AppUser;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.view.RegisterForm;
import com.gabriel.smartclass.view.StudentMainMenu;
import com.gabriel.smartclass.view.UserInstitutionMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragmentViewModel extends ViewModel {
    private InstitutionDAO institutionDAO;
    private AppUserDAO appUserDAO;

    private List<Institution> institutions;
    private List<Institution> userInstitutions;
    private InstitutionsAdapter userInstitutionsAdapter;
    private InstitutionsAdapter institutionsAdapter;
    private Context context;
    private Institution selectedInstitution ;

    public Institution getSelectedInstitution() {
        return selectedInstitution;
    }

    public void setSelectedInstitution(Institution selectedInstitution) {
        this.selectedInstitution = selectedInstitution;
    }

    public InstitutionsAdapter getUserInstitutionsAdapter() {
        return userInstitutionsAdapter;
    }

    public InstitutionsAdapter getInstitutionsAdapter() {
        return institutionsAdapter;
    }


    public HomeFragmentViewModel(Context context){
        institutionDAO = new InstitutionDAO();
        appUserDAO = new AppUserDAO();
        institutions = new ArrayList<>();
        userInstitutions = new ArrayList<>();
        this.context = context;

    }

    public void getAllInstitutions(RecyclerView recyclerView, InstitutionsAdapter.ItemClickListener onInstitutionClick){
        institutionsAdapter = new InstitutionsAdapter(institutions, onInstitutionClick);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(institutionsAdapter);
        institutionDAO.listAllInstitutions(new OnSuccessListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots.getDocuments()) {
                    institutionsAdapter.addItem(documentSnapshot.toObject(Institution.class));
                    institutionsAdapter.notifyDataSetChanged();
                }

            }
        });
    }
    public void getUserInstitutions(RecyclerView recyclerView, InstitutionsAdapter.ItemClickListener onClickUserInstitution){
        userInstitutionsAdapter = new InstitutionsAdapter(userInstitutions, onClickUserInstitution);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(userInstitutionsAdapter);
        appUserDAO.getUserInstitutions(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot documentSnapshot : task.getResult()){
                    AppUser appUser = documentSnapshot.toObject(AppUser.class);
                    if(appUser!=null){
                        for (DocumentReference documentReference: appUser.getInstitutions()) {
                            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @SuppressLint("NotifyDataSetChanged")
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    userInstitutionsAdapter.addItem(documentSnapshot.toObject(Institution.class));
                                    userInstitutionsAdapter.notifyDataSetChanged();
                                }
                            });
                        }
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
