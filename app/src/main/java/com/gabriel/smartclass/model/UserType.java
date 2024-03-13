package com.gabriel.smartclass.model;

import java.util.Objects;

public class UserType {
    private String uuid;
    private String description;


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
