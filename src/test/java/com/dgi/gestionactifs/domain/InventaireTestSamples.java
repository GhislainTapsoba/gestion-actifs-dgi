package com.dgi.gestionactifs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InventaireTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Inventaire getInventaireSample1() {
        return new Inventaire().id(1L).nomFichier("nomFichier1");
    }

    public static Inventaire getInventaireSample2() {
        return new Inventaire().id(2L).nomFichier("nomFichier2");
    }

    public static Inventaire getInventaireRandomSampleGenerator() {
        return new Inventaire().id(longCount.incrementAndGet()).nomFichier(UUID.randomUUID().toString());
    }
}
