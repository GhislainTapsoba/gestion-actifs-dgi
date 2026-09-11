package com.dgi.gestionactifs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HistoriqueActionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static HistoriqueAction getHistoriqueActionSample1() {
        return new HistoriqueAction()
            .id(1L)
            .entiteCiblee("entiteCiblee1")
            .ancienneValeur("ancienneValeur1")
            .nouvelleValeur("nouvelleValeur1");
    }

    public static HistoriqueAction getHistoriqueActionSample2() {
        return new HistoriqueAction()
            .id(2L)
            .entiteCiblee("entiteCiblee2")
            .ancienneValeur("ancienneValeur2")
            .nouvelleValeur("nouvelleValeur2");
    }

    public static HistoriqueAction getHistoriqueActionRandomSampleGenerator() {
        return new HistoriqueAction()
            .id(longCount.incrementAndGet())
            .entiteCiblee(UUID.randomUUID().toString())
            .ancienneValeur(UUID.randomUUID().toString())
            .nouvelleValeur(UUID.randomUUID().toString());
    }
}
