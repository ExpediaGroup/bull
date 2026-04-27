/**
 * Copyright (C) 2019-2026 Expedia, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.expediagroup.beans.benchmark;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import com.expediagroup.beans.BeanUtils;
import com.expediagroup.beans.benchmark.sample.immutable.ImmutableComplex;
import com.expediagroup.beans.benchmark.sample.immutable.ImmutableSimple;
import com.expediagroup.beans.benchmark.sample.mutable.MutableComplex;
import com.expediagroup.beans.benchmark.sample.mutable.MutableSimple;
import com.expediagroup.beans.conversion.ConverterImpl;
import com.expediagroup.beans.transformer.BeanTransformer;

/**
 * JMH microbenchmarks for the BULL transformation pipeline.
 *
 * <p>Run with: {@code java -jar target/benchmarks.jar}
 *
 * <p>Or a single benchmark: {@code java -jar target/benchmarks.jar TransformerBenchmark.mutableSimpleBean}
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 10, time = 1)
@Fork(2)
@State(Scope.Benchmark)
public class TransformerBenchmark {

    private BeanTransformer transformer;
    private MutableSimple sourceSimple;
    private MutableComplex sourceComplex;
    private Map<String, String> sourceMap;
    private ConverterImpl converter;

    @Setup
    public void setUp() {
        transformer = new BeanUtils().getTransformer();

        MutableSimple nested = new MutableSimple();
        nested.setName("nested");
        nested.setId(BigInteger.TWO);
        nested.setActive(false);
        nested.setAge(10);
        nested.setScore(1.5);

        sourceSimple = new MutableSimple();
        sourceSimple.setName("Goofy");
        sourceSimple.setId(new BigInteger("1234"));
        sourceSimple.setActive(true);
        sourceSimple.setAge(42);
        sourceSimple.setScore(9.99);

        sourceComplex = new MutableComplex();
        sourceComplex.setName("Donald");
        sourceComplex.setId(new BigInteger("5678"));
        sourceComplex.setTags(List.of("tag1", "tag2", "tag3"));
        sourceComplex.setAttributes(Map.of("k1", "v1", "k2", "v2", "k3", "v3"));
        sourceComplex.setNested(nested);
        sourceComplex.setNestedList(List.of(nested, nested, nested));

        sourceMap = Map.of("alpha", "one", "beta", "two", "gamma", "three");

        converter = new ConverterImpl();
    }

    /** Mutable simple bean: setter-based copy of 5 fields. */
    @Benchmark
    public MutableSimple mutableSimpleBean() {
        return transformer.transform(sourceSimple, MutableSimple.class);
    }

    /** Immutable simple bean: constructor-based copy of 5 fields. */
    @Benchmark
    public ImmutableSimple immutableSimpleBean() {
        return transformer.transform(sourceSimple, ImmutableSimple.class);
    }

    /** Mutable complex bean: nested object + list + map. */
    @Benchmark
    public MutableComplex mutableComplexBean() {
        return transformer.transform(sourceComplex, MutableComplex.class);
    }

    /** Immutable complex bean: all-args constructor with nested objects. */
    @Benchmark
    public ImmutableComplex immutableComplexBean() {
        return transformer.transform(sourceComplex, ImmutableComplex.class);
    }

    /** Type conversion hot path: int → long, repeated single-field conversion. */
    @Benchmark
    public Long typeConversionIntToLong() {
        return converter.convertValue(42, Long.class);
    }

    /** Type conversion: String → Integer. */
    @Benchmark
    public Integer typeConversionStringToInt() {
        return converter.convertValue("123", Integer.class);
    }

    /** Type conversion: Boolean → BigDecimal (exercises convertBoolean cached lambda). */
    @Benchmark
    public BigDecimal typeConversionBoolToBigDecimal() {
        return converter.convertValue(Boolean.TRUE, BigDecimal.class);
    }

    public static void main(final String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(TransformerBenchmark.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
}
