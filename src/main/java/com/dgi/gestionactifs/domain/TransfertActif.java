package com.dgi.gestionactifs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TransfertActif.
 */
@Entity
@Table(name = "transfert_actif")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransfertActif implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "observation")
    private String observation;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "serviceOrigine", "serviceDestinataire", "demandeur", "validateur" }, allowSetters = true)
    private Transfert transfert;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "categorie" }, allowSetters = true)
    private Actif actif;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TransfertActif id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObservation() {
        return this.observation;
    }

    public TransfertActif observation(String observation) {
        this.setObservation(observation);
        return this;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Transfert getTransfert() {
        return this.transfert;
    }

    public void setTransfert(Transfert transfert) {
        this.transfert = transfert;
    }

    public TransfertActif transfert(Transfert transfert) {
        this.setTransfert(transfert);
        return this;
    }

    public Actif getActif() {
        return this.actif;
    }

    public void setActif(Actif actif) {
        this.actif = actif;
    }

    public TransfertActif actif(Actif actif) {
        this.setActif(actif);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransfertActif)) {
            return false;
        }
        return getId() != null && getId().equals(((TransfertActif) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransfertActif{" +
            "id=" + getId() +
            ", observation='" + getObservation() + "'" +
            "}";
    }
}
