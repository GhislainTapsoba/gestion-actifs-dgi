package com.dgi.gestionactifs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.dgi.gestionactifs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransfertActifDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransfertActifDTO.class);
        TransfertActifDTO transfertActifDTO1 = new TransfertActifDTO();
        transfertActifDTO1.setId(1L);
        TransfertActifDTO transfertActifDTO2 = new TransfertActifDTO();
        assertThat(transfertActifDTO1).isNotEqualTo(transfertActifDTO2);
        transfertActifDTO2.setId(transfertActifDTO1.getId());
        assertThat(transfertActifDTO1).isEqualTo(transfertActifDTO2);
        transfertActifDTO2.setId(2L);
        assertThat(transfertActifDTO1).isNotEqualTo(transfertActifDTO2);
        transfertActifDTO1.setId(null);
        assertThat(transfertActifDTO1).isNotEqualTo(transfertActifDTO2);
    }
}
