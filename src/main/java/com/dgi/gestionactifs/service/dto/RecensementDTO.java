package com.dgi.gestionactifs.service.dto;

import com.dgi.gestionactifs.domain.enumeration.StatutRecensement;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.dgi.gestionactifs.domain.Recensement} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RecensementDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate dateDebut;

    private LocalDate dateFin;

    @NotNull
    private StatutRecensement statut;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public StatutRecensement getStatut() {
        return statut;
    }

    public void setStatut(StatutRecensement statut) {
        this.statut = statut;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecensementDTO)) {
            return false;
        }

        RecensementDTO recensementDTO = (RecensementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, recensementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecensementDTO{" +
            "id=" + getId() +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", statut='" + getStatut() + "'" +
            "}";
    }
}
