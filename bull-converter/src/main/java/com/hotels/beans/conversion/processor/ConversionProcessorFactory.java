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

package com.hotels.beans.conversion.processor;

import static java.util.Optional.empty;
import static java.util.Optional.of;

import static com.hotels.beans.utils.ClassUtils.isBoolean;
import static com.hotels.beans.utils.ClassUtils.isByte;
import static com.hotels.beans.utils.ClassUtils.isChar;
import static com.hotels.beans.utils.ClassUtils.isDouble;
import static com.hotels.beans.utils.ClassUtils.isFloat;
import static com.hotels.beans.utils.ClassUtils.isInt;
import static com.hotels.beans.utils.ClassUtils.isLong;
import static com.hotels.beans.utils.ClassUtils.isShort;
import static com.hotels.beans.utils.ClassUtils.isString;

import static lombok.AccessLevel.PRIVATE;

import java.util.Optional;

import com.hotels.beans.conversion.processor.impl.BooleanConversionProcessor;
import com.hotels.beans.conversion.processor.impl.ByteConversionProcessor;
import com.hotels.beans.conversion.processor.impl.CharacterConversionProcessor;
import com.hotels.beans.conversion.processor.impl.DoubleConversionProcessor;
import com.hotels.beans.conversion.processor.impl.FloatConversionProcessor;
import com.hotels.beans.conversion.processor.impl.IntegerConversionProcessor;
import com.hotels.beans.conversion.processor.impl.LongConversionProcessor;
import com.hotels.beans.conversion.processor.impl.ShortConversionProcessor;
import com.hotels.beans.conversion.processor.impl.StringConversionProcessor;

import lombok.NoArgsConstructor;

/**
 * Creates a {@link ConversionProcessor} instance for the given class.
 */
@NoArgsConstructor(access = PRIVATE)
public final class ConversionProcessorFactory {
    /**
     * Returns a conversion processor for the given type.
     * @param clazz the class for which the conversion processor has to be retrieved.
     * @return a conversion processor for the given type wrapped into {@link Optional}
     */
    public static Optional<ConversionProcessor> getConversionProcessor(final Class<?> clazz) {
        Optional<ConversionProcessor> conversionProcessor = empty();
        if (isByte(clazz)) {
            conversionProcessor = of(new ByteConversionProcessor());
        } else if (isShort(clazz)) {
            conversionProcessor = of(new ShortConversionProcessor());
        } else if (isInt(clazz)) {
            conversionProcessor = of(new IntegerConversionProcessor());
        } else if (isLong(clazz)) {
            conversionProcessor = of(new LongConversionProcessor());
        } else if (isFloat(clazz)) {
            conversionProcessor = of(new FloatConversionProcessor());
        } else if (isDouble(clazz)) {
            conversionProcessor = of(new DoubleConversionProcessor());
        } else if (isChar(clazz)) {
            conversionProcessor = of(new CharacterConversionProcessor());
        } else if (isString(clazz)) {
            conversionProcessor = of(new StringConversionProcessor());
        } else if (isBoolean(clazz)) {
            conversionProcessor = of(new BooleanConversionProcessor());
        }
        return conversionProcessor;
    }
}
