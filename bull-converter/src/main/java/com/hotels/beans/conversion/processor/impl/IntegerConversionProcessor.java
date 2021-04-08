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
import static java.nio.ByteBuffer.wrap;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.BufferUnderflowException;
import java.util.function.Function;

import com.hotels.beans.conversion.error.TypeConversionException;
import com.hotels.beans.conversion.processor.ConversionProcessor;

/**
 * Provides all method for converting any primitive type to a {@link Integer}.
 */
public final class IntegerConversionProcessor implements ConversionProcessor<Integer> {
    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Byte, Integer> convertByte() {
        return Byte::intValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<byte[], Integer> convertByteArray() {
        return val -> {
            try {
                return wrap(val).getInt();
            } catch (BufferUnderflowException e) {
                throw new TypeConversionException("Not enough byte to represents an Integer. At least 4 bytes are required.");
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Short, Integer> convertShort() {
        return Short::intValue;
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
    public Function<Long, Integer> convertLong() {
        return Long::intValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Float, Integer> convertFloat() {
        return Float::intValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Double, Integer> convertDouble() {
        return Double::intValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Character, Integer> convertCharacter() {
        return Character::getNumericValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Boolean, Integer> convertBoolean() {
        return val -> TRUE.equals(val) ? 1 : 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<String, Integer> convertString() {
        return Integer::valueOf;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<BigInteger, Integer> convertBigInteger() {
        return BigInteger::intValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<BigDecimal, Integer> convertBigDecimal() {
        return BigDecimal::intValue;
    }
}
