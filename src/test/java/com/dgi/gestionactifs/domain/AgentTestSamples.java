package com.dgi.gestionactifs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AgentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Agent getAgentSample1() {
        return new Agent().id(1L).nom("nom1").prenom("prenom1");
    }

    public static Agent getAgentSample2() {
        return new Agent().id(2L).nom("nom2").prenom("prenom2");
    }

    public static Agent getAgentRandomSampleGenerator() {
        return new Agent().id(longCount.incrementAndGet()).nom(UUID.randomUUID().toString()).prenom(UUID.randomUUID().toString());
    }
}
