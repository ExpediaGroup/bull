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
 * Provides all method for converting any primitive type to a {@link Double}.
 */
public final class DoubleConversionProcessor implements ConversionProcessor<Double> {
    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Byte, Double> convertByte() {
        return Byte::doubleValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<byte[], Double> convertByteArray() {
        return val -> {
            try {
                return wrap(val).getDouble();
            } catch (BufferUnderflowException e) {
                throw new TypeConversionException("Not enough byte to represents a Double. At least 8 bytes are required.");
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Short, Double> convertShort() {
        return Short::doubleValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Integer, Double> convertInteger() {
        return Integer::doubleValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Long, Double> convertLong() {
        return Long::doubleValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Float, Double> convertFloat() {
        return Float::doubleValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Double, Double> convertDouble() {
        return val -> val;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Character, Double> convertCharacter() {
        return val -> (double) getNumericValue(val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Boolean, Double> convertBoolean() {
        return val -> TRUE.equals(val) ? (double) 1 : (double) 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<String, Double> convertString() {
        return Double::valueOf;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<BigInteger, Double> convertBigInteger() {
        return BigInteger::doubleValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<BigDecimal, Double> convertBigDecimal() {
        return BigDecimal::doubleValue;
    }
}
