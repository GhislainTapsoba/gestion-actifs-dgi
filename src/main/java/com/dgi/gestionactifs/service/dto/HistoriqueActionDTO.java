package com.dgi.gestionactifs.service.dto;

import com.dgi.gestionactifs.domain.enumeration.TypeMouvement;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.dgi.gestionactifs.domain.HistoriqueAction} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistoriqueActionDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime dateAction;

    @NotNull
    private TypeMouvement typeAction;

    private String entiteCiblee;

    private String ancienneValeur;

    private String nouvelleValeur;

    @NotNull
    private UserDTO utilisateur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateAction() {
        return dateAction;
    }

    public void setDateAction(ZonedDateTime dateAction) {
        this.dateAction = dateAction;
    }

    public TypeMouvement getTypeAction() {
        return typeAction;
    }

    public void setTypeAction(TypeMouvement typeAction) {
        this.typeAction = typeAction;
    }

    public String getEntiteCiblee() {
        return entiteCiblee;
    }

    public void setEntiteCiblee(String entiteCiblee) {
        this.entiteCiblee = entiteCiblee;
    }

    public String getAncienneValeur() {
        return ancienneValeur;
    }

    public void setAncienneValeur(String ancienneValeur) {
        this.ancienneValeur = ancienneValeur;
    }

    public String getNouvelleValeur() {
        return nouvelleValeur;
    }

    public void setNouvelleValeur(String nouvelleValeur) {
        this.nouvelleValeur = nouvelleValeur;
    }

    public UserDTO getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(UserDTO utilisateur) {
        this.utilisateur = utilisateur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HistoriqueActionDTO)) {
            return false;
        }

        HistoriqueActionDTO historiqueActionDTO = (HistoriqueActionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, historiqueActionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoriqueActionDTO{" +
            "id=" + getId() +
            ", dateAction='" + getDateAction() + "'" +
            ", typeAction='" + getTypeAction() + "'" +
            ", entiteCiblee='" + getEntiteCiblee() + "'" +
            ", ancienneValeur='" + getAncienneValeur() + "'" +
            ", nouvelleValeur='" + getNouvelleValeur() + "'" +
            ", utilisateur=" + getUtilisateur() +
            "}";
    }
}
