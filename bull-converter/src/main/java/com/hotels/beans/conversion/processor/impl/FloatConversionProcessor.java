/**
 * Copyright (C) 2019-2021 Expedia, Inc.
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

import static java.lang.Boolean.TRUE;
import static java.lang.Character.getNumericValue;
import static java.nio.ByteBuffer.wrap;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.BufferUnderflowException;
import java.util.function.Function;

import com.hotels.beans.conversion.error.TypeConversionException;
import com.hotels.beans.conversion.processor.ConversionProcessor;

/**
 * Provides all method for converting any primitive type to a {@link Float}.
 */
public final class FloatConversionProcessor implements ConversionProcessor<Float> {
    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Byte, Float> convertByte() {
        return Byte::floatValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<byte[], Float> convertByteArray() {
        return val -> {
            try {
                return wrap(val).getFloat();
            } catch (BufferUnderflowException e) {
                throw new TypeConversionException("Not enough byte to represents a Float. At least 4 bytes are required.");
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Short, Float> convertShort() {
        return Short::floatValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Integer, Float> convertInteger() {
        return Integer::floatValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Long, Float> convertLong() {
        return Long::floatValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Float, Float> convertFloat() {
        return val -> val;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Double, Float> convertDouble() {
        return Double::floatValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Character, Float> convertCharacter() {
        return val -> (float) getNumericValue(val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Boolean, Float> convertBoolean() {
        return val -> TRUE.equals(val) ? (float) 1 : (float) 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<String, Float> convertString() {
        return Float::valueOf;
    }

    @Override
    public Function<BigInteger, Float> convertBigInteger() {
        return BigInteger::floatValue;
    }

    @Override
    public Function<BigDecimal, Float> convertBigDecimal() {
        return BigDecimal::floatValue;
    }
}
