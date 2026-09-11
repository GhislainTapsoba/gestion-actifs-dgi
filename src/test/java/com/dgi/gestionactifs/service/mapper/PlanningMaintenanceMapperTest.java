package com.dgi.gestionactifs.service.mapper;

import static com.dgi.gestionactifs.domain.PlanningMaintenanceAsserts.*;
import static com.dgi.gestionactifs.domain.PlanningMaintenanceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlanningMaintenanceMapperTest {

    private PlanningMaintenanceMapper planningMaintenanceMapper;

    @BeforeEach
    void setUp() {
        planningMaintenanceMapper = new PlanningMaintenanceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPlanningMaintenanceSample1();
        var actual = planningMaintenanceMapper.toEntity(planningMaintenanceMapper.toDto(expected));
        assertPlanningMaintenanceAllPropertiesEquals(expected, actual);
    }
}
