package com.dgi.gestionactifs.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TransfertActifCriteriaTest {

    @Test
    void newTransfertActifCriteriaHasAllFiltersNullTest() {
        var transfertActifCriteria = new TransfertActifCriteria();
        assertThat(transfertActifCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void transfertActifCriteriaFluentMethodsCreatesFiltersTest() {
        var transfertActifCriteria = new TransfertActifCriteria();

        setAllFilters(transfertActifCriteria);

        assertThat(transfertActifCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void transfertActifCriteriaCopyCreatesNullFilterTest() {
        var transfertActifCriteria = new TransfertActifCriteria();
        var copy = transfertActifCriteria.copy();

        assertThat(transfertActifCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(transfertActifCriteria)
        );
    }

    @Test
    void transfertActifCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var transfertActifCriteria = new TransfertActifCriteria();
        setAllFilters(transfertActifCriteria);

        var copy = transfertActifCriteria.copy();

        assertThat(transfertActifCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(transfertActifCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var transfertActifCriteria = new TransfertActifCriteria();

        assertThat(transfertActifCriteria).hasToString("TransfertActifCriteria{}");
    }

    private static void setAllFilters(TransfertActifCriteria transfertActifCriteria) {
        transfertActifCriteria.id();
        transfertActifCriteria.observation();
        transfertActifCriteria.transfertId();
        transfertActifCriteria.actifId();
        transfertActifCriteria.distinct();
    }

    private static Condition<TransfertActifCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getObservation()) &&
                condition.apply(criteria.getTransfertId()) &&
                condition.apply(criteria.getActifId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TransfertActifCriteria> copyFiltersAre(
        TransfertActifCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getObservation(), copy.getObservation()) &&
                condition.apply(criteria.getTransfertId(), copy.getTransfertId()) &&
                condition.apply(criteria.getActifId(), copy.getActifId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
