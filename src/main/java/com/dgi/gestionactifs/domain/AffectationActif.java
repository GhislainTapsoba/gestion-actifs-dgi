package com.dgi.gestionactifs.domain;

import com.dgi.gestionactifs.domain.enumeration.StatutAffectation;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AffectationActif.
 */
@Entity
@Table(name = "affectation_actif")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AffectationActif implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "observation")
    private String observation;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private StatutAffectation statut;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "agent" }, allowSetters = true)
    private Affectation affectation;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "categorie" }, allowSetters = true)
    private Actif actif;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AffectationActif id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObservation() {
        return this.observation;
    }

    public AffectationActif observation(String observation) {
        this.setObservation(observation);
        return this;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public StatutAffectation getStatut() {
        return this.statut;
    }

    public AffectationActif statut(StatutAffectation statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(StatutAffectation statut) {
        this.statut = statut;
    }

    public Affectation getAffectation() {
        return this.affectation;
    }

    public void setAffectation(Affectation affectation) {
        this.affectation = affectation;
    }

    public AffectationActif affectation(Affectation affectation) {
        this.setAffectation(affectation);
        return this;
    }

    public Actif getActif() {
        return this.actif;
    }

    public void setActif(Actif actif) {
        this.actif = actif;
    }

    public AffectationActif actif(Actif actif) {
        this.setActif(actif);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AffectationActif)) {
            return false;
        }
        return getId() != null && getId().equals(((AffectationActif) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AffectationActif{" +
            "id=" + getId() +
            ", observation='" + getObservation() + "'" +
            ", statut='" + getStatut() + "'" +
            "}";
    }
}
