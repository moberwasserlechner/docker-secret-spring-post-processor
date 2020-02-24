package com.byteowls.docker.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author m.oberwasserlechner@byteowls.com
 */
public class DockerSecretEnvPostProcessor implements EnvironmentPostProcessor, Ordered {

    private static final String PROP_ENABLED = "docker.boot.secret.enabled";
    private static final boolean PROP_ENABLED_DEFAULT = true;

    private static final String PROP_PREFIX = "docker.boot.secret.prefix";
    private static final String PROD_PREFIX_DEFAULT = "docker_secret_";

    private static final String PROP_PATH = "docker.boot.secret.path";
    private static final String PROP_PATH_DEFAULT = "/run/secrets";

    private static final String PROP_TRIM = "docker.boot.secret.trim";
    private static final boolean PROP_TRIM_DEFAULT = true;

    private static final String PROP_PRINT_ERRORS = "docker.boot.secret.print.errors";
    private static final boolean PROP_PRINT_ERRORS_DEFAULT = true;

    private static final String PROP_PRINT_SECRETS = "docker.boot.secret.print.secrets";
    private static final boolean PROP_PRINT_SECRETS_DEFAULT = false;
    private static final String SECRET_MASK = "********";

    private static final String PROP_PRINT_SECRETS_UNMASKED = "docker.boot.secret.print.secrets.unmasked";
    private static final boolean PROP_PRINT_SECRETS_UNMASKED_DEFAULT = false;

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment env, SpringApplication application) {
        final boolean enabled = env.getProperty(PROP_ENABLED, Boolean.class, PROP_ENABLED_DEFAULT);
        if (enabled) {
            final String prefix = env.getProperty(PROP_PREFIX, PROD_PREFIX_DEFAULT);
            final boolean printErrors = env.getProperty(PROP_PRINT_ERRORS, Boolean.class, PROP_PRINT_ERRORS_DEFAULT);
            final String path = env.getProperty(PROP_PATH, PROP_PATH_DEFAULT);
            final boolean trim = env.getProperty(PROP_TRIM, Boolean.class, PROP_TRIM_DEFAULT);

            try {
                Map<String, Object> dockerSecretsMap = getDockerSecretsMap(path, prefix, trim, printErrors);
                if (dockerSecretsMap != null && !dockerSecretsMap.isEmpty()) {
                    final boolean printSecrets = env.getProperty(PROP_PRINT_SECRETS, Boolean.class, PROP_PRINT_SECRETS_DEFAULT);
                    if (printSecrets) {
                        final boolean printUnmasked = env.getProperty(PROP_PRINT_SECRETS_UNMASKED, Boolean.class, PROP_PRINT_SECRETS_UNMASKED_DEFAULT);
                        printOut("===========================");
                        printOut("     Docker Properties     ");
                        printOut("===========================");
                        List<String> keyPairs = new ArrayList<>();
                        dockerSecretsMap.forEach((k, v) -> keyPairs.add(getMaskedSecretKeyPair(k, v, printUnmasked)));
                        Collections.sort(keyPairs);
                        keyPairs.forEach(this::printOut);
                        printOut("===========================");
                    }

                    MapPropertySource mps = new MapPropertySource("Docker", dockerSecretsMap);
                    env.getPropertySources().addLast(mps);
                }
            } catch (IOException e) {
                if (printErrors) {
                    printOut("Invalid path '" + path + "'. Check if it's a valid directory!");
                }
            }

        }
    }

    protected String getMaskedSecretKeyPair(String key, Object value, boolean printUnmasked) {
        String strValue = (String) value;
        String maskedValue = key + "=";
        if (!printUnmasked) {
            maskedValue += SECRET_MASK;
        } else {
            maskedValue += strValue;
        }
        return maskedValue;
    }

    protected Map<String, Object> getDockerSecretsMap(String path, String prefix, boolean trim, boolean printErrors) throws IOException {
        final Path secretsDirectory = Paths.get(path);
        return Files
            .list(secretsDirectory)
            .filter(p -> Files.isRegularFile(p))
            .collect(
                Collectors.toMap(
                    filePath -> {
                        String name = "";
                        if (prefix != null) {
                            name = prefix;
                        }
                        name += filePath.toFile().getName();
                        return name;
                    },
                    filePath -> {
                        final File in = filePath.toFile();
                        try {
                            final byte[] content = FileCopyUtils.copyToByteArray(in);
                            String value = new String(content, StandardCharsets.UTF_8);
                            if (trim) {
                                // remove \n\r
                                return value.trim();
                            }
                            return value;
                        } catch (IOException e) {
                            if (printErrors) {
                                printOut("Content of " + in.getPath() + " not copied!");
                            }
                        }
                        // set property to empty value
                        return "";
                    }
                )
            );
    }

    private void printOut(String message) {
        System.out.println(getClass().getName() + ": " + message);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

}
