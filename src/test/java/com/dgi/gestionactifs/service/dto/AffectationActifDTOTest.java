package com.dgi.gestionactifs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.dgi.gestionactifs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AffectationActifDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AffectationActifDTO.class);
        AffectationActifDTO affectationActifDTO1 = new AffectationActifDTO();
        affectationActifDTO1.setId(1L);
        AffectationActifDTO affectationActifDTO2 = new AffectationActifDTO();
        assertThat(affectationActifDTO1).isNotEqualTo(affectationActifDTO2);
        affectationActifDTO2.setId(affectationActifDTO1.getId());
        assertThat(affectationActifDTO1).isEqualTo(affectationActifDTO2);
        affectationActifDTO2.setId(2L);
        assertThat(affectationActifDTO1).isNotEqualTo(affectationActifDTO2);
        affectationActifDTO1.setId(null);
        assertThat(affectationActifDTO1).isNotEqualTo(affectationActifDTO2);
    }
}
