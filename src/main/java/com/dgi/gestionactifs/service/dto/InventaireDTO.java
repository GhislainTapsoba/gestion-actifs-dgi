package com.dgi.gestionactifs.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.dgi.gestionactifs.domain.Inventaire} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InventaireDTO implements Serializable {

    private Long id;

    @NotNull
    private String nomFichier;

    @NotNull
    private LocalDate dateImport;

    private ActifDTO actif;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFichier() {
        return nomFichier;
    }

    public void setNomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
    }

    public LocalDate getDateImport() {
        return dateImport;
    }

    public void setDateImport(LocalDate dateImport) {
        this.dateImport = dateImport;
    }

    public ActifDTO getActif() {
        return actif;
    }

    public void setActif(ActifDTO actif) {
        this.actif = actif;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InventaireDTO)) {
            return false;
        }

        InventaireDTO inventaireDTO = (InventaireDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, inventaireDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InventaireDTO{" +
            "id=" + getId() +
            ", nomFichier='" + getNomFichier() + "'" +
            ", dateImport='" + getDateImport() + "'" +
            ", actif=" + getActif() +
            "}";
    }
}
