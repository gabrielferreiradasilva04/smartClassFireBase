package com.gabriel.smartclass.viewModels;

import android.content.Context;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.gabriel.smartclass.adapter.SpinnerUserTypeAdapter;
import com.gabriel.smartclass.dao.UserTypeDAO;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.UserType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class InstitutionLinkRequestViewModel {
    private SpinnerUserTypeAdapter userTypeAdapter;
    private List<UserType> userTypeList;
    private UserTypeDAO userTypeDAO;

    public InstitutionLinkRequestViewModel(){
        userTypeDAO = new UserTypeDAO();
    }
    public void getUserTypesAndPopulateSpinner(Spinner userTypeSpinner, Context context){
        this.userTypeList = new ArrayList<>();
        userTypeAdapter = new SpinnerUserTypeAdapter(context, this.userTypeList);
        userTypeSpinner.setAdapter(userTypeAdapter);
        userTypeDAO.getAllUserTypes(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot documentSnapshot: task.getResult()){
                    UserType userType = documentSnapshot.toObject(UserType.class);
                    userTypeAdapter.getMutableLiveDataUserType().getValue().add(userType);
                    userTypeAdapter.notifyDataSetChanged();
                }

            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void createNewInstitutionLinkRequest(UserType userType, String title, Institution institution){
        /*Preciso determinar uma instituição -> documentReference -> Posso passar como parametro da tela de pesquisa para a tela de requisição via intent*/
        /*o userType eu consigo pegar da combobox e transformar em um document reference*/
        /*Preciso determinar um appUser -> documentReference também -> Buscar com base no id do usuário logado "fireBaseAtuh.getcurrentUser()"*/

    }

}
