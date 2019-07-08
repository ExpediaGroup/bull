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
import static java.lang.Float.valueOf;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

import org.mockito.InjectMocks;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for {@link FloatConversionProcessor}.
 */
public class FloatConversionProcessorTest extends AbstractConversionProcessorTest {
    /**
     * The class to be tested.
     */
    @InjectMocks
    private FloatConversionProcessor underTest;

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
        Float actual = underTest.convertByte().apply(BYTE_VALUE);

        // THEN
        assertEquals((Float) BYTE_VALUE.floatValue(), actual);
    }

    @Test
    public void testConvertShortShouldReturnProperResult() {
        // GIVEN

        // WHEN
        Float actual = underTest.convertShort().apply(SHORT_VALUE);

        // THEN
        assertEquals((Float) SHORT_VALUE.floatValue(), actual);
    }

    @Test
    public void testConvertIntegerShouldReturnProperResult() {
        // GIVEN

        // WHEN
        Float actual = underTest.convertInteger().apply(INTEGER_VALUE);

        // THEN
        assertEquals((Float) INTEGER_VALUE.floatValue(), actual);
    }

    @Test
    public void testConvertLongShouldReturnProperResult() {
        // GIVEN

        // WHEN
        Float actual = underTest.convertLong().apply(LONG_VALUE);

        // THEN
        assertEquals((Float) LONG_VALUE.floatValue(), actual);
    }

    @Test
    public void testConvertFloatShouldReturnProperResult() {
        // GIVEN

        // WHEN
        Float actual = underTest.convertFloat().apply(FLOAT_VALUE);

        // THEN
        assertEquals(FLOAT_VALUE, actual);
    }

    @Test
    public void testConvertDoubleShouldReturnProperResult() {
        // GIVEN

        // WHEN
        Float actual = underTest.convertDouble().apply(DOUBLE_VALUE);

        // THEN
        assertEquals((Float) DOUBLE_VALUE.floatValue(), actual);
    }

    @Test
    public void testConvertCharacterShouldReturnProperResult() {
        // GIVEN

        // WHEN
        Float actual = underTest.convertCharacter().apply(CHAR_VALUE);

        // THEN
        assertEquals(Float.valueOf(getNumericValue(CHAR_VALUE)), actual);
    }

    @Test
    public void testConvertBooleanShouldReturnProperResult() {
        // GIVEN

        // WHEN
        Float actual = underTest.convertBoolean().apply(BOOLEAN_VALUE);

        // THEN
        assertEquals(BOOLEAN_VALUE, actual.equals(1f));
    }

    @Test
    public void testConvertStringShouldReturnProperResult() {
        // GIVEN

        // WHEN
        Float actual = underTest.convertString().apply(STRING_VALUE);

        // THEN
        assertEquals(valueOf(STRING_VALUE), actual);
    }
}

