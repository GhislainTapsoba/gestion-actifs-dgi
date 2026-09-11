package com.dgi.gestionactifs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ActifTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Actif getActifSample1() {
        return new Actif()
            .id(1L)
            .codeInventaire("codeInventaire1")
            .designation("designation1")
            .marque("marque1")
            .modele("modele1")
            .numeroSerie("numeroSerie1")
            .codeBarre("codeBarre1")
            .localisation("localisation1");
    }

    public static Actif getActifSample2() {
        return new Actif()
            .id(2L)
            .codeInventaire("codeInventaire2")
            .designation("designation2")
            .marque("marque2")
            .modele("modele2")
            .numeroSerie("numeroSerie2")
            .codeBarre("codeBarre2")
            .localisation("localisation2");
    }

    public static Actif getActifRandomSampleGenerator() {
        return new Actif()
            .id(longCount.incrementAndGet())
            .codeInventaire(UUID.randomUUID().toString())
            .designation(UUID.randomUUID().toString())
            .marque(UUID.randomUUID().toString())
            .modele(UUID.randomUUID().toString())
            .numeroSerie(UUID.randomUUID().toString())
            .codeBarre(UUID.randomUUID().toString())
            .localisation(UUID.randomUUID().toString());
    }
}
