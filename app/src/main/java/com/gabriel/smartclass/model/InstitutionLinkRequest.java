package com.gabriel.smartclass.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.DocumentReference;

import java.util.Objects;

public class InstitutionLinkRequest implements Parcelable {
    private String id;
    private String title;
    private DocumentReference user_id;
    private DocumentReference userType_id;
    private DocumentReference institution_id ;
    private Boolean approved;

    protected InstitutionLinkRequest(Parcel in) {
        id = in.readString();
        title = in.readString();
        byte tmpApproved = in.readByte();
        approved = tmpApproved == 0 ? null : tmpApproved == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeByte((byte) (approved == null ? 0 : approved ? 1 : 2));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<InstitutionLinkRequest> CREATOR = new Creator<InstitutionLinkRequest>() {
        @Override
        public InstitutionLinkRequest createFromParcel(Parcel in) {
            return new InstitutionLinkRequest(in);
        }

        @Override
        public InstitutionLinkRequest[] newArray(int size) {
            return new InstitutionLinkRequest[size];
        }
    };

    public DocumentReference getUser_id() {
        return user_id;
    }

    public void setUser_id(DocumentReference user_id) {
        this.user_id = user_id;
    }

    public DocumentReference getUserType_id() {
        return userType_id;
    }

    public void setUserType_id(DocumentReference userType_id) {
        this.userType_id = userType_id;
    }

    public DocumentReference getInstitution_id() {
        return institution_id;
    }

    public void setInstitution_id(DocumentReference institution_id) {
        this.institution_id = institution_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DocumentReference getUser() {
        return user_id;
    }

    public void setUser(DocumentReference user_id) {
        this.user_id = user_id;
    }

    public DocumentReference getUserType() {
        return userType_id;
    }

    public void setUserType(DocumentReference userType) {
        this.userType_id = userType;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public InstitutionLinkRequest(String id, String title, DocumentReference user_id, DocumentReference userType_id, Boolean approved) {
        this.id = id;
        this.title = title;
        this.user_id = user_id;
        this.userType_id = userType_id;
        this.approved = approved;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstitutionLinkRequest that = (InstitutionLinkRequest) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(user_id, that.user_id) && Objects.equals(userType_id, that.userType_id) && Objects.equals(institution_id, that.institution_id) && Objects.equals(approved, that.approved);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, user_id, userType_id, institution_id, approved);
    }

    public InstitutionLinkRequest() {
    }

    public InstitutionLinkRequest(String title, DocumentReference user_id, DocumentReference userType_id, Boolean approved) {
        this.title = title;
        this.user_id = user_id;
        this.userType_id = userType_id;
        this.approved = approved;
    }

    /*PARCELABLE IMPLEMENTATION*/


}
