package com.gabriel.smartclass.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.DocumentReference;

import java.util.Map;
import java.util.Objects;

public class InstitutionUser  implements Parcelable {
    private String id;
    private DocumentReference  userType_id;
    private Map<String, Object> identification;
    private boolean active;


    protected InstitutionUser(Parcel in) {
        id = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<InstitutionUser> CREATOR = new Creator<InstitutionUser>() {
        @Override
        public InstitutionUser createFromParcel(Parcel in) {
            return new InstitutionUser(in);
        }

        @Override
        public InstitutionUser[] newArray(int size) {
            return new InstitutionUser[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public DocumentReference getUserType_id() {
        return userType_id;
    }

    public void setUserType_id(DocumentReference userType_id) {
        this.userType_id = userType_id;
    }

    public Map<String, Object> getIdentification() {
        return identification;
    }

    public void setIdentification(Map<String, Object> identification) {
        this.identification = identification;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public InstitutionUser() {
    }


    public InstitutionUser(DocumentReference userType_id, Map<String, Object> identification) {
        this.userType_id = userType_id;
        this.identification = identification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstitutionUser that = (InstitutionUser) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }




}
