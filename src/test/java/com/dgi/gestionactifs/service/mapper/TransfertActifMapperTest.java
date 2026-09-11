package com.dgi.gestionactifs.service.mapper;

import static com.dgi.gestionactifs.domain.TransfertActifAsserts.*;
import static com.dgi.gestionactifs.domain.TransfertActifTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransfertActifMapperTest {

    private TransfertActifMapper transfertActifMapper;

    @BeforeEach
    void setUp() {
        transfertActifMapper = new TransfertActifMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTransfertActifSample1();
        var actual = transfertActifMapper.toEntity(transfertActifMapper.toDto(expected));
        assertTransfertActifAllPropertiesEquals(expected, actual);
    }
}
