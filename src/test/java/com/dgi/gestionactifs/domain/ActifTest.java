package com.dgi.gestionactifs.domain;

import static com.dgi.gestionactifs.domain.ActifTestSamples.*;
import static com.dgi.gestionactifs.domain.AffectationTestSamples.*;
import static com.dgi.gestionactifs.domain.MaintenanceTestSamples.*;
import static com.dgi.gestionactifs.domain.TransfertTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.dgi.gestionactifs.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ActifTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Actif.class);
        Actif actif1 = getActifSample1();
        Actif actif2 = new Actif();
        assertThat(actif1).isNotEqualTo(actif2);

        actif2.setId(actif1.getId());
        assertThat(actif1).isEqualTo(actif2);

        actif2 = getActifSample2();
        assertThat(actif1).isNotEqualTo(actif2);
    }

    @Test
    void affectationTest() {
        Actif actif = getActifRandomSampleGenerator();
        Affectation affectationBack = getAffectationRandomSampleGenerator();

        actif.addAffectation(affectationBack);
        assertThat(actif.getAffectations()).containsOnly(affectationBack);
        assertThat(affectationBack.getActif()).isEqualTo(actif);

        actif.removeAffectation(affectationBack);
        assertThat(actif.getAffectations()).doesNotContain(affectationBack);
        assertThat(affectationBack.getActif()).isNull();

        actif.affectations(new HashSet<>(Set.of(affectationBack)));
        assertThat(actif.getAffectations()).containsOnly(affectationBack);
        assertThat(affectationBack.getActif()).isEqualTo(actif);

        actif.setAffectations(new HashSet<>());
        assertThat(actif.getAffectations()).doesNotContain(affectationBack);
        assertThat(affectationBack.getActif()).isNull();
    }

    @Test
    void transfertTest() {
        Actif actif = getActifRandomSampleGenerator();
        Transfert transfertBack = getTransfertRandomSampleGenerator();

        actif.addTransfert(transfertBack);
        assertThat(actif.getTransferts()).containsOnly(transfertBack);
        assertThat(transfertBack.getActif()).isEqualTo(actif);

        actif.removeTransfert(transfertBack);
        assertThat(actif.getTransferts()).doesNotContain(transfertBack);
        assertThat(transfertBack.getActif()).isNull();

        actif.transferts(new HashSet<>(Set.of(transfertBack)));
        assertThat(actif.getTransferts()).containsOnly(transfertBack);
        assertThat(transfertBack.getActif()).isEqualTo(actif);

        actif.setTransferts(new HashSet<>());
        assertThat(actif.getTransferts()).doesNotContain(transfertBack);
        assertThat(transfertBack.getActif()).isNull();
    }

    @Test
    void maintenanceTest() {
        Actif actif = getActifRandomSampleGenerator();
        Maintenance maintenanceBack = getMaintenanceRandomSampleGenerator();

        actif.addMaintenance(maintenanceBack);
        assertThat(actif.getMaintenances()).containsOnly(maintenanceBack);
        assertThat(maintenanceBack.getActif()).isEqualTo(actif);

        actif.removeMaintenance(maintenanceBack);
        assertThat(actif.getMaintenances()).doesNotContain(maintenanceBack);
        assertThat(maintenanceBack.getActif()).isNull();

        actif.maintenances(new HashSet<>(Set.of(maintenanceBack)));
        assertThat(actif.getMaintenances()).containsOnly(maintenanceBack);
        assertThat(maintenanceBack.getActif()).isEqualTo(actif);

        actif.setMaintenances(new HashSet<>());
        assertThat(actif.getMaintenances()).doesNotContain(maintenanceBack);
        assertThat(maintenanceBack.getActif()).isNull();
    }
}
