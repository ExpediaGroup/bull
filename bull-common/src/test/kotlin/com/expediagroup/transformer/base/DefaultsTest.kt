/**
 * Copyright (C) 2019-2022 Expedia, Inc.
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
package com.expediagroup.transformer.base

import com.expediagroup.beans.sample.FromFoo
import org.assertj.core.api.Assertions
import org.mockito.InjectMocks
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

/**
 * Unit test for [Defaults].
 */
class DefaultsTest {
    /**
     * The class to be tested.
     */
    @InjectMocks
    private val underTest: Defaults? = null

    /**
     * Tests that the method: `defaultValue` returns the expected result for the given type.
     * @param type the class type.
     * @param expectedResult the expected result.
     */
    @Test(dataProvider = "dataProvider")
    fun testDefaultValueShouldReturnTheExpectedResult(type: Class<*>?, expectedResult: Any?) {
        // GIVEN

        // WHEN
        val actual = Defaults.defaultValue(type)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expectedResult)
    }

    /**
     * Data provider for the required test cases.
     * @return the test cases.
     */
    @DataProvider
    private fun dataProvider(): Array<Array<Any?>> {
        return arrayOf(
            arrayOf(Boolean::class.javaPrimitiveType, Boolean.FALSE),
            arrayOf(
                Boolean::class.java,
                Boolean.FALSE
            ),
            arrayOf(Char::class.java, '\u0000'),
            arrayOf(
                Char::class.javaPrimitiveType,
                '\u0000'
            ),
            arrayOf(Byte::class.java, 0),
            arrayOf(
                Byte::class.javaPrimitiveType,
                0
            ),
            arrayOf(Int::class.java, 0), arrayOf(Int::class.javaPrimitiveType, 0),
            arrayOf(
                Short::class.java,
                0
            ),
            arrayOf(Short::class.javaPrimitiveType, 0), arrayOf(Long::class.java, 0L),
            arrayOf(
                Long::class.javaPrimitiveType,
                0L
            ),
            arrayOf(Float::class.java, 0.0f),
            arrayOf(
                Float::class.javaPrimitiveType,
                0.0f
            ),
            arrayOf(Double::class.java, 0.0),
            arrayOf(
                Double::class.javaPrimitiveType,
                0.0
            ),
            arrayOf(FromFoo::class.java, null)
        )
    }
}
