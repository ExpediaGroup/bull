package com.hotels.beans.conversion.processor.impl;

import java.util.function.Function;

import com.hotels.beans.conversion.processor.ConversionProcessor;

/**
 * Provides all method for converting any primitive type to a primitive short.
 */
public class PrimitiveShortConversionProcessor implements ConversionProcessor {
    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Byte, Short> convertPrimitiveByte() {
        return val -> (short) ((byte) val);
    }

    @Override
    public Function<Byte, Short> convertByte() {
        return Byte::shortValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Short, Short> convertPrimitiveShort() {
        return val -> (short) val;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Short, Short> convertShort() {
        return Short::shortValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Integer, Short> convertPrimitiveInt() {
        return val -> (short) ((int) val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Integer, Short> convertInteger() {
        return Integer::shortValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Long, Short> convertPrimitiveLong() {
        return val -> (short) ((long) val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Long, Short> convertLong() {
        return Long::shortValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Float, Short> convertPrimitiveFloat() {
        return val -> (short) ((float) val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Float, Short> convertFloat() {
        return Float::shortValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Double, Short> convertPrimitiveDouble() {
        return val -> (short) ((double) val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Double, Short> convertDouble() {
        return Double::shortValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Character, Short> convertChar() {
        return val -> (short) ((char) val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Character, Short> convertCharacter() {
        return val -> (short) val.charValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Boolean, Short> convertPrimitiveBoolean() {
        return val -> val ? (short) 1 : (short) 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Boolean, Short> convertBoolean() {
        return val -> val ? (short) 1 : (short) 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<String, Short> convertString() {
        return Short::parseShort;
    }
}
