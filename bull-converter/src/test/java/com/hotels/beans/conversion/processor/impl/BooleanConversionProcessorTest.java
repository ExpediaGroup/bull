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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

import org.mockito.InjectMocks;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for {@link BooleanConversionProcessor}.
 */
public class BooleanConversionProcessorTest extends AbstractConversionProcessorTest {
    /**
     * The class to be tested.
     */
    @InjectMocks
    private BooleanConversionProcessor underTest;

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
        boolean actual = underTest.convertByte().apply(BYTE_VALUE);

        // THEN
        assertTrue(actual);
    }

    @Test
    public void testConvertShortShouldReturnProperResult() {
        // GIVEN

        // WHEN
        boolean actual = underTest.convertShort().apply(SHORT_VALUE);

        // THEN
        assertTrue(actual);
    }

    @Test
    public void testConvertIntegerShouldReturnProperResult() {
        // GIVEN

        // WHEN
        boolean actual = underTest.convertInteger().apply(INTEGER_VALUE);

        // THEN
        assertTrue(actual);
    }

    @Test
    public void testConvertLongShouldReturnProperResult() {
        // GIVEN

        // WHEN
        boolean actual = underTest.convertLong().apply(LONG_VALUE);

        // THEN
        assertTrue(actual);
    }

    @Test
    public void testConvertFloatShouldReturnProperResult() {
        // GIVEN

        // WHEN
        boolean actual = underTest.convertFloat().apply(FLOAT_VALUE);

        // THEN
        assertTrue(actual);
    }

    @Test
    public void testConvertDoubleShouldReturnProperResult() {
        // GIVEN

        // WHEN
        boolean actual = underTest.convertDouble().apply(DOUBLE_VALUE);

        // THEN
        assertTrue(actual);
    }

    @Test
    public void testConvertCharacterShouldReturnProperResult() {
        // GIVEN

        // WHEN
        boolean actual = underTest.convertCharacter().apply(CHAR_VALUE);

        // THEN
        assertFalse(actual);
    }

    @Test
    public void testConvertBooleanShouldReturnProperResult() {
        // GIVEN

        // WHEN
        boolean actual = underTest.convertBoolean().apply(BOOLEAN_VALUE);

        // THEN
        assertEquals(BOOLEAN_VALUE, actual);
    }

    @Test
    public void testConvertStringShouldReturnProperResult() {
        // GIVEN

        // WHEN
        boolean actual = underTest.convertString().apply(TRUE_AS_STRING);

        // THEN
        assertTrue(actual);
    }
}
