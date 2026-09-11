package com.dgi.gestionactifs.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.dgi.gestionactifs.domain.Affectation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AffectationDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate dateAffectation;

    private String motif;

    private LocalDate dateRestitution;

    @NotNull
    private AgentDTO agent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateAffectation() {
        return dateAffectation;
    }

    public void setDateAffectation(LocalDate dateAffectation) {
        this.dateAffectation = dateAffectation;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public LocalDate getDateRestitution() {
        return dateRestitution;
    }

    public void setDateRestitution(LocalDate dateRestitution) {
        this.dateRestitution = dateRestitution;
    }

    public AgentDTO getAgent() {
        return agent;
    }

    public void setAgent(AgentDTO agent) {
        this.agent = agent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AffectationDTO)) {
            return false;
        }

        AffectationDTO affectationDTO = (AffectationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, affectationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AffectationDTO{" +
            "id=" + getId() +
            ", dateAffectation='" + getDateAffectation() + "'" +
            ", motif='" + getMotif() + "'" +
            ", dateRestitution='" + getDateRestitution() + "'" +
            ", agent=" + getAgent() +
            "}";
    }
}
