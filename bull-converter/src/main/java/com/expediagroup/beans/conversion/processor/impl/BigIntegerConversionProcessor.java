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
import static java.math.BigInteger.valueOf;
import static java.nio.ByteBuffer.wrap;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.BufferUnderflowException;
import java.util.function.Function;

import com.expediagroup.beans.conversion.error.TypeConversionException;
import com.expediagroup.beans.conversion.processor.ConversionProcessor;

/**
 * Provides all method for converting any primitive type to a {@link BigInteger}.
 */
public final class BigIntegerConversionProcessor implements ConversionProcessor<BigInteger> {
    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Byte, BigInteger> convertByte() {
        return val -> valueOf(val.longValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<byte[], BigInteger> convertByteArray() {
        return val -> {
            try {
                return valueOf(wrap(val).getLong());
            } catch (BufferUnderflowException e) {
                throw new TypeConversionException("Not enough byte to represents a BigInteger. At least 8 bytes are required.");
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Short, BigInteger> convertShort() {
        return BigInteger::valueOf;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Integer, BigInteger> convertInteger() {
        return BigInteger::valueOf;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Long, BigInteger> convertLong() {
        return BigInteger::valueOf;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Float, BigInteger> convertFloat() {
        return val -> valueOf(val.intValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Double, BigInteger> convertDouble() {
        return val -> valueOf(val.intValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Character, BigInteger> convertCharacter() {
        return val -> valueOf(getNumericValue(val));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Boolean, BigInteger> convertBoolean() {
        return val -> valueOf(TRUE.equals(val) ? 1 : 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<String, BigInteger> convertString() {
        return BigInteger::new;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<BigInteger, BigInteger> convertBigInteger() {
        return val -> val;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<BigDecimal, BigInteger> convertBigDecimal() {
        return BigDecimal::toBigInteger;
    }
}
