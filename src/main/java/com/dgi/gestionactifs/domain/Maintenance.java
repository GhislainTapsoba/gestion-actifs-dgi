package com.dgi.gestionactifs.domain;

import com.dgi.gestionactifs.domain.enumeration.StatutMaintenance;
import com.dgi.gestionactifs.domain.enumeration.TypeMaintenance;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Maintenance.
 */
@Entity
@Table(name = "maintenance")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Maintenance implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type_maintenance", nullable = false)
    private TypeMaintenance typeMaintenance;

    @Column(name = "date_panne")
    private LocalDate datePanne;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private StatutMaintenance statut;

    @Lob
    @Column(name = "compte_rendu")
    private String compteRendu;

    @Column(name = "date_cloture")
    private LocalDate dateCloture;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "categorie" }, allowSetters = true)
    private Actif actif;

    @ManyToOne(fetch = FetchType.LAZY)
    private User technicien;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Maintenance id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeMaintenance getTypeMaintenance() {
        return this.typeMaintenance;
    }

    public Maintenance typeMaintenance(TypeMaintenance typeMaintenance) {
        this.setTypeMaintenance(typeMaintenance);
        return this;
    }

    public void setTypeMaintenance(TypeMaintenance typeMaintenance) {
        this.typeMaintenance = typeMaintenance;
    }

    public LocalDate getDatePanne() {
        return this.datePanne;
    }

    public Maintenance datePanne(LocalDate datePanne) {
        this.setDatePanne(datePanne);
        return this;
    }

    public void setDatePanne(LocalDate datePanne) {
        this.datePanne = datePanne;
    }

    public StatutMaintenance getStatut() {
        return this.statut;
    }

    public Maintenance statut(StatutMaintenance statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(StatutMaintenance statut) {
        this.statut = statut;
    }

    public String getCompteRendu() {
        return this.compteRendu;
    }

    public Maintenance compteRendu(String compteRendu) {
        this.setCompteRendu(compteRendu);
        return this;
    }

    public void setCompteRendu(String compteRendu) {
        this.compteRendu = compteRendu;
    }

    public LocalDate getDateCloture() {
        return this.dateCloture;
    }

    public Maintenance dateCloture(LocalDate dateCloture) {
        this.setDateCloture(dateCloture);
        return this;
    }

    public void setDateCloture(LocalDate dateCloture) {
        this.dateCloture = dateCloture;
    }

    public Actif getActif() {
        return this.actif;
    }

    public void setActif(Actif actif) {
        this.actif = actif;
    }

    public Maintenance actif(Actif actif) {
        this.setActif(actif);
        return this;
    }

    public User getTechnicien() {
        return this.technicien;
    }

    public void setTechnicien(User user) {
        this.technicien = user;
    }

    public Maintenance technicien(User user) {
        this.setTechnicien(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Maintenance)) {
            return false;
        }
        return getId() != null && getId().equals(((Maintenance) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Maintenance{" +
            "id=" + getId() +
            ", typeMaintenance='" + getTypeMaintenance() + "'" +
            ", datePanne='" + getDatePanne() + "'" +
            ", statut='" + getStatut() + "'" +
            ", compteRendu='" + getCompteRendu() + "'" +
            ", dateCloture='" + getDateCloture() + "'" +
            "}";
    }
}
