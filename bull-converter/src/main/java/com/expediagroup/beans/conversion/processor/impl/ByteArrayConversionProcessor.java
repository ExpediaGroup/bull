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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Function;

import com.expediagroup.beans.conversion.processor.ConversionProcessor;

/**
 * Provides all method for converting any primitive type to a byte[].
 */
public final class ByteArrayConversionProcessor implements ConversionProcessor<byte[]> {
    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Byte, byte[]> convertByte() {
        return val -> new byte[] {val};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<byte[], byte[]> convertByteArray() {
        return val -> val;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Short, byte[]> convertShort() {
        return val -> new byte[] {val.byteValue()};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Integer, byte[]> convertInteger() {
        return val -> new byte[] {val.byteValue()};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Long, byte[]> convertLong() {
        return val -> new byte[] {val.byteValue()};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Float, byte[]> convertFloat() {
        return val -> new byte[] {val.byteValue()};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Double, byte[]> convertDouble() {
        return val -> new byte[] {val.byteValue()};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Character, byte[]> convertCharacter() {
        return val -> new byte[] {(byte) val.charValue()};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Boolean, byte[]> convertBoolean() {
        return val -> new byte[] {TRUE.equals(val) ? (byte) 1 : (byte) 0};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<String, byte[]> convertString() {
        return String::getBytes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<BigInteger, byte[]> convertBigInteger() {
        return val -> new byte[] {val.byteValue()};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<BigDecimal, byte[]> convertBigDecimal() {
        return val -> new byte[] {val.byteValue()};
    }
}
