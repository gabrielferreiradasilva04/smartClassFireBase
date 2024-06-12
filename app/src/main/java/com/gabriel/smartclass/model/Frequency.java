package com.gabriel.smartclass.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.gabriel.smartclass.model.baseEntitys.SimpleAuxEntity;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class Frequency extends SimpleAuxEntity implements Parcelable {
    private String id;
    private String description;
    private DocumentReference type_id;
    private Timestamp date;

    protected Frequency(Parcel in) {
        id = in.readString();
        description = in.readString();
        long seconds = in.readLong();
        int nanoseconds = in.readInt();
        this.date = new Timestamp(seconds, nanoseconds);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(description);
        dest.writeString(type_id.getPath());
        dest.writeLong(date.getSeconds());
        dest.writeInt(date.getNanoseconds());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Frequency> CREATOR = new Creator<Frequency>() {
        @Override
        public Frequency createFromParcel(Parcel in) {
            String id = in.readString();
            String description = in.readString();
            String typeReferenceString = in.readString();
            DocumentReference typeReference  = null;
            long seconds = in.readLong();
            int nanoseconds = in.readInt();
            Timestamp date = new Timestamp(seconds, nanoseconds);
            if(typeReferenceString != null){
                typeReference = FirebaseFirestore.getInstance().collection(FrequencyType.class.getSimpleName()).document(typeReferenceString);
            }
            return new Frequency(id,description,typeReference, date);
        }

        @Override
        public Frequency[] newArray(int size) {
            return new Frequency[size];
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

    public DocumentReference getType_id() {
        return type_id;
    }

    public void setType_id(DocumentReference type_id) {
        this.type_id = type_id;
    }

    public Frequency() {
    }

    public Frequency(String id, String description, DocumentReference type_id, Timestamp date) {
        this.id = id;
        this.description = description;
        this.type_id = type_id;
        this.date = date;
    }

    public Frequency(String id, String description) {
        this.id = id;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Frequency)) return false;
        Frequency frequency = (Frequency) o;
        return Objects.equals(getId(), frequency.getId()) && Objects.equals(getDescription(), frequency.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDescription());
    }

    @Override
    public String toString() {
        return "Frequency{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
