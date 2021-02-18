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

package com.hotels.beans.conversion.processor.impl;

import static java.lang.Double.valueOf;
import static java.nio.ByteBuffer.wrap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.openMocks;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.mockito.InjectMocks;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.hotels.beans.conversion.AbstractConversionTest;
import com.hotels.beans.conversion.error.TypeConversionException;

/**
 * Unit test for {@link DoubleConversionProcessor}.
 */
public class DoubleConversionTest extends AbstractConversionTest {
    private static final double TRUE_AS_DOUBLE = 1d;
    private static final double FALSE_AS_DOUBLE = 0d;

    /**
     * The class to be tested.
     */
    @InjectMocks
    private DoubleConversionProcessor underTest;

    /**
     * Initializes mock.
     */
    @BeforeClass
    public void beforeClass() {
        openMocks(this);
    }

    @Test
    public void testConvertByteShouldReturnProperResult() {
        // GIVEN

        // WHEN
        Double actual = underTest.convertByte().apply(BYTE_VALUE);

        // THEN
        assertThat(actual).isEqualTo(DOUBLE_VALUE);
    }

    @Test
    public void testConvertByteArrayShouldReturnProperResult() {
        // GIVEN
        Double expected = wrap(EIGHT_BYTE_BYTE_ARRAY).getDouble();

        // WHEN
        Double actual = underTest.convertByteArray().apply(EIGHT_BYTE_BYTE_ARRAY);

        // THEN
        assertThat(actual).isEqualTo(expected);
    }

    @Test(expectedExceptions = TypeConversionException.class)
    public void testConvertByteArrayShouldThrowExceptionIfByteArrayIsTooSmall() {
        // GIVEN

        // WHEN
        underTest.convertByteArray().apply(ONE_BYTE_BYTE_ARRAY);
    }

    @Test
    public void testConvertShortShouldReturnProperResult() {
        // GIVEN

        // WHEN
        Double actual = underTest.convertShort().apply(SHORT_VALUE);

        // THEN
        assertThat(actual).isEqualTo(DOUBLE_VALUE);
    }

    @Test
    public void testConvertIntegerShouldReturnProperResult() {
        // GIVEN

        // WHEN
        Double actual = underTest.convertInteger().apply(INTEGER_VALUE);

        // THEN
        assertThat(actual).isEqualTo(DOUBLE_VALUE);
    }

    @Test
    public void testConvertLongShouldReturnProperResult() {
        // GIVEN

        // WHEN
        Double actual = underTest.convertLong().apply(LONG_VALUE);

        // THEN
        assertThat(actual).isEqualTo(DOUBLE_VALUE);
    }

    @Test
    public void testConvertFloatShouldReturnProperResult() {
        // GIVEN

        // WHEN
        Double actual = underTest.convertFloat().apply(FLOAT_VALUE);

        // THEN
        assertThat(actual).isEqualTo(DOUBLE_VALUE);
    }

    @Test
    public void testConvertDoubleShouldReturnProperResult() {
        // GIVEN

        // WHEN
        Double actual = underTest.convertDouble().apply(DOUBLE_VALUE);

        // THEN
        assertThat(actual).isEqualTo(DOUBLE_VALUE);
    }

    @Test
    public void testConvertCharacterShouldReturnProperResult() {
        // GIVEN
        double expected = 1.0;

        // WHEN
        Double actual = underTest.convertCharacter().apply(CHAR_VALUE);

        // THEN
        assertThat(actual).isEqualTo(expected);
    }

    /**
     * Tests that the method {@code convertBoolean} returns the expected double.
     * @param testCaseDescription the test case description
     * @param valueToConvert the value to be converted
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "booleanToDoubleConvertValueTesting")
    public void testConvertBooleanShouldReturnProperResult(final String testCaseDescription, final boolean valueToConvert, final double expectedResult) {
        // GIVEN

        // WHEN
        Double actual = underTest.convertBoolean().apply(valueToConvert);

        // THEN
        assertThat(actual).isEqualTo(expectedResult);
    }

    /**
     * Creates the parameters to be used for testing that the method {@code convertBoolean} returns the expected result.
     * @return parameters to be used for testing that the method {@code convertBoolean} returns the expected result.
     */
    @DataProvider
    private Object[][] booleanToDoubleConvertValueTesting() {
        return new Object[][]{
                {"Tests that the method returns 1 if the value is true", BOOLEAN_VALUE, TRUE_AS_DOUBLE},
                {"Tests that the method returns 0 if the value is false", Boolean.FALSE, FALSE_AS_DOUBLE}
        };
    }

    @Test
    public void testConvertStringShouldReturnProperResult() {
        // GIVEN

        // WHEN
        Double actual = underTest.convertString().apply(STRING_VALUE);

        // THEN
        assertThat(actual).isEqualTo(valueOf(STRING_VALUE));
    }

    @Test
    public void testConvertBigIntegerShouldReturnProperResult() {
        // GIVEN

        // WHEN
        double actual = underTest.convertBigInteger().apply(BigInteger.ZERO);

        // THEN
        assertThat(actual).isZero();
    }

    @Test
    public void testConvertBigDecimalShouldReturnProperResult() {
        // GIVEN

        // WHEN
        double actual = underTest.convertBigDecimal().apply(BigDecimal.ZERO);

        // THEN
        assertThat(actual).isZero();
    }
}
