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
    private LocalDate dateTransfert;

    @NotNull
    private StatutTransfert statut;

    private String commentaireRejet;

    private LocalDate dateTraitement;

    @NotNull
    private ServiceDgiDTO serviceOrigine;

    @NotNull
    private ServiceDgiDTO serviceDestinataire;

    private UserDTO demandeur;

    private UserDTO validateur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateTransfert() {
        return dateTransfert;
    }

    public void setDateTransfert(LocalDate dateTransfert) {
        this.dateTransfert = dateTransfert;
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

    public ServiceDgiDTO getServiceOrigine() {
        return serviceOrigine;
    }

    public void setServiceOrigine(ServiceDgiDTO serviceOrigine) {
        this.serviceOrigine = serviceOrigine;
    }

    public ServiceDgiDTO getServiceDestinataire() {
        return serviceDestinataire;
    }

    public void setServiceDestinataire(ServiceDgiDTO serviceDestinataire) {
        this.serviceDestinataire = serviceDestinataire;
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
            ", dateTransfert='" + getDateTransfert() + "'" +
            ", statut='" + getStatut() + "'" +
            ", commentaireRejet='" + getCommentaireRejet() + "'" +
            ", dateTraitement='" + getDateTraitement() + "'" +
            ", serviceOrigine=" + getServiceOrigine() +
            ", serviceDestinataire=" + getServiceDestinataire() +
            ", demandeur=" + getDemandeur() +
            ", validateur=" + getValidateur() +
            "}";
    }
}
