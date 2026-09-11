package com.dgi.gestionactifs.service.dto;

import com.dgi.gestionactifs.domain.enumeration.StatutPlanning;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.dgi.gestionactifs.domain.PlanningMaintenance} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlanningMaintenanceDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate datePrevue;

    private String periodicite;

    @NotNull
    private StatutPlanning statut;

    private String description;

    private Set<InterventionDTO> interventions = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDatePrevue() {
        return datePrevue;
    }

    public void setDatePrevue(LocalDate datePrevue) {
        this.datePrevue = datePrevue;
    }

    public String getPeriodicite() {
        return periodicite;
    }

    public void setPeriodicite(String periodicite) {
        this.periodicite = periodicite;
    }

    public StatutPlanning getStatut() {
        return statut;
    }

    public void setStatut(StatutPlanning statut) {
        this.statut = statut;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<InterventionDTO> getInterventions() {
        return interventions;
    }

    public void setInterventions(Set<InterventionDTO> interventions) {
        this.interventions = interventions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlanningMaintenanceDTO)) {
            return false;
        }

        PlanningMaintenanceDTO planningMaintenanceDTO = (PlanningMaintenanceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, planningMaintenanceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlanningMaintenanceDTO{" +
            "id=" + getId() +
            ", datePrevue='" + getDatePrevue() + "'" +
            ", periodicite='" + getPeriodicite() + "'" +
            ", statut='" + getStatut() + "'" +
            ", description='" + getDescription() + "'" +
            ", interventions=" + getInterventions() +
            "}";
    }
}
