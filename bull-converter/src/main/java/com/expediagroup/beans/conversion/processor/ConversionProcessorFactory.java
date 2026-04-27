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
package com.expediagroup.beans.conversion.processor;

import static java.util.Optional.empty;
import static java.util.Optional.of;

import static lombok.AccessLevel.PRIVATE;

import java.util.Optional;

import com.expediagroup.beans.conversion.processor.impl.BigDecimalConversionProcessor;
import com.expediagroup.beans.conversion.processor.impl.BigIntegerConversionProcessor;
import com.expediagroup.beans.conversion.processor.impl.BooleanConversionProcessor;
import com.expediagroup.beans.conversion.processor.impl.ByteArrayConversionProcessor;
import com.expediagroup.beans.conversion.processor.impl.ByteConversionProcessor;
import com.expediagroup.beans.conversion.processor.impl.CharacterConversionProcessor;
import com.expediagroup.beans.conversion.processor.impl.DoubleConversionProcessor;
import com.expediagroup.beans.conversion.processor.impl.FloatConversionProcessor;
import com.expediagroup.beans.conversion.processor.impl.IntegerConversionProcessor;
import com.expediagroup.beans.conversion.processor.impl.LongConversionProcessor;
import com.expediagroup.beans.conversion.processor.impl.ShortConversionProcessor;
import com.expediagroup.beans.conversion.processor.impl.StringConversionProcessor;

import lombok.NoArgsConstructor;

/**
 * Creates a {@link ConversionProcessor} instance for the given class.
 */
@NoArgsConstructor(access = PRIVATE)
public final class ConversionProcessorFactory {
    /** Cached processor for {@link Byte}. */
    private static final Optional<ConversionProcessor> OPT_BYTE = of(new ByteConversionProcessor());
    /** Cached processor for {@link Short}. */
    private static final Optional<ConversionProcessor> OPT_SHORT = of(new ShortConversionProcessor());
    /** Cached processor for {@link Integer}. */
    private static final Optional<ConversionProcessor> OPT_INTEGER = of(new IntegerConversionProcessor());
    /** Cached processor for {@link Long}. */
    private static final Optional<ConversionProcessor> OPT_LONG = of(new LongConversionProcessor());
    /** Cached processor for {@link Float}. */
    private static final Optional<ConversionProcessor> OPT_FLOAT = of(new FloatConversionProcessor());
    /** Cached processor for {@link Double}. */
    private static final Optional<ConversionProcessor> OPT_DOUBLE = of(new DoubleConversionProcessor());
    /** Cached processor for {@link Character}. */
    private static final Optional<ConversionProcessor> OPT_CHARACTER = of(new CharacterConversionProcessor());
    /** Cached processor for {@link String}. */
    private static final Optional<ConversionProcessor> OPT_STRING = of(new StringConversionProcessor());
    /** Cached processor for {@link Boolean}. */
    private static final Optional<ConversionProcessor> OPT_BOOLEAN = of(new BooleanConversionProcessor());
    /** Cached processor for {@link java.math.BigInteger}. */
    private static final Optional<ConversionProcessor> OPT_BIG_INTEGER = of(new BigIntegerConversionProcessor());
    /** Cached processor for {@link java.math.BigDecimal}. */
    private static final Optional<ConversionProcessor> OPT_BIG_DECIMAL = of(new BigDecimalConversionProcessor());
    /** Cached processor for byte arrays. */
    private static final Optional<ConversionProcessor> OPT_BYTE_ARRAY = of(new ByteArrayConversionProcessor());

    /**
     * Returns a conversion processor for the given type.
     * @param clazz the class for which the conversion processor has to be retrieved.
     * @return a conversion processor for the given type wrapped into {@link Optional}
     */
    public static Optional<ConversionProcessor> getConversionProcessor(final Class<?> clazz) {
        return switch (clazz.getName()) {
            case "java.lang.Byte", "byte" -> OPT_BYTE;
            case "java.lang.Short", "short" -> OPT_SHORT;
            case "java.lang.Integer", "int" -> OPT_INTEGER;
            case "java.lang.Long", "long" -> OPT_LONG;
            case "java.lang.Float", "float" -> OPT_FLOAT;
            case "java.lang.Double", "double" -> OPT_DOUBLE;
            case "java.lang.Character", "char" -> OPT_CHARACTER;
            case "java.lang.String" -> OPT_STRING;
            case "java.lang.Boolean", "boolean" -> OPT_BOOLEAN;
            case "java.math.BigInteger" -> OPT_BIG_INTEGER;
            case "java.math.BigDecimal" -> OPT_BIG_DECIMAL;
            case "[B" -> OPT_BYTE_ARRAY;
            default -> empty();
        };
    }
}
