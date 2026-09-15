package com.dgi.gestionactifs.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.dgi.gestionactifs.domain.Rapport} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RapportDTO implements Serializable {

    private Long id;

    private String titre;

    private String typeRapport;

    private String description;

    private String cheminFichier;

    private Instant dateGeneration;

    private String generePar;

    private String formatExport;

    private String parametres;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getTypeRapport() {
        return typeRapport;
    }

    public void setTypeRapport(String typeRapport) {
        this.typeRapport = typeRapport;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCheminFichier() {
        return cheminFichier;
    }

    public void setCheminFichier(String cheminFichier) {
        this.cheminFichier = cheminFichier;
    }

    public Instant getDateGeneration() {
        return dateGeneration;
    }

    public void setDateGeneration(Instant dateGeneration) {
        this.dateGeneration = dateGeneration;
    }

    public String getGenerePar() {
        return generePar;
    }

    public void setGenerePar(String generePar) {
        this.generePar = generePar;
    }

    public String getFormatExport() {
        return formatExport;
    }

    public void setFormatExport(String formatExport) {
        this.formatExport = formatExport;
    }

    public String getParametres() {
        return parametres;
    }

    public void setParametres(String parametres) {
        this.parametres = parametres;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RapportDTO)) {
            return false;
        }

        RapportDTO rapportDTO = (RapportDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rapportDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RapportDTO{" +
            "id=" + getId() +
            ", titre='" + getTitre() + "'" +
            ", typeRapport='" + getTypeRapport() + "'" +
            ", description='" + getDescription() + "'" +
            ", cheminFichier='" + getCheminFichier() + "'" +
            ", dateGeneration='" + getDateGeneration() + "'" +
            ", generePar='" + getGenerePar() + "'" +
            ", formatExport='" + getFormatExport() + "'" +
            ", parametres='" + getParametres() + "'" +
            "}";
    }
}
