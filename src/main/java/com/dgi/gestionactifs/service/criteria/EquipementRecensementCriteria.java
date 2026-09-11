package com.dgi.gestionactifs.service.criteria;

import com.dgi.gestionactifs.domain.enumeration.EtatMateriel;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.dgi.gestionactifs.domain.EquipementRecensement} entity. This class is used
 * in {@link com.dgi.gestionactifs.web.rest.EquipementRecensementResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /equipement-recensements?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EquipementRecensementCriteria implements Serializable, Criteria {

    /**
     * Class for filtering EtatMateriel
     */
    public static class EtatMaterielFilter extends Filter<EtatMateriel> {

        public EtatMaterielFilter() {}

        public EtatMaterielFilter(EtatMaterielFilter filter) {
            super(filter);
        }

        @Override
        public EtatMaterielFilter copy() {
            return new EtatMaterielFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private EtatMaterielFilter etatConstate;

    private LocalDateFilter dateConstat;

    private StringFilter emplacementConstate;

    private BooleanFilter anomalieConstatee;

    private LongFilter recensementId;

    private LongFilter actifId;

    private Boolean distinct;

    public EquipementRecensementCriteria() {}

    public EquipementRecensementCriteria(EquipementRecensementCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.etatConstate = other.optionalEtatConstate().map(EtatMaterielFilter::copy).orElse(null);
        this.dateConstat = other.optionalDateConstat().map(LocalDateFilter::copy).orElse(null);
        this.emplacementConstate = other.optionalEmplacementConstate().map(StringFilter::copy).orElse(null);
        this.anomalieConstatee = other.optionalAnomalieConstatee().map(BooleanFilter::copy).orElse(null);
        this.recensementId = other.optionalRecensementId().map(LongFilter::copy).orElse(null);
        this.actifId = other.optionalActifId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public EquipementRecensementCriteria copy() {
        return new EquipementRecensementCriteria(this);
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

    public EtatMaterielFilter getEtatConstate() {
        return etatConstate;
    }

    public Optional<EtatMaterielFilter> optionalEtatConstate() {
        return Optional.ofNullable(etatConstate);
    }

    public EtatMaterielFilter etatConstate() {
        if (etatConstate == null) {
            setEtatConstate(new EtatMaterielFilter());
        }
        return etatConstate;
    }

    public void setEtatConstate(EtatMaterielFilter etatConstate) {
        this.etatConstate = etatConstate;
    }

    public LocalDateFilter getDateConstat() {
        return dateConstat;
    }

    public Optional<LocalDateFilter> optionalDateConstat() {
        return Optional.ofNullable(dateConstat);
    }

    public LocalDateFilter dateConstat() {
        if (dateConstat == null) {
            setDateConstat(new LocalDateFilter());
        }
        return dateConstat;
    }

    public void setDateConstat(LocalDateFilter dateConstat) {
        this.dateConstat = dateConstat;
    }

    public StringFilter getEmplacementConstate() {
        return emplacementConstate;
    }

    public Optional<StringFilter> optionalEmplacementConstate() {
        return Optional.ofNullable(emplacementConstate);
    }

    public StringFilter emplacementConstate() {
        if (emplacementConstate == null) {
            setEmplacementConstate(new StringFilter());
        }
        return emplacementConstate;
    }

    public void setEmplacementConstate(StringFilter emplacementConstate) {
        this.emplacementConstate = emplacementConstate;
    }

    public BooleanFilter getAnomalieConstatee() {
        return anomalieConstatee;
    }

    public Optional<BooleanFilter> optionalAnomalieConstatee() {
        return Optional.ofNullable(anomalieConstatee);
    }

    public BooleanFilter anomalieConstatee() {
        if (anomalieConstatee == null) {
            setAnomalieConstatee(new BooleanFilter());
        }
        return anomalieConstatee;
    }

    public void setAnomalieConstatee(BooleanFilter anomalieConstatee) {
        this.anomalieConstatee = anomalieConstatee;
    }

    public LongFilter getRecensementId() {
        return recensementId;
    }

    public Optional<LongFilter> optionalRecensementId() {
        return Optional.ofNullable(recensementId);
    }

    public LongFilter recensementId() {
        if (recensementId == null) {
            setRecensementId(new LongFilter());
        }
        return recensementId;
    }

    public void setRecensementId(LongFilter recensementId) {
        this.recensementId = recensementId;
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
        final EquipementRecensementCriteria that = (EquipementRecensementCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(etatConstate, that.etatConstate) &&
            Objects.equals(dateConstat, that.dateConstat) &&
            Objects.equals(emplacementConstate, that.emplacementConstate) &&
            Objects.equals(anomalieConstatee, that.anomalieConstatee) &&
            Objects.equals(recensementId, that.recensementId) &&
            Objects.equals(actifId, that.actifId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, etatConstate, dateConstat, emplacementConstate, anomalieConstatee, recensementId, actifId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EquipementRecensementCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalEtatConstate().map(f -> "etatConstate=" + f + ", ").orElse("") +
            optionalDateConstat().map(f -> "dateConstat=" + f + ", ").orElse("") +
            optionalEmplacementConstate().map(f -> "emplacementConstate=" + f + ", ").orElse("") +
            optionalAnomalieConstatee().map(f -> "anomalieConstatee=" + f + ", ").orElse("") +
            optionalRecensementId().map(f -> "recensementId=" + f + ", ").orElse("") +
            optionalActifId().map(f -> "actifId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
