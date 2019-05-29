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

import static java.util.Optional.empty;
import static java.util.Optional.of;

import static com.hotels.beans.cache.CacheManagerFactory.getCacheManager;
import static com.hotels.beans.conversion.ConversionProcessorFactory.getConversionProcessor;

import java.util.Optional;
import java.util.function.Function;

import com.hotels.beans.cache.CacheManager;
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
    public Optional<Function<Object, Object>> getConversionFunction(final Class<?> sourceFieldType, final Class<?> destinationFieldType) {
        return getConversionFunction(sourceFieldType, destinationFieldType, classUtils.isPrimitiveType(destinationFieldType));
    }

    /**
     * Analyzes Fields given as input and returns the conversion processor.
     * @param sourceFieldType source field class
     * @param destinationFieldType the destination field class
     * @param isDestinationFieldPrimitiveType indicates if the destination field type is primitive or not
     * @return an {@link Optional} containing the conversion function (if exists)
     */
    @SuppressWarnings("unchecked")
    public Optional<Function<Object, Object>> getConversionFunction(final Class<?> sourceFieldType,
        final Class<?> destinationFieldType, final boolean isDestinationFieldPrimitiveType) {
        final String cacheKey = "ConversionFunction-" + sourceFieldType.getName() + "-" + destinationFieldType.getName();
        return CACHE_MANAGER.getFromCache(cacheKey, Optional.class).orElseGet(() -> {
            Optional<Function<?, ?>> conversionFunction = empty();
            if (destinationFieldType != sourceFieldType && isDestinationFieldPrimitiveType && classUtils.isPrimitiveType(sourceFieldType)) {
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
    private Optional<Function<?, ?>> getConversionFunction(final ConversionProcessor conversionProcessor, final Class<?> sourceFieldType) {
        final String cacheKey = "ConversionFunction-" + sourceFieldType.getName();
        return CACHE_MANAGER.getFromCache(cacheKey, Optional.class).orElseGet(() -> {
            Optional<Function<?, ?>> conversionFunction;
            if (sourceFieldType == String.class) {
                conversionFunction = of(conversionProcessor.convertString());
            } else if (sourceFieldType == Byte.class) {
                conversionFunction = of(conversionProcessor.convertByte());
            } else if (sourceFieldType == byte.class) {
                conversionFunction = of(conversionProcessor.convertPrimitiveByte());
            } else if (sourceFieldType == Short.class) {
                conversionFunction = of(conversionProcessor.convertShort());
            } else if (sourceFieldType == short.class) {
                conversionFunction = of(conversionProcessor.convertPrimitiveShort());
            } else if (sourceFieldType == Integer.class) {
                conversionFunction = of(conversionProcessor.convertInteger());
            } else if (sourceFieldType == int.class) {
                conversionFunction = of(conversionProcessor.convertInt());
            } else if (sourceFieldType == Long.class) {
                conversionFunction = of(conversionProcessor.convertLong());
            } else if (sourceFieldType == long.class) {
                conversionFunction = of(conversionProcessor.convertPrimitiveLong());
            } else if (sourceFieldType == Float.class) {
                conversionFunction = of(conversionProcessor.convertFloat());
            } else if (sourceFieldType == float.class) {
                conversionFunction = of(conversionProcessor.convertPrimitiveFloat());
            } else if (sourceFieldType == Double.class) {
                conversionFunction = of(conversionProcessor.convertDouble());
            } else if (sourceFieldType == double.class) {
                conversionFunction = of(conversionProcessor.convertPrimitiveDouble());
            } else if (sourceFieldType == Character.class) {
                conversionFunction = of(conversionProcessor.convertCharacter());
            } else if (sourceFieldType == char.class) {
                conversionFunction = of(conversionProcessor.convertChar());
            } else if (sourceFieldType == Boolean.class) {
                conversionFunction = of(conversionProcessor.convertBoolean());
            } else if (sourceFieldType == boolean.class) {
                conversionFunction = of(conversionProcessor.convertPrimitiveBoolean());
            } else {
                conversionFunction = empty();
            }
            CACHE_MANAGER.cacheObject(cacheKey, conversionFunction);
            return conversionFunction;
        });
    }
}
