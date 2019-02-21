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

package com.hotels.beans.transformer;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;

import static javax.validation.Validation.buildDefaultValidatorFactory;

import static org.apache.commons.lang3.StringUtils.SPACE;

import static com.hotels.beans.constant.Punctuation.DOT;
import static com.hotels.beans.constant.Punctuation.SEMICOLON;
import static com.hotels.beans.utils.ValidationUtils.notNull;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import javax.validation.Validator;
import javax.validation.ConstraintViolation;

import com.hotels.beans.cache.CacheManager;
import com.hotels.beans.cache.CacheManagerFactory;
import com.hotels.beans.error.InvalidBeanException;
import com.hotels.beans.model.FieldMapping;
import com.hotels.beans.model.FieldTransformer;
import com.hotels.beans.utils.ClassUtils;
import com.hotels.beans.utils.ReflectionUtils;

import lombok.Getter;

/**
 * Utility methods for populating Mutable, Immutable and Hybrid JavaBeans properties via reflection.
 * Contains all method implementation that will be common to any {@link Transformer} implementation.
 */
@Getter
abstract class AbstractTransformer implements Transformer {
    /**
     * Reflection utils class {@link ReflectionUtils}.
     */
    private final ReflectionUtils reflectionUtils;

    /**
     * Class reflection utils class {@link ClassUtils}.
     */
    private final ClassUtils classUtils;

    /**
     * CacheManager class {@link CacheManager}.
     */
    private final CacheManager cacheManager;

    /**
     * Contains both the field name mapping and the lambda function to be applied on fields.
     */
    private final TransformerSettings transformerSettings;

    /**
     * Default constructor.
     */
    AbstractTransformer() {
        this.reflectionUtils = new ReflectionUtils();
        this.classUtils = new ClassUtils();
        this.transformerSettings = new TransformerSettings();
        this.cacheManager = CacheManagerFactory.getCacheManager("transformer");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Transformer withFieldMapping(final FieldMapping... fieldMapping) {
        final Map<String, String> fieldsNameMapping = transformerSettings.getFieldsNameMapping();
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
        transformerSettings.getFieldsNameMapping().remove(destFieldName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void resetFieldsMapping() {
        transformerSettings.getFieldsNameMapping().clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public final Transformer withFieldTransformer(final FieldTransformer... fieldTransformer) {
        Map<String, Function<Object, Object>> fieldsTransformers = transformerSettings.getFieldsTransformers();
        for (FieldTransformer transformer : fieldTransformer) {
            fieldsTransformers.put(transformer.getDestFieldName(), transformer.getTransformerFunction());
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void removeFieldTransformer(final String destFieldName) {
        notNull(destFieldName, "The field name for which the transformer function has to be removed cannot be null!");
        transformerSettings.getFieldsTransformers().remove(destFieldName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void resetFieldsTransformer() {
        transformerSettings.getFieldsTransformers().clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Transformer setDefaultValueForMissingField(final boolean useDefaultValue) {
        transformerSettings.setSetDefaultValue(useDefaultValue);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Transformer setFlatFieldNameTransformation(final boolean useFlatTransformation) {
        transformerSettings.setFlatFieldNameTransformation(useFlatTransformation);
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
     * Checks if an object is valid.
     * @param k the object to check
     * @param <K> the object class
     * @throws InvalidBeanException {@link InvalidBeanException} if the validation fails
     */
    final <K> void validate(final K k) {
        final Set<ConstraintViolation<Object>> constraintViolations = getValidator().validate(k);
        if (!constraintViolations.isEmpty()) {
            final String errors = constraintViolations.stream()
                    .map(cv -> cv.getRootBeanClass().getCanonicalName()
                            + DOT.getSymbol()
                            + cv.getPropertyPath()
                            + SPACE
                            + cv.getMessage())
                    .collect(joining(SEMICOLON.getSymbol()));
            throw new InvalidBeanException(errors);
        }
    }

    /**
     * Creates the validator.
     * @return a {@link Validator} instance.
     */
    private Validator getValidator() {
        String cacheKey = "BeanValidator";
        return ofNullable(cacheManager.getFromCache(cacheKey, Validator.class))
                .orElseGet(() -> {
                    Validator validator = buildDefaultValidatorFactory().getValidator();
                    cacheManager.cacheObject(cacheKey, validator);
                    return validator;
                });
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
}
