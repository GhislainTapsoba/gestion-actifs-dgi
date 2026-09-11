package com.dgi.gestionactifs.domain;

import static com.dgi.gestionactifs.domain.HistoriqueActionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.dgi.gestionactifs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HistoriqueActionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoriqueAction.class);
        HistoriqueAction historiqueAction1 = getHistoriqueActionSample1();
        HistoriqueAction historiqueAction2 = new HistoriqueAction();
        assertThat(historiqueAction1).isNotEqualTo(historiqueAction2);

        historiqueAction2.setId(historiqueAction1.getId());
        assertThat(historiqueAction1).isEqualTo(historiqueAction2);

        historiqueAction2 = getHistoriqueActionSample2();
        assertThat(historiqueAction1).isNotEqualTo(historiqueAction2);
    }
}
