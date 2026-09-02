package com.dgi.gestionactifs.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.dgi.gestionactifs.domain.Affectation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AffectationDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate dateAffectation;

    private LocalDate dateRestitution;

    private String numeroBordereau;

    private UserDTO utilisateur;

    @NotNull
    private ActifDTO actif;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateAffectation() {
        return dateAffectation;
    }

    public void setDateAffectation(LocalDate dateAffectation) {
        this.dateAffectation = dateAffectation;
    }

    public LocalDate getDateRestitution() {
        return dateRestitution;
    }

    public void setDateRestitution(LocalDate dateRestitution) {
        this.dateRestitution = dateRestitution;
    }

    public String getNumeroBordereau() {
        return numeroBordereau;
    }

    public void setNumeroBordereau(String numeroBordereau) {
        this.numeroBordereau = numeroBordereau;
    }

    public UserDTO getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(UserDTO utilisateur) {
        this.utilisateur = utilisateur;
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
        if (!(o instanceof AffectationDTO)) {
            return false;
        }

        AffectationDTO affectationDTO = (AffectationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, affectationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AffectationDTO{" +
            "id=" + getId() +
            ", dateAffectation='" + getDateAffectation() + "'" +
            ", dateRestitution='" + getDateRestitution() + "'" +
            ", numeroBordereau='" + getNumeroBordereau() + "'" +
            ", utilisateur=" + getUtilisateur() +
            ", actif=" + getActif() +
            "}";
    }
}
