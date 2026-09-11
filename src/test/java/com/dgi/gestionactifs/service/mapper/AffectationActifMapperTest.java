package com.dgi.gestionactifs.service.mapper;

import static com.dgi.gestionactifs.domain.AffectationActifAsserts.*;
import static com.dgi.gestionactifs.domain.AffectationActifTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AffectationActifMapperTest {

    private AffectationActifMapper affectationActifMapper;

    @BeforeEach
    void setUp() {
        affectationActifMapper = new AffectationActifMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAffectationActifSample1();
        var actual = affectationActifMapper.toEntity(affectationActifMapper.toDto(expected));
        assertAffectationActifAllPropertiesEquals(expected, actual);
    }
}
