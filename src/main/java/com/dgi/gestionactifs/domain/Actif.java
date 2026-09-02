package com.dgi.gestionactifs.domain;

import com.dgi.gestionactifs.domain.enumeration.StatutActif;
import com.dgi.gestionactifs.domain.enumeration.TypeActif;
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
 * A Actif.
 */
@Entity
@Table(name = "actif")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Actif implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "identifiant_unique", nullable = false, unique = true)
    private String identifiantUnique;

    @Column(name = "code_barre_qr")
    private String codeBarreQR;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TypeActif type;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "etat", nullable = false)
    private StatutActif etat;

    @Column(name = "localisation")
    private String localisation;

    @Column(name = "date_acquisition")
    private LocalDate dateAcquisition;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "actif")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "utilisateur", "actif" }, allowSetters = true)
    private Set<Affectation> affectations = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "actif")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "demandeur", "validateur", "actif" }, allowSetters = true)
    private Set<Transfert> transferts = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "actif")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "technicien", "actif" }, allowSetters = true)
    private Set<Maintenance> maintenances = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Actif id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentifiantUnique() {
        return this.identifiantUnique;
    }

    public Actif identifiantUnique(String identifiantUnique) {
        this.setIdentifiantUnique(identifiantUnique);
        return this;
    }

    public void setIdentifiantUnique(String identifiantUnique) {
        this.identifiantUnique = identifiantUnique;
    }

    public String getCodeBarreQR() {
        return this.codeBarreQR;
    }

    public Actif codeBarreQR(String codeBarreQR) {
        this.setCodeBarreQR(codeBarreQR);
        return this;
    }

    public void setCodeBarreQR(String codeBarreQR) {
        this.codeBarreQR = codeBarreQR;
    }

    public TypeActif getType() {
        return this.type;
    }

    public Actif type(TypeActif type) {
        this.setType(type);
        return this;
    }

    public void setType(TypeActif type) {
        this.type = type;
    }

    public StatutActif getEtat() {
        return this.etat;
    }

    public Actif etat(StatutActif etat) {
        this.setEtat(etat);
        return this;
    }

    public void setEtat(StatutActif etat) {
        this.etat = etat;
    }

    public String getLocalisation() {
        return this.localisation;
    }

    public Actif localisation(String localisation) {
        this.setLocalisation(localisation);
        return this;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public LocalDate getDateAcquisition() {
        return this.dateAcquisition;
    }

    public Actif dateAcquisition(LocalDate dateAcquisition) {
        this.setDateAcquisition(dateAcquisition);
        return this;
    }

    public void setDateAcquisition(LocalDate dateAcquisition) {
        this.dateAcquisition = dateAcquisition;
    }

    public Set<Affectation> getAffectations() {
        return this.affectations;
    }

    public void setAffectations(Set<Affectation> affectations) {
        if (this.affectations != null) {
            this.affectations.forEach(i -> i.setActif(null));
        }
        if (affectations != null) {
            affectations.forEach(i -> i.setActif(this));
        }
        this.affectations = affectations;
    }

    public Actif affectations(Set<Affectation> affectations) {
        this.setAffectations(affectations);
        return this;
    }

    public Actif addAffectation(Affectation affectation) {
        this.affectations.add(affectation);
        affectation.setActif(this);
        return this;
    }

    public Actif removeAffectation(Affectation affectation) {
        this.affectations.remove(affectation);
        affectation.setActif(null);
        return this;
    }

    public Set<Transfert> getTransferts() {
        return this.transferts;
    }

    public void setTransferts(Set<Transfert> transferts) {
        if (this.transferts != null) {
            this.transferts.forEach(i -> i.setActif(null));
        }
        if (transferts != null) {
            transferts.forEach(i -> i.setActif(this));
        }
        this.transferts = transferts;
    }

    public Actif transferts(Set<Transfert> transferts) {
        this.setTransferts(transferts);
        return this;
    }

    public Actif addTransfert(Transfert transfert) {
        this.transferts.add(transfert);
        transfert.setActif(this);
        return this;
    }

    public Actif removeTransfert(Transfert transfert) {
        this.transferts.remove(transfert);
        transfert.setActif(null);
        return this;
    }

    public Set<Maintenance> getMaintenances() {
        return this.maintenances;
    }

    public void setMaintenances(Set<Maintenance> maintenances) {
        if (this.maintenances != null) {
            this.maintenances.forEach(i -> i.setActif(null));
        }
        if (maintenances != null) {
            maintenances.forEach(i -> i.setActif(this));
        }
        this.maintenances = maintenances;
    }

    public Actif maintenances(Set<Maintenance> maintenances) {
        this.setMaintenances(maintenances);
        return this;
    }

    public Actif addMaintenance(Maintenance maintenance) {
        this.maintenances.add(maintenance);
        maintenance.setActif(this);
        return this;
    }

    public Actif removeMaintenance(Maintenance maintenance) {
        this.maintenances.remove(maintenance);
        maintenance.setActif(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Actif)) {
            return false;
        }
        return getId() != null && getId().equals(((Actif) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Actif{" +
            "id=" + getId() +
            ", identifiantUnique='" + getIdentifiantUnique() + "'" +
            ", codeBarreQR='" + getCodeBarreQR() + "'" +
            ", type='" + getType() + "'" +
            ", etat='" + getEtat() + "'" +
            ", localisation='" + getLocalisation() + "'" +
            ", dateAcquisition='" + getDateAcquisition() + "'" +
            "}";
    }
}
