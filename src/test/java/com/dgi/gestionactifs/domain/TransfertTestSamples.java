package com.dgi.gestionactifs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TransfertTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Transfert getTransfertSample1() {
        return new Transfert().id(1L).commentaireRejet("commentaireRejet1");
    }

    public static Transfert getTransfertSample2() {
        return new Transfert().id(2L).commentaireRejet("commentaireRejet2");
    }

    public static Transfert getTransfertRandomSampleGenerator() {
        return new Transfert().id(longCount.incrementAndGet()).commentaireRejet(UUID.randomUUID().toString());
    }
}
