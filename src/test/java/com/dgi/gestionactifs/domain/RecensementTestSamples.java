package com.dgi.gestionactifs.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class RecensementTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Recensement getRecensementSample1() {
        return new Recensement().id(1L);
    }

    public static Recensement getRecensementSample2() {
        return new Recensement().id(2L);
    }

    public static Recensement getRecensementRandomSampleGenerator() {
        return new Recensement().id(longCount.incrementAndGet());
    }
}
