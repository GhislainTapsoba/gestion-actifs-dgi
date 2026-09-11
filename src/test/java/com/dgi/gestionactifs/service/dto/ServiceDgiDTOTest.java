package com.dgi.gestionactifs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.dgi.gestionactifs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ServiceDgiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceDgiDTO.class);
        ServiceDgiDTO serviceDgiDTO1 = new ServiceDgiDTO();
        serviceDgiDTO1.setId(1L);
        ServiceDgiDTO serviceDgiDTO2 = new ServiceDgiDTO();
        assertThat(serviceDgiDTO1).isNotEqualTo(serviceDgiDTO2);
        serviceDgiDTO2.setId(serviceDgiDTO1.getId());
        assertThat(serviceDgiDTO1).isEqualTo(serviceDgiDTO2);
        serviceDgiDTO2.setId(2L);
        assertThat(serviceDgiDTO1).isNotEqualTo(serviceDgiDTO2);
        serviceDgiDTO1.setId(null);
        assertThat(serviceDgiDTO1).isNotEqualTo(serviceDgiDTO2);
    }
}
