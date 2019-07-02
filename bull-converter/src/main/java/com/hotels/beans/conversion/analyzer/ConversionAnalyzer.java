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

package com.hotels.beans.conversion.analyzer;

import static java.util.Optional.empty;
import static java.util.Optional.of;

import static com.hotels.beans.cache.CacheManagerFactory.getCacheManager;
import static com.hotels.beans.conversion.processor.ConversionProcessorFactory.getConversionProcessor;
import static com.hotels.beans.utils.ClassUtils.isBoolean;
import static com.hotels.beans.utils.ClassUtils.isByte;
import static com.hotels.beans.utils.ClassUtils.isChar;
import static com.hotels.beans.utils.ClassUtils.isDouble;
import static com.hotels.beans.utils.ClassUtils.isFloat;
import static com.hotels.beans.utils.ClassUtils.isInt;
import static com.hotels.beans.utils.ClassUtils.isLong;
import static com.hotels.beans.utils.ClassUtils.isShort;
import static com.hotels.beans.utils.ClassUtils.isString;

import java.util.Optional;
import java.util.function.Function;

import com.hotels.beans.cache.CacheManager;
import com.hotels.beans.conversion.processor.ConversionProcessor;
import com.hotels.beans.utils.ClassUtils;

/**
 * This class analyzes the two classes received in input, and returns the conversion processor to be used.
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
     * @param sourceFieldType source field class
     * @param destinationFieldType the destination field class
     * @return an {@link Optional} containing the conversion function (if exists)
     */
    @SuppressWarnings("unchecked")
    public Optional<Function<Object, Object>> getConversionFunction(final Class<?> sourceFieldType, final Class<?> destinationFieldType) {
        final String cacheKey = "ConversionFunction-" + sourceFieldType.getName() + "-" + destinationFieldType.getName();
        return CACHE_MANAGER.getFromCache(cacheKey, Optional.class).orElseGet(() -> {
            Optional<Function<Object, Object>> conversionFunction = empty();
            if (destinationFieldType != sourceFieldType && classUtils.isPrimitiveType(sourceFieldType)) {
                conversionFunction = getConversionFunction(getConversionProcessor(destinationFieldType), sourceFieldType);
            }
            CACHE_MANAGER.cacheObject(cacheKey, conversionFunction);
            return conversionFunction;
        });
    }

    /**
     * Returns the type transformer function for the given type.
     * @param conversionProcessor the {@link ConversionProcessor} for the given type
     * @param sourceFieldType the source field class
     * @return the conversion function
     */
    @SuppressWarnings("unchecked")
    private Optional<Function<Object, Object>> getConversionFunction(final ConversionProcessor conversionProcessor, final Class<?> sourceFieldType) {
        final String cacheKey = "ConversionFunction-" + sourceFieldType.getName();
        return CACHE_MANAGER.getFromCache(cacheKey, Optional.class).orElseGet(() -> {
            Optional conversionFunction = getTypeConversionFunction(conversionProcessor, sourceFieldType);
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
    private Optional<Function<?, ?>> getTypeConversionFunction(final ConversionProcessor conversionProcessor, final Class<?> sourceFieldType) {
        final Optional<Function<?, ?>> conversionFunction;
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
        } else {
            conversionFunction = empty();
        }
        return conversionFunction;
    }
}
