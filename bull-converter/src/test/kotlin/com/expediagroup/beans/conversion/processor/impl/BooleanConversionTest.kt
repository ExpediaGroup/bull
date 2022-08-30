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
 * Unit test for [BooleanConversionProcessor].
 */
class BooleanConversionTest : AbstractConversionTest() {
    /**
     * The class to be tested.
     */
    @InjectMocks
    private val underTest: BooleanConversionProcessor? = null

    /**
     * Initializes mock.
     */
    @BeforeClass
    fun beforeClass() {
        MockitoAnnotations.openMocks(this)
    }

    /**
     * Tests that the method `convertByte` returns the expected boolean.
     * @param testCaseDescription the test case description
     * @param valueToConvert the value to be converted
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "byteToBooleanConvertValueTesting")
    fun testConvertByteShouldReturnProperResult(
        testCaseDescription: String?,
        valueToConvert: Byte,
        expectedResult: Boolean
    ) {
        // GIVEN

        // WHEN
        val actual = underTest!!.convertByte().apply(valueToConvert)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expectedResult)
    }

    /**
     * Creates the parameters to be used for testing that the method `convertByte` returns the expected result.
     * @return parameters to be used for testing that the method `convertByte` returns the expected result.
     */
    @DataProvider
    private fun byteToBooleanConvertValueTesting(): Array<Array<Any>> {
        return arrayOf(
            arrayOf(
                "Tests that the method returns true if the value is not 0",
                BYTE_VALUE,
                true
            ),
            arrayOf(
                "Tests that the method returns false if the value is 0",
                BYTE_VALUE_ZERO,
                false
            )
        )
    }

    /**
     * Tests that the method `convertByteArray` returns the expected boolean.
     * @param testCaseDescription the test case description
     * @param valueToConvert the value to be converted
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "byteArrayToBooleanConvertValueTesting")
    fun testConvertByteArrayShouldReturnProperResult(
        testCaseDescription: String?,
        valueToConvert: ByteArray?,
        expectedResult: Boolean
    ) {
        // GIVEN

        // WHEN
        val actual = underTest!!.convertByteArray().apply(valueToConvert)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expectedResult)
    }

    /**
     * Creates the parameters to be used for testing that the method `convertByte` returns the expected result.
     * @return parameters to be used for testing that the method `convertByte` returns the expected result.
     */
    @DataProvider
    private fun byteArrayToBooleanConvertValueTesting(): Array<Array<Any>> {
        return arrayOf(
            arrayOf(
                "Tests that the method returns true if the value is not 0",
                byteArrayOf(BYTE_VALUE),
                true
            ),
            arrayOf(
                "Tests that the method returns false if the value is 0",
                byteArrayOf(BYTE_VALUE_ZERO),
                false
            )
        )
    }

    /**
     * Tests that the method `convertShort` returns the expected boolean.
     * @param testCaseDescription the test case description
     * @param valueToConvert the value to be converted
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "shortToBooleanConvertValueTesting")
    fun testConvertShortShouldReturnProperResult(
        testCaseDescription: String?,
        valueToConvert: Short,
        expectedResult: Boolean
    ) {
        // GIVEN

        // WHEN
        val actual = underTest!!.convertShort().apply(valueToConvert)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expectedResult)
    }

    /**
     * Creates the parameters to be used for testing that the method `convertShort` returns the expected result.
     * @return parameters to be used for testing that the method `convertShort` returns the expected result.
     */
    @DataProvider
    private fun shortToBooleanConvertValueTesting(): Array<Array<Any>> {
        return arrayOf(
            arrayOf(
                "Tests that the method returns true if the value is not 0",
                SHORT_VALUE,
                true
            ),
            arrayOf(
                "Tests that the method returns false if the value is 0",
                SHORT_VALUE_ZERO,
                false
            )
        )
    }

    /**
     * Tests that the method `convertInteger` returns the expected boolean.
     * @param testCaseDescription the test case description
     * @param valueToConvert the value to be converted
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "intToBooleanConvertValueTesting")
    fun testConvertIntegerShouldReturnProperResult(
        testCaseDescription: String?,
        valueToConvert: Int,
        expectedResult: Boolean
    ) {
        // GIVEN

        // WHEN
        val actual = underTest!!.convertInteger().apply(valueToConvert)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expectedResult)
    }

    /**
     * Creates the parameters to be used for testing that the method `convertInteger` returns the expected result.
     * @return parameters to be used for testing that the method `convertInteger` returns the expected result.
     */
    @DataProvider
    private fun intToBooleanConvertValueTesting(): Array<Array<Any>> {
        return arrayOf(
            arrayOf(
                "Tests that the method returns true if the value is not 0",
                INTEGER_VALUE,
                true
            ),
            arrayOf(
                "Tests that the method returns false if the value is 0",
                INTEGER_VALUE_ZERO,
                false
            )
        )
    }

    /**
     * Tests that the method `convertLong` returns the expected boolean.
     * @param testCaseDescription the test case description
     * @param valueToConvert the value to be converted
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "longToBooleanConvertValueTesting")
    fun testConvertLongShouldReturnProperResult(
        testCaseDescription: String?,
        valueToConvert: Long,
        expectedResult: Boolean
    ) {
        // GIVEN

        // WHEN
        val actual = underTest!!.convertLong().apply(valueToConvert)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expectedResult)
    }

    /**
     * Creates the parameters to be used for testing that the method `convertLong` returns the expected result.
     * @return parameters to be used for testing that the method `convertLong` returns the expected result.
     */
    @DataProvider
    private fun longToBooleanConvertValueTesting(): Array<Array<Any>> {
        return arrayOf(
            arrayOf(
                "Tests that the method returns true if the value is not 0",
                LONG_VALUE,
                true
            ),
            arrayOf(
                "Tests that the method returns false if the value is 0",
                LONG_VALUE_ZERO,
                false
            )
        )
    }

    /**
     * Tests that the method `convertFloat` returns the expected boolean.
     * @param testCaseDescription the test case description
     * @param valueToConvert the value to be converted
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "floatToBooleanConvertValueTesting")
    fun testConvertFloatShouldReturnProperResult(
        testCaseDescription: String?,
        valueToConvert: Float,
        expectedResult: Boolean
    ) {
        // GIVEN

        // WHEN
        val actual = underTest!!.convertFloat().apply(valueToConvert)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expectedResult)
    }

    /**
     * Creates the parameters to be used for testing that the method `convertFloat` returns the expected result.
     * @return parameters to be used for testing that the method `convertFloat` returns the expected result.
     */
    @DataProvider
    private fun floatToBooleanConvertValueTesting(): Array<Array<Any>> {
        return arrayOf(
            arrayOf(
                "Tests that the method returns true if the value is not 0",
                FLOAT_VALUE,
                true
            ),
            arrayOf(
                "Tests that the method returns false if the value is 0",
                FLOAT_VALUE_ZERO,
                false
            )
        )
    }

    /**
     * Tests that the method `convertDouble` returns the expected boolean.
     * @param testCaseDescription the test case description
     * @param valueToConvert the value to be converted
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "floatToBooleanConvertValueTesting")
    fun testConvertDoubleShouldReturnProperResult(
        testCaseDescription: String?,
        valueToConvert: Double,
        expectedResult: Boolean
    ) {
        // GIVEN

        // WHEN
        val actual = underTest!!.convertDouble().apply(valueToConvert)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expectedResult)
    }

    /**
     * Creates the parameters to be used for testing that the method `convertDouble` returns the expected result.
     * @return parameters to be used for testing that the method `convertDouble` returns the expected result.
     */
    @DataProvider
    private fun doubleToBooleanConvertValueTesting(): Array<Array<Any>> {
        return arrayOf(
            arrayOf(
                "Tests that the method returns true if the value is not 0",
                DOUBLE_VALUE,
                true
            ),
            arrayOf(
                "Tests that the method returns false if the value is 0",
                DOUBLE_VALUE_ZERO,
                false
            )
        )
    }

    @Test
    fun testConvertCharacterShouldReturnProperResult() {
        // GIVEN

        // WHEN
        val actual = underTest!!.convertCharacter().apply(CHAR_VALUE)

        // THEN
        Assertions.assertThat(actual).isFalse
    }

    @Test
    fun testConvertBooleanShouldReturnProperResult() {
        // GIVEN

        // WHEN
        val actual = underTest!!.convertBoolean().apply(BOOLEAN_VALUE)

        // THEN
        Assertions.assertThat(actual).isEqualTo(BOOLEAN_VALUE)
    }

    @Test
    fun testConvertStringShouldReturnProperResult() {
        // GIVEN

        // WHEN
        val actual = underTest!!.convertString().apply(TRUE_AS_STRING)

        // THEN
        Assertions.assertThat(actual).isTrue
    }

    /**
     * Tests that the method `convertBigInteger` returns the expected boolean.
     * @param testCaseDescription the test case description
     * @param valueToConvert the value to be converted
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "bigIntegerToBooleanConvertValueTesting")
    fun testConvertBigIntegerShouldReturnProperResult(
        testCaseDescription: String?,
        valueToConvert: BigInteger?,
        expectedResult: Boolean
    ) {
        // GIVEN

        // WHEN
        val actual = underTest!!.convertBigInteger().apply(valueToConvert)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expectedResult)
    }

    /**
     * Creates the parameters to be used for testing that the method `convertFloat` returns the expected result.
     * @return parameters to be used for testing that the method `convertFloat` returns the expected result.
     */
    @DataProvider
    private fun bigIntegerToBooleanConvertValueTesting(): Array<Array<Any>> {
        return arrayOf(
            arrayOf("Tests that the method returns true if the value is not 0", BigInteger.ONE, true),
            arrayOf("Tests that the method returns false if the value is 0", BigInteger.ZERO, false)
        )
    }

    /**
     * Tests that the method `convertBigDecimal` returns the expected boolean.
     * @param testCaseDescription the test case description
     * @param valueToConvert the value to be converted
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "bigDecimalToBooleanConvertValueTesting")
    fun testConvertBigIntegerShouldReturnProperResult(
        testCaseDescription: String?,
        valueToConvert: BigDecimal?,
        expectedResult: Boolean
    ) {
        // GIVEN

        // WHEN
        val actual = underTest!!.convertBigDecimal().apply(valueToConvert)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expectedResult)
    }

    /**
     * Creates the parameters to be used for testing that the method `convertFloat` returns the expected result.
     * @return parameters to be used for testing that the method `convertFloat` returns the expected result.
     */
    @DataProvider
    private fun bigDecimalToBooleanConvertValueTesting(): Array<Array<Any>> {
        return arrayOf(
            arrayOf("Tests that the method returns true if the value is not 0", BigDecimal.ONE, true),
            arrayOf("Tests that the method returns false if the value is 0", BigDecimal.ZERO, false)
        )
    }
}
