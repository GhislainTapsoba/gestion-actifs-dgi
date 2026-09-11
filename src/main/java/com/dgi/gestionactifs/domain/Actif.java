package com.dgi.gestionactifs.domain;

import com.dgi.gestionactifs.domain.enumeration.StatutActif;
import com.dgi.gestionactifs.domain.enumeration.TypeActif;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Actif.
 */
@Entity
@Table(name = "actif")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Actif implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "code_inventaire", nullable = false, unique = true)
    private String codeInventaire;

    @NotNull
    @Column(name = "designation", nullable = false)
    private String designation;

    @Column(name = "marque")
    private String marque;

    @Column(name = "modele")
    private String modele;

    @Column(name = "numero_serie")
    private String numeroSerie;

    @Column(name = "code_barre")
    private String codeBarre;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TypeActif type;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "etat", nullable = false)
    private StatutActif etat;

    @Column(name = "localisation")
    private String localisation;

    @Column(name = "date_acquisition")
    private LocalDate dateAcquisition;

    @Column(name = "valeur_acquisition")
    private Double valeurAcquisition;

    @ManyToOne(optional = false)
    @NotNull
    private CategorieMateriel categorie;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Actif id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeInventaire() {
        return this.codeInventaire;
    }

    public Actif codeInventaire(String codeInventaire) {
        this.setCodeInventaire(codeInventaire);
        return this;
    }

    public void setCodeInventaire(String codeInventaire) {
        this.codeInventaire = codeInventaire;
    }

    public String getDesignation() {
        return this.designation;
    }

    public Actif designation(String designation) {
        this.setDesignation(designation);
        return this;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getMarque() {
        return this.marque;
    }

    public Actif marque(String marque) {
        this.setMarque(marque);
        return this;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getModele() {
        return this.modele;
    }

    public Actif modele(String modele) {
        this.setModele(modele);
        return this;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public String getNumeroSerie() {
        return this.numeroSerie;
    }

    public Actif numeroSerie(String numeroSerie) {
        this.setNumeroSerie(numeroSerie);
        return this;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getCodeBarre() {
        return this.codeBarre;
    }

    public Actif codeBarre(String codeBarre) {
        this.setCodeBarre(codeBarre);
        return this;
    }

    public void setCodeBarre(String codeBarre) {
        this.codeBarre = codeBarre;
    }

    public TypeActif getType() {
        return this.type;
    }

    public Actif type(TypeActif type) {
        this.setType(type);
        return this;
    }

    public void setType(TypeActif type) {
        this.type = type;
    }

    public StatutActif getEtat() {
        return this.etat;
    }

    public Actif etat(StatutActif etat) {
        this.setEtat(etat);
        return this;
    }

    public void setEtat(StatutActif etat) {
        this.etat = etat;
    }

    public String getLocalisation() {
        return this.localisation;
    }

    public Actif localisation(String localisation) {
        this.setLocalisation(localisation);
        return this;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public LocalDate getDateAcquisition() {
        return this.dateAcquisition;
    }

    public Actif dateAcquisition(LocalDate dateAcquisition) {
        this.setDateAcquisition(dateAcquisition);
        return this;
    }

    public void setDateAcquisition(LocalDate dateAcquisition) {
        this.dateAcquisition = dateAcquisition;
    }

    public Double getValeurAcquisition() {
        return this.valeurAcquisition;
    }

    public Actif valeurAcquisition(Double valeurAcquisition) {
        this.setValeurAcquisition(valeurAcquisition);
        return this;
    }

    public void setValeurAcquisition(Double valeurAcquisition) {
        this.valeurAcquisition = valeurAcquisition;
    }

    public CategorieMateriel getCategorie() {
        return this.categorie;
    }

    public void setCategorie(CategorieMateriel categorieMateriel) {
        this.categorie = categorieMateriel;
    }

    public Actif categorie(CategorieMateriel categorieMateriel) {
        this.setCategorie(categorieMateriel);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Actif)) {
            return false;
        }
        return getId() != null && getId().equals(((Actif) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Actif{" +
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
            "}";
    }
}
