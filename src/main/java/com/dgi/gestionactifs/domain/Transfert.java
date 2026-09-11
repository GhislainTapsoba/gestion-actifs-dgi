package com.dgi.gestionactifs.domain;

import com.dgi.gestionactifs.domain.enumeration.StatutTransfert;
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
    @Column(name = "date_transfert", nullable = false)
    private LocalDate dateTransfert;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private StatutTransfert statut;

    @Column(name = "commentaire_rejet")
    private String commentaireRejet;

    @Column(name = "date_traitement")
    private LocalDate dateTraitement;

    @ManyToOne(optional = false)
    @NotNull
    private ServiceDgi serviceOrigine;

    @ManyToOne(optional = false)
    @NotNull
    private ServiceDgi serviceDestinataire;

    @ManyToOne(fetch = FetchType.LAZY)
    private User demandeur;

    @ManyToOne(fetch = FetchType.LAZY)
    private User validateur;

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

    public LocalDate getDateTransfert() {
        return this.dateTransfert;
    }

    public Transfert dateTransfert(LocalDate dateTransfert) {
        this.setDateTransfert(dateTransfert);
        return this;
    }

    public void setDateTransfert(LocalDate dateTransfert) {
        this.dateTransfert = dateTransfert;
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

    public ServiceDgi getServiceOrigine() {
        return this.serviceOrigine;
    }

    public void setServiceOrigine(ServiceDgi serviceDgi) {
        this.serviceOrigine = serviceDgi;
    }

    public Transfert serviceOrigine(ServiceDgi serviceDgi) {
        this.setServiceOrigine(serviceDgi);
        return this;
    }

    public ServiceDgi getServiceDestinataire() {
        return this.serviceDestinataire;
    }

    public void setServiceDestinataire(ServiceDgi serviceDgi) {
        this.serviceDestinataire = serviceDgi;
    }

    public Transfert serviceDestinataire(ServiceDgi serviceDgi) {
        this.setServiceDestinataire(serviceDgi);
        return this;
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
            ", dateTransfert='" + getDateTransfert() + "'" +
            ", statut='" + getStatut() + "'" +
            ", commentaireRejet='" + getCommentaireRejet() + "'" +
            ", dateTraitement='" + getDateTraitement() + "'" +
            "}";
    }
}
