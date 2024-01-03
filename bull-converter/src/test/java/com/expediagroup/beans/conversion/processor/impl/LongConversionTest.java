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
 * Unit test for {@link LongConversionProcessor}.
 */
public class LongConversionTest extends AbstractConversionTest {
    private static final long TRUE_AS_LONG = 1L;
    private static final long FALSE_AS_LONG = 0L;

    /**
     * The class to be tested.
     */
    @InjectMocks
    private LongConversionProcessor underTest;

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
        Long actual = underTest.convertByte().apply(BYTE_VALUE);

        // THEN
        assertThat(actual).isEqualTo(LONG_VALUE);
    }

    @Test
    public void testConvertByteArrayShouldReturnProperResult() {
        // GIVEN
        Long expected = wrap(EIGHT_BYTE_BYTE_ARRAY).getLong();

        // WHEN
        Long actual = underTest.convertByteArray().apply(EIGHT_BYTE_BYTE_ARRAY);

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
        Long actual = underTest.convertShort().apply(SHORT_VALUE);

        // THEN
        assertThat(actual).isEqualTo(LONG_VALUE);
    }

    @Test
    public void testConvertIntegerShouldReturnProperResult() {
        // GIVEN

        // WHEN
        Long actual = underTest.convertInteger().apply(INTEGER_VALUE);

        // THEN
        assertThat(actual).isEqualTo(LONG_VALUE);
    }

    @Test
    public void testConvertLongShouldReturnProperResult() {
        // GIVEN

        // WHEN
        Long actual = underTest.convertLong().apply(LONG_VALUE);

        // THEN
        assertThat(actual).isEqualTo(LONG_VALUE);
    }

    @Test
    public void testConvertFloatShouldReturnProperResult() {
        // GIVEN

        // WHEN
        Long actual = underTest.convertFloat().apply(FLOAT_VALUE);

        // THEN
        assertThat(actual).isEqualTo(LONG_VALUE);
    }

    @Test
    public void testConvertDoubleShouldReturnProperResult() {
        // GIVEN

        // WHEN
        Long actual = underTest.convertDouble().apply(DOUBLE_VALUE);

        // THEN
        assertThat(actual).isEqualTo(LONG_VALUE);
    }

    @Test
    public void testConvertCharacterShouldReturnProperResult() {
        // GIVEN

        // WHEN
        Long actual = underTest.convertCharacter().apply(CHAR_VALUE);

        // THEN
        assertThat(actual).isOne();
    }

    /**
     * Tests that the method {@code convertBoolean} returns the expected long.
     * @param testCaseDescription the test case description
     * @param valueToConvert the value to be converted
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "booleanToLongConvertValueTesting")
    public void testConvertBooleanShouldReturnProperResult(final String testCaseDescription, final boolean valueToConvert, final long expectedResult) {
        // GIVEN

        // WHEN
        long actual = underTest.convertBoolean().apply(valueToConvert);

        // THEN
        assertThat(actual).isEqualTo(expectedResult);
    }

    /**
     * Creates the parameters to be used for testing that the method {@code convertBoolean} returns the expected result.
     * @return parameters to be used for testing that the method {@code convertBoolean} returns the expected result.
     */
    @DataProvider
    private Object[][] booleanToLongConvertValueTesting() {
        return new Object[][]{
                {"Tests that the method returns 1 if the value is true", BOOLEAN_VALUE, TRUE_AS_LONG},
                {"Tests that the method returns 0 if the value is false", Boolean.FALSE, FALSE_AS_LONG}
        };
    }

    @Test
    public void testConvertStringShouldReturnProperResult() {
        // GIVEN

        // WHEN
        Long actual = underTest.convertString().apply(STRING_VALUE);

        // THEN
        assertThat(actual).isEqualTo(LONG_VALUE);
    }

    @Test
    public void testConvertBigIntegerShouldReturnProperResult() {
        // GIVEN

        // WHEN
        long actual = underTest.convertBigInteger().apply(BigInteger.ZERO);

        // THEN
        assertThat(actual).isZero();
    }

    @Test
    public void testConvertBigDecimalShouldReturnProperResult() {
        // GIVEN

        // WHEN
        long actual = underTest.convertBigDecimal().apply(BigDecimal.ZERO);

        // THEN
        assertThat(actual).isZero();
    }
}
