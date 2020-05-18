/**
 * Copyright (C) 2019-2020 Expedia, Inc.
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

package com.hotels.beans.generator.bytecode;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertNotNull;

import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.hotels.beans.generator.core.Transformer;
import com.hotels.beans.generator.core.TransformerSpec;
import com.hotels.beans.generator.core.mapping.MappingCodeFactory;
import com.hotels.beans.generator.core.sample.javabean.Destination;
import com.hotels.beans.generator.core.sample.javabean.Source;

import net.openhft.compiler.CachedCompiler;

/**
 * Tests for {@link TransformerBytecodeAdapter}.
 */
public class TransformerBytecodeAdapterTest {
    @Spy
    TransformerSpec spec = new TransformerSpec(MappingCodeFactory.getInstance());

    TransformerBytecodeAdapter transformerBytecodeAdapter;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        transformerBytecodeAdapter = TransformerBytecodeAdapter.builder()
                .spec(spec)
                .build();
    }

    @Test
    public void shouldCreateANewTransformerWithGivenSpec() {
        // WHEN
        Transformer<Source, Destination> transformer =
                transformerBytecodeAdapter.newTransformer(Source.class, Destination.class);

        // THEN
        assertNotNull(transformer, "a new Transformer instance is never null");
        then(spec).should().build(Source.class, Destination.class);
    }

    @Test
    public void shouldCreateANewTransformerWithDefaultPackage() {
        // WHEN
        Transformer<Source, Destination> transformer =
                transformerBytecodeAdapter.newTransformer(Source.class, Destination.class);

        // THEN
        assertThat(transformer.getClass().getName(), startsWith(TransformerBytecodeAdapter.DEFAULT_PACKAGE));
    }

    @Test
    public void shouldCreateANewTransformerWithGivenPackage() {
        // GIVEN
        String packageName = "foo.bar";
        transformerBytecodeAdapter = TransformerBytecodeAdapter.builder()
                .packageName(packageName)
                .spec(spec)
                .build();

        // WHEN
        Transformer<Source, Destination> transformer =
                transformerBytecodeAdapter.newTransformer(Source.class, Destination.class);

        // THEN
        assertThat(transformer.getClass().getName(), startsWith(packageName));
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void shouldThrowRuntimeExceptionIfCompilerFails() throws Exception {
        // GIVEN
        CachedCompiler compiler = given(mock(CachedCompiler.class).loadFromJava(anyString(), anyString()))
                .willThrow(ClassNotFoundException.class)
                .getMock();
        transformerBytecodeAdapter = TransformerBytecodeAdapter.builder()
                .compiler(compiler)
                .spec(spec)
                .build();

        // WHEN
        transformerBytecodeAdapter.newTransformer(Source.class, Destination.class);
    }

    @Test(dataProvider = "nonCompliantTransformers", expectedExceptions = RuntimeException.class)
    public void shouldThrowRuntimeExceptionIfTransformerIsNonCompliant(final Class<? extends Transformer> trClass)
            throws Exception {
        // GIVEN
        transformerBytecodeAdapter = TransformerBytecodeAdapter.builder()
                .compiler(mockCompilerWith(trClass))
                .spec(spec)
                .build();

        // WHEN
        transformerBytecodeAdapter.newTransformer(Source.class, Destination.class);
    }

    @DataProvider
    public static Object[][] nonCompliantTransformers() {
        return new Object[][]{
                {TransformerPrivateCtor.class},
                {TransformerCtorWithArgs.class},
                {TransformerCtorThrows.class}
        };
    }

    private static CachedCompiler mockCompilerWith(final Class<? extends Transformer> trClass)
            throws ClassNotFoundException {
        return given(mock(CachedCompiler.class).loadFromJava(anyString(), anyString()))
                .willReturn(trClass)
                .getMock();
    }

    //********************************************************
    //  Transformer stubs with non-compliant constructors.
    //********************************************************

    /**
     * Constructor is not instantiable.
     */
    private static final class TransformerPrivateCtor implements Transformer<Source, Destination> {
        private TransformerPrivateCtor() {
        }

        @Override
        public Destination transform(final Source source) {
            return null;
        }
    }

    /**
     * Constructor has arguments.
     */
    private static final class TransformerCtorWithArgs implements Transformer<Source, Destination> {
        TransformerCtorWithArgs(final Object arg) {
        }

        @Override
        public Destination transform(final Source source) {
            return null;
        }

    }

    /**
     * Constructor throws exception.
     */
    private static final class TransformerCtorThrows implements Transformer<Source, Destination> {
        TransformerCtorThrows() {
            throw new RuntimeException("simulated error");
        }

        @Override
        public Destination transform(final Source source) {
            return null;
        }
    }

}
