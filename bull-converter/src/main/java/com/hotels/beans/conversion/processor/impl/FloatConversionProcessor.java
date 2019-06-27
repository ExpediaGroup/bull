package com.hotels.beans.conversion.processor.impl;


import static java.lang.Float.valueOf;

import java.util.function.Function;

import com.hotels.beans.conversion.processor.ConversionProcessor;

/**
 * Provides all method for converting any primitive type to a {@link Float}.
 */
public class FloatConversionProcessor implements ConversionProcessor {
    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Byte, Float> convertByte() {
        return Float::valueOf;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Short, Float> convertShort() {
        return Short::floatValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Integer, Float> convertInteger() {
        return Integer::floatValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Long, Float> convertLong() {
        return Long::floatValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Float, Float> convertFloat() {
        return val -> val;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Double, Float> convertDouble() {
        return Double::floatValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Character, Float> convertCharacter() {
        return val -> valueOf((float) val);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Boolean, Float> convertBoolean() {
        return val -> valueOf(val ? (float) 1 : (float) 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<String, Float> convertString() {
        return Float::valueOf;
    }
}
