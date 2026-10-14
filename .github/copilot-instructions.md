# Repository instructions

## Build, test, and quality checks

- The project targets Java 21 and requires Maven 3.8.1 or later. Prefer the Maven wrapper.
- Run the full build and tests with `./mvnw clean install`.
- Run the CI verification lifecycle, including tests and quality checks, with `./mvnw verify`. CI uses `-P compatibility-mode` to omit performance tests, Javadoc generation, and Spotless execution; use this profile when you need to match CI.
- Run one test class in a module and its required reactor modules with `./mvnw -pl <module> -am -Dtest=<TestClass> -Dsurefire.failIfNoSpecifiedTests=false test`. For example: `./mvnw -pl bull-bean-transformer -am -Dtest=BeanTransformerTest -Dsurefire.failIfNoSpecifiedTests=false test`.
- Run Checkstyle directly with `./mvnw checkstyle:check`. The project config is in `config/checkstyle/rules.xml`; the normal verification build also runs its configured quality checks.
- `./mvnw clean install -P relaxed` is available when you intentionally need to bypass Checkstyle and coverage gates; do not use it as the default validation command.

## Architecture

BULL is a Java bean transformation library. The root Maven reactor contains the shared `bull-common` core, `bull-bean-transformer`, `bull-map-transformer`, and `bull-converter`, plus the BOM, reporting, and benchmark modules.

`bull-common` owns shared transformer settings and contracts, reflection/class utilities, cache management, validation, and base transformer behavior. The bean transformer builds on that core to recursively populate mutable, immutable, and mixed Java beans (including records and builder-based targets), applying configured field mappings, custom field functions, optional type conversion, and validation. The map transformer handles map keys and values and delegates nested bean transformations to the bean transformer. The converter provides the type-conversion analyzer and conversion processors used by the library.

Transformer configuration is stored in settings objects and shared base behavior is implemented in `AbstractTransformer`; implementations specialize how values are populated. When changing transformation behavior, follow the call path across these shared settings/utilities and the affected transformer, and keep the related module tests aligned.

## Code and test conventions

- Main sources use `com.expediagroup` packages. Keep packages documented with `package-info.java`; Javadoc and Checkstyle rules apply to production and test sources.
- Follow the repository's import grouping: static imports first, then Java/Jakarta/third-party/project imports, with groups separated. Avoid wildcard imports.
- Method and constructor parameters are commonly declared `final`; retain the surrounding style. The compiler preserves parameter names (`<parameters>true</parameters>`), which matters for constructor-based bean mapping.
- Tests use TestNG (`@Test`, `@DataProvider`), AssertJ assertions, and Mockito where mocking is needed. Test classes are named `*Test`; reusable source/target bean fixtures live in test packages.
- Coverage thresholds are enforced by the normal build. Add or update tests in the module owning the behavior; for shared reflection, caching, validation, or settings behavior, check downstream transformer usage as well.

For contributor workflow and pull-request conventions, see `CONTRIBUTING.md`; for library capabilities and user-facing examples, see `README.md`.
