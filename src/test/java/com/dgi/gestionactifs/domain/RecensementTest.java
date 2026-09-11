package com.dgi.gestionactifs.domain;

import static com.dgi.gestionactifs.domain.RecensementTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.dgi.gestionactifs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RecensementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Recensement.class);
        Recensement recensement1 = getRecensementSample1();
        Recensement recensement2 = new Recensement();
        assertThat(recensement1).isNotEqualTo(recensement2);

        recensement2.setId(recensement1.getId());
        assertThat(recensement1).isEqualTo(recensement2);

        recensement2 = getRecensementSample2();
        assertThat(recensement1).isNotEqualTo(recensement2);
    }
}
