package com.hotels.beans.conversion.processor.impl;

import static java.lang.Byte.valueOf;

import java.util.function.Function;

import com.hotels.beans.conversion.processor.ConversionProcessor;

/**
 * Provides all method for converting any primitive type to a {@link Boolean}.
 */
public class BooleanConversionProcessor implements ConversionProcessor {
    @Override
    public Function<Byte, Boolean> convertByte() {
        return val -> val != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Short, Boolean> convertShort() {
        return val -> val != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Integer, Boolean> convertInteger() {
        return val -> val != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Long, Boolean> convertLong() {
        return val -> val != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Float, Boolean> convertFloat() {
        return val -> val != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Double, Boolean> convertDouble() {
        return val -> val != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Character, Boolean> convertCharacter() {
        return val -> val == 'T';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Boolean, Boolean> convertBoolean() {
        return val -> val;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<String, Boolean> convertString() {
        return Boolean::valueOf;
    }
}
