package com.dgi.gestionactifs.domain;

import static com.dgi.gestionactifs.domain.ActifTestSamples.*;
import static com.dgi.gestionactifs.domain.AffectationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.dgi.gestionactifs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AffectationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Affectation.class);
        Affectation affectation1 = getAffectationSample1();
        Affectation affectation2 = new Affectation();
        assertThat(affectation1).isNotEqualTo(affectation2);

        affectation2.setId(affectation1.getId());
        assertThat(affectation1).isEqualTo(affectation2);

        affectation2 = getAffectationSample2();
        assertThat(affectation1).isNotEqualTo(affectation2);
    }

    @Test
    void actifTest() {
        Affectation affectation = getAffectationRandomSampleGenerator();
        Actif actifBack = getActifRandomSampleGenerator();

        affectation.setActif(actifBack);
        assertThat(affectation.getActif()).isEqualTo(actifBack);

        affectation.actif(null);
        assertThat(affectation.getActif()).isNull();
    }
}
