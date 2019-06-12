/**
 * Copyright (C) 2019 Expedia, Inc.
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

import java.util.function.Function;

import com.hotels.beans.conversion.processor.ConversionProcessor;

/**
 * Provides all method for converting any primitive type to a {@link String}.
 */
public final class StringConversionProcessor implements ConversionProcessor {

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Byte, String> convertPrimitiveByte() {
        return val -> Byte.toString(val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Byte, Object> convertByte() {
        return Object::toString;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Short, String> convertPrimitiveShort() {
        return val -> Short.toString(val);
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
    public Function<Integer, String> convertPrimitiveInt() {
        return val -> Integer.toString(val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Integer, String> convertInteger() {
        return Object::toString;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Long, String> convertPrimitiveLong() {
        return val -> Long.toString(val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Long, String> convertLong() {
        return Object::toString;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Float, String> convertPrimitiveFloat() {
        return val -> Float.toString(val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Float, String> convertFloat() {
        return Object::toString;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Double, String> convertPrimitiveDouble() {
        return val -> Double.toString(val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Double, String> convertDouble() {
        return Object::toString;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Character, String> convertChar() {
        return val -> Character.toString(val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Character, String> convertCharacter() {
        return Object::toString;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Boolean, String> convertPrimitiveBoolean() {
        return val -> Boolean.toString(val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Boolean, String> convertBoolean() {
        return Object::toString;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<String, String> convertString() {
        return val -> val;
    }
}
