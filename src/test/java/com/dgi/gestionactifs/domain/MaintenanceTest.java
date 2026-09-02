package com.dgi.gestionactifs.domain;

import static com.dgi.gestionactifs.domain.ActifTestSamples.*;
import static com.dgi.gestionactifs.domain.MaintenanceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.dgi.gestionactifs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MaintenanceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Maintenance.class);
        Maintenance maintenance1 = getMaintenanceSample1();
        Maintenance maintenance2 = new Maintenance();
        assertThat(maintenance1).isNotEqualTo(maintenance2);

        maintenance2.setId(maintenance1.getId());
        assertThat(maintenance1).isEqualTo(maintenance2);

        maintenance2 = getMaintenanceSample2();
        assertThat(maintenance1).isNotEqualTo(maintenance2);
    }

    @Test
    void actifTest() {
        Maintenance maintenance = getMaintenanceRandomSampleGenerator();
        Actif actifBack = getActifRandomSampleGenerator();

        maintenance.setActif(actifBack);
        assertThat(maintenance.getActif()).isEqualTo(actifBack);

        maintenance.actif(null);
        assertThat(maintenance.getActif()).isNull();
    }
}
