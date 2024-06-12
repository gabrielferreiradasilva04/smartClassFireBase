package com.gabriel.smartclass.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.gabriel.smartclass.model.baseEntitys.SimpleAuxEntity;

import java.util.Objects;

public class Location extends SimpleAuxEntity implements Parcelable {
    private String id;
    private String description;

    protected Location(Parcel in) {
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

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;
        Location location = (Location) o;
        return Objects.equals(getId(), location.getId()) && Objects.equals(getDescription(), location.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDescription());
    }

    public void setId(String id) {
        this.id = id;
    }

    public Location(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public Location() {
    }

    @Override
    public String toString() {
        return "Location{}";
    }
}
