package com.dgi.gestionactifs.domain;

import static com.dgi.gestionactifs.domain.ActifTestSamples.*;
import static com.dgi.gestionactifs.domain.InventaireTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.dgi.gestionactifs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InventaireTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Inventaire.class);
        Inventaire inventaire1 = getInventaireSample1();
        Inventaire inventaire2 = new Inventaire();
        assertThat(inventaire1).isNotEqualTo(inventaire2);

        inventaire2.setId(inventaire1.getId());
        assertThat(inventaire1).isEqualTo(inventaire2);

        inventaire2 = getInventaireSample2();
        assertThat(inventaire1).isNotEqualTo(inventaire2);
    }

    @Test
    void actifTest() {
        Inventaire inventaire = getInventaireRandomSampleGenerator();
        Actif actifBack = getActifRandomSampleGenerator();

        inventaire.setActif(actifBack);
        assertThat(inventaire.getActif()).isEqualTo(actifBack);

        inventaire.actif(null);
        assertThat(inventaire.getActif()).isNull();
    }
}
