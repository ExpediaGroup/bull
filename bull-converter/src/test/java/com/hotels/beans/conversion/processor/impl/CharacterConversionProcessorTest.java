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

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

import org.mockito.InjectMocks;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for {@link CharacterConversionProcessor}.
 */
public class CharacterConversionProcessorTest  extends AbstractConversionProcessorTest {
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
        initMocks(this);
    }

    @Test
    public void testConvertByteShouldReturnProperResult() {
        // GIVEN

        // WHEN
        char actual = underTest.convertByte().apply(BYTE_VALUE);

        // THEN
        assertEquals((char) BYTE_VALUE.byteValue(), actual);
    }

    @Test
    public void testConvertShortShouldReturnProperResult() {
        // GIVEN

        // WHEN
        char actual = underTest.convertShort().apply(SHORT_VALUE);

        // THEN
        assertEquals((char) SHORT_VALUE.byteValue(), actual);
    }

    @Test
    public void testConvertIntegerShouldReturnProperResult() {
        // GIVEN

        // WHEN
        char actual = underTest.convertInteger().apply(INTEGER_VALUE);

        // THEN
        assertEquals((char) INTEGER_VALUE.intValue(), actual);
    }

    @Test
    public void testConvertLongShouldReturnProperResult() {
        // GIVEN

        // WHEN
        char actual = underTest.convertLong().apply(LONG_VALUE);

        // THEN
        assertEquals((char) LONG_VALUE.longValue(), actual);
    }

    @Test
    public void testConvertFloatShouldReturnProperResult() {
        // GIVEN

        // WHEN
        char actual = underTest.convertFloat().apply(FLOAT_VALUE);

        // THEN
        assertEquals((char) FLOAT_VALUE.floatValue(), actual);
    }

    @Test
    public void testConvertDoubleShouldReturnProperResult() {
        // GIVEN

        // WHEN
        char actual = underTest.convertDouble().apply(DOUBLE_VALUE);

        // THEN
        assertEquals((char) DOUBLE_VALUE.doubleValue(), actual);
    }

    @Test
    public void testConvertCharacterShouldReturnProperResult() {
        // GIVEN

        // WHEN
        char actual = underTest.convertCharacter().apply(CHAR_VALUE);

        // THEN
        assertEquals(CHAR_VALUE, actual);
    }

    @Test
    public void testConvertBooleanShouldReturnProperResult() {
        // GIVEN

        // WHEN
        char actual = underTest.convertBoolean().apply(BOOLEAN_VALUE);

        // THEN
        assertEquals(TRUE_AS_CHAR, actual);
    }

    @Test
    public void testConvertStringShouldReturnProperResult() {
        // GIVEN

        // WHEN
        char actual = underTest.convertString().apply(STRING_VALUE);

        // THEN
        assertEquals(STRING_VALUE.charAt(0), actual);
    }
}
