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

import static java.lang.String.valueOf;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

import org.mockito.InjectMocks;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for {@link StringConversionProcessor}.
 */
public class StringConversionProcessorTest extends AbstractConversionProcessorTest {
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
        initMocks(this);
    }

    @Test
    public void testConvertByteShouldReturnProperResult() {
        // GIVEN

        // WHEN
        String actual = underTest.convertByte().apply(BYTE_VALUE);

        // THEN
        assertEquals(BYTE_VALUE.toString(), actual);
    }

    @Test
    public void testConvertShortShouldReturnProperResult() {
        // GIVEN

        // WHEN
        String actual = underTest.convertShort().apply(SHORT_VALUE);

        // THEN
        assertEquals(SHORT_VALUE.toString(), actual);
    }

    @Test
    public void testConvertIntegerShouldReturnProperResult() {
        // GIVEN

        // WHEN
        String actual = underTest.convertInteger().apply(INTEGER_VALUE);

        // THEN
        assertEquals(INTEGER_VALUE.toString(), actual);
    }

    @Test
    public void testConvertLongShouldReturnProperResult() {
        // GIVEN

        // WHEN
        String actual = underTest.convertLong().apply(LONG_VALUE);

        // THEN
        assertEquals(LONG_VALUE.toString(), actual);
    }

    @Test
    public void testConvertFloatShouldReturnProperResult() {
        // GIVEN

        // WHEN
        String actual = underTest.convertFloat().apply(FLOAT_VALUE);

        // THEN
        assertEquals(FLOAT_VALUE.toString(), actual);
    }

    @Test
    public void testConvertDoubleShouldReturnProperResult() {
        // GIVEN

        // WHEN
        String actual = underTest.convertDouble().apply(DOUBLE_VALUE);

        // THEN
        assertEquals(DOUBLE_VALUE.toString(), actual);
    }

    @Test
    public void testConvertCharacterShouldReturnProperResult() {
        // GIVEN

        // WHEN
        String actual = underTest.convertCharacter().apply(CHAR_VALUE);

        // THEN
        assertEquals(valueOf(CHAR_VALUE), actual);
    }

    @Test
    public void testConvertBooleanShouldReturnProperResult() {
        // GIVEN

        // WHEN
        String actual = underTest.convertBoolean().apply(BOOLEAN_VALUE);

        // THEN
        assertEquals(BOOLEAN_VALUE.toString(), actual);
    }

    @Test
    public void testConvertStringShouldReturnProperResult() {
        // GIVEN

        // WHEN
        String actual = underTest.convertString().apply(STRING_VALUE);

        // THEN
        assertEquals(STRING_VALUE, actual);
    }
}
