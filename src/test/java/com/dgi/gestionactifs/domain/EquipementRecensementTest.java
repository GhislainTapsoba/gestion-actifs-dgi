package com.dgi.gestionactifs.domain;

import static com.dgi.gestionactifs.domain.ActifTestSamples.*;
import static com.dgi.gestionactifs.domain.EquipementRecensementTestSamples.*;
import static com.dgi.gestionactifs.domain.RecensementTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.dgi.gestionactifs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EquipementRecensementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EquipementRecensement.class);
        EquipementRecensement equipementRecensement1 = getEquipementRecensementSample1();
        EquipementRecensement equipementRecensement2 = new EquipementRecensement();
        assertThat(equipementRecensement1).isNotEqualTo(equipementRecensement2);

        equipementRecensement2.setId(equipementRecensement1.getId());
        assertThat(equipementRecensement1).isEqualTo(equipementRecensement2);

        equipementRecensement2 = getEquipementRecensementSample2();
        assertThat(equipementRecensement1).isNotEqualTo(equipementRecensement2);
    }

    @Test
    void recensementTest() {
        EquipementRecensement equipementRecensement = getEquipementRecensementRandomSampleGenerator();
        Recensement recensementBack = getRecensementRandomSampleGenerator();

        equipementRecensement.setRecensement(recensementBack);
        assertThat(equipementRecensement.getRecensement()).isEqualTo(recensementBack);

        equipementRecensement.recensement(null);
        assertThat(equipementRecensement.getRecensement()).isNull();
    }

    @Test
    void actifTest() {
        EquipementRecensement equipementRecensement = getEquipementRecensementRandomSampleGenerator();
        Actif actifBack = getActifRandomSampleGenerator();

        equipementRecensement.setActif(actifBack);
        assertThat(equipementRecensement.getActif()).isEqualTo(actifBack);

        equipementRecensement.actif(null);
        assertThat(equipementRecensement.getActif()).isNull();
    }
}
