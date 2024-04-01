package com.gabriel.smartclass.viewModels;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import com.gabriel.smartclass.adapter.InstitutionsAdapter;
import com.gabriel.smartclass.dao.UserDAO;
import com.gabriel.smartclass.dao.UserProfilePictureDAO;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HostStudentActivityViewModel extends ViewModel {
    private InstitutionsAdapter userInstitutionsAdapter;
    private UserDAO userDAO;
    private Institution selectedInstitution;
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private UserProfilePictureDAO userProfilePictureDAO;
    /*User properties*/
    private  Bitmap pictureProfileBitmap;
    private String email;
    private String displayName;
    /*User properties*/

    public void setPictureProfileBitmap(Bitmap pictureProfileBitmap) {
        this.pictureProfileBitmap = pictureProfileBitmap;
    }

    public Bitmap getPictureProfileBitmap() {
        return pictureProfileBitmap;
    }
    public String getEmail() {
        return email;
    }
    public String getDisplayName() {
        return displayName;
    }
    public Institution getSelectedInstitution() {
        return selectedInstitution;
    }

    public void setSelectedInstitution(Institution selectedInstitution) {
        this.selectedInstitution = selectedInstitution;
    }

    public InstitutionsAdapter getUserInstitutionsAdapter() {
        return userInstitutionsAdapter;
    }

    public void setUserInstitutionsAdapter(InstitutionsAdapter userInstitutionsAdapter) {
        this.userInstitutionsAdapter = userInstitutionsAdapter;
    }

    public HostStudentActivityViewModel(){
        userProfilePictureDAO = new UserProfilePictureDAO();
        userDAO = new UserDAO();
    }
    public void getUserInstitutions(){
        List<Institution> institutions = new ArrayList<>();
        userInstitutionsAdapter = new InstitutionsAdapter(institutions);
        userDAO.getUserInstitutions(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                User user = task.getResult().toObject(User.class);
                assert user != null;
                if(user.getInstitutions() != null){
                    for (DocumentReference documentReference: user.getInstitutions()) {
                        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Institution institution = documentSnapshot.toObject(Institution.class);
                                if(institution != null){
                                    institution.setId(documentSnapshot.getId());
                                    userInstitutionsAdapter.addItem(institution);
                                    userInstitutionsAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }
                }else{
                    userInstitutionsAdapter.notifyDataSetChanged();
                }
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }
    public void loadUserDetails(Context context){
        displayName = currentUser.getDisplayName();
        email = currentUser.getEmail();
        if(currentUser.getPhotoUrl() != null && !currentUser.getPhotoUrl().equals(Uri.parse(""))){
            userProfilePictureDAO.downloadImage(currentUser.getEmail(), new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    pictureProfileBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                }
            }, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast toast = Toast.makeText(context, "Erro ao carregar sua foto de perfil", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        }
    }
    public void updateProfile(String displayName, String email, Context context){
        if(!displayName.equals("") && !email.equals("")){
            userDAO.updateProfile(displayName,new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    Toast toast = Toast.makeText(context, "Alterações salvas", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        }else{
            Toast toast = Toast.makeText(context, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT);
            toast.show();
        }

    }
}
