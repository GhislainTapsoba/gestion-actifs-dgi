package com.dgi.gestionactifs.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.dgi.gestionactifs.domain.TransfertActif} entity. This class is used
 * in {@link com.dgi.gestionactifs.web.rest.TransfertActifResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /transfert-actifs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransfertActifCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter observation;

    private LongFilter transfertId;

    private LongFilter actifId;

    private Boolean distinct;

    public TransfertActifCriteria() {}

    public TransfertActifCriteria(TransfertActifCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.observation = other.optionalObservation().map(StringFilter::copy).orElse(null);
        this.transfertId = other.optionalTransfertId().map(LongFilter::copy).orElse(null);
        this.actifId = other.optionalActifId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TransfertActifCriteria copy() {
        return new TransfertActifCriteria(this);
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

    public LongFilter getTransfertId() {
        return transfertId;
    }

    public Optional<LongFilter> optionalTransfertId() {
        return Optional.ofNullable(transfertId);
    }

    public LongFilter transfertId() {
        if (transfertId == null) {
            setTransfertId(new LongFilter());
        }
        return transfertId;
    }

    public void setTransfertId(LongFilter transfertId) {
        this.transfertId = transfertId;
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
        final TransfertActifCriteria that = (TransfertActifCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(observation, that.observation) &&
            Objects.equals(transfertId, that.transfertId) &&
            Objects.equals(actifId, that.actifId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, observation, transfertId, actifId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransfertActifCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalObservation().map(f -> "observation=" + f + ", ").orElse("") +
            optionalTransfertId().map(f -> "transfertId=" + f + ", ").orElse("") +
            optionalActifId().map(f -> "actifId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
