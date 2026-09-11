package com.dgi.gestionactifs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.dgi.gestionactifs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EquipementRecensementDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EquipementRecensementDTO.class);
        EquipementRecensementDTO equipementRecensementDTO1 = new EquipementRecensementDTO();
        equipementRecensementDTO1.setId(1L);
        EquipementRecensementDTO equipementRecensementDTO2 = new EquipementRecensementDTO();
        assertThat(equipementRecensementDTO1).isNotEqualTo(equipementRecensementDTO2);
        equipementRecensementDTO2.setId(equipementRecensementDTO1.getId());
        assertThat(equipementRecensementDTO1).isEqualTo(equipementRecensementDTO2);
        equipementRecensementDTO2.setId(2L);
        assertThat(equipementRecensementDTO1).isNotEqualTo(equipementRecensementDTO2);
        equipementRecensementDTO1.setId(null);
        assertThat(equipementRecensementDTO1).isNotEqualTo(equipementRecensementDTO2);
    }
}
