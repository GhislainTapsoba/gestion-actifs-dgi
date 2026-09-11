package com.dgi.gestionactifs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PanneTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Panne getPanneSample1() {
        return new Panne().id(1L).description("description1");
    }

    public static Panne getPanneSample2() {
        return new Panne().id(2L).description("description2");
    }

    public static Panne getPanneRandomSampleGenerator() {
        return new Panne().id(longCount.incrementAndGet()).description(UUID.randomUUID().toString());
    }
}
