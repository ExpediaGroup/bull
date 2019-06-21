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
package com.hotels.beans.conversion.processor.impl;

import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import com.hotels.beans.conversion.function.ToBooleanFunction;
import com.hotels.beans.conversion.function.ToByteFunction;
import com.hotels.beans.conversion.function.ToCharFunction;
import com.hotels.beans.conversion.function.ToFloatFunction;
import com.hotels.beans.conversion.function.ToShortFunction;
import com.hotels.beans.conversion.processor.ConversionProcessor;

public class PrimitiveConversionProcessor<T> extends ConversionProcessor<T> {

    @Override
    public ToByteFunction<T> convertPrimitiveByte() {
        return super.convertPrimitiveByte();
    }

    @Override
    public ToShortFunction<T> convertPrimitiveShort() {
        return super.convertPrimitiveShort();
    }

    @Override
    public ToIntFunction<T> convertPrimitiveInt() {
        return super.convertPrimitiveInt();
    }

    @Override
    public ToLongFunction<T> convertPrimitiveLong() {
        return super.convertPrimitiveLong();
    }

    @Override
    public ToFloatFunction<T> convertPrimitiveFloat() {
        return super.convertPrimitiveFloat();
    }

    @Override
    public ToDoubleFunction<T> convertPrimitiveDouble() {
        return super.convertPrimitiveDouble();
    }

    @Override
    public ToCharFunction<T> convertPrimitiveChar() {
        return super.convertPrimitiveChar();
    }

    @Override
    public ToBooleanFunction<T> convertPrimitiveBoolean() {
        return super.convertPrimitiveBoolean();
    }
}
