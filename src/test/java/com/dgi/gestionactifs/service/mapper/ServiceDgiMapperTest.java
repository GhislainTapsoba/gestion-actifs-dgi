package com.dgi.gestionactifs.service.mapper;

import static com.dgi.gestionactifs.domain.ServiceDgiAsserts.*;
import static com.dgi.gestionactifs.domain.ServiceDgiTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ServiceDgiMapperTest {

    private ServiceDgiMapper serviceDgiMapper;

    @BeforeEach
    void setUp() {
        serviceDgiMapper = new ServiceDgiMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getServiceDgiSample1();
        var actual = serviceDgiMapper.toEntity(serviceDgiMapper.toDto(expected));
        assertServiceDgiAllPropertiesEquals(expected, actual);
    }
}
