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

package com.hotels.map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.openMocks;

import org.mockito.InjectMocks;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.hotels.map.transformer.MapTransformer;

/**
 * Unit test for {@link MapUtils}.
 */
public class MapUtilsTest {
    /**
     * The class to be tested.
     */
    @InjectMocks
    private MapUtils underTest;

    /**
     * Initialized mocks.
     */
    @BeforeClass
    public void beforeClass() {
        openMocks(this);
    }

    /**
     * Test that a Transformer is returned.
     */
    @Test
    public void testGetTransformerWorksProperly() {
        //GIVEN

        //WHEN
        final MapTransformer transformer = underTest.getTransformer();

        //THEN
        assertThat(transformer).isNotNull();
    }
}
