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

import static java.lang.String.valueOf;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.openMocks;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.mockito.InjectMocks;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.hotels.beans.conversion.AbstractConversionTest;

/**
 * Unit test for {@link StringConversionProcessor}.
 */
public class StringConversionTest extends AbstractConversionTest {
    /**
     * The class to be tested.
     */
    @InjectMocks
    private StringConversionProcessor underTest;

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
        String actual = underTest.convertByte().apply(BYTE_VALUE);

        // THEN
        assertThat(actual).isEqualTo(STRING_VALUE);
    }

    @Test
    public void testConvertByteArrayShouldReturnProperResult() {
        // GIVEN
        String expected = new String(EIGHT_BYTE_BYTE_ARRAY);

        // WHEN
        String actual = underTest.convertByteArray().apply(EIGHT_BYTE_BYTE_ARRAY);

        // THEN
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testConvertShortShouldReturnProperResult() {
        // GIVEN

        // WHEN
        String actual = underTest.convertShort().apply(SHORT_VALUE);

        // THEN
        assertThat(actual).isEqualTo(STRING_VALUE);
    }

    @Test
    public void testConvertIntegerShouldReturnProperResult() {
        // GIVEN

        // WHEN
        String actual = underTest.convertInteger().apply(INTEGER_VALUE);

        // THEN
        assertThat(actual).isEqualTo(STRING_VALUE);
    }

    @Test
    public void testConvertLongShouldReturnProperResult() {
        // GIVEN

        // WHEN
        String actual = underTest.convertLong().apply(LONG_VALUE);

        // THEN
        assertThat(actual).isEqualTo(STRING_VALUE);
    }

    @Test
    public void testConvertFloatShouldReturnProperResult() {
        // GIVEN

        // WHEN
        String actual = underTest.convertFloat().apply(FLOAT_VALUE);

        // THEN
        assertThat(actual).isEqualTo(FLOAT_VALUE.toString());
    }

    @Test
    public void testConvertDoubleShouldReturnProperResult() {
        // GIVEN

        // WHEN
        String actual = underTest.convertDouble().apply(DOUBLE_VALUE);

        // THEN
        assertThat(actual).isEqualTo(DOUBLE_VALUE.toString());
    }

    @Test
    public void testConvertCharacterShouldReturnProperResult() {
        // GIVEN

        // WHEN
        String actual = underTest.convertCharacter().apply(CHAR_VALUE);

        // THEN
        assertThat(actual).isEqualTo(valueOf(CHAR_VALUE));
    }

    @Test
    public void testConvertBooleanShouldReturnProperResult() {
        // GIVEN

        // WHEN
        String actual = underTest.convertBoolean().apply(BOOLEAN_VALUE);

        // THEN
        assertThat(actual).isEqualTo(TRUE_AS_STRING);
    }

    @Test
    public void testConvertStringShouldReturnProperResult() {
        // GIVEN

        // WHEN
        String actual = underTest.convertString().apply(STRING_VALUE);

        // THEN
        assertThat(actual).isEqualTo(STRING_VALUE);
    }

    @Test
    public void testConvertBigIntegerShouldReturnProperResult() {
        // GIVEN
        String expectedValue = BigDecimal.ZERO.toPlainString();

        // WHEN
        String actual = underTest.convertBigInteger().apply(BigInteger.ZERO);

        // THEN
        assertThat(actual).isEqualTo(expectedValue);
    }

    @Test
    public void testConvertBigDecimalShouldReturnProperResult() {
        // GIVEN
        String expectedValue = BigDecimal.ZERO.toPlainString();

        // WHEN
        String actual = underTest.convertBigDecimal().apply(BigDecimal.ZERO);

        // THEN
        assertThat(actual).isEqualTo(expectedValue);
    }
}
