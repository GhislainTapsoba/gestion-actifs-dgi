package com.dgi.gestionactifs.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class BordereauCriteriaTest {

    @Test
    void newBordereauCriteriaHasAllFiltersNullTest() {
        var bordereauCriteria = new BordereauCriteria();
        assertThat(bordereauCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void bordereauCriteriaFluentMethodsCreatesFiltersTest() {
        var bordereauCriteria = new BordereauCriteria();

        setAllFilters(bordereauCriteria);

        assertThat(bordereauCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void bordereauCriteriaCopyCreatesNullFilterTest() {
        var bordereauCriteria = new BordereauCriteria();
        var copy = bordereauCriteria.copy();

        assertThat(bordereauCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(bordereauCriteria)
        );
    }

    @Test
    void bordereauCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var bordereauCriteria = new BordereauCriteria();
        setAllFilters(bordereauCriteria);

        var copy = bordereauCriteria.copy();

        assertThat(bordereauCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(bordereauCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var bordereauCriteria = new BordereauCriteria();

        assertThat(bordereauCriteria).hasToString("BordereauCriteria{}");
    }

    private static void setAllFilters(BordereauCriteria bordereauCriteria) {
        bordereauCriteria.id();
        bordereauCriteria.numero();
        bordereauCriteria.dateEmission();
        bordereauCriteria.typeBordereau();
        bordereauCriteria.statutValidation();
        bordereauCriteria.dateValidation();
        bordereauCriteria.transfertId();
        bordereauCriteria.affectationId();
        bordereauCriteria.emetteurId();
        bordereauCriteria.distinct();
    }

    private static Condition<BordereauCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNumero()) &&
                condition.apply(criteria.getDateEmission()) &&
                condition.apply(criteria.getTypeBordereau()) &&
                condition.apply(criteria.getStatutValidation()) &&
                condition.apply(criteria.getDateValidation()) &&
                condition.apply(criteria.getTransfertId()) &&
                condition.apply(criteria.getAffectationId()) &&
                condition.apply(criteria.getEmetteurId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<BordereauCriteria> copyFiltersAre(BordereauCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNumero(), copy.getNumero()) &&
                condition.apply(criteria.getDateEmission(), copy.getDateEmission()) &&
                condition.apply(criteria.getTypeBordereau(), copy.getTypeBordereau()) &&
                condition.apply(criteria.getStatutValidation(), copy.getStatutValidation()) &&
                condition.apply(criteria.getDateValidation(), copy.getDateValidation()) &&
                condition.apply(criteria.getTransfertId(), copy.getTransfertId()) &&
                condition.apply(criteria.getAffectationId(), copy.getAffectationId()) &&
                condition.apply(criteria.getEmetteurId(), copy.getEmetteurId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
