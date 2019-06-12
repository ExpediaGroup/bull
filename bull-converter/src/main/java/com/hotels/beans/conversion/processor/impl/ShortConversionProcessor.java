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

import static java.lang.Short.valueOf;

import java.util.function.Function;

import com.hotels.beans.conversion.processor.ConversionProcessor;

/**
 * Provides all method for converting any primitive type to a {@link Short}.
 */
public final class ShortConversionProcessor implements ConversionProcessor {
    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Byte, Short> convertPrimitiveByte() {
        return val -> (short) val;
    }

    @Override
    public Function<Byte, Short> convertByte() {
        return Short::valueOf;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Short, Short> convertPrimitiveShort() {
        return Short::shortValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Short, Short> convertShort() {
        return val -> val;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Integer, Short> convertPrimitiveInt() {
        return val -> valueOf((short) ((int) val));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Integer, Short> convertInteger() {
        return val -> valueOf(val.shortValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Long, Short> convertPrimitiveLong() {
        return val -> valueOf((short) ((long) val));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Long, Short> convertLong() {
        return val -> valueOf(val.shortValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Float, Short> convertPrimitiveFloat() {
        return val -> valueOf((short) ((float) val));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Float, Short> convertFloat() {
        return val -> valueOf(val.shortValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Double, Short> convertPrimitiveDouble() {
        return val -> valueOf((short) ((double) val));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Double, Short> convertDouble() {
        return val -> valueOf(val.shortValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Character, Short> convertChar() {
        return val -> valueOf((short) ((char) val));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Character, Short> convertCharacter() {
        return val -> valueOf((short) val.charValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Boolean, Short> convertPrimitiveBoolean() {
        return val -> valueOf(val ? (short) 1 : (short) 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Boolean, Short> convertBoolean() {
        return val -> valueOf(val ? (short) 1 : (short) 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<String, Short> convertString() {
        return Short::valueOf;
    }
}
