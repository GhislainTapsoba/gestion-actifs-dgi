package com.dgi.gestionactifs.service.criteria;

import com.dgi.gestionactifs.domain.enumeration.StatutPanne;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.dgi.gestionactifs.domain.Panne} entity. This class is used
 * in {@link com.dgi.gestionactifs.web.rest.PanneResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /pannes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PanneCriteria implements Serializable, Criteria {

    /**
     * Class for filtering StatutPanne
     */
    public static class StatutPanneFilter extends Filter<StatutPanne> {

        public StatutPanneFilter() {}

        public StatutPanneFilter(StatutPanneFilter filter) {
            super(filter);
        }

        @Override
        public StatutPanneFilter copy() {
            return new StatutPanneFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter description;

    private LocalDateFilter dateDeclaration;

    private StatutPanneFilter statutPanne;

    private LongFilter actifId;

    private Boolean distinct;

    public PanneCriteria() {}

    public PanneCriteria(PanneCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.dateDeclaration = other.optionalDateDeclaration().map(LocalDateFilter::copy).orElse(null);
        this.statutPanne = other.optionalStatutPanne().map(StatutPanneFilter::copy).orElse(null);
        this.actifId = other.optionalActifId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PanneCriteria copy() {
        return new PanneCriteria(this);
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

    public StatutPanneFilter getStatutPanne() {
        return statutPanne;
    }

    public Optional<StatutPanneFilter> optionalStatutPanne() {
        return Optional.ofNullable(statutPanne);
    }

    public StatutPanneFilter statutPanne() {
        if (statutPanne == null) {
            setStatutPanne(new StatutPanneFilter());
        }
        return statutPanne;
    }

    public void setStatutPanne(StatutPanneFilter statutPanne) {
        this.statutPanne = statutPanne;
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
        final PanneCriteria that = (PanneCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(description, that.description) &&
            Objects.equals(dateDeclaration, that.dateDeclaration) &&
            Objects.equals(statutPanne, that.statutPanne) &&
            Objects.equals(actifId, that.actifId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, dateDeclaration, statutPanne, actifId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PanneCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalDateDeclaration().map(f -> "dateDeclaration=" + f + ", ").orElse("") +
            optionalStatutPanne().map(f -> "statutPanne=" + f + ", ").orElse("") +
            optionalActifId().map(f -> "actifId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
