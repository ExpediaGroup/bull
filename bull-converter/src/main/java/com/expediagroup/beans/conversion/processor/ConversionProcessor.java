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
package com.expediagroup.beans.conversion.processor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Function;

import com.expediagroup.beans.conversion.error.TypeConversionException;

/**
 * Conversion methods for all primitive types.
 * @param <T> indicates any primitive type object
 */
public interface ConversionProcessor<T> {
    /**
     * Converts a {@link Byte} type.
     * @return the converted value
     */
    Function<Byte, T> convertByte();

    /**
     * Converts a byte[] type.
     * @return the converted value
     * @throws TypeConversionException if the number of bytes in the array is less
     * than the minimum required to create the destination type
     */
    Function<byte[], T> convertByteArray();

    /**
     * Converts a {@link Short} type.
     * @return the converted value
     */
    Function<Short, T> convertShort();

    /**
     * Converts an {@link Integer} type.
     * @return the converted value
     */
    Function<Integer, T> convertInteger();

    /**
     * Converts a {@link Long} type.
     * @return the converted value
     */
    Function<Long, T> convertLong();

    /**
     * Converts an {@link Float} type.
     * @return the converted value
     */
    Function<Float, T> convertFloat();

    /**
     * Converts a {@link Double} type.
     * @return the converted value
     */
    Function<Double, T> convertDouble();

    /**
     * Converts a {@link Character} type.
     * @return the converted value
     */
    Function<Character, T> convertCharacter();

    /**
     * Converts a {@link Boolean} type.
     * @return the converted value
     */
    Function<Boolean, T> convertBoolean();

    /**
     * Converts a {@link String} type.
     * @return the converted value
     */
    Function<String, T> convertString();

    /**
     * Converts an {@link BigInteger} type.
     * @return the converted value
     */
    Function<BigInteger, T> convertBigInteger();

    /**
     * Converts an {@link BigDecimal} type.
     * @return the converted value
     */
    Function<BigDecimal, T> convertBigDecimal();
}
