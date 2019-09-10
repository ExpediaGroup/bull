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

import static java.util.Collections.singletonList;

import static org.junit.Assert.assertNotNull;
import static org.mockito.MockitoAnnotations.initMocks;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mockito.InjectMocks;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.hotels.beans.sample.FromFooSimple;

/**
 * Unit test for {@link MapTransformer}.
 */
public class MapTransformerTest {
    private static final Map<String, String> SAMPLE_MAP = new HashMap<>();
    private static final Map<String, List<String>> COMPLEX_MAP = new HashMap<>();
    private static final Map<String, Map<String, String>> VERY_COMPLEX_MAP = new HashMap<>();
    private static final Map<FromFooSimple, Map<String, String>> EXTREME_COMPLEX_MAP = new HashMap<>();
    private static final String ITEM_1 = "donald";
    private static final String ITEM_2 = "duck";
    private static final BigInteger ID = new BigInteger("1234");
    private static final String NAME = "Goofy";

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
//                {"Test that a Map containing a list is correctly transformed", COMPLEX_MAP},
//                {"Test that a Map containing a Map is correctly transformed", VERY_COMPLEX_MAP},
//                {"Test that a Map containing a Map that has an object as key, is correctly transformed", EXTREME_COMPLEX_MAP}
        };
    }

    /**
     * Create an instance of two objects: one without custom annotation and another one with custom annotations then execute the copy into a specular immutable object.
     */
    private void initObjects() {
        SAMPLE_MAP.put(ITEM_1, ITEM_2);
        COMPLEX_MAP.put(ITEM_1, singletonList(ITEM_2));
        VERY_COMPLEX_MAP.put(ITEM_1, SAMPLE_MAP);
        EXTREME_COMPLEX_MAP.put(createFromFooSimple(), SAMPLE_MAP);
    }

    /**
     * Creates a {@link FromFooSimple} instance.
     * @return the {@link FromFooSimple} instance.
     */
    private FromFooSimple createFromFooSimple() {
        return new FromFooSimple(NAME, ID, Boolean.TRUE);
    }
}
