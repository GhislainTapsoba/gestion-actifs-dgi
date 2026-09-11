package com.dgi.gestionactifs.service.mapper;

import static com.dgi.gestionactifs.domain.BordereauAsserts.*;
import static com.dgi.gestionactifs.domain.BordereauTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BordereauMapperTest {

    private BordereauMapper bordereauMapper;

    @BeforeEach
    void setUp() {
        bordereauMapper = new BordereauMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBordereauSample1();
        var actual = bordereauMapper.toEntity(bordereauMapper.toDto(expected));
        assertBordereauAllPropertiesEquals(expected, actual);
    }
}
