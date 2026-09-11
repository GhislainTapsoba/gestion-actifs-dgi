package com.dgi.gestionactifs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AffectationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Affectation getAffectationSample1() {
        return new Affectation().id(1L).motif("motif1");
    }

    public static Affectation getAffectationSample2() {
        return new Affectation().id(2L).motif("motif2");
    }

    public static Affectation getAffectationRandomSampleGenerator() {
        return new Affectation().id(longCount.incrementAndGet()).motif(UUID.randomUUID().toString());
    }
}
