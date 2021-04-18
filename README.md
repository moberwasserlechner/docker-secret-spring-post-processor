# Spring Boot PostProcessor for Docker Secrets

![Tests](https://github.com/moberwasserlechner/docker-secret-spring-post-processor/workflows/Tests/badge.svg)

Docker secrets are mounted to the container on a file basis. This is an approach not support by Spring out of the box.

The class `DockerSecretEnvPostProcessor` reads these secret files and creates properties usable like any other spring property.

```properties
# `docker_secret_` is the prefix and `email_username` the name of the secret file
app.email.username=${docker_secret_email_username}
```

## Setup

To enable this post processor you have to create `src/main/resources/META-INF/spring.factories` containing
```
org.springframework.boot.env.EnvironmentPostProcessor=com.byteowls.docker.boot.DockerSecretEnvPostProcessor
```

## Configure

After that you can configure the post processor from your `application-<env>.properties` or any other Spring supported mechanism.

```properties
#key=default_value

# enable/disable the post processor during runtime
docker.boot.secret.enabled=true
# docker secret mount path
docker.boot.secret.path=/run/secrets
# prefix for secret name 
docker.boot.secret.prefix=docker_secret_
# remove leading and trailing whitespace chars
docker.boot.secret.trim=true
# print errors to System.out
docker.boot.secret.print.errors=true
# Attention: print all found secrets (masked) to System.out. Use with care.
docker.boot.secret.print.secrets=false
# Attention: unmasks all printed secrets. Use with care.
docker.boot.secret.print.secrets.unmasked=false
```

Note: We print to System.out because the log system is **not ready**, when the post processor is executed.

## Dependency

Maven

```xml
<dependency>
    <groupId>com.byteowls</groupId>
    <artifactId>docker-secret-spring-post-processor</artifactId>
    <version>replace.with.version</version>
</dependency>
```

Gradle

```gradle
dependencies {
  implementation "com.byteowls:docker-secret-spring-post-processor:REPLACE.WITH.VERSION"
}
```

## Contribute

See [Contribution Guidelines](https://github.com/moberwasserlechner/docker-secret-spring-post-processor/blob/master/.github/CONTRIBUTING.md).

## Gradle

```
./gradlew wrapper --gradle-version 6.8.3
```

## Changelog
See [CHANGELOG](https://github.com/moberwasserlechner/docker-secret-spring-post-processor/blob/master/CHANGELOG.md).

## License

MIT. Please see [LICENSE](https://github.com/moberwasserlechner/docker-secret-spring-post-processor/blob/master/LICENSE).

## BYTEOWLS Software & Consulting

This plugin is powered by [BYTEOWLS Software & Consulting](https://byteowls.com).

If you need extended support for this project like critical changes or releases ahead of schedule. Feel free to contact us for a consulting offer.
