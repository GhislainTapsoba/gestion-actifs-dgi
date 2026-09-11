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

    private StringFilter motif;

    private LocalDateFilter dateRestitution;

    private LongFilter agentId;

    private Boolean distinct;

    public AffectationCriteria() {}

    public AffectationCriteria(AffectationCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.dateAffectation = other.optionalDateAffectation().map(LocalDateFilter::copy).orElse(null);
        this.motif = other.optionalMotif().map(StringFilter::copy).orElse(null);
        this.dateRestitution = other.optionalDateRestitution().map(LocalDateFilter::copy).orElse(null);
        this.agentId = other.optionalAgentId().map(LongFilter::copy).orElse(null);
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

    public StringFilter getMotif() {
        return motif;
    }

    public Optional<StringFilter> optionalMotif() {
        return Optional.ofNullable(motif);
    }

    public StringFilter motif() {
        if (motif == null) {
            setMotif(new StringFilter());
        }
        return motif;
    }

    public void setMotif(StringFilter motif) {
        this.motif = motif;
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

    public LongFilter getAgentId() {
        return agentId;
    }

    public Optional<LongFilter> optionalAgentId() {
        return Optional.ofNullable(agentId);
    }

    public LongFilter agentId() {
        if (agentId == null) {
            setAgentId(new LongFilter());
        }
        return agentId;
    }

    public void setAgentId(LongFilter agentId) {
        this.agentId = agentId;
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
            Objects.equals(motif, that.motif) &&
            Objects.equals(dateRestitution, that.dateRestitution) &&
            Objects.equals(agentId, that.agentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateAffectation, motif, dateRestitution, agentId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AffectationCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDateAffectation().map(f -> "dateAffectation=" + f + ", ").orElse("") +
            optionalMotif().map(f -> "motif=" + f + ", ").orElse("") +
            optionalDateRestitution().map(f -> "dateRestitution=" + f + ", ").orElse("") +
            optionalAgentId().map(f -> "agentId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
