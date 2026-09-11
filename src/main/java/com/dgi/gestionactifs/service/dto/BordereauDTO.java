package com.dgi.gestionactifs.service.dto;

import com.dgi.gestionactifs.domain.enumeration.StatutBordereau;
import com.dgi.gestionactifs.domain.enumeration.TypeBordereau;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.dgi.gestionactifs.domain.Bordereau} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BordereauDTO implements Serializable {

    private Long id;

    @NotNull
    private String numero;

    @NotNull
    private LocalDate dateEmission;

    @NotNull
    private TypeBordereau typeBordereau;

    @NotNull
    private StatutBordereau statutValidation;

    private LocalDate dateValidation;

    private TransfertDTO transfert;

    private AffectationDTO affectation;

    @NotNull
    private UserDTO emetteur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDate getDateEmission() {
        return dateEmission;
    }

    public void setDateEmission(LocalDate dateEmission) {
        this.dateEmission = dateEmission;
    }

    public TypeBordereau getTypeBordereau() {
        return typeBordereau;
    }

    public void setTypeBordereau(TypeBordereau typeBordereau) {
        this.typeBordereau = typeBordereau;
    }

    public StatutBordereau getStatutValidation() {
        return statutValidation;
    }

    public void setStatutValidation(StatutBordereau statutValidation) {
        this.statutValidation = statutValidation;
    }

    public LocalDate getDateValidation() {
        return dateValidation;
    }

    public void setDateValidation(LocalDate dateValidation) {
        this.dateValidation = dateValidation;
    }

    public TransfertDTO getTransfert() {
        return transfert;
    }

    public void setTransfert(TransfertDTO transfert) {
        this.transfert = transfert;
    }

    public AffectationDTO getAffectation() {
        return affectation;
    }

    public void setAffectation(AffectationDTO affectation) {
        this.affectation = affectation;
    }

    public UserDTO getEmetteur() {
        return emetteur;
    }

    public void setEmetteur(UserDTO emetteur) {
        this.emetteur = emetteur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BordereauDTO)) {
            return false;
        }

        BordereauDTO bordereauDTO = (BordereauDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, bordereauDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BordereauDTO{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            ", dateEmission='" + getDateEmission() + "'" +
            ", typeBordereau='" + getTypeBordereau() + "'" +
            ", statutValidation='" + getStatutValidation() + "'" +
            ", dateValidation='" + getDateValidation() + "'" +
            ", transfert=" + getTransfert() +
            ", affectation=" + getAffectation() +
            ", emetteur=" + getEmetteur() +
            "}";
    }
}
