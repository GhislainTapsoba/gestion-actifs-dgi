package com.dgi.gestionactifs.domain;

import static com.dgi.gestionactifs.domain.ActifTestSamples.*;
import static com.dgi.gestionactifs.domain.PanneTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.dgi.gestionactifs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PanneTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Panne.class);
        Panne panne1 = getPanneSample1();
        Panne panne2 = new Panne();
        assertThat(panne1).isNotEqualTo(panne2);

        panne2.setId(panne1.getId());
        assertThat(panne1).isEqualTo(panne2);

        panne2 = getPanneSample2();
        assertThat(panne1).isNotEqualTo(panne2);
    }

    @Test
    void actifTest() {
        Panne panne = getPanneRandomSampleGenerator();
        Actif actifBack = getActifRandomSampleGenerator();

        panne.setActif(actifBack);
        assertThat(panne.getActif()).isEqualTo(actifBack);

        panne.actif(null);
        assertThat(panne.getActif()).isNull();
    }
}
