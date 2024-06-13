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
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gabriel.smartclass.adapter.InstitutionLinkRequestsAdapter;
import com.gabriel.smartclass.adapter.InstitutionsAdapter;
import com.gabriel.smartclass.dao.InstitutionDAO;
import com.gabriel.smartclass.dao.InstitutionLinkRequestDAO;
import com.gabriel.smartclass.dao.InstitutionUserDAO;
import com.gabriel.smartclass.dao.LinkRequestStatusDAO;
import com.gabriel.smartclass.dao.UserDAO;
import com.gabriel.smartclass.dao.UserTypeDAO;
import com.gabriel.smartclass.model.Course;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.InstitutionLinkRequest;
import com.gabriel.smartclass.model.InstitutionUser;
import com.gabriel.smartclass.model.User;
import com.gabriel.smartclass.view.BaseNotification;
import com.gabriel.smartclass.view.user.fragments.institution.InstitutionLinkRequestFragment;
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
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
public class HostUserActivityViewModel extends ViewModel {
    private final MutableLiveData<String> snackBarText = new MutableLiveData<>();
    /*User variables*/
    private final UserDAO userDAO;
    private final UserTypeDAO userTypeDAO;
    private final MutableLiveData<Bitmap> profilePictureLiveData = new MutableLiveData<>();
    /*Institution variables*/
    private final InstitutionDAO institutionDAO;
    private final MutableLiveData<Institution> institutionMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<HashMap<String, Integer>> institutionStatisticsLiveData = new MutableLiveData<>();
    private final String teachers = "teachers";
    private final String students = "students";
    private final String coordinators = "coordinators";
    private final String courses = "courses";
    private InstitutionsAdapter userInstitutionsAdapter;
//  institution User viriables
    private final MutableLiveData<InstitutionUser> institutionUserMutableLiveData = new MutableLiveData<>();
    // institution link request variables
    private InstitutionLinkRequestsAdapter institutionLinkRequestsAdapter = new InstitutionLinkRequestsAdapter();


    public MutableLiveData<InstitutionUser> getInstitutionUserMutableLiveData() {
        return institutionUserMutableLiveData;
    }

    public MutableLiveData<HashMap<String, Integer>> getInstitutionStatisticsLiveData() {
        return institutionStatisticsLiveData;
    }

    public MutableLiveData<Institution> getInstitutionMutableLiveData() {
        return institutionMutableLiveData;
    }

    public MutableLiveData<String> getSnackBarText() {
        return snackBarText;
    }

    public MutableLiveData<Bitmap> getProfilePictureLiveData() {
        return profilePictureLiveData;
    }

    public InstitutionsAdapter getUserInstitutionsAdapter() {
        return userInstitutionsAdapter;
    }


    public HostUserActivityViewModel() {
        userDAO = new UserDAO();
        institutionDAO = new InstitutionDAO();
        userTypeDAO = new UserTypeDAO();
    }

    //metodos dos usuários comuns
    @SuppressLint("NotifyDataSetChanged")
    public void getUserInstitutions() {
        List<Institution> institutions = new ArrayList<>();
        userInstitutionsAdapter = new InstitutionsAdapter(institutions);
        userDAO.getUserInstitutions(task -> {
            User user = task.getResult().toObject(User.class);
            assert user != null;
            if (user.getInstitutions() != null) {
                for (DocumentReference documentReference : user.getInstitutions()) {
                    documentReference.get().addOnSuccessListener(documentSnapshot -> {
                        Institution institution = documentSnapshot.toObject(Institution.class);
                        if (institution != null) {
                            institution.setId(documentSnapshot.getId());
                            institution.setId(documentSnapshot.getId());
                            userInstitutionsAdapter.addItem(institution);
                            userInstitutionsAdapter.notifyDataSetChanged();
                        }
                    });
                }
            } else {
                userInstitutionsAdapter.notifyDataSetChanged();
            }
        }, e -> {
        });
    }

    public void getInstitutionUserById(String userID, String institutionID) {
        new InstitutionUserDAO().getInstitutionUserById(userID, institutionID, task -> {
          if(task.isSuccessful() && task.getResult().exists()){
              this.institutionUserMutableLiveData.setValue(task.getResult().toObject(InstitutionUser.class));
          }else{
              snackBarText.setValue("Parece que algo deu errado... Tente novamente mais tarde.");
          }
        }, e -> snackBarText.setValue("Parece que algo deu errado... Tente novamente mais tarde."));
    }

    public void loadUserPicture() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            if (currentUser.getPhotoUrl() != null && !currentUser.getPhotoUrl().equals(Uri.parse(""))) {
                userDAO.downloadImage(currentUser.getEmail(), bytes -> profilePictureLiveData.setValue(BitmapFactory.decodeByteArray(bytes, 0, bytes.length)), e -> {
                });
            }
        }
    }

    public void updateUserProfile(@NonNull String displayName, @NonNull String email, @NonNull Bitmap profilePictureCurrent, @NonNull ProgressBar progressBar, @NonNull View viewLoading) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            if (!displayName.equals(currentUser.getDisplayName()) || !email.equals(currentUser.getEmail()) || !(profilePictureCurrent == profilePictureLiveData.getValue())) {
                if (!displayName.equals("") || email.equals("")) {
                    if (profilePictureCurrent != this.profilePictureLiveData.getValue()) {
                        uploadUserPhoto(email, profilePictureCurrent, progressBar, viewLoading);
                    }
                    userDAO.updateProfile(displayName, o -> {
                        HashMap<String, Object> updates = new HashMap<>();
                        updates.put("name", displayName);
                        userDAO.updateApplicationUser(updates, task -> snackBarText.setValue("Perfil de usuário atualizado!"), error -> snackBarText.setValue("Erro ao atualizar o perfil de usuário, tente novamente mais tarde"));
                    });
                } else {
                    snackBarText.setValue("Todos os campos são obrigatórios");
                }
            } else {
                snackBarText.setValue("Não detectamos alterações a serem salvas");
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    public void uploadUserPhoto(String email, Bitmap imageBitmap, ProgressBar progressBar, View view) {
        FirebaseUser currentUserApplication = FirebaseAuth.getInstance().getCurrentUser();
        if (!email.equals("") && imageBitmap != null && imageBitmap != profilePictureLiveData.getValue() && currentUserApplication != null) {
            view.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            userDAO.uploadProfileImage(email, imageBitmap, taskSnapshot -> {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                StorageReference storagePictures = storageReference.child("profilePictures");
                storagePictures.child(email).getDownloadUrl().addOnSuccessListener(uri -> {
                    UserProfileChangeRequest changes = new UserProfileChangeRequest.Builder().setPhotoUri(uri).build();
                    currentUserApplication.updateProfile(changes);
                    profilePictureLiveData.setValue(imageBitmap);
                    HashMap<String, Object> userApplicationUpdates = new HashMap<>();
                    userApplicationUpdates.put("photoUrl", uri);
                    userDAO.updateApplicationUser(userApplicationUpdates, unused -> {
                        snackBarText.setValue("Foto de perfil Salva!");
                        progressBar.setVisibility(View.INVISIBLE);
                        view.setVisibility(View.GONE);
                    }, e -> {
                        snackBarText.setValue("Ocorreu um erro ao finalizar o salvamento sua foto de perfil");
                        progressBar.setVisibility(View.INVISIBLE);
                        view.setVisibility(View.GONE);
                    });
                }).addOnFailureListener(e -> {
                    snackBarText.setValue("Ocorreu um erro ao salvar sua foto de perfil");
                    progressBar.setVisibility(View.INVISIBLE);
                    view.setVisibility(View.GONE);
                });
            }, e -> {
                progressBar.setVisibility(View.INVISIBLE);
                view.setVisibility(View.GONE);
                snackBarText.setValue("Erro no upload");
            });
        }
    }

    public void updatePassword(String password, String newPassword, @NonNull Context context, @NonNull Dialog dialog) {
        if (password.equals(newPassword)) {
            userDAO.updatePassword(password, task -> {
                if (task.isComplete() && task.isSuccessful()) {
                    snackBarText.setValue("Senha atualizada com sucesso!");
                    dialog.dismiss();
                }
            }, e -> {
                String errorMessage = "";
                if (e.getClass().equals(FirebaseAuthWeakPasswordException.class)) {
                    errorMessage = "A senha deve conter pelo menos 6 caractéres";
                }
                if (e.getClass().equals(FirebaseAuthInvalidCredentialsException.class)) {
                    errorMessage = "Digite um e-mail válido";
                }
                if (e.getClass().equals(FirebaseAuthUserCollisionException.class)) {
                    errorMessage = "Já existe um usuário cadastrado com esse e-mail";
                }
                if (e.getClass().equals(FirebaseNetworkException.class)) {
                    errorMessage = "Sem conexão com a internet, tente novamente mais tarde";
                }
                Toast toast = Toast.makeText(context, errorMessage, Toast.LENGTH_LONG);
                toast.show();
            });

        } else {

            Toast toast = Toast.makeText(context, "Senhas não conferem", Toast.LENGTH_SHORT);
            toast.show();

        }
    }

    public void excludeUserPhoto(String email, @NonNull ProgressBar progressBar, @NonNull View view) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest removeProfilePicture = new UserProfileChangeRequest.Builder().setPhotoUri(null).build();
        if (currentUser != null) {
            currentUser.updateProfile(removeProfilePicture).addOnSuccessListener(task -> {
                if (profilePictureLiveData.getValue() != null) {

                    view.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    userDAO.excludeImageStorage(email, o -> {
                        HashMap<String, Object> update = new HashMap<>();
                        update.put("photoUrl", Uri.parse(""));
                        userDAO.updateApplicationUser(update, unused -> snackBarText.setValue("Foto de perfil excluida com sucesso!"), e -> snackBarText.setValue("Erro ao deltar foto de perfil!"));
                        profilePictureLiveData.setValue(null);
                        progressBar.setVisibility(View.INVISIBLE);
                        view.setVisibility(View.GONE);

                    }, e -> snackBarText.setValue("Erro ao deletar sua foto de perfil"));
                }

            }).addOnFailureListener(e -> snackBarText.setValue("Erro ao deletar sua foto de perfil, tente novamente mais tarde!"));
        }
    }

//metodos das instituições

    public void uploadInstitutionPhoto(String email, Bitmap imageBitmap, ProgressBar progressBar, View view) {
        FirebaseUser currentUserApplication = FirebaseAuth.getInstance().getCurrentUser();
        if (!email.equals("") && imageBitmap != null && imageBitmap != profilePictureLiveData.getValue() && currentUserApplication != null) {
            view.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            userDAO.uploadProfileImage(email, imageBitmap, taskSnapshot -> {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                StorageReference storagePictures = storageReference.child("profilePictures");
                storagePictures.child(email).getDownloadUrl().addOnSuccessListener(uri -> {
                    UserProfileChangeRequest changes = new UserProfileChangeRequest.Builder().setPhotoUri(uri).build();
                    currentUserApplication.updateProfile(changes);
                    profilePictureLiveData.setValue(imageBitmap);
                    HashMap<String, Object> institutionUpdades = new HashMap<>();
                    institutionUpdades.put("photoUrl", uri);
                    institutionDAO.update(institutionUpdades, currentUserApplication.getUid(), unused -> {
                        snackBarText.setValue("Foto de perfil Salva!");
                        progressBar.setVisibility(View.INVISIBLE);
                        view.setVisibility(View.GONE);
                    }, e -> {
                        snackBarText.setValue("Ocorreu um erro ao salvar sua foto de perfil");
                        progressBar.setVisibility(View.INVISIBLE);
                        view.setVisibility(View.GONE);
                    });

                }).addOnFailureListener(e -> {
                    snackBarText.setValue("Ocorreu um erro ao salvar sua foto de perfil");
                    progressBar.setVisibility(View.INVISIBLE);
                    view.setVisibility(View.GONE);
                });
            }, e -> {
                progressBar.setVisibility(View.INVISIBLE);
                view.setVisibility(View.GONE);
                snackBarText.setValue("Erro no upload");
            });
        }
    }

    public void getInstitutionByCurrentUser() {
        institutionDAO.getInstitutionByCurrentUser(task -> {
            if (task.isComplete()) {
                Institution institution = task.getResult().toObject(Institution.class);
                institutionMutableLiveData.setValue(institution);
            }
        }, e -> snackBarText.setValue("Algo deu errado, tente novamente mais tarde"));
    }

    public void syncInstitutionInRealTime() {
        institutionDAO.syncChangesInRealTime(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(), (value, error) -> {
            if (error != null) {
                return;
            }
            if (value != null && value.exists()) {
                institutionMutableLiveData.setValue(value.toObject(Institution.class));
            }
        });
    }

    public void updateInstitutionProfile(@NonNull String displayName, @NonNull String email, @NonNull Bitmap profilePictureCurrent, @NonNull ProgressBar progressBar, @NonNull View viewLoading, int maxTeachers, int maxStudents, int maxCoordinators, int maxClassrooms) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null && institutionMutableLiveData.getValue() != null) {
            if (!displayName.equals(currentUser.getDisplayName()) || !email.equals(currentUser.getEmail()) || profilePictureCurrent != profilePictureLiveData.getValue() ||
                    maxTeachers != institutionMutableLiveData.getValue().getMaxTeachers() || maxStudents != institutionMutableLiveData.getValue().getMaxStudents()
                    || maxCoordinators != institutionMutableLiveData.getValue().getMaxCoordinators() || maxClassrooms != institutionMutableLiveData.getValue().getMaxClassrooms()) {
                if (profilePictureCurrent != this.profilePictureLiveData.getValue()) {
                    uploadInstitutionPhoto(email, profilePictureCurrent, progressBar, viewLoading);
                }
                userDAO.updateProfile(displayName, o -> {
                    HashMap<String, Object> institutionDocumentUpdate = new HashMap<>();
                    institutionDocumentUpdate.put("maxTeachers", maxTeachers);
                    institutionDocumentUpdate.put("maxStudents", maxStudents);
                    institutionDocumentUpdate.put("maxCoordinators", maxCoordinators);
                    institutionDocumentUpdate.put("maxClassrooms", maxClassrooms);
                    institutionDocumentUpdate.put("name", displayName);
                    institutionDAO.update(institutionDocumentUpdate, currentUser.getUid(), unused -> snackBarText.setValue("Perfil atualizado"), e -> snackBarText.setValue("Erro ao atualizar Perfil"));
                });
            } else {
                snackBarText.setValue("Não detectamos alterações a serem salvas");
            }
        } else {
            snackBarText.setValue("Ops... Algo deu errado, tente novamente mais tarde!");
        }
    }

    public void excludeInstitutionPhoto(String email, @NonNull ProgressBar progressBar, @NonNull View view) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest removeProfilePicture = new UserProfileChangeRequest.Builder().setPhotoUri(null).build();
        if (currentUser != null) {
            currentUser.updateProfile(removeProfilePicture).addOnSuccessListener(task -> {
                if (profilePictureLiveData.getValue() != null) {
                    view.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    userDAO.excludeImageStorage(email, o -> {
                        HashMap<String, Object> update = new HashMap<>();
                        update.put("photoUrl", Uri.parse(""));
                        institutionDAO.update(update, currentUser.getUid(), unused -> snackBarText.setValue("Foto de perfil excluida com sucesso!"), e -> snackBarText.setValue("Erro ao deltar foto de perfil!"));
                        profilePictureLiveData.setValue(null);
                        progressBar.setVisibility(View.INVISIBLE);
                        view.setVisibility(View.GONE);

                    }, e -> snackBarText.setValue("Erro ao deletar sua foto de perfil"));
                }
            }).addOnFailureListener(e -> snackBarText.setValue("Erro ao deletar sua foto de perfil, tente novamente mais tarde!"));
        }
    }

    public void getInstitutionStatistics() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            HashMap<String, Integer> statisticsTemp = new HashMap<>();
            institutionDAO.getInstitutionUsers(FirebaseAuth.getInstance().getCurrentUser().getUid(), taskInstitutionUsers -> {
                if (taskInstitutionUsers.isComplete() && taskInstitutionUsers.isSuccessful()) {
                    List<InstitutionUser> institutionUserList = taskInstitutionUsers.getResult().toObjects(InstitutionUser.class);
                    Stream<InstitutionUser> teachers = institutionUserList.stream().filter(institutionUser -> institutionUser.getUserType_id().equals(userTypeDAO.TEACHER_TYPE_REFERENCE));
                    Stream<InstitutionUser> students = institutionUserList.stream().filter(institutionUser -> institutionUser.getUserType_id().equals(userTypeDAO.STUDENT_TYPE_REFERENCE));
                    Stream<InstitutionUser> coordinators = institutionUserList.stream().filter(institutionUser -> institutionUser.getUserType_id().equals(userTypeDAO.COORDINATOR_TYPE_REFERENCE));
                    statisticsTemp.put(this.teachers, teachers.mapToInt(obj -> 1).sum());
                    statisticsTemp.put(this.students, students.mapToInt(obj -> 1).sum());
                    statisticsTemp.put(this.coordinators, coordinators.mapToInt(obj -> 1).sum());

                    institutionDAO.getInstitutionCourses(FirebaseAuth.getInstance().getCurrentUser().getUid(), taskInstitutionCourses -> {
                        if (taskInstitutionCourses.isComplete() && taskInstitutionCourses.isSuccessful()) {
                            List<Course> institutionCourses = taskInstitutionCourses.getResult().toObjects(Course.class);
                            statisticsTemp.put(courses, institutionCourses.size());
                            institutionStatisticsLiveData.setValue(statisticsTemp);
                        }
                    }, e2 -> snackBarText.setValue("Ops... Ocorreu um erro ao buscar algumas informações"));

                }
            }, e -> snackBarText.setValue("Ops... Ocorreu um erro ao buscar algumas informações"));
        }
    }

    public void listenerLinkRequestsPending(@NonNull MutableLiveData<AtomicInteger> notificationsNumber, Context context) {
        new InstitutionLinkRequestDAO().syncNewLinkRequestInRealTime(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(), LinkRequestStatusDAO.PENDING_REFERENCE, (value, error) -> {
            if (error == null) {
                if (value != null && !value.isEmpty()) {
                    List<InstitutionLinkRequest> allInstitutionLinkRequests = value.toObjects(InstitutionLinkRequest.class);
                    Stream<InstitutionLinkRequest> pendingInstitutionLinkRequests = allInstitutionLinkRequests.stream().filter(object -> object.getLinkRequestStatus_id().equals(LinkRequestStatusDAO.PENDING_REFERENCE));
                    int i = (int) pendingInstitutionLinkRequests.count();
                    AtomicInteger aux = new AtomicInteger(i);
                    notificationsNumber.setValue(aux);
                    sendNotificationAboutNewRequest(aux, context);
                }
            }

        });
    }

    private void sendNotificationAboutNewRequest(AtomicInteger i, Context context) {
        if (i.get() > 0 && InstitutionLinkRequestFragment.actualPendingNotifications < i.get()) {
            BaseNotification notification = new BaseNotification();
            notification.trigger("Solicitações", "Você tem novas solicitações, verifique a tela de solicitações", context);
        }
    }
}
