package com.gabriel.smartclass.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.gabriel.smartclass.model.baseEntitys.SimpleAuxEntity;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class CourseMember extends SimpleAuxEntity implements Parcelable {
    private String id;
    private String description;
    private DocumentReference mainUserReference;

    protected CourseMember(Parcel in) {
        id = in.readString();
        description = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(description);
        dest.writeString(mainUserReference.getPath());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CourseMember> CREATOR = new Creator<CourseMember>() {
        @Override
        public CourseMember createFromParcel(Parcel in) {
            String id = in.readString();
            String description = in.readString();
            String instUserString = in.readString();
            DocumentReference institutionUserReference = null;
            if(instUserString!=null && !instUserString.equals("")){
               institutionUserReference = FirebaseFirestore.getInstance().document(instUserString);
            }
            return new CourseMember(id, description, institutionUserReference);
        }

        @Override
        public CourseMember[] newArray(int size) {
            return new CourseMember[size];
        }
    };

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DocumentReference getMainUserReference() {
        return mainUserReference;
    }

    public void setMainUserReference(DocumentReference mainUserReference) {
        this.mainUserReference = mainUserReference;
    }

    public CourseMember(String id, String description, DocumentReference institutionUserReference) {
        this.id = id;
        this.description = description;
        this.mainUserReference = institutionUserReference;
    }

    public CourseMember() {
    }
}
