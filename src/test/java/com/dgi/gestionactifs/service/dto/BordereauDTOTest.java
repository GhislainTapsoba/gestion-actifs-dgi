package com.dgi.gestionactifs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.dgi.gestionactifs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BordereauDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BordereauDTO.class);
        BordereauDTO bordereauDTO1 = new BordereauDTO();
        bordereauDTO1.setId(1L);
        BordereauDTO bordereauDTO2 = new BordereauDTO();
        assertThat(bordereauDTO1).isNotEqualTo(bordereauDTO2);
        bordereauDTO2.setId(bordereauDTO1.getId());
        assertThat(bordereauDTO1).isEqualTo(bordereauDTO2);
        bordereauDTO2.setId(2L);
        assertThat(bordereauDTO1).isNotEqualTo(bordereauDTO2);
        bordereauDTO1.setId(null);
        assertThat(bordereauDTO1).isNotEqualTo(bordereauDTO2);
    }
}
