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
package com.expediagroup.beans.conversion.processor

import com.expediagroup.beans.conversion.processor.impl.BigDecimalConversionProcessor
import com.expediagroup.beans.conversion.processor.impl.BigIntegerConversionProcessor
import com.expediagroup.beans.conversion.processor.impl.BooleanConversionProcessor
import com.expediagroup.beans.conversion.processor.impl.ByteArrayConversionProcessor
import com.expediagroup.beans.conversion.processor.impl.ByteConversionProcessor
import com.expediagroup.beans.conversion.processor.impl.CharacterConversionProcessor
import com.expediagroup.beans.conversion.processor.impl.DoubleConversionProcessor
import com.expediagroup.beans.conversion.processor.impl.FloatConversionProcessor
import com.expediagroup.beans.conversion.processor.impl.IntegerConversionProcessor
import com.expediagroup.beans.conversion.processor.impl.LongConversionProcessor
import com.expediagroup.beans.conversion.processor.impl.ShortConversionProcessor
import com.expediagroup.beans.conversion.processor.impl.StringConversionProcessor
import org.apache.commons.lang3.tuple.Pair
import org.assertj.core.api.Assertions
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations
import org.testng.annotations.BeforeClass
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import java.math.BigDecimal
import java.math.BigInteger

/**
 * Unit test for [ConversionProcessorFactory].
 */
class ConversionProcessorFactoryTest {
    /**
     * The class to be tested.
     */
    @InjectMocks
    private val underTest: ConversionProcessorFactory? = null

    /**
     * Initializes mock.
     */
    @BeforeClass
    fun beforeClass() {
        MockitoAnnotations.openMocks(this)
    }

    /**
     * Tests that the method `getConversionProcessor` returns null in case there is no processor defined for the given type.
     */
    @Test
    fun testGetConversionProcessorReturnsNullInCaseNoProcessorExistsForTheGivenType() {
        // GIVEN

        // WHEN
        val actual = ConversionProcessorFactory.getConversionProcessor(Pair::class.java)

        // THEN
        Assertions.assertThat(actual).isEmpty
    }

    /**
     * Tests that the method `getConversionProcessor` returns the expected Conversion processor for the given type.
     * @param testCaseDescription the test case description
     * @param targetClass object type for which the conversion processor has to be retrieved
     * @param expectedResult the expected [ConversionProcessor] class for the given object type
     */
    @Test(dataProvider = "dataGetConversionProcessorTesting")
    fun testGetConversionProcessorWorksAsExpected(
        testCaseDescription: String?,
        targetClass: Class<*>?,
        expectedResult: Class<ConversionProcessor<*>?>?
    ) {
        // GIVEN

        // WHEN
        val actual = ConversionProcessorFactory.getConversionProcessor(targetClass)

        // THEN
        Assertions.assertThat(actual).containsInstanceOf(expectedResult)
    }

    /**
     * Creates the parameters to be used for testing the method `getConversionProcessor`.
     * @return parameters to be used for testing the method `getConversionProcessor`.
     */
    @DataProvider
    private fun dataGetConversionProcessorTesting(): Array<Array<Any?>> {
        return arrayOf(
            arrayOf(
                "Tests that the method returns a ByteConversionProcessor is case the target class ia a byte",
                Byte::class.javaPrimitiveType,
                ByteConversionProcessor::class.java
            ),
            arrayOf(
                "Tests that the method returns a ByteConversionProcessor is case the target class ia a Byte",
                Byte::class.java,
                ByteConversionProcessor::class.java
            ),
            arrayOf(
                "Tests that the method returns a ShortConversionProcessor is case the target class ia a Short",
                Short::class.java,
                ShortConversionProcessor::class.java
            ),
            arrayOf(
                "Tests that the method returns a ShortConversionProcessor is case the target class ia a short",
                Short::class.javaPrimitiveType,
                ShortConversionProcessor::class.java
            ),
            arrayOf(
                "Tests that the method returns a IntegerConversionProcessor is case the target class is an Integer",
                Int::class.java,
                IntegerConversionProcessor::class.java
            ),
            arrayOf(
                "Tests that the method returns a IntegerConversionProcessor is case the target class is an int",
                Int::class.javaPrimitiveType,
                IntegerConversionProcessor::class.java
            ),
            arrayOf(
                "Tests that the method returns a LongConversionProcessor is case the target class is a Long",
                Long::class.java,
                LongConversionProcessor::class.java
            ),
            arrayOf(
                "Tests that the method returns a LongConversionProcessor is case the target class is a long",
                Long::class.javaPrimitiveType,
                LongConversionProcessor::class.java
            ),
            arrayOf(
                "Tests that the method returns a FloatConversionProcessor is case the target class is a Float",
                Float::class.java,
                FloatConversionProcessor::class.java
            ),
            arrayOf(
                "Tests that the method returns a FloatConversionProcessor is case the target class is a float",
                Float::class.javaPrimitiveType,
                FloatConversionProcessor::class.java
            ),
            arrayOf(
                "Tests that the method returns a DoubleConversionProcessor is case the target class is a Double",
                Double::class.java,
                DoubleConversionProcessor::class.java
            ),
            arrayOf(
                "Tests that the method returns a DoubleConversionProcessor is case the target class is a double",
                Double::class.javaPrimitiveType,
                DoubleConversionProcessor::class.java
            ),
            arrayOf(
                "Tests that the method returns a CharacterConversionProcessor is case the target class is a Character",
                Char::class.java,
                CharacterConversionProcessor::class.java
            ),
            arrayOf(
                "Tests that the method returns a CharacterConversionProcessor is case the target class is a char",
                Char::class.javaPrimitiveType,
                CharacterConversionProcessor::class.java
            ),
            arrayOf(
                "Tests that the method returns a StringConversionProcessor is case the target class is a String",
                String::class.java,
                StringConversionProcessor::class.java
            ),
            arrayOf(
                "Tests that the method returns a BooleanConversionProcessor is case the target class is a Boolean",
                Boolean::class.java,
                BooleanConversionProcessor::class.java
            ),
            arrayOf(
                "Tests that the method returns a BooleanConversionProcessor is case the target class is a boolean",
                Boolean::class.javaPrimitiveType,
                BooleanConversionProcessor::class.java
            ),
            arrayOf(
                "Tests that the method returns a BigIntegerConversionProcessor is case the target class is a BigInteger",
                BigInteger::class.java,
                BigIntegerConversionProcessor::class.java
            ),
            arrayOf(
                "Tests that the method returns a BigDecimalConversionProcessor is case the target class is a BigDecimal",
                BigDecimal::class.java,
                BigDecimalConversionProcessor::class.java
            ),
            arrayOf(
                "Tests that the method returns a ByteConversionProcessor is case the target class is a byte[]",
                ByteArray::class.java,
                ByteArrayConversionProcessor::class.java
            )
        )
    }
}