/**
 * Copyright (C) 2019-2020 Expedia, Inc.
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

package com.hotels.beans.generator.core.mapping;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Method;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.hotels.beans.generator.core.sample.javabean.Source;

/**
 * Tests for {@link BeanProperty}.
 */
public class BeanPropertyTest {
    private BeanProperty underTest;

    @BeforeClass
    public void beforeClass() throws NoSuchMethodException {
        final Method getAnInt = Source.class.getDeclaredMethod("getAnInt");
        underTest = new BeanProperty(getAnInt);
    }

    @Test
    public void shouldBeEqualToPropertyWithSameName() throws NoSuchMethodException {
        // GIVEN
        Method setAnInt = Source.class.getDeclaredMethod("setAnInt", int.class);

        // WHEN
        boolean result = underTest.equals(new BeanProperty(setAnInt));

        // THEN
        assertTrue(result);
    }

    @Test
    public void shouldBeEqualToPropertyWithSameNameFromShortPrefixMethod() throws NoSuchMethodException {
        // GIVEN
        Method isABoolean = Source.class.getDeclaredMethod("isABoolean");
        Method setABoolean = Source.class.getDeclaredMethod("setABoolean", boolean.class);

        // WHEN
        boolean result = new BeanProperty(isABoolean).equals(new BeanProperty(setABoolean));

        // THEN
        assertTrue(result);
    }

    @Test
    public void shouldBeEqualToSelf() {
        // WHEN
        boolean result = underTest.equals(underTest);

        // THEN
        assertTrue(result);
    }

    @Test
    public void shouldNotBeEqualToPropertyWithDifferentName() throws NoSuchMethodException {
        // GIVEN
        Method getAString = Source.class.getDeclaredMethod("getAString");

        // WHEN
        boolean result = underTest.equals(new BeanProperty(getAString));

        // THEN
        assertFalse(result);
    }

    @Test
    public void shouldNotBeEqualToNull() {
        // WHEN
        boolean result = underTest.equals(null);

        // THEN
        assertFalse(result);
    }

    @Test
    public void shouldNotBeEqualToOtherType() {
        // GIVEN
        Object other = new Object();

        // WHEN
        boolean result = underTest.equals(other);

        // THEN
        assertFalse(result);
    }

    @Test
    public void shouldHaveSameHashCodeAsPropertyWithSameName() throws NoSuchMethodException {
        // GIVEN
        BeanProperty setAnInt = new BeanProperty(Source.class.getDeclaredMethod("setAnInt", int.class));

        // WHEN
        int hashCode = underTest.hashCode();

        // THEN
        assertEquals(hashCode, setAnInt.hashCode());
    }

    @Test
    public void shouldHaveDifferentHashCodeFromPropertyWithDifferentName() throws NoSuchMethodException {
        // GIVEN
        BeanProperty getAString = new BeanProperty(Source.class.getDeclaredMethod("getAString"));

        // WHEN
        int hashCode = underTest.hashCode();

        // THEN
        assertNotEquals(hashCode, getAString.hashCode());
    }

    @Test
    public void shouldReturnPropertyNameAsString() {
        assertEquals("anInt", underTest.toString());
    }
}
