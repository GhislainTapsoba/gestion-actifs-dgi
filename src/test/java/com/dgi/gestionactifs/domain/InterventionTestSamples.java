package com.dgi.gestionactifs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InterventionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Intervention getInterventionSample1() {
        return new Intervention().id(1L).description("description1");
    }

    public static Intervention getInterventionSample2() {
        return new Intervention().id(2L).description("description2");
    }

    public static Intervention getInterventionRandomSampleGenerator() {
        return new Intervention().id(longCount.incrementAndGet()).description(UUID.randomUUID().toString());
    }
}
