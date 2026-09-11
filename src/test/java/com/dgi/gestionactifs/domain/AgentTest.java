package com.dgi.gestionactifs.domain;

import static com.dgi.gestionactifs.domain.AgentTestSamples.*;
import static com.dgi.gestionactifs.domain.ServiceDgiTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.dgi.gestionactifs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AgentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Agent.class);
        Agent agent1 = getAgentSample1();
        Agent agent2 = new Agent();
        assertThat(agent1).isNotEqualTo(agent2);

        agent2.setId(agent1.getId());
        assertThat(agent1).isEqualTo(agent2);

        agent2 = getAgentSample2();
        assertThat(agent1).isNotEqualTo(agent2);
    }

    @Test
    void serviceTest() {
        Agent agent = getAgentRandomSampleGenerator();
        ServiceDgi serviceDgiBack = getServiceDgiRandomSampleGenerator();

        agent.setService(serviceDgiBack);
        assertThat(agent.getService()).isEqualTo(serviceDgiBack);

        agent.service(null);
        assertThat(agent.getService()).isNull();
    }
}
