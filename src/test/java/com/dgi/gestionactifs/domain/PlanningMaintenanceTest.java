package com.dgi.gestionactifs.domain;

import static com.dgi.gestionactifs.domain.InterventionTestSamples.*;
import static com.dgi.gestionactifs.domain.PlanningMaintenanceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.dgi.gestionactifs.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PlanningMaintenanceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlanningMaintenance.class);
        PlanningMaintenance planningMaintenance1 = getPlanningMaintenanceSample1();
        PlanningMaintenance planningMaintenance2 = new PlanningMaintenance();
        assertThat(planningMaintenance1).isNotEqualTo(planningMaintenance2);

        planningMaintenance2.setId(planningMaintenance1.getId());
        assertThat(planningMaintenance1).isEqualTo(planningMaintenance2);

        planningMaintenance2 = getPlanningMaintenanceSample2();
        assertThat(planningMaintenance1).isNotEqualTo(planningMaintenance2);
    }

    @Test
    void interventionTest() {
        PlanningMaintenance planningMaintenance = getPlanningMaintenanceRandomSampleGenerator();
        Intervention interventionBack = getInterventionRandomSampleGenerator();

        planningMaintenance.addIntervention(interventionBack);
        assertThat(planningMaintenance.getInterventions()).containsOnly(interventionBack);

        planningMaintenance.removeIntervention(interventionBack);
        assertThat(planningMaintenance.getInterventions()).doesNotContain(interventionBack);

        planningMaintenance.interventions(new HashSet<>(Set.of(interventionBack)));
        assertThat(planningMaintenance.getInterventions()).containsOnly(interventionBack);

        planningMaintenance.setInterventions(new HashSet<>());
        assertThat(planningMaintenance.getInterventions()).doesNotContain(interventionBack);
    }
}
