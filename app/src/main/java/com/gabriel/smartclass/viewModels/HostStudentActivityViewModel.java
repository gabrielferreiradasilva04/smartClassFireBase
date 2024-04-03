package com.gabriel.smartclass.viewModels;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import com.gabriel.smartclass.adapter.InstitutionsAdapter;
import com.gabriel.smartclass.dao.UserDAO;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class HostStudentActivityViewModel extends ViewModel {
    private InstitutionsAdapter userInstitutionsAdapter;
    private UserDAO userDAO;
    private Institution selectedInstitution;
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
        userDAO = new UserDAO();
    }

    /***
     * realiza a busca das instituições do usuário com base na lista presente no mesmo
     * Ao encontrar a lista ele precisa percorrer ela e tranformar os DocumentReferences em objetos instituição
     */
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

    /***
     * Realiza o recarregamento dos dados do usuário, como nome e foto
     * @param context
     */
    public void loadUserDetails(Context context){
        displayName = UserDAO.currentUserAplication.getDisplayName();
        email = UserDAO.currentUserAplication.getEmail();
        if(UserDAO.currentUserAplication.getPhotoUrl() != null && !UserDAO.currentUserAplication.getPhotoUrl().equals(Uri.parse(""))){
            userDAO.downloadImage(UserDAO.currentUserAplication.getEmail(), new OnSuccessListener<byte[]>() {
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

    /***
     * realiza a atualização do perfil do usuário
     * @param displayName novo nome
     * @param email novo email
     * @param context
     * @param profilePictureCurrent nova foto. Se for null ou igual a atual ele não atualiza
     * @param progressBar barra de progresso para realizar o carregamento
     * @param viewLoading view para escurecer o fundo
     */
    public void updateProfile(String displayName, String email, Context context, Bitmap profilePictureCurrent, ProgressBar progressBar, View viewLoading){
        if(displayName.equals(this.displayName) && email.equals(this.email) && profilePictureCurrent == this.pictureProfileBitmap){
            Toast toast = Toast.makeText(context, "Não detectamos alterações a serem salvas", Toast.LENGTH_SHORT);
            toast.show();
        } else if (!displayName.equals("") && !email.equals("")) {
            if(profilePictureCurrent != this.pictureProfileBitmap && profilePictureCurrent!= null){
                uploadProfilePicture(email,profilePictureCurrent, progressBar, viewLoading);
            }
            userDAO.updateProfile(displayName,new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    Toast toast = Toast.makeText(context, "Alterações salvas", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        }
        else{
            Toast toast = Toast.makeText(context, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /***
     * realiza o upload da nova foto do usuário para a nuvem salvando-a como seu e-mail
     * @param email nome da foto
     * @param imageBitmap se for null se o email for "" ele não realiza o upload
     * @param progressBar
     * @param view
     */
    public void uploadProfilePicture(String email, Bitmap imageBitmap, ProgressBar progressBar, View view){
        if (!email.equals("") && imageBitmap != null && imageBitmap != pictureProfileBitmap){
            view.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            userDAO.uploadProfileImage(email, imageBitmap, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    UserProfileChangeRequest changes = new UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse("profilePictures\\"+email)).build();
                    UserDAO.currentUserAplication.updateProfile(changes);
                    pictureProfileBitmap = imageBitmap;
                    progressBar.setVisibility(View.INVISIBLE);
                    view.setVisibility(View.GONE);
                }
            });
        }
    }
    /***
     * atualiza a senha do usuário
     * @param password senha
     * @param newPassword confirmação
     * @param context para exibir as mensagens
     * @param dialog ao final da execução ele fecha o dialog
     */
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

    /***
     * exclui a foto de perfil do usuário, tanto do repositório na nuvem como em tempo de execução
     * @param email foto a ser excluida
     * @param context
     * @param progressBar
     * @param view
     */
    public void excludeProfilePicture(String email, Context context, ProgressBar progressBar, View view){
        if (pictureProfileBitmap!= null){
            view.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            userDAO.excludeImageStorage(email, new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    Toast toast = Toast.makeText(context, "Foto de perfil excluída!", Toast.LENGTH_SHORT);
                    toast.show();
                    setPictureProfileBitmap(null);
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


}
