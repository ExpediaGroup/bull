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
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test
import java.util.concurrent.ConcurrentHashMap

/**
 * Unit test for [CacheManager].
 */
class CacheManagerTest {
    /**
     * The class to be tested.
     */
    private var underTest: CacheManager? = null

    /**
     * Initializes mock.
     */
    @BeforeClass
    fun beforeClass() {
        underTest = CacheManager(ConcurrentHashMap())
    }

    /**
     * Tests that the method `cacheObject` caches the object.
     */
    @Test
    fun testCacheObjectStoresTheGivenObjectWithTheGivenKey() {
        // GIVEN
        underTest!!.cacheObject(CACHE_KEY, VALUE)

        // WHEN
        val actual = underTest!!.getFromCache(CACHE_KEY, CACHED_OBJECT_CLASS)

        // THEN
        Assertions.assertThat(actual).containsSame(VALUE)
    }

    /**
     * Tests that the method `cacheObject` caches the default value if the object is null.
     */
    @Test
    fun testCacheObjectStoresTheDefaultValueIfTheGivenObjectIsNull() {
        // GIVEN
        underTest!!.cacheObject<Any?>(CACHE_KEY, null, DEFAULT_VALUE)

        // WHEN
        val actual = underTest!!.getFromCache(CACHE_KEY, CACHED_OBJECT_CLASS)

        // THEN
        Assertions.assertThat(actual).containsSame(DEFAULT_VALUE)
    }

    /**
     * Tests that the method `removeFromCache` really removes the object from the cache.
     */
    @Test
    fun testRemoveFromCacheRemovesTheObject() {
        // GIVEN
        underTest!!.cacheObject(CACHE_KEY, VALUE)

        // WHEN
        underTest!!.removeFromCache(CACHE_KEY)
        val actual = underTest!!.getFromCache(CACHE_KEY, CACHED_OBJECT_CLASS)

        // THEN
        Assertions.assertThat(actual).isNotPresent
    }

    /**
     * Tests that the method `removeMatchingKeys` really removes the object with the matching key from the cache.
     */
    @Test
    fun testRemoveMatchingKeysFromCacheRemovesTheObject() {
        // GIVEN
        underTest!!.cacheObject(CACHE_KEY, VALUE)

        // WHEN
        underTest!!.removeMatchingKeys(STARTS_WITH_REGEX)
        val actual = underTest!!.getFromCache(CACHE_KEY, CACHED_OBJECT_CLASS)

        // THEN
        Assertions.assertThat(actual).isNotPresent
    }

    companion object {
        private const val VALUE = "value"
        private const val DEFAULT_VALUE = "defaultValue"
        private const val CACHE_KEY = "cacheKey"
        private val CACHED_OBJECT_CLASS = String::class.java
        private const val STARTS_WITH_REGEX = "^ca.*"
    }
}
