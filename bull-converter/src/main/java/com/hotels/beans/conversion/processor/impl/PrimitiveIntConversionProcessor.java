package com.hotels.beans.conversion.processor.impl;

import java.util.function.Function;

import com.hotels.beans.conversion.processor.ConversionProcessor;

/**
 * Provides all method for converting any primitive type to a primitive int.
 */
public class PrimitiveIntConversionProcessor implements ConversionProcessor {
    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Byte, Integer> convertPrimitiveByte() {
        return val -> (int) ((byte) val);
    }

    @Override
    public Function<Byte, Integer> convertByte() {
        return Byte::intValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Short, Integer> convertPrimitiveShort() {
        return val -> (int) val;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Short, Integer> convertShort() {
        return Short::intValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Integer, Integer> convertPrimitiveInt() {
        return val -> (int) val;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Integer, Integer> convertInteger() {
        return val -> val;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Long, Integer> convertPrimitiveLong() {
        return val -> (int) ((long) val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Long, Integer> convertLong() {
        return Long::intValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Float, Integer> convertPrimitiveFloat() {
        return val -> (int) ((float) val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Float, Integer> convertFloat() {
        return Float::intValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Double, Integer> convertPrimitiveDouble() {
        return val -> (int) ((double) val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Double, Integer> convertDouble() {
        return Double::intValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Character, Integer> convertChar() {
        return val -> (int) ((char) val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Character, Integer> convertCharacter() {
        return val -> (int) val;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Boolean, Integer> convertPrimitiveBoolean() {
        return val -> val ? (int) 1 : (int) 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Boolean, Integer> convertBoolean() {
        return val -> val ? (int) 1 : (int) 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<String, Integer> convertString() {
        return Integer::parseInt;
    }
}
