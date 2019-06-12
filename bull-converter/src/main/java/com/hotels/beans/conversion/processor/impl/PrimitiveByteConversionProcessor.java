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
    public Function<Byte, Byte> convertPrimitiveByte() {
        return val -> (byte) val;
    }

    @Override
    public Function<Byte, Byte> convertByte() {
        return Byte::byteValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Short, Byte> convertPrimitiveShort() {
        return val -> (byte) ((short) val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Short, Byte> convertShort() {
        return Short::byteValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Integer, Byte> convertPrimitiveInt() {
        return val -> (byte) ((int) val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Integer, Byte> convertInteger() {
        return Integer::byteValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Long, Byte> convertPrimitiveLong() {
        return val -> (byte) ((long) val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Long, Byte> convertLong() {
        return Long::byteValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Float, Byte> convertPrimitiveFloat() {
        return val -> (byte) ((float) val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Float, Byte> convertFloat() {
        return Float::byteValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Double, Byte> convertPrimitiveDouble() {
        return val -> (byte) ((double) val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Double, Byte> convertDouble() {
        return Double::byteValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Character, Byte> convertChar() {
        return val -> (byte) ((char) val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Character, Byte> convertCharacter() {
        return val -> (byte) val.charValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Boolean, Byte> convertPrimitiveBoolean() {
        return val -> val ? (byte) 1 : (byte) 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Boolean, Byte> convertBoolean() {
        return val -> val ? (byte) 1 : (byte) 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<String, Byte> convertString() {
        return Byte::parseByte;
    }
}
