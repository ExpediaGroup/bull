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
import static java.math.BigDecimal.valueOf;
import static java.nio.ByteBuffer.wrap;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.BufferUnderflowException;
import java.util.function.Function;

import com.hotels.beans.conversion.error.TypeConversionException;
import com.hotels.beans.conversion.processor.ConversionProcessor;

/**
 * Provides all method for converting any primitive type to a {@link BigDecimal}.
 */
public final class BigDecimalConversionProcessor implements ConversionProcessor<BigDecimal> {
    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Byte, BigDecimal> convertByte() {
        return val -> valueOf(val.doubleValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<byte[], BigDecimal> convertByteArray() {
        return val -> {
            try {
                return valueOf(wrap(val).getDouble());
            } catch (BufferUnderflowException e) {
                throw new TypeConversionException("Not enough byte to represents a BigDecimal. At least 8 bytes are required.");
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Short, BigDecimal> convertShort() {
        return BigDecimal::valueOf;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Integer, BigDecimal> convertInteger() {
        return BigDecimal::valueOf;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Long, BigDecimal> convertLong() {
        return BigDecimal::valueOf;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Float, BigDecimal> convertFloat() {
        return BigDecimal::valueOf;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Double, BigDecimal> convertDouble() {
        return BigDecimal::valueOf;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Character, BigDecimal> convertCharacter() {
        return val -> valueOf(getNumericValue(val));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Boolean, BigDecimal> convertBoolean() {
        return val -> valueOf(TRUE.equals(val) ? 1 : 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<String, BigDecimal> convertString() {
        return BigDecimal::new;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<BigInteger, BigDecimal> convertBigInteger() {
        return val -> valueOf(val.intValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<BigDecimal, BigDecimal> convertBigDecimal() {
        return val -> val;
    }
}
