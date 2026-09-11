package com.dgi.gestionactifs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.dgi.gestionactifs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlanningMaintenanceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlanningMaintenanceDTO.class);
        PlanningMaintenanceDTO planningMaintenanceDTO1 = new PlanningMaintenanceDTO();
        planningMaintenanceDTO1.setId(1L);
        PlanningMaintenanceDTO planningMaintenanceDTO2 = new PlanningMaintenanceDTO();
        assertThat(planningMaintenanceDTO1).isNotEqualTo(planningMaintenanceDTO2);
        planningMaintenanceDTO2.setId(planningMaintenanceDTO1.getId());
        assertThat(planningMaintenanceDTO1).isEqualTo(planningMaintenanceDTO2);
        planningMaintenanceDTO2.setId(2L);
        assertThat(planningMaintenanceDTO1).isNotEqualTo(planningMaintenanceDTO2);
        planningMaintenanceDTO1.setId(null);
        assertThat(planningMaintenanceDTO1).isNotEqualTo(planningMaintenanceDTO2);
    }
}
