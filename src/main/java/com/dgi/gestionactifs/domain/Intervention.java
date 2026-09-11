package com.dgi.gestionactifs.domain;

import com.dgi.gestionactifs.domain.enumeration.StatutIntervention;
import com.dgi.gestionactifs.domain.enumeration.TypeIntervention;
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
 * A Intervention.
 */
@Entity
@Table(name = "intervention")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Intervention implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date_declaration", nullable = false)
    private LocalDate dateDeclaration;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type_intervention", nullable = false)
    private TypeIntervention typeIntervention;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private StatutIntervention statut;

    @Column(name = "description")
    private String description;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "actif" }, allowSetters = true)
    private Panne panne;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "interventions")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "interventions" }, allowSetters = true)
    private Set<PlanningMaintenance> plannings = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Intervention id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateDeclaration() {
        return this.dateDeclaration;
    }

    public Intervention dateDeclaration(LocalDate dateDeclaration) {
        this.setDateDeclaration(dateDeclaration);
        return this;
    }

    public void setDateDeclaration(LocalDate dateDeclaration) {
        this.dateDeclaration = dateDeclaration;
    }

    public TypeIntervention getTypeIntervention() {
        return this.typeIntervention;
    }

    public Intervention typeIntervention(TypeIntervention typeIntervention) {
        this.setTypeIntervention(typeIntervention);
        return this;
    }

    public void setTypeIntervention(TypeIntervention typeIntervention) {
        this.typeIntervention = typeIntervention;
    }

    public StatutIntervention getStatut() {
        return this.statut;
    }

    public Intervention statut(StatutIntervention statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(StatutIntervention statut) {
        this.statut = statut;
    }

    public String getDescription() {
        return this.description;
    }

    public Intervention description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Panne getPanne() {
        return this.panne;
    }

    public void setPanne(Panne panne) {
        this.panne = panne;
    }

    public Intervention panne(Panne panne) {
        this.setPanne(panne);
        return this;
    }

    public Set<PlanningMaintenance> getPlannings() {
        return this.plannings;
    }

    public void setPlannings(Set<PlanningMaintenance> planningMaintenances) {
        if (this.plannings != null) {
            this.plannings.forEach(i -> i.removeIntervention(this));
        }
        if (planningMaintenances != null) {
            planningMaintenances.forEach(i -> i.addIntervention(this));
        }
        this.plannings = planningMaintenances;
    }

    public Intervention plannings(Set<PlanningMaintenance> planningMaintenances) {
        this.setPlannings(planningMaintenances);
        return this;
    }

    public Intervention addPlanning(PlanningMaintenance planningMaintenance) {
        this.plannings.add(planningMaintenance);
        planningMaintenance.getInterventions().add(this);
        return this;
    }

    public Intervention removePlanning(PlanningMaintenance planningMaintenance) {
        this.plannings.remove(planningMaintenance);
        planningMaintenance.getInterventions().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Intervention)) {
            return false;
        }
        return getId() != null && getId().equals(((Intervention) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Intervention{" +
            "id=" + getId() +
            ", dateDeclaration='" + getDateDeclaration() + "'" +
            ", typeIntervention='" + getTypeIntervention() + "'" +
            ", statut='" + getStatut() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
