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

import java.util.function.Function;

import com.hotels.beans.conversion.processor.ConversionProcessor;

/**
 * Provides all method for converting any primitive type to a primitive byte.
 */
public class PrimitiveByteConversionProcessor implements ConversionProcessor {
    /**
     * {@inheritDoc}
     */
    @Override
    public final Function<Byte, Byte> convertPrimitiveByte() {
        return val -> val;
    }

    @Override
    public final Function<Byte, Byte> convertByte() {
        return Byte::byteValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Function<Short, Byte> convertPrimitiveShort() {
        return val -> (byte) ((short) val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Function<Short, Byte> convertShort() {
        return Short::byteValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Function<Integer, Byte> convertInt() {
        return val -> (byte) ((int) val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Function<Integer, Byte> convertInteger() {
        return Integer::byteValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Function<Long, Byte> convertPrimitiveLong() {
        return val -> (byte) ((long) val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Function<Long, Byte> convertLong() {
        return Long::byteValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Function<Float, Byte> convertPrimitiveFloat() {
        return val -> (byte) ((float) val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Function<Float, Byte> convertFloat() {
        return Float::byteValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Function<Double, Byte> convertPrimitiveDouble() {
        return val -> (byte) ((double) val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Function<Double, Byte> convertDouble() {
        return Double::byteValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Function<Character, Byte> convertChar() {
        return val -> (byte) ((char) val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Function<Character, Byte> convertCharacter() {
        return val -> (byte) val.charValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Function<Boolean, Byte> convertPrimitiveBoolean() {
        return val -> val ? (byte) 1 : (byte) 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Function<Boolean, Byte> convertBoolean() {
        return val -> val ? (byte) 1 : (byte) 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Function<String, Byte> convertString() {
        return Byte::parseByte;
    }
}
