package com.gabriel.smartclass.viewModels;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.gabriel.smartclass.R;
import com.gabriel.smartclass.dao.InstitutionUserDAO;
import com.gabriel.smartclass.dao.UserDAO;
import com.gabriel.smartclass.dao.UserTypeDAO;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.InstitutionUser;
import com.gabriel.smartclass.model.User;
import com.gabriel.smartclass.model.UserType;
import com.gabriel.smartclass.view.UserInstitutionMenu;
import com.gabriel.smartclass.view.fragments.institutionusersfragments.InstitutionUserProfile;

import java.util.concurrent.atomic.AtomicInteger;

public class InstitutionUserProfileViewModel extends ViewModel {
    private InstitutionUserProfile view;
    private MutableLiveData<String> snackbarText;

    public InstitutionUserProfileViewModel(InstitutionUserProfile view) {
        this.view = view;
    }

    public InstitutionUserProfileViewModel() {
    }

    public void loadUserDetails(InstitutionUser institutionUser, Institution institution) {
        AtomicInteger aux = new AtomicInteger(0);
        MutableLiveData<User> user = new MutableLiveData<>();
        MutableLiveData<UserType> usertype = new MutableLiveData<>();
        new UserDAO().getUserByDocumentReference(institutionUser.getUser_id(), task -> {
            if (task.isComplete() && task.isSuccessful() && task.getResult().exists()) {
                user.setValue(task.getResult().toObject(User.class));
                aux.getAndIncrement();
                verifyAndLoadData(user.getValue(), usertype.getValue(), institution, aux);
            }
        }, e -> {
            snackbarText.setValue("Erro ao carregar perfil de usuário...");
        });
        new UserTypeDAO().getUserTypeByDocumentReference(institutionUser.getUserType_id(), task2 -> {
            if (task2.isComplete() && task2.isSuccessful() && task2.getResult().exists()) {
                usertype.setValue(task2.getResult().toObject(UserType.class));
                aux.getAndIncrement();
                verifyAndLoadData(user.getValue(), usertype.getValue(), institution, aux);
            }
        }, e2 -> {
            snackbarText.setValue("Erro ao carregar perfil de usuário");
        });
    }

    private void verifyAndLoadData(User user, UserType userType, Institution institution, AtomicInteger aux) {
        if (user != null && userType != null && institution != null && aux.get() == 2) {
            view.getEdtxt_username().setText(user.getName());
            view.getEdtxt_userType().setText(userType.getDescription());
            view.getEdtxt_institutionName().setText(institution.getName());
            try {
                downloadProfilePicture(user);
            } catch (RuntimeException e) {
                snackbarText.setValue("Erro ao carregar foto de perfil: " + e.getLocalizedMessage());
            }
        }
    }

    private void downloadProfilePicture(User user) {
        new UserDAO().downloadImage(user.getEmail(), bytes -> {
            UserInstitutionMenu.institutionUserProfilePicture.setValue(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
        }, e -> {
        });
    }

}
