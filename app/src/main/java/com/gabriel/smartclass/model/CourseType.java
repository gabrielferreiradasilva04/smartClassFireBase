package com.gabriel.smartclass.model;

import java.util.Objects;

public class CourseType {
    private String id;
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CourseType(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public CourseType() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseType)) return false;
        CourseType that = (CourseType) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getDescription(), that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDescription());
    }

    @Override
    public String toString() {
        return "CourseType{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
