package com.gabriel.smartclass.model;

import java.util.Objects;

public class Institution {
    private String id;
    private String cnpj;
    private String name;
    private InstitutionUser responsable_id;
    private String duration;

    //getters and setters


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InstitutionUser getResponsable_id() {
        return responsable_id;
    }

    public void setResponsable_id(InstitutionUser responsable_id) {
        this.responsable_id = responsable_id;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Institution(String cnpj, String name, InstitutionUser responsable_id, String duration) {
        this.cnpj = cnpj;
        this.name = name;
        this.responsable_id = responsable_id;
        this.duration = duration;
    }

    public Institution(String id, String cnpj, String name, InstitutionUser responsable_id, String duration) {
        this.id = id;
        this.cnpj = cnpj;
        this.name = name;
        this.responsable_id = responsable_id;
        this.duration = duration;
    }

    public Institution() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Institution institution = (Institution) o;
        return Objects.equals(id, institution.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
