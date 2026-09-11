package com.dgi.gestionactifs.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AffectationCriteriaTest {

    @Test
    void newAffectationCriteriaHasAllFiltersNullTest() {
        var affectationCriteria = new AffectationCriteria();
        assertThat(affectationCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void affectationCriteriaFluentMethodsCreatesFiltersTest() {
        var affectationCriteria = new AffectationCriteria();

        setAllFilters(affectationCriteria);

        assertThat(affectationCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void affectationCriteriaCopyCreatesNullFilterTest() {
        var affectationCriteria = new AffectationCriteria();
        var copy = affectationCriteria.copy();

        assertThat(affectationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(affectationCriteria)
        );
    }

    @Test
    void affectationCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var affectationCriteria = new AffectationCriteria();
        setAllFilters(affectationCriteria);

        var copy = affectationCriteria.copy();

        assertThat(affectationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(affectationCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var affectationCriteria = new AffectationCriteria();

        assertThat(affectationCriteria).hasToString("AffectationCriteria{}");
    }

    private static void setAllFilters(AffectationCriteria affectationCriteria) {
        affectationCriteria.id();
        affectationCriteria.dateAffectation();
        affectationCriteria.motif();
        affectationCriteria.dateRestitution();
        affectationCriteria.agentId();
        affectationCriteria.distinct();
    }

    private static Condition<AffectationCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDateAffectation()) &&
                condition.apply(criteria.getMotif()) &&
                condition.apply(criteria.getDateRestitution()) &&
                condition.apply(criteria.getAgentId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AffectationCriteria> copyFiltersAre(AffectationCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDateAffectation(), copy.getDateAffectation()) &&
                condition.apply(criteria.getMotif(), copy.getMotif()) &&
                condition.apply(criteria.getDateRestitution(), copy.getDateRestitution()) &&
                condition.apply(criteria.getAgentId(), copy.getAgentId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
