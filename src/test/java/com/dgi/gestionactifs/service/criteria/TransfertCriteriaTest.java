package com.dgi.gestionactifs.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TransfertCriteriaTest {

    @Test
    void newTransfertCriteriaHasAllFiltersNullTest() {
        var transfertCriteria = new TransfertCriteria();
        assertThat(transfertCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void transfertCriteriaFluentMethodsCreatesFiltersTest() {
        var transfertCriteria = new TransfertCriteria();

        setAllFilters(transfertCriteria);

        assertThat(transfertCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void transfertCriteriaCopyCreatesNullFilterTest() {
        var transfertCriteria = new TransfertCriteria();
        var copy = transfertCriteria.copy();

        assertThat(transfertCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(transfertCriteria)
        );
    }

    @Test
    void transfertCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var transfertCriteria = new TransfertCriteria();
        setAllFilters(transfertCriteria);

        var copy = transfertCriteria.copy();

        assertThat(transfertCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(transfertCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var transfertCriteria = new TransfertCriteria();

        assertThat(transfertCriteria).hasToString("TransfertCriteria{}");
    }

    private static void setAllFilters(TransfertCriteria transfertCriteria) {
        transfertCriteria.id();
        transfertCriteria.dateDemande();
        transfertCriteria.statut();
        transfertCriteria.commentaireRejet();
        transfertCriteria.dateTraitement();
        transfertCriteria.demandeurId();
        transfertCriteria.validateurId();
        transfertCriteria.actifId();
        transfertCriteria.distinct();
    }

    private static Condition<TransfertCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDateDemande()) &&
                condition.apply(criteria.getStatut()) &&
                condition.apply(criteria.getCommentaireRejet()) &&
                condition.apply(criteria.getDateTraitement()) &&
                condition.apply(criteria.getDemandeurId()) &&
                condition.apply(criteria.getValidateurId()) &&
                condition.apply(criteria.getActifId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TransfertCriteria> copyFiltersAre(TransfertCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDateDemande(), copy.getDateDemande()) &&
                condition.apply(criteria.getStatut(), copy.getStatut()) &&
                condition.apply(criteria.getCommentaireRejet(), copy.getCommentaireRejet()) &&
                condition.apply(criteria.getDateTraitement(), copy.getDateTraitement()) &&
                condition.apply(criteria.getDemandeurId(), copy.getDemandeurId()) &&
                condition.apply(criteria.getValidateurId(), copy.getValidateurId()) &&
                condition.apply(criteria.getActifId(), copy.getActifId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
