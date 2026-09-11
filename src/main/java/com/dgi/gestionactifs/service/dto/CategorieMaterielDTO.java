package com.dgi.gestionactifs.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.dgi.gestionactifs.domain.CategorieMateriel} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CategorieMaterielDTO implements Serializable {

    private Long id;

    @NotNull
    private String libelle;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CategorieMaterielDTO)) {
            return false;
        }

        CategorieMaterielDTO categorieMaterielDTO = (CategorieMaterielDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, categorieMaterielDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CategorieMaterielDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
