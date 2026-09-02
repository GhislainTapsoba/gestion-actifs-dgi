package com.dgi.gestionactifs.service.criteria;

import com.dgi.gestionactifs.domain.enumeration.StatutTransfert;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.dgi.gestionactifs.domain.Transfert} entity. This class is used
 * in {@link com.dgi.gestionactifs.web.rest.TransfertResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /transferts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransfertCriteria implements Serializable, Criteria {

    /**
     * Class for filtering StatutTransfert
     */
    public static class StatutTransfertFilter extends Filter<StatutTransfert> {

        public StatutTransfertFilter() {}

        public StatutTransfertFilter(StatutTransfertFilter filter) {
            super(filter);
        }

        @Override
        public StatutTransfertFilter copy() {
            return new StatutTransfertFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter dateDemande;

    private StatutTransfertFilter statut;

    private StringFilter commentaireRejet;

    private LocalDateFilter dateTraitement;

    private LongFilter demandeurId;

    private LongFilter validateurId;

    private LongFilter actifId;

    private Boolean distinct;

    public TransfertCriteria() {}

    public TransfertCriteria(TransfertCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.dateDemande = other.optionalDateDemande().map(LocalDateFilter::copy).orElse(null);
        this.statut = other.optionalStatut().map(StatutTransfertFilter::copy).orElse(null);
        this.commentaireRejet = other.optionalCommentaireRejet().map(StringFilter::copy).orElse(null);
        this.dateTraitement = other.optionalDateTraitement().map(LocalDateFilter::copy).orElse(null);
        this.demandeurId = other.optionalDemandeurId().map(LongFilter::copy).orElse(null);
        this.validateurId = other.optionalValidateurId().map(LongFilter::copy).orElse(null);
        this.actifId = other.optionalActifId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TransfertCriteria copy() {
        return new TransfertCriteria(this);
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

    public LocalDateFilter getDateDemande() {
        return dateDemande;
    }

    public Optional<LocalDateFilter> optionalDateDemande() {
        return Optional.ofNullable(dateDemande);
    }

    public LocalDateFilter dateDemande() {
        if (dateDemande == null) {
            setDateDemande(new LocalDateFilter());
        }
        return dateDemande;
    }

    public void setDateDemande(LocalDateFilter dateDemande) {
        this.dateDemande = dateDemande;
    }

    public StatutTransfertFilter getStatut() {
        return statut;
    }

    public Optional<StatutTransfertFilter> optionalStatut() {
        return Optional.ofNullable(statut);
    }

    public StatutTransfertFilter statut() {
        if (statut == null) {
            setStatut(new StatutTransfertFilter());
        }
        return statut;
    }

    public void setStatut(StatutTransfertFilter statut) {
        this.statut = statut;
    }

    public StringFilter getCommentaireRejet() {
        return commentaireRejet;
    }

    public Optional<StringFilter> optionalCommentaireRejet() {
        return Optional.ofNullable(commentaireRejet);
    }

    public StringFilter commentaireRejet() {
        if (commentaireRejet == null) {
            setCommentaireRejet(new StringFilter());
        }
        return commentaireRejet;
    }

    public void setCommentaireRejet(StringFilter commentaireRejet) {
        this.commentaireRejet = commentaireRejet;
    }

    public LocalDateFilter getDateTraitement() {
        return dateTraitement;
    }

    public Optional<LocalDateFilter> optionalDateTraitement() {
        return Optional.ofNullable(dateTraitement);
    }

    public LocalDateFilter dateTraitement() {
        if (dateTraitement == null) {
            setDateTraitement(new LocalDateFilter());
        }
        return dateTraitement;
    }

    public void setDateTraitement(LocalDateFilter dateTraitement) {
        this.dateTraitement = dateTraitement;
    }

    public LongFilter getDemandeurId() {
        return demandeurId;
    }

    public Optional<LongFilter> optionalDemandeurId() {
        return Optional.ofNullable(demandeurId);
    }

    public LongFilter demandeurId() {
        if (demandeurId == null) {
            setDemandeurId(new LongFilter());
        }
        return demandeurId;
    }

    public void setDemandeurId(LongFilter demandeurId) {
        this.demandeurId = demandeurId;
    }

    public LongFilter getValidateurId() {
        return validateurId;
    }

    public Optional<LongFilter> optionalValidateurId() {
        return Optional.ofNullable(validateurId);
    }

    public LongFilter validateurId() {
        if (validateurId == null) {
            setValidateurId(new LongFilter());
        }
        return validateurId;
    }

    public void setValidateurId(LongFilter validateurId) {
        this.validateurId = validateurId;
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
        final TransfertCriteria that = (TransfertCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dateDemande, that.dateDemande) &&
            Objects.equals(statut, that.statut) &&
            Objects.equals(commentaireRejet, that.commentaireRejet) &&
            Objects.equals(dateTraitement, that.dateTraitement) &&
            Objects.equals(demandeurId, that.demandeurId) &&
            Objects.equals(validateurId, that.validateurId) &&
            Objects.equals(actifId, that.actifId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateDemande, statut, commentaireRejet, dateTraitement, demandeurId, validateurId, actifId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransfertCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDateDemande().map(f -> "dateDemande=" + f + ", ").orElse("") +
            optionalStatut().map(f -> "statut=" + f + ", ").orElse("") +
            optionalCommentaireRejet().map(f -> "commentaireRejet=" + f + ", ").orElse("") +
            optionalDateTraitement().map(f -> "dateTraitement=" + f + ", ").orElse("") +
            optionalDemandeurId().map(f -> "demandeurId=" + f + ", ").orElse("") +
            optionalValidateurId().map(f -> "validateurId=" + f + ", ").orElse("") +
            optionalActifId().map(f -> "actifId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
