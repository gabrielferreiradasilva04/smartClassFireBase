package com.gabriel.smartclass.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.DocumentReference;

import java.util.Objects;

public class InstitutionLinkRequest implements Parcelable {
    private String id;
    private String title;
    private DocumentReference appUser_id;
    private DocumentReference userType_id;
    private Boolean approved;


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

    public DocumentReference getAppUser() {
        return appUser_id;
    }

    public void setAppUser(DocumentReference appUser) {
        this.appUser_id = appUser_id;
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

    public InstitutionLinkRequest(String id, String title, DocumentReference appUser_id, DocumentReference userType_id, Boolean approved) {
        this.id = id;
        this.title = title;
        this.appUser_id = appUser_id;
        this.userType_id = userType_id;
        this.approved = approved;
    }

    public InstitutionLinkRequest() {
    }

    public InstitutionLinkRequest(String title, DocumentReference appUser_id, DocumentReference userType_id, Boolean approved) {
        this.title = title;
        this.appUser_id = appUser_id;
        this.userType_id = userType_id;
        this.approved = approved;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstitutionLinkRequest that = (InstitutionLinkRequest) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(appUser_id, that.appUser_id) && Objects.equals(userType_id, that.userType_id) && Objects.equals(approved, that.approved);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, appUser_id, userType_id, approved);
    }

    /*PARCELABLE IMPLEMENTATION*/
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

}
