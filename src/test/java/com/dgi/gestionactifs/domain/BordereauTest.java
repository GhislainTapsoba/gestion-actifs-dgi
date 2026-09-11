package com.dgi.gestionactifs.domain;

import static com.dgi.gestionactifs.domain.AffectationTestSamples.*;
import static com.dgi.gestionactifs.domain.BordereauTestSamples.*;
import static com.dgi.gestionactifs.domain.TransfertTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.dgi.gestionactifs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BordereauTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bordereau.class);
        Bordereau bordereau1 = getBordereauSample1();
        Bordereau bordereau2 = new Bordereau();
        assertThat(bordereau1).isNotEqualTo(bordereau2);

        bordereau2.setId(bordereau1.getId());
        assertThat(bordereau1).isEqualTo(bordereau2);

        bordereau2 = getBordereauSample2();
        assertThat(bordereau1).isNotEqualTo(bordereau2);
    }

    @Test
    void transfertTest() {
        Bordereau bordereau = getBordereauRandomSampleGenerator();
        Transfert transfertBack = getTransfertRandomSampleGenerator();

        bordereau.setTransfert(transfertBack);
        assertThat(bordereau.getTransfert()).isEqualTo(transfertBack);

        bordereau.transfert(null);
        assertThat(bordereau.getTransfert()).isNull();
    }

    @Test
    void affectationTest() {
        Bordereau bordereau = getBordereauRandomSampleGenerator();
        Affectation affectationBack = getAffectationRandomSampleGenerator();

        bordereau.setAffectation(affectationBack);
        assertThat(bordereau.getAffectation()).isEqualTo(affectationBack);

        bordereau.affectation(null);
        assertThat(bordereau.getAffectation()).isNull();
    }
}
