package com.hotels.beans.conversion.processor.impl;

import static java.lang.Byte.valueOf;

import java.util.function.Function;

import com.hotels.beans.conversion.processor.ConversionProcessor;

/**
 * Provides all method for converting any primitive type to a {@link Byte}.
 */
public class ByteConversionProcessor implements ConversionProcessor {
    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Byte, Byte> convertPrimitiveByte() {
        return Byte::valueOf;
    }

    @Override
    public Function<Byte, Byte> convertByte() {
        return val -> val;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Short, Byte> convertPrimitiveShort() {
        return val -> valueOf((byte) ((short) val));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Short, Byte> convertShort() {
        return val -> valueOf(val.byteValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Integer, Byte> convertInt() {
        return val -> valueOf((byte) ((int) val));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Integer, Byte> convertInteger() {
        return val -> valueOf(val.byteValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Long, Byte> convertPrimitiveLong() {
        return val -> valueOf((byte) ((long) val));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Long, Byte> convertLong() {
        return val -> valueOf(val.byteValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Float, Byte> convertPrimitiveFloat() {
        return val -> valueOf((byte) ((float) val));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Float, Byte> convertFloat() {
        return val -> valueOf(val.byteValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Double, Byte> convertPrimitiveDouble() {
        return val -> valueOf((byte) ((double) val));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Double, Byte> convertDouble() {
        return val -> valueOf(val.byteValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Character, Byte> convertChar() {
        return val -> valueOf((byte) ((char) val));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Character, Byte> convertCharacter() {
        return val -> valueOf((byte) val.charValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Boolean, Byte> convertPrimitiveBoolean() {
        return val -> valueOf(val ? (byte) 1 : (byte) 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Boolean, Byte> convertBoolean() {
        return val -> valueOf(val ? (byte) 1 : (byte) 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<String, Byte> convertString() {
        return Byte::valueOf;
    }
}
