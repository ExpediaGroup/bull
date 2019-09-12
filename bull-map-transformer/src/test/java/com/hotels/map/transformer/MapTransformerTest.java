/**
 * Copyright (C) 2019 Expedia, Inc.
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

package com.hotels.map.transformer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

import java.util.HashMap;
import java.util.Map;

import org.mockito.InjectMocks;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.hotels.beans.BeanUtils;
import com.hotels.beans.sample.FromFoo;
import com.hotels.beans.sample.FromFooSimple;
import com.hotels.beans.sample.immutable.ImmutableToFoo;
import com.hotels.beans.sample.mutable.MutableToFooSimple;
import com.hotels.beans.transformer.AbstractTransformerTest;
import com.hotels.beans.transformer.BeanTransformer;

/**
 * Unit test for {@link MapTransformer}.
 */
public class MapTransformerTest extends AbstractTransformerTest {
    private static final Map<FromFooSimple, FromFoo> COMPLEX_OBJECT_MAP = new HashMap<>();
    private static final BeanTransformer BEAN_TRANSFORMER = new BeanUtils().getTransformer();

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
        initMocks(this);
        initObjects();
    }

    /**
     * Test that the method {@code transform} raises an {@link IllegalArgumentException} if any parameter is null.
     * @param testCaseDescription the test case description
     * @param sourceMap the map to transform
     * @param beanTransformer the bean transformer
     */
    @Test(expectedExceptions = IllegalArgumentException.class, dataProvider = "dataTransformMethodWithTwoArgument")
    public <T, K> void testTransformRaisesExceptionIfItsCalledWithNullParameter(final String testCaseDescription, final Map<T, K> sourceMap, final BeanTransformer beanTransformer) {
        //GIVEN

        //WHEN
        underTest.transform(sourceMap, beanTransformer);
    }

    /**
     * Created the parameter to test that the method {@code transform} raises an {@link IllegalArgumentException} with an invalid parameter.
     * @return parameters to be used for testing.
     */
    @DataProvider
    private Object[][] dataTransformMethodWithTwoArgument() {
        return new Object[][] {
                {"Test that an IllegalArgumentException is thrown if the sourceMap is null", null, BEAN_TRANSFORMER},
                {"Test that an IllegalArgumentException is thrown if the transformer is null", SAMPLE_MAP, null}
        };
    }

    /**
     * Test that the method {@code transform} raises an {@link IllegalArgumentException} with an invalid parameter.
     * @param testCaseDescription the test case description
     * @param sourceMap the map to transform
     * @param targetMap the target map
     * @param beanTransformer the bean transformer
     * @param <T> the key type
     * @param <K> the element type
     */
    @Test(expectedExceptions = IllegalArgumentException.class, dataProvider = "dataTransformMethodWithThreeArgument")
    public <T, K> void testTransformRaisesExceptionIfItsCalledWithAnyNullParameter(final String testCaseDescription, final Map<T, K> sourceMap,
                                                                                   final Map<T, K> targetMap, final BeanTransformer beanTransformer) {
        //GIVEN

        //WHEN
        underTest.transform(sourceMap, targetMap, beanTransformer);
    }

    /**
     * Created the parameter to test that the method {@code transform} raises an {@link IllegalArgumentException} with an invalid parameter.
     * @return parameters to be used for testing.
     */
    @DataProvider
    private Object[][] dataTransformMethodWithThreeArgument() {
        return new Object[][] {
                {"Test that an IllegalArgumentException is thrown if the sourceMap is null", null, SAMPLE_MAP, BEAN_TRANSFORMER},
                {"Test that an IllegalArgumentException is thrown if the targetMap is null", SAMPLE_MAP, null, BEAN_TRANSFORMER},
                {"Test that an IllegalArgumentException is thrown if the bean transformer is null", SAMPLE_MAP, SAMPLE_MAP, null}
        };
    }

    /**
     * Test that the method {@code transform} raises an {@link IllegalArgumentException} if any parameter is null.
     * @param testCaseDescription the test case description
     * @param sourceMap the map to transform
     * @param targetKeyClass the Map key class type in the target Map
     * @param targetElemClass the Map element class type in the target Map
     * @param beanTransformer the bean transformer
     */
    @Test(expectedExceptions = IllegalArgumentException.class, dataProvider = "dataTransformMethodWithFourArgument")
    public <T, K> void testTransformRaisesExceptionIfItsCalledWithNullParameter(final String testCaseDescription, final Map<T, K> sourceMap,
        final Class<T> targetKeyClass, final Class<K> targetElemClass, final BeanTransformer beanTransformer) {
        //GIVEN

        //WHEN
        underTest.transform(sourceMap, targetKeyClass, targetElemClass, beanTransformer);
    }

    /**
     * Created the parameter to test that the method {@code transform} raises an {@link IllegalArgumentException} with an invalid parameter.
     * @return parameters to be used for testing.
     */
    @DataProvider
    private Object[][] dataTransformMethodWithFourArgument() {
        return new Object[][] {
                {"Test that an IllegalArgumentException is thrown if the sourceMap is null", null, String.class, String.class, BEAN_TRANSFORMER},
                {"Test that an IllegalArgumentException is thrown if the targetKeyClass is null", SAMPLE_MAP, null, String.class, BEAN_TRANSFORMER},
                {"Test that an IllegalArgumentException is thrown if the targetElemClass is null", SAMPLE_MAP, String.class, null, BEAN_TRANSFORMER},
                {"Test that an IllegalArgumentException is thrown if the bean transformer is null", SAMPLE_MAP, String.class, String.class, null}
        };
    }

    /**
     * Test that the method {@code transform} raises an {@link IllegalArgumentException} with an invalid parameter.
     * @param testCaseDescription the test case description
     * @param sourceMap the map to transform
     * @param sourceMap the map to transform
     * @param <T> the key type
     * @param <K> the element type
     */
    @Test(expectedExceptions = IllegalArgumentException.class, dataProvider = "dataTransformWithTargetClassesAndInvalidParameter")
    public <T, K> void testTransformRaisesExceptionIfItsCalledWithAnyNullParameter(final String testCaseDescription, final Map<T, K> sourceMap, final Class<T> targetKeyClass, final Class<K> targetElemClass) {
        //GIVEN

        //WHEN
        underTest.transform(sourceMap, targetKeyClass, targetElemClass);
    }

    /**
     * Created the parameter to test that the method {@code transform} raises an {@link IllegalArgumentException} with an invalid parameter.
     * @return parameters to be used for testing.
     */
    @DataProvider
    private Object[][] dataTransformWithTargetClassesAndInvalidParameter() {
        return new Object[][] {
                {"Test that an IllegalArgumentException is thrown if the sourceMap is null", null, String.class, String.class},
                {"Test that an IllegalArgumentException is thrown if the targetKeyClass is null", SAMPLE_MAP, null, String.class},
                {"Test that an IllegalArgumentException is thrown if the targetElemClass is null", SAMPLE_MAP, String.class, null},
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
        //GIVEN

        //WHEN
        Map<T, K> actual = underTest.transform(sourceMap);

        //THEN
        assertNotNull(actual);
    }

    /**
     * Creates the parameters to be used for testing the map transformations.
     * @return parameters to be used for testing the map transformation.
     */
    @DataProvider
    private Object[][] dataMapTransformerObject() {
        return new Object[][] {
                {"Test that a simple Map is correctly transformed", SAMPLE_MAP},
                {"Test that a Map containing a list is correctly transformed", COMPLEX_MAP},
                {"Test that a Map containing a Map is correctly transformed", VERY_COMPLEX_MAP},
                {"Test that a Map containing a Map that has an object as key, is correctly transformed", EXTREME_COMPLEX_MAP}
        };
    }

    /**
     * Test that the given map is correctly transformed.
     */
    @Test
    public void testTransformWorksProperly() {
        //GIVEN
        COMPLEX_OBJECT_MAP.put(fromFooSimple, fromFoo);

        //WHEN
        Map<MutableToFooSimple, ImmutableToFoo> actual = underTest.transform(COMPLEX_OBJECT_MAP, MutableToFooSimple.class, ImmutableToFoo.class);

        //THEN
        assertNotNull(actual);
        for (Map.Entry<MutableToFooSimple, ImmutableToFoo> entry : actual.entrySet()) {
            assertThat(entry.getKey(), sameBeanAs(fromFooSimple));
            assertThat(entry.getValue(), sameBeanAs(fromFoo));

        }
    }
}
