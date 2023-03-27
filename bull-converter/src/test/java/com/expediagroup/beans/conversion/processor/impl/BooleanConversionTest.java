/**
 * Copyright (C) 2018-2023 Expedia, Inc.
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
package com.expediagroup.beans.conversion.processor.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.openMocks;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.mockito.InjectMocks;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.expediagroup.beans.conversion.AbstractConversionTest;

/**
 * Unit test for {@link BooleanConversionProcessor}.
 */
public class BooleanConversionTest extends AbstractConversionTest {
    /**
     * The class to be tested.
     */
    @InjectMocks
    private BooleanConversionProcessor underTest;

    /**
     * Initializes mock.
     */
    @BeforeClass
    public void beforeClass() {
        openMocks(this);
    }

    /**
     * Tests that the method {@code convertByte} returns the expected boolean.
     * @param testCaseDescription the test case description
     * @param valueToConvert the value to be converted
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "byteToBooleanConvertValueTesting")
    public void testConvertByteShouldReturnProperResult(final String testCaseDescription, final byte valueToConvert, final boolean expectedResult) {
        // GIVEN

        // WHEN
        boolean actual = underTest.convertByte().apply(valueToConvert);

        // THEN
        assertThat(actual).isEqualTo(expectedResult);
    }

    /**
     * Creates the parameters to be used for testing that the method {@code convertByte} returns the expected result.
     * @return parameters to be used for testing that the method {@code convertByte} returns the expected result.
     */
    @DataProvider
    private Object[][] byteToBooleanConvertValueTesting() {
        return new Object[][]{
                {"Tests that the method returns true if the value is not 0", BYTE_VALUE, true},
                {"Tests that the method returns false if the value is 0", BYTE_VALUE_ZERO, false}
        };
    }

    /**
     * Tests that the method {@code convertByteArray} returns the expected boolean.
     * @param testCaseDescription the test case description
     * @param valueToConvert the value to be converted
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "byteArrayToBooleanConvertValueTesting")
    public void testConvertByteArrayShouldReturnProperResult(final String testCaseDescription, final byte[] valueToConvert, final boolean expectedResult) {
        // GIVEN

        // WHEN
        boolean actual = underTest.convertByteArray().apply(valueToConvert);

        // THEN
        assertThat(actual).isEqualTo(expectedResult);
    }

    /**
     * Creates the parameters to be used for testing that the method {@code convertByte} returns the expected result.
     * @return parameters to be used for testing that the method {@code convertByte} returns the expected result.
     */
    @DataProvider
    private Object[][] byteArrayToBooleanConvertValueTesting() {
        return new Object[][]{
                {"Tests that the method returns true if the value is not 0", new byte[] {BYTE_VALUE}, true},
                {"Tests that the method returns false if the value is 0", new byte[] {BYTE_VALUE_ZERO}, false}
        };
    }

    /**
     * Tests that the method {@code convertShort} returns the expected boolean.
     * @param testCaseDescription the test case description
     * @param valueToConvert the value to be converted
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "shortToBooleanConvertValueTesting")
    public void testConvertShortShouldReturnProperResult(final String testCaseDescription, final short valueToConvert, final boolean expectedResult) {
        // GIVEN

        // WHEN
        boolean actual = underTest.convertShort().apply(valueToConvert);

        // THEN
        assertThat(actual).isEqualTo(expectedResult);
    }

    /**
     * Creates the parameters to be used for testing that the method {@code convertShort} returns the expected result.
     * @return parameters to be used for testing that the method {@code convertShort} returns the expected result.
     */
    @DataProvider
    private Object[][] shortToBooleanConvertValueTesting() {
        return new Object[][]{
                {"Tests that the method returns true if the value is not 0", SHORT_VALUE, true},
                {"Tests that the method returns false if the value is 0", SHORT_VALUE_ZERO, false}
        };
    }

    /**
     * Tests that the method {@code convertInteger} returns the expected boolean.
     * @param testCaseDescription the test case description
     * @param valueToConvert the value to be converted
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "intToBooleanConvertValueTesting")
    public void testConvertIntegerShouldReturnProperResult(final String testCaseDescription, final int valueToConvert, final boolean expectedResult) {
        // GIVEN

        // WHEN
        boolean actual = underTest.convertInteger().apply(valueToConvert);

        // THEN
        assertThat(actual).isEqualTo(expectedResult);
    }

    /**
     * Creates the parameters to be used for testing that the method {@code convertInteger} returns the expected result.
     * @return parameters to be used for testing that the method {@code convertInteger} returns the expected result.
     */
    @DataProvider
    private Object[][] intToBooleanConvertValueTesting() {
        return new Object[][]{
                {"Tests that the method returns true if the value is not 0", INTEGER_VALUE, true},
                {"Tests that the method returns false if the value is 0", INTEGER_VALUE_ZERO, false}
        };
    }

    /**
     * Tests that the method {@code convertLong} returns the expected boolean.
     * @param testCaseDescription the test case description
     * @param valueToConvert the value to be converted
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "longToBooleanConvertValueTesting")
    public void testConvertLongShouldReturnProperResult(final String testCaseDescription, final long valueToConvert, final boolean expectedResult) {
        // GIVEN

        // WHEN
        boolean actual = underTest.convertLong().apply(valueToConvert);

        // THEN
        assertThat(actual).isEqualTo(expectedResult);
    }

    /**
     * Creates the parameters to be used for testing that the method {@code convertLong} returns the expected result.
     * @return parameters to be used for testing that the method {@code convertLong} returns the expected result.
     */
    @DataProvider
    private Object[][] longToBooleanConvertValueTesting() {
        return new Object[][]{
                {"Tests that the method returns true if the value is not 0", LONG_VALUE, true},
                {"Tests that the method returns false if the value is 0", LONG_VALUE_ZERO, false}
        };
    }

    /**
     * Tests that the method {@code convertFloat} returns the expected boolean.
     * @param testCaseDescription the test case description
     * @param valueToConvert the value to be converted
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "floatToBooleanConvertValueTesting")
    public void testConvertFloatShouldReturnProperResult(final String testCaseDescription, final float valueToConvert, final boolean expectedResult) {
        // GIVEN

        // WHEN
        boolean actual = underTest.convertFloat().apply(valueToConvert);

        // THEN
        assertThat(actual).isEqualTo(expectedResult);
    }

    /**
     * Creates the parameters to be used for testing that the method {@code convertFloat} returns the expected result.
     * @return parameters to be used for testing that the method {@code convertFloat} returns the expected result.
     */
    @DataProvider
    private Object[][] floatToBooleanConvertValueTesting() {
        return new Object[][]{
                {"Tests that the method returns true if the value is not 0", FLOAT_VALUE, true},
                {"Tests that the method returns false if the value is 0", FLOAT_VALUE_ZERO, false}
        };
    }

    /**
     * Tests that the method {@code convertDouble} returns the expected boolean.
     * @param testCaseDescription the test case description
     * @param valueToConvert the value to be converted
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "floatToBooleanConvertValueTesting")
    public void testConvertDoubleShouldReturnProperResult(final String testCaseDescription, final double valueToConvert, final boolean expectedResult) {
        // GIVEN

        // WHEN
        boolean actual = underTest.convertDouble().apply(valueToConvert);

        // THEN
        assertThat(actual).isEqualTo(expectedResult);
    }

    /**
     * Creates the parameters to be used for testing that the method {@code convertDouble} returns the expected result.
     * @return parameters to be used for testing that the method {@code convertDouble} returns the expected result.
     */
    @DataProvider
    private Object[][] doubleToBooleanConvertValueTesting() {
        return new Object[][]{
                {"Tests that the method returns true if the value is not 0", DOUBLE_VALUE, true},
                {"Tests that the method returns false if the value is 0", DOUBLE_VALUE_ZERO, false}
        };
    }

    @Test
    public void testConvertCharacterShouldReturnProperResult() {
        // GIVEN

        // WHEN
        boolean actual = underTest.convertCharacter().apply(CHAR_VALUE);

        // THEN
        assertThat(actual).isFalse();
    }

    @Test
    public void testConvertBooleanShouldReturnProperResult() {
        // GIVEN

        // WHEN
        boolean actual = underTest.convertBoolean().apply(BOOLEAN_VALUE);

        // THEN
        assertThat(actual).isEqualTo(BOOLEAN_VALUE);
    }

    @Test
    public void testConvertStringShouldReturnProperResult() {
        // GIVEN

        // WHEN
        boolean actual = underTest.convertString().apply(TRUE_AS_STRING);

        // THEN
        assertThat(actual).isTrue();
    }

    /**
     * Tests that the method {@code convertBigInteger} returns the expected boolean.
     * @param testCaseDescription the test case description
     * @param valueToConvert the value to be converted
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "bigIntegerToBooleanConvertValueTesting")
    public void testConvertBigIntegerShouldReturnProperResult(final String testCaseDescription, final BigInteger valueToConvert, final boolean expectedResult) {
        // GIVEN

        // WHEN
        boolean actual = underTest.convertBigInteger().apply(valueToConvert);

        // THEN
        assertThat(actual).isEqualTo(expectedResult);
    }

    /**
     * Creates the parameters to be used for testing that the method {@code convertFloat} returns the expected result.
     * @return parameters to be used for testing that the method {@code convertFloat} returns the expected result.
     */
    @DataProvider
    private Object[][] bigIntegerToBooleanConvertValueTesting() {
        return new Object[][]{
                {"Tests that the method returns true if the value is not 0", BigInteger.ONE, true},
                {"Tests that the method returns false if the value is 0", BigInteger.ZERO, false}
        };
    }

    /**
     * Tests that the method {@code convertBigDecimal} returns the expected boolean.
     * @param testCaseDescription the test case description
     * @param valueToConvert the value to be converted
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "bigDecimalToBooleanConvertValueTesting")
    public void testConvertBigIntegerShouldReturnProperResult(final String testCaseDescription, final BigDecimal valueToConvert, final boolean expectedResult) {
        // GIVEN

        // WHEN
        boolean actual = underTest.convertBigDecimal().apply(valueToConvert);

        // THEN
        assertThat(actual).isEqualTo(expectedResult);
    }

    /**
     * Creates the parameters to be used for testing that the method {@code convertFloat} returns the expected result.
     * @return parameters to be used for testing that the method {@code convertFloat} returns the expected result.
     */
    @DataProvider
    private Object[][] bigDecimalToBooleanConvertValueTesting() {
        return new Object[][]{
                {"Tests that the method returns true if the value is not 0", BigDecimal.ONE, true},
                {"Tests that the method returns false if the value is 0", BigDecimal.ZERO, false}
        };
    }
}
