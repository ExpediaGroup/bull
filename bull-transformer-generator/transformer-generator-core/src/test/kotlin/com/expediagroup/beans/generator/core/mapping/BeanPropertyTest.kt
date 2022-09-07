/**
 * Copyright (C) 2019-2021 Expedia, Inc.
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
package com.expediagroup.beans.generator.core.mapping

import org.assertj.core.api.Assertions.assertThat

/**
 * Tests for [BeanProperty].
 */
class BeanPropertyTest {
    /**
     * The class to be tested.
     */
    private var underTest: BeanProperty? = null
    @BeforeClass
    @Throws(NoSuchMethodException::class)
    fun beforeClass() {
        val getAnInt: Method = Source::class.java.getDeclaredMethod("getAnInt")
        underTest = BeanProperty(getAnInt)
    }

    @Test
    @Throws(NoSuchMethodException::class)
    fun shouldBeEqualToPropertyWithSameName() {
        // GIVEN
        val setAnInt: Unit = Source::class.java.getDeclaredMethod("setAnInt", Int::class.javaPrimitiveType)

        // WHEN
        val actual: Boolean = underTest.equals(BeanProperty(setAnInt))

        // THEN
        assertThat(actual).isTrue()
    }

    @Test
    @Throws(NoSuchMethodException::class)
    fun shouldBeEqualToPropertyWithSameNameFromShortPrefixMethod() {
        // GIVEN
        val isABoolean: Unit = Source::class.java.getDeclaredMethod("isABoolean")
        val setABoolean: Unit = Source::class.java.getDeclaredMethod("setABoolean", Boolean::class.javaPrimitiveType)

        // WHEN
        val actual: Boolean = BeanProperty(isABoolean).equals(BeanProperty(setABoolean))

        // THEN
        assertThat(actual).isTrue()
    }

    @Test
    fun shouldBeEqualToSelf() {
        // WHEN
        val actual: Boolean = underTest.equals(underTest)

        // THEN
        assertThat(actual).isTrue()
    }

    @Test
    @Throws(NoSuchMethodException::class)
    fun shouldNotBeEqualToPropertyWithDifferentName() {
        // GIVEN
        val getAString: Unit = Source::class.java.getDeclaredMethod("getAString")

        // WHEN
        val actual: Boolean = underTest.equals(BeanProperty(getAString))

        // THEN
        assertThat(actual).isFalse()
    }

    @Test
    fun shouldNotBeEqualToNull() {
        // WHEN
        val actual: Boolean = underTest.equals(null)

        // THEN
        assertThat(actual).isFalse()
    }

    @Test
    fun shouldNotBeEqualToOtherType() {
        // GIVEN
        val other = Object()

        // WHEN
        val actual: Boolean = underTest.equals(other)

        // THEN
        assertThat(actual).isFalse()
    }

    @Test
    @Throws(NoSuchMethodException::class)
    fun shouldHaveSameHashCodeAsPropertyWithSameName() {
        // GIVEN
        val setAnInt = BeanProperty(Source::class.java.getDeclaredMethod("setAnInt", Int::class.javaPrimitiveType))

        // WHEN
        val actual: Int = underTest.hashCode()

        // THEN
        assertThat(actual).isEqualTo(setAnInt.hashCode())
    }

    @Test
    @Throws(NoSuchMethodException::class)
    fun shouldHaveDifferentHashCodeFromPropertyWithDifferentName() {
        // GIVEN
        val getAString = BeanProperty(Source::class.java.getDeclaredMethod("getAString"))

        // WHEN
        val actual: Int = underTest.hashCode()

        // THEN
        assertThat(actual).isNotEqualTo(getAString.hashCode())
    }

    @Test
    fun shouldReturnPropertyNameAsString() {
        assertThat(underTest.toString()).isEqualTo("anInt")
    }
}