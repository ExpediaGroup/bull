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
     * @return a conversion processor for the given type
     */
    public static ConversionProcessor getConversionProcessor(final Class<?> clazz) {
        ConversionProcessor conversionProcessor = null;
        if (isByte(clazz)) {
            conversionProcessor = new ByteConversionProcessor();
        } else if (isShort(clazz)) {
            conversionProcessor = new ShortConversionProcessor();
        } else if (isInt(clazz)) {
            conversionProcessor = new IntegerConversionProcessor();
        } else if (isLong(clazz)) {
            conversionProcessor = new LongConversionProcessor();
        } else if (isFloat(clazz)) {
            conversionProcessor = new FloatConversionProcessor();
        } else if (isDouble(clazz)) {
            conversionProcessor = new DoubleConversionProcessor();
        } else if (isChar(clazz)) {
            conversionProcessor = new CharacterConversionProcessor();
        } else if (isString(clazz)) {
            conversionProcessor = new StringConversionProcessor();
        } else if (isBoolean(clazz)) {
            conversionProcessor = new BooleanConversionProcessor();
        }
        return conversionProcessor;
    }
}
