package com.dgi.gestionactifs.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ActifCriteriaTest {

    @Test
    void newActifCriteriaHasAllFiltersNullTest() {
        var actifCriteria = new ActifCriteria();
        assertThat(actifCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void actifCriteriaFluentMethodsCreatesFiltersTest() {
        var actifCriteria = new ActifCriteria();

        setAllFilters(actifCriteria);

        assertThat(actifCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void actifCriteriaCopyCreatesNullFilterTest() {
        var actifCriteria = new ActifCriteria();
        var copy = actifCriteria.copy();

        assertThat(actifCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(actifCriteria)
        );
    }

    @Test
    void actifCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var actifCriteria = new ActifCriteria();
        setAllFilters(actifCriteria);

        var copy = actifCriteria.copy();

        assertThat(actifCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(actifCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var actifCriteria = new ActifCriteria();

        assertThat(actifCriteria).hasToString("ActifCriteria{}");
    }

    private static void setAllFilters(ActifCriteria actifCriteria) {
        actifCriteria.id();
        actifCriteria.codeInventaire();
        actifCriteria.designation();
        actifCriteria.marque();
        actifCriteria.modele();
        actifCriteria.numeroSerie();
        actifCriteria.codeBarre();
        actifCriteria.type();
        actifCriteria.etat();
        actifCriteria.localisation();
        actifCriteria.dateAcquisition();
        actifCriteria.valeurAcquisition();
        actifCriteria.categorieId();
        actifCriteria.distinct();
    }

    private static Condition<ActifCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCodeInventaire()) &&
                condition.apply(criteria.getDesignation()) &&
                condition.apply(criteria.getMarque()) &&
                condition.apply(criteria.getModele()) &&
                condition.apply(criteria.getNumeroSerie()) &&
                condition.apply(criteria.getCodeBarre()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getEtat()) &&
                condition.apply(criteria.getLocalisation()) &&
                condition.apply(criteria.getDateAcquisition()) &&
                condition.apply(criteria.getValeurAcquisition()) &&
                condition.apply(criteria.getCategorieId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ActifCriteria> copyFiltersAre(ActifCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCodeInventaire(), copy.getCodeInventaire()) &&
                condition.apply(criteria.getDesignation(), copy.getDesignation()) &&
                condition.apply(criteria.getMarque(), copy.getMarque()) &&
                condition.apply(criteria.getModele(), copy.getModele()) &&
                condition.apply(criteria.getNumeroSerie(), copy.getNumeroSerie()) &&
                condition.apply(criteria.getCodeBarre(), copy.getCodeBarre()) &&
                condition.apply(criteria.getType(), copy.getType()) &&
                condition.apply(criteria.getEtat(), copy.getEtat()) &&
                condition.apply(criteria.getLocalisation(), copy.getLocalisation()) &&
                condition.apply(criteria.getDateAcquisition(), copy.getDateAcquisition()) &&
                condition.apply(criteria.getValeurAcquisition(), copy.getValeurAcquisition()) &&
                condition.apply(criteria.getCategorieId(), copy.getCategorieId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
