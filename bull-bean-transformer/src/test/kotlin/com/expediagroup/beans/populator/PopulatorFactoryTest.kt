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

import com.expediagroup.beans.sample.FromFoo
import com.expediagroup.beans.transformer.BeanTransformer
import org.assertj.core.api.Assertions
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.testng.annotations.BeforeClass
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import java.util.*

/**
 * Unit test for class: [PopulatorFactory].
 */
class PopulatorFactoryTest {
    /**
     * The class to be tested.
     */
    @InjectMocks
    private val underTest: PopulatorFactory? = null

    /**
     * Transformer object.
     */
    @Mock
    private val transformer: BeanTransformer? = null

    /**
     * Initializes mock.
     */
    @BeforeClass
    fun beforeClass() {
        MockitoAnnotations.openMocks(this)
    }

    /**
     * Data provider for the required test cases.
     * @return the test cases.
     */
    @DataProvider
    fun dataProvider(): Array<Array<Any?>> {
        return arrayOf(
            arrayOf(Array<String>::class.java, ArrayPopulator::class.java),
            arrayOf(
                MutableList::class.java,
                CollectionPopulator::class.java
            ),
            arrayOf(MutableMap::class.java, MapPopulator::class.java),
            arrayOf(
                FromFoo::class.java,
                null
            ),
            arrayOf(Optional::class.java, OptionalPopulator::class.java)
        )
    }

    /**
     * Tests that the method: `defaultValue` returns the expected result for the given type.
     * @param type the type for which a populator needs to be found
     * @param expectedResult the expected populator
     */
    @Test(dataProvider = "dataProvider")
    fun testGetPopulatorReturnsTheExpectedResult(type: Class<*>?, expectedResult: Class<out Populator<*>?>?) {
        // GIVEN

        // WHEN
        val populator = PopulatorFactory.getPopulator<Any, Any>(type, type, transformer)

        // THEN
        val isNonNullObjectExpected = Objects.nonNull(expectedResult)
        Assertions.assertThat(populator.isPresent).isEqualTo(isNonNullObjectExpected)
        if (isNonNullObjectExpected) {
            Assertions.assertThat(populator).containsInstanceOf(expectedResult)
        }
    }
}
