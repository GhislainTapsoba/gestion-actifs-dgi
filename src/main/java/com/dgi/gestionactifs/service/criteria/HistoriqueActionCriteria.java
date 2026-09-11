package com.dgi.gestionactifs.service.criteria;

import com.dgi.gestionactifs.domain.enumeration.TypeMouvement;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.dgi.gestionactifs.domain.HistoriqueAction} entity. This class is used
 * in {@link com.dgi.gestionactifs.web.rest.HistoriqueActionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /historique-actions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistoriqueActionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TypeMouvement
     */
    public static class TypeMouvementFilter extends Filter<TypeMouvement> {

        public TypeMouvementFilter() {}

        public TypeMouvementFilter(TypeMouvementFilter filter) {
            super(filter);
        }

        @Override
        public TypeMouvementFilter copy() {
            return new TypeMouvementFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter dateAction;

    private TypeMouvementFilter typeAction;

    private StringFilter entiteCiblee;

    private StringFilter ancienneValeur;

    private StringFilter nouvelleValeur;

    private LongFilter utilisateurId;

    private Boolean distinct;

    public HistoriqueActionCriteria() {}

    public HistoriqueActionCriteria(HistoriqueActionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.dateAction = other.optionalDateAction().map(ZonedDateTimeFilter::copy).orElse(null);
        this.typeAction = other.optionalTypeAction().map(TypeMouvementFilter::copy).orElse(null);
        this.entiteCiblee = other.optionalEntiteCiblee().map(StringFilter::copy).orElse(null);
        this.ancienneValeur = other.optionalAncienneValeur().map(StringFilter::copy).orElse(null);
        this.nouvelleValeur = other.optionalNouvelleValeur().map(StringFilter::copy).orElse(null);
        this.utilisateurId = other.optionalUtilisateurId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public HistoriqueActionCriteria copy() {
        return new HistoriqueActionCriteria(this);
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

    public ZonedDateTimeFilter getDateAction() {
        return dateAction;
    }

    public Optional<ZonedDateTimeFilter> optionalDateAction() {
        return Optional.ofNullable(dateAction);
    }

    public ZonedDateTimeFilter dateAction() {
        if (dateAction == null) {
            setDateAction(new ZonedDateTimeFilter());
        }
        return dateAction;
    }

    public void setDateAction(ZonedDateTimeFilter dateAction) {
        this.dateAction = dateAction;
    }

    public TypeMouvementFilter getTypeAction() {
        return typeAction;
    }

    public Optional<TypeMouvementFilter> optionalTypeAction() {
        return Optional.ofNullable(typeAction);
    }

    public TypeMouvementFilter typeAction() {
        if (typeAction == null) {
            setTypeAction(new TypeMouvementFilter());
        }
        return typeAction;
    }

    public void setTypeAction(TypeMouvementFilter typeAction) {
        this.typeAction = typeAction;
    }

    public StringFilter getEntiteCiblee() {
        return entiteCiblee;
    }

    public Optional<StringFilter> optionalEntiteCiblee() {
        return Optional.ofNullable(entiteCiblee);
    }

    public StringFilter entiteCiblee() {
        if (entiteCiblee == null) {
            setEntiteCiblee(new StringFilter());
        }
        return entiteCiblee;
    }

    public void setEntiteCiblee(StringFilter entiteCiblee) {
        this.entiteCiblee = entiteCiblee;
    }

    public StringFilter getAncienneValeur() {
        return ancienneValeur;
    }

    public Optional<StringFilter> optionalAncienneValeur() {
        return Optional.ofNullable(ancienneValeur);
    }

    public StringFilter ancienneValeur() {
        if (ancienneValeur == null) {
            setAncienneValeur(new StringFilter());
        }
        return ancienneValeur;
    }

    public void setAncienneValeur(StringFilter ancienneValeur) {
        this.ancienneValeur = ancienneValeur;
    }

    public StringFilter getNouvelleValeur() {
        return nouvelleValeur;
    }

    public Optional<StringFilter> optionalNouvelleValeur() {
        return Optional.ofNullable(nouvelleValeur);
    }

    public StringFilter nouvelleValeur() {
        if (nouvelleValeur == null) {
            setNouvelleValeur(new StringFilter());
        }
        return nouvelleValeur;
    }

    public void setNouvelleValeur(StringFilter nouvelleValeur) {
        this.nouvelleValeur = nouvelleValeur;
    }

    public LongFilter getUtilisateurId() {
        return utilisateurId;
    }

    public Optional<LongFilter> optionalUtilisateurId() {
        return Optional.ofNullable(utilisateurId);
    }

    public LongFilter utilisateurId() {
        if (utilisateurId == null) {
            setUtilisateurId(new LongFilter());
        }
        return utilisateurId;
    }

    public void setUtilisateurId(LongFilter utilisateurId) {
        this.utilisateurId = utilisateurId;
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
        final HistoriqueActionCriteria that = (HistoriqueActionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dateAction, that.dateAction) &&
            Objects.equals(typeAction, that.typeAction) &&
            Objects.equals(entiteCiblee, that.entiteCiblee) &&
            Objects.equals(ancienneValeur, that.ancienneValeur) &&
            Objects.equals(nouvelleValeur, that.nouvelleValeur) &&
            Objects.equals(utilisateurId, that.utilisateurId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateAction, typeAction, entiteCiblee, ancienneValeur, nouvelleValeur, utilisateurId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoriqueActionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDateAction().map(f -> "dateAction=" + f + ", ").orElse("") +
            optionalTypeAction().map(f -> "typeAction=" + f + ", ").orElse("") +
            optionalEntiteCiblee().map(f -> "entiteCiblee=" + f + ", ").orElse("") +
            optionalAncienneValeur().map(f -> "ancienneValeur=" + f + ", ").orElse("") +
            optionalNouvelleValeur().map(f -> "nouvelleValeur=" + f + ", ").orElse("") +
            optionalUtilisateurId().map(f -> "utilisateurId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
