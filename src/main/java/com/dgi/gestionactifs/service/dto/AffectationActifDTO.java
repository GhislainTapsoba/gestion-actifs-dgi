package com.dgi.gestionactifs.service.dto;

import com.dgi.gestionactifs.domain.enumeration.StatutAffectation;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.dgi.gestionactifs.domain.AffectationActif} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AffectationActifDTO implements Serializable {

    private Long id;

    private String observation;

    @NotNull
    private StatutAffectation statut;

    @NotNull
    private AffectationDTO affectation;

    @NotNull
    private ActifDTO actif;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public StatutAffectation getStatut() {
        return statut;
    }

    public void setStatut(StatutAffectation statut) {
        this.statut = statut;
    }

    public AffectationDTO getAffectation() {
        return affectation;
    }

    public void setAffectation(AffectationDTO affectation) {
        this.affectation = affectation;
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
        if (!(o instanceof AffectationActifDTO)) {
            return false;
        }

        AffectationActifDTO affectationActifDTO = (AffectationActifDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, affectationActifDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AffectationActifDTO{" +
            "id=" + getId() +
            ", observation='" + getObservation() + "'" +
            ", statut='" + getStatut() + "'" +
            ", affectation=" + getAffectation() +
            ", actif=" + getActif() +
            "}";
    }
}
