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

    private StringFilter identifiantUnique;

    private StringFilter codeBarreQR;

    private TypeActifFilter type;

    private StatutActifFilter etat;

    private StringFilter localisation;

    private LocalDateFilter dateAcquisition;

    private LongFilter affectationId;

    private LongFilter transfertId;

    private LongFilter maintenanceId;

    private Boolean distinct;

    public ActifCriteria() {}

    public ActifCriteria(ActifCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.identifiantUnique = other.optionalIdentifiantUnique().map(StringFilter::copy).orElse(null);
        this.codeBarreQR = other.optionalCodeBarreQR().map(StringFilter::copy).orElse(null);
        this.type = other.optionalType().map(TypeActifFilter::copy).orElse(null);
        this.etat = other.optionalEtat().map(StatutActifFilter::copy).orElse(null);
        this.localisation = other.optionalLocalisation().map(StringFilter::copy).orElse(null);
        this.dateAcquisition = other.optionalDateAcquisition().map(LocalDateFilter::copy).orElse(null);
        this.affectationId = other.optionalAffectationId().map(LongFilter::copy).orElse(null);
        this.transfertId = other.optionalTransfertId().map(LongFilter::copy).orElse(null);
        this.maintenanceId = other.optionalMaintenanceId().map(LongFilter::copy).orElse(null);
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

    public StringFilter getIdentifiantUnique() {
        return identifiantUnique;
    }

    public Optional<StringFilter> optionalIdentifiantUnique() {
        return Optional.ofNullable(identifiantUnique);
    }

    public StringFilter identifiantUnique() {
        if (identifiantUnique == null) {
            setIdentifiantUnique(new StringFilter());
        }
        return identifiantUnique;
    }

    public void setIdentifiantUnique(StringFilter identifiantUnique) {
        this.identifiantUnique = identifiantUnique;
    }

    public StringFilter getCodeBarreQR() {
        return codeBarreQR;
    }

    public Optional<StringFilter> optionalCodeBarreQR() {
        return Optional.ofNullable(codeBarreQR);
    }

    public StringFilter codeBarreQR() {
        if (codeBarreQR == null) {
            setCodeBarreQR(new StringFilter());
        }
        return codeBarreQR;
    }

    public void setCodeBarreQR(StringFilter codeBarreQR) {
        this.codeBarreQR = codeBarreQR;
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

    public LongFilter getMaintenanceId() {
        return maintenanceId;
    }

    public Optional<LongFilter> optionalMaintenanceId() {
        return Optional.ofNullable(maintenanceId);
    }

    public LongFilter maintenanceId() {
        if (maintenanceId == null) {
            setMaintenanceId(new LongFilter());
        }
        return maintenanceId;
    }

    public void setMaintenanceId(LongFilter maintenanceId) {
        this.maintenanceId = maintenanceId;
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
            Objects.equals(identifiantUnique, that.identifiantUnique) &&
            Objects.equals(codeBarreQR, that.codeBarreQR) &&
            Objects.equals(type, that.type) &&
            Objects.equals(etat, that.etat) &&
            Objects.equals(localisation, that.localisation) &&
            Objects.equals(dateAcquisition, that.dateAcquisition) &&
            Objects.equals(affectationId, that.affectationId) &&
            Objects.equals(transfertId, that.transfertId) &&
            Objects.equals(maintenanceId, that.maintenanceId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            identifiantUnique,
            codeBarreQR,
            type,
            etat,
            localisation,
            dateAcquisition,
            affectationId,
            transfertId,
            maintenanceId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ActifCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalIdentifiantUnique().map(f -> "identifiantUnique=" + f + ", ").orElse("") +
            optionalCodeBarreQR().map(f -> "codeBarreQR=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalEtat().map(f -> "etat=" + f + ", ").orElse("") +
            optionalLocalisation().map(f -> "localisation=" + f + ", ").orElse("") +
            optionalDateAcquisition().map(f -> "dateAcquisition=" + f + ", ").orElse("") +
            optionalAffectationId().map(f -> "affectationId=" + f + ", ").orElse("") +
            optionalTransfertId().map(f -> "transfertId=" + f + ", ").orElse("") +
            optionalMaintenanceId().map(f -> "maintenanceId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
