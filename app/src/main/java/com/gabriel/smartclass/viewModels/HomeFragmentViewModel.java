package com.gabriel.smartclass.viewModels;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gabriel.smartclass.adapter.InstitutionsAdapter;
import com.gabriel.smartclass.dao.AppUserDAO;
import com.gabriel.smartclass.dao.InstitutionDAO;
import com.gabriel.smartclass.model.AppUser;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.observer.EmptyRecyclerViewObserver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragmentViewModel extends ViewModel {
    private AppUserDAO appUserDAO;/*objeto usado para buscar os dados do usuario e suas instituições*/
    private List<Institution> userInstitutions;
    private InstitutionsAdapter userInstitutionsAdapter;
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



    public HomeFragmentViewModel(Context context){
        appUserDAO = new AppUserDAO();
        userInstitutions = new ArrayList<>();
        this.context = context;
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
                    appUser.setId(documentSnapshot.getId());
                    for (DocumentReference documentReference: appUser.getInstitutions()) {
                        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Institution institution = documentSnapshot.toObject(Institution.class);
                                institution.setId(documentSnapshot.getId());
                                userInstitutionsAdapter.addItem(institution);
                                userInstitutionsAdapter.notifyDataSetChanged();
                            }
                        });
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
