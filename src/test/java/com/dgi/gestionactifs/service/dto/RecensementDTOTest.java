package com.dgi.gestionactifs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.dgi.gestionactifs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RecensementDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecensementDTO.class);
        RecensementDTO recensementDTO1 = new RecensementDTO();
        recensementDTO1.setId(1L);
        RecensementDTO recensementDTO2 = new RecensementDTO();
        assertThat(recensementDTO1).isNotEqualTo(recensementDTO2);
        recensementDTO2.setId(recensementDTO1.getId());
        assertThat(recensementDTO1).isEqualTo(recensementDTO2);
        recensementDTO2.setId(2L);
        assertThat(recensementDTO1).isNotEqualTo(recensementDTO2);
        recensementDTO1.setId(null);
        assertThat(recensementDTO1).isNotEqualTo(recensementDTO2);
    }
}
