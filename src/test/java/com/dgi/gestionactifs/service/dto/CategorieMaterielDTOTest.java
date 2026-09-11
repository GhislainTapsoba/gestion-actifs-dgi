package com.dgi.gestionactifs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.dgi.gestionactifs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CategorieMaterielDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategorieMaterielDTO.class);
        CategorieMaterielDTO categorieMaterielDTO1 = new CategorieMaterielDTO();
        categorieMaterielDTO1.setId(1L);
        CategorieMaterielDTO categorieMaterielDTO2 = new CategorieMaterielDTO();
        assertThat(categorieMaterielDTO1).isNotEqualTo(categorieMaterielDTO2);
        categorieMaterielDTO2.setId(categorieMaterielDTO1.getId());
        assertThat(categorieMaterielDTO1).isEqualTo(categorieMaterielDTO2);
        categorieMaterielDTO2.setId(2L);
        assertThat(categorieMaterielDTO1).isNotEqualTo(categorieMaterielDTO2);
        categorieMaterielDTO1.setId(null);
        assertThat(categorieMaterielDTO1).isNotEqualTo(categorieMaterielDTO2);
    }
}
