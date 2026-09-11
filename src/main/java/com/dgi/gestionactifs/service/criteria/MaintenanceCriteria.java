package com.dgi.gestionactifs.service.criteria;

import com.dgi.gestionactifs.domain.enumeration.StatutMaintenance;
import com.dgi.gestionactifs.domain.enumeration.TypeMaintenance;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.dgi.gestionactifs.domain.Maintenance} entity. This class is used
 * in {@link com.dgi.gestionactifs.web.rest.MaintenanceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /maintenances?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MaintenanceCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TypeMaintenance
     */
    public static class TypeMaintenanceFilter extends Filter<TypeMaintenance> {

        public TypeMaintenanceFilter() {}

        public TypeMaintenanceFilter(TypeMaintenanceFilter filter) {
            super(filter);
        }

        @Override
        public TypeMaintenanceFilter copy() {
            return new TypeMaintenanceFilter(this);
        }
    }

    /**
     * Class for filtering StatutMaintenance
     */
    public static class StatutMaintenanceFilter extends Filter<StatutMaintenance> {

        public StatutMaintenanceFilter() {}

        public StatutMaintenanceFilter(StatutMaintenanceFilter filter) {
            super(filter);
        }

        @Override
        public StatutMaintenanceFilter copy() {
            return new StatutMaintenanceFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private TypeMaintenanceFilter typeMaintenance;

    private LocalDateFilter datePanne;

    private StatutMaintenanceFilter statut;

    private LocalDateFilter dateCloture;

    private LongFilter actifId;

    private LongFilter technicienId;

    private Boolean distinct;

    public MaintenanceCriteria() {}

    public MaintenanceCriteria(MaintenanceCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.typeMaintenance = other.optionalTypeMaintenance().map(TypeMaintenanceFilter::copy).orElse(null);
        this.datePanne = other.optionalDatePanne().map(LocalDateFilter::copy).orElse(null);
        this.statut = other.optionalStatut().map(StatutMaintenanceFilter::copy).orElse(null);
        this.dateCloture = other.optionalDateCloture().map(LocalDateFilter::copy).orElse(null);
        this.actifId = other.optionalActifId().map(LongFilter::copy).orElse(null);
        this.technicienId = other.optionalTechnicienId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public MaintenanceCriteria copy() {
        return new MaintenanceCriteria(this);
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

    public TypeMaintenanceFilter getTypeMaintenance() {
        return typeMaintenance;
    }

    public Optional<TypeMaintenanceFilter> optionalTypeMaintenance() {
        return Optional.ofNullable(typeMaintenance);
    }

    public TypeMaintenanceFilter typeMaintenance() {
        if (typeMaintenance == null) {
            setTypeMaintenance(new TypeMaintenanceFilter());
        }
        return typeMaintenance;
    }

    public void setTypeMaintenance(TypeMaintenanceFilter typeMaintenance) {
        this.typeMaintenance = typeMaintenance;
    }

    public LocalDateFilter getDatePanne() {
        return datePanne;
    }

    public Optional<LocalDateFilter> optionalDatePanne() {
        return Optional.ofNullable(datePanne);
    }

    public LocalDateFilter datePanne() {
        if (datePanne == null) {
            setDatePanne(new LocalDateFilter());
        }
        return datePanne;
    }

    public void setDatePanne(LocalDateFilter datePanne) {
        this.datePanne = datePanne;
    }

    public StatutMaintenanceFilter getStatut() {
        return statut;
    }

    public Optional<StatutMaintenanceFilter> optionalStatut() {
        return Optional.ofNullable(statut);
    }

    public StatutMaintenanceFilter statut() {
        if (statut == null) {
            setStatut(new StatutMaintenanceFilter());
        }
        return statut;
    }

    public void setStatut(StatutMaintenanceFilter statut) {
        this.statut = statut;
    }

    public LocalDateFilter getDateCloture() {
        return dateCloture;
    }

    public Optional<LocalDateFilter> optionalDateCloture() {
        return Optional.ofNullable(dateCloture);
    }

    public LocalDateFilter dateCloture() {
        if (dateCloture == null) {
            setDateCloture(new LocalDateFilter());
        }
        return dateCloture;
    }

    public void setDateCloture(LocalDateFilter dateCloture) {
        this.dateCloture = dateCloture;
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

    public LongFilter getTechnicienId() {
        return technicienId;
    }

    public Optional<LongFilter> optionalTechnicienId() {
        return Optional.ofNullable(technicienId);
    }

    public LongFilter technicienId() {
        if (technicienId == null) {
            setTechnicienId(new LongFilter());
        }
        return technicienId;
    }

    public void setTechnicienId(LongFilter technicienId) {
        this.technicienId = technicienId;
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
        final MaintenanceCriteria that = (MaintenanceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(typeMaintenance, that.typeMaintenance) &&
            Objects.equals(datePanne, that.datePanne) &&
            Objects.equals(statut, that.statut) &&
            Objects.equals(dateCloture, that.dateCloture) &&
            Objects.equals(actifId, that.actifId) &&
            Objects.equals(technicienId, that.technicienId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, typeMaintenance, datePanne, statut, dateCloture, actifId, technicienId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaintenanceCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTypeMaintenance().map(f -> "typeMaintenance=" + f + ", ").orElse("") +
            optionalDatePanne().map(f -> "datePanne=" + f + ", ").orElse("") +
            optionalStatut().map(f -> "statut=" + f + ", ").orElse("") +
            optionalDateCloture().map(f -> "dateCloture=" + f + ", ").orElse("") +
            optionalActifId().map(f -> "actifId=" + f + ", ").orElse("") +
            optionalTechnicienId().map(f -> "technicienId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
