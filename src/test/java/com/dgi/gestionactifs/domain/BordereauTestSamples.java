package com.dgi.gestionactifs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BordereauTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Bordereau getBordereauSample1() {
        return new Bordereau().id(1L).numero("numero1");
    }

    public static Bordereau getBordereauSample2() {
        return new Bordereau().id(2L).numero("numero2");
    }

    public static Bordereau getBordereauRandomSampleGenerator() {
        return new Bordereau().id(longCount.incrementAndGet()).numero(UUID.randomUUID().toString());
    }
}
