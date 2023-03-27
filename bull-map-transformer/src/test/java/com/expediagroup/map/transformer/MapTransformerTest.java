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
package com.expediagroup.map.transformer;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Maps.newHashMap;
import static org.mockito.MockitoAnnotations.openMocks;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mockito.InjectMocks;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.expediagroup.beans.BeanUtils;
import com.expediagroup.beans.sample.FromFooSimple;
import com.expediagroup.beans.sample.mutable.MutableToFooSimple;
import com.expediagroup.beans.transformer.BeanTransformer;
import com.expediagroup.map.transformer.model.MapTransformerSettings;
import com.expediagroup.transformer.AbstractTransformerTest;
import com.expediagroup.transformer.error.InvalidFunctionException;
import com.expediagroup.transformer.model.FieldMapping;
import com.expediagroup.transformer.model.FieldTransformer;
import com.expediagroup.transformer.model.TransformerSettings;

/**
 * Unit test for {@link MapTransformer}.
 */
public class MapTransformerTest extends AbstractTransformerTest {
    private static final BeanTransformer BEAN_TRANSFORMER = new BeanUtils().getTransformer();
    private static final String MAP_KEY_1 = "key1";
    private static final String MAP_KEY_2 = "key2";

    /**
     * The class to be tested.
     */
    @InjectMocks
    private MapTransformerImpl underTest;

    /**
     * Initialized mocks.
     */
    @BeforeClass
    public void beforeClass() {
        openMocks(this);
        initObjects();
    }

    /**
     * After method actions.
     */
    @AfterMethod
    public void afterMethod() {
        underTest.resetKeyTransformer();
    }

    /**
     * Test that the method {@code transform} raises an {@link IllegalArgumentException} if any parameter is null.
     * @param testCaseDescription the test case description
     * @param sourceMap the map to transform
     * @param beanTransformer the bean transformer
     * @param <T> the key type
     * @param <K> the element type
     */
    @Test(expectedExceptions = IllegalArgumentException.class, dataProvider = "dataTransformMethodWithTwoArgument")
    public <T, K> void testTransformRaisesExceptionIfItsCalledWithNullParameter(final String testCaseDescription,
                                                                                final Map<T, K> sourceMap, final BeanTransformer beanTransformer) {
        // GIVEN

        // WHEN
        underTest.transform(sourceMap, beanTransformer);
    }

    /**
     * Created the parameter to test that the method {@code transform} raises an {@link IllegalArgumentException} with an invalid parameter.
     * @return parameters to be used for testing.
     */
    @DataProvider
    private Object[][] dataTransformMethodWithTwoArgument() {
        return new Object[][]{
                {"Test that an IllegalArgumentException is thrown if the sourceMap is null", null, BEAN_TRANSFORMER},
                {"Test that an IllegalArgumentException is thrown if the transformer is null", SAMPLE_MAP, null}
        };
    }

    /**
     * Test that the given map is correctly transformed.
     * @param testCaseDescription the test case description
     * @param sourceMap the map to transform
     * @param <T> the key type
     * @param <K> the element type
     */
    @Test(dataProvider = "dataMapTransformerObject")
    public <T, K> void testTransformWorksProperly(final String testCaseDescription, final Map<T, K> sourceMap) {
        // GIVEN

        // WHEN
        Map<T, K> actual = underTest.transform(sourceMap);

        // THEN
        assertThat(actual).isEqualTo(sourceMap);
    }

    /**
     * Creates the parameters to be used for testing the map transformations.
     * @return parameters to be used for testing the map transformation.
     */
    @DataProvider
    private Object[][] dataMapTransformerObject() {
        return new Object[][]{
                {"Test that a simple Map is correctly transformed", SAMPLE_MAP},
                {"Test that a Map containing a list is correctly transformed", COMPLEX_MAP},
                {"Test that a Map containing a Map is correctly transformed", VERY_COMPLEX_MAP},
                {"Test that a Map containing a Map that has an object as key, is correctly transformed", EXTREME_COMPLEX_MAP}
        };
    }

    /**
     * Test that the given map is correctly transformed if key mappings are defined.
     */
    @Test
    public void testTransformWorksProperlyWithKeyMapping() {
        // GIVEN
        Map<String, BigInteger> sourceMap = new HashMap<>();
        sourceMap.put(MAP_KEY_1, ZERO);
        sourceMap.put(MAP_KEY_2, ONE);
        underTest.withFieldMapping(new FieldMapping<>(MAP_KEY_1, MAP_KEY_2));

        // WHEN
        Map<String, BigInteger> actual = underTest.transform(sourceMap);

        // THEN
        assertThat(actual).containsOnly(
                entry(MAP_KEY_1, ZERO),
                entry(MAP_KEY_2, ZERO)
        );
        underTest.resetFieldsMapping();
    }

    /**
     * Test that the given map is correctly transformed if key transformers are defined.
     */
    @Test
    public void testTransformWorksProperlyWithTransformer() {
        // GIVEN
        Map<String, BigInteger> sourceMap = new HashMap<>();
        sourceMap.put(MAP_KEY_1, ZERO);
        sourceMap.put(MAP_KEY_2, ONE);
        underTest.withKeyTransformer(new FieldTransformer<String, String>(MAP_KEY_1, String::toUpperCase));

        // WHEN
        Map<String, BigInteger> actual = underTest.transform(sourceMap);

        // THEN
        assertThat(actual)
                .isNotNull()
                .hasSize(sourceMap.size())
                .containsKey(MAP_KEY_1.toUpperCase());
    }

    /**
     * Test that an {@link InvalidFunctionException} is raised if the transformer function defined is not valid.
     */
    @Test(expectedExceptions = InvalidFunctionException.class)
    public void testTransformRaiseAnExceptionIfTheTransformerFunctionIsNotValid() {
        // GIVEN
        Map<String, BigInteger> sourceMap = new HashMap<>();
        sourceMap.put(MAP_KEY_1, ZERO);
        underTest.withKeyTransformer(new FieldTransformer<>(MAP_KEY_1, ONE::add));

        // WHEN
        underTest.transform(sourceMap);
    }

    /**
     * Test that the given map is correctly transformed and the elements are correctly transformed.
     */
    @Test
    public void testTransformWorksProperlyWithTargetKeyAndElemType() {
        // GIVEN

        // WHEN
        Map<MutableToFooSimple, Map> actual = underTest.transform(EXTREME_COMPLEX_MAP, MutableToFooSimple.class, Map.class);

        // THEN
        assertThat(actual)
                .isNotNull()
                .hasSize(EXTREME_COMPLEX_MAP.size())
                .allSatisfy((key, value) ->
                        assertThat(key).isInstanceOf(MutableToFooSimple.class));
    }

    /**
     * Test that the given map is correctly transformed and the elements are correctly transformed if the Map contains a List.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testTransformWorksProperlyWithMapContainingList() {
        // GIVEN
        Map<FromFooSimple, List<String>> sourceMap = newHashMap(fromFooSimple, list(ITEM_1));

        // WHEN
        Map<MutableToFooSimple, List> actual = underTest.transform(sourceMap, MutableToFooSimple.class, List.class);

        // THEN
        assertThat(actual).allSatisfy((key, value) -> {
            assertThat(key).isInstanceOf(MutableToFooSimple.class);
            assertThat(value).containsOnly(ITEM_1);
        });
    }

    /**
     * Test that is possible to remove all the key transformer defined.
     */
    @Test
    public void testResetKeyTransformerWorksProperly() {
        // GIVEN
        underTest.withKeyTransformer(new FieldTransformer<String, String>(MAP_KEY_1, String::toUpperCase));

        // WHEN
        underTest.resetKeyTransformer();
        MapTransformerSettings transformerSettings =
                (MapTransformerSettings) REFLECTION_UTILS.getFieldValue(underTest, TRANSFORMER_SETTINGS_FIELD_NAME, TransformerSettings.class);

        // THEN
        assertThat(transformerSettings.getKeyFieldsTransformers()).isEmpty();
        underTest.resetKeyTransformer();
    }
}
