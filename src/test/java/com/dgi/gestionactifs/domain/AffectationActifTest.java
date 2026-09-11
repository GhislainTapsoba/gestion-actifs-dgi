package com.dgi.gestionactifs.domain;

import static com.dgi.gestionactifs.domain.ActifTestSamples.*;
import static com.dgi.gestionactifs.domain.AffectationActifTestSamples.*;
import static com.dgi.gestionactifs.domain.AffectationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.dgi.gestionactifs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AffectationActifTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AffectationActif.class);
        AffectationActif affectationActif1 = getAffectationActifSample1();
        AffectationActif affectationActif2 = new AffectationActif();
        assertThat(affectationActif1).isNotEqualTo(affectationActif2);

        affectationActif2.setId(affectationActif1.getId());
        assertThat(affectationActif1).isEqualTo(affectationActif2);

        affectationActif2 = getAffectationActifSample2();
        assertThat(affectationActif1).isNotEqualTo(affectationActif2);
    }

    @Test
    void affectationTest() {
        AffectationActif affectationActif = getAffectationActifRandomSampleGenerator();
        Affectation affectationBack = getAffectationRandomSampleGenerator();

        affectationActif.setAffectation(affectationBack);
        assertThat(affectationActif.getAffectation()).isEqualTo(affectationBack);

        affectationActif.affectation(null);
        assertThat(affectationActif.getAffectation()).isNull();
    }

    @Test
    void actifTest() {
        AffectationActif affectationActif = getAffectationActifRandomSampleGenerator();
        Actif actifBack = getActifRandomSampleGenerator();

        affectationActif.setActif(actifBack);
        assertThat(affectationActif.getActif()).isEqualTo(actifBack);

        affectationActif.actif(null);
        assertThat(affectationActif.getActif()).isNull();
    }
}
