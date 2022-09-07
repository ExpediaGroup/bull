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
 * Tests for [MappingCodeFactory].
 */
class MappingCodeFactoryTest {
    /**
     * The class to be tested.
     */
    private val underTest: MappingCodeFactory = MappingCodeFactory.newInstance()
    @Test
    fun shouldReturnAnInstanceForMutableDestination() {
        // WHEN
        val mappingCode: Unit = underTest.of(Source::class.java, Destination::class.java)

        // THEN
        assertThat(mappingCode).isNotNull()
    }

    /**
     * TODO: remove this test after implementation of immutable mapping code.
     */
    @Test(expectedExceptions = NotImplementedException::class)
    fun shouldFailForImmutableDestination() {
        underTest.of(Source::class.java, ImmutableDestination::class.java)
    }

    /**
     * TODO: remove this test after implementation of mixed mapping code.
     */
    @Test(expectedExceptions = NotImplementedException::class)
    fun shouldFailForMixedDestination() {
        underTest.of(Source::class.java, MixedDestination::class.java)
    }

    @Test(expectedExceptions = AssertionError::class, expectedExceptionsMessageRegExp = ".*UNSUPPORTED.*")
    fun shouldFailForUnsupportedDestinationType() {
        // GIVEN
        val classUtils: ClassUtils = given(mock(ClassUtils::class.java).getClassType(any()))
            .willReturn(UNSUPPORTED)
            .getMock()
        val underTest: MappingCodeFactory = MappingCodeFactory.newInstance(classUtils)

        // WHEN
        underTest.of(Source::class.java, Destination::class.java)
    }

    @Test(dataProvider = "typePairs", expectedExceptions = IllegalArgumentException::class)
    fun shouldFailIfAnyTypeIsNull(source: Class<*>?, destination: Class<*>?) {
        underTest.of(source, destination)
    }

    @DataProvider
    private fun typePairs(): Array<Array<Object?>> {
        return arrayOf(arrayOf(null, Object::class.java), arrayOf(Object::class.java, null), arrayOf(null, null))
    }
}