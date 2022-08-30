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
package com.expediagroup.beans.conversion.processor.impl

import com.expediagroup.beans.conversion.AbstractConversionTest
import org.assertj.core.api.Assertions
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations
import org.testng.annotations.BeforeClass
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import java.math.BigDecimal
import java.math.BigInteger

/**
 * Unit test for [ByteArrayConversionProcessor].
 */
class ByteArrayConversionTest : AbstractConversionTest() {
    /**
     * The class to be tested.
     */
    @InjectMocks
    private val underTest: ByteArrayConversionProcessor? = null

    /**
     * Initializes mock.
     */
    @BeforeClass
    fun beforeClass() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun testConvertByteShouldReturnProperResult() {
        // GIVEN
        val expected = byteArrayOf(BYTE_VALUE)

        // WHEN
        val actual = underTest!!.convertByte().apply(BYTE_VALUE)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun testConvertByteArrayShouldReturnProperResult() {
        // GIVEN

        // WHEN
        val actual = underTest!!.convertByteArray().apply(ONE_BYTE_BYTE_ARRAY)

        // THEN
        Assertions.assertThat(actual).isEqualTo(ONE_BYTE_BYTE_ARRAY)
    }

    @Test
    fun testConvertShortShouldReturnProperResult() {
        // GIVEN
        val expected = byteArrayOf(SHORT_VALUE.toByte())

        // WHEN
        val actual = underTest!!.convertShort().apply(SHORT_VALUE)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun testConvertIntegerShouldReturnProperResult() {
        // GIVEN
        val expected = byteArrayOf(INTEGER_VALUE.toByte())

        // WHEN
        val actual = underTest!!.convertInteger().apply(INTEGER_VALUE)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun testConvertLongShouldReturnProperResult() {
        // GIVEN
        val expected = byteArrayOf(LONG_VALUE.toByte())

        // WHEN
        val actual = underTest!!.convertLong().apply(LONG_VALUE)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun testConvertFloatShouldReturnProperResult() {
        // GIVEN
        val expected = byteArrayOf(FLOAT_VALUE.toInt().toByte())

        // WHEN
        val actual = underTest!!.convertFloat().apply(FLOAT_VALUE)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun testConvertDoubleShouldReturnProperResult() {
        // GIVEN
        val expected = byteArrayOf(DOUBLE_VALUE.toInt().toByte())

        // WHEN
        val actual = underTest!!.convertDouble().apply(DOUBLE_VALUE)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun testConvertCharacterShouldReturnProperResult() {
        // GIVEN
        val expected = byteArrayOf(CHAR_VALUE.code.toByte())

        // WHEN
        val actual = underTest!!.convertCharacter().apply(CHAR_VALUE)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expected)
    }

    /**
     * Tests that the method `convertBoolean` returns the expected byte.
     * @param testCaseDescription the test case description
     * @param valueToConvert the value to be converted
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "booleanToByteConvertValueTesting")
    fun testConvertBooleanShouldReturnProperResult(
        testCaseDescription: String?,
        valueToConvert: Boolean,
        expectedResult: ByteArray?
    ) {
        // GIVEN

        // WHEN
        val actual = underTest!!.convertBoolean().apply(valueToConvert)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expectedResult)
    }

    /**
     * Creates the parameters to be used for testing that the method `convertBoolean` returns the expected result.
     * @return parameters to be used for testing that the method `convertBoolean` returns the expected result.
     */
    @DataProvider
    private fun booleanToByteConvertValueTesting(): Array<Array<Any>> {
        return arrayOf(
            arrayOf(
                "Tests that the method returns {1} if the value is true",
                BOOLEAN_VALUE,
                byteArrayOf(
                    TRUE_AS_BYTE
                )
            ),
            arrayOf(
                "Tests that the method returns {0} if the value is false",
                java.lang.Boolean.FALSE,
                byteArrayOf(
                    FALSE_AS_BYTE
                )
            )
        )
    }

    @Test
    fun testConvertStringShouldReturnProperResult() {
        // GIVEN
        val expected: ByteArray = STRING_VALUE.toByteArray()

        // WHEN
        val actual = underTest!!.convertString().apply(STRING_VALUE)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun testConvertBigIntegerShouldReturnProperResult() {
        // GIVEN
        val expectedValue = byteArrayOf(BigInteger.ZERO.toByte())

        // WHEN
        val actual = underTest!!.convertBigInteger().apply(BigInteger.ZERO)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expectedValue)
    }

    @Test
    fun testConvertBigDecimalShouldReturnProperResult() {
        // GIVEN
        val expectedValue = byteArrayOf(BigDecimal.ZERO.toByte())

        // WHEN
        val actual = underTest!!.convertBigDecimal().apply(BigDecimal.ZERO)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expectedValue)
    }

    companion object {
        private const val TRUE_AS_BYTE: Byte = 1
        private const val FALSE_AS_BYTE: Byte = 0
    }
}
