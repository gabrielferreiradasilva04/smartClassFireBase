package com.gabriel.smartclass.model;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Objects;

public class InstitutionUser {
    private String id;
    private String userAuth_id;
    private UserType userType_id;
    private Map<String, Object> identification;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserAuth_id() {
        return userAuth_id;
    }

    public void setUserAuth_id(String userAuth_id) {
        this.userAuth_id = userAuth_id;
    }

    public UserType getUserType_id() {
        return userType_id;
    }

    public void setUserType_id(UserType userType_id) {
        this.userType_id = userType_id;
    }

    public Map<String, Object> getIdentification() {
        return identification;
    }

    public void setIdentification(Map<String, Object> identification) {
        this.identification = identification;
    }

    public InstitutionUser() {
    }


    public InstitutionUser(String userAuth_id, UserType userType_id, Map<String, Object> identification) {
        this.userAuth_id = userAuth_id;
        this.userType_id = userType_id;
        this.identification = identification;
    }

    public InstitutionUser(String id, String userAuth_id, UserType userType_id, Map<String, Object> identification) {
        this.id = id;
        this.userAuth_id = userAuth_id;
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
