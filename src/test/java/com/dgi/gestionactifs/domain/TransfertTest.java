package com.dgi.gestionactifs.domain;

import static com.dgi.gestionactifs.domain.ActifTestSamples.*;
import static com.dgi.gestionactifs.domain.TransfertTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.dgi.gestionactifs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransfertTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Transfert.class);
        Transfert transfert1 = getTransfertSample1();
        Transfert transfert2 = new Transfert();
        assertThat(transfert1).isNotEqualTo(transfert2);

        transfert2.setId(transfert1.getId());
        assertThat(transfert1).isEqualTo(transfert2);

        transfert2 = getTransfertSample2();
        assertThat(transfert1).isNotEqualTo(transfert2);
    }

    @Test
    void actifTest() {
        Transfert transfert = getTransfertRandomSampleGenerator();
        Actif actifBack = getActifRandomSampleGenerator();

        transfert.setActif(actifBack);
        assertThat(transfert.getActif()).isEqualTo(actifBack);

        transfert.actif(null);
        assertThat(transfert.getActif()).isNull();
    }
}
