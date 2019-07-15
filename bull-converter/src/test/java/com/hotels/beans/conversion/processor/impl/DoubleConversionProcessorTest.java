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

import static java.lang.Character.getNumericValue;
import static java.lang.Double.valueOf;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

import org.mockito.InjectMocks;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit test for {@link DoubleConversionProcessor}.
 */
public class DoubleConversionProcessorTest extends AbstractConversionProcessorTest {
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
        initMocks(this);
    }

    @Test
    public void testConvertByteShouldReturnProperResult() {
        // GIVEN

        // WHEN
        Double actual = underTest.convertByte().apply(BYTE_VALUE);

        // THEN
        assertEquals((Double) BYTE_VALUE.doubleValue(), actual);
    }

    @Test
    public void testConvertShortShouldReturnProperResult() {
        // GIVEN

        // WHEN
        Double actual = underTest.convertShort().apply(SHORT_VALUE);

        // THEN
        assertEquals(DOUBLE_VALUE, actual);
    }

    @Test
    public void testConvertIntegerShouldReturnProperResult() {
        // GIVEN

        // WHEN
        Double actual = underTest.convertInteger().apply(INTEGER_VALUE);

        // THEN
        assertEquals((Double) INTEGER_VALUE.doubleValue(), actual);
    }

    @Test
    public void testConvertLongShouldReturnProperResult() {
        // GIVEN

        // WHEN
        Double actual = underTest.convertLong().apply(LONG_VALUE);

        // THEN
        assertEquals((Double) LONG_VALUE.doubleValue(), actual);
    }

    @Test
    public void testConvertFloatShouldReturnProperResult() {
        // GIVEN

        // WHEN
        Double actual = underTest.convertFloat().apply(FLOAT_VALUE);

        // THEN
        assertEquals((Double) FLOAT_VALUE.doubleValue(), actual);
    }

    @Test
    public void testConvertDoubleShouldReturnProperResult() {
        // GIVEN

        // WHEN
        Double actual = underTest.convertDouble().apply(DOUBLE_VALUE);

        // THEN
        assertEquals(DOUBLE_VALUE, actual);
    }

    @Test
    public void testConvertCharacterShouldReturnProperResult() {
        // GIVEN

        // WHEN
        Double actual = underTest.convertCharacter().apply(CHAR_VALUE);

        // THEN
        assertEquals(valueOf((short) getNumericValue(CHAR_VALUE)), actual);
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
        assertEquals(expectedResult, actual, 0.0);
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
        assertEquals(valueOf(STRING_VALUE), actual);
    }
}

