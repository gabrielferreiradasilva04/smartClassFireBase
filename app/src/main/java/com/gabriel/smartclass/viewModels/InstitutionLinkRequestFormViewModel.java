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
import com.gabriel.smartclass.adapter.spinnerAdapters.SpinnerAdapterGeneric;
import com.gabriel.smartclass.dao.InstitutionLinkRequestDAO;
import com.gabriel.smartclass.dao.LinkRequestStatusDAO;
import com.gabriel.smartclass.dao.UserDAO;
import com.gabriel.smartclass.dao.UserTypeDAO;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.InstitutionLinkRequest;
import com.gabriel.smartclass.model.User;
import com.gabriel.smartclass.model.UserType;
import com.gabriel.smartclass.view.user.views.commonUser.CommonUserMainMenu;
import com.gabriel.smartclass.view.linkRequests.InstitutionLinkRequestForm;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class InstitutionLinkRequestFormViewModel {
    private InstitutionLinkRequestForm institutionLinkRequestForm;
    private SpinnerAdapterGeneric<UserType> userTypeAdapter;
    private MutableLiveData<List<UserType>> userTypeList;
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
        this.userTypeList = new MutableLiveData<>();
        Spinner userTypeSpinner = institutionLinkRequestForm.findViewById(R.id.spinner_user_type_link);
        userTypeAdapter = new SpinnerAdapterGeneric<UserType>(this.institutionLinkRequestForm.getApplicationContext(), this.userTypeList);
        userTypeSpinner.setAdapter(userTypeAdapter);
        userTypeDAO.getAllUserTypes(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot documentSnapshot: task.getResult()){
                    UserType userType = documentSnapshot.toObject(UserType.class);
                    userType.setUuid(documentSnapshot.getId());
                    userTypeAdapter.getMutableLiveDataTList().getValue().add(userType);
                    userTypeAdapter.notifyDataSetChanged();
                }
            }
        }, e -> snackbarText.setValue("Erro ao carregar tipos de usuário!"));
    }

    public void createNewInstitutionLinkRequest(UserType userType, String title, Institution institution,Context context){
        if(userType == null || title.isEmpty() || institution == null){
            snackbarText.setValue("Preencha todos os campos");
        }else{
            FirebaseFirestore.getInstance().collection(Institution.class.getSimpleName()).document(institution.getId()).get().addOnCompleteListener(task -> {
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
                                            createNewInstitutionLinkRequest(institution, userType, title, context);
                                        }else{
                                            snackbarText.setValue("Você já esta vinculado a esta instituição, desfaça o vinculo e tente novamente");
                                        }

                                    }
                                });
                            }
                        }else{
                            createNewInstitutionLinkRequest(institution, userType, title, context);
                        }
                    }
                });
            });
        }

    }

    private void createNewInstitutionLinkRequest(Institution institution, UserType userType, String title, Context context) {
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        DocumentReference userReference = fb.collection(User.class.getSimpleName()).document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        DocumentReference institutionReference = fb.collection(Institution.class.getSimpleName()).document(institution.getId());
        DocumentReference userTypeReference = fb.collection(UserType.class.getSimpleName()).document(userType.getUuid());
        InstitutionLinkRequest institutionLinkRequest = new InstitutionLinkRequest();
        institutionLinkRequest.setLinkRequestStatus_id(LinkRequestStatusDAO.PENDING_REFERENCE);
        institutionLinkRequest.setUser(userReference);
        institutionLinkRequest.setTitle(title);
        institutionLinkRequest.setUserType(userTypeReference);
        institutionLinkRequest.setInstitution_id(institutionReference);
        linkRequestDAO.createNewLinkRequest(institutionLinkRequest, institutionReference, task3 -> {
            if(task3.isSuccessful()){
                updateLinkRequestId(institution, context, task3);
            }
        }, e -> {
            snackbarText.setValue("Algo deu errado: "+e);
        });
    }

    private void updateLinkRequestId(Institution institution, Context context, Task<DocumentReference> task3) {
        HashMap<String, Object> update = new HashMap<>();
        update.put("id", task3.getResult().getId());
        linkRequestDAO.updateInstitutionLinkRequest(task3.getResult().getId(), institution.getId(), update, task4->{
            showSuccessDialog(context);
        },e4->{});
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
            Intent i = new Intent(context, CommonUserMainMenu.class);
            context.startActivity(i);
            institutionLinkRequestForm.finish();
        });
    }


}
