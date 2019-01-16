/**
 * Copyright (C) 2019 Expedia Inc.
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

package com.hotels.beans.base;

import static java.lang.Boolean.FALSE;
import static java.util.Arrays.asList;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.InjectMocks;

import com.hotels.beans.sample.FromFoo;

/**
 * Unit test for {@link Defaults}.
 */
@RunWith(value = Parameterized.class)
public class DefaultsTest {
    /**
     * The class type.
     */
    private final Class<?> type;

    /**
     * The expected result.
     */
    private final Object expectedResult;

    /**
     * The class to be tested.
     */
    @InjectMocks
    private Defaults underTest;

    /**
     * All args constructor.
     * @param type the class type.
     * @param expectedResult the expected result.
     */
    public DefaultsTest(final Class<?> type, final Object expectedResult) {
        this.type = type;
        this.expectedResult = expectedResult;
    }

    /**
     * Tests that the method: {@code defaultValue} throws exception when type is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDefaultValueThrowsExceptionWhenTypeIsNull() {
        underTest.defaultValue(null);
    }

    /**
     * Tests that the method: {@code defaultValue} returns the expected result for the given type.
     */
    @Test
    public void testDefaultValueShouldReturnTheExpectedResult() {
        // GIVEN

        // WHEN
        final Object actual = underTest.defaultValue(type);

        // THEN
        assertEquals(expectedResult, actual);
    }

    /**
     * Data provider for the required test cases.
     * @return the test cases.
     */
    @Parameterized.Parameters(name = "{index}. Type: {0}.")
    public static Collection<Object[]> dataProvider() {
      return asList(new Object[][]{
                {boolean.class, FALSE},
                {Boolean.class, FALSE},
                {Character.class, '\u0000'},
                {char.class, '\u0000'},
                {Byte.class, 0},
                {byte.class, 0},
                {Integer.class, 0},
                {int.class, 0},
                {Short.class, 0},
                {short.class, 0},
                {Long.class, 0L},
                {long.class, 0L},
                {Float.class, 0.0F},
                {float.class, 0.0F},
                {Double.class, 0.0D},
                {double.class, 0.0D},
                {FromFoo.class, null}
        });
    }
}
