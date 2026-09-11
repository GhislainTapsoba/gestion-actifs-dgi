package com.dgi.gestionactifs.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ServiceDgiCriteriaTest {

    @Test
    void newServiceDgiCriteriaHasAllFiltersNullTest() {
        var serviceDgiCriteria = new ServiceDgiCriteria();
        assertThat(serviceDgiCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void serviceDgiCriteriaFluentMethodsCreatesFiltersTest() {
        var serviceDgiCriteria = new ServiceDgiCriteria();

        setAllFilters(serviceDgiCriteria);

        assertThat(serviceDgiCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void serviceDgiCriteriaCopyCreatesNullFilterTest() {
        var serviceDgiCriteria = new ServiceDgiCriteria();
        var copy = serviceDgiCriteria.copy();

        assertThat(serviceDgiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(serviceDgiCriteria)
        );
    }

    @Test
    void serviceDgiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var serviceDgiCriteria = new ServiceDgiCriteria();
        setAllFilters(serviceDgiCriteria);

        var copy = serviceDgiCriteria.copy();

        assertThat(serviceDgiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(serviceDgiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var serviceDgiCriteria = new ServiceDgiCriteria();

        assertThat(serviceDgiCriteria).hasToString("ServiceDgiCriteria{}");
    }

    private static void setAllFilters(ServiceDgiCriteria serviceDgiCriteria) {
        serviceDgiCriteria.id();
        serviceDgiCriteria.nomService();
        serviceDgiCriteria.chefService();
        serviceDgiCriteria.distinct();
    }

    private static Condition<ServiceDgiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNomService()) &&
                condition.apply(criteria.getChefService()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ServiceDgiCriteria> copyFiltersAre(ServiceDgiCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNomService(), copy.getNomService()) &&
                condition.apply(criteria.getChefService(), copy.getChefService()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
