# BULL Change Log

All notable changes to this project will be documented in this file.

### [1.2.0] 2019.02.25
#### Added
* Added possibility to skip the object validation (see: [Issue 31](https://github.com/HotelsDotCom/bull/issues/31))
* Added documentation and samples for the above functionality
### Changed
- Updated `jdk` version to 11 (was 1.8).
- Updated Travis configuration in order to work with java 11
- Modified Travis configuration in order to automatically create the GitHub site as soon as a tag is created

### [1.1.5] 2019.03.03
#### Added
* Added all new features into the java 8 project version.

### [1.1.4] 2019.02.20
#### Added
* Added possibility to apply a transformation function only on a specific field (see: [Issue 27](https://github.com/HotelsDotCom/bull/issues/27)).
* Added possibility to apply a transformation function on all fields matching with the given name without evaluating the full field path.
* Added samples and tests for the above functionality
#### Fixed
* Fixed issue that was preventing the `Set` transformation

### [1.1.3] 2019.02.17
#### Added
* Added static transformation functionality (see: [Issue 25](https://github.com/HotelsDotCom/bull/issues/25)).

### [1.1.2] 2019.02
#### Added
* Made the field name mapping more flexible adding the possibility to map destination object field with field contained into nested objects. 
* Added samples and tests for the above functionality.
#### Changed
* Updated hibernate dependency: `org.hibernate.validator` (was `org.hibernate.validator`).
* Removed `parallel` execution where not needed because this could cause performance degradation.

### [1.1.1] 2019.02.09
#### Changed
* Improved exception messaging in order to simplify the troubleshooting process
* Improved readme file

### [1.1.0] 2019.02.04
#### Changed
* Improved travis configuration
#### Added
* Added dependency to: `slf4j-api` as no longer available from Spring.
- Added ValidationUtils class for raising an `IllegalArgumentException` in case any parameter is null.
#### Removed
* Removed dependency: `spring-boot-starter-validation` and imported one by one the required validation dependencies
* Removed dependency: `spring-boot-starter-cache` and imported one by one the required validation dependencies

### [1.0.17] 2019.02.03
#### Changed
* Improved package-info comments
#### Added
* Configured Travis in order to automatically release artifacts

### [1.0.16] 2019.01.26
#### Changed
* Updated `spring-boot` version to `2.1.2.RELEASE` (was `2.1.0.RELEASE`).
* Updated `hotels-oss-parent` version to `4.0.0` (was `2.3.5`).
#### Added
* Configured Travis in order to automatically build the application, perform a quality check and publish site. Travis build site available [here](https://travis-ci.org/HotelsDotCom/bull) 
* Added build, test coverage and security badge to readme file.

### [1.0.15] 2019.01.23
#### Added
* Added GitHub site build with maven.

### [1.0.14] 2019.01.18
#### Added
* Added possibility to configure the transformer in order to set the default value for all destination's object fields that are not existing in the source object.
  See [README.md](https://github.com/HotelsDotCom/bull/blob/master/README.md) for more details.
#### Changed
* Jumped to version `1.0.14` in order to be consequent to the previous library version hosted on a private repo.

### [1.0.3] 2019.01.17
#### Added
* Added changelog file.

### [1.0.2] 2019.01.17
#### Changed
* Removed not needed comments

### [1.0.1] 2019.01.17
#### Changed
* Added maven build info to the readme file.

### [1.0.0] 2019.01.16
#### Added
* First `BULL` release.
