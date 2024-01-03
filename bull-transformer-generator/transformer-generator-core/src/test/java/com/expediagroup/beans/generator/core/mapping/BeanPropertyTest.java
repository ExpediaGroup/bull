/**
 * Copyright (C) 2018-2023 Expedia, Inc.
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
package com.expediagroup.beans.generator.core.mapping;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.expediagroup.beans.generator.core.sample.javabean.Source;

/**
 * Tests for {@link BeanProperty}.
 */
public class BeanPropertyTest {
    /**
     * The class to be tested.
     */
    private BeanProperty underTest;

    @BeforeClass
    public void beforeClass() throws NoSuchMethodException {
        final Method getAnInt = Source.class.getDeclaredMethod("getAnInt");
        underTest = new BeanProperty(getAnInt);
    }

    @Test
    public void shouldBeEqualToPropertyWithSameName() throws NoSuchMethodException {
        // GIVEN
        var setAnInt = Source.class.getDeclaredMethod("setAnInt", int.class);

        // WHEN
        boolean actual = underTest.equals(new BeanProperty(setAnInt));

        // THEN
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldBeEqualToPropertyWithSameNameFromShortPrefixMethod() throws NoSuchMethodException {
        // GIVEN
        var isABoolean = Source.class.getDeclaredMethod("isABoolean");
        var setABoolean = Source.class.getDeclaredMethod("setABoolean", boolean.class);

        // WHEN
        boolean actual = new BeanProperty(isABoolean).equals(new BeanProperty(setABoolean));

        // THEN
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldBeEqualToSelf() {
        // WHEN
        boolean actual = underTest.equals(underTest);

        // THEN
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldNotBeEqualToPropertyWithDifferentName() throws NoSuchMethodException {
        // GIVEN
        var getAString = Source.class.getDeclaredMethod("getAString");

        // WHEN
        boolean actual = underTest.equals(new BeanProperty(getAString));

        // THEN
        assertThat(actual).isFalse();
    }

    @Test
    public void shouldNotBeEqualToNull() {
        // WHEN
        boolean actual = underTest.equals(null);

        // THEN
        assertThat(actual).isFalse();
    }

    @Test
    public void shouldNotBeEqualToOtherType() {
        // GIVEN
        Object other = new Object();

        // WHEN
        boolean actual = underTest.equals(other);

        // THEN
        assertThat(actual).isFalse();
    }

    @Test
    public void shouldHaveSameHashCodeAsPropertyWithSameName() throws NoSuchMethodException {
        // GIVEN
        var setAnInt = new BeanProperty(Source.class.getDeclaredMethod("setAnInt", int.class));

        // WHEN
        int actual = underTest.hashCode();

        // THEN
        assertThat(actual).isEqualTo(setAnInt.hashCode());
    }

    @Test
    public void shouldHaveDifferentHashCodeFromPropertyWithDifferentName() throws NoSuchMethodException {
        // GIVEN
        var getAString = new BeanProperty(Source.class.getDeclaredMethod("getAString"));

        // WHEN
        int actual = underTest.hashCode();

        // THEN
        assertThat(actual).isNotEqualTo(getAString.hashCode());
    }

    @Test
    public void shouldReturnPropertyNameAsString() {
        assertThat(underTest.toString()).isEqualTo("anInt");
    }
}
