/**
 * Copyright (C) 2019-2023 Expedia, Inc.
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

import static java.nio.ByteBuffer.wrap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.openMocks;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.mockito.InjectMocks;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.expediagroup.beans.conversion.AbstractConversionTest;
import com.expediagroup.beans.conversion.error.TypeConversionException;

/**
 * Unit test for {@link CharacterConversionProcessor}.
 */
public class CharacterConversionTest extends AbstractConversionTest {
    private static final char TRUE_AS_CHAR = 'T';
    private static final char FALSE_AS_CHAR = 'F';

    /**
     * The class to be tested.
     */
    @InjectMocks
    private CharacterConversionProcessor underTest;

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
        char actual = underTest.convertByte().apply(BYTE_VALUE);

        // THEN
        assertThat(actual).isEqualTo((char) BYTE_VALUE.byteValue());
    }

    @Test
    public void testConvertByteArrayShouldReturnProperResult() {
        // GIVEN
        char expected = wrap(EIGHT_BYTE_BYTE_ARRAY).getChar();

        // WHEN
        char actual = underTest.convertByteArray().apply(EIGHT_BYTE_BYTE_ARRAY);

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
        char actual = underTest.convertShort().apply(SHORT_VALUE);

        // THEN
        assertThat(actual).isEqualTo((char) SHORT_VALUE.byteValue());
    }

    @Test
    public void testConvertIntegerShouldReturnProperResult() {
        // GIVEN

        // WHEN
        char actual = underTest.convertInteger().apply(INTEGER_VALUE);

        // THEN
        assertThat(actual).isEqualTo((char) INTEGER_VALUE.intValue());
    }

    @Test
    public void testConvertLongShouldReturnProperResult() {
        // GIVEN

        // WHEN
        char actual = underTest.convertLong().apply(LONG_VALUE);

        // THEN
        assertThat(actual).isEqualTo((char) LONG_VALUE.longValue());
    }

    @Test
    public void testConvertFloatShouldReturnProperResult() {
        // GIVEN

        // WHEN
        char actual = underTest.convertFloat().apply(FLOAT_VALUE);

        // THEN
        assertThat(actual).isEqualTo((char) FLOAT_VALUE.floatValue());
    }

    @Test
    public void testConvertDoubleShouldReturnProperResult() {
        // GIVEN

        // WHEN
        char actual = underTest.convertDouble().apply(DOUBLE_VALUE);

        // THEN
        assertThat(actual).isEqualTo((char) DOUBLE_VALUE.doubleValue());
    }

    @Test
    public void testConvertCharacterShouldReturnProperResult() {
        // GIVEN

        // WHEN
        char actual = underTest.convertCharacter().apply(CHAR_VALUE);

        // THEN
        assertThat(actual).isEqualTo(CHAR_VALUE);
    }

    /**
     * Tests that the method {@code convertBoolean} returns the expected char.
     * @param testCaseDescription the test case description
     * @param valueToConvert the value to be converted
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "booleanToCharConvertValueTesting")
    public void testConvertBooleanShouldReturnProperResult(final String testCaseDescription, final boolean valueToConvert, final char expectedResult) {
        // GIVEN

        // WHEN
        char actual = underTest.convertBoolean().apply(valueToConvert);

        // THEN
        assertThat(actual).isEqualTo(expectedResult);
    }

    /**
     * Creates the parameters to be used for testing that the method {@code convertBoolean} returns the expected result.
     * @return parameters to be used for testing that the method {@code convertBoolean} returns the expected result.
     */
    @DataProvider
    private Object[][] booleanToCharConvertValueTesting() {
        return new Object[][]{
                {"Tests that the method returns T if the value is true", BOOLEAN_VALUE, TRUE_AS_CHAR},
                {"Tests that the method returns F if the value is false", Boolean.FALSE, FALSE_AS_CHAR}
        };
    }

    @Test
    public void testConvertStringShouldReturnProperResult() {
        // GIVEN

        // WHEN
        char actual = underTest.convertString().apply(STRING_VALUE);

        // THEN
        assertThat(actual).isEqualTo(STRING_VALUE.charAt(0));
    }

    @Test
    public void testConvertBigIntegerShouldReturnProperResult() {
        // GIVEN
        char expectedValue = (char) BigInteger.ZERO.intValue();

        // WHEN
        char actual = underTest.convertBigInteger().apply(BigInteger.ZERO);

        // THEN
        assertThat(actual).isEqualTo(expectedValue);
    }

    @Test
    public void testConvertBigDecimalShouldReturnProperResult() {
        // GIVEN
        char expectedValue = (char) BigDecimal.ZERO.doubleValue();

        // WHEN
        char actual = underTest.convertBigDecimal().apply(BigDecimal.ZERO);

        // THEN
        assertThat(actual).isEqualTo(expectedValue);
    }
}
