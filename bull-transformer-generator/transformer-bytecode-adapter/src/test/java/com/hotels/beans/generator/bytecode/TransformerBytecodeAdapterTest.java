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
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertNotNull;

import static com.hotels.beans.generator.bytecode.TransformerBytecodeAdapter.DEFAULT_PACKAGE;

import java.io.IOException;

import org.mockito.Mock;
import org.mockito.Spy;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.hotels.beans.generator.bytecode.sample.TransformerCtorThrows;
import com.hotels.beans.generator.bytecode.sample.TransformerCtorWithArgs;
import com.hotels.beans.generator.bytecode.sample.TransformerPrivateCtor;
import com.hotels.beans.generator.core.Transformer;
import com.hotels.beans.generator.core.TransformerSpec;
import com.hotels.beans.generator.core.mapping.MappingCodeFactory;
import com.hotels.beans.generator.core.sample.javabean.Destination;
import com.hotels.beans.generator.core.sample.javabean.Source;
import com.itranswarp.compiler.JavaStringCompiler;

/**
 * Tests for {@link TransformerBytecodeAdapter}.
 */
public class TransformerBytecodeAdapterTest {
    @Spy
    private final TransformerSpec spec = new TransformerSpec(MappingCodeFactory.newInstance());

    @Mock
    private JavaStringCompiler compiler;

    private TransformerBytecodeAdapter underTest;

    @BeforeMethod
    public void setUp() {
        initMocks(this);
        underTest = TransformerBytecodeAdapter.builder()
                .spec(spec)
                .build();
    }

    @Test
    public void shouldCreateANewTransformerWithGivenSpec() {
        // WHEN
        var actual = underTest.newTransformer(Source.class, Destination.class);

        // THEN
        assertNotNull(actual, "a new Transformer instance is never null");
        then(spec).should().build(Source.class, Destination.class);
    }

    @Test
    public void shouldCreateANewTransformerWithDefaultPackage() {
        // WHEN
        var actual = underTest.newTransformer(Source.class, Destination.class);

        // THEN
        assertThat(actual.getClass().getName(), startsWith(DEFAULT_PACKAGE));
    }

    @Test
    public void shouldCreateANewTransformerWithGivenPackage() {
        // GIVEN
        var packageName = "foo.bar";
        underTest = TransformerBytecodeAdapter.builder()
                .packageName(packageName)
                .spec(spec)
                .build();

        // WHEN
        var actual = underTest.newTransformer(Source.class, Destination.class);

        // THEN
        assertThat(actual.getClass().getName(), startsWith(packageName));
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void shouldThrowRuntimeExceptionIfCompilationFails() throws Exception {
        // GIVEN
        underTest = TransformerBytecodeAdapter.builder()
                .compiler(mockCompilationFail())
                .spec(spec)
                .build();

        // WHEN
        underTest.newTransformer(Source.class, Destination.class);
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void shouldThrowRuntimeExceptionIfLoadingFails() throws Exception {
        // GIVEN
        underTest = TransformerBytecodeAdapter.builder()
                .compiler(mockLoadingFail())
                .spec(spec)
                .build();

        // WHEN
        underTest.newTransformer(Source.class, Destination.class);
    }

    @Test(dataProvider = "nonCompliantTransformers", expectedExceptions = RuntimeException.class)
    public void shouldThrowRuntimeExceptionIfTransformerIsNonCompliant(final Class<? extends Transformer> trClass)
            throws Exception {
        // GIVEN
        underTest = TransformerBytecodeAdapter.builder()
                .compiler(mockCompilerReturning(trClass))
                .spec(spec)
                .build();

        // WHEN
        underTest.newTransformer(Source.class, Destination.class);
    }

    @DataProvider
    public static Object[][] nonCompliantTransformers() {
        return new Object[][]{
                {TransformerPrivateCtor.class},
                {TransformerCtorWithArgs.class},
                {TransformerCtorThrows.class}
        };
    }

    private JavaStringCompiler mockCompilationFail() throws IOException {
        return given(compiler.compile(anyString(), anyString()))
                .willThrow(IOException.class)
                .getMock();
    }

    private JavaStringCompiler mockLoadingFail() throws ClassNotFoundException, IOException {
        return given(compiler.loadClass(anyString(), anyMap()))
                .willThrow(ClassNotFoundException.class)
                .getMock();
    }

    private JavaStringCompiler mockCompilerReturning(final Class trClass) throws Exception {
        return (JavaStringCompiler) given(compiler.loadClass(anyString(), anyMap()))
                .willReturn(trClass)
                .getMock();
    }
}
