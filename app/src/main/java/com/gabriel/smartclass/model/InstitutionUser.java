package com.gabriel.smartclass.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.gabriel.smartclass.model.baseEntitys.SimpleAuxEntity;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.Map;
import java.util.Objects;

public class InstitutionUser extends SimpleAuxEntity implements Parcelable{
    private String id;
    private String description;
    private DocumentReference  userType_id;
    private DocumentReference user_id;
    private Map<String, Object> identification;
    private boolean active;

    protected InstitutionUser(Parcel in) {
        id = in.readString();
        description = in.readString();
        active = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(description);
        dest.writeByte((byte) (active ? 1 : 0));
        dest.writeString(userType_id.getPath());
        dest.writeString(user_id.getPath());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<InstitutionUser> CREATOR = new Creator<InstitutionUser>() {
        @Override
        public InstitutionUser createFromParcel(Parcel in) {
            String id = in.readString();
            String description = in.readString();
            byte byteBoolean = in.readByte();
            boolean active = byteBoolean == 1;
            String userTypeReferenceString  = in.readString();
            String userReferenceString = in.readString();
            DocumentReference userType_id = null;
            DocumentReference user_id = null;
            if(userTypeReferenceString != null && !userTypeReferenceString.equals("")){ userType_id = FirebaseFirestore.getInstance().document(userTypeReferenceString);}
            if(userReferenceString != null && !userReferenceString.equals("")){ user_id = FirebaseFirestore.getInstance().document(userReferenceString);}
            return new InstitutionUser(id,userType_id, user_id, active, description);
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

    public DocumentReference getUser_id() {
        return user_id;
    }

    public void setUser_id(DocumentReference user_id) {
        this.user_id = user_id;
    }

    public void setUserType_id(DocumentReference userType_id) {
        this.userType_id = userType_id;
    }

    public Map<String, Object> getIdentification() {
        return identification;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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



    public InstitutionUser(DocumentReference userType_id, Map<String, Object> identification) {
        this.userType_id = userType_id;
        this.identification = identification;
    }

    public InstitutionUser(String id, DocumentReference userType_id, DocumentReference user_id, boolean active, String description) {
        this.id = id;
        this.userType_id = userType_id;
        this.user_id = user_id;
        this.active = active;
        this.description = description;
    }

    public InstitutionUser() {
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
