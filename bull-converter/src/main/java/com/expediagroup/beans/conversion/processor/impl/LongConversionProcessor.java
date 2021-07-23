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
package com.expediagroup.beans.conversion.processor.impl;

import static java.lang.Boolean.TRUE;
import static java.lang.Character.getNumericValue;
import static java.nio.ByteBuffer.wrap;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.BufferUnderflowException;
import java.util.function.Function;

import com.expediagroup.beans.conversion.error.TypeConversionException;
import com.expediagroup.beans.conversion.processor.ConversionProcessor;

/**
 * Provides all method for converting any primitive type to a {@link Long}.
 */
public final class LongConversionProcessor implements ConversionProcessor<Long> {
    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Byte, Long> convertByte() {
        return Byte::longValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<byte[], Long> convertByteArray() {
        return val -> {
            try {
                return wrap(val).getLong();
            } catch (BufferUnderflowException e) {
                throw new TypeConversionException("Not enough byte to represents a Long. At least 8 bytes are required.");
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Short, Long> convertShort() {
        return Short::longValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Integer, Long> convertInteger() {
        return Integer::longValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Long, Long> convertLong() {
        return Long::longValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Float, Long> convertFloat() {
        return Float::longValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Double, Long> convertDouble() {
        return Double::longValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Character, Long> convertCharacter() {
        return val -> (long) getNumericValue(val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Boolean, Long> convertBoolean() {
        return val -> TRUE.equals(val) ? (long) 1 : (long) 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<String, Long> convertString() {
        return Long::valueOf;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<BigInteger, Long> convertBigInteger() {
        return BigInteger::longValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<BigDecimal, Long> convertBigDecimal() {
        return BigDecimal::longValue;
    }
}
