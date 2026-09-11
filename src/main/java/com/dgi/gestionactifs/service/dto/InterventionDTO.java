package com.dgi.gestionactifs.service.dto;

import com.dgi.gestionactifs.domain.enumeration.StatutIntervention;
import com.dgi.gestionactifs.domain.enumeration.TypeIntervention;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.dgi.gestionactifs.domain.Intervention} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InterventionDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate dateDeclaration;

    @NotNull
    private TypeIntervention typeIntervention;

    @NotNull
    private StatutIntervention statut;

    private String description;

    @NotNull
    private PanneDTO panne;

    private Set<PlanningMaintenanceDTO> plannings = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateDeclaration() {
        return dateDeclaration;
    }

    public void setDateDeclaration(LocalDate dateDeclaration) {
        this.dateDeclaration = dateDeclaration;
    }

    public TypeIntervention getTypeIntervention() {
        return typeIntervention;
    }

    public void setTypeIntervention(TypeIntervention typeIntervention) {
        this.typeIntervention = typeIntervention;
    }

    public StatutIntervention getStatut() {
        return statut;
    }

    public void setStatut(StatutIntervention statut) {
        this.statut = statut;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PanneDTO getPanne() {
        return panne;
    }

    public void setPanne(PanneDTO panne) {
        this.panne = panne;
    }

    public Set<PlanningMaintenanceDTO> getPlannings() {
        return plannings;
    }

    public void setPlannings(Set<PlanningMaintenanceDTO> plannings) {
        this.plannings = plannings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InterventionDTO)) {
            return false;
        }

        InterventionDTO interventionDTO = (InterventionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, interventionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InterventionDTO{" +
            "id=" + getId() +
            ", dateDeclaration='" + getDateDeclaration() + "'" +
            ", typeIntervention='" + getTypeIntervention() + "'" +
            ", statut='" + getStatut() + "'" +
            ", description='" + getDescription() + "'" +
            ", panne=" + getPanne() +
            ", plannings=" + getPlannings() +
            "}";
    }
}
