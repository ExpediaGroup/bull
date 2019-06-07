package com.hotels.beans.conversion.processor.impl;

import static java.lang.Long.valueOf;

import java.util.function.Function;

import com.hotels.beans.conversion.processor.ConversionProcessor;

/**
 * Provides all method for converting any primitive type to a {@link Long}.
 */
public class LongConversionProcessor implements ConversionProcessor {
    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Byte, Long> convertPrimitiveByte() {
        return val -> valueOf((long) ((byte) val));
    }

    @Override
    public Function<Byte, Long> convertByte() {
        return Long::valueOf;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Short, Long> convertPrimitiveShort() {
        return val -> valueOf((short) ((long) val));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Short, Long> convertShort() {
        return Short::longValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Integer, Long> convertPrimitiveInt() {
        return val -> valueOf((long) ((int) val));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Integer, Long> convertInteger() {
        return val -> valueOf(val.longValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Long, Long> convertPrimitiveLong() {
        return Long::valueOf;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Long, Long> convertLong() {
        return Long::longValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Float, Long> convertPrimitiveFloat() {
        return val -> valueOf((long) ((float) val));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Float, Long> convertFloat() {
        return Float::longValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Double, Long> convertPrimitiveDouble() {
        return val -> valueOf((long) ((double) val));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Double, Long> convertDouble() {
        return Double::longValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Character, Long> convertChar() {
        return val -> valueOf((long) ((char) val));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Character, Long> convertCharacter() {
        return val -> valueOf((long) val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Boolean, Long> convertPrimitiveBoolean() {
        return val -> valueOf(val ? (long) 1 : (long) 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Boolean, Long> convertBoolean() {
        return val -> valueOf(val ? (long) 1 : (long) 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<String, Long> convertString() {
        return Long::valueOf;
    }
}
