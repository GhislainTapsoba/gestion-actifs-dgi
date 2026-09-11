package com.dgi.gestionactifs.service.mapper;

import static com.dgi.gestionactifs.domain.InterventionAsserts.*;
import static com.dgi.gestionactifs.domain.InterventionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InterventionMapperTest {

    private InterventionMapper interventionMapper;

    @BeforeEach
    void setUp() {
        interventionMapper = new InterventionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getInterventionSample1();
        var actual = interventionMapper.toEntity(interventionMapper.toDto(expected));
        assertInterventionAllPropertiesEquals(expected, actual);
    }
}
