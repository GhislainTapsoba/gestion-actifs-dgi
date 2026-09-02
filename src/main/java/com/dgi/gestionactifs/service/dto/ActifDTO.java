package com.dgi.gestionactifs.service.dto;

import com.dgi.gestionactifs.domain.enumeration.StatutActif;
import com.dgi.gestionactifs.domain.enumeration.TypeActif;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.dgi.gestionactifs.domain.Actif} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ActifDTO implements Serializable {

    private Long id;

    @NotNull
    private String identifiantUnique;

    private String codeBarreQR;

    @NotNull
    private TypeActif type;

    @NotNull
    private StatutActif etat;

    private String localisation;

    private LocalDate dateAcquisition;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentifiantUnique() {
        return identifiantUnique;
    }

    public void setIdentifiantUnique(String identifiantUnique) {
        this.identifiantUnique = identifiantUnique;
    }

    public String getCodeBarreQR() {
        return codeBarreQR;
    }

    public void setCodeBarreQR(String codeBarreQR) {
        this.codeBarreQR = codeBarreQR;
    }

    public TypeActif getType() {
        return type;
    }

    public void setType(TypeActif type) {
        this.type = type;
    }

    public StatutActif getEtat() {
        return etat;
    }

    public void setEtat(StatutActif etat) {
        this.etat = etat;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public LocalDate getDateAcquisition() {
        return dateAcquisition;
    }

    public void setDateAcquisition(LocalDate dateAcquisition) {
        this.dateAcquisition = dateAcquisition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ActifDTO)) {
            return false;
        }

        ActifDTO actifDTO = (ActifDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, actifDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ActifDTO{" +
            "id=" + getId() +
            ", identifiantUnique='" + getIdentifiantUnique() + "'" +
            ", codeBarreQR='" + getCodeBarreQR() + "'" +
            ", type='" + getType() + "'" +
            ", etat='" + getEtat() + "'" +
            ", localisation='" + getLocalisation() + "'" +
            ", dateAcquisition='" + getDateAcquisition() + "'" +
            "}";
    }
}
