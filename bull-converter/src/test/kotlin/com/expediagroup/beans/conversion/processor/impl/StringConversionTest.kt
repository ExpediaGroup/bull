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
import org.testng.annotations.Test
import java.math.BigDecimal
import java.math.BigInteger

/**
 * Unit test for [StringConversionProcessor].
 */
class StringConversionTest : AbstractConversionTest() {
    /**
     * The class to be tested.
     */
    @InjectMocks
    private lateinit var underTest: StringConversionProcessor

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

        // WHEN
        val actual = underTest.convertByte().apply(BYTE_VALUE)

        // THEN
        Assertions.assertThat(actual).isEqualTo(STRING_VALUE)
    }

    @Test
    fun testConvertByteArrayShouldReturnProperResult() {
        // GIVEN
        val expected = String(EIGHT_BYTE_BYTE_ARRAY)

        // WHEN
        val actual = underTest.convertByteArray().apply(EIGHT_BYTE_BYTE_ARRAY)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun testConvertShortShouldReturnProperResult() {
        // GIVEN

        // WHEN
        val actual = underTest.convertShort().apply(SHORT_VALUE)

        // THEN
        Assertions.assertThat(actual).isEqualTo(STRING_VALUE)
    }

    @Test
    fun testConvertIntegerShouldReturnProperResult() {
        // GIVEN

        // WHEN
        val actual = underTest.convertInteger().apply(INTEGER_VALUE)

        // THEN
        Assertions.assertThat(actual).isEqualTo(STRING_VALUE)
    }

    @Test
    fun testConvertLongShouldReturnProperResult() {
        // GIVEN

        // WHEN
        val actual = underTest.convertLong().apply(LONG_VALUE)

        // THEN
        Assertions.assertThat(actual).isEqualTo(STRING_VALUE)
    }

    @Test
    fun testConvertFloatShouldReturnProperResult() {
        // GIVEN

        // WHEN
        val actual = underTest.convertFloat().apply(FLOAT_VALUE)

        // THEN
        Assertions.assertThat(actual).isEqualTo(FLOAT_VALUE.toString())
    }

    @Test
    fun testConvertDoubleShouldReturnProperResult() {
        // GIVEN

        // WHEN
        val actual = underTest.convertDouble().apply(DOUBLE_VALUE)

        // THEN
        Assertions.assertThat(actual).isEqualTo(DOUBLE_VALUE.toString())
    }

    @Test
    fun testConvertCharacterShouldReturnProperResult() {
        // GIVEN

        // WHEN
        val actual = underTest.convertCharacter().apply(CHAR_VALUE)

        // THEN
        Assertions.assertThat(actual).isEqualTo(CHAR_VALUE.toString())
    }

    @Test
    fun testConvertBooleanShouldReturnProperResult() {
        // GIVEN

        // WHEN
        val actual = underTest.convertBoolean().apply(BOOLEAN_VALUE)

        // THEN
        Assertions.assertThat(actual).isEqualTo(TRUE_AS_STRING)
    }

    @Test
    fun testConvertStringShouldReturnProperResult() {
        // GIVEN

        // WHEN
        val actual = underTest.convertString().apply(STRING_VALUE)

        // THEN
        Assertions.assertThat(actual).isEqualTo(STRING_VALUE)
    }

    @Test
    fun testConvertBigIntegerShouldReturnProperResult() {
        // GIVEN
        val expectedValue = BigDecimal.ZERO.toPlainString()

        // WHEN
        val actual = underTest.convertBigInteger().apply(BigInteger.ZERO)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expectedValue)
    }

    @Test
    fun testConvertBigDecimalShouldReturnProperResult() {
        // GIVEN
        val expectedValue = BigDecimal.ZERO.toPlainString()

        // WHEN
        val actual = underTest.convertBigDecimal().apply(BigDecimal.ZERO)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expectedValue)
    }
}