package com.dgi.gestionactifs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Affectation.
 */
@Entity
@Table(name = "affectation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Affectation implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date_affectation", nullable = false)
    private LocalDate dateAffectation;

    @Column(name = "date_restitution")
    private LocalDate dateRestitution;

    @Column(name = "numero_bordereau")
    private String numeroBordereau;

    @ManyToOne(fetch = FetchType.LAZY)
    private User utilisateur;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "affectations", "transferts", "maintenances" }, allowSetters = true)
    private Actif actif;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Affectation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateAffectation() {
        return this.dateAffectation;
    }

    public Affectation dateAffectation(LocalDate dateAffectation) {
        this.setDateAffectation(dateAffectation);
        return this;
    }

    public void setDateAffectation(LocalDate dateAffectation) {
        this.dateAffectation = dateAffectation;
    }

    public LocalDate getDateRestitution() {
        return this.dateRestitution;
    }

    public Affectation dateRestitution(LocalDate dateRestitution) {
        this.setDateRestitution(dateRestitution);
        return this;
    }

    public void setDateRestitution(LocalDate dateRestitution) {
        this.dateRestitution = dateRestitution;
    }

    public String getNumeroBordereau() {
        return this.numeroBordereau;
    }

    public Affectation numeroBordereau(String numeroBordereau) {
        this.setNumeroBordereau(numeroBordereau);
        return this;
    }

    public void setNumeroBordereau(String numeroBordereau) {
        this.numeroBordereau = numeroBordereau;
    }

    public User getUtilisateur() {
        return this.utilisateur;
    }

    public void setUtilisateur(User user) {
        this.utilisateur = user;
    }

    public Affectation utilisateur(User user) {
        this.setUtilisateur(user);
        return this;
    }

    public Actif getActif() {
        return this.actif;
    }

    public void setActif(Actif actif) {
        this.actif = actif;
    }

    public Affectation actif(Actif actif) {
        this.setActif(actif);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Affectation)) {
            return false;
        }
        return getId() != null && getId().equals(((Affectation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Affectation{" +
            "id=" + getId() +
            ", dateAffectation='" + getDateAffectation() + "'" +
            ", dateRestitution='" + getDateRestitution() + "'" +
            ", numeroBordereau='" + getNumeroBordereau() + "'" +
            "}";
    }
}
