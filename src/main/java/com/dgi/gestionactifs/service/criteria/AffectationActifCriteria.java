package com.dgi.gestionactifs.service.criteria;

import com.dgi.gestionactifs.domain.enumeration.StatutAffectation;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.dgi.gestionactifs.domain.AffectationActif} entity. This class is used
 * in {@link com.dgi.gestionactifs.web.rest.AffectationActifResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /affectation-actifs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AffectationActifCriteria implements Serializable, Criteria {

    /**
     * Class for filtering StatutAffectation
     */
    public static class StatutAffectationFilter extends Filter<StatutAffectation> {

        public StatutAffectationFilter() {}

        public StatutAffectationFilter(StatutAffectationFilter filter) {
            super(filter);
        }

        @Override
        public StatutAffectationFilter copy() {
            return new StatutAffectationFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter observation;

    private StatutAffectationFilter statut;

    private LongFilter affectationId;

    private LongFilter actifId;

    private Boolean distinct;

    public AffectationActifCriteria() {}

    public AffectationActifCriteria(AffectationActifCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.observation = other.optionalObservation().map(StringFilter::copy).orElse(null);
        this.statut = other.optionalStatut().map(StatutAffectationFilter::copy).orElse(null);
        this.affectationId = other.optionalAffectationId().map(LongFilter::copy).orElse(null);
        this.actifId = other.optionalActifId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AffectationActifCriteria copy() {
        return new AffectationActifCriteria(this);
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

    public StringFilter getObservation() {
        return observation;
    }

    public Optional<StringFilter> optionalObservation() {
        return Optional.ofNullable(observation);
    }

    public StringFilter observation() {
        if (observation == null) {
            setObservation(new StringFilter());
        }
        return observation;
    }

    public void setObservation(StringFilter observation) {
        this.observation = observation;
    }

    public StatutAffectationFilter getStatut() {
        return statut;
    }

    public Optional<StatutAffectationFilter> optionalStatut() {
        return Optional.ofNullable(statut);
    }

    public StatutAffectationFilter statut() {
        if (statut == null) {
            setStatut(new StatutAffectationFilter());
        }
        return statut;
    }

    public void setStatut(StatutAffectationFilter statut) {
        this.statut = statut;
    }

    public LongFilter getAffectationId() {
        return affectationId;
    }

    public Optional<LongFilter> optionalAffectationId() {
        return Optional.ofNullable(affectationId);
    }

    public LongFilter affectationId() {
        if (affectationId == null) {
            setAffectationId(new LongFilter());
        }
        return affectationId;
    }

    public void setAffectationId(LongFilter affectationId) {
        this.affectationId = affectationId;
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
        final AffectationActifCriteria that = (AffectationActifCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(observation, that.observation) &&
            Objects.equals(statut, that.statut) &&
            Objects.equals(affectationId, that.affectationId) &&
            Objects.equals(actifId, that.actifId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, observation, statut, affectationId, actifId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AffectationActifCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalObservation().map(f -> "observation=" + f + ", ").orElse("") +
            optionalStatut().map(f -> "statut=" + f + ", ").orElse("") +
            optionalAffectationId().map(f -> "affectationId=" + f + ", ").orElse("") +
            optionalActifId().map(f -> "actifId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
