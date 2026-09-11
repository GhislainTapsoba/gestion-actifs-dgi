package com.dgi.gestionactifs.service.mapper;

import static com.dgi.gestionactifs.domain.EquipementRecensementAsserts.*;
import static com.dgi.gestionactifs.domain.EquipementRecensementTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EquipementRecensementMapperTest {

    private EquipementRecensementMapper equipementRecensementMapper;

    @BeforeEach
    void setUp() {
        equipementRecensementMapper = new EquipementRecensementMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEquipementRecensementSample1();
        var actual = equipementRecensementMapper.toEntity(equipementRecensementMapper.toDto(expected));
        assertEquipementRecensementAllPropertiesEquals(expected, actual);
    }
}
