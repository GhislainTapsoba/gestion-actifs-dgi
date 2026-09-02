package com.dgi.gestionactifs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.dgi.gestionactifs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransfertDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransfertDTO.class);
        TransfertDTO transfertDTO1 = new TransfertDTO();
        transfertDTO1.setId(1L);
        TransfertDTO transfertDTO2 = new TransfertDTO();
        assertThat(transfertDTO1).isNotEqualTo(transfertDTO2);
        transfertDTO2.setId(transfertDTO1.getId());
        assertThat(transfertDTO1).isEqualTo(transfertDTO2);
        transfertDTO2.setId(2L);
        assertThat(transfertDTO1).isNotEqualTo(transfertDTO2);
        transfertDTO1.setId(null);
        assertThat(transfertDTO1).isNotEqualTo(transfertDTO2);
    }
}
