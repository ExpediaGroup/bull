/**
 * Copyright (C) 2018-2023 Expedia, Inc.
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
package com.expediagroup.transformer.cache;

import static com.expediagroup.transformer.validator.Validator.notNull;

import static lombok.AccessLevel.PRIVATE;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.NoArgsConstructor;

/**
 * Creates a {@link CacheManager} instance for the given class.
 */
@NoArgsConstructor(access = PRIVATE)
public final class CacheManagerFactory {
    /**
     * Cache store.
     */
    private static final Map<String, Map<String, Object>> CACHE_MAP = new ConcurrentHashMap<>();

    /**
     * Creates a new {@link CacheManager} instance.
     * @param cacheName the cache name
     * @return a cache manager instance
     */
    public static CacheManager getCacheManager(final String cacheName) {
        notNull(cacheName, "cacheName cannot be null!");
        return new CacheManager(CACHE_MAP.computeIfAbsent(cacheName, k -> new ConcurrentHashMap<>()));
    }
}
