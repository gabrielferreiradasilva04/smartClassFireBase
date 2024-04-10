package com.gabriel.smartclass.viewModels;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.gabriel.smartclass.adapter.InstitutionsAdapter;
import com.gabriel.smartclass.dao.UserDAO;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.User;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class HostStudentActivityViewModel extends ViewModel {
    private InstitutionsAdapter userInstitutionsAdapter;
    private UserDAO userDAO;
    private Institution selectedInstitution;
    /*User properties*/
    private MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private MutableLiveData<FirebaseUser> firebaseUserLiveData = new MutableLiveData<>();
    private MutableLiveData<Bitmap> profilePictureLiveData = new MutableLiveData<>();
    private MutableLiveData<String> snackBarText = new MutableLiveData<>();
    /*User properties*/

    public MutableLiveData<FirebaseUser> getFirebaseUserLiveData() {
        return firebaseUserLiveData;
    }
    public MutableLiveData<Bitmap> getProfilePictureLiveData() {
        return profilePictureLiveData;
    }
    public void setSelectedInstitution(Institution selectedInstitution) {
        this.selectedInstitution = selectedInstitution;
    }

    public InstitutionsAdapter getUserInstitutionsAdapter() {
        return userInstitutionsAdapter;
    }


    public HostStudentActivityViewModel(){
        userDAO = new UserDAO();
        firebaseUserLiveData.setValue(FirebaseAuth.getInstance().getCurrentUser());
    }

    /***
     * Realiza a busca das instituições do usuário
     *
     */

    public void listenerSnapshotChanges(){
        userDAO.listenerSnapshotChanges((value, error) -> {
            if(error == null){
                User userUpdate = value.toObject(User.class);
                userLiveData.setValue(userUpdate);
            }
        });
    }
    public void getUserInstitutions(){
        List<Institution> institutions = new ArrayList<>();
        userInstitutionsAdapter = new InstitutionsAdapter(institutions);
        userDAO.getUserInstitutions(task -> {
            User user = task.getResult().toObject(User.class);
            assert user != null;
            if(user.getInstitutions() != null){
                for (DocumentReference documentReference: user.getInstitutions()) {
                    documentReference.get().addOnSuccessListener(documentSnapshot -> {
                        Institution institution = documentSnapshot.toObject(Institution.class);
                        institution.setId(documentSnapshot.getId());
                        if(institution != null){
                            institution.setId(documentSnapshot.getId());
                            userInstitutionsAdapter.addItem(institution);
                            userInstitutionsAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }else{
                userInstitutionsAdapter.notifyDataSetChanged();
            }
        }, e -> {
        });
    }

    /***
     * Realiza o recarregamento dos dados do usuário, como nome e foto
     */
    public void loadUserDetails(){
        if(firebaseUserLiveData.getValue().getPhotoUrl() != null && !firebaseUserLiveData.getValue().getPhotoUrl().equals(Uri.parse(""))){
            userDAO.downloadImage(firebaseUserLiveData.getValue().getEmail(), bytes -> {
                profilePictureLiveData.setValue(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }, e -> {

            });
        }
    }

    /***
     * realiza a atualização do perfil do usuário
     * @param displayName novo nome
     * @param email novo email
     * @param profilePictureCurrent nova foto. Se for null ou igual a atual ele não atualiza
     * @param progressBar barra de progresso para realizar o carregamento
     * @param viewLoading view para escurecer o fundo
     */
    public void updateProfile(@NonNull String displayName,@NonNull String email,@NonNull Context context, @NonNull Bitmap profilePictureCurrent, @NonNull ProgressBar progressBar,@NonNull View viewLoading){
        if(displayName.equals(firebaseUserLiveData.getValue().getDisplayName()) && email.equals(this.getFirebaseUserLiveData().getValue().getEmail()) && profilePictureCurrent == profilePictureLiveData.getValue()){
            Toast toast = Toast.makeText(context, "Não detectamos alterações a serem salvas", Toast.LENGTH_SHORT);
            toast.show();
        } else if (!displayName.equals("") && !email.equals("")) {
            if(profilePictureCurrent != this.profilePictureLiveData.getValue() && profilePictureCurrent!= null){
                uploadProfilePicture(email,profilePictureCurrent, progressBar, viewLoading,context);
            }
            userDAO.updateProfile(displayName, o -> {
                Toast toast = Toast.makeText(context, "Alterações salvas", Toast.LENGTH_SHORT);
                toast.show();
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

     */
    public void uploadProfilePicture(String email, Bitmap imageBitmap, ProgressBar progressBar, View view, Context context){
        FirebaseUser currentUserApplication = FirebaseAuth.getInstance().getCurrentUser();
        if (!email.equals("") && imageBitmap != null && imageBitmap != profilePictureLiveData.getValue()){
            view.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            userDAO.uploadProfileImage(email, imageBitmap, taskSnapshot -> {
                UserProfileChangeRequest changes = new UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse("profilePictures\\" + email)).build();
                currentUserApplication.updateProfile(changes);
                profilePictureLiveData.setValue(imageBitmap);
                progressBar.setVisibility(View.INVISIBLE);
                view.setVisibility(View.GONE);
            }, e -> {
                progressBar.setVisibility(View.INVISIBLE);
                view.setVisibility(View.GONE);
                Toast toast = Toast.makeText(context, "Erro ao durante o upload", Toast.LENGTH_SHORT);
                toast.show();
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
    public void updatePassword(String password, String newPassword, @NonNull Context context, @NonNull Dialog dialog){
        if(password.equals(newPassword)){
            userDAO.updatePassword(password, task -> {
                Toast toast = Toast.makeText(context, "Senha atualizada", Toast.LENGTH_SHORT);
                toast.show();
                dialog.dismiss();
            }, e -> {
                String errorMessage = "";
                if(e.getClass().equals(FirebaseAuthWeakPasswordException.class)){
                    errorMessage = "A senha deve conter pelo mesnos 6 caractéres";
                }
                if(e.getClass().equals(FirebaseAuthInvalidCredentialsException.class)){
                    errorMessage = "Digite um e-mail válido";
                }
                if(e.getClass().equals(FirebaseAuthUserCollisionException.class)){
                    errorMessage = "Já existe um usuário cadastrado com esse e-mail";
                }
                if(e.getClass().equals(FirebaseNetworkException.class)){
                    errorMessage = "Sem conexão com a internet, tente novamente mais tarde";
                }
                Toast toast = Toast.makeText(context, errorMessage, Toast.LENGTH_LONG);
                toast.show();
            });
        }else{
            Toast toast = Toast.makeText(context, "Senhas não conferem", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /***
     * exclui a foto de perfil do usuário, tanto do repositório na nuvem como em tempo de execução
     * @param email foto a ser excluida
     */
    public void excludeProfilePicture(String email, Context context, @NonNull ProgressBar progressBar, @NonNull View view){
        if (profilePictureLiveData.getValue()!= null){
            view.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            userDAO.excludeImageStorage(email, o -> {
                Toast toast = Toast.makeText(context, "Foto de perfil excluída!", Toast.LENGTH_SHORT);
                toast.show();
                profilePictureLiveData.setValue(null);
                progressBar.setVisibility(View.INVISIBLE);
                view.setVisibility(View.GONE);
            }, e -> {
            });
            userDAO.excludeProfilePicture(task -> {
            }, e -> {

            })  ;
        }
    }



}
