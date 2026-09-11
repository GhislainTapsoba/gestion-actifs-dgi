package com.dgi.gestionactifs.domain;

import static com.dgi.gestionactifs.domain.ActifTestSamples.*;
import static com.dgi.gestionactifs.domain.TransfertActifTestSamples.*;
import static com.dgi.gestionactifs.domain.TransfertTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.dgi.gestionactifs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransfertActifTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransfertActif.class);
        TransfertActif transfertActif1 = getTransfertActifSample1();
        TransfertActif transfertActif2 = new TransfertActif();
        assertThat(transfertActif1).isNotEqualTo(transfertActif2);

        transfertActif2.setId(transfertActif1.getId());
        assertThat(transfertActif1).isEqualTo(transfertActif2);

        transfertActif2 = getTransfertActifSample2();
        assertThat(transfertActif1).isNotEqualTo(transfertActif2);
    }

    @Test
    void transfertTest() {
        TransfertActif transfertActif = getTransfertActifRandomSampleGenerator();
        Transfert transfertBack = getTransfertRandomSampleGenerator();

        transfertActif.setTransfert(transfertBack);
        assertThat(transfertActif.getTransfert()).isEqualTo(transfertBack);

        transfertActif.transfert(null);
        assertThat(transfertActif.getTransfert()).isNull();
    }

    @Test
    void actifTest() {
        TransfertActif transfertActif = getTransfertActifRandomSampleGenerator();
        Actif actifBack = getActifRandomSampleGenerator();

        transfertActif.setActif(actifBack);
        assertThat(transfertActif.getActif()).isEqualTo(actifBack);

        transfertActif.actif(null);
        assertThat(transfertActif.getActif()).isNull();
    }
}
