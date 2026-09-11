package com.dgi.gestionactifs.service.criteria;

import com.dgi.gestionactifs.domain.enumeration.StatutActif;
import com.dgi.gestionactifs.domain.enumeration.TypeActif;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.dgi.gestionactifs.domain.Actif} entity. This class is used
 * in {@link com.dgi.gestionactifs.web.rest.ActifResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /actifs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ActifCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TypeActif
     */
    public static class TypeActifFilter extends Filter<TypeActif> {

        public TypeActifFilter() {}

        public TypeActifFilter(TypeActifFilter filter) {
            super(filter);
        }

        @Override
        public TypeActifFilter copy() {
            return new TypeActifFilter(this);
        }
    }

    /**
     * Class for filtering StatutActif
     */
    public static class StatutActifFilter extends Filter<StatutActif> {

        public StatutActifFilter() {}

        public StatutActifFilter(StatutActifFilter filter) {
            super(filter);
        }

        @Override
        public StatutActifFilter copy() {
            return new StatutActifFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter codeInventaire;

    private StringFilter designation;

    private StringFilter marque;

    private StringFilter modele;

    private StringFilter numeroSerie;

    private StringFilter codeBarre;

    private TypeActifFilter type;

    private StatutActifFilter etat;

    private StringFilter localisation;

    private LocalDateFilter dateAcquisition;

    private DoubleFilter valeurAcquisition;

    private LongFilter categorieId;

    private Boolean distinct;

    public ActifCriteria() {}

    public ActifCriteria(ActifCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.codeInventaire = other.optionalCodeInventaire().map(StringFilter::copy).orElse(null);
        this.designation = other.optionalDesignation().map(StringFilter::copy).orElse(null);
        this.marque = other.optionalMarque().map(StringFilter::copy).orElse(null);
        this.modele = other.optionalModele().map(StringFilter::copy).orElse(null);
        this.numeroSerie = other.optionalNumeroSerie().map(StringFilter::copy).orElse(null);
        this.codeBarre = other.optionalCodeBarre().map(StringFilter::copy).orElse(null);
        this.type = other.optionalType().map(TypeActifFilter::copy).orElse(null);
        this.etat = other.optionalEtat().map(StatutActifFilter::copy).orElse(null);
        this.localisation = other.optionalLocalisation().map(StringFilter::copy).orElse(null);
        this.dateAcquisition = other.optionalDateAcquisition().map(LocalDateFilter::copy).orElse(null);
        this.valeurAcquisition = other.optionalValeurAcquisition().map(DoubleFilter::copy).orElse(null);
        this.categorieId = other.optionalCategorieId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ActifCriteria copy() {
        return new ActifCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCodeInventaire() {
        return codeInventaire;
    }

    public Optional<StringFilter> optionalCodeInventaire() {
        return Optional.ofNullable(codeInventaire);
    }

    public StringFilter codeInventaire() {
        if (codeInventaire == null) {
            setCodeInventaire(new StringFilter());
        }
        return codeInventaire;
    }

    public void setCodeInventaire(StringFilter codeInventaire) {
        this.codeInventaire = codeInventaire;
    }

    public StringFilter getDesignation() {
        return designation;
    }

    public Optional<StringFilter> optionalDesignation() {
        return Optional.ofNullable(designation);
    }

    public StringFilter designation() {
        if (designation == null) {
            setDesignation(new StringFilter());
        }
        return designation;
    }

    public void setDesignation(StringFilter designation) {
        this.designation = designation;
    }

    public StringFilter getMarque() {
        return marque;
    }

    public Optional<StringFilter> optionalMarque() {
        return Optional.ofNullable(marque);
    }

    public StringFilter marque() {
        if (marque == null) {
            setMarque(new StringFilter());
        }
        return marque;
    }

    public void setMarque(StringFilter marque) {
        this.marque = marque;
    }

    public StringFilter getModele() {
        return modele;
    }

    public Optional<StringFilter> optionalModele() {
        return Optional.ofNullable(modele);
    }

    public StringFilter modele() {
        if (modele == null) {
            setModele(new StringFilter());
        }
        return modele;
    }

    public void setModele(StringFilter modele) {
        this.modele = modele;
    }

    public StringFilter getNumeroSerie() {
        return numeroSerie;
    }

    public Optional<StringFilter> optionalNumeroSerie() {
        return Optional.ofNullable(numeroSerie);
    }

    public StringFilter numeroSerie() {
        if (numeroSerie == null) {
            setNumeroSerie(new StringFilter());
        }
        return numeroSerie;
    }

    public void setNumeroSerie(StringFilter numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public StringFilter getCodeBarre() {
        return codeBarre;
    }

    public Optional<StringFilter> optionalCodeBarre() {
        return Optional.ofNullable(codeBarre);
    }

    public StringFilter codeBarre() {
        if (codeBarre == null) {
            setCodeBarre(new StringFilter());
        }
        return codeBarre;
    }

    public void setCodeBarre(StringFilter codeBarre) {
        this.codeBarre = codeBarre;
    }

    public TypeActifFilter getType() {
        return type;
    }

    public Optional<TypeActifFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public TypeActifFilter type() {
        if (type == null) {
            setType(new TypeActifFilter());
        }
        return type;
    }

    public void setType(TypeActifFilter type) {
        this.type = type;
    }

    public StatutActifFilter getEtat() {
        return etat;
    }

    public Optional<StatutActifFilter> optionalEtat() {
        return Optional.ofNullable(etat);
    }

    public StatutActifFilter etat() {
        if (etat == null) {
            setEtat(new StatutActifFilter());
        }
        return etat;
    }

    public void setEtat(StatutActifFilter etat) {
        this.etat = etat;
    }

    public StringFilter getLocalisation() {
        return localisation;
    }

    public Optional<StringFilter> optionalLocalisation() {
        return Optional.ofNullable(localisation);
    }

    public StringFilter localisation() {
        if (localisation == null) {
            setLocalisation(new StringFilter());
        }
        return localisation;
    }

    public void setLocalisation(StringFilter localisation) {
        this.localisation = localisation;
    }

    public LocalDateFilter getDateAcquisition() {
        return dateAcquisition;
    }

    public Optional<LocalDateFilter> optionalDateAcquisition() {
        return Optional.ofNullable(dateAcquisition);
    }

    public LocalDateFilter dateAcquisition() {
        if (dateAcquisition == null) {
            setDateAcquisition(new LocalDateFilter());
        }
        return dateAcquisition;
    }

    public void setDateAcquisition(LocalDateFilter dateAcquisition) {
        this.dateAcquisition = dateAcquisition;
    }

    public DoubleFilter getValeurAcquisition() {
        return valeurAcquisition;
    }

    public Optional<DoubleFilter> optionalValeurAcquisition() {
        return Optional.ofNullable(valeurAcquisition);
    }

    public DoubleFilter valeurAcquisition() {
        if (valeurAcquisition == null) {
            setValeurAcquisition(new DoubleFilter());
        }
        return valeurAcquisition;
    }

    public void setValeurAcquisition(DoubleFilter valeurAcquisition) {
        this.valeurAcquisition = valeurAcquisition;
    }

    public LongFilter getCategorieId() {
        return categorieId;
    }

    public Optional<LongFilter> optionalCategorieId() {
        return Optional.ofNullable(categorieId);
    }

    public LongFilter categorieId() {
        if (categorieId == null) {
            setCategorieId(new LongFilter());
        }
        return categorieId;
    }

    public void setCategorieId(LongFilter categorieId) {
        this.categorieId = categorieId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ActifCriteria that = (ActifCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(codeInventaire, that.codeInventaire) &&
            Objects.equals(designation, that.designation) &&
            Objects.equals(marque, that.marque) &&
            Objects.equals(modele, that.modele) &&
            Objects.equals(numeroSerie, that.numeroSerie) &&
            Objects.equals(codeBarre, that.codeBarre) &&
            Objects.equals(type, that.type) &&
            Objects.equals(etat, that.etat) &&
            Objects.equals(localisation, that.localisation) &&
            Objects.equals(dateAcquisition, that.dateAcquisition) &&
            Objects.equals(valeurAcquisition, that.valeurAcquisition) &&
            Objects.equals(categorieId, that.categorieId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            codeInventaire,
            designation,
            marque,
            modele,
            numeroSerie,
            codeBarre,
            type,
            etat,
            localisation,
            dateAcquisition,
            valeurAcquisition,
            categorieId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ActifCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCodeInventaire().map(f -> "codeInventaire=" + f + ", ").orElse("") +
            optionalDesignation().map(f -> "designation=" + f + ", ").orElse("") +
            optionalMarque().map(f -> "marque=" + f + ", ").orElse("") +
            optionalModele().map(f -> "modele=" + f + ", ").orElse("") +
            optionalNumeroSerie().map(f -> "numeroSerie=" + f + ", ").orElse("") +
            optionalCodeBarre().map(f -> "codeBarre=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalEtat().map(f -> "etat=" + f + ", ").orElse("") +
            optionalLocalisation().map(f -> "localisation=" + f + ", ").orElse("") +
            optionalDateAcquisition().map(f -> "dateAcquisition=" + f + ", ").orElse("") +
            optionalValeurAcquisition().map(f -> "valeurAcquisition=" + f + ", ").orElse("") +
            optionalCategorieId().map(f -> "categorieId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
