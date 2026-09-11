package com.dgi.gestionactifs.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class InterventionCriteriaTest {

    @Test
    void newInterventionCriteriaHasAllFiltersNullTest() {
        var interventionCriteria = new InterventionCriteria();
        assertThat(interventionCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void interventionCriteriaFluentMethodsCreatesFiltersTest() {
        var interventionCriteria = new InterventionCriteria();

        setAllFilters(interventionCriteria);

        assertThat(interventionCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void interventionCriteriaCopyCreatesNullFilterTest() {
        var interventionCriteria = new InterventionCriteria();
        var copy = interventionCriteria.copy();

        assertThat(interventionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(interventionCriteria)
        );
    }

    @Test
    void interventionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var interventionCriteria = new InterventionCriteria();
        setAllFilters(interventionCriteria);

        var copy = interventionCriteria.copy();

        assertThat(interventionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(interventionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var interventionCriteria = new InterventionCriteria();

        assertThat(interventionCriteria).hasToString("InterventionCriteria{}");
    }

    private static void setAllFilters(InterventionCriteria interventionCriteria) {
        interventionCriteria.id();
        interventionCriteria.dateDeclaration();
        interventionCriteria.typeIntervention();
        interventionCriteria.statut();
        interventionCriteria.description();
        interventionCriteria.panneId();
        interventionCriteria.planningId();
        interventionCriteria.distinct();
    }

    private static Condition<InterventionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDateDeclaration()) &&
                condition.apply(criteria.getTypeIntervention()) &&
                condition.apply(criteria.getStatut()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getPanneId()) &&
                condition.apply(criteria.getPlanningId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<InterventionCriteria> copyFiltersAre(
        InterventionCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDateDeclaration(), copy.getDateDeclaration()) &&
                condition.apply(criteria.getTypeIntervention(), copy.getTypeIntervention()) &&
                condition.apply(criteria.getStatut(), copy.getStatut()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getPanneId(), copy.getPanneId()) &&
                condition.apply(criteria.getPlanningId(), copy.getPlanningId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
