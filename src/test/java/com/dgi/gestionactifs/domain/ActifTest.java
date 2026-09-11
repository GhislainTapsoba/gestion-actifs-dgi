package com.dgi.gestionactifs.domain;

import static com.dgi.gestionactifs.domain.ActifTestSamples.*;
import static com.dgi.gestionactifs.domain.CategorieMaterielTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.dgi.gestionactifs.web.rest.TestUtil;
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
    void categorieTest() {
        Actif actif = getActifRandomSampleGenerator();
        CategorieMateriel categorieMaterielBack = getCategorieMaterielRandomSampleGenerator();

        actif.setCategorie(categorieMaterielBack);
        assertThat(actif.getCategorie()).isEqualTo(categorieMaterielBack);

        actif.categorie(null);
        assertThat(actif.getCategorie()).isNull();
    }
}
