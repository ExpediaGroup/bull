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
package com.hotels.beans.transformer;

import static java.util.Arrays.asList;

import static com.hotels.transformer.validator.Validator.notNull;

import com.hotels.beans.conversion.analyzer.ConversionAnalyzer;
import com.hotels.transformer.AbstractTransformer;
import com.hotels.transformer.model.TransformerSettings;
import com.hotels.transformer.validator.Validator;
import com.hotels.transformer.validator.ValidatorImpl;

/**
 * Utility methods for populating Mutable, Immutable and Hybrid JavaBeans properties via reflection.
 * Contains all method implementation that will be common to any {@link BeanTransformer} implementation.
 */
abstract class AbstractBeanTransformer extends AbstractTransformer<BeanTransformer, String, TransformerSettings<String>> implements BeanTransformer {
    /**
     * The cache key prefix for the Transformer Functions.
     */
    static final String TRANSFORMER_FUNCTION_CACHE_PREFIX = "BeanTransformerFunction";

    /**
     * Bean Validator. It offers the possibility to validate a given Java Bean against a set of defined constraints.
     */
    Validator validator;

    /**
     * Conversion analyzer. It allows to automatically convert common field types.
     */
    ConversionAnalyzer conversionAnalyzer;

    /**
     * Default constructor.
     */
    AbstractBeanTransformer() {
        super(TRANSFORMER_FUNCTION_CACHE_PREFIX, "beanTransformer", new TransformerSettings<>());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final BeanTransformer setDefaultValueForMissingField(final boolean useDefaultValue) {
        settings.setSetDefaultValueForMissingField(useDefaultValue);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BeanTransformer setDefaultValueForMissingPrimitiveField(final boolean useDefaultValue) {
        settings.setDefaultValueForMissingPrimitiveField(useDefaultValue);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final BeanTransformer setFlatFieldNameTransformation(final boolean useFlatTransformation) {
        settings.setFlatFieldNameTransformation(useFlatTransformation);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BeanTransformer setValidationEnabled(final boolean validationEnabled) {
        settings.setValidationEnabled(validationEnabled);
        if (validationEnabled) {
            validator = new ValidatorImpl();
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BeanTransformer setPrimitiveTypeConversionEnabled(final boolean primitiveTypeConversionEnabled) {
        settings.setPrimitiveTypeConversionEnabled(primitiveTypeConversionEnabled);
        if (primitiveTypeConversionEnabled) {
            conversionAnalyzer = new ConversionAnalyzer();
        } else {
            cacheManager.removeMatchingKeys(transformerFunctionRegex);
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BeanTransformer setCustomBuilderTransformationEnabled(final boolean customBuilderTransformationEnabled) {
        settings.setCustomBuilderTransformationEnabled(customBuilderTransformationEnabled);
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
    public BeanTransformer skipTransformationForField(final String... fieldName) {
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
