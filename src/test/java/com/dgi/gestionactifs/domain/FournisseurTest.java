package com.dgi.gestionactifs.domain;

import static com.dgi.gestionactifs.domain.ContratTestSamples.*;
import static com.dgi.gestionactifs.domain.FournisseurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.dgi.gestionactifs.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class FournisseurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Fournisseur.class);
        Fournisseur fournisseur1 = getFournisseurSample1();
        Fournisseur fournisseur2 = new Fournisseur();
        assertThat(fournisseur1).isNotEqualTo(fournisseur2);

        fournisseur2.setId(fournisseur1.getId());
        assertThat(fournisseur1).isEqualTo(fournisseur2);

        fournisseur2 = getFournisseurSample2();
        assertThat(fournisseur1).isNotEqualTo(fournisseur2);
    }

    @Test
    void contratTest() {
        Fournisseur fournisseur = getFournisseurRandomSampleGenerator();
        Contrat contratBack = getContratRandomSampleGenerator();

        fournisseur.addContrat(contratBack);
        assertThat(fournisseur.getContrats()).containsOnly(contratBack);
        assertThat(contratBack.getFournisseur()).isEqualTo(fournisseur);

        fournisseur.removeContrat(contratBack);
        assertThat(fournisseur.getContrats()).doesNotContain(contratBack);
        assertThat(contratBack.getFournisseur()).isNull();

        fournisseur.contrats(new HashSet<>(Set.of(contratBack)));
        assertThat(fournisseur.getContrats()).containsOnly(contratBack);
        assertThat(contratBack.getFournisseur()).isEqualTo(fournisseur);

        fournisseur.setContrats(new HashSet<>());
        assertThat(fournisseur.getContrats()).doesNotContain(contratBack);
        assertThat(contratBack.getFournisseur()).isNull();
    }
}
