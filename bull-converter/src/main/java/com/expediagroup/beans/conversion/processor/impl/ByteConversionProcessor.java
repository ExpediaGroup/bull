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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Function;

import com.expediagroup.beans.conversion.processor.ConversionProcessor;

/**
 * Provides all method for converting any primitive type to a {@link Byte}.
 */
public final class ByteConversionProcessor implements ConversionProcessor<Byte> {
    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Byte, Byte> convertByte() {
        return val -> val;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<byte[], Byte> convertByteArray() {
        return val -> val[0];
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
    public Function<Integer, Byte> convertInteger() {
        return Integer::byteValue;
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
    public Function<Float, Byte> convertFloat() {
        return Float::byteValue;
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
    public Function<Character, Byte> convertCharacter() {
        return val -> (byte) val.charValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Boolean, Byte> convertBoolean() {
        return val -> TRUE.equals(val) ? (byte) 1 : (byte) 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<String, Byte> convertString() {
        return Byte::valueOf;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<BigInteger, Byte> convertBigInteger() {
        return Number::byteValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<BigDecimal, Byte> convertBigDecimal() {
        return Number::byteValue;
    }
}
