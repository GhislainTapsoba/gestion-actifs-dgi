package com.dgi.gestionactifs.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class RecensementCriteriaTest {

    @Test
    void newRecensementCriteriaHasAllFiltersNullTest() {
        var recensementCriteria = new RecensementCriteria();
        assertThat(recensementCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void recensementCriteriaFluentMethodsCreatesFiltersTest() {
        var recensementCriteria = new RecensementCriteria();

        setAllFilters(recensementCriteria);

        assertThat(recensementCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void recensementCriteriaCopyCreatesNullFilterTest() {
        var recensementCriteria = new RecensementCriteria();
        var copy = recensementCriteria.copy();

        assertThat(recensementCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(recensementCriteria)
        );
    }

    @Test
    void recensementCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var recensementCriteria = new RecensementCriteria();
        setAllFilters(recensementCriteria);

        var copy = recensementCriteria.copy();

        assertThat(recensementCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(recensementCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var recensementCriteria = new RecensementCriteria();

        assertThat(recensementCriteria).hasToString("RecensementCriteria{}");
    }

    private static void setAllFilters(RecensementCriteria recensementCriteria) {
        recensementCriteria.id();
        recensementCriteria.dateDebut();
        recensementCriteria.dateFin();
        recensementCriteria.statut();
        recensementCriteria.distinct();
    }

    private static Condition<RecensementCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDateDebut()) &&
                condition.apply(criteria.getDateFin()) &&
                condition.apply(criteria.getStatut()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<RecensementCriteria> copyFiltersAre(RecensementCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDateDebut(), copy.getDateDebut()) &&
                condition.apply(criteria.getDateFin(), copy.getDateFin()) &&
                condition.apply(criteria.getStatut(), copy.getStatut()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
