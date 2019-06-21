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

import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import com.hotels.beans.conversion.function.ToBooleanFunction;
import com.hotels.beans.conversion.function.ToByteFunction;
import com.hotels.beans.conversion.function.ToCharFunction;
import com.hotels.beans.conversion.function.ToFloatFunction;
import com.hotels.beans.conversion.function.ToShortFunction;

/**
 * Conversion methods for all primitive types.
 */
public abstract class ConversionProcessor<T> {
    /**
     * Converts a byte type.
     * @return the converted value
     */
    public ToByteFunction<T> convertPrimitiveByte() {
        return val -> (byte) val;
    }

    /**
     * Converts a {@link Byte} type.
     * @return the converted value
     */
    public Function<Byte, ?> convertByte() {
        return val -> val;
    }

    /**
     * Converts a short type.
     * @return the converted value
     */
    public ToShortFunction<T> convertPrimitiveShort() {
        return val -> (short) val;
    }

    /**
     * Converts a {@link Short} type.
     * @return the converted value
     */
    public Function<Short, ?> convertShort() {
        return val -> val;
    }

    /**
     * Converts an int type.
     * @return the converted value
     */
    public ToIntFunction<T> convertPrimitiveInt() {
        return val -> (int) val;
    }

    /**
     * Converts an {@link Integer} type.
     * @return the converted value
     */
    public Function<Integer, ?> convertInteger() {
        return val -> val;
    }

    /**
     * Converts a long type.
     * @return the converted value
     */
    public ToLongFunction<T> convertPrimitiveLong() {
        return val -> (long) val;
    }

    /**
     * Converts a {@link Long} type.
     * @return the converted value
     */
    public Function<Long, ?> convertLong() {
        return val -> val;
    }

    /**
     * Converts a float type.
     * @return the converted value
     */
    public ToFloatFunction<T> convertPrimitiveFloat() {
        return val -> (float) val;
    }

    /**
     * Converts an {@link Float} type.
     * @return the converted value
     */
    public Function<Float, ?> convertFloat() {
        return val -> val;
    }

    /**
     * Converts a double type.
     * @return the converted value
     */
    public ToDoubleFunction<T> convertPrimitiveDouble() {
        return val -> (double) val;
    }

    /**
     * Converts a {@link Double} type.
     * @return the converted value
     */
    public Function<Double, ?> convertDouble() {
        return val -> val;
    }

    /**
     * Converts a char type.
     * @return the converted value
     */
    public ToCharFunction<T> convertPrimitiveChar() {
        return val -> (char) val;
    }

    /**
     * Converts a {@link Character} type.
     * @return the converted value
     */
    public Function<Character, ?> convertCharacter() {
        return val -> val;
    }

    /**
     * Converts a boolean type.
     * @return the converted value
     */
    public ToBooleanFunction<T> convertPrimitiveBoolean() {
        return val -> (boolean) val;
    }

    /**
     * Converts a {@link Boolean} type.
     * @return the converted value
     */
    public Function<Boolean, ?> convertBoolean() {
        return val -> val;
    }

    /**
     * Converts a {@link String} type.
     * @return the converted value
     */
    public Function<String, ?> convertString() {
        return val -> val;
    }
}
