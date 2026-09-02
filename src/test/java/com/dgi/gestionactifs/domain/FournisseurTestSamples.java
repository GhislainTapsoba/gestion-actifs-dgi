package com.dgi.gestionactifs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FournisseurTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Fournisseur getFournisseurSample1() {
        return new Fournisseur().id(1L).nom("nom1").contact("contact1").email("email1").telephone("telephone1");
    }

    public static Fournisseur getFournisseurSample2() {
        return new Fournisseur().id(2L).nom("nom2").contact("contact2").email("email2").telephone("telephone2");
    }

    public static Fournisseur getFournisseurRandomSampleGenerator() {
        return new Fournisseur()
            .id(longCount.incrementAndGet())
            .nom(UUID.randomUUID().toString())
            .contact(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .telephone(UUID.randomUUID().toString());
    }
}
