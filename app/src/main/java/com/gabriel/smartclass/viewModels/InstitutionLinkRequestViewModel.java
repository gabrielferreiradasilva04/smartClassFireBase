package com.gabriel.smartclass.viewModels;

import android.content.Context;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.adapter.SpinnerUserTypeAdapter;
import com.gabriel.smartclass.dao.InstitutionLinkRequestDAO;
import com.gabriel.smartclass.dao.UserTypeDAO;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.InstitutionLinkRequest;
import com.gabriel.smartclass.model.UserType;
import com.gabriel.smartclass.view.linkRequests.InstitutionLinkRequestForm;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class InstitutionLinkRequestViewModel {
    private InstitutionLinkRequestForm institutionLinkRequestForm;
    private SpinnerUserTypeAdapter userTypeAdapter;
    private List<UserType> userTypeList;
    private UserTypeDAO userTypeDAO;
    private InstitutionLinkRequestDAO linkRequestDAO;
    private MutableLiveData<String> snackbarText = new MutableLiveData<>();

    public MutableLiveData<String> getSnackbarText() {
        return snackbarText;
    }

    public void setSnackbarText(MutableLiveData<String> snackbarText) {
        this.snackbarText = snackbarText;
    }

    public InstitutionLinkRequestViewModel(InstitutionLinkRequestForm institutionLinkRequestForm){
        userTypeDAO = new UserTypeDAO();
        this.institutionLinkRequestForm = institutionLinkRequestForm;
        this.linkRequestDAO = new InstitutionLinkRequestDAO();
    }
    public void getUserTypesAndPopulateSpinner(){
        this.userTypeList = new ArrayList<>();
        Spinner userTypeSpinner = institutionLinkRequestForm.findViewById(R.id.spinner_user_type_link);
        userTypeAdapter = new SpinnerUserTypeAdapter(this.institutionLinkRequestForm.getApplicationContext(), this.userTypeList);
        userTypeSpinner.setAdapter(userTypeAdapter);
        userTypeDAO.getAllUserTypes(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot documentSnapshot: task.getResult()){
                    UserType userType = documentSnapshot.toObject(UserType.class);
                    userType.setUuid(documentSnapshot.getId());
                    userTypeAdapter.getMutableLiveDataUserType().getValue().add(userType);
                    userTypeAdapter.notifyDataSetChanged();
                }
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                snackbarText.setValue("Erro ao carregar tipos de usuário!");
            }
        });
    }

    public void createNewInstitutionLinkRequest(UserType userType, String title, Institution institution){
        if(userType != null && !title.equals("") && institution != null){
            FirebaseFirestore fb = FirebaseFirestore.getInstance();
            DocumentReference userReference = fb.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
            DocumentReference institutionReference = fb.collection("Institutions").document(institution.getId());
            DocumentReference userTypeReference = fb.collection("userTypes").document(userType.getUuid());
            InstitutionLinkRequest institutionLinkRequest = new InstitutionLinkRequest();
            institutionLinkRequest.setApproved(false);
            institutionLinkRequest.setUser(userReference);
            institutionLinkRequest.setTitle(title);
            institutionLinkRequest.setUserType(userTypeReference);

            linkRequestDAO.createNewLinkRequest(institutionLinkRequest, institutionReference, task -> {
                if(task.isSuccessful()){
                    snackbarText.setValue("Sua solicitação voi enviada, aguarde o retorno da instituição");
                }
            }, e -> {
                snackbarText.setValue("Algo deu errado: "+e);
            });

        }else{
            snackbarText.setValue("Preencha todos os campos da solicitação");
        }

        /*Preciso determinar uma instituição -> documentReference -> Posso passar como parametro da tela de pesquisa para a tela de requisição via intent*/
        /*o userType eu consigo pegar da combobox e transformar em um document reference*/
        /*Preciso determinar um appUser -> documentReference também -> Buscar com base no id do usuário logado "fireBaseAtuh.getcurrentUser()"*/

    }

}
