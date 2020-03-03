# Docker Spring Boot Utils

[![Download](https://img.shields.io/bintray/v/moberwasserlechner/maven/docker-boot-utils.svg)](https://bintray.com/moberwasserlechner/maven/docker-boot-utils/_latestVersion) ![Tests](https://github.com/moberwasserlechner/docker-boot-utils/workflows/Tests/badge.svg) [![Twitter Follow](https://img.shields.io/twitter/follow/michaelowl_web.svg?style=social&label=Follow&style=flat-square)](https://twitter.com/michaelowl_web)

## Docker Secret EnvironmentPostProcessor

Docker secrets are mounted to the container on a file basis. This is a approach not support by Spring out of the box.

The class `DockerSecretEnvPostProcessor` reads these secret files and creates properties usable like any other spring property.

```properties
# `docker_secret_` is the prefix and `email_username` the name of the secret file
app.email.username=${docker_secret_email_username}
```

### Setup

To enable this post processor you have to create `src/main/resources/META-INF/spring.factories` containing
```
org.springframework.boot.env.EnvironmentPostProcessor=com.byteowls.docker.boot.DockerSecretEnvPostProcessor
```

### Configure

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

### Gradle

```gradle
repositories {
  jcenter()
}
dependencies {
  compile ("com.byteowls:docker-boot-utils:replace.with.version")
}
```

### Maven

```xml
<repositories>
  <!-- ... other repository elements ... -->
  <repository>
    <snapshots>
      <enabled>false</enabled>
    </snapshots>
    <id>central</id>
    <name>bintray</name>
    <url>https://jcenter.bintray.com</url>
  </repository>
</repositories>

<dependencies>
  <!-- ... other dependency elements ... -->
  <dependency>
    <groupId>com.byteowls</groupId>
    <artifactId>docker-boot-utils</artifactId>
    <version>replace.with.version</version>
  </dependency>
</dependencies>
    
```

## Contribute

### Fix a bug or create a new feature

Please do not mix more than one issue in a feature branch. Each feature/bugfix should have its own branch and its own Pull Request (PR).

1. Create a issue and describe what you want to do at [Issue Tracker](https://github.com/moberwasserlechner/docker-boot-utils/issues)
2. Create your feature branch (`git checkout -b feature/my-feature` or `git checkout -b bugfix/my-bugfix`)
3. Test your changes to the best of your ability.
5. Commit your changes (`git commit -m 'Describe feature or bug'`)
6. Push to the branch (`git push origin feature/my-feature`)
7. Create a Github pull request

### Code Style

This repo includes a .editorconfig file, which your IDE should pickup automatically.

If not please use the sun coding convention. Please do not use tabs in Java files!

Try to change only parts your feature or bugfix requires.

## Changelog
See [CHANGELOG](https://github.com/moberwasserlechner/docker-boot-utils/blob/master/CHANGELOG.md).

## License

MIT. Please see [LICENSE](https://github.com/moberwasserlechner/docker-boot-utils/blob/master/LICENSE).
