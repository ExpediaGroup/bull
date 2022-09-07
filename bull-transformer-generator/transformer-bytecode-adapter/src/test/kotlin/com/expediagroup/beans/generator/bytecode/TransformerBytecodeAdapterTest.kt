/**
 * Copyright (C) 2019-2021 Expedia, Inc.
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
package com.expediagroup.beans.generator.bytecode

import org.assertj.core.api.Assertions.assertThat

/**
 * Tests for [TransformerBytecodeAdapter].
 */
class TransformerBytecodeAdapterTest {
    @Spy
    private val spec: TransformerSpec = TransformerSpec(MappingCodeFactory.newInstance())

    @Mock
    private val compiler: JavaStringCompiler? = null

    /**
     * The class to be tested.
     */
    private var underTest: TransformerBytecodeAdapter? = null

    @BeforeMethod
    private fun beforeMethod() {
        openMocks(this)
        underTest = TransformerBytecodeAdapter.builder()
            .spec(spec)
            .build()
    }

    @Test
    fun shouldCreateANewTransformerWithGivenSpec() {
        // WHEN
        val actual: Unit = underTest.newTransformer(Source::class.java, Destination::class.java)

        // THEN
        assertThat(actual).`as`("a new Transformer instance is never null")
            .isNotNull()
        then(spec).should()
            .build(Source::class.java, Destination::class.java)
    }

    @Test
    fun shouldCreateANewTransformerWithDefaultPackage() {
        // WHEN
        val actual: Unit = underTest.newTransformer(Source::class.java, Destination::class.java)

        // THEN
        assertThat(actual.getClass()).hasPackage(DEFAULT_PACKAGE)
    }

    @Test
    fun shouldCreateANewTransformerWithGivenPackage() {
        // GIVEN
        val packageName = "foo.bar"
        underTest = TransformerBytecodeAdapter.builder()
            .packageName(packageName)
            .spec(spec)
            .build()

        // WHEN
        val actual: Unit = underTest.newTransformer(Source::class.java, Destination::class.java)

        // THEN
        assertThat(actual.getClass()).hasPackage(packageName)
    }

    @Test(expectedExceptions = TransformerGeneratorException::class)
    @Throws(Exception::class)
    fun shouldThrowRuntimeExceptionIfCompilationFails() {
        // GIVEN
        underTest = TransformerBytecodeAdapter.builder()
            .compiler(mockCompilationFail())
            .spec(spec)
            .build()

        // WHEN
        underTest.newTransformer(Source::class.java, Destination::class.java)
    }

    @Test(expectedExceptions = TransformerGeneratorException::class)
    @Throws(Exception::class)
    fun shouldThrowRuntimeExceptionIfLoadingFails() {
        // GIVEN
        underTest = TransformerBytecodeAdapter.builder()
            .compiler(mockLoadingFail())
            .spec(spec)
            .build()

        // WHEN
        underTest.newTransformer(Source::class.java, Destination::class.java)
    }

    @Test(dataProvider = "nonCompliantTransformers", expectedExceptions = TransformerGeneratorException::class)
    @Throws(
        Exception::class
    )
    fun shouldThrowRuntimeExceptionIfTransformerIsNonCompliant(trClass: Class<out Transformer?>) {
        // GIVEN
        underTest = TransformerBytecodeAdapter.builder()
            .compiler(mockCompilerReturning(trClass))
            .spec(spec)
            .build()

        // WHEN
        underTest.newTransformer(Source::class.java, Destination::class.java)
    }

    @Throws(IOException::class)
    private fun mockCompilationFail(): JavaStringCompiler {
        return given(compiler.compile(anyString(), anyString()))
            .willThrow(IOException::class.java)
            .getMock()
    }

    @Throws(ClassNotFoundException::class, IOException::class)
    private fun mockLoadingFail(): JavaStringCompiler {
        return given(compiler.loadClass(anyString(), anyMap()))
            .willThrow(ClassNotFoundException::class.java)
            .getMock()
    }

    @SuppressWarnings("unchecked")
    @Throws(Exception::class)
    private fun mockCompilerReturning(trClass: Class): JavaStringCompiler {
        return given(compiler.loadClass(anyString(), anyMap()))
            .willReturn(trClass)
            .getMock() as JavaStringCompiler
    }

    companion object {
        @DataProvider
        fun nonCompliantTransformers(): Array<Array<Object>> {
            return arrayOf(
                arrayOf<Object>(TransformerPrivateCtor::class.java),
                arrayOf<Object>(
                    TransformerCtorWithArgs::class.java
                ),
                arrayOf<Object>(TransformerCtorThrows::class.java)
            )
        }
    }
}
