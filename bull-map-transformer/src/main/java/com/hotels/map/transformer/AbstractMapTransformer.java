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

import com.hotels.beans.BeanUtils;
import com.hotels.map.transformer.model.MapTransformerSettings;
import com.hotels.transformer.AbstractTransformer;
import com.hotels.transformer.model.FieldTransformer;

/**
 * Utility methods for populating {@link java.util.Map} elements via reflection..
 * Contains all method implementation that will be common to any {@link MapTransformer} implementation.
 */
abstract class AbstractMapTransformer extends AbstractTransformer<MapTransformer, Object, MapTransformerSettings> implements MapTransformer {
    /**
     * The cache key prefix for the Transformer Functions.
     */
    private static final String TRANSFORMER_FUNCTION_CACHE_PREFIX = "MapTransformerFunction";

    /**
     * Default constructor.
     */
    AbstractMapTransformer() {
        super(TRANSFORMER_FUNCTION_CACHE_PREFIX, "mapTransformer", new MapTransformerSettings());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T, K> Map<T, K> transform(final Map<T, K> sourceMap) {
        return transform(sourceMap, new BeanUtils().getTransformer());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T, K, R, V> Map<R, V> transform(final Map<T, K> sourceMap, final Class<R> targetKeyType, final Class<V> targetElemType) {
        return transform(sourceMap, new BeanUtils().getTransformer(), targetKeyType, targetElemType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MapTransformer withKeyTransformer(final FieldTransformer... keyFieldTransformer) {
        final Map<Object, FieldTransformer> keyFieldsTransformers = settings.getKeyFieldsTransformers();
        for (FieldTransformer transformer : keyFieldTransformer) {
            keyFieldsTransformers.put(transformer.getDestFieldName(), transformer);
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void resetKeyTransformer() {
        settings.getKeyFieldsTransformers().clear();
        cacheManager.removeMatchingKeys(transformerFunctionRegex);
    }
}
