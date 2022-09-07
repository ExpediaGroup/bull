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
package com.expediagroup.beans.conversion.analyzer

import com.expediagroup.beans.conversion.processor.ConversionProcessor
import com.expediagroup.beans.conversion.processor.impl.BigDecimalConversionProcessor
import com.expediagroup.beans.conversion.processor.impl.BigIntegerConversionProcessor
import com.expediagroup.beans.conversion.processor.impl.BooleanConversionProcessor
import com.expediagroup.beans.conversion.processor.impl.ByteArrayConversionProcessor
import com.expediagroup.beans.conversion.processor.impl.ByteConversionProcessor
import com.expediagroup.beans.conversion.processor.impl.CharacterConversionProcessor
import com.expediagroup.beans.conversion.processor.impl.DoubleConversionProcessor
import com.expediagroup.beans.conversion.processor.impl.FloatConversionProcessor
import com.expediagroup.beans.conversion.processor.impl.IntegerConversionProcessor
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
import java.util.function.Function

/**
 * Unit test for [ConversionAnalyzer].
 */
class ConversionAnalyzerTest {
    /**
     * The class to be tested.
     */
    @InjectMocks
    private lateinit var underTest: ConversionAnalyzer

    /**
     * Initializes mock.
     */
    @BeforeClass
    fun beforeClass() {
        MockitoAnnotations.openMocks(this)
    }

    /**
     * Tests that the method `getConversionFunction` returns an empty optional.
     * @param testCaseDescription the test case description
     * @param sourceFieldType source field class
     * @param destinationFieldType the destination field class
     */
    @Test(dataProvider = "dataGetConversionFunctionEmptyCaseTesting")
    fun testGetConversionFunctionReturnsAnEmptyOptional(
        testCaseDescription: String?,
        sourceFieldType: Class<*>?,
        destinationFieldType: Class<*>?
    ) {
        // GIVEN

        // WHEN
        val actual = underTest.getConversionFunction(sourceFieldType, destinationFieldType)

        // THEN
        Assertions.assertThat(actual).isEmpty
    }

    /**
     * Creates the parameters to be used for testing that the method `getConversionFunction` returns an empty Optional.
     * @return parameters to be used for testing that the method `getConversionFunction` returns an empty Optional.
     */
    @DataProvider
    private fun dataGetConversionFunctionEmptyCaseTesting(): Array<Array<Any?>> {
        return arrayOf(
            arrayOf(
                "Tests that the method returns an empty optional in case the source field type is equal to the destination field type",
                Int::class.javaPrimitiveType, Int::class.javaPrimitiveType
            ), arrayOf(
                "Tests that the method returns an empty optional in case the source field type is not equal to the source field type but it's not primitive",
                Pair::class.java, Int::class.javaPrimitiveType
            ), arrayOf(
                "Tests that the method returns an empty optional in case the source field type is not equal to the source field type and the destination type is void",
                Int::class.javaPrimitiveType, Void::class.java
            ), arrayOf(
                "Tests that the method returns an empty optional in case the source field type is not equal to the source field type and the source type is void",
                Void::class.java, Int::class.javaPrimitiveType
            )
        )
    }

    /**
     * Tests that the method `getConversionFunction` returns the expected conversion function.
     * @param testCaseDescription the test case description
     * @param sourceFieldType source field class
     * @param destinationFieldType the destination field class
     * @param expectedConversionFunction the expected [ConversionProcessor] instance
     */
    @Test(dataProvider = "dataGetConversionFunctionTesting")
    fun testGetConversionFunctionReturnsTheExpectedConversionFunction(
        testCaseDescription: String?,
        sourceFieldType: Class<*>?,
        destinationFieldType: Class<*>?,
        expectedConversionFunction: Function<*, *>
    ) {
        // GIVEN

        // WHEN
        val actual = underTest.getConversionFunction(sourceFieldType, destinationFieldType)

        // THEN
        Assertions.assertThat(actual).contains(expectedConversionFunction as Function<Any?, Any?>)
    }

    /**
     * Creates the parameters to be used for testing the method `getConversionFunction`.
     * @return parameters to be used for testing the method `getConversionFunction`.
     */
    @DataProvider
    private fun dataGetConversionFunctionTesting(): Array<Array<Any?>> {
        return arrayOf(
            arrayOf(
                "Tests that the method returns a IntegerConversionProcessor that converts from String to int",
                String::class.java, Int::class.javaPrimitiveType, IntegerConversionProcessor().convertString()
            ), arrayOf(
                "Tests that the method returns a StringConversionProcessor that converts from byte to String",
                Byte::class.javaPrimitiveType, String::class.java, StringConversionProcessor().convertByte()
            ), arrayOf(
                "Tests that the method returns a ShortConversionProcessor that converts from short to byte",
                Short::class.javaPrimitiveType, Byte::class.javaPrimitiveType, ByteConversionProcessor().convertShort()
            ), arrayOf(
                "Tests that the method returns a StringConversionProcessor that converts from int to String",
                Int::class.javaPrimitiveType, String::class.java, StringConversionProcessor().convertInteger()
            ), arrayOf(
                "Tests that the method returns a IntegerConversionProcessor that converts from long to int",
                Long::class.javaPrimitiveType, Int::class.javaPrimitiveType, IntegerConversionProcessor().convertLong()
            ), arrayOf(
                "Tests that the method returns a DoubleConversionProcessor that converts from float to double",
                Float::class.javaPrimitiveType,
                Double::class.javaPrimitiveType,
                DoubleConversionProcessor().convertFloat()
            ), arrayOf(
                "Tests that the method returns a FloatConversionProcessor that converts from double to float",
                Double::class.javaPrimitiveType,
                Float::class.javaPrimitiveType,
                FloatConversionProcessor().convertDouble()
            ), arrayOf(
                "Tests that the method returns a StringConversionProcessor that converts from char to String",
                Char::class.javaPrimitiveType, String::class.java, StringConversionProcessor().convertCharacter()
            ), arrayOf(
                "Tests that the method returns a CharacterConversionProcessor that converts from char to boolean",
                Boolean::class.javaPrimitiveType,
                Char::class.javaPrimitiveType,
                CharacterConversionProcessor().convertBoolean()
            ), arrayOf(
                "Tests that the method returns a BooleanConversionProcessor that converts from String to boolean",
                String::class.java, Boolean::class.javaPrimitiveType, BooleanConversionProcessor().convertString()
            ), arrayOf(
                "Tests that the method returns a BigIntegerConversionProcessor that converts from BigDecimal to BigInteger",
                BigDecimal::class.java, BigInteger::class.java, BigIntegerConversionProcessor().convertBigDecimal()
            ), arrayOf(
                "Tests that the method returns a BigDecimalConversionProcessor that converts from String to BigDecimal",
                String::class.java, BigDecimal::class.java, BigDecimalConversionProcessor().convertString()
            ), arrayOf(
                "Tests that the method returns a ByteConversionProcessor that converts from String to byte[]",
                String::class.java, ByteArray::class.java, ByteArrayConversionProcessor().convertString()
            )
        )
    }
}