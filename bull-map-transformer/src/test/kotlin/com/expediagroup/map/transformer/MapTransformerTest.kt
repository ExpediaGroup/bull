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
package com.expediagroup.map.transformer

import com.expediagroup.beans.BeanUtils
import com.expediagroup.beans.sample.FromFooSimple
import com.expediagroup.beans.sample.mutable.MutableToFooSimple
import com.expediagroup.beans.transformer.BeanTransformer
import com.expediagroup.map.transformer.model.MapTransformerSettings
import com.expediagroup.transformer.AbstractTransformerTest
import com.expediagroup.transformer.error.InvalidFunctionException
import com.expediagroup.transformer.model.FieldMapping
import com.expediagroup.transformer.model.FieldTransformer
import com.expediagroup.transformer.model.TransformerSettings
import org.assertj.core.api.Assertions
import org.assertj.core.util.Lists
import org.assertj.core.util.Maps
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeClass
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import java.math.BigInteger
import java.util.*

/**
 * Unit test for [MapTransformer].
 */
class MapTransformerTest : AbstractTransformerTest() {
    /**
     * The class to be tested.
     */
    @InjectMocks
    private lateinit var underTest: MapTransformerImpl

    /**
     * Initialized mocks.
     */
    @BeforeClass
    fun beforeClass() {
        MockitoAnnotations.openMocks(this)
        initObjects()
    }

    /**
     * After method actions.
     */
    @AfterMethod
    fun afterMethod() {
        underTest.resetKeyTransformer()
    }

    /**
     * Test that the method `transform` raises an [IllegalArgumentException] if any parameter is null.
     * @param testCaseDescription the test case description
     * @param sourceMap the map to transform
     * @param beanTransformer the bean transformer
     * @param <T> the key type
     * @param <K> the element type
    </K></T> */
    @Test(expectedExceptions = [IllegalArgumentException::class], dataProvider = "dataTransformMethodWithTwoArgument")
    fun <T, K> testTransformRaisesExceptionIfItsCalledWithNullParameter(
        testCaseDescription: String?,
        sourceMap: Map<T, K>?,
        beanTransformer: BeanTransformer?
    ) {
        // GIVEN

        // WHEN
        underTest.transform(sourceMap, beanTransformer)
    }

    /**
     * Created the parameter to test that the method `transform` raises an [IllegalArgumentException] with an invalid parameter.
     * @return parameters to be used for testing.
     */
    @DataProvider
    private fun dataTransformMethodWithTwoArgument(): Array<Array<Any?>> {
        return arrayOf(
            arrayOf(
                "Test that an IllegalArgumentException is thrown if the sourceMap is null",
                null,
                BEAN_TRANSFORMER
            ),
            arrayOf("Test that an IllegalArgumentException is thrown if the transformer is null", SAMPLE_MAP, null)
        )
    }

    /**
     * Test that the given map is correctly transformed.
     * @param testCaseDescription the test case description
     * @param sourceMap the map to transform
     * @param <T> the key type
     * @param <K> the element type
    </K></T> */
    @Test(dataProvider = "dataMapTransformerObject")
    fun <T, K> testTransformWorksProperly(testCaseDescription: String?, sourceMap: Map<T, K>) {
        // GIVEN

        // WHEN
        val actual = underTest.transform(sourceMap)

        // THEN
        Assertions.assertThat(actual).isEqualTo(sourceMap)
    }

    /**
     * Creates the parameters to be used for testing the map transformations.
     * @return parameters to be used for testing the map transformation.
     */
    @DataProvider
    private fun dataMapTransformerObject(): Array<Array<Any>> {
        return arrayOf(
            arrayOf("Test that a simple Map is correctly transformed", SAMPLE_MAP),
            arrayOf("Test that a Map containing a list is correctly transformed", COMPLEX_MAP),
            arrayOf("Test that a Map containing a Map is correctly transformed", VERY_COMPLEX_MAP),
            arrayOf(
                "Test that a Map containing a Map that has an object as key, is correctly transformed",
                EXTREME_COMPLEX_MAP
            )
        )
    }

    /**
     * Test that the given map is correctly transformed if key mappings are defined.
     */
    @Test
    fun testTransformWorksProperlyWithKeyMapping() {
        // GIVEN
        val sourceMap: MutableMap<String, BigInteger> = HashMap()
        sourceMap[MAP_KEY_1] = BigInteger.ZERO
        sourceMap[MAP_KEY_2] = BigInteger.ONE
        underTest.withFieldMapping(FieldMapping(MAP_KEY_1, MAP_KEY_2))

        // WHEN
        val actual = underTest.transform(sourceMap)

        // THEN
        Assertions.assertThat(actual).containsOnly(
            Assertions.entry(MAP_KEY_1, BigInteger.ZERO),
            Assertions.entry(MAP_KEY_2, BigInteger.ZERO)
        )
        underTest.resetFieldsMapping()
    }

    /**
     * Test that the given map is correctly transformed if key transformers are defined.
     */
    @Test
    fun testTransformWorksProperlyWithTransformer() {
        // GIVEN
        val sourceMap: MutableMap<String, BigInteger> = HashMap()
        sourceMap[MAP_KEY_1] = BigInteger.ZERO
        sourceMap[MAP_KEY_2] = BigInteger.ONE
        underTest.withKeyTransformer(FieldTransformer(MAP_KEY_1) { obj: String -> obj.uppercase(Locale.getDefault()) })

        // WHEN
        val actual = underTest.transform(sourceMap)

        // THEN
        Assertions.assertThat(actual)
            .isNotNull
            .hasSize(sourceMap.size)
            .containsKey(MAP_KEY_1.uppercase(Locale.getDefault()))
    }

    /**
     * Test that an [InvalidFunctionException] is raised if the transformer function defined is not valid.
     */
    @Test(expectedExceptions = [InvalidFunctionException::class])
    fun testTransformRaiseAnExceptionIfTheTransformerFunctionIsNotValid() {
        // GIVEN
        val sourceMap: MutableMap<String, BigInteger> = HashMap()
        sourceMap[MAP_KEY_1] = BigInteger.ZERO
        underTest.withKeyTransformer(FieldTransformer(MAP_KEY_1) { `val`: BigInteger? -> BigInteger.ONE.add(`val`) })

        // WHEN
        underTest.transform(sourceMap)
    }

    /**
     * Test that the given map is correctly transformed and the elements are correctly transformed.
     */
    @Test
    fun testTransformWorksProperlyWithTargetKeyAndElemType() {
        // GIVEN

        // WHEN
        val actual = underTest.transform<FromFooSimple, Map<String, String>, MutableToFooSimple, Map<*, *>>(
            EXTREME_COMPLEX_MAP, MutableToFooSimple::class.java, MutableMap::class.java as Class<Map<*, *>>
        )

        // THEN
        Assertions.assertThat(actual)
            .isNotNull
            .hasSize(EXTREME_COMPLEX_MAP.size)
            .allSatisfy { key: MutableToFooSimple, _: Map<*, *>? ->
                Assertions.assertThat(key).isInstanceOf(
                    MutableToFooSimple::class.java
                )
            }
    }

    /**
     * Test that the given map is correctly transformed and the elements are correctly transformed if the Map contains a List.
     */
    @Test
    fun testTransformWorksProperlyWithMapContainingList() {
        // GIVEN
        val sourceMap = Maps.newHashMap(fromFooSimple, Lists.list(ITEM_1))

        // WHEN
        val actual = underTest.transform<FromFooSimple, List<String>, MutableToFooSimple, List<*>>(
            sourceMap,
            MutableToFooSimple::class.java,
            MutableList::class.java as Class<List<*>>
        )

        // THEN
        Assertions.assertThat(actual).allSatisfy { key: MutableToFooSimple, value: List<*>? ->
            Assertions.assertThat(key).isInstanceOf(
                MutableToFooSimple::class.java
            )
            Assertions.assertThat(value).containsOnly(ITEM_1)
        }
    }

    /**
     * Test that is possible to remove all the key transformer defined.
     */
    @Test
    fun testResetKeyTransformerWorksProperly() {
        // GIVEN
        underTest.withKeyTransformer(FieldTransformer(MAP_KEY_1) { obj: String -> obj.uppercase(Locale.getDefault()) })

        // WHEN
        underTest.resetKeyTransformer()
        val transformerSettings = REFLECTION_UTILS.getFieldValue(
            underTest,
            TRANSFORMER_SETTINGS_FIELD_NAME,
            TransformerSettings::class.java
        ) as MapTransformerSettings

        // THEN
        Assertions.assertThat(transformerSettings.keyFieldsTransformers).isEmpty()
        underTest.resetKeyTransformer()
    }

    companion object {
        private val BEAN_TRANSFORMER = BeanUtils().transformer
        private const val MAP_KEY_1 = "key1"
        private const val MAP_KEY_2 = "key2"
    }
}