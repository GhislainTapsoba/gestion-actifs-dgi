package com.dgi.gestionactifs.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MaintenanceCriteriaTest {

    @Test
    void newMaintenanceCriteriaHasAllFiltersNullTest() {
        var maintenanceCriteria = new MaintenanceCriteria();
        assertThat(maintenanceCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void maintenanceCriteriaFluentMethodsCreatesFiltersTest() {
        var maintenanceCriteria = new MaintenanceCriteria();

        setAllFilters(maintenanceCriteria);

        assertThat(maintenanceCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void maintenanceCriteriaCopyCreatesNullFilterTest() {
        var maintenanceCriteria = new MaintenanceCriteria();
        var copy = maintenanceCriteria.copy();

        assertThat(maintenanceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(maintenanceCriteria)
        );
    }

    @Test
    void maintenanceCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var maintenanceCriteria = new MaintenanceCriteria();
        setAllFilters(maintenanceCriteria);

        var copy = maintenanceCriteria.copy();

        assertThat(maintenanceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(maintenanceCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var maintenanceCriteria = new MaintenanceCriteria();

        assertThat(maintenanceCriteria).hasToString("MaintenanceCriteria{}");
    }

    private static void setAllFilters(MaintenanceCriteria maintenanceCriteria) {
        maintenanceCriteria.id();
        maintenanceCriteria.typeMaintenance();
        maintenanceCriteria.datePanne();
        maintenanceCriteria.statut();
        maintenanceCriteria.dateCloture();
        maintenanceCriteria.actifId();
        maintenanceCriteria.technicienId();
        maintenanceCriteria.distinct();
    }

    private static Condition<MaintenanceCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTypeMaintenance()) &&
                condition.apply(criteria.getDatePanne()) &&
                condition.apply(criteria.getStatut()) &&
                condition.apply(criteria.getDateCloture()) &&
                condition.apply(criteria.getActifId()) &&
                condition.apply(criteria.getTechnicienId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MaintenanceCriteria> copyFiltersAre(MaintenanceCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTypeMaintenance(), copy.getTypeMaintenance()) &&
                condition.apply(criteria.getDatePanne(), copy.getDatePanne()) &&
                condition.apply(criteria.getStatut(), copy.getStatut()) &&
                condition.apply(criteria.getDateCloture(), copy.getDateCloture()) &&
                condition.apply(criteria.getActifId(), copy.getActifId()) &&
                condition.apply(criteria.getTechnicienId(), copy.getTechnicienId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
