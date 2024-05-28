package com.gabriel.smartclass.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.gabriel.smartclass.model.baseEntitys.SimpleAuxEntity;

import java.util.Objects;

public class UserType extends SimpleAuxEntity implements Parcelable {
    private String id;
    private String description;

    protected UserType(Parcel in) {
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

    public static final Creator<UserType> CREATOR = new Creator<UserType>() {
        @Override
        public UserType createFromParcel(Parcel in) {
            return new UserType(in);
        }

        @Override
        public UserType[] newArray(int size) {
            return new UserType[size];
        }
    };

    public String getUuid() {
        return id;
    }

    public void setUuid(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserType(String description) {
        this.description = description;
    }

    public UserType() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserType userType = (UserType) o;
        return Objects.equals(description, userType.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }

    @Override
    public String toString() {
        return "UserType{" +
                "description='" + description + '\'' +
                '}';
    }


}
