package com.dgi.gestionactifs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AffectationActifTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static AffectationActif getAffectationActifSample1() {
        return new AffectationActif().id(1L).observation("observation1");
    }

    public static AffectationActif getAffectationActifSample2() {
        return new AffectationActif().id(2L).observation("observation2");
    }

    public static AffectationActif getAffectationActifRandomSampleGenerator() {
        return new AffectationActif().id(longCount.incrementAndGet()).observation(UUID.randomUUID().toString());
    }
}
