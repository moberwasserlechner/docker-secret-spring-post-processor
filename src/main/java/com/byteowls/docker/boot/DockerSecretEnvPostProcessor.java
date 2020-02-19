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
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author m.oberwasserlechner@byteowls.com
 */
public class DockerSecretEnvPostProcessor implements EnvironmentPostProcessor, Ordered {

    private static final String PROP_PREFIX = "docker.boot.secret.";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment env, SpringApplication application) {
        final boolean enabled = env.getProperty(PROP_PREFIX + "enabled", Boolean.class, true);
        if (enabled) {
            final String path = env.getProperty(PROP_PREFIX + "path", "/run/secrets");
            final String prefix = env.getProperty(PROP_PREFIX + "prefix", "docker_secret_");
            final boolean printErrors = env.getProperty(PROP_PREFIX + "print.errors", Boolean.class, true);

            final Path secretsDirectory = Paths.get(path);
            if (Files.isDirectory(secretsDirectory)) {
                Map<String, Object> dockerSecretsMap = null;
                try {
                    dockerSecretsMap = Files
                        .list(secretsDirectory)
                        .filter(p -> Files.isRegularFile(p))
                        .collect(
                            Collectors.toMap(
                                filePath -> prefix + filePath.toFile().getName(),
                                filePath -> {
                                    final File in = filePath.toFile();
                                    try {
                                        final byte[] content = FileCopyUtils.copyToByteArray(in);
                                        String value = new String(content, StandardCharsets.UTF_8);
                                        // remove \n\r
                                        return value.trim();
                                    } catch (IOException e) {
                                        if (printErrors) {
                                            printOut("Content of " + in.getPath() + "not copied!");
                                        }
                                    }
                                    // set property to empty value
                                    return "";
                                }
                            )
                        );
                } catch (IOException e) {
                    if (printErrors) {
                        final File dir = secretsDirectory.toFile();
                        printOut("Not able to read file list from " + dir.getPath());
                    }
                }

                if (dockerSecretsMap != null && !dockerSecretsMap.isEmpty()) {
                    final boolean printSecrets = env.getProperty(PROP_PREFIX + "print.secrets", Boolean.class, false);
                    if (printSecrets) {
                        printOut("=======================");
                        printOut("    Docker Secrets     ");
                        printOut("=======================");
                        dockerSecretsMap.forEach((k, v) -> printOut(k + "=" + v));
                        printOut("=======================");
                    }

                    MapPropertySource mps = new MapPropertySource("DockerSecrets", dockerSecretsMap);
                    env.getPropertySources().addLast(mps);
                }
            } else {
                if (printErrors) {
                    printOut("Path " + secretsDirectory.toFile().getPath() + " for Docker Secrets not found!");
                }
            }
        }
    }

    private void printOut(String message) {
        System.out.println(message);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
