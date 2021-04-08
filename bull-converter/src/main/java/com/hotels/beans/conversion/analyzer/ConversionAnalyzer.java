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
package com.hotels.beans.conversion.analyzer;

import static java.util.Optional.empty;
import static java.util.Optional.of;

import static com.hotels.beans.conversion.processor.ConversionProcessorFactory.getConversionProcessor;
import static com.hotels.transformer.cache.CacheManagerFactory.getCacheManager;
import static com.hotels.transformer.utils.ClassUtils.isBigDecimal;
import static com.hotels.transformer.utils.ClassUtils.isBigInteger;
import static com.hotels.transformer.utils.ClassUtils.isBoolean;
import static com.hotels.transformer.utils.ClassUtils.isByte;
import static com.hotels.transformer.utils.ClassUtils.isByteArray;
import static com.hotels.transformer.utils.ClassUtils.isChar;
import static com.hotels.transformer.utils.ClassUtils.isDouble;
import static com.hotels.transformer.utils.ClassUtils.isFloat;
import static com.hotels.transformer.utils.ClassUtils.isInt;
import static com.hotels.transformer.utils.ClassUtils.isLong;
import static com.hotels.transformer.utils.ClassUtils.isShort;
import static com.hotels.transformer.utils.ClassUtils.isString;

import java.util.Optional;
import java.util.function.Function;

import com.hotels.beans.conversion.processor.ConversionProcessor;
import com.hotels.transformer.cache.CacheManager;
import com.hotels.transformer.utils.ClassUtils;

/**
 * This class provides method for converting a primitive input into another.
 */
public final class ConversionAnalyzer {
    /**
     * CacheManager instance {@link CacheManager}.
     */
    private static final CacheManager CACHE_MANAGER = getCacheManager("conversionAnalyzer");

    /**
     * Class utils instance {@link ClassUtils}.
     */
    private final ClassUtils classUtils;

    /**
     * Default constructor.
     */
    public ConversionAnalyzer() {
        this.classUtils = new ClassUtils();
    }

    /**
     * Analyzes Fields given as input and returns the conversion processor.
     * @param sourceClass source field class
     * @param targetClass the destination field class
     * @return an {@link Optional} containing the conversion function (if exists)
     */
    @SuppressWarnings("unchecked")
    public Optional<Function<Object, Object>> getConversionFunction(final Class<?> sourceClass, final Class<?> targetClass) {
        final String cacheKey = "ConversionFunction-" + sourceClass.getName() + "-" + targetClass.getName();
        return CACHE_MANAGER.getFromCache(cacheKey, Optional.class).orElseGet(() -> {
            Optional conversionFunction = empty();
            if (!targetClass.getSimpleName().equalsIgnoreCase(sourceClass.getSimpleName())
                    && (classUtils.isPrimitiveType(sourceClass) || classUtils.isPrimitiveTypeArray(sourceClass))) {
                conversionFunction = getConversionProcessor(targetClass)
                        .flatMap(cp -> getTypeConversionFunction(cp, sourceClass));
            }
            CACHE_MANAGER.cacheObject(cacheKey, conversionFunction);
            return conversionFunction;
        });
    }

    /**
     * Returns a function that converts to a primitive type: {@link Byte}, {@link Short}, {@link Integer}, {@link Long},
     * {@link Float}, {@link Double}, {@link Character} and {@link Boolean}.
     * @param conversionProcessor the {@link ConversionProcessor} for the given type
     * @param sourceFieldType he source field class
     * @return the conversion function
     */
    @SuppressWarnings("unchecked")
    private Optional<Function<?, ?>> getTypeConversionFunction(final ConversionProcessor conversionProcessor, final Class<?> sourceFieldType) {
        Optional<Function<?, ?>> conversionFunction = empty();
        if (isString(sourceFieldType)) {
            conversionFunction = of(conversionProcessor.convertString());
        } else if (isByte(sourceFieldType)) {
            conversionFunction = of(conversionProcessor.convertByte());
        } else if (isShort(sourceFieldType)) {
            conversionFunction = of(conversionProcessor.convertShort());
        } else if (isInt(sourceFieldType)) {
            conversionFunction = of(conversionProcessor.convertInteger());
        } else if (isLong(sourceFieldType)) {
            conversionFunction = of(conversionProcessor.convertLong());
        } else if (isFloat(sourceFieldType)) {
            conversionFunction = of(conversionProcessor.convertFloat());
        } else if (isDouble(sourceFieldType)) {
            conversionFunction = of(conversionProcessor.convertDouble());
        } else if (isChar(sourceFieldType)) {
            conversionFunction = of(conversionProcessor.convertCharacter());
        } else if (isBoolean(sourceFieldType)) {
            conversionFunction = of(conversionProcessor.convertBoolean());
        } else if (isBigInteger(sourceFieldType)) {
            conversionFunction = of(conversionProcessor.convertBigInteger());
        } else if (isBigDecimal(sourceFieldType)) {
            conversionFunction = of(conversionProcessor.convertBigDecimal());
        } else if (isByteArray(sourceFieldType)) {
            conversionFunction = of(conversionProcessor.convertByteArray());
        }
        return conversionFunction;
    }
}
