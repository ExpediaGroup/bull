package com.hotels.beans.conversion.processor.impl;

import static java.lang.Integer.valueOf;

import java.util.function.Function;

import com.hotels.beans.conversion.processor.ConversionProcessor;

/**
 * Provides all method for converting any primitive type to a {@link Integer}.
 */
public class IntegerConversionProcessor implements ConversionProcessor {
    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Byte, Integer> convertPrimitiveByte() {
        return val -> valueOf((int) ((byte)val));
    }

    @Override
    public Function<Byte, Integer> convertByte() {
        return Integer::valueOf;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Short, Integer> convertPrimitiveShort() {
        return val -> valueOf((int) ((short)val));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Short, Integer> convertShort() {
        return Integer::valueOf;
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
        return val -> valueOf((int) ((long) val));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Long, Integer> convertLong() {
        return val -> valueOf(val.intValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Float, Integer> convertPrimitiveFloat() {
        return val -> valueOf((int) ((float) val));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Float, Integer> convertFloat() {
        return val -> valueOf(val.intValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Double, Integer> convertPrimitiveDouble() {
        return val -> valueOf((int) ((double) val));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Double, Integer> convertDouble() {
        return val -> valueOf(val.intValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Character, Integer> convertChar() {
        return val -> valueOf((int) ((char) val));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Character, Integer> convertCharacter() {
        return Integer::valueOf;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Boolean, Integer> convertPrimitiveBoolean() {
        return val -> valueOf(val ? (int) 1 : (int) 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Boolean, Integer> convertBoolean() {
        return val -> valueOf(val ? (int) 1 : (int) 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<String, Integer> convertString() {
        return Integer::valueOf;
    }
}
