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

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.MockitoAnnotations.initMocks;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.mockito.InjectMocks;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit test for {@link ByteArrayConversionProcessor}.
 */
public class ByteArrayConversionProcessorTest extends AbstractConversionProcessorTest {
    private static final byte TRUE_AS_BYTE = 1;
    private static final byte FALSE_AS_BYTE = 0;

    /**
     * The class to be tested.
     */
    @InjectMocks
    private ByteArrayConversionProcessor underTest;

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
        byte[] expected = new byte[] {BYTE_VALUE};

        // WHEN
        byte[] actual = underTest.convertByte().apply(BYTE_VALUE);

        // THEN
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testConvertByteArrayShouldReturnProperResult() {
        // GIVEN

        // WHEN
        byte[] actual = underTest.convertByteArray().apply(ONE_BYTE_BYTE_ARRAY);

        // THEN
        assertArrayEquals(ONE_BYTE_BYTE_ARRAY, actual);
    }

    @Test
    public void testConvertShortShouldReturnProperResult() {
        // GIVEN
        byte[] expected = new byte[] {SHORT_VALUE.byteValue()};

        // WHEN
        byte[] actual = underTest.convertShort().apply(SHORT_VALUE);

        // THEN
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testConvertIntegerShouldReturnProperResult() {
        // GIVEN
        byte[] expected = new byte[] {INTEGER_VALUE.byteValue()};

        // WHEN
        byte[] actual = underTest.convertInteger().apply(INTEGER_VALUE);

        // THEN
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testConvertLongShouldReturnProperResult() {
        // GIVEN
        byte[] expected = new byte[] {LONG_VALUE.byteValue()};

        // WHEN
        byte[] actual = underTest.convertLong().apply(LONG_VALUE);

        // THEN
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testConvertFloatShouldReturnProperResult() {
        // GIVEN
        byte[] expected = new byte[] {FLOAT_VALUE.byteValue()};

        // WHEN
        byte[] actual = underTest.convertFloat().apply(FLOAT_VALUE);

        // THEN
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testConvertDoubleShouldReturnProperResult() {
        // GIVEN
        byte[] expected = new byte[] {DOUBLE_VALUE.byteValue()};

        // WHEN
        byte[] actual = underTest.convertDouble().apply(DOUBLE_VALUE);

        // THEN
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testConvertCharacterShouldReturnProperResult() {
        // GIVEN
        byte[] expected = new byte[] {CHAR_VALUE};

        // WHEN
        byte[] actual = underTest.convertCharacter().apply(CHAR_VALUE);

        // THEN
        assertArrayEquals(expected, actual);
    }

    /**
     * Tests that the method {@code convertBoolean} returns the expected byte.
     * @param testCaseDescription the test case description
     * @param valueToConvert the value to be converted
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "booleanToByteConvertValueTesting")
    public void testConvertBooleanShouldReturnProperResult(final String testCaseDescription, final boolean valueToConvert, final byte[] expectedResult) {
        // GIVEN

        // WHEN
        byte[] actual = underTest.convertBoolean().apply(valueToConvert);

        // THEN
        assertArrayEquals(expectedResult, actual);
    }

    /**
     * Creates the parameters to be used for testing that the method {@code convertBoolean} returns the expected result.
     * @return parameters to be used for testing that the method {@code convertBoolean} returns the expected result.
     */
    @DataProvider
    private Object[][] booleanToByteConvertValueTesting() {
        return new Object[][]{
                {"Tests that the method returns {1} if the value is true", BOOLEAN_VALUE, new byte[] {TRUE_AS_BYTE}},
                {"Tests that the method returns {0} if the value is false", Boolean.FALSE, new byte[] {FALSE_AS_BYTE}}
        };
    }

    @Test
    public void testConvertStringShouldReturnProperResult() {
        // GIVEN
        byte[] expected = STRING_VALUE.getBytes();

        // WHEN
        byte[] actual = underTest.convertString().apply(STRING_VALUE);

        // THEN
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testConvertBigIntegerShouldReturnProperResult() {
        // GIVEN
        byte[] expectedValue = new byte[] {BigInteger.ZERO.byteValue()};

        // WHEN
        byte[] actual = underTest.convertBigInteger().apply(BigInteger.ZERO);

        // THEN
        assertArrayEquals(expectedValue, actual);
    }

    @Test
    public void testConvertBigDecimalShouldReturnProperResult() {
        // GIVEN
        byte[] expectedValue = new byte[] {BigDecimal.ZERO.byteValue()};

        // WHEN
        byte[] actual = underTest.convertBigDecimal().apply(BigDecimal.ZERO);

        // THEN
        assertArrayEquals(expectedValue, actual);
    }
}
