package com.dgi.gestionactifs.service.criteria;

import com.dgi.gestionactifs.domain.enumeration.StatutIntervention;
import com.dgi.gestionactifs.domain.enumeration.TypeIntervention;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.dgi.gestionactifs.domain.Intervention} entity. This class is used
 * in {@link com.dgi.gestionactifs.web.rest.InterventionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /interventions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InterventionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TypeIntervention
     */
    public static class TypeInterventionFilter extends Filter<TypeIntervention> {

        public TypeInterventionFilter() {}

        public TypeInterventionFilter(TypeInterventionFilter filter) {
            super(filter);
        }

        @Override
        public TypeInterventionFilter copy() {
            return new TypeInterventionFilter(this);
        }
    }

    /**
     * Class for filtering StatutIntervention
     */
    public static class StatutInterventionFilter extends Filter<StatutIntervention> {

        public StatutInterventionFilter() {}

        public StatutInterventionFilter(StatutInterventionFilter filter) {
            super(filter);
        }

        @Override
        public StatutInterventionFilter copy() {
            return new StatutInterventionFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter dateDeclaration;

    private TypeInterventionFilter typeIntervention;

    private StatutInterventionFilter statut;

    private StringFilter description;

    private LongFilter panneId;

    private LongFilter planningId;

    private Boolean distinct;

    public InterventionCriteria() {}

    public InterventionCriteria(InterventionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.dateDeclaration = other.optionalDateDeclaration().map(LocalDateFilter::copy).orElse(null);
        this.typeIntervention = other.optionalTypeIntervention().map(TypeInterventionFilter::copy).orElse(null);
        this.statut = other.optionalStatut().map(StatutInterventionFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.panneId = other.optionalPanneId().map(LongFilter::copy).orElse(null);
        this.planningId = other.optionalPlanningId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public InterventionCriteria copy() {
        return new InterventionCriteria(this);
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

    public LocalDateFilter getDateDeclaration() {
        return dateDeclaration;
    }

    public Optional<LocalDateFilter> optionalDateDeclaration() {
        return Optional.ofNullable(dateDeclaration);
    }

    public LocalDateFilter dateDeclaration() {
        if (dateDeclaration == null) {
            setDateDeclaration(new LocalDateFilter());
        }
        return dateDeclaration;
    }

    public void setDateDeclaration(LocalDateFilter dateDeclaration) {
        this.dateDeclaration = dateDeclaration;
    }

    public TypeInterventionFilter getTypeIntervention() {
        return typeIntervention;
    }

    public Optional<TypeInterventionFilter> optionalTypeIntervention() {
        return Optional.ofNullable(typeIntervention);
    }

    public TypeInterventionFilter typeIntervention() {
        if (typeIntervention == null) {
            setTypeIntervention(new TypeInterventionFilter());
        }
        return typeIntervention;
    }

    public void setTypeIntervention(TypeInterventionFilter typeIntervention) {
        this.typeIntervention = typeIntervention;
    }

    public StatutInterventionFilter getStatut() {
        return statut;
    }

    public Optional<StatutInterventionFilter> optionalStatut() {
        return Optional.ofNullable(statut);
    }

    public StatutInterventionFilter statut() {
        if (statut == null) {
            setStatut(new StatutInterventionFilter());
        }
        return statut;
    }

    public void setStatut(StatutInterventionFilter statut) {
        this.statut = statut;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LongFilter getPanneId() {
        return panneId;
    }

    public Optional<LongFilter> optionalPanneId() {
        return Optional.ofNullable(panneId);
    }

    public LongFilter panneId() {
        if (panneId == null) {
            setPanneId(new LongFilter());
        }
        return panneId;
    }

    public void setPanneId(LongFilter panneId) {
        this.panneId = panneId;
    }

    public LongFilter getPlanningId() {
        return planningId;
    }

    public Optional<LongFilter> optionalPlanningId() {
        return Optional.ofNullable(planningId);
    }

    public LongFilter planningId() {
        if (planningId == null) {
            setPlanningId(new LongFilter());
        }
        return planningId;
    }

    public void setPlanningId(LongFilter planningId) {
        this.planningId = planningId;
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
        final InterventionCriteria that = (InterventionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dateDeclaration, that.dateDeclaration) &&
            Objects.equals(typeIntervention, that.typeIntervention) &&
            Objects.equals(statut, that.statut) &&
            Objects.equals(description, that.description) &&
            Objects.equals(panneId, that.panneId) &&
            Objects.equals(planningId, that.planningId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateDeclaration, typeIntervention, statut, description, panneId, planningId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InterventionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDateDeclaration().map(f -> "dateDeclaration=" + f + ", ").orElse("") +
            optionalTypeIntervention().map(f -> "typeIntervention=" + f + ", ").orElse("") +
            optionalStatut().map(f -> "statut=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalPanneId().map(f -> "panneId=" + f + ", ").orElse("") +
            optionalPlanningId().map(f -> "planningId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
