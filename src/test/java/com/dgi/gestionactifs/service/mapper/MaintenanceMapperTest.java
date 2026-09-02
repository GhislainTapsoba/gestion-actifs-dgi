package com.dgi.gestionactifs.service.mapper;

import static com.dgi.gestionactifs.domain.MaintenanceAsserts.*;
import static com.dgi.gestionactifs.domain.MaintenanceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MaintenanceMapperTest {

    private MaintenanceMapper maintenanceMapper;

    @BeforeEach
    void setUp() {
        maintenanceMapper = new MaintenanceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMaintenanceSample1();
        var actual = maintenanceMapper.toEntity(maintenanceMapper.toDto(expected));
        assertMaintenanceAllPropertiesEquals(expected, actual);
    }
}
