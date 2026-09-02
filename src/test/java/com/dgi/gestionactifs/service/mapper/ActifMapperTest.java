package com.dgi.gestionactifs.service.mapper;

import static com.dgi.gestionactifs.domain.ActifAsserts.*;
import static com.dgi.gestionactifs.domain.ActifTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ActifMapperTest {

    private ActifMapper actifMapper;

    @BeforeEach
    void setUp() {
        actifMapper = new ActifMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getActifSample1();
        var actual = actifMapper.toEntity(actifMapper.toDto(expected));
        assertActifAllPropertiesEquals(expected, actual);
    }
}
