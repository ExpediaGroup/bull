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

package com.hotels.beans.conversion;

import static java.util.Objects.isNull;

import java.util.Optional;
import java.util.function.Function;

import com.hotels.beans.conversion.analyzer.ConversionAnalyzer;
import com.hotels.beans.conversion.error.NoConverterAvailableException;
import com.hotels.beans.validator.Validator;

/**
 * This class provides method for converting a primitive input into another.
 */
public class ConverterImpl implements Converter {
    /**
     * Conversion analyzer. It allows to automatically convert common field types.
     */
    private final ConversionAnalyzer conversionAnalyzer;

    /**
     * Default constructor.
     */
    public ConverterImpl() {
        conversionAnalyzer = new ConversionAnalyzer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Function<Object, Object>> getConversionFunction(final Class<?> sourceClass, final Class<?> targetClass) {
        return conversionAnalyzer.getConversionFunction(sourceClass, targetClass);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T, K> K convertValue(final T valueToConvert, final Class<K> targetClass) {
        Validator.notNull(targetClass, "The destination field type cannot be null.");
        if (isNull(valueToConvert)) {
            return null;
        }
        return (K) getConversionFunction(valueToConvert.getClass(), targetClass)
                .map(processor -> processor.apply(targetClass))
                .orElseThrow(() -> new NoConverterAvailableException("No converter available for type: " + targetClass.getName()));
    }
}
