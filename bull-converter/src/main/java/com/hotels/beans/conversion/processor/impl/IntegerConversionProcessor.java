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

import static java.lang.Integer.valueOf;

import java.util.function.Function;

import com.hotels.beans.conversion.processor.ConversionProcessor;

/**
 * Provides all method for converting any primitive type to a {@link Integer}.
 */
public class IntegerConversionProcessor implements ConversionProcessor {
    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Byte, Integer> convertPrimitiveByte() {
        return val -> valueOf((int) ((byte)val));
    }

    @Override
    public Function<Byte, Integer> convertByte() {
        return Integer::valueOf;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Short, Integer> convertPrimitiveShort() {
        return val -> valueOf((int) ((short)val));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Short, Integer> convertShort() {
        return Integer::valueOf;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Integer, Integer> convertPrimitiveInt() {
        return val -> (int) val;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Integer, Integer> convertInteger() {
        return val -> val;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Long, Integer> convertPrimitiveLong() {
        return val -> valueOf((int) ((long) val));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Long, Integer> convertLong() {
        return val -> valueOf(val.intValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Float, Integer> convertPrimitiveFloat() {
        return val -> valueOf((int) ((float) val));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Float, Integer> convertFloat() {
        return val -> valueOf(val.intValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Double, Integer> convertPrimitiveDouble() {
        return val -> valueOf((int) ((double) val));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Double, Integer> convertDouble() {
        return val -> valueOf(val.intValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Character, Integer> convertChar() {
        return val -> valueOf((int) ((char) val));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Character, Integer> convertCharacter() {
        return Integer::valueOf;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Boolean, Integer> convertPrimitiveBoolean() {
        return val -> valueOf(val ? (int) 1 : (int) 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Boolean, Integer> convertBoolean() {
        return val -> valueOf(val ? (int) 1 : (int) 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<String, Integer> convertString() {
        return Integer::valueOf;
    }
}
