package com.hotels.beans.conversion.processor.impl;

import static java.lang.Short.valueOf;

import java.util.function.Function;

import com.hotels.beans.conversion.processor.ConversionProcessor;

/**
 * Provides all method for converting any primitive type to a {@link Short}.
 */
public class ShortConversionProcessor implements ConversionProcessor {
    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Byte, Short> convertPrimitiveByte() {
        return val -> (short) val;
    }

    @Override
    public Function<Byte, Short> convertByte() {
        return Short::valueOf;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Short, Short> convertPrimitiveShort() {
        return Short::shortValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Short, Short> convertShort() {
        return val -> val;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Integer, Short> convertPrimitiveInt() {
        return val -> valueOf((short) ((int) val));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Integer, Short> convertInteger() {
        return val -> valueOf(val.shortValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Long, Short> convertPrimitiveLong() {
        return val -> valueOf((short) ((long) val));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Long, Short> convertLong() {
        return val -> valueOf(val.shortValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Float, Short> convertPrimitiveFloat() {
        return val -> valueOf((short) ((float) val));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Float, Short> convertFloat() {
        return val -> valueOf(val.shortValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Double, Short> convertPrimitiveDouble() {
        return val -> valueOf((short) ((double) val));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Double, Short> convertDouble() {
        return val -> valueOf(val.shortValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Character, Short> convertChar() {
        return val -> valueOf((short) ((char) val));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Character, Short> convertCharacter() {
        return val -> valueOf((short) val.charValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Boolean, Short> convertPrimitiveBoolean() {
        return val -> valueOf(val ? (short) 1 : (short) 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Boolean, Short> convertBoolean() {
        return val -> valueOf(val ? (short) 1 : (short) 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<String, Short> convertString() {
        return Short::valueOf;
    }
}
