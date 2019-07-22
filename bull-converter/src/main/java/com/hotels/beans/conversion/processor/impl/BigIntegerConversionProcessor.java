package com.hotels.beans.conversion.processor.impl;

import static java.lang.Integer.valueOf;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Function;

import com.hotels.beans.conversion.processor.ConversionProcessor;

import lombok.val;

/**
 * Provides all method for converting any primitive type to a {@link BigInteger}.
 */
public class BigIntegerConversionProcessor implements ConversionProcessor<BigInteger> {

    @Override
    public Function<Byte, BigInteger> convertByte() {
        return Byte::intValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Short, BigInteger> convertShort() {
        return Short::intValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Integer, BigInteger> convertInteger() {
        return val -> val;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Long, BigInteger> convertLong() {
        return Long::intValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Float, BigInteger> convertFloat() {
        return Float::intValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Double, BigInteger> convertDouble() {
        return Double::intValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Character, BigInteger> convertCharacter() {
        return Character::getNumericValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<Boolean, BigInteger> convertBoolean() {
        return val -> valueOf(val ? 1 : 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<String, BigInteger> convertString() {
        return Integer::valueOf;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<BigInteger, BigBigInteger> convertBigInteger() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Function<BigDecimal, BigBigInteger> convertBigDecimal() {
        return null;
    }
}
