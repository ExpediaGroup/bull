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
 * Provides all method for converting any primitive type to a {@link Boolean}.
 */
public final class BooleanConversionProcessor implements ConversionProcessor<Boolean> {
    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Byte, Boolean> convertByte() {
        return val -> val != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<byte[], Boolean> convertByteArray() {
        return val -> val[0] != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Short, Boolean> convertShort() {
        return val -> val != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Integer, Boolean> convertInteger() {
        return val -> val != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Long, Boolean> convertLong() {
        return val -> val != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Float, Boolean> convertFloat() {
        return val -> val != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Double, Boolean> convertDouble() {
        return val -> val != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Character, Boolean> convertCharacter() {
        return val -> val == 'T';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Boolean, Boolean> convertBoolean() {
        return val -> val;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<String, Boolean> convertString() {
        return Boolean::valueOf;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<BigInteger, Boolean> convertBigInteger() {
        return val -> val.intValue() != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<BigDecimal, Boolean> convertBigDecimal() {
        return val -> val.intValue() != 0;
    }
}
