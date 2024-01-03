/**
 * Copyright (C) 2019-2023 Expedia, Inc.
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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Function;

import com.expediagroup.beans.conversion.processor.ConversionProcessor;

/**
 * Provides all method for converting any primitive type to a {@link String}.
 */
public final class StringConversionProcessor implements ConversionProcessor<String> {
    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Byte, String> convertByte() {
        return Object::toString;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<byte[], String> convertByteArray() {
        return String::new;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Short, String> convertShort() {
        return Object::toString;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Integer, String> convertInteger() {
        return String::valueOf;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Long, String> convertLong() {
        return String::valueOf;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Float, String> convertFloat() {
        return String::valueOf;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Double, String> convertDouble() {
        return String::valueOf;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Character, String> convertCharacter() {
        return String::valueOf;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Boolean, String> convertBoolean() {
        return String::valueOf;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<String, String> convertString() {
        return val -> val;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<BigInteger, String> convertBigInteger() {
        return BigInteger::toString;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<BigDecimal, String> convertBigDecimal() {
        return BigDecimal::toPlainString;
    }
}
