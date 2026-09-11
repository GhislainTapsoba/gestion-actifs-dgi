package com.dgi.gestionactifs.service.mapper;

import static com.dgi.gestionactifs.domain.PanneAsserts.*;
import static com.dgi.gestionactifs.domain.PanneTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PanneMapperTest {

    private PanneMapper panneMapper;

    @BeforeEach
    void setUp() {
        panneMapper = new PanneMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPanneSample1();
        var actual = panneMapper.toEntity(panneMapper.toDto(expected));
        assertPanneAllPropertiesEquals(expected, actual);
    }
}
