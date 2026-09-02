package com.dgi.gestionactifs.service.dto;

import com.dgi.gestionactifs.domain.enumeration.TypeContrat;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.dgi.gestionactifs.domain.Contrat} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContratDTO implements Serializable {

    private Long id;

    @NotNull
    private TypeContrat typeContrat;

    private String reference;

    private LocalDate dateDebut;

    @NotNull
    private LocalDate dateFin;

    private ActifDTO actif;

    @NotNull
    private FournisseurDTO fournisseur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeContrat getTypeContrat() {
        return typeContrat;
    }

    public void setTypeContrat(TypeContrat typeContrat) {
        this.typeContrat = typeContrat;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public ActifDTO getActif() {
        return actif;
    }

    public void setActif(ActifDTO actif) {
        this.actif = actif;
    }

    public FournisseurDTO getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(FournisseurDTO fournisseur) {
        this.fournisseur = fournisseur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContratDTO)) {
            return false;
        }

        ContratDTO contratDTO = (ContratDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contratDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContratDTO{" +
            "id=" + getId() +
            ", typeContrat='" + getTypeContrat() + "'" +
            ", reference='" + getReference() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", actif=" + getActif() +
            ", fournisseur=" + getFournisseur() +
            "}";
    }
}
