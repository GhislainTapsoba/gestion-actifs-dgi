package com.dgi.gestionactifs.service.mapper;

import static com.dgi.gestionactifs.domain.TransfertAsserts.*;
import static com.dgi.gestionactifs.domain.TransfertTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransfertMapperTest {

    private TransfertMapper transfertMapper;

    @BeforeEach
    void setUp() {
        transfertMapper = new TransfertMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTransfertSample1();
        var actual = transfertMapper.toEntity(transfertMapper.toDto(expected));
        assertTransfertAllPropertiesEquals(expected, actual);
    }
}
