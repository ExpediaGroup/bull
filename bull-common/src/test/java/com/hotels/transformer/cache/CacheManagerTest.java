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

package com.hotels.transformer.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for {@link CacheManager}.
 */
public class CacheManagerTest {
    private static final String VALUE = "value";
    private static final String DEFAULT_VALUE = "defaultValue";
    private static final String CACHE_KEY = "cacheKey";
    private static final Class<String> CACHED_OBJECT_CLASS = String.class;
    private static final String STARTS_WITH_REGEX = "^ca.*";

    /**
     * The class to be tested.
     */
    private CacheManager underTest;

    /**
     * Initializes mock.
     */
    @BeforeClass
    public void beforeClass() {
        underTest = new CacheManager(new ConcurrentHashMap<>());
    }

    /**
     * Tests that the method {@code cacheObject} caches the object.
     */
    @Test
    public void testCacheObjectStoresTheGivenObjectWithTheGivenKey() {
        // GIVEN
        underTest.cacheObject(CACHE_KEY, VALUE);

        // WHEN
        Optional<String> actual = underTest.getFromCache(CACHE_KEY, CACHED_OBJECT_CLASS);

        // THEN
        assertTrue(actual.isPresent());
        assertEquals(CACHED_OBJECT_CLASS, actual.get().getClass());
        assertSame(VALUE, actual.get());
    }

    /**
     * Tests that the method {@code cacheObject} caches the default value if the object is null.
     */
    @Test
    public void testCacheObjectStoresTheDefaultValueIfTheGivenObjectIsNull() {
        // GIVEN
        underTest.cacheObject(CACHE_KEY, null, DEFAULT_VALUE);

        // WHEN
        Optional<String> actual = underTest.getFromCache(CACHE_KEY, CACHED_OBJECT_CLASS);

        // THEN
        assertTrue(actual.isPresent());
        assertEquals(CACHED_OBJECT_CLASS, actual.get().getClass());
        assertSame(DEFAULT_VALUE, actual.get());
    }

    /**
     * Tests that the method {@code removeFromCache} really removes the object from the cache.
     */
    @Test
    public void testRemoveFromCacheRemovesTheObject() {
        // GIVEN
        underTest.cacheObject(CACHE_KEY, VALUE);

        // WHEN
        underTest.removeFromCache(CACHE_KEY);
        Optional<String> actual = underTest.getFromCache(CACHE_KEY, CACHED_OBJECT_CLASS);

        // THEN
        assertFalse(actual.isPresent());
    }

    /**
     * Tests that the method {@code removeMatchingKeys} really removes the object with the matching key from the cache.
     */
    @Test
    public void testRemoveMatchingKeysFromCacheRemovesTheObject() {
        // GIVEN
        underTest.cacheObject(CACHE_KEY, VALUE);

        // WHEN
        underTest.removeMatchingKeys(STARTS_WITH_REGEX);
        Optional<String> actual = underTest.getFromCache(CACHE_KEY, CACHED_OBJECT_CLASS);

        // THEN
        assertFalse(actual.isPresent());
    }
}
