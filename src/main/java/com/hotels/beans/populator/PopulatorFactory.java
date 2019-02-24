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

package com.hotels.beans.populator;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

import static com.hotels.beans.cache.CacheManagerFactory.getCacheManager;

import static lombok.AccessLevel.PRIVATE;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import com.hotels.beans.cache.CacheManager;
import com.hotels.beans.transformer.Transformer;

import lombok.NoArgsConstructor;

/**
 * Creates a {@link Populator} instance for the given class.
 */
@NoArgsConstructor(access = PRIVATE)
public final class PopulatorFactory {
    /**
     * The Cache manager for this class.
     */
    private static final CacheManager CACHE_MANAGER = getCacheManager("populatorFactory");

    /**
     * Creates an instance of the populator object based on the given class.
     * @param <O> the generic type of the contained object in the destination object
     * @param <T> the generic type of the contained object in the source object
     * @param destObjectClass the destination object class
     * @param sourceObjectClass the source object class
     * @param transformer the bean transformer containing the field name mapping and transformation functions
     * @return the populator instance
     */
    public static <O, T> Optional<Populator> getPopulator(final Class<O> destObjectClass, final Class<T> sourceObjectClass, final Transformer transformer) {
        Optional<Populator> populator = empty();
        if (destObjectClass.isArray()) {
            populator = of(new ArrayPopulator(transformer));
        } else if (Collection.class.isAssignableFrom(destObjectClass)) {
            populator = of(new CollectionPopulator(transformer));
        } else if (Map.class.isAssignableFrom(destObjectClass)) {
            populator = of(new MapPopulator(transformer));
        } else if (Optional.class == sourceObjectClass || Optional.class == destObjectClass) {
            populator = of(new OptionalPopulator(transformer));
        }
        return populator;
    }

    /**
     * Creates an instance of the populator object based on the given class for the given field.
     * @param <O> the generic type of the contained object in the destination object
     * @param <T> the generic type of the contained object in the source object
     * @param destObjectClass the destination object class
     * @param sourceObjectClass the source object class
     * @param transformer the bean transformer containing the field name mapping and transformation functions
     * @param fieldName the name of the field to transform
     * @return the populator instance
     */
    @SuppressWarnings("unchecked")
    public static <O, T> Optional<Populator> getPopulator(final Class<O> destObjectClass, final Class<T> sourceObjectClass, final Transformer transformer, final String fieldName) {
        String cacheKey = "FieldPopulator-" + destObjectClass.getCanonicalName() + "-" + fieldName;
        return ofNullable(CACHE_MANAGER.getFromCache(cacheKey, Optional.class))
                .orElseGet(() -> {
                    Optional<Populator> populator = PopulatorFactory.getPopulator(destObjectClass, sourceObjectClass, transformer);
                    CACHE_MANAGER.cacheObject(cacheKey, populator);
                    return populator;
                });
    }
}
