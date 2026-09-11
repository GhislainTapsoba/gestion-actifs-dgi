package com.dgi.gestionactifs.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.dgi.gestionactifs.domain.ServiceDgi} entity. This class is used
 * in {@link com.dgi.gestionactifs.web.rest.ServiceDgiResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /service-dgis?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServiceDgiCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomService;

    private StringFilter chefService;

    private Boolean distinct;

    public ServiceDgiCriteria() {}

    public ServiceDgiCriteria(ServiceDgiCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nomService = other.optionalNomService().map(StringFilter::copy).orElse(null);
        this.chefService = other.optionalChefService().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ServiceDgiCriteria copy() {
        return new ServiceDgiCriteria(this);
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

    public StringFilter getNomService() {
        return nomService;
    }

    public Optional<StringFilter> optionalNomService() {
        return Optional.ofNullable(nomService);
    }

    public StringFilter nomService() {
        if (nomService == null) {
            setNomService(new StringFilter());
        }
        return nomService;
    }

    public void setNomService(StringFilter nomService) {
        this.nomService = nomService;
    }

    public StringFilter getChefService() {
        return chefService;
    }

    public Optional<StringFilter> optionalChefService() {
        return Optional.ofNullable(chefService);
    }

    public StringFilter chefService() {
        if (chefService == null) {
            setChefService(new StringFilter());
        }
        return chefService;
    }

    public void setChefService(StringFilter chefService) {
        this.chefService = chefService;
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
        final ServiceDgiCriteria that = (ServiceDgiCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nomService, that.nomService) &&
            Objects.equals(chefService, that.chefService) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomService, chefService, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceDgiCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNomService().map(f -> "nomService=" + f + ", ").orElse("") +
            optionalChefService().map(f -> "chefService=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
