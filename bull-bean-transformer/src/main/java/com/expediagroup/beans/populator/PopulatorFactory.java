/**
 * Copyright (C) 2019-2026 Expedia, Inc.
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
package com.expediagroup.beans.populator;

import static java.util.Optional.empty;
import static java.util.Optional.of;

import static lombok.AccessLevel.PRIVATE;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import com.expediagroup.beans.transformer.BeanTransformer;

import lombok.NoArgsConstructor;

/**
 * Creates a {@link Populator} instance for the given class.
 */
@NoArgsConstructor(access = PRIVATE)
public final class PopulatorFactory {
    /**
     * Creates an instance of the populator object based on the given class using Java 26 pattern matching.
     * Sealed classes allow the compiler to check exhaustiveness of type patterns.
     * @param <O> the generic type of the contained object in the destination object
     * @param <T> the generic type of the contained object in the source object
     * @param destObjectClass the destination object class
     * @param sourceObjectClass the source object class
     * @param transformer the bean transformer containing the field name mapping and transformation functions
     * @return the populator instance
     */
    public static <O, T> Optional<Populator> getPopulator(final Class<O> destObjectClass, final Class<T> sourceObjectClass, final BeanTransformer transformer) {
        if (destObjectClass.isArray()) {
            return of(new ArrayPopulator(transformer));
        } else if (Collection.class.isAssignableFrom(destObjectClass)) {
            return of(new CollectionPopulator(transformer));
        } else if (Map.class.isAssignableFrom(destObjectClass)) {
            return of(new MapPopulator(transformer));
        } else if (Optional.class == sourceObjectClass || Optional.class == destObjectClass) {
            return of(new OptionalPopulator(transformer));
        }
        return empty();
    }
}
