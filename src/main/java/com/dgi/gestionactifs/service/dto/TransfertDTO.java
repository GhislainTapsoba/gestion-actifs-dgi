package com.dgi.gestionactifs.service.dto;

import com.dgi.gestionactifs.domain.enumeration.StatutTransfert;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.dgi.gestionactifs.domain.Transfert} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransfertDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate dateDemande;

    @NotNull
    private StatutTransfert statut;

    private String commentaireRejet;

    private LocalDate dateTraitement;

    private UserDTO demandeur;

    private UserDTO validateur;

    @NotNull
    private ActifDTO actif;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(LocalDate dateDemande) {
        this.dateDemande = dateDemande;
    }

    public StatutTransfert getStatut() {
        return statut;
    }

    public void setStatut(StatutTransfert statut) {
        this.statut = statut;
    }

    public String getCommentaireRejet() {
        return commentaireRejet;
    }

    public void setCommentaireRejet(String commentaireRejet) {
        this.commentaireRejet = commentaireRejet;
    }

    public LocalDate getDateTraitement() {
        return dateTraitement;
    }

    public void setDateTraitement(LocalDate dateTraitement) {
        this.dateTraitement = dateTraitement;
    }

    public UserDTO getDemandeur() {
        return demandeur;
    }

    public void setDemandeur(UserDTO demandeur) {
        this.demandeur = demandeur;
    }

    public UserDTO getValidateur() {
        return validateur;
    }

    public void setValidateur(UserDTO validateur) {
        this.validateur = validateur;
    }

    public ActifDTO getActif() {
        return actif;
    }

    public void setActif(ActifDTO actif) {
        this.actif = actif;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransfertDTO)) {
            return false;
        }

        TransfertDTO transfertDTO = (TransfertDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, transfertDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransfertDTO{" +
            "id=" + getId() +
            ", dateDemande='" + getDateDemande() + "'" +
            ", statut='" + getStatut() + "'" +
            ", commentaireRejet='" + getCommentaireRejet() + "'" +
            ", dateTraitement='" + getDateTraitement() + "'" +
            ", demandeur=" + getDemandeur() +
            ", validateur=" + getValidateur() +
            ", actif=" + getActif() +
            "}";
    }
}
