# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.1.2] - 2020-02-22

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


[Unreleased]: https://github.com/moberwasserlechner/docker-boot-utils/compare/1.1.1...master
[1.1.1]: https://github.com/moberwasserlechner/docker-boot-utils/compare/1.1.0...1.1.1
[1.1.0]: https://github.com/moberwasserlechner/docker-boot-utils/compare/1.0.0...1.1.0
[1.0.0]: https://github.com/moberwasserlechner/docker-boot-utils/releases/tag/1.0.0
