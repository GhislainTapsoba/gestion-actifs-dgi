package com.dgi.gestionactifs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EquipementRecensementTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static EquipementRecensement getEquipementRecensementSample1() {
        return new EquipementRecensement().id(1L).emplacementConstate("emplacementConstate1");
    }

    public static EquipementRecensement getEquipementRecensementSample2() {
        return new EquipementRecensement().id(2L).emplacementConstate("emplacementConstate2");
    }

    public static EquipementRecensement getEquipementRecensementRandomSampleGenerator() {
        return new EquipementRecensement().id(longCount.incrementAndGet()).emplacementConstate(UUID.randomUUID().toString());
    }
}
