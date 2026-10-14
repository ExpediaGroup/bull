# BULL Java 26 Guide

BULL 4.0.0+ requires JDK 26. Use the 3.x releases if you need an older JDK.

## 1. Records

Java Records are handled as immutable objects: every record field is final, so BULL
classifies them as `IMMUTABLE` and transforms them through the canonical constructor.

```java
public record Person(String name, int age, String email) {}
public record PersonDto(String name, int age, String email) {}

var person = new Person("John Doe", 30, "john@example.com");
var personDto = transformer.transform(person, PersonDto.class);
```

## 2. RecordUtils

An opt-in utility for working with Java Records programmatically. Non-record classes yield
empty results (or `null` for the constructor) instead of throwing.

```java
RecordComponent[] components = RecordUtils.getRecordComponents(MyRecord.class);
List<String> names = RecordUtils.getRecordComponentNames(MyRecord.class);
List<Class<?>> types = RecordUtils.getRecordComponentTypes(MyRecord.class);
Constructor<?> constructor = RecordUtils.getCanonicalConstructor(MyRecord.class);
RecordUtils.streamComponents(MyRecord.class).forEach(c -> System.out.println(c.getName()));
boolean isRecord = RecordUtils.isRecord(MyRecord.class);
```

## 3. VirtualThreadUtils

An opt-in utility for running work on virtual threads, which suit I/O-bound tasks.

```java
// Transform a collection concurrently; results keep the input order
List<PersonDto> dtos = VirtualThreadUtils.transformConcurrently(
    people,
    person -> transformer.transform(person, PersonDto.class)
);

// Run a task asynchronously
Future<PersonDto> future = VirtualThreadUtils.executeAsync(
    () -> transformer.transform(person, PersonDto.class)
);

// Create a named, unstarted virtual thread
Thread thread = VirtualThreadUtils.createVirtualThread("bean-transformer-1", () -> { });
thread.start();
```

Behaviour notes:
* `transformConcurrently` wraps any transformation failure in an `IllegalStateException`
  and cancels the remaining tasks.
* If the calling thread is interrupted, the interrupt flag is restored before the
  exception is thrown.

For CPU-bound transformations virtual threads bring no benefit; measure before adopting them.

## 4. Build requirements

```xml
<properties>
    <jdk.version>26</jdk.version>
</properties>
```

No preview features are required.

## References

- [JEP 395: Records](https://openjdk.org/jeps/395)
- [JEP 444: Virtual Threads](https://openjdk.org/jeps/444)
