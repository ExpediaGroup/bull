/**
 * Copyright (C) 2019 Expedia Inc.
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

package com.hotels.beans.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.concurrent.ConcurrentHashMap;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for {@link CacheManager}.
 */
public class CacheManagerTest {
    private static final String CACHED_VALUE = "cachedValue";
    private static final String CACHE_KEY = "cacheKey";
    private static final Class<String> CACHED_OBJECT_CLASS = String.class;
    /**
     * The class to be tested.
     */
    private CacheManager underTest;

    /**
     * Initializes mock.
     */
    @BeforeMethod
    public void before() {
        underTest = new CacheManager(new ConcurrentHashMap<>());
    }

    /**
     * Tests that the method {@code cacheObject} caches the object.
     */
    @Test
    public void testCacheObjectStoresTheGivenObjectWithTheGivenKey() {
        // GIVEN
        underTest.cacheObject(CACHE_KEY, CACHED_VALUE);

        // WHEN
        Object actual = underTest.getFromCache(CACHE_KEY, CACHED_OBJECT_CLASS);

        // THEN
        assertNotNull(actual);
        assertEquals(CACHED_OBJECT_CLASS, actual.getClass());
        assertSame(CACHED_VALUE, actual);
    }

    /**
     * Tests that the method {@code getFromCache} throw exception when the cache key.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetFromCacheThrowsExceptionWhenTheCacheKeyIsNull() {
        // GIVEN

        // WHEN
        underTest.getFromCache(null, CACHED_OBJECT_CLASS);
    }

    /**
     * Tests that the method {@code getFromCache} throw exception when the cached value class is null.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetFromCacheThrowsExceptionWhenTheCachedValueClassIsNull() {
        // GIVEN

        // WHEN
        underTest.getFromCache(CACHE_KEY, null);
    }

    /**
     * Tests that the method {@code removeFromCache} really removes the object from the cache.
     */
    @Test
    public void testRemoveFromCacheRemovesTheObject() {
        // GIVEN
        underTest.cacheObject(CACHE_KEY, CACHED_VALUE);

        // WHEN
        underTest.removeFromCache(CACHE_KEY);
        Object actual = underTest.getFromCache(CACHE_KEY, CACHED_OBJECT_CLASS);

        // THEN
        assertNull(actual);
    }
}
