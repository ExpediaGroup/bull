package com.hotels.beans.conversion.processor.impl;

import static java.lang.Double.valueOf;

import java.util.function.Function;

import com.hotels.beans.conversion.processor.ConversionProcessor;

/**
 * Provides all method for converting any primitive type to a {@link Double}.
 */
public class DoubleConversionProcessor implements ConversionProcessor {
    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Byte, Double> convertByte() {
        return Byte::doubleValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Short, Double> convertShort() {
        return Short::doubleValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Integer, Double> convertInteger() {
        return Integer::doubleValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Long, Double> convertLong() {
        return Long::doubleValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Float, Double> convertFloat() {
        return Float::doubleValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Double, Double> convertDouble() {
        return val -> val;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Character, Double> convertCharacter() {
        return Double::valueOf;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Boolean, Double> convertBoolean() {
        return val -> valueOf(val ? (double) 1 : (double) 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<String, Double> convertString() {
        return Double::valueOf;
    }
}
