package com.dgi.gestionactifs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.dgi.gestionactifs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ActifDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActifDTO.class);
        ActifDTO actifDTO1 = new ActifDTO();
        actifDTO1.setId(1L);
        ActifDTO actifDTO2 = new ActifDTO();
        assertThat(actifDTO1).isNotEqualTo(actifDTO2);
        actifDTO2.setId(actifDTO1.getId());
        assertThat(actifDTO1).isEqualTo(actifDTO2);
        actifDTO2.setId(2L);
        assertThat(actifDTO1).isNotEqualTo(actifDTO2);
        actifDTO1.setId(null);
        assertThat(actifDTO1).isNotEqualTo(actifDTO2);
    }
}
