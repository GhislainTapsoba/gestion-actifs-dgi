package com.dgi.gestionactifs.domain;

import static com.dgi.gestionactifs.domain.ServiceDgiTestSamples.*;
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
    void serviceOrigineTest() {
        Transfert transfert = getTransfertRandomSampleGenerator();
        ServiceDgi serviceDgiBack = getServiceDgiRandomSampleGenerator();

        transfert.setServiceOrigine(serviceDgiBack);
        assertThat(transfert.getServiceOrigine()).isEqualTo(serviceDgiBack);

        transfert.serviceOrigine(null);
        assertThat(transfert.getServiceOrigine()).isNull();
    }

    @Test
    void serviceDestinataireTest() {
        Transfert transfert = getTransfertRandomSampleGenerator();
        ServiceDgi serviceDgiBack = getServiceDgiRandomSampleGenerator();

        transfert.setServiceDestinataire(serviceDgiBack);
        assertThat(transfert.getServiceDestinataire()).isEqualTo(serviceDgiBack);

        transfert.serviceDestinataire(null);
        assertThat(transfert.getServiceDestinataire()).isNull();
    }
}
