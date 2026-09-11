package com.dgi.gestionactifs.domain;

import static com.dgi.gestionactifs.domain.ServiceDgiTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.dgi.gestionactifs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ServiceDgiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceDgi.class);
        ServiceDgi serviceDgi1 = getServiceDgiSample1();
        ServiceDgi serviceDgi2 = new ServiceDgi();
        assertThat(serviceDgi1).isNotEqualTo(serviceDgi2);

        serviceDgi2.setId(serviceDgi1.getId());
        assertThat(serviceDgi1).isEqualTo(serviceDgi2);

        serviceDgi2 = getServiceDgiSample2();
        assertThat(serviceDgi1).isNotEqualTo(serviceDgi2);
    }
}
