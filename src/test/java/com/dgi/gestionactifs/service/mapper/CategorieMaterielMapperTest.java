package com.dgi.gestionactifs.service.mapper;

import static com.dgi.gestionactifs.domain.CategorieMaterielAsserts.*;
import static com.dgi.gestionactifs.domain.CategorieMaterielTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CategorieMaterielMapperTest {

    private CategorieMaterielMapper categorieMaterielMapper;

    @BeforeEach
    void setUp() {
        categorieMaterielMapper = new CategorieMaterielMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCategorieMaterielSample1();
        var actual = categorieMaterielMapper.toEntity(categorieMaterielMapper.toDto(expected));
        assertCategorieMaterielAllPropertiesEquals(expected, actual);
    }
}
