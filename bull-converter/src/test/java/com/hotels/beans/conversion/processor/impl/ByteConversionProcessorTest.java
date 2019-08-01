/**
 * Copyright (C) 2019 Expedia, Inc.
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

import static java.lang.Byte.valueOf;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.mockito.InjectMocks;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit test for {@link ByteConversionProcessor}.
 */
public class ByteConversionProcessorTest extends AbstractConversionProcessorTest {
    private static final byte TRUE_AS_BYTE = 1;
    private static final byte FALSE_AS_BYTE = 0;

    /**
     * The class to be tested.
     */
    @InjectMocks
    private ByteConversionProcessor underTest;

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
        Byte actual = underTest.convertByte().apply(BYTE_VALUE);

        // THEN
        assertEquals(BYTE_VALUE, actual);
    }

    @Test
    public void testConvertByteArrayShouldReturnProperResult() {
        // GIVEN

        // WHEN
        byte actual = underTest.convertByteArray().apply(ONE_BYTE_BYTE_ARRAY);

        // THEN
        assertEquals(ONE_BYTE_BYTE_ARRAY[0], actual);
    }

    @Test
    public void testConvertShortShouldReturnProperResult() {
        // GIVEN

        // WHEN
        byte actual = underTest.convertShort().apply(SHORT_VALUE);

        // THEN
        assertEquals(SHORT_VALUE.byteValue(), actual);
    }

    @Test
    public void testConvertIntegerShouldReturnProperResult() {
        // GIVEN

        // WHEN
        byte actual = underTest.convertInteger().apply(INTEGER_VALUE);

        // THEN
        assertEquals(INTEGER_VALUE.byteValue(), actual);
    }

    @Test
    public void testConvertLongShouldReturnProperResult() {
        // GIVEN

        // WHEN
        byte actual = underTest.convertLong().apply(LONG_VALUE);

        // THEN
        assertEquals(LONG_VALUE.byteValue(), actual);
    }

    @Test
    public void testConvertFloatShouldReturnProperResult() {
        // GIVEN

        // WHEN
        byte actual = underTest.convertFloat().apply(FLOAT_VALUE);

        // THEN
        assertEquals(FLOAT_VALUE.byteValue(), actual);
    }

    @Test
    public void testConvertDoubleShouldReturnProperResult() {
        // GIVEN

        // WHEN
        byte actual = underTest.convertDouble().apply(DOUBLE_VALUE);

        // THEN
        assertEquals(DOUBLE_VALUE.byteValue(), actual);
    }

    @Test
    public void testConvertCharacterShouldReturnProperResult() {
        // GIVEN

        // WHEN
        byte actual = underTest.convertCharacter().apply(CHAR_VALUE);

        // THEN
        assertEquals((byte) CHAR_VALUE, actual);
    }

    /**
     * Tests that the method {@code convertBoolean} returns the expected byte.
     * @param testCaseDescription the test case description
     * @param valueToConvert the value to be converted
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "booleanToByteConvertValueTesting")
    public void testConvertBooleanShouldReturnProperResult(final String testCaseDescription, final boolean valueToConvert, final byte expectedResult) {
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
    private Object[][] booleanToByteConvertValueTesting() {
        return new Object[][]{
                {"Tests that the method returns 1 if the value is true", BOOLEAN_VALUE, TRUE_AS_BYTE},
                {"Tests that the method returns 0 if the value is false", Boolean.FALSE, FALSE_AS_BYTE}
        };
    }

    @Test
    public void testConvertStringShouldReturnProperResult() {
        // GIVEN

        // WHEN
        Byte actual = underTest.convertString().apply(STRING_VALUE);

        // THEN
        assertEquals(valueOf(STRING_VALUE), actual);
    }

    @Test
    public void testConvertBigIntegerShouldReturnProperResult() {
        // GIVEN
        byte expectedValue = BigInteger.ZERO.byteValue();

        // WHEN
        byte actual = underTest.convertBigInteger().apply(BigInteger.ZERO);

        // THEN
        assertEquals(expectedValue, actual);
    }

    @Test
    public void testConvertBigDecimalShouldReturnProperResult() {
        // GIVEN
        byte expectedValue = BigDecimal.ZERO.byteValue();

        // WHEN
        byte actual = underTest.convertBigDecimal().apply(BigDecimal.ZERO);

        // THEN
        assertEquals(expectedValue, actual);
    }
}
