package com.dgi.gestionactifs.domain;

import com.dgi.gestionactifs.domain.enumeration.EtatMateriel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EquipementRecensement.
 */
@Entity
@Table(name = "equipement_recensement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EquipementRecensement implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "etat_constate", nullable = false)
    private EtatMateriel etatConstate;

    @NotNull
    @Column(name = "date_constat", nullable = false)
    private LocalDate dateConstat;

    @Column(name = "emplacement_constate")
    private String emplacementConstate;

    @Column(name = "anomalie_constatee")
    private Boolean anomalieConstatee;

    @ManyToOne(optional = false)
    @NotNull
    private Recensement recensement;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "categorie" }, allowSetters = true)
    private Actif actif;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EquipementRecensement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EtatMateriel getEtatConstate() {
        return this.etatConstate;
    }

    public EquipementRecensement etatConstate(EtatMateriel etatConstate) {
        this.setEtatConstate(etatConstate);
        return this;
    }

    public void setEtatConstate(EtatMateriel etatConstate) {
        this.etatConstate = etatConstate;
    }

    public LocalDate getDateConstat() {
        return this.dateConstat;
    }

    public EquipementRecensement dateConstat(LocalDate dateConstat) {
        this.setDateConstat(dateConstat);
        return this;
    }

    public void setDateConstat(LocalDate dateConstat) {
        this.dateConstat = dateConstat;
    }

    public String getEmplacementConstate() {
        return this.emplacementConstate;
    }

    public EquipementRecensement emplacementConstate(String emplacementConstate) {
        this.setEmplacementConstate(emplacementConstate);
        return this;
    }

    public void setEmplacementConstate(String emplacementConstate) {
        this.emplacementConstate = emplacementConstate;
    }

    public Boolean getAnomalieConstatee() {
        return this.anomalieConstatee;
    }

    public EquipementRecensement anomalieConstatee(Boolean anomalieConstatee) {
        this.setAnomalieConstatee(anomalieConstatee);
        return this;
    }

    public void setAnomalieConstatee(Boolean anomalieConstatee) {
        this.anomalieConstatee = anomalieConstatee;
    }

    public Recensement getRecensement() {
        return this.recensement;
    }

    public void setRecensement(Recensement recensement) {
        this.recensement = recensement;
    }

    public EquipementRecensement recensement(Recensement recensement) {
        this.setRecensement(recensement);
        return this;
    }

    public Actif getActif() {
        return this.actif;
    }

    public void setActif(Actif actif) {
        this.actif = actif;
    }

    public EquipementRecensement actif(Actif actif) {
        this.setActif(actif);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EquipementRecensement)) {
            return false;
        }
        return getId() != null && getId().equals(((EquipementRecensement) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EquipementRecensement{" +
            "id=" + getId() +
            ", etatConstate='" + getEtatConstate() + "'" +
            ", dateConstat='" + getDateConstat() + "'" +
            ", emplacementConstate='" + getEmplacementConstate() + "'" +
            ", anomalieConstatee='" + getAnomalieConstatee() + "'" +
            "}";
    }
}
