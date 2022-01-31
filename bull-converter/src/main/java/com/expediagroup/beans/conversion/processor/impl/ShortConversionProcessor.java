/**
 * Copyright (C) 2019-2022 Expedia, Inc.
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
 * Provides all method for converting any primitive type to a {@link Short}.
 */
public final class ShortConversionProcessor implements ConversionProcessor<Short> {
    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Byte, Short> convertByte() {
        return Byte::shortValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<byte[], Short> convertByteArray() {
        return val -> {
            try {
                return wrap(val).getShort();
            } catch (BufferUnderflowException e) {
                throw new TypeConversionException("Not enough byte to represents a Short. At least 2 bytes are required.");
            }
        };
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
    public Function<Integer, Short> convertInteger() {
        return Integer::shortValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Long, Short> convertLong() {
        return Long::shortValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Float, Short> convertFloat() {
        return Float::shortValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Double, Short> convertDouble() {
        return Double::shortValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Character, Short> convertCharacter() {
        return val -> (short) getNumericValue(val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Boolean, Short> convertBoolean() {
        return val -> TRUE.equals(val) ? (short) 1 : (short) 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<String, Short> convertString() {
        return Short::valueOf;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<BigInteger, Short> convertBigInteger() {
        return Number::shortValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<BigDecimal, Short> convertBigDecimal() {
        return Number::shortValue;
    }
}
