package com.dgi.gestionactifs.domain;

import com.dgi.gestionactifs.domain.enumeration.StatutBordereau;
import com.dgi.gestionactifs.domain.enumeration.TypeBordereau;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Bordereau.
 */
@Entity
@Table(name = "bordereau")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Bordereau implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "numero", nullable = false)
    private String numero;

    @NotNull
    @Column(name = "date_emission", nullable = false)
    private LocalDate dateEmission;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type_bordereau", nullable = false)
    private TypeBordereau typeBordereau;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "statut_validation", nullable = false)
    private StatutBordereau statutValidation;

    @Column(name = "date_validation")
    private LocalDate dateValidation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "serviceOrigine", "serviceDestinataire", "demandeur", "validateur" }, allowSetters = true)
    private Transfert transfert;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "agent" }, allowSetters = true)
    private Affectation affectation;

    @ManyToOne(optional = false)
    @NotNull
    private User emetteur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Bordereau id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return this.numero;
    }

    public Bordereau numero(String numero) {
        this.setNumero(numero);
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDate getDateEmission() {
        return this.dateEmission;
    }

    public Bordereau dateEmission(LocalDate dateEmission) {
        this.setDateEmission(dateEmission);
        return this;
    }

    public void setDateEmission(LocalDate dateEmission) {
        this.dateEmission = dateEmission;
    }

    public TypeBordereau getTypeBordereau() {
        return this.typeBordereau;
    }

    public Bordereau typeBordereau(TypeBordereau typeBordereau) {
        this.setTypeBordereau(typeBordereau);
        return this;
    }

    public void setTypeBordereau(TypeBordereau typeBordereau) {
        this.typeBordereau = typeBordereau;
    }

    public StatutBordereau getStatutValidation() {
        return this.statutValidation;
    }

    public Bordereau statutValidation(StatutBordereau statutValidation) {
        this.setStatutValidation(statutValidation);
        return this;
    }

    public void setStatutValidation(StatutBordereau statutValidation) {
        this.statutValidation = statutValidation;
    }

    public LocalDate getDateValidation() {
        return this.dateValidation;
    }

    public Bordereau dateValidation(LocalDate dateValidation) {
        this.setDateValidation(dateValidation);
        return this;
    }

    public void setDateValidation(LocalDate dateValidation) {
        this.dateValidation = dateValidation;
    }

    public Transfert getTransfert() {
        return this.transfert;
    }

    public void setTransfert(Transfert transfert) {
        this.transfert = transfert;
    }

    public Bordereau transfert(Transfert transfert) {
        this.setTransfert(transfert);
        return this;
    }

    public Affectation getAffectation() {
        return this.affectation;
    }

    public void setAffectation(Affectation affectation) {
        this.affectation = affectation;
    }

    public Bordereau affectation(Affectation affectation) {
        this.setAffectation(affectation);
        return this;
    }

    public User getEmetteur() {
        return this.emetteur;
    }

    public void setEmetteur(User user) {
        this.emetteur = user;
    }

    public Bordereau emetteur(User user) {
        this.setEmetteur(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bordereau)) {
            return false;
        }
        return getId() != null && getId().equals(((Bordereau) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Bordereau{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            ", dateEmission='" + getDateEmission() + "'" +
            ", typeBordereau='" + getTypeBordereau() + "'" +
            ", statutValidation='" + getStatutValidation() + "'" +
            ", dateValidation='" + getDateValidation() + "'" +
            "}";
    }
}
