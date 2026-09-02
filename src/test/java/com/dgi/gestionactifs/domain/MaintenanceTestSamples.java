package com.dgi.gestionactifs.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class MaintenanceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Maintenance getMaintenanceSample1() {
        return new Maintenance().id(1L);
    }

    public static Maintenance getMaintenanceSample2() {
        return new Maintenance().id(2L);
    }

    public static Maintenance getMaintenanceRandomSampleGenerator() {
        return new Maintenance().id(longCount.incrementAndGet());
    }
}
