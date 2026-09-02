package com.dgi.gestionactifs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ActifTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Actif getActifSample1() {
        return new Actif().id(1L).identifiantUnique("identifiantUnique1").codeBarreQR("codeBarreQR1").localisation("localisation1");
    }

    public static Actif getActifSample2() {
        return new Actif().id(2L).identifiantUnique("identifiantUnique2").codeBarreQR("codeBarreQR2").localisation("localisation2");
    }

    public static Actif getActifRandomSampleGenerator() {
        return new Actif()
            .id(longCount.incrementAndGet())
            .identifiantUnique(UUID.randomUUID().toString())
            .codeBarreQR(UUID.randomUUID().toString())
            .localisation(UUID.randomUUID().toString());
    }
}
