<head>
    <title>Benchmarks</title>
</head>

# Benchmarks

BULL ships a dedicated `bull-benchmark` module that contains [JMH](https://openjdk.org/projects/code-tools/jmh/)
microbenchmarks for the transformation pipeline. JMH handles JVM warmup, dead-code elimination prevention, and
statistical sampling correctly — unlike a plain `StopWatch` loop — so the numbers it produces are trustworthy.

## What is measured

| Benchmark | Description |
|:---|:---|
| `mutableSimpleBean` | Setter-based copy of a 5-field mutable bean |
| `immutableSimpleBean` | Constructor-based copy of the same 5 fields into an immutable bean |
| `mutableComplexBean` | Mutable bean containing a nested object, a `List`, and a `Map` |
| `immutableComplexBean` | Same structure copied into an all-args-constructor immutable bean |
| `typeConversionIntToLong` | Single `int → Long` conversion through the converter hot path |
| `typeConversionStringToInt` | Single `String → Integer` conversion |
| `typeConversionBoolToBigDecimal` | Single `Boolean → BigDecimal` conversion |

All benchmarks report **average time per operation in microseconds** (`AverageTime / µs`).

## Building

The benchmark fat jar is built as part of the normal Maven build. It is skipped during `./mvnw install` because
`bull-benchmark` sets `<skipTests>true</skipTests>` and `<maven.deploy.skip>true</maven.deploy.skip>`.

Build the fat jar explicitly:

```shell
./mvnw package -pl bull-common,bull-converter,bull-bean-transformer,bull-benchmark -DskipTests -Djacoco.skip=true
```

or on Windows:

```shell
mvnw.cmd package -pl bull-common,bull-converter,bull-bean-transformer,bull-benchmark -DskipTests -Djacoco.skip=true
```

The output jar is: `bull-benchmark/target/benchmarks.jar`

## Running

### Full run (recommended for accurate results)

The default configuration uses **2 forks**, **5 warmup iterations** of 1 s each, and **10 measurement
iterations** of 1 s each per benchmark. This takes roughly 10–15 minutes and produces statistically
stable results.

```shell
java -jar bull-benchmark/target/benchmarks.jar
```

### Quick run

Reduce forks and iterations for a fast sanity check (~2 minutes):

```shell
java -jar bull-benchmark/target/benchmarks.jar -f 1 -wi 2 -i 3
```

### Single benchmark

Run only one benchmark by passing a regex that matches its name:

```shell
java -jar bull-benchmark/target/benchmarks.jar mutableSimpleBean
```

### Change output time unit

```shell
# Report in nanoseconds instead of microseconds
java -jar bull-benchmark/target/benchmarks.jar -tu ns
```

### Write results to a file

```shell
# JSON output (useful for plotting or diffing across commits)
java -jar bull-benchmark/target/benchmarks.jar -rf json -rff results.json

# CSV output
java -jar bull-benchmark/target/benchmarks.jar -rf csv -rff results.csv
```

### All JMH options

```shell
java -jar bull-benchmark/target/benchmarks.jar -help
```

## Comparing two commits

A reliable way to detect regressions or improvements is to build and run on each branch separately,
then compare the JSON outputs:

```shell
# On the baseline branch (e.g. master)
git checkout master
./mvnw package -pl bull-common,bull-converter,bull-bean-transformer,bull-benchmark -DskipTests -Djacoco.skip=true -q
java -jar bull-benchmark/target/benchmarks.jar -rf json -rff baseline.json

# On the candidate branch
git checkout feature/my-improvement
./mvnw package -pl bull-common,bull-converter,bull-bean-transformer,bull-benchmark -DskipTests -Djacoco.skip=true -q
java -jar bull-benchmark/target/benchmarks.jar -rf json -rff candidate.json
```

Then diff the two JSON files or load them into
[jmh-visualizer](https://jmh.morethan.io/) for a side-by-side chart.

## Interpreting results

A typical run prints a table like the following:

```
Benchmark                                        Mode  Cnt   Score   Error  Units
TransformerBenchmark.mutableSimpleBean           avgt   20   1.234 ± 0.045  us/op
TransformerBenchmark.immutableSimpleBean         avgt   20   1.891 ± 0.062  us/op
TransformerBenchmark.mutableComplexBean          avgt   20  12.540 ± 0.310  us/op
TransformerBenchmark.immutableComplexBean        avgt   20  14.120 ± 0.420  us/op
TransformerBenchmark.typeConversionIntToLong     avgt   20   0.043 ± 0.001  us/op
TransformerBenchmark.typeConversionStringToInt   avgt   20   0.058 ± 0.002  us/op
TransformerBenchmark.typeConversionBoolToBigDecimal avgt 20  0.071 ± 0.002  us/op
```

* **Score** — the mean time per operation across all measurement iterations.
* **Error** — the 99.9% confidence interval half-width. A small error relative to the score means the
  result is stable. If the error is large (more than ~10% of the score) run more iterations (`-i 20`).
* **Cnt** — the number of measurement data points (forks × iterations).

A change is significant only if the confidence intervals of the two runs **do not overlap**. JMH reports
this automatically in comparative mode:

```shell
java -jar bull-benchmark/target/benchmarks.jar -prof gc
```

The `-prof gc` profiler also reports allocation rates, which is often more informative than raw
throughput when evaluating object-creation optimisations.

## Adding a new benchmark

1. Add your source and destination beans under `bull-benchmark/src/main/java/com/expediagroup/beans/benchmark/sample/`.
2. Add a `@Benchmark` method to `TransformerBenchmark` (or create a new `@State` class in the same package).
3. Rebuild the fat jar and run.

Benchmark methods must return their result (or consume it via a `Blackhole` parameter) so the JIT cannot
eliminate the computation as dead code.
