# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.3.0] - 2020-04-18

### Important

- JCenter is shutting down! Lib was migrated to MavenCentral (#1)

## [1.2.1] - 2020-03-30

### Added
- Config property `docker.boot.secret.print.secrets.unmasked` to print secrets unmasked. Defaults to false. If users decide to print the unmasked secrets to System.out it is their decision and responsibility, not the libraries one.

### Improved
- Print secrets alphabetically

## [1.1.1] - 2020-02-20

### Fixed
- Mask secrets because users might use the feature unintentional in production

## [1.1.0] - 2020-02-20

### Added
- Config property `docker.boot.secret.trim` to control trimming of secret value. Defaults to true.

### Fixed
- Tests added ;)

## [1.0.0] - 2020-02-19

### Added
- DockerSecretEnvPostProcessor for including Docker Secrets as property


[Unreleased]: https://github.com/moberwasserlechner/docker-secret-spring-integration/compare/1.3.0...master
[1.3.0]: https://github.com/moberwasserlechner/docker-secret-spring-integration/compare/1.2.1...1.3.0
[1.2.1]: https://github.com/moberwasserlechner/docker-secret-spring-integration/compare/1.1.1...1.2.1
[1.1.1]: https://github.com/moberwasserlechner/docker-secret-spring-integration/compare/1.1.0...1.1.1
[1.1.0]: https://github.com/moberwasserlechner/docker-secret-spring-integration/compare/1.0.0...1.1.0
[1.0.0]: https://github.com/moberwasserlechner/docker-secret-spring-integration/releases/tag/1.0.0
