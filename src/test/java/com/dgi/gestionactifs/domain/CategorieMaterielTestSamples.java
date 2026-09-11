package com.dgi.gestionactifs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CategorieMaterielTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static CategorieMateriel getCategorieMaterielSample1() {
        return new CategorieMateriel().id(1L).libelle("libelle1").description("description1");
    }

    public static CategorieMateriel getCategorieMaterielSample2() {
        return new CategorieMateriel().id(2L).libelle("libelle2").description("description2");
    }

    public static CategorieMateriel getCategorieMaterielRandomSampleGenerator() {
        return new CategorieMateriel()
            .id(longCount.incrementAndGet())
            .libelle(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
