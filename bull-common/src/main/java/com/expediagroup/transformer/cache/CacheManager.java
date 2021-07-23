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
package com.expediagroup.transformer.cache;

import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;

import static lombok.AccessLevel.PROTECTED;

import java.util.Map;
import java.util.Optional;

import lombok.AllArgsConstructor;

/**
 * Cache Utils class.
 */
@AllArgsConstructor(access = PROTECTED)
public final class CacheManager {
    /**
     * Cache store.
     */
    private final Map<String, Object> cacheMap;

    /**
     * Caches the given object.
     * @param cacheKey the cache key.
     * @param object the object to cache.
     * @param <T> the class object type.
     */
    public <T> void cacheObject(final String cacheKey, final T object) {
        cacheObject(cacheKey, object, null);
    }

    /**
     * Caches the given object.
     * @param cacheKey the cache key.
     * @param object the object to cache.
     * @param defaultValue a default value to add to cache if the object it null
     * @param <T> the class object type.
     */
    public <T> void cacheObject(final String cacheKey, final T object, final Object defaultValue) {
        if (nonNull(object)) {
            cacheMap.put(cacheKey, object);
        } else if (nonNull(defaultValue)) {
            cacheMap.put(cacheKey, defaultValue);
        }
    }

    /**
     * Retrieves an object from cache.
     * @param cacheKey the cache key.
     * @param objectClass the class of the object to return.
     * @param <T> the class object type.
     * @return the cached object or {@code Optional.empty()} if not existing.
     */
    public <T> Optional<T> getFromCache(final String cacheKey, final Class<? extends T> objectClass) {
        return ofNullable(cacheMap.get(cacheKey)).map(objectClass::cast);
    }

    /**
     * Removes an object from cache.
     * @param cacheKey the cache key.
     */
    public void removeFromCache(final String cacheKey) {
        ofNullable(cacheKey).ifPresent(cacheMap::remove);
    }

    /**
     * Removes all items matching with the given regex.
     * @param regex the regex key.
     */
    public void removeMatchingKeys(final String regex) {
        cacheMap.keySet().removeIf(key -> key.matches(regex));
    }
}
