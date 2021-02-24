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

package com.hotels.map.transformer;

import java.util.Map;

import com.hotels.beans.transformer.BeanTransformer;
import com.hotels.transformer.Transformer;
import com.hotels.transformer.model.FieldTransformer;

/**
 * Utility methods for populating {@link java.util.Map} elements via reflection.
 * The implementations are provided by {@link MapTransformerImpl}.
 */
public interface MapTransformer extends Transformer<MapTransformer> {
    /**
     * Copies all properties from a map to a new one applying the transformation function and mappings defined.
     * @param sourceMap the source map
     * @param <T> the key object type in the source map
     * @param <K> the elem object type in the source map
     * @return a copy of the source object into the destination object
     * @throws IllegalArgumentException if any parameter is invalid
     */
    <T, K> Map<T, K> transform(Map<T, K> sourceMap);

    /**
     * Copies all properties from a map to a new one applying the transformation function and mappings defined.
     * @param sourceMap the source map
     * @param beanTransformer the bean transformer to use for the map elements transformation
     * @param <T> the key object type in the source map
     * @param <K> the elem object type in the source map
     * @return a copy of the source object into the destination object
     * @throws IllegalArgumentException if any parameter is invalid
     */
    <T, K> Map<T, K> transform(Map<T, K> sourceMap, BeanTransformer beanTransformer);

    /**
     * Copies all properties from a map to a new one applying the transformation function and mappings defined.
     * @param sourceMap the source map
     * @param targetKeyType the key type in the target map
     * @param targetElemType the elem type in the target map
     * @param <T> the key object type in the source map
     * @param <K> the elem object type in the source map
     * @param <R> the key object type in the target map
     * @param <V> the elem object type in the target map
     * @return a copy of the source object into the destination object
     * @throws IllegalArgumentException if any parameter is invalid
     */
    <T, K, R, V> Map<R, V> transform(Map<T, K> sourceMap, Class<R> targetKeyType, Class<V> targetElemType);

    /**
     * Copies all properties from a map to a new one applying the transformation function and mappings defined.
     * @param sourceMap the source map
     * @param beanTransformer the bean transformer to use for the map elements transformation
     * @param targetKeyType the key type in the target map
     * @param targetElemType the elem type in the target map
     * @param <T> the key object type in the source map
     * @param <K> the elem object type in the source map
     * @param <R> the key object type in the target map
     * @param <V> the elem object type in the target map
     * @return a copy of the source object into the destination object
     * @throws IllegalArgumentException if any parameter is invalid
     */
    <T, K, R, V> Map<R, V> transform(Map<T, K> sourceMap, BeanTransformer beanTransformer, Class<R> targetKeyType, Class<V> targetElemType);

    /**
     * Initializes the transformer functions to apply on a Map key. The transformer function returns directly the field value.
     * @param keyFieldTransformer the fields transformer function
     * @return the {@link Transformer} instance
     */
    MapTransformer withKeyTransformer(FieldTransformer... keyFieldTransformer);

    /**
     * Removes all the configured key transformer.
     */
    void resetKeyTransformer();
}
