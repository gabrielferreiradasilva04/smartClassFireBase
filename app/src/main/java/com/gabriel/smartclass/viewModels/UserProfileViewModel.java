package com.gabriel.smartclass.viewModels;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import com.gabriel.smartclass.dao.UserDAO;
import com.gabriel.smartclass.dao.UserProfilePictureDAO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.UploadTask;

public class UserProfileViewModel extends ViewModel {
    private UserProfilePictureDAO userProfilePictureDAO;
    private UserDAO userDAO;

    public UserProfileViewModel(){
        userProfilePictureDAO = new UserProfilePictureDAO();
        userDAO = new UserDAO();
    }

    public void uploadProfilePicture(String email, Bitmap imageBitmap, Context context, ProgressBar progressBar, View view){
        if (!email.equals("") && imageBitmap != null){
            view.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            userProfilePictureDAO.uploadProfileImage(email, imageBitmap, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    UserProfileChangeRequest changes = new UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse("profilePictures\\"+email)).build();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    user.updateProfile(changes);
                    progressBar.setVisibility(View.INVISIBLE);
                    view.setVisibility(View.GONE);
                }
            });
        }else{
            Toast toast = Toast.makeText(context, "Verifique se os dados estão corretos!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void updatePassword(String password, String newPassword, Context context, Dialog dialog){
        if(password.equals(newPassword)){
            userDAO.updatePassword(password, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast toast = Toast.makeText(context, "Senha atualizada", Toast.LENGTH_SHORT);
                    toast.show();
                    dialog.dismiss();
                }
            }, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast toast = Toast.makeText(context, "Erro ao atualziar senha", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        }else{
            Toast toast = Toast.makeText(context, "Senhas não conferem", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    public void excludeProfilePicture(String email, Context context, ProgressBar progressBar, View view){
        view.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        userProfilePictureDAO.excludeImageStorage(email, new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast toast = Toast.makeText(context, "Foto de perfil excluída!", Toast.LENGTH_SHORT);
                toast.show();
                progressBar.setVisibility(View.INVISIBLE);
                view.setVisibility(View.GONE);
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        userDAO.excludeProfilePicture(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        })  ;
    }


}
