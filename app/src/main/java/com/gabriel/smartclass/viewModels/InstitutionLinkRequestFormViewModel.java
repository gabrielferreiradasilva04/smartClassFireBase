package com.gabriel.smartclass.viewModels;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.adapter.SpinnerUserTypeAdapter;
import com.gabriel.smartclass.dao.InstitutionLinkRequestDAO;
import com.gabriel.smartclass.dao.UserDAO;
import com.gabriel.smartclass.dao.UserTypeDAO;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.InstitutionLinkRequest;
import com.gabriel.smartclass.model.User;
import com.gabriel.smartclass.model.UserType;
import com.gabriel.smartclass.view.StudentMainMenu;
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
import java.util.concurrent.atomic.AtomicInteger;

public class InstitutionLinkRequestFormViewModel {
    private InstitutionLinkRequestForm institutionLinkRequestForm;
    private SpinnerUserTypeAdapter userTypeAdapter;
    private List<UserType> userTypeList;
    private UserTypeDAO userTypeDAO;
    private UserDAO userDAO;
    private InstitutionLinkRequestDAO linkRequestDAO;
    private MutableLiveData<String> snackbarText = new MutableLiveData<>();

    public MutableLiveData<String> getSnackbarText() {
        return snackbarText;
    }

    public InstitutionLinkRequestFormViewModel(InstitutionLinkRequestForm institutionLinkRequestForm){
        userTypeDAO = new UserTypeDAO();
        this.institutionLinkRequestForm = institutionLinkRequestForm;
        this.linkRequestDAO = new InstitutionLinkRequestDAO(this);
        userDAO = new UserDAO();
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

    public void createNewInstitutionLinkRequest(UserType userType, String title, Institution institution,Context context){
        if(userType == null || title.isEmpty() || institution == null){
            snackbarText.setValue("Preencha todos os campos");
        }else{
            FirebaseFirestore.getInstance().collection("Institutions").document(institution.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    Institution institutionFind = task.getResult().toObject(Institution.class);

                    ArrayList<Institution> userInstitutions = new ArrayList<>();
                    AtomicInteger counter = new AtomicInteger();
                    userDAO.getUserByUserAuthId(task2 -> {
                        User user = task2.getResult().toObject(User.class);
                        if(user!=null){
                            if(user.getInstitutions() != null && !user.getInstitutions().isEmpty()) {
                                for(DocumentReference doc : user.getInstitutions()){
                                    doc.addSnapshotListener((value, error) -> {
                                        if(error == null && value!= null){
                                            Institution userInstitution = value.toObject(Institution.class);
                                            userInstitutions.add(userInstitution);
                                            counter.getAndIncrement();
                                        }
                                        if(counter.get() == user.getInstitutions().size()){
                                            if(!userInstitutions.contains(institutionFind)){
                                                FirebaseFirestore fb = FirebaseFirestore.getInstance();
                                                DocumentReference userReference = fb.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                DocumentReference institutionReference = fb.collection("Institutions").document(institution.getId());
                                                DocumentReference userTypeReference = fb.collection("userTypes").document(userType.getUuid());
                                                InstitutionLinkRequest institutionLinkRequest = new InstitutionLinkRequest();
                                                institutionLinkRequest.setApproved(false);
                                                institutionLinkRequest.setUser(userReference);
                                                institutionLinkRequest.setTitle(title);
                                                institutionLinkRequest.setUserType(userTypeReference);
                                                institutionLinkRequest.setInstitution_id(institutionReference);
                                                linkRequestDAO.createNewLinkRequest(institutionLinkRequest, institutionReference, task3 -> {
                                                    if(task3.isSuccessful()){
                                                        showSuccessDialog(context);
                                                    }
                                                }, e -> {
                                                    snackbarText.setValue("Algo deu errado: "+e);
                                                });
                                            }else{
                                                snackbarText.setValue("Você já esta vinculado a esta instituição, desfaça o vinculo e tente novamente");
                                            }

                                        }
                                    });
                                }
                            }else{
                                FirebaseFirestore fb = FirebaseFirestore.getInstance();
                                DocumentReference userReference = fb.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                DocumentReference institutionReference = fb.collection("Institutions").document(institution.getId());
                                DocumentReference userTypeReference = fb.collection("userTypes").document(userType.getUuid());
                                InstitutionLinkRequest institutionLinkRequest = new InstitutionLinkRequest();
                                institutionLinkRequest.setApproved(false);
                                institutionLinkRequest.setUser(userReference);
                                institutionLinkRequest.setTitle(title);
                                institutionLinkRequest.setUserType(userTypeReference);
                                institutionLinkRequest.setInstitution_id(institutionReference);
                                linkRequestDAO.createNewLinkRequest(institutionLinkRequest, institutionReference, task4 -> {
                                    if (task4.isSuccessful()) {
                                        showSuccessDialog(context);
                                    }
                                }, e -> {
                                    snackbarText.setValue("Algo deu errado: " + e);
                                });
                            }
                        }
                    });
                }
            });
        }


    }

    public void showSuccessDialog(Context context){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setCancelable(false);
        final View popUp = institutionLinkRequestForm.getLayoutInflater().inflate(R.layout.success_link_request_layout,null);
        dialogBuilder.setView(popUp);
        Dialog dialog = dialogBuilder.create();
        dialog.show();
        Button buttonConfirm = popUp.findViewById(R.id.buttonConfirmLinkRequestSuccessDialog);
        buttonConfirm.setOnClickListener(veiw -> {
            dialog.dismiss();
            Intent i = new Intent(context, StudentMainMenu.class);
            context.startActivity(i);
        });

    }


}
