package com.dgi.gestionactifs.service.mapper;

import static com.dgi.gestionactifs.domain.RecensementAsserts.*;
import static com.dgi.gestionactifs.domain.RecensementTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecensementMapperTest {

    private RecensementMapper recensementMapper;

    @BeforeEach
    void setUp() {
        recensementMapper = new RecensementMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRecensementSample1();
        var actual = recensementMapper.toEntity(recensementMapper.toDto(expected));
        assertRecensementAllPropertiesEquals(expected, actual);
    }
}
