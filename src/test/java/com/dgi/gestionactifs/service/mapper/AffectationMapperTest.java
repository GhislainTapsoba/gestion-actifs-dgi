package com.dgi.gestionactifs.service.mapper;

import static com.dgi.gestionactifs.domain.AffectationAsserts.*;
import static com.dgi.gestionactifs.domain.AffectationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AffectationMapperTest {

    private AffectationMapper affectationMapper;

    @BeforeEach
    void setUp() {
        affectationMapper = new AffectationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAffectationSample1();
        var actual = affectationMapper.toEntity(affectationMapper.toDto(expected));
        assertAffectationAllPropertiesEquals(expected, actual);
    }
}
