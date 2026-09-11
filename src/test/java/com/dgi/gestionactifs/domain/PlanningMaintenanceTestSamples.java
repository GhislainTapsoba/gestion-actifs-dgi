package com.dgi.gestionactifs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PlanningMaintenanceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static PlanningMaintenance getPlanningMaintenanceSample1() {
        return new PlanningMaintenance().id(1L).periodicite("periodicite1").description("description1");
    }

    public static PlanningMaintenance getPlanningMaintenanceSample2() {
        return new PlanningMaintenance().id(2L).periodicite("periodicite2").description("description2");
    }

    public static PlanningMaintenance getPlanningMaintenanceRandomSampleGenerator() {
        return new PlanningMaintenance()
            .id(longCount.incrementAndGet())
            .periodicite(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
