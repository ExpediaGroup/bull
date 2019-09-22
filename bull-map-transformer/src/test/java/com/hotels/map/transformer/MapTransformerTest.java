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

import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.isIn;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.HashMap;
import java.util.Map;

import org.mockito.InjectMocks;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.hotels.beans.BeanUtils;
import com.hotels.beans.transformer.AbstractTransformerTest;
import com.hotels.beans.transformer.BeanTransformer;
import com.hotels.transformer.model.FieldMapping;

/**
 * Unit test for {@link MapTransformer}.
 */
public class MapTransformerTest extends AbstractTransformerTest {
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
    public <T, K> void testTransformRaisesExceptionIfItsCalledWithNullParameter(final String testCaseDescription,
        final Map<T, K> sourceMap, final BeanTransformer beanTransformer) {
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
        assertEquals(actual.size(), sourceMap.size());
        assertThat(actual.entrySet(), both(everyItem(isIn(sourceMap.entrySet()))).and(containsInAnyOrder(sourceMap.entrySet().toArray())));
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
     * Test that the given map is correctly transformed if key mappings are defined.
     */
    @Test
    public void testTransformWorksProperlyWithKeyMapping() {
        //GIVEN
        Map<String, Integer> sourceMap = new HashMap<>();
        sourceMap.put("key1", 1);
        sourceMap.put("key2", 2);
        underTest.withFieldMapping(new FieldMapping<>("key1", "key2"));

        //WHEN
        Map<String, Integer> actual = underTest.transform(sourceMap);

        //THEN
        assertNotNull(actual);
        assertEquals(actual.size(), sourceMap.size());
        assertEquals(actual.get("key1"), sourceMap.get("key1"));
        assertEquals(actual.get("key2"), sourceMap.get("key1"));
        underTest.resetFieldsMapping();
    }
}
