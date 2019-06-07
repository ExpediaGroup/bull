package com.hotels.beans.conversion.processor.impl;

import java.util.function.Function;

import com.hotels.beans.conversion.processor.ConversionProcessor;

/**
 * Provides all method for converting any primitive type to a primitive byte.
 */
public class PrimitiveByteConversionProcessor implements ConversionProcessor {
    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Byte, Byte> convertPrimitiveByte() {
        return val -> (byte) val;
    }

    @Override
    public Function<Byte, Byte> convertByte() {
        return Byte::byteValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Short, Byte> convertPrimitiveShort() {
        return val -> (byte) ((short) val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Short, Byte> convertShort() {
        return Short::byteValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Integer, Byte> convertPrimitiveInt() {
        return val -> (byte) ((int) val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Integer, Byte> convertInteger() {
        return Integer::byteValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Long, Byte> convertPrimitiveLong() {
        return val -> (byte) ((long) val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Long, Byte> convertLong() {
        return Long::byteValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Float, Byte> convertPrimitiveFloat() {
        return val -> (byte) ((float) val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Float, Byte> convertFloat() {
        return Float::byteValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Double, Byte> convertPrimitiveDouble() {
        return val -> (byte) ((double) val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Double, Byte> convertDouble() {
        return Double::byteValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Character, Byte> convertChar() {
        return val -> (byte) ((char) val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Character, Byte> convertCharacter() {
        return val -> (byte) val.charValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Boolean, Byte> convertPrimitiveBoolean() {
        return val -> val ? (byte) 1 : (byte) 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Boolean, Byte> convertBoolean() {
        return val -> val ? (byte) 1 : (byte) 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<String, Byte> convertString() {
        return Byte::parseByte;
    }
}
