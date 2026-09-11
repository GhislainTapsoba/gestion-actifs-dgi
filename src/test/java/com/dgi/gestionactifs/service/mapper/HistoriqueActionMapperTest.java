package com.dgi.gestionactifs.service.mapper;

import static com.dgi.gestionactifs.domain.HistoriqueActionAsserts.*;
import static com.dgi.gestionactifs.domain.HistoriqueActionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HistoriqueActionMapperTest {

    private HistoriqueActionMapper historiqueActionMapper;

    @BeforeEach
    void setUp() {
        historiqueActionMapper = new HistoriqueActionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHistoriqueActionSample1();
        var actual = historiqueActionMapper.toEntity(historiqueActionMapper.toDto(expected));
        assertHistoriqueActionAllPropertiesEquals(expected, actual);
    }
}
