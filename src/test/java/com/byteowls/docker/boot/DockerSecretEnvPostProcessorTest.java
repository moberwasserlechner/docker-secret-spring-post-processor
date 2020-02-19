package com.byteowls.docker.boot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.SpringApplication;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author m.oberwasserlechner@byteowls.com
 */
class DockerSecretEnvPostProcessorTest {

    private DockerSecretEnvPostProcessor pp;
//    @Mock
//    private ConfigurableEnvironment env;
//    @Mock
//    private SpringApplication app;

    @BeforeEach
    void setUp() {
        pp = new DockerSecretEnvPostProcessor();
    }

    @Test
    void postProcessEnvironment() {
//        pp.postProcessEnvironment(env, app);
    }

    @Test
    void getOrder() {
        assertEquals(Ordered.LOWEST_PRECEDENCE, pp.getOrder());
    }
}
