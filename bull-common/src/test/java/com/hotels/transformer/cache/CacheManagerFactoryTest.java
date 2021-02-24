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

package com.hotels.transformer.cache;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.openMocks;

import org.mockito.InjectMocks;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for class: {@link CacheManagerFactory}.
 */
public class CacheManagerFactoryTest {
    /**
     * Cache name param.
     */
    private static final String CACHE_NAME = "cacheName";

    /**
     * The class to be tested.
     */
    @InjectMocks
    private CacheManagerFactory underTest;

    /**
     * Initializes mock.
     */
    @BeforeClass
    public void beforeClass() {
        openMocks(this);
    }

    /**
     * Tests that the method: {@code getCacheManager} returns a {@link CacheManager} instance.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetCacheThrowsExceptionIfTheCacheNameIsNull() {
        // GIVEN

        // WHEN
        underTest.getCacheManager(null);
    }

    /**
     * Tests that the method: {@code getCacheManager} returns a {@link CacheManager} instance.
     */
    @Test
    public void testGetCacheReturnsACacheManagerInstance() {
        // GIVEN

        // WHEN
        final CacheManager actual = underTest.getCacheManager(CACHE_NAME);

        // THEN
        assertThat(actual).isNotNull();
    }
}
