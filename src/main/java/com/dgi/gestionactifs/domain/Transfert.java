package com.dgi.gestionactifs.domain;

import com.dgi.gestionactifs.domain.enumeration.StatutTransfert;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Transfert.
 */
@Entity
@Table(name = "transfert")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Transfert implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date_demande", nullable = false)
    private LocalDate dateDemande;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private StatutTransfert statut;

    @Column(name = "commentaire_rejet")
    private String commentaireRejet;

    @Column(name = "date_traitement")
    private LocalDate dateTraitement;

    @ManyToOne(fetch = FetchType.LAZY)
    private User demandeur;

    @ManyToOne(fetch = FetchType.LAZY)
    private User validateur;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "affectations", "transferts", "maintenances" }, allowSetters = true)
    private Actif actif;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Transfert id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateDemande() {
        return this.dateDemande;
    }

    public Transfert dateDemande(LocalDate dateDemande) {
        this.setDateDemande(dateDemande);
        return this;
    }

    public void setDateDemande(LocalDate dateDemande) {
        this.dateDemande = dateDemande;
    }

    public StatutTransfert getStatut() {
        return this.statut;
    }

    public Transfert statut(StatutTransfert statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(StatutTransfert statut) {
        this.statut = statut;
    }

    public String getCommentaireRejet() {
        return this.commentaireRejet;
    }

    public Transfert commentaireRejet(String commentaireRejet) {
        this.setCommentaireRejet(commentaireRejet);
        return this;
    }

    public void setCommentaireRejet(String commentaireRejet) {
        this.commentaireRejet = commentaireRejet;
    }

    public LocalDate getDateTraitement() {
        return this.dateTraitement;
    }

    public Transfert dateTraitement(LocalDate dateTraitement) {
        this.setDateTraitement(dateTraitement);
        return this;
    }

    public void setDateTraitement(LocalDate dateTraitement) {
        this.dateTraitement = dateTraitement;
    }

    public User getDemandeur() {
        return this.demandeur;
    }

    public void setDemandeur(User user) {
        this.demandeur = user;
    }

    public Transfert demandeur(User user) {
        this.setDemandeur(user);
        return this;
    }

    public User getValidateur() {
        return this.validateur;
    }

    public void setValidateur(User user) {
        this.validateur = user;
    }

    public Transfert validateur(User user) {
        this.setValidateur(user);
        return this;
    }

    public Actif getActif() {
        return this.actif;
    }

    public void setActif(Actif actif) {
        this.actif = actif;
    }

    public Transfert actif(Actif actif) {
        this.setActif(actif);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transfert)) {
            return false;
        }
        return getId() != null && getId().equals(((Transfert) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Transfert{" +
            "id=" + getId() +
            ", dateDemande='" + getDateDemande() + "'" +
            ", statut='" + getStatut() + "'" +
            ", commentaireRejet='" + getCommentaireRejet() + "'" +
            ", dateTraitement='" + getDateTraitement() + "'" +
            "}";
    }
}
