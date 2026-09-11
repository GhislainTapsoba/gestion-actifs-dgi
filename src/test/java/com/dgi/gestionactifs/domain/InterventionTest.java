package com.dgi.gestionactifs.domain;

import static com.dgi.gestionactifs.domain.InterventionTestSamples.*;
import static com.dgi.gestionactifs.domain.PanneTestSamples.*;
import static com.dgi.gestionactifs.domain.PlanningMaintenanceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.dgi.gestionactifs.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class InterventionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Intervention.class);
        Intervention intervention1 = getInterventionSample1();
        Intervention intervention2 = new Intervention();
        assertThat(intervention1).isNotEqualTo(intervention2);

        intervention2.setId(intervention1.getId());
        assertThat(intervention1).isEqualTo(intervention2);

        intervention2 = getInterventionSample2();
        assertThat(intervention1).isNotEqualTo(intervention2);
    }

    @Test
    void panneTest() {
        Intervention intervention = getInterventionRandomSampleGenerator();
        Panne panneBack = getPanneRandomSampleGenerator();

        intervention.setPanne(panneBack);
        assertThat(intervention.getPanne()).isEqualTo(panneBack);

        intervention.panne(null);
        assertThat(intervention.getPanne()).isNull();
    }

    @Test
    void planningTest() {
        Intervention intervention = getInterventionRandomSampleGenerator();
        PlanningMaintenance planningMaintenanceBack = getPlanningMaintenanceRandomSampleGenerator();

        intervention.addPlanning(planningMaintenanceBack);
        assertThat(intervention.getPlannings()).containsOnly(planningMaintenanceBack);
        assertThat(planningMaintenanceBack.getInterventions()).containsOnly(intervention);

        intervention.removePlanning(planningMaintenanceBack);
        assertThat(intervention.getPlannings()).doesNotContain(planningMaintenanceBack);
        assertThat(planningMaintenanceBack.getInterventions()).doesNotContain(intervention);

        intervention.plannings(new HashSet<>(Set.of(planningMaintenanceBack)));
        assertThat(intervention.getPlannings()).containsOnly(planningMaintenanceBack);
        assertThat(planningMaintenanceBack.getInterventions()).containsOnly(intervention);

        intervention.setPlannings(new HashSet<>());
        assertThat(intervention.getPlannings()).doesNotContain(planningMaintenanceBack);
        assertThat(planningMaintenanceBack.getInterventions()).doesNotContain(intervention);
    }
}
