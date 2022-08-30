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
package com.expediagroup.beans.populator

import com.expediagroup.beans.sample.mixed.MixedToFooStaticField
import com.expediagroup.beans.transformer.BeanTransformer
import org.assertj.core.api.Assertions
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.testng.annotations.BeforeClass
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

/**
 * Unit test for class: [ArrayPopulator].
 */
class ArrayPopulatorTest {
    @Mock
    private val transformer: BeanTransformer? = null

    /**
     * The class to be tested.
     */
    @InjectMocks
    private val underTest: ArrayPopulator? = null

    /**
     * Initializes mock.
     */
    @BeforeClass
    fun beforeClass() {
        MockitoAnnotations.openMocks(this)
    }

    /**
     * Tests that the method `getPopulatedObject` works as expected.
     * @param genericFieldType the field to be populated class
     * @param array the source object from which extract the values
     */
    @Test(dataProvider = "dataProvider")
    fun testGetPopulatedObjectWorksProperly(genericFieldType: Class<*>, array: Any?) {
        // GIVEN
        Mockito.`when`(
            transformer!!.transform(
                ArgumentMatchers.any<Any>(),
                ArgumentMatchers.eq(
                    MixedToFooStaticField::class.java
                )
            )
        ).thenReturn(MIXED_TO_FOO_STATIC_FIELDS_OBJECTS)

        // WHEN
        val actual = underTest!!.getPopulatedObject(null, genericFieldType, array, null)

        // THEN
        when (genericFieldType) {
            Char::class.java -> {
                Assertions.assertThat(actual as CharArray).isEqualTo(array)
            }
            Int::class.java -> {
                Assertions.assertThat(actual as IntArray).isEqualTo(array)
            }
            Any::class.java -> {
                Assertions.assertThat(actual as Array<Any>).isEqualTo(array)
            }
            MixedToFooStaticField::class.java -> {
                Assertions.assertThat(actual as Array<Any>)
                    .usingRecursiveFieldByFieldElementComparatorOnFields(NORMAL_FIELD)
                    .isEqualTo(array)
            }
            else -> {
                Assertions.assertThat(actual).isEqualTo(array)
            }
        }
    }

    /**
     * Data provider for the required test cases.
     * @return the test cases.
     */
    @DataProvider
    fun dataProvider(): Array<Array<Any>> {
        return arrayOf(
            arrayOf(String::class.java, STRING_ARRAY),
            arrayOf(
                Char::class.java,
                CHAR_ARRAY
            ),
            arrayOf(Int::class.java, INT_ARRAY),
            arrayOf(
                MixedToFooStaticField::class.java,
                createMixedToFooArray()
            ),
            arrayOf(
                Any::class.java,
                createBooleanArray()
            )
        )
    }

    companion object {
        private const val VAL_1 = "val1"
        private const val VAL_2 = "val2"
        private val STRING_ARRAY = arrayOf(VAL_1, VAL_2)
        private const val CHAR = '\u0000'
        private val CHAR_ARRAY = charArrayOf(CHAR)
        private const val ZERO = 0
        private val INT_ARRAY = intArrayOf(ZERO)
        private val MIXED_TO_FOO_STATIC_FIELDS_OBJECTS = MixedToFooStaticField()
        private const val NORMAL_FIELD = "normalField"

        /**
         * Creates an array containing an instance of [MixedToFooStaticField].
         * @return an array containing an instance of [MixedToFooStaticField].
         */
        private fun createMixedToFooArray(): Array<MixedToFooStaticField> {
            MIXED_TO_FOO_STATIC_FIELDS_OBJECTS.normalField = VAL_1
            return arrayOf(MIXED_TO_FOO_STATIC_FIELDS_OBJECTS)
        }

        /**
         * Creates an array containing an instance of [Boolean].
         * @return an array containing an instance of [Boolean].
         */
        private fun createBooleanArray(): Array<Boolean> {
            return arrayOf(Boolean.TRUE, Boolean.FALSE)
        }
    }
}
