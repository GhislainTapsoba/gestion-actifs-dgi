package com.dgi.gestionactifs.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.dgi.gestionactifs.domain.Affectation} entity. This class is used
 * in {@link com.dgi.gestionactifs.web.rest.AffectationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /affectations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AffectationCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter dateAffectation;

    private LocalDateFilter dateRestitution;

    private StringFilter numeroBordereau;

    private LongFilter utilisateurId;

    private LongFilter actifId;

    private Boolean distinct;

    public AffectationCriteria() {}

    public AffectationCriteria(AffectationCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.dateAffectation = other.optionalDateAffectation().map(LocalDateFilter::copy).orElse(null);
        this.dateRestitution = other.optionalDateRestitution().map(LocalDateFilter::copy).orElse(null);
        this.numeroBordereau = other.optionalNumeroBordereau().map(StringFilter::copy).orElse(null);
        this.utilisateurId = other.optionalUtilisateurId().map(LongFilter::copy).orElse(null);
        this.actifId = other.optionalActifId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AffectationCriteria copy() {
        return new AffectationCriteria(this);
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

    public LocalDateFilter getDateAffectation() {
        return dateAffectation;
    }

    public Optional<LocalDateFilter> optionalDateAffectation() {
        return Optional.ofNullable(dateAffectation);
    }

    public LocalDateFilter dateAffectation() {
        if (dateAffectation == null) {
            setDateAffectation(new LocalDateFilter());
        }
        return dateAffectation;
    }

    public void setDateAffectation(LocalDateFilter dateAffectation) {
        this.dateAffectation = dateAffectation;
    }

    public LocalDateFilter getDateRestitution() {
        return dateRestitution;
    }

    public Optional<LocalDateFilter> optionalDateRestitution() {
        return Optional.ofNullable(dateRestitution);
    }

    public LocalDateFilter dateRestitution() {
        if (dateRestitution == null) {
            setDateRestitution(new LocalDateFilter());
        }
        return dateRestitution;
    }

    public void setDateRestitution(LocalDateFilter dateRestitution) {
        this.dateRestitution = dateRestitution;
    }

    public StringFilter getNumeroBordereau() {
        return numeroBordereau;
    }

    public Optional<StringFilter> optionalNumeroBordereau() {
        return Optional.ofNullable(numeroBordereau);
    }

    public StringFilter numeroBordereau() {
        if (numeroBordereau == null) {
            setNumeroBordereau(new StringFilter());
        }
        return numeroBordereau;
    }

    public void setNumeroBordereau(StringFilter numeroBordereau) {
        this.numeroBordereau = numeroBordereau;
    }

    public LongFilter getUtilisateurId() {
        return utilisateurId;
    }

    public Optional<LongFilter> optionalUtilisateurId() {
        return Optional.ofNullable(utilisateurId);
    }

    public LongFilter utilisateurId() {
        if (utilisateurId == null) {
            setUtilisateurId(new LongFilter());
        }
        return utilisateurId;
    }

    public void setUtilisateurId(LongFilter utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public LongFilter getActifId() {
        return actifId;
    }

    public Optional<LongFilter> optionalActifId() {
        return Optional.ofNullable(actifId);
    }

    public LongFilter actifId() {
        if (actifId == null) {
            setActifId(new LongFilter());
        }
        return actifId;
    }

    public void setActifId(LongFilter actifId) {
        this.actifId = actifId;
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
        final AffectationCriteria that = (AffectationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dateAffectation, that.dateAffectation) &&
            Objects.equals(dateRestitution, that.dateRestitution) &&
            Objects.equals(numeroBordereau, that.numeroBordereau) &&
            Objects.equals(utilisateurId, that.utilisateurId) &&
            Objects.equals(actifId, that.actifId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateAffectation, dateRestitution, numeroBordereau, utilisateurId, actifId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AffectationCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDateAffectation().map(f -> "dateAffectation=" + f + ", ").orElse("") +
            optionalDateRestitution().map(f -> "dateRestitution=" + f + ", ").orElse("") +
            optionalNumeroBordereau().map(f -> "numeroBordereau=" + f + ", ").orElse("") +
            optionalUtilisateurId().map(f -> "utilisateurId=" + f + ", ").orElse("") +
            optionalActifId().map(f -> "actifId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
