# BULL Change Log (`jdk 8` or above)

All notable changes to this project will be documented in this file.

### [1.7.6-jdk8] 2020.06.23
#### Changed
* * Fixes an issue that was preventing the transformation of Object type fields

### [1.7.4-jdk8] 2020.12.24
#### Changed
* Updated `hibernate-validator` version to `7.0.0.Final` (was `6.1.5.Final`).

### [1.7.3-jdk8] 2020.10.07
#### Changed
* Provides new utilities methods

### [1.7.2-jdk8] 2020.06.09
#### Changed
* Removes the deprecated method: `setDefaultValueSetEnabled`

### [1.7.1-jdk8] 2020.06.09
#### Changed
* Deprecates the method: `setDefaultValueSetEnabled` and replaces it with: `setDefaultValueForMissingPrimitiveField`
* Updated `hibernate-validator` version to `6.1.5.Final` (was `6.1.4.Final`).

### [1.7.0-jdk8] 2020.03.20
#### Added
* Implemented transformation of JavaBeans using custom Builder pattern (see: [Issue 144](https://github.com/HotelsDotCom/bull/issues/144)).
#### Changed
* Updated `hibernate-validator` version to `6.1.3.Final` (was `6.1.2.Final`).

### [1.6.5-jdk8] 2020.03.16
#### Changed
* Updated `hotels-oss-parent` version to `5.0.0` (was `4.2.0`).
* Updated `commons-lang3` version to `3.10` (was `3.9`).

### [1.6.5] 2020.01.21
#### Changed
* Testing dependencies update

### [1.6.4] 2019.12.24
#### Added
* Implemented Wildcards types support (see: [Issue 111](https://github.com/HotelsDotCom/bull/issues/111)).
* Implemented transformation of a field declared with its interface.

### [1.6.3.1-jdk8] 2019.12.19
#### Added
* Added method for retrieving the class getter methods.

### [1.6.3-jdk8] 2019.12.02
### Added
* Added retry mechanism on the Bean injection in case the parameter names are not available in the compiled code.
* Modified Travis configuration in order to test the compatibility with other JDKs versions

### [1.6.2-jdk8] 2019.11.27
#### Changed
* Aligned `jdk8` version to the `jdk11` one

### [1.1.27] 2019.11.22
#### Changed
* Removed warning leg message in case the constructor parameter names are not available in the compiled code.
* Removed `slf4j-api` dependency from the library jar.

### [1.1.26] 2019.11.18
### Added
* Added specific exception message in case the constructor invoke fails due to missing parameter name in the compiled code.
#### Changed
* Updated `hotels-oss-parent` version to `4.2.0` (was `4.1.0`).

### [1.1.25] 2019.10.30
### Removed
* Removed deprecated module `bean-utils-library`, the new one is: `bean-bean-transformer`
* The following deprecated classes has been removed:
    * `com.hotels.beans.model.FieldMapping`
    * `com.hotels.beans.model.FieldTransformer`
    * `com.hotels.beans.Transformer`
### Added
* New specific exception in case the Field Transformation function defined is not valid
* Implemented a new functionality that allows to transform also Map object applying transformation function and mappings
#### Changed
* `Transformer` class previously in charge of the Java Bean transformation has been moved to `BeanTransformer`
* Updated `hotels-oss-parent` version to `4.2.0` (was `4.1.0`).
* Updated `hibernate-validator` version to `6.1.0.Final` (was `6.0.17.Final`).

### [1.1.24] 2019.09.02
#### Changed
* **The module `bean-utils-library` has been deprecated and will be no longer available since version `1.1.25`, use `bull-bean-transformer` instead.**
    ```xml
    <dependency>
        <groupId>com.hotels.beans</groupId>
        <artifactId>bull-bean-transformer</artifactId>
        <version>x.y.z</version>
    </dependency>
    ```
* Module `bean-utils-library` has been relocated into `bull-bean-transformer`.
* The following classes has been deprecated, please find below the complete list and the new one to be used:

    | Deprecated | **New one** |
    | :----------- | :----------- |
    | `com.hotels.beans.model.FieldMapping` | `com.hotels.transformer.model.FieldMapping` |
    | `com.hotels.beans.model.FieldTransformer` | `com.hotels.transformer.model.FieldTransformer` |
    | `com.hotels.beans.Transformer` | `com.hotels.transformer.Transformer` |
    
### [1.1.23] 2019.08.06
#### Added
* Implemented automatic conversion of basic types (see: [Issue 61](https://github.com/HotelsDotCom/bull/issues/61)).
#### Changed
* Modified Transformer initialization in order to create a `Validator` instance only if the validation is enabled
* Modified Transformer initialization in order to create a `ConversionAnalyzer` instance only if the automatic conversion is enabled

### [1.1.22] 2019.07.03
#### Added
* Implemented possibility to disable the default value set for primitive types in case its value is null (see: [Issue 73](https://github.com/HotelsDotCom/bull/issues/73)).

### [1.1.21] 2019.06.27
#### Changed
* Improved exception messages in order to provide more details (see: [Issue 70](https://github.com/HotelsDotCom/bull/issues/70)).

### [1.1.20] 2019.05.24
#### Added
* Added possibility to define transformer function without arguments if not needed (see: [Issue 62](https://github.com/HotelsDotCom/bull/issues/62)).
* Added new maven profile: `check-for-updates` for checking if any dependency can be updated (see: [Issue 68](https://github.com/HotelsDotCom/bull/issues/68)).
* Added check during project build in order to prevent the add different versions of the same dependency.
#### Fixed
* Fixed a bug: FieldTransformer was receiving a default value instead of the source bean one (see: [Issue 64](https://github.com/HotelsDotCom/bull/issues/64)).

### [1.1.19] 2019.05.23
#### Changed
* Made the project multi module

### [1.1.18] 2019.05.18
#### Changed
* Removed deprecated method: `setValidationDisabled`
* Testing dependencies update

### [1.1.17] 2019.05.13
#### Changed
* **Modified project behaviour:** since this version the **"Bean Validation" is disabled by default**, to enable it, the following instruction needs to be executed: `transformer.setValidationEnabled(true);`

### [1.1.16] 2019.05.11
### Added
* Modified project structure in order to offer Java Bean validation feature against the defined constraints as public feature (see: [Issue 57](https://github.com/HotelsDotCom/bull/issues/57)).

### [1.1.15] 2019.05.08
#### Changed
* Modified value retrieval/set from/to source/destination object in order to minimise the executed actions
* Updated `hotels-oss-parent` version to `4.0.1` (was `4.0.0`).

### [1.1.14] 2019.04.28
#### Added
* Added support for the transformation of Java Beans built through Builder

### [1.1.13] 2019.04.18
#### Changed
* Improved optional usage.
* Fixed bug that was preventing the transformer function to return a null value (see: [Issue 52](https://github.com/HotelsDotCom/bull/issues/52)).  

### [1.1.12] 2019.04.06
#### Added
* Implemented possibility to use static transformation with a given transformer (see: [Issue 44](https://github.com/HotelsDotCom/bull/issues/44)).

### [1.1.10] 2019.03.31
#### Added
* Improved field value retrieval function.

### [1.1.9] 2019.03.23
#### Changed
* Added caching for method: `getDeclaredField`

### [1.1.8] 2019.03.22
#### Added
* Implemented a new feature that allows to skip the transformation for a given set of fields (see: [Issue 38](https://github.com/HotelsDotCom/bull/issues/38))
* Performance improvement

### [1.1.7] 2019.03.20
#### Changed
* Testing dependencies update

### [1.1.6] 2019.03.05
#### Added
* Implemented a new feature that allows the copy on an existing object instance (see: [Issue 24](https://github.com/HotelsDotCom/bull/issues/24)) for project compiled with `jdk` 8.
* Added profile: `fast` that skips the following plugin execution: `javadoc`, `checkstyle`, `pmd` and `jacoco`

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
