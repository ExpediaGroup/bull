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
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Method;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.hotels.beans.generator.core.sample.javabean.Source;

/**
 * Tests for {@link BeanProperty}.
 */
public class BeanPropertyTest {
    private BeanProperty anInt;

    @BeforeClass
    public void beforeClass() throws NoSuchMethodException {
        final Method getAnInt = Source.class.getDeclaredMethod("getAnInt");
        anInt = new BeanProperty(getAnInt);
    }

    @Test
    public void shouldBeEqualToPropertyWithSameName() throws NoSuchMethodException {
        // GIVEN
        var setAnInt = Source.class.getDeclaredMethod("setAnInt", int.class);

        // WHEN
        boolean result = anInt.equals(new BeanProperty(setAnInt));

        // THEN
        assertTrue(result);
    }

    @Test
    public void shouldBeEqualToPropertyWithSameNameFromShortPrefixMethod() throws NoSuchMethodException {
        // GIVEN
        var isABoolean = Source.class.getDeclaredMethod("isABoolean");
        var setABoolean = Source.class.getDeclaredMethod("setABoolean", boolean.class);

        // WHEN
        boolean result = new BeanProperty(isABoolean).equals(new BeanProperty(setABoolean));

        // THEN
        assertTrue(result);
    }

    @Test
    public void shouldBeEqualToSelf() {
        // WHEN
        boolean result = anInt.equals(anInt);

        // THEN
        assertTrue(result);
    }

    @Test
    public void shouldNotBeEqualToPropertyWithDifferentName() throws NoSuchMethodException {
        // GIVEN
        var getAString = Source.class.getDeclaredMethod("getAString");

        // WHEN
        boolean result = anInt.equals(new BeanProperty(getAString));

        // THEN
        assertFalse(result);
    }

    @Test
    public void shouldReturnPropertyNameAsString() {
        assertEquals("anInt", anInt.toString());
    }
}
