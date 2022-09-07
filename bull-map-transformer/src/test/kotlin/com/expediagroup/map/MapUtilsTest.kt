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
package com.expediagroup.map

import org.assertj.core.api.Assertions
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

/**
 * Unit test for [MapUtils].
 */
class MapUtilsTest {
    /**
     * The class to be tested.
     */
    @InjectMocks
    private lateinit var underTest: MapUtils

    /**
     * Initialized mocks.
     */
    @BeforeClass
    fun beforeClass() {
        MockitoAnnotations.openMocks(this)
    }

    /**
     * Test that a Transformer is returned.
     */
    @Test
    fun testGetTransformerWorksProperly() {
        // GIVEN

        // WHEN
        val transformer = underTest.transformer

        // THEN
        Assertions.assertThat(transformer).isNotNull
    }
}