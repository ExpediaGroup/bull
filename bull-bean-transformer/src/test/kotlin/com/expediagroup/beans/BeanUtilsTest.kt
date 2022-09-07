/**
 * Copyright (C) 2019-2022 Expedia, Inc.
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
package com.expediagroup.beans

import com.expediagroup.beans.sample.FromFooSimple
import com.expediagroup.beans.sample.immutable.ImmutableToFoo
import com.expediagroup.beans.sample.immutable.ImmutableToFooMissingCustomAnnotation
import com.expediagroup.beans.sample.immutable.ImmutableToFooSimple
import com.expediagroup.transformer.error.InvalidBeanException
import org.assertj.core.api.Assertions
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations
import org.testng.annotations.BeforeClass
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import java.math.BigInteger
import java.util.*
import java.util.function.Function
import java.util.stream.Collectors

/**
 * Unit test for [BeanUtils].
 */
class BeanUtilsTest {
    /**
     * The class to be tested.
     */
    @InjectMocks
    private lateinit var underTest: BeanUtils

    /**
     * Initialized mocks.
     */
    @BeforeClass
    fun beforeClass() {
        MockitoAnnotations.openMocks(this)
    }

    /**
     * Test that a Transformer is returned.
     */
    @Test
    fun testGetTransformerWorksProperly() {
        // GIVEN

        // WHEN
        val transformer = underTest.transformer

        // THEN
        Assertions.assertThat(transformer).isNotNull
    }

    /**
     * Test that an exception is returned if the given transformer is null.
     */
    @Test(expectedExceptions = [IllegalArgumentException::class])
    fun testGetTransformerThrowsExceptionIfTheBeanTransformerIsNull() {
        // GIVEN

        // WHEN
        BeanUtils.getTransformer<Any, ImmutableToFooSimple>(null, ImmutableToFooSimple::class.java)
    }

    /**
     * Test that the transformer function returned is able to transform the given object.
     * @param testCaseDescription the test case description
     * @param transformerFunction the transform function to use
     */
    @Test(dataProvider = "dataStaticTransformationTesting")
    fun testGetTransformerFunctionWorksProperly(
        testCaseDescription: String?,
        transformerFunction: Function<FromFooSimple, ImmutableToFooSimple>
    ) {
        // GIVEN
        val fromFooSimple = createFromFooSimple()
        val fromFooSimpleList = listOf(fromFooSimple, fromFooSimple)

        // WHEN
        val actual = fromFooSimpleList.stream()
            .map(transformerFunction)
            .collect(Collectors.toList())

        // THEN
        Assertions.assertThat(transformerFunction).isNotNull
        Assertions.assertThat(actual).usingRecursiveComparison()
            .ignoringAllOverriddenEquals()
            .isEqualTo(fromFooSimpleList)
    }

    /**
     * Creates the parameters to be used for testing the default transformation operations.
     * @return parameters to be used for testing the default transformation operations.
     */
    @DataProvider(parallel = true)
    private fun dataStaticTransformationTesting(): Array<Array<Any>> {
        val beanUtils = BeanUtils()
        return arrayOf(
            arrayOf(
                "Test that the transformer function returned is able to transform the given object.",
                BeanUtils.getTransformer<Any, ImmutableToFooSimple>(ImmutableToFooSimple::class.java)
            ),
            arrayOf(
                "Test that the transformer function returned is able to transform the given object with the given transformer.",
                BeanUtils.getTransformer<Any, ImmutableToFooSimple>(
                    beanUtils.transformer,
                    ImmutableToFooSimple::class.java
                )
            )
        )
    }

    /**
     * Test that no exceptions are thrown when the bean is valid.
     */
    @Test
    fun testValidateWorksProperly() {
        // GIVEN
        val validBean = ImmutableToFooMissingCustomAnnotation(NAME, ID.toInt())

        // WHEN
        underTest.validator.validate(validBean)
    }

    /**
     * Test that an exception is returned if the given bean is not valid.
     */
    @Test(expectedExceptions = [InvalidBeanException::class])
    fun testValidateThrowsExceptionIfTheGivenBeanIsInvalid() {
        // GIVEN
        val immutableToFoo = ImmutableToFoo(null, null, null, null, null)

        // WHEN
        underTest.validator.validate(immutableToFoo)
    }

    /**
     * Test that a [Converter] is correctly returned.
     */
    @Test
    fun testGetPrimitiveTypeConverterWorksProperly() {
        // GIVEN

        // WHEN
        val actual = underTest.primitiveTypeConverter

        // THEN
        Assertions.assertThat(actual).isNotNull
    }

    /**
     * Creates a [FromFooSimple] instance.
     * @return the [FromFooSimple] instance.
     */
    private fun createFromFooSimple(): FromFooSimple {
        return FromFooSimple(NAME, ID, ACTIVE)
    }

    companion object {
        private val ID = BigInteger("1234")
        private const val NAME = "Goofy"
        private const val ACTIVE = true
    }
}