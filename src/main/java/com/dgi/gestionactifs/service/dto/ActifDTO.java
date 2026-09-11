package com.dgi.gestionactifs.service.dto;

import com.dgi.gestionactifs.domain.enumeration.StatutActif;
import com.dgi.gestionactifs.domain.enumeration.TypeActif;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.dgi.gestionactifs.domain.Actif} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ActifDTO implements Serializable {

    private Long id;

    @NotNull
    private String codeInventaire;

    @NotNull
    private String designation;

    private String marque;

    private String modele;

    private String numeroSerie;

    private String codeBarre;

    @NotNull
    private TypeActif type;

    @NotNull
    private StatutActif etat;

    private String localisation;

    private LocalDate dateAcquisition;

    private Double valeurAcquisition;

    @NotNull
    private CategorieMaterielDTO categorie;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeInventaire() {
        return codeInventaire;
    }

    public void setCodeInventaire(String codeInventaire) {
        this.codeInventaire = codeInventaire;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getCodeBarre() {
        return codeBarre;
    }

    public void setCodeBarre(String codeBarre) {
        this.codeBarre = codeBarre;
    }

    public TypeActif getType() {
        return type;
    }

    public void setType(TypeActif type) {
        this.type = type;
    }

    public StatutActif getEtat() {
        return etat;
    }

    public void setEtat(StatutActif etat) {
        this.etat = etat;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public LocalDate getDateAcquisition() {
        return dateAcquisition;
    }

    public void setDateAcquisition(LocalDate dateAcquisition) {
        this.dateAcquisition = dateAcquisition;
    }

    public Double getValeurAcquisition() {
        return valeurAcquisition;
    }

    public void setValeurAcquisition(Double valeurAcquisition) {
        this.valeurAcquisition = valeurAcquisition;
    }

    public CategorieMaterielDTO getCategorie() {
        return categorie;
    }

    public void setCategorie(CategorieMaterielDTO categorie) {
        this.categorie = categorie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ActifDTO)) {
            return false;
        }

        ActifDTO actifDTO = (ActifDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, actifDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ActifDTO{" +
            "id=" + getId() +
            ", codeInventaire='" + getCodeInventaire() + "'" +
            ", designation='" + getDesignation() + "'" +
            ", marque='" + getMarque() + "'" +
            ", modele='" + getModele() + "'" +
            ", numeroSerie='" + getNumeroSerie() + "'" +
            ", codeBarre='" + getCodeBarre() + "'" +
            ", type='" + getType() + "'" +
            ", etat='" + getEtat() + "'" +
            ", localisation='" + getLocalisation() + "'" +
            ", dateAcquisition='" + getDateAcquisition() + "'" +
            ", valeurAcquisition=" + getValeurAcquisition() +
            ", categorie=" + getCategorie() +
            "}";
    }
}
