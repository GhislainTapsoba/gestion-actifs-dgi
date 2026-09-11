package com.dgi.gestionactifs.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AffectationActifCriteriaTest {

    @Test
    void newAffectationActifCriteriaHasAllFiltersNullTest() {
        var affectationActifCriteria = new AffectationActifCriteria();
        assertThat(affectationActifCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void affectationActifCriteriaFluentMethodsCreatesFiltersTest() {
        var affectationActifCriteria = new AffectationActifCriteria();

        setAllFilters(affectationActifCriteria);

        assertThat(affectationActifCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void affectationActifCriteriaCopyCreatesNullFilterTest() {
        var affectationActifCriteria = new AffectationActifCriteria();
        var copy = affectationActifCriteria.copy();

        assertThat(affectationActifCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(affectationActifCriteria)
        );
    }

    @Test
    void affectationActifCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var affectationActifCriteria = new AffectationActifCriteria();
        setAllFilters(affectationActifCriteria);

        var copy = affectationActifCriteria.copy();

        assertThat(affectationActifCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(affectationActifCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var affectationActifCriteria = new AffectationActifCriteria();

        assertThat(affectationActifCriteria).hasToString("AffectationActifCriteria{}");
    }

    private static void setAllFilters(AffectationActifCriteria affectationActifCriteria) {
        affectationActifCriteria.id();
        affectationActifCriteria.observation();
        affectationActifCriteria.statut();
        affectationActifCriteria.affectationId();
        affectationActifCriteria.actifId();
        affectationActifCriteria.distinct();
    }

    private static Condition<AffectationActifCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getObservation()) &&
                condition.apply(criteria.getStatut()) &&
                condition.apply(criteria.getAffectationId()) &&
                condition.apply(criteria.getActifId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AffectationActifCriteria> copyFiltersAre(
        AffectationActifCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getObservation(), copy.getObservation()) &&
                condition.apply(criteria.getStatut(), copy.getStatut()) &&
                condition.apply(criteria.getAffectationId(), copy.getAffectationId()) &&
                condition.apply(criteria.getActifId(), copy.getActifId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
