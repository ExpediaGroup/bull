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

package com.hotels.beans.conversion.processor.impl;

import static java.lang.Character.getNumericValue;
import static java.lang.Integer.valueOf;
import static java.nio.ByteBuffer.wrap;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.mockito.InjectMocks;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.hotels.beans.conversion.AbstractConversionTest;
import com.hotels.beans.conversion.error.TypeConversionException;

/**
 * Unit test for {@link IntegerConversionProcessor}.
 */
public class IntegerConversionTest extends AbstractConversionTest {
    private static final int TRUE_AS_INT = 1;
    private static final int FALSE_AS_INT = 0;

    /**
     * The class to be tested.
     */
    @InjectMocks
    private IntegerConversionProcessor underTest;

    /**
     * Initializes mock.
     */
    @BeforeClass
    public void beforeClass() {
        initMocks(this);
    }

    @Test
    public void testConvertByteShouldReturnProperResult() {
        // GIVEN

        // WHEN
        Integer actual = underTest.convertByte().apply(BYTE_VALUE);

        // THEN
        assertEquals((Integer) BYTE_VALUE.intValue(), actual);
    }

    @Test
    public void testConvertByteArrayShouldReturnProperResult() {
        // GIVEN
        Integer expected = wrap(EIGHT_BYTE_BYTE_ARRAY).getInt();

        // WHEN
        Integer actual = underTest.convertByteArray().apply(EIGHT_BYTE_BYTE_ARRAY);

        // THEN
        assertEquals(expected, actual);
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
        Integer actual = underTest.convertShort().apply(SHORT_VALUE);

        // THEN
        assertEquals((Integer) SHORT_VALUE.intValue(), actual);
    }

    @Test
    public void testConvertIntegerShouldReturnProperResult() {
        // GIVEN

        // WHEN
        Integer actual = underTest.convertInteger().apply(INTEGER_VALUE);

        // THEN
        assertEquals(INTEGER_VALUE, actual);
    }

    @Test
    public void testConvertLongShouldReturnProperResult() {
        // GIVEN

        // WHEN
        Integer actual = underTest.convertLong().apply(LONG_VALUE);

        // THEN
        assertEquals((Integer) LONG_VALUE.intValue(), actual);
    }

    @Test
    public void testConvertFloatShouldReturnProperResult() {
        // GIVEN

        // WHEN
        Integer actual = underTest.convertFloat().apply(FLOAT_VALUE);

        // THEN
        assertEquals((Integer) FLOAT_VALUE.intValue(), actual);
    }

    @Test
    public void testConvertDoubleShouldReturnProperResult() {
        // GIVEN

        // WHEN
        Integer actual = underTest.convertDouble().apply(DOUBLE_VALUE);

        // THEN
        assertEquals((Integer) DOUBLE_VALUE.intValue(), actual);
    }

    @Test
    public void testConvertCharacterShouldReturnProperResult() {
        // GIVEN

        // WHEN
        Integer actual = underTest.convertCharacter().apply(CHAR_VALUE);

        // THEN
        assertEquals((Integer) getNumericValue(CHAR_VALUE), actual);
    }

    /**
     * Tests that the method {@code convertBoolean} returns the expected int.
     * @param testCaseDescription the test case description
     * @param valueToConvert the value to be converted
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "booleanToIntConvertValueTesting")
    public void testConvertBooleanShouldReturnProperResult(final String testCaseDescription, final boolean valueToConvert, final int expectedResult) {
        // GIVEN

        // WHEN
        int actual = underTest.convertBoolean().apply(valueToConvert);

        // THEN
        assertEquals(expectedResult, actual);
    }

    /**
     * Creates the parameters to be used for testing that the method {@code convertBoolean} returns the expected result.
     * @return parameters to be used for testing that the method {@code convertBoolean} returns the expected result.
     */
    @DataProvider
    private Object[][] booleanToIntConvertValueTesting() {
        return new Object[][]{
                {"Tests that the method returns 1 if the value is true", BOOLEAN_VALUE, TRUE_AS_INT},
                {"Tests that the method returns 0 if the value is false", Boolean.FALSE, FALSE_AS_INT}
        };
    }

    @Test
    public void testConvertStringShouldReturnProperResult() {
        // GIVEN

        // WHEN
        Integer actual = underTest.convertString().apply(STRING_VALUE);

        // THEN
        assertEquals(valueOf(STRING_VALUE), actual);
    }

    @Test
    public void testConvertBigIntegerShouldReturnProperResult() {
        // GIVEN
        int expectedValue = BigInteger.ZERO.intValue();

        // WHEN
        int actual = underTest.convertBigInteger().apply(BigInteger.ZERO);

        // THEN
        assertEquals(expectedValue, actual);
    }

    @Test
    public void testConvertBigDecimalShouldReturnProperResult() {
        // GIVEN
        int expectedValue = BigDecimal.ZERO.intValue();

        // WHEN
        int actual = underTest.convertBigDecimal().apply(BigDecimal.ZERO);

        // THEN
        assertEquals(expectedValue, actual);
    }
}
