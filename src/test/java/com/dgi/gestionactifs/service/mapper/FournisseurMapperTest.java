package com.dgi.gestionactifs.service.mapper;

import static com.dgi.gestionactifs.domain.FournisseurAsserts.*;
import static com.dgi.gestionactifs.domain.FournisseurTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FournisseurMapperTest {

    private FournisseurMapper fournisseurMapper;

    @BeforeEach
    void setUp() {
        fournisseurMapper = new FournisseurMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFournisseurSample1();
        var actual = fournisseurMapper.toEntity(fournisseurMapper.toDto(expected));
        assertFournisseurAllPropertiesEquals(expected, actual);
    }
}
