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
package com.hotels.transformer;

import static com.hotels.transformer.cache.CacheManagerFactory.getCacheManager;
import static com.hotels.transformer.validator.Validator.notNull;

import java.util.Map;

import com.hotels.transformer.cache.CacheManager;
import com.hotels.transformer.model.FieldMapping;
import com.hotels.transformer.model.FieldTransformer;
import com.hotels.transformer.model.TransformerSettings;
import com.hotels.transformer.utils.ClassUtils;
import com.hotels.transformer.utils.ReflectionUtils;

import lombok.Getter;

/**
 * Abstract class containing all method implementation that will be common to any {@link Transformer}.
 * @param <T> the {@link Transformer} implementation.
 * @param <S> the {@link TransformerSettings} implementation.
 * @param <P> the {@link FieldMapping} and {@link FieldTransformer} key class type.
 */
public abstract class AbstractTransformer<T extends Transformer, P, S extends TransformerSettings<P>> implements Transformer<T> {
    /**
     * Reflection utils instance {@link ReflectionUtils}.
     */
    protected final ReflectionUtils reflectionUtils;

    /**
     * Class utils instance {@link ClassUtils}.
     */
    protected final ClassUtils classUtils;

    /**
     * CacheManager instance {@link CacheManager}.
     */
    protected final CacheManager cacheManager;

    /**
     * A regex that returns all the transformer function cached items.
     */
    protected final String transformerFunctionRegex;

    /**
     * Contains both the field name mapping and the lambda function to be applied on fields.
     */
    @Getter
    protected final S settings;

    /**
     * Default constructor.
     * @param transformerFunctionCachePrefix the cache key prefix for the Transformer Functions.
     * @param cacheName the cache key prefix for the transformer.
     * @param transformerSettings contains the transformer configuration.
     */
    protected AbstractTransformer(final String transformerFunctionCachePrefix, final String cacheName, final S transformerSettings) {
        this.reflectionUtils = new ReflectionUtils();
        this.classUtils = new ClassUtils();
        this.settings = transformerSettings;
        this.transformerFunctionRegex = "^" + transformerFunctionCachePrefix + ".*";
        this.cacheManager = getCacheManager(cacheName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public final T withFieldMapping(final FieldMapping... fieldMapping) {
        final Map<P, P> fieldsNameMapping = settings.getFieldsNameMapping();
        for (FieldMapping mapping : fieldMapping) {
            fieldsNameMapping.put((P) mapping.getDestFieldName(), (P) mapping.getSourceFieldName());
        }
        return (T) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public final T withFieldTransformer(final FieldTransformer... fieldTransformer) {
        Map<P, FieldTransformer> fieldsTransformers = settings.getFieldsTransformers();
        for (FieldTransformer transformer : fieldTransformer) {
            fieldsTransformers.put((P) transformer.getDestFieldName(), transformer);
        }
        return (T) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("SuspiciousMethodCalls")
    public final void removeFieldMapping(final String destFieldName) {
        notNull(destFieldName, "The field name for which the mapping has to be removed cannot be null!");
        settings.getFieldsNameMapping().remove(destFieldName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void resetFieldsMapping() {
        settings.getFieldsNameMapping().clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("SuspiciousMethodCalls")
    public final void removeFieldTransformer(final String destFieldName) {
        notNull(destFieldName, "The field name for which the transformer function has to be removed cannot be null!");
        settings.getFieldsTransformers().remove(destFieldName);
        cacheManager.removeMatchingKeys(transformerFunctionRegex + destFieldName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void resetFieldsTransformer() {
        settings.getFieldsTransformers().clear();
        cacheManager.removeMatchingKeys(transformerFunctionRegex);
    }
}
