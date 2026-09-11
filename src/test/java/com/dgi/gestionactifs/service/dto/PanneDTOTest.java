package com.dgi.gestionactifs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.dgi.gestionactifs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PanneDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PanneDTO.class);
        PanneDTO panneDTO1 = new PanneDTO();
        panneDTO1.setId(1L);
        PanneDTO panneDTO2 = new PanneDTO();
        assertThat(panneDTO1).isNotEqualTo(panneDTO2);
        panneDTO2.setId(panneDTO1.getId());
        assertThat(panneDTO1).isEqualTo(panneDTO2);
        panneDTO2.setId(2L);
        assertThat(panneDTO1).isNotEqualTo(panneDTO2);
        panneDTO1.setId(null);
        assertThat(panneDTO1).isNotEqualTo(panneDTO2);
    }
}
