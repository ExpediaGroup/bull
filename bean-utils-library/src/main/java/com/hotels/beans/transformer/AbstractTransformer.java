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

package com.hotels.beans.transformer;

import static java.util.Arrays.asList;

import static com.hotels.beans.validator.Validator.notNull;
import static com.hotels.beans.cache.CacheManagerFactory.getCacheManager;

import java.util.Map;

import com.hotels.beans.cache.CacheManager;
import com.hotels.beans.conversion.analyzer.ConversionAnalyzer;
import com.hotels.beans.model.FieldMapping;
import com.hotels.beans.model.FieldTransformer;
import com.hotels.beans.utils.ClassUtils;
import com.hotels.beans.utils.ReflectionUtils;
import com.hotels.beans.validator.Validator;
import com.hotels.beans.validator.ValidatorImpl;

/**
 * Utility methods for populating Mutable, Immutable and Hybrid JavaBeans properties via reflection.
 * Contains all method implementation that will be common to any {@link Transformer} implementation.
 */
abstract class AbstractTransformer implements Transformer {
    /**
     * Reflection utils instance {@link ReflectionUtils}.
     */
    final ReflectionUtils reflectionUtils;

    /**
     * Class utils instance {@link ClassUtils}.
     */
    final ClassUtils classUtils;

    /**
     * CacheManager instance {@link CacheManager}.
     */
    final CacheManager cacheManager;

    /**
     * Contains both the field name mapping and the lambda function to be applied on fields.
     */
    final TransformerSettings settings;

    /**
     * Bean Validator. It offers the possibility to validate a given Java Bean against a set of defined constraints.
     */
    final Validator validator;

    /**
     * Conversion analyzer. It allows to automatically convert common field types.
     */
    final ConversionAnalyzer conversionAnalyzer;

    /**
     * Default constructor.
     */
    AbstractTransformer() {
        this.reflectionUtils = new ReflectionUtils();
        this.classUtils = new ClassUtils();
        this.validator = new ValidatorImpl();
        this.settings = new TransformerSettings();
        this.conversionAnalyzer = new ConversionAnalyzer();
        this.cacheManager = getCacheManager("transformer");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Transformer withFieldMapping(final FieldMapping... fieldMapping) {
        final Map<String, String> fieldsNameMapping = settings.getFieldsNameMapping();
        for (FieldMapping mapping : fieldMapping) {
            fieldsNameMapping.put(mapping.getDestFieldName(), mapping.getSourceFieldName());
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
    public final Transformer withFieldTransformer(final FieldTransformer... fieldTransformer) {
        Map<String, FieldTransformer> fieldsTransformers = settings.getFieldsTransformers();
        for (FieldTransformer transformer : fieldTransformer) {
            fieldsTransformers.put(transformer.getDestFieldName(), transformer);
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void removeFieldTransformer(final String destFieldName) {
        notNull(destFieldName, "The field name for which the transformer function has to be removed cannot be null!");
        settings.getFieldsTransformers().remove(destFieldName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void resetFieldsTransformer() {
        settings.getFieldsTransformers().clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Transformer setDefaultValueForMissingField(final boolean useDefaultValue) {
        settings.setSetDefaultValueForMissingFields(useDefaultValue);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Transformer setFlatFieldNameTransformation(final boolean useFlatTransformation) {
        settings.setFlatFieldNameTransformation(useFlatTransformation);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Transformer setValidationEnabled(final boolean validationEnabled) {
        settings.setValidationEnabled(validationEnabled);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Transformer setDefaultValueSetEnabled(final boolean defaultValueSetEnabled) {
        settings.setDefaultValueSetEnabled(defaultValueSetEnabled);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Transformer setDefaultPrimitiveTypeConversionEnabled(final boolean defaultPrimitiveTypeConversionEnabled) {
        settings.setDefaultPrimitiveTypeConversionEnabled(defaultPrimitiveTypeConversionEnabled);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <T, K> K transform(final T sourceObj, final Class<? extends K> targetClass) {
        notNull(sourceObj, "The object to copy cannot be null!");
        notNull(targetClass, "The destination class cannot be null!");
        return transform(sourceObj, targetClass, null);
    }

    /**
     * Copies all properties from an object to a new one.
     * @param sourceObj the source object
     * @param targetClass the destination object class
     * @param breadcrumb the full path of the current field starting from his ancestor
     * @param <T> the Source object type
     * @param <K> the target object type
     * @return a copy of the source object into the destination object
     */
    protected abstract <T, K> K transform(T sourceObj, Class<? extends K> targetClass, String breadcrumb);

    /**
     * {@inheritDoc}
     */
    @Override
    public final <T, K> void transform(final T sourceObj, final K targetObject) {
        notNull(sourceObj, "The object to copy cannot be null!");
        notNull(targetObject, "The destination object cannot be null!");
        transform(sourceObj, targetObject, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Transformer skipTransformationForField(final String... fieldName) {
        if (fieldName.length != 0) {
            settings.getFieldsToSkip().addAll(asList(fieldName));
        }
        return this;
    }

    @Override
    public void resetFieldsTransformationSkip() {
        settings.getFieldsToSkip().clear();
    }

    /**
     * Copies all properties from an object to a new one.
     * @param sourceObj the source object
     * @param targetObject the destination object
     * @param breadcrumb the full path of the current field starting from his ancestor
     * @param <T> the Source object type
     * @param <K> the target object type
     */
    protected abstract <T, K> void transform(T sourceObj, K targetObject, String breadcrumb);
}
