package com.byteowls.docker.boot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.Ordered;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author m.oberwasserlechner@byteowls.com
 */
class DockerSecretEnvPostProcessorTest {

    private static final String TEST_SECRET_DIR = "src/test/resources/secrets/";

    private DockerSecretEnvPostProcessor pp;

    @BeforeEach
    void setUp() {
        pp = new DockerSecretEnvPostProcessor();
    }

    @Test
    void getOrder() {
        assertEquals(Ordered.LOWEST_PRECEDENCE, pp.getOrder());
    }

    @Test
    void exceptionsThrowing() {
        final String dockerSecretPrefix = "docker_secret_";
        // no such file
        assertThrows(IOException.class, () ->
            pp.getDockerSecretsMap("/path/not/exists", dockerSecretPrefix, true, false));

        // no directory
        assertThrows(IOException.class, () ->
            pp.getDockerSecretsMap("/secrets/password", dockerSecretPrefix, true, false));
    }

    @Test
    void basicFileParsing() throws IOException {
        final String prefix = "docker_secret_";
        final Map<String, Object> dockerSecretsMap = pp.getDockerSecretsMap(TEST_SECRET_DIR, prefix, true,false);
        assertNotNull(dockerSecretsMap);
        assertEquals(3, dockerSecretsMap.size());
        for (String key : dockerSecretsMap.keySet()) {
            assertTrue(key.startsWith(prefix));
        }
    }

    @Test
    void maskingSecrets() throws IOException {
        final Map<String, Object> map = pp.getDockerSecretsMap(TEST_SECRET_DIR, null, true, false);
        final String key = "username";
        final String maskedKeyPair = pp.getMaskedSecretKeyPair(key, map.get(key));
        assertEquals(key + "=m********", maskedKeyPair);
    }

    @Test
    void maskingEmptySecrets() throws IOException {
        final Map<String, Object> map = pp.getDockerSecretsMap(TEST_SECRET_DIR, null, true, false);
        final String key = "empty";
        final String maskedKeyPair = pp.getMaskedSecretKeyPair(key, map.get(key));
        assertEquals(key + "=_********", maskedKeyPair);
    }

    @Test
    void whitespaceHandling() throws IOException {
        final Map<String, Object> mapTrim = pp.getDockerSecretsMap(TEST_SECRET_DIR, null, true, false);
        assertEquals("michael", mapTrim.get("username"));

        final Map<String, Object> mapNotTrim = pp.getDockerSecretsMap(TEST_SECRET_DIR, null, false, false);
        final String username = (String) mapNotTrim.get("username");
        assertTrue(username.endsWith("\r\n") || username.endsWith("\n"));
        assertNotEquals("michael", username);
    }

    @Test
    void noPrefixHandling() throws IOException {
        assertNoPrefixUsernameOrPassword(pp.getDockerSecretsMap(TEST_SECRET_DIR, null, true,false));

        assertNoPrefixUsernameOrPassword(pp.getDockerSecretsMap(TEST_SECRET_DIR, "", true,false));

    }

    private void assertNoPrefixUsernameOrPassword(Map<String, Object> map) {
        for (String key : map.keySet()) {
            assertTrue("password".equals(key) || "username".equals(key) || "empty".equals(key));
        }
    }
}
