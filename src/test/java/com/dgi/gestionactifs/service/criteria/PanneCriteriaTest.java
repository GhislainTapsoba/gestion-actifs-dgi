package com.dgi.gestionactifs.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PanneCriteriaTest {

    @Test
    void newPanneCriteriaHasAllFiltersNullTest() {
        var panneCriteria = new PanneCriteria();
        assertThat(panneCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void panneCriteriaFluentMethodsCreatesFiltersTest() {
        var panneCriteria = new PanneCriteria();

        setAllFilters(panneCriteria);

        assertThat(panneCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void panneCriteriaCopyCreatesNullFilterTest() {
        var panneCriteria = new PanneCriteria();
        var copy = panneCriteria.copy();

        assertThat(panneCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(panneCriteria)
        );
    }

    @Test
    void panneCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var panneCriteria = new PanneCriteria();
        setAllFilters(panneCriteria);

        var copy = panneCriteria.copy();

        assertThat(panneCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(panneCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var panneCriteria = new PanneCriteria();

        assertThat(panneCriteria).hasToString("PanneCriteria{}");
    }

    private static void setAllFilters(PanneCriteria panneCriteria) {
        panneCriteria.id();
        panneCriteria.description();
        panneCriteria.dateDeclaration();
        panneCriteria.statutPanne();
        panneCriteria.actifId();
        panneCriteria.distinct();
    }

    private static Condition<PanneCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getDateDeclaration()) &&
                condition.apply(criteria.getStatutPanne()) &&
                condition.apply(criteria.getActifId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PanneCriteria> copyFiltersAre(PanneCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getDateDeclaration(), copy.getDateDeclaration()) &&
                condition.apply(criteria.getStatutPanne(), copy.getStatutPanne()) &&
                condition.apply(criteria.getActifId(), copy.getActifId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
