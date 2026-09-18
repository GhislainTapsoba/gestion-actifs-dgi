package com.dgi.gestionactifs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import org.springframework.data.domain.Persistable;

/**
 * A Rapport.
 */
@Entity
@Table(name = "rapport")
@JsonIgnoreProperties(value = { "new", "id" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Rapport implements Serializable, Persistable<Long> {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rapportSequence")
    @SequenceGenerator(name = "rapportSequence", sequenceName = "rapport_seq")
    private Long id;

    @NotNull
    @Column(name = "titre", nullable = false)
    private String titre;

    @NotNull
    @Column(name = "type_rapport", nullable = false)
    private String typeRapport;

    @Column(name = "description")
    private String description;

    @Column(name = "chemin_fichier")
    private String cheminFichier;

    @Column(name = "date_generation")
    private Instant dateGeneration;

    @Column(name = "genere_par")
    private String generePar;

    @Column(name = "format_export")
    private String formatExport;

    @Column(name = "parametres")
    private String parametres;

    @org.springframework.data.annotation.Transient
    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Rapport id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return this.titre;
    }

    public Rapport titre(String titre) {
        this.setTitre(titre);
        return this;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getTypeRapport() {
        return this.typeRapport;
    }

    public Rapport typeRapport(String typeRapport) {
        this.setTypeRapport(typeRapport);
        return this;
    }

    public void setTypeRapport(String typeRapport) {
        this.typeRapport = typeRapport;
    }

    public String getDescription() {
        return this.description;
    }

    public Rapport description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCheminFichier() {
        return this.cheminFichier;
    }

    public Rapport cheminFichier(String cheminFichier) {
        this.setCheminFichier(cheminFichier);
        return this;
    }

    public void setCheminFichier(String cheminFichier) {
        this.cheminFichier = cheminFichier;
    }

    public Instant getDateGeneration() {
        return this.dateGeneration;
    }

    public Rapport dateGeneration(Instant dateGeneration) {
        this.setDateGeneration(dateGeneration);
        return this;
    }

    public void setDateGeneration(Instant dateGeneration) {
        this.dateGeneration = dateGeneration;
    }

    public String getGenerePar() {
        return this.generePar;
    }

    public Rapport generePar(String generePar) {
        this.setGenerePar(generePar);
        return this;
    }

    public void setGenerePar(String generePar) {
        this.generePar = generePar;
    }

    public String getFormatExport() {
        return this.formatExport;
    }

    public Rapport formatExport(String formatExport) {
        this.setFormatExport(formatExport);
        return this;
    }

    public void setFormatExport(String formatExport) {
        this.formatExport = formatExport;
    }

    public String getParametres() {
        return this.parametres;
    }

    public Rapport parametres(String parametres) {
        this.setParametres(parametres);
        return this;
    }

    public void setParametres(String parametres) {
        this.parametres = parametres;
    }

    @PostLoad
    @PostPersist
    public void updateEntityState() {
        this.setIsPersisted();
    }

    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Rapport setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rapport)) {
            return false;
        }
        return getId() != null && getId().equals(((Rapport) o).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Rapport{" +
            "id=" + getId() +
            ", titre='" + getTitre() + "'" +
            ", typeRapport='" + getTypeRapport() + "'" +
            ", description='" + getDescription() + "'" +
            ", cheminFichier='" + getCheminFichier() + "'" +
            ", dateGeneration='" + getDateGeneration() + "'" +
            ", generePar='" + getGenerePar() + "'" +
            ", formatExport='" + getFormatExport() + "'" +
            ", parametres='" + getParametres() + "'" +
            "}";
    }
}
