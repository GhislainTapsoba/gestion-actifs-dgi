package com.dgi.gestionactifs.domain;

import static com.dgi.gestionactifs.domain.CategorieMaterielTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.dgi.gestionactifs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CategorieMaterielTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategorieMateriel.class);
        CategorieMateriel categorieMateriel1 = getCategorieMaterielSample1();
        CategorieMateriel categorieMateriel2 = new CategorieMateriel();
        assertThat(categorieMateriel1).isNotEqualTo(categorieMateriel2);

        categorieMateriel2.setId(categorieMateriel1.getId());
        assertThat(categorieMateriel1).isEqualTo(categorieMateriel2);

        categorieMateriel2 = getCategorieMaterielSample2();
        assertThat(categorieMateriel1).isNotEqualTo(categorieMateriel2);
    }
}
