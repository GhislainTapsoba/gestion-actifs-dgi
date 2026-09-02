package com.dgi.gestionactifs.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.dgi.gestionactifs.domain.Fournisseur} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FournisseurDTO implements Serializable {

    private Long id;

    @NotNull
    private String nom;

    private String contact;

    private String email;

    private String telephone;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FournisseurDTO)) {
            return false;
        }

        FournisseurDTO fournisseurDTO = (FournisseurDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fournisseurDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FournisseurDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", contact='" + getContact() + "'" +
            ", email='" + getEmail() + "'" +
            ", telephone='" + getTelephone() + "'" +
            "}";
    }
}
