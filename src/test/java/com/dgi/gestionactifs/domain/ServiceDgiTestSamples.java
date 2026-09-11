package com.dgi.gestionactifs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ServiceDgiTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static ServiceDgi getServiceDgiSample1() {
        return new ServiceDgi().id(1L).nomService("nomService1").chefService("chefService1");
    }

    public static ServiceDgi getServiceDgiSample2() {
        return new ServiceDgi().id(2L).nomService("nomService2").chefService("chefService2");
    }

    public static ServiceDgi getServiceDgiRandomSampleGenerator() {
        return new ServiceDgi()
            .id(longCount.incrementAndGet())
            .nomService(UUID.randomUUID().toString())
            .chefService(UUID.randomUUID().toString());
    }
}
