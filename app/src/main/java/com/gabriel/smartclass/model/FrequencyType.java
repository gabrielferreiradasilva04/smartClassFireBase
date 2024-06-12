package com.gabriel.smartclass.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.gabriel.smartclass.model.baseEntitys.SimpleAuxEntity;

import java.util.Objects;

public class FrequencyType extends SimpleAuxEntity implements Parcelable {
    private String id;
    private String description;

    protected FrequencyType(Parcel in) {
        id = in.readString();
        description = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(description);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FrequencyType> CREATOR = new Creator<FrequencyType>() {
        @Override
        public FrequencyType createFromParcel(Parcel in) {
            return new FrequencyType(in);
        }

        @Override
        public FrequencyType[] newArray(int size) {
            return new FrequencyType[size];
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

    public FrequencyType(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public FrequencyType() {
    }

    @Override
    public String toString() {
        return "FrequencyType{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FrequencyType)) return false;
        FrequencyType that = (FrequencyType) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getDescription(), that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDescription());
    }
}
