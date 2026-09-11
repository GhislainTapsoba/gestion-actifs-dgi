package com.dgi.gestionactifs.domain;

import com.dgi.gestionactifs.domain.enumeration.StatutPlanning;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PlanningMaintenance.
 */
@Entity
@Table(name = "planning_maintenance")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlanningMaintenance implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date_prevue", nullable = false)
    private LocalDate datePrevue;

    @Column(name = "periodicite")
    private String periodicite;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private StatutPlanning statut;

    @Column(name = "description")
    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_planning_maintenance__intervention",
        joinColumns = @JoinColumn(name = "planning_maintenance_id"),
        inverseJoinColumns = @JoinColumn(name = "intervention_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "panne", "plannings" }, allowSetters = true)
    private Set<Intervention> interventions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PlanningMaintenance id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDatePrevue() {
        return this.datePrevue;
    }

    public PlanningMaintenance datePrevue(LocalDate datePrevue) {
        this.setDatePrevue(datePrevue);
        return this;
    }

    public void setDatePrevue(LocalDate datePrevue) {
        this.datePrevue = datePrevue;
    }

    public String getPeriodicite() {
        return this.periodicite;
    }

    public PlanningMaintenance periodicite(String periodicite) {
        this.setPeriodicite(periodicite);
        return this;
    }

    public void setPeriodicite(String periodicite) {
        this.periodicite = periodicite;
    }

    public StatutPlanning getStatut() {
        return this.statut;
    }

    public PlanningMaintenance statut(StatutPlanning statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(StatutPlanning statut) {
        this.statut = statut;
    }

    public String getDescription() {
        return this.description;
    }

    public PlanningMaintenance description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Intervention> getInterventions() {
        return this.interventions;
    }

    public void setInterventions(Set<Intervention> interventions) {
        this.interventions = interventions;
    }

    public PlanningMaintenance interventions(Set<Intervention> interventions) {
        this.setInterventions(interventions);
        return this;
    }

    public PlanningMaintenance addIntervention(Intervention intervention) {
        this.interventions.add(intervention);
        return this;
    }

    public PlanningMaintenance removeIntervention(Intervention intervention) {
        this.interventions.remove(intervention);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlanningMaintenance)) {
            return false;
        }
        return getId() != null && getId().equals(((PlanningMaintenance) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlanningMaintenance{" +
            "id=" + getId() +
            ", datePrevue='" + getDatePrevue() + "'" +
            ", periodicite='" + getPeriodicite() + "'" +
            ", statut='" + getStatut() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
