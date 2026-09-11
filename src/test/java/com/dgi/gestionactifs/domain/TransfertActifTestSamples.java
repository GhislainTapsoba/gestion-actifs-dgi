package com.dgi.gestionactifs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TransfertActifTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static TransfertActif getTransfertActifSample1() {
        return new TransfertActif().id(1L).observation("observation1");
    }

    public static TransfertActif getTransfertActifSample2() {
        return new TransfertActif().id(2L).observation("observation2");
    }

    public static TransfertActif getTransfertActifRandomSampleGenerator() {
        return new TransfertActif().id(longCount.incrementAndGet()).observation(UUID.randomUUID().toString());
    }
}
