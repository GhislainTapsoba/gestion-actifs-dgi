package com.dgi.gestionactifs.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EquipementRecensementCriteriaTest {

    @Test
    void newEquipementRecensementCriteriaHasAllFiltersNullTest() {
        var equipementRecensementCriteria = new EquipementRecensementCriteria();
        assertThat(equipementRecensementCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void equipementRecensementCriteriaFluentMethodsCreatesFiltersTest() {
        var equipementRecensementCriteria = new EquipementRecensementCriteria();

        setAllFilters(equipementRecensementCriteria);

        assertThat(equipementRecensementCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void equipementRecensementCriteriaCopyCreatesNullFilterTest() {
        var equipementRecensementCriteria = new EquipementRecensementCriteria();
        var copy = equipementRecensementCriteria.copy();

        assertThat(equipementRecensementCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(equipementRecensementCriteria)
        );
    }

    @Test
    void equipementRecensementCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var equipementRecensementCriteria = new EquipementRecensementCriteria();
        setAllFilters(equipementRecensementCriteria);

        var copy = equipementRecensementCriteria.copy();

        assertThat(equipementRecensementCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(equipementRecensementCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var equipementRecensementCriteria = new EquipementRecensementCriteria();

        assertThat(equipementRecensementCriteria).hasToString("EquipementRecensementCriteria{}");
    }

    private static void setAllFilters(EquipementRecensementCriteria equipementRecensementCriteria) {
        equipementRecensementCriteria.id();
        equipementRecensementCriteria.etatConstate();
        equipementRecensementCriteria.dateConstat();
        equipementRecensementCriteria.emplacementConstate();
        equipementRecensementCriteria.anomalieConstatee();
        equipementRecensementCriteria.recensementId();
        equipementRecensementCriteria.actifId();
        equipementRecensementCriteria.distinct();
    }

    private static Condition<EquipementRecensementCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getEtatConstate()) &&
                condition.apply(criteria.getDateConstat()) &&
                condition.apply(criteria.getEmplacementConstate()) &&
                condition.apply(criteria.getAnomalieConstatee()) &&
                condition.apply(criteria.getRecensementId()) &&
                condition.apply(criteria.getActifId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<EquipementRecensementCriteria> copyFiltersAre(
        EquipementRecensementCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getEtatConstate(), copy.getEtatConstate()) &&
                condition.apply(criteria.getDateConstat(), copy.getDateConstat()) &&
                condition.apply(criteria.getEmplacementConstate(), copy.getEmplacementConstate()) &&
                condition.apply(criteria.getAnomalieConstatee(), copy.getAnomalieConstatee()) &&
                condition.apply(criteria.getRecensementId(), copy.getRecensementId()) &&
                condition.apply(criteria.getActifId(), copy.getActifId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
