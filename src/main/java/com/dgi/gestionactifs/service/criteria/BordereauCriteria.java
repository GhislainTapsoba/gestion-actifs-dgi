package com.dgi.gestionactifs.service.criteria;

import com.dgi.gestionactifs.domain.enumeration.StatutBordereau;
import com.dgi.gestionactifs.domain.enumeration.TypeBordereau;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.dgi.gestionactifs.domain.Bordereau} entity. This class is used
 * in {@link com.dgi.gestionactifs.web.rest.BordereauResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /bordereaus?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BordereauCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TypeBordereau
     */
    public static class TypeBordereauFilter extends Filter<TypeBordereau> {

        public TypeBordereauFilter() {}

        public TypeBordereauFilter(TypeBordereauFilter filter) {
            super(filter);
        }

        @Override
        public TypeBordereauFilter copy() {
            return new TypeBordereauFilter(this);
        }
    }

    /**
     * Class for filtering StatutBordereau
     */
    public static class StatutBordereauFilter extends Filter<StatutBordereau> {

        public StatutBordereauFilter() {}

        public StatutBordereauFilter(StatutBordereauFilter filter) {
            super(filter);
        }

        @Override
        public StatutBordereauFilter copy() {
            return new StatutBordereauFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter numero;

    private LocalDateFilter dateEmission;

    private TypeBordereauFilter typeBordereau;

    private StatutBordereauFilter statutValidation;

    private LocalDateFilter dateValidation;

    private LongFilter transfertId;

    private LongFilter affectationId;

    private LongFilter emetteurId;

    private Boolean distinct;

    public BordereauCriteria() {}

    public BordereauCriteria(BordereauCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.numero = other.optionalNumero().map(StringFilter::copy).orElse(null);
        this.dateEmission = other.optionalDateEmission().map(LocalDateFilter::copy).orElse(null);
        this.typeBordereau = other.optionalTypeBordereau().map(TypeBordereauFilter::copy).orElse(null);
        this.statutValidation = other.optionalStatutValidation().map(StatutBordereauFilter::copy).orElse(null);
        this.dateValidation = other.optionalDateValidation().map(LocalDateFilter::copy).orElse(null);
        this.transfertId = other.optionalTransfertId().map(LongFilter::copy).orElse(null);
        this.affectationId = other.optionalAffectationId().map(LongFilter::copy).orElse(null);
        this.emetteurId = other.optionalEmetteurId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public BordereauCriteria copy() {
        return new BordereauCriteria(this);
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

    public StringFilter getNumero() {
        return numero;
    }

    public Optional<StringFilter> optionalNumero() {
        return Optional.ofNullable(numero);
    }

    public StringFilter numero() {
        if (numero == null) {
            setNumero(new StringFilter());
        }
        return numero;
    }

    public void setNumero(StringFilter numero) {
        this.numero = numero;
    }

    public LocalDateFilter getDateEmission() {
        return dateEmission;
    }

    public Optional<LocalDateFilter> optionalDateEmission() {
        return Optional.ofNullable(dateEmission);
    }

    public LocalDateFilter dateEmission() {
        if (dateEmission == null) {
            setDateEmission(new LocalDateFilter());
        }
        return dateEmission;
    }

    public void setDateEmission(LocalDateFilter dateEmission) {
        this.dateEmission = dateEmission;
    }

    public TypeBordereauFilter getTypeBordereau() {
        return typeBordereau;
    }

    public Optional<TypeBordereauFilter> optionalTypeBordereau() {
        return Optional.ofNullable(typeBordereau);
    }

    public TypeBordereauFilter typeBordereau() {
        if (typeBordereau == null) {
            setTypeBordereau(new TypeBordereauFilter());
        }
        return typeBordereau;
    }

    public void setTypeBordereau(TypeBordereauFilter typeBordereau) {
        this.typeBordereau = typeBordereau;
    }

    public StatutBordereauFilter getStatutValidation() {
        return statutValidation;
    }

    public Optional<StatutBordereauFilter> optionalStatutValidation() {
        return Optional.ofNullable(statutValidation);
    }

    public StatutBordereauFilter statutValidation() {
        if (statutValidation == null) {
            setStatutValidation(new StatutBordereauFilter());
        }
        return statutValidation;
    }

    public void setStatutValidation(StatutBordereauFilter statutValidation) {
        this.statutValidation = statutValidation;
    }

    public LocalDateFilter getDateValidation() {
        return dateValidation;
    }

    public Optional<LocalDateFilter> optionalDateValidation() {
        return Optional.ofNullable(dateValidation);
    }

    public LocalDateFilter dateValidation() {
        if (dateValidation == null) {
            setDateValidation(new LocalDateFilter());
        }
        return dateValidation;
    }

    public void setDateValidation(LocalDateFilter dateValidation) {
        this.dateValidation = dateValidation;
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

    public LongFilter getEmetteurId() {
        return emetteurId;
    }

    public Optional<LongFilter> optionalEmetteurId() {
        return Optional.ofNullable(emetteurId);
    }

    public LongFilter emetteurId() {
        if (emetteurId == null) {
            setEmetteurId(new LongFilter());
        }
        return emetteurId;
    }

    public void setEmetteurId(LongFilter emetteurId) {
        this.emetteurId = emetteurId;
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
        final BordereauCriteria that = (BordereauCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(numero, that.numero) &&
            Objects.equals(dateEmission, that.dateEmission) &&
            Objects.equals(typeBordereau, that.typeBordereau) &&
            Objects.equals(statutValidation, that.statutValidation) &&
            Objects.equals(dateValidation, that.dateValidation) &&
            Objects.equals(transfertId, that.transfertId) &&
            Objects.equals(affectationId, that.affectationId) &&
            Objects.equals(emetteurId, that.emetteurId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            numero,
            dateEmission,
            typeBordereau,
            statutValidation,
            dateValidation,
            transfertId,
            affectationId,
            emetteurId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BordereauCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNumero().map(f -> "numero=" + f + ", ").orElse("") +
            optionalDateEmission().map(f -> "dateEmission=" + f + ", ").orElse("") +
            optionalTypeBordereau().map(f -> "typeBordereau=" + f + ", ").orElse("") +
            optionalStatutValidation().map(f -> "statutValidation=" + f + ", ").orElse("") +
            optionalDateValidation().map(f -> "dateValidation=" + f + ", ").orElse("") +
            optionalTransfertId().map(f -> "transfertId=" + f + ", ").orElse("") +
            optionalAffectationId().map(f -> "affectationId=" + f + ", ").orElse("") +
            optionalEmetteurId().map(f -> "emetteurId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
