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
package com.expediagroup.transformer;

import static java.util.Arrays.stream;

import static com.expediagroup.transformer.cache.CacheManagerFactory.getCacheManager;
import static com.expediagroup.transformer.validator.Validator.notNull;

import com.expediagroup.transformer.cache.CacheManager;
import com.expediagroup.transformer.model.FieldMapping;
import com.expediagroup.transformer.model.FieldTransformer;
import com.expediagroup.transformer.model.TransformerSettings;
import com.expediagroup.transformer.utils.ClassUtils;
import com.expediagroup.transformer.utils.ReflectionUtils;

import lombok.Getter;

/**
 * Abstract class containing all method implementation that will be common to any {@link Transformer}.
 *
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
    protected S settings;

    /**
     * Default constructor.
     *
     * @param transformerFunctionCachePrefix the cache key prefix for the Transformer Functions.
     * @param cacheName                      the cache key prefix for the transformer.
     * @param transformerSettings            contains the transformer configuration.
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
        var fieldsNameMapping = settings.getFieldsNameMapping();
        for (FieldMapping<P, P> mapping : fieldMapping) {
            stream(mapping.destFieldName())
                    .forEach(destField -> fieldsNameMapping.put(destField, mapping.sourceFieldName()));
        }
        return (T) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public final T withFieldTransformer(final FieldTransformer... fieldTransformer) {
        var fieldsTransformers = settings.getFieldsTransformers();
        for (var transformer : fieldTransformer) {
            transformer.getDestFieldName()
                    .forEach(destFieldName -> fieldsTransformers.put((P) destFieldName, transformer));
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

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public final void reset() {
        settings = (S) new TransformerSettings<>();
    }
}
