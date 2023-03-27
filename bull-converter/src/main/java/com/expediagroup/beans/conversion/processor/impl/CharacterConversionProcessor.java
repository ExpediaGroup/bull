/**
 * Copyright (C) 2018-2023 Expedia, Inc.
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
import static java.nio.ByteBuffer.wrap;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.BufferUnderflowException;
import java.util.function.Function;

import com.expediagroup.beans.conversion.error.TypeConversionException;
import com.expediagroup.beans.conversion.processor.ConversionProcessor;

/**
 * Provides all method for converting any primitive type to a {@link Character}.
 */
public final class CharacterConversionProcessor implements ConversionProcessor<Character> {
    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Byte, Character> convertByte() {
        return val -> (char) val.byteValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<byte[], Character> convertByteArray() {
        return val -> {
            try {
                return wrap(val).getChar();
            } catch (BufferUnderflowException e) {
                throw new TypeConversionException("Not enough byte to represents a Char. At least 2 bytes are required.");
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Short, Character> convertShort() {
        return val -> (char) val.shortValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Integer, Character> convertInteger() {
        return val -> (char) val.intValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Long, Character> convertLong() {
        return val -> (char) val.longValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Float, Character> convertFloat() {
        return val -> (char) val.floatValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Double, Character> convertDouble() {
        return val -> (char) val.doubleValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Character, Character> convertCharacter() {
        return val -> val;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Boolean, Character> convertBoolean() {
        return val -> TRUE.equals(val) ? 'T' : 'F';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<String, Character> convertString() {
        return val -> val.charAt(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<BigInteger, Character> convertBigInteger() {
        return val -> (char) val.intValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<BigDecimal, Character> convertBigDecimal() {
        return val -> (char) val.doubleValue();
    }
}
