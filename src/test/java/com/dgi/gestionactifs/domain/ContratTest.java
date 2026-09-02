package com.dgi.gestionactifs.domain;

import static com.dgi.gestionactifs.domain.ActifTestSamples.*;
import static com.dgi.gestionactifs.domain.ContratTestSamples.*;
import static com.dgi.gestionactifs.domain.FournisseurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.dgi.gestionactifs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContratTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Contrat.class);
        Contrat contrat1 = getContratSample1();
        Contrat contrat2 = new Contrat();
        assertThat(contrat1).isNotEqualTo(contrat2);

        contrat2.setId(contrat1.getId());
        assertThat(contrat1).isEqualTo(contrat2);

        contrat2 = getContratSample2();
        assertThat(contrat1).isNotEqualTo(contrat2);
    }

    @Test
    void actifTest() {
        Contrat contrat = getContratRandomSampleGenerator();
        Actif actifBack = getActifRandomSampleGenerator();

        contrat.setActif(actifBack);
        assertThat(contrat.getActif()).isEqualTo(actifBack);

        contrat.actif(null);
        assertThat(contrat.getActif()).isNull();
    }

    @Test
    void fournisseurTest() {
        Contrat contrat = getContratRandomSampleGenerator();
        Fournisseur fournisseurBack = getFournisseurRandomSampleGenerator();

        contrat.setFournisseur(fournisseurBack);
        assertThat(contrat.getFournisseur()).isEqualTo(fournisseurBack);

        contrat.fournisseur(null);
        assertThat(contrat.getFournisseur()).isNull();
    }
}
