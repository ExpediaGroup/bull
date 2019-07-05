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
import static java.lang.Integer.valueOf;
import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.function.Function;

import org.mockito.InjectMocks;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for {@link IntegerConversionProcessor}.
 */
public class IntegerConversionProcessorTest {
    /**
     * The class to be tested.
     */
    @InjectMocks
    private IntegerConversionProcessor underTest;

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
        Byte actual = 10;

        // WHEN
        Function<Byte, Integer> function = underTest.convertByte();

        // THEN
        Integer expected = function.apply(actual);
        assertEquals(expected, (Integer) actual.intValue());
    }

    @Test
    public void testConvertShortShouldReturnProperResult() {
        // GIVEN
        Short actual = 10;

        // WHEN
        Function<Short, Integer> function = underTest.convertShort();

        // THEN
        Integer expected = function.apply(actual);
        assertEquals(expected, (Integer) actual.intValue());
    }

    @Test
    public void testConvertIntegerShouldReturnProperResult() {
        // GIVEN
        Integer actual = 10;

        // WHEN
        Function<Integer, Integer> function = underTest.convertInteger();

        // THEN
        Integer expected = function.apply(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testConvertLongShouldReturnProperResult() {
        // GIVEN
        Long actual = 10L;

        // WHEN
        Function<Long, Integer> function = underTest.convertLong();

        // THEN
        Integer expected = function.apply(actual);
        assertEquals(expected, (Integer) actual.intValue());
    }

    @Test
    public void testConvertFloatShouldReturnProperResult() {
        // GIVEN
        Float actual = 10f;

        // WHEN
        Function<Float, Integer> function = underTest.convertFloat();

        // THEN
        Integer expected = function.apply(actual);
        assertEquals(expected, (Integer) actual.intValue());
    }

    @Test
    public void testConvertDoubleShouldReturnProperResult() {
        // GIVEN
        Double actual = 10.51;

        // WHEN
        Function<Double, Integer> function = underTest.convertDouble();

        // THEN
        Integer expected = function.apply(actual);
        assertEquals(expected, (Integer) actual.intValue());
    }

    @Test
    public void testConvertCharacterShouldReturnProperResult() {
        // GIVEN
        Character expected = 'c';

        // WHEN
        Integer actual = underTest.convertCharacter().apply(expected);

        // THEN
        assertEquals((Integer) getNumericValue(expected), actual);
    }

    @Test
    public void testConvertBooleanShouldReturnProperResult() {
        // GIVEN
        Boolean expected = true;

        // WHEN
        Integer actual = underTest.convertBoolean().apply(expected);

        // THEN
        assertEquals(expected, actual.equals(1));
    }

    @Test
    public void testConvertStringShouldReturnProperResult() {
        // GIVEN
        String expected = "140";

        // WHEN
        Integer actual = underTest.convertString().apply(expected);

        // THEN
        assertEquals(valueOf(expected), actual);
    }
}

