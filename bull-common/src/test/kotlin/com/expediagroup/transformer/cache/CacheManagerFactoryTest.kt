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
package com.expediagroup.transformer.cache

import org.assertj.core.api.Assertions
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

/**
 * Unit test for class: [CacheManagerFactory].
 */
class CacheManagerFactoryTest {
    /**
     * The class to be tested.
     */
    @InjectMocks
    private val underTest: CacheManagerFactory? = null

    /**
     * Initializes mock.
     */
    @BeforeClass
    fun beforeClass() {
        MockitoAnnotations.openMocks(this)
    }

    /**
     * Tests that the method: `getCacheManager` returns a [CacheManager] instance.
     */
    @Test(expectedExceptions = [IllegalArgumentException::class])
    fun testGetCacheThrowsExceptionIfTheCacheNameIsNull() {
        // GIVEN

        // WHEN
        CacheManagerFactory.getCacheManager(null)
    }

    /**
     * Tests that the method: `getCacheManager` returns a [CacheManager] instance.
     */
    @Test
    fun testGetCacheReturnsACacheManagerInstance() {
        // GIVEN

        // WHEN
        val actual = CacheManagerFactory.getCacheManager(CACHE_NAME)

        // THEN
        Assertions.assertThat(actual).isNotNull
    }

    companion object {
        /**
         * Cache name param.
         */
        private const val CACHE_NAME = "cacheName"
    }
}