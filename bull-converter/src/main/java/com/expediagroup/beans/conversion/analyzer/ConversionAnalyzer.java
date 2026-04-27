/**
 * Copyright (C) 2019-2026 Expedia, Inc.
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
package com.expediagroup.beans.conversion.analyzer;

import static java.util.Optional.empty;
import static java.util.Optional.of;

import static com.expediagroup.transformer.cache.CacheManagerFactory.getCacheManager;

import java.util.Optional;
import java.util.function.Function;

import com.expediagroup.beans.conversion.processor.ConversionProcessor;
import com.expediagroup.beans.conversion.processor.ConversionProcessorFactory;
import com.expediagroup.transformer.cache.CacheManager;
import com.expediagroup.transformer.utils.ClassUtils;

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
                conversionFunction = ConversionProcessorFactory.getConversionProcessor(targetClass)
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
     * @param sourceFieldType the source field class
     * @return the conversion function
     */
    @SuppressWarnings("unchecked")
    private Optional<Function<?, ?>> getTypeConversionFunction(final ConversionProcessor conversionProcessor, final Class<?> sourceFieldType) {
        return switch (sourceFieldType.getName()) {
            case "java.lang.String" -> of(conversionProcessor.convertString());
            case "java.lang.Byte", "byte" -> of(conversionProcessor.convertByte());
            case "java.lang.Short", "short" -> of(conversionProcessor.convertShort());
            case "java.lang.Integer", "int" -> of(conversionProcessor.convertInteger());
            case "java.lang.Long", "long" -> of(conversionProcessor.convertLong());
            case "java.lang.Float", "float" -> of(conversionProcessor.convertFloat());
            case "java.lang.Double", "double" -> of(conversionProcessor.convertDouble());
            case "java.lang.Character", "char" -> of(conversionProcessor.convertCharacter());
            case "java.lang.Boolean", "boolean" -> of(conversionProcessor.convertBoolean());
            case "java.math.BigInteger" -> of(conversionProcessor.convertBigInteger());
            case "java.math.BigDecimal" -> of(conversionProcessor.convertBigDecimal());
            case "[B" -> of(conversionProcessor.convertByteArray());
            default -> empty();
        };
    }
}
