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
public class FloatConversionProcessorTest {
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
        Byte expected = 10;

        // WHEN
        Float actual = underTest.convertByte().apply(expected);

        // THEN
        assertEquals((Float) expected.floatValue(), actual);
    }

    @Test
    public void testConvertShortShouldReturnProperResult() {
        // GIVEN
        Short expected = 10;

        // WHEN
        Float actual = underTest.convertShort().apply(expected);

        // THEN
        assertEquals((Float) expected.floatValue(), actual);
    }

    @Test
    public void testConvertIntegerShouldReturnProperResult() {
        // GIVEN
        Integer expected = 10;

        // WHEN
        Float actual = underTest.convertInteger().apply(expected);

        // THEN
        assertEquals((Float) expected.floatValue(), actual);
    }

    @Test
    public void testConvertLongShouldReturnProperResult() {
        // GIVEN
        Long expected = 10L;

        // WHEN
        Float actual = underTest.convertLong().apply(expected);

        // THEN
        assertEquals((Float) expected.floatValue(), actual);
    }

    @Test
    public void testConvertFloatShouldReturnProperResult() {
        // GIVEN
        Float expected = 10f;

        // WHEN
        Float actual = underTest.convertFloat().apply(expected);

        // THEN
        assertEquals(expected, actual);
    }

    @Test
    public void testConvertDoubleShouldReturnProperResult() {
        // GIVEN
        Double expected = 10.51;

        // WHEN
        Float actual = underTest.convertDouble().apply(expected);

        // THEN
        assertEquals((Float) expected.floatValue(), actual);
    }

    @Test
    public void testConvertCharacterShouldReturnProperResult() {
        // GIVEN
        char expected = 'c';

        // WHEN
        Float actual = underTest.convertCharacter().apply(expected);

        // THEN
        assertEquals(Float.valueOf(getNumericValue(expected)), actual);
    }

    @Test
    public void testConvertBooleanShouldReturnProperResult() {
        // GIVEN
        Boolean expected = true;

        // WHEN
        Float actual = underTest.convertBoolean().apply(expected);

        // THEN
        assertEquals(expected, actual.equals(1f));
    }

    @Test
    public void testConvertStringShouldReturnProperResult() {
        // GIVEN
        String expected = "140";

        // WHEN
        Float actual = underTest.convertString().apply(expected);

        // THEN
        assertEquals(valueOf(expected), actual);
    }
}

