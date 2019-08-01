/**
 * Copyright (C) 2019 Expedia, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hotels.beans.conversion;

import static java.lang.Character.getNumericValue;
import static java.math.BigInteger.ZERO;
import static java.nio.ByteBuffer.wrap;
import static java.util.Optional.empty;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;
import org.mockito.InjectMocks;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.hotels.beans.conversion.error.TypeConversionException;
import com.hotels.beans.conversion.processor.impl.BigDecimalConversionProcessor;
import com.hotels.beans.conversion.processor.impl.BigIntegerConversionProcessor;
import com.hotels.beans.conversion.processor.impl.BooleanConversionProcessor;
import com.hotels.beans.conversion.processor.impl.ByteConversionProcessor;
import com.hotels.beans.conversion.processor.impl.CharacterConversionProcessor;
import com.hotels.beans.conversion.processor.impl.DoubleConversionProcessor;
import com.hotels.beans.conversion.processor.impl.FloatConversionProcessor;
import com.hotels.beans.conversion.processor.impl.IntegerConversionProcessor;
import com.hotels.beans.conversion.processor.impl.StringConversionProcessor;

/**
 * Unit test for {@link Converter}.
 */
public class ConverterTest {
    private static final String ONE_AS_STRING = "1";
    private static final char CHAR_VALUE = 'T';
    private static final char CHAR_INT_VALUE = '1';
    private static final byte TRUE_AS_BYTE = 1;
    private static final int TRUE_AS_INT = 1;
    private static final char BIG_DECIMAL_ZERO_AS_CHAR = (char) BigDecimal.ZERO.doubleValue();
    private static final char BIG_INTEGER_ZERO_AS_CHAR = (char) BigInteger.ZERO.intValue();
    private static final byte[] TRUE_AS_BYTE_ARRAY = new byte[]{1};
    private static final byte[] BYTE_ARRAY = new byte[]{1, 2, 3, 4, 5, 6, 7, 8};

    /**
     * The class to be tested.
     */
    @InjectMocks
    private ConverterImpl underTest;

    /**
     * Initializes mock.
     */
    @BeforeClass
    public void beforeClass() {
        initMocks(this);
    }

    /**
     * Tests that the method {@code getConversionFunction} returns an empty optional.
     * @param testCaseDescription the test case description
     * @param sourceFieldType source field class
     * @param destinationFieldType the destination field class
     */
    @Test(dataProvider = "dataGetConversionFunctionEmptyCaseTesting")
    public void testGetConversionFunctionReturnsAnEmptyOptional(final String testCaseDescription, final Class<?> sourceFieldType,
        final Class<?> destinationFieldType) {
        // GIVEN

        // WHEN
        Optional<Function<Object, Object>> actual = underTest.getConversionFunction(sourceFieldType, destinationFieldType);

        // THEN
        assertEquals(empty(), actual);
    }

    /**
     * Creates the parameters to be used for testing that the method {@code getConversionFunction} returns an empty Optional.
     * @return parameters to be used for testing that the method {@code getConversionFunction} returns an empty Optional.
     */
    @DataProvider
    private Object[][] dataGetConversionFunctionEmptyCaseTesting() {
        return new Object[][]{
                {"Tests that the method returns an empty optional in case the source field type is not equal to the source field type but it's not primitive",
                        Pair.class, int.class},
                {"Tests that the method returns an empty optional in case the source field type is not equal to the source field type and the destination type is void",
                        int.class, Void.class}
        };
    }

    /**
     * Tests that the method {@code getConversionFunction} returns the expected conversion function.
     * @param testCaseDescription the test case description
     * @param sourceFieldType source field class
     * @param destinationFieldType the destination field class
     * @param expectedConversionFunction the expected {@link com.hotels.beans.conversion.processor.ConversionProcessor} instance
     */
    @Test(dataProvider = "dataGetConversionFunctionTesting")
    public void testGetConversionFunctionReturnsTheExpectedConversionFunction(final String testCaseDescription, final Class<?> sourceFieldType,
        final Class<?> destinationFieldType, final Function<?, ?> expectedConversionFunction) {
        // GIVEN

        // WHEN
        Optional<Function<Object, Object>> actual = underTest.getConversionFunction(sourceFieldType, destinationFieldType);

        // THEN
        assertTrue(actual.isPresent());
        assertEquals(expectedConversionFunction, actual.get());
    }

    /**
     * Creates the parameters to be used for testing the method {@code getConversionFunction}.
     * @return parameters to be used for testing the method {@code getConversionFunction}.
     */
    @DataProvider
    private Object[][] dataGetConversionFunctionTesting() {
        return new Object[][] {
                {"Tests that the method returns a IntegerConversionProcessor that converts from String to int",
                        String.class, int.class, new IntegerConversionProcessor().convertString()},
                {"Tests that the method returns a StringConversionProcessor that converts from byte to String",
                        byte.class, String.class, new StringConversionProcessor().convertByte()},
                {"Tests that the method returns a ShortConversionProcessor that converts from short to byte",
                        short.class, byte.class, new ByteConversionProcessor().convertShort()},
                {"Tests that the method returns a StringConversionProcessor that converts from int to String",
                        int.class, String.class, new StringConversionProcessor().convertInteger()},
                {"Tests that the method returns a IntegerConversionProcessor that converts from long to int",
                        long.class, int.class, new IntegerConversionProcessor().convertLong()},
                {"Tests that the method returns a DoubleConversionProcessor that converts from float to double",
                        float.class, double.class, new DoubleConversionProcessor().convertFloat()},
                {"Tests that the method returns a FloatConversionProcessor that converts from double to float",
                        double.class, float.class, new FloatConversionProcessor().convertDouble()},
                {"Tests that the method returns a StringConversionProcessor that converts from char to String",
                        char.class, String.class, new StringConversionProcessor().convertCharacter()},
                {"Tests that the method returns a BooleanConversionProcessor that converts from boolean to char",
                        char.class, boolean.class, new BooleanConversionProcessor().convertCharacter()},
                {"Tests that the method returns a CharacterConversionProcessor that converts from char to boolean",
                        boolean.class, char.class, new CharacterConversionProcessor().convertBoolean()},
                {"Tests that the method returns a CharacterConversionProcessor that converts from BigInteger to BigDecimal",
                        BigInteger.class, BigDecimal.class, new BigDecimalConversionProcessor().convertBigInteger()},
                {"Tests that the method returns a CharacterConversionProcessor that converts from BigDecimal to BigInteger",
                        BigDecimal.class, BigInteger.class, new BigIntegerConversionProcessor().convertBigDecimal()},
                {"Tests that the method returns a CharacterConversionProcessor that converts from byte[] to String",
                        byte[].class, String.class, new StringConversionProcessor().convertByteArray()}
        };
    }

    /**
     * Test that the method {@code convertValue} raises an {@link IllegalArgumentException} if the target class parameter is null.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConvertValueRaisesExceptionIfItsCalledWithNullTargetClassParam() {
        //GIVEN

        //WHEN
        underTest.convertValue(ZERO, null);
    }

    /**
     * Tests that the method {@code convertValue} returns the expected converted value.
     * @param testCaseDescription the test case description
     * @param valueToConvert the value to convert
     * @param targetClass the destination class
     * @param expectedValue the expected result
     * @param <T> the value to convert class type
     */
    @Test(dataProvider = "dataConvertValueTesting")
    public <T> void testConvertValueWorksProperly(final String testCaseDescription, final T valueToConvert,
        final Class<?> targetClass, final Object expectedValue) {
        // GIVEN

        // WHEN
        Object actual = underTest.convertValue(valueToConvert, targetClass);

        // THEN
        assertEquals(expectedValue, actual);
    }

    /**
     * Creates the parameters to be used for testing that the method {@code convertValue} returns the expected result.
     * @return parameters to be used for testing that the method {@code convertValue} returns the expected result.
     */
    @DataProvider
    private Object[][] dataConvertValueTesting() {
        return new Object[][]{
                {"Tests that the method returns a null value in case the value to convert is null", null, BigInteger.class, null},
                // boolean conversion test cases
                {"Tests that the method returns a boolean value from a byte", Byte.MIN_VALUE, boolean.class, Boolean.TRUE},
                {"Tests that the method returns a boolean value from a short", Short.MIN_VALUE, boolean.class, Boolean.TRUE},
                {"Tests that the method returns a boolean value from an Integer", Integer.MIN_VALUE, boolean.class, Boolean.TRUE},
                {"Tests that the method returns a boolean value from a Long", Long.MIN_VALUE, boolean.class, Boolean.TRUE},
                {"Tests that the method returns a boolean value from a Float", Float.MIN_VALUE, boolean.class, Boolean.TRUE},
                {"Tests that the method returns a boolean value from a Double", Double.MIN_VALUE, boolean.class, Boolean.TRUE},
                {"Tests that the method returns a boolean value from a char", CHAR_VALUE, boolean.class, Boolean.TRUE},
                {"Tests that the method returns a boolean value from a Boolean", Boolean.TRUE, boolean.class, Boolean.TRUE},
                {"Tests that the method returns a boolean value from a String", String.valueOf(Boolean.FALSE), boolean.class, Boolean.FALSE},
                {"Tests that the method returns a boolean value from a BigInteger", BigInteger.ZERO, boolean.class, Boolean.FALSE},
                {"Tests that the method returns a boolean value from a BigDecimal", BigDecimal.ZERO, boolean.class, Boolean.FALSE},
                {"Tests that the method returns a boolean value from a byte[]", TRUE_AS_BYTE_ARRAY, boolean.class, Boolean.TRUE},
                // byte conversion test cases
                {"Tests that the method returns a byte value from a byte", Byte.MIN_VALUE, byte.class, Byte.MIN_VALUE},
                {"Tests that the method returns a byte value from a short", Short.MIN_VALUE, byte.class, Short.valueOf(Short.MIN_VALUE).byteValue()},
                {"Tests that the method returns a byte value from an Integer", Integer.MIN_VALUE, byte.class, Integer.valueOf(Integer.MIN_VALUE).byteValue()},
                {"Tests that the method returns a byte value from a Long", Long.MIN_VALUE, byte.class, Long.valueOf(Long.MIN_VALUE).byteValue()},
                {"Tests that the method returns a byte value from a Float", Float.MIN_VALUE, byte.class, Float.valueOf(Float.MIN_VALUE).byteValue()},
                {"Tests that the method returns a byte value from a Double", Double.MIN_VALUE, byte.class, Double.valueOf(Double.MIN_VALUE).byteValue()},
                {"Tests that the method returns a byte value from a char", CHAR_VALUE, byte.class, (byte) Character.valueOf(CHAR_VALUE).charValue()},
                {"Tests that the method returns a byte value from a Boolean", Boolean.TRUE, byte.class, TRUE_AS_BYTE},
                {"Tests that the method returns a byte value from a String", ONE_AS_STRING, byte.class, Byte.valueOf(ONE_AS_STRING)},
                {"Tests that the method returns a byte value from a BigInteger", BigInteger.ZERO, byte.class, BigInteger.ZERO.byteValue()},
                {"Tests that the method returns a byte value from a BigDecimal", BigDecimal.ZERO, byte.class, BigDecimal.ZERO.byteValue()},
                {"Tests that the method returns a byte value from a byte[]", TRUE_AS_BYTE_ARRAY, byte.class, TRUE_AS_BYTE},
                // char conversion test cases
                {"Tests that the method returns a char value from a byte", Byte.MIN_VALUE, char.class, (char) Byte.MIN_VALUE},
                {"Tests that the method returns a char value from a short", Short.MIN_VALUE, char.class, (char) Short.MIN_VALUE},
                {"Tests that the method returns a char value from an Integer", Integer.MIN_VALUE, char.class, (char) Integer.MIN_VALUE},
                {"Tests that the method returns a char value from a Long", Long.MIN_VALUE, char.class, (char) Long.MIN_VALUE},
                {"Tests that the method returns a char value from a Float", Float.MIN_VALUE, char.class, (char) Float.MIN_VALUE},
                {"Tests that the method returns a char value from a Double", Double.MIN_VALUE, char.class, (char) Double.MIN_VALUE},
                {"Tests that the method returns a char value from a char", CHAR_VALUE, char.class, CHAR_VALUE},
                {"Tests that the method returns a char value from a Boolean", Boolean.TRUE, char.class, CHAR_VALUE},
                {"Tests that the method returns a char value from a String", ONE_AS_STRING, char.class, ONE_AS_STRING.charAt(0)},
                {"Tests that the method returns a char value from a BigInteger", BigInteger.ZERO, char.class, BIG_INTEGER_ZERO_AS_CHAR},
                {"Tests that the method returns a char value from a BigDecimal", BigDecimal.ZERO, char.class, BIG_DECIMAL_ZERO_AS_CHAR},
                {"Tests that the method returns a char value from a byte[]", BYTE_ARRAY, char.class, wrap(BYTE_ARRAY).getChar()},
                // double conversion test cases
                {"Tests that the method returns a double value from a byte", Byte.MIN_VALUE, double.class, Byte.valueOf(Byte.MIN_VALUE).doubleValue()},
                {"Tests that the method returns a double value from a short", Short.MIN_VALUE, double.class, Short.valueOf(Short.MIN_VALUE).doubleValue()},
                {"Tests that the method returns a double value from an Integer", Integer.MIN_VALUE, double.class, Integer.valueOf(Integer.MIN_VALUE).doubleValue()},
                {"Tests that the method returns a double value from a Long", Long.MIN_VALUE, double.class, Long.valueOf(Long.MIN_VALUE).doubleValue()},
                {"Tests that the method returns a double value from a Float", Float.MIN_VALUE, double.class, Float.valueOf(Float.MIN_VALUE).doubleValue()},
                {"Tests that the method returns a double value from a Double", Double.MIN_VALUE, double.class, Double.MIN_VALUE},
                {"Tests that the method returns a double value from a char", CHAR_INT_VALUE, double.class, Double.valueOf(String.valueOf(CHAR_INT_VALUE))},
                {"Tests that the method returns a double value from a Boolean", Boolean.TRUE, double.class, (double) TRUE_AS_INT},
                {"Tests that the method returns a double value from a String", ONE_AS_STRING, double.class, Double.valueOf(ONE_AS_STRING)},
                {"Tests that the method returns a double value from a BigInteger", BigInteger.ZERO, double.class, BigInteger.ZERO.doubleValue()},
                {"Tests that the method returns a double value from a BigDecimal", BigDecimal.ZERO, double.class, BigDecimal.ZERO.doubleValue()},
                {"Tests that the method returns a double value from a byte[]", BYTE_ARRAY, double.class, wrap(BYTE_ARRAY).getDouble()},
                // float conversion test cases
                {"Tests that the method returns a float value from a byte", Byte.MIN_VALUE, float.class, Byte.valueOf(Byte.MIN_VALUE).floatValue()},
                {"Tests that the method returns a float value from a short", Short.MIN_VALUE, float.class, Short.valueOf(Short.MIN_VALUE).floatValue()},
                {"Tests that the method returns a float value from an Integer", Integer.MIN_VALUE, float.class, Integer.valueOf(Integer.MIN_VALUE).floatValue()},
                {"Tests that the method returns a float value from a Long", Long.MIN_VALUE, float.class, Long.valueOf(Long.MIN_VALUE).floatValue()},
                {"Tests that the method returns a float value from a Float", Float.MIN_VALUE, float.class, Float.MIN_VALUE},
                {"Tests that the method returns a float value from a Double", Double.MIN_VALUE, float.class, Double.valueOf(Double.MIN_VALUE).floatValue()},
                {"Tests that the method returns a float value from a char", CHAR_INT_VALUE, float.class, (float) getNumericValue(CHAR_INT_VALUE)},
                {"Tests that the method returns a float value from a Boolean", Boolean.TRUE, float.class, (float) TRUE_AS_INT},
                {"Tests that the method returns a float value from a String", ONE_AS_STRING, float.class, Float.valueOf(ONE_AS_STRING)},
                {"Tests that the method returns a float value from a BigInteger", BigInteger.ZERO, float.class, BigInteger.ZERO.floatValue()},
                {"Tests that the method returns a float value from a BigDecimal", BigDecimal.ZERO, float.class, BigDecimal.ZERO.floatValue()},
                {"Tests that the method returns a float value from a byte[]", BYTE_ARRAY, float.class, wrap(BYTE_ARRAY).getFloat()},
                // integer conversion test cases
                {"Tests that the method returns an int value from a byte", Byte.MIN_VALUE, int.class, Byte.valueOf(Byte.MIN_VALUE).intValue()},
                {"Tests that the method returns an int value from a short", Short.MIN_VALUE, int.class, Short.valueOf(Short.MIN_VALUE).intValue()},
                {"Tests that the method returns an int value from an Integer", Integer.MIN_VALUE, int.class, Integer.MIN_VALUE},
                {"Tests that the method returns an int value from a Long", Long.MIN_VALUE, int.class, Long.valueOf(Long.MIN_VALUE).intValue()},
                {"Tests that the method returns an int value from a Float", Float.MIN_VALUE, int.class, Float.valueOf(Float.MIN_VALUE).intValue()},
                {"Tests that the method returns an int value from a Double", Double.MIN_VALUE, int.class, Double.valueOf(Double.MIN_VALUE).intValue()},
                {"Tests that the method returns an int value from a char", CHAR_INT_VALUE, int.class, getNumericValue(CHAR_INT_VALUE)},
                {"Tests that the method returns an int value from a Boolean", Boolean.TRUE, int.class, TRUE_AS_INT},
                {"Tests that the method returns an int value from a String", ONE_AS_STRING, int.class, Integer.valueOf(ONE_AS_STRING)},
                {"Tests that the method returns an int value from a BigInteger", BigInteger.ZERO, int.class, BigInteger.ZERO.intValue()},
                {"Tests that the method returns an int value from a BigDecimal", BigDecimal.ZERO, int.class, BigDecimal.ZERO.intValue()},
                {"Tests that the method returns a int value from a byte[]", BYTE_ARRAY, int.class, wrap(BYTE_ARRAY).getInt()},
                // long conversion test cases
                {"Tests that the method returns a long value from a byte", Byte.MIN_VALUE, long.class, Byte.valueOf(Byte.MIN_VALUE).longValue()},
                {"Tests that the method returns a long value from a short", Short.MIN_VALUE, long.class, Short.valueOf(Short.MIN_VALUE).longValue()},
                {"Tests that the method returns a long value from an Integer", Integer.MIN_VALUE, long.class, Integer.valueOf(Integer.MIN_VALUE).longValue()},
                {"Tests that the method returns a long value from a Long", Long.MIN_VALUE, long.class, Long.MIN_VALUE},
                {"Tests that the method returns a long value from a Float", Float.MIN_VALUE, long.class, Float.valueOf(Float.MIN_VALUE).longValue()},
                {"Tests that the method returns a long value from a Double", Double.MIN_VALUE, long.class, Double.valueOf(Double.MIN_VALUE).longValue()},
                {"Tests that the method returns a long value from a char", CHAR_INT_VALUE, long.class, (long) getNumericValue(CHAR_INT_VALUE)},
                {"Tests that the method returns a long value from a Boolean", Boolean.TRUE, long.class, (long) TRUE_AS_INT},
                {"Tests that the method returns a long value from a String", ONE_AS_STRING, long.class, Long.valueOf(ONE_AS_STRING)},
                {"Tests that the method returns a long value from a BigInteger", BigInteger.ZERO, long.class, BigInteger.ZERO.longValue()},
                {"Tests that the method returns a long value from a BigDecimal", BigDecimal.ZERO, long.class, BigDecimal.ZERO.longValue()},
                {"Tests that the method returns a long value from a byte[]", BYTE_ARRAY, long.class, wrap(BYTE_ARRAY).getLong()},
                // short conversion test cases
                {"Tests that the method returns a short value from a byte", Byte.MIN_VALUE, short.class, Byte.valueOf(Byte.MIN_VALUE).shortValue()},
                {"Tests that the method returns a short value from a short", Short.MIN_VALUE, short.class, Short.MIN_VALUE},
                {"Tests that the method returns a short value from an Integer", Integer.MIN_VALUE, short.class, Integer.valueOf(Integer.MIN_VALUE).shortValue()},
                {"Tests that the method returns a short value from a Long", Long.MIN_VALUE, short.class, Long.valueOf(Long.MIN_VALUE).shortValue()},
                {"Tests that the method returns a short value from a Float", Float.MIN_VALUE, short.class, Float.valueOf(Float.MIN_VALUE).shortValue()},
                {"Tests that the method returns a short value from a Double", Double.MIN_VALUE, short.class, Double.valueOf(Double.MIN_VALUE).shortValue()},
                {"Tests that the method returns a short value from a char", CHAR_INT_VALUE, short.class, (short) getNumericValue(CHAR_INT_VALUE)},
                {"Tests that the method returns a short value from a Boolean", Boolean.TRUE, short.class, (short) TRUE_AS_INT},
                {"Tests that the method returns a short value from a String", ONE_AS_STRING, short.class, Short.valueOf(ONE_AS_STRING)},
                {"Tests that the method returns a short value from a BigInteger", BigInteger.ZERO, short.class, BigInteger.ZERO.shortValue()},
                {"Tests that the method returns a short value from a BigDecimal", BigDecimal.ZERO, short.class, BigDecimal.ZERO.shortValue()},
                {"Tests that the method returns a short value from a byte[]", BYTE_ARRAY, short.class, wrap(BYTE_ARRAY).getShort()},
                // string conversion test cases
                {"Tests that the method returns a String value from a byte", Byte.MIN_VALUE, String.class, Byte.valueOf(Byte.MIN_VALUE).toString()},
                {"Tests that the method returns a String value from a short", Short.MIN_VALUE, String.class, Short.valueOf(Short.MIN_VALUE).toString()},
                {"Tests that the method returns a String value from an Integer", Integer.MIN_VALUE, String.class, String.valueOf(Integer.MIN_VALUE)},
                {"Tests that the method returns a String value from a Long", Long.MIN_VALUE, String.class, String.valueOf(Long.MIN_VALUE)},
                {"Tests that the method returns a String value from a Float", Float.MIN_VALUE, String.class, String.valueOf(Float.MIN_VALUE)},
                {"Tests that the method returns a String value from a Double", Double.MIN_VALUE, String.class, String.valueOf(Double.MIN_VALUE)},
                {"Tests that the method returns a String value from a char", CHAR_INT_VALUE, String.class, String.valueOf(CHAR_INT_VALUE)},
                {"Tests that the method returns a String value from a Boolean", Boolean.TRUE, String.class, String.valueOf(Boolean.TRUE)},
                {"Tests that the method returns a String value from a String", ONE_AS_STRING, String.class, ONE_AS_STRING},
                {"Tests that the method returns a String value from a BigInteger", BigInteger.ZERO, String.class, BigInteger.ZERO.toString()},
                {"Tests that the method returns a String value from a BigDecimal", BigDecimal.ZERO, String.class, BigDecimal.ZERO.toPlainString()},
                {"Tests that the method returns a String value from a byte[]", TRUE_AS_BYTE_ARRAY, String.class, new String(TRUE_AS_BYTE_ARRAY)},
                // BigInteger conversion test cases
                {"Tests that the method returns a BigInteger value from a byte", Byte.MIN_VALUE, BigInteger.class, BigInteger.valueOf(Byte.valueOf(Byte.MIN_VALUE).longValue())},
                {"Tests that the method returns a BigInteger value from a short", Short.MIN_VALUE, BigInteger.class, BigInteger.valueOf(Short.MIN_VALUE)},
                {"Tests that the method returns a BigInteger value from an Integer", Integer.MIN_VALUE, BigInteger.class, BigInteger.valueOf(Integer.MIN_VALUE)},
                {"Tests that the method returns a BigInteger value from a Long", Long.MIN_VALUE, BigInteger.class, BigInteger.valueOf(Long.MIN_VALUE)},
                {"Tests that the method returns a BigInteger value from a Float", Float.MIN_VALUE, BigInteger.class, BigInteger.valueOf(Float.valueOf(Float.MIN_VALUE).intValue())},
                {"Tests that the method returns a BigInteger value from a Double", Double.MIN_VALUE, BigInteger.class,
                        BigInteger.valueOf(Double.valueOf(Double.MIN_VALUE).intValue())},
                {"Tests that the method returns a BigInteger value from a char", CHAR_INT_VALUE, BigInteger.class, BigInteger.valueOf(getNumericValue(CHAR_INT_VALUE))},
                {"Tests that the method returns a BigInteger value from a Boolean", Boolean.TRUE, BigInteger.class, BigInteger.valueOf(TRUE_AS_INT)},
                {"Tests that the method returns a BigInteger value from a String", ONE_AS_STRING, BigInteger.class, new BigInteger(ONE_AS_STRING)},
                {"Tests that the method returns a BigInteger value from a BigInteger", BigInteger.ZERO, BigInteger.class, BigInteger.ZERO},
                {"Tests that the method returns a BigInteger value from a BigDecimal", BigDecimal.ZERO, BigInteger.class, BigDecimal.ZERO.toBigInteger()},
                {"Tests that the method returns a BigInteger value from a byte[]", BYTE_ARRAY, BigInteger.class, BigInteger.valueOf(wrap(BYTE_ARRAY).getLong())},
                // BigDecimal conversion test cases
                {"Tests that the method returns a BigDecimal value from a byte", Byte.MIN_VALUE, BigDecimal.class, BigDecimal.valueOf(Byte.valueOf(Byte.MIN_VALUE).doubleValue())},
                {"Tests that the method returns a BigDecimal value from a short", Short.MIN_VALUE, BigDecimal.class, BigDecimal.valueOf(Short.MIN_VALUE)},
                {"Tests that the method returns a BigDecimal value from an Integer", Integer.MIN_VALUE, BigDecimal.class, BigDecimal.valueOf(Integer.MIN_VALUE)},
                {"Tests that the method returns a BigDecimal value from a Long", Long.MIN_VALUE, BigDecimal.class, BigDecimal.valueOf(Long.MIN_VALUE)},
                {"Tests that the method returns a BigDecimal value from a Float", Float.MIN_VALUE, BigDecimal.class, BigDecimal.valueOf(Float.MIN_VALUE)},
                {"Tests that the method returns a BigDecimal value from a Double", Double.MIN_VALUE, BigDecimal.class, BigDecimal.valueOf(Double.MIN_VALUE)},
                {"Tests that the method returns a BigDecimal value from a char", CHAR_INT_VALUE, BigDecimal.class, BigDecimal.valueOf(getNumericValue(CHAR_INT_VALUE))},
                {"Tests that the method returns a BigDecimal value from a Boolean", Boolean.TRUE, BigDecimal.class, BigDecimal.valueOf(TRUE_AS_INT)},
                {"Tests that the method returns a BigDecimal value from a String", ONE_AS_STRING, BigDecimal.class, new BigDecimal(ONE_AS_STRING)},
                {"Tests that the method returns a BigDecimal value from a BigInteger", BigInteger.ZERO, BigDecimal.class, BigDecimal.valueOf(BigInteger.ZERO.intValue())},
                {"Tests that the method returns a BigDecimal value from a BigDecimal", BigDecimal.ZERO, BigDecimal.class, BigDecimal.ZERO},
                {"Tests that the method returns a BigDecimal value from a byte[]", BYTE_ARRAY, BigDecimal.class, BigDecimal.valueOf(wrap(BYTE_ARRAY).getDouble())}
        };
    }

    /**
     * Tests that the method {@code convertValue} returns the expected converted byte[] value.
     * @param testCaseDescription the test case description
     * @param valueToConvert the value to convert
     * @param expectedValue the expected result
     * @param <T> the value to convert class type
     */
    @Test(dataProvider = "dataConvertByteArrayValueTesting")
    public <T> void testConvertByteArrayValueWorksProperly(final String testCaseDescription, final T valueToConvert, final byte[] expectedValue) {
        // GIVEN

        // WHEN
        byte[] actual = (byte[]) underTest.convertValue(valueToConvert, byte[].class);

        // THEN
        assertArrayEquals(expectedValue, actual);
    }

    /**
     * Creates the parameters to be used for testing that the method {@code convertValue} returns the expected result for byte[] type.
     * @return parameters to be used for testing that the method {@code convertValue} returns the expected result for byte[] type.
     */
    @DataProvider
    private Object[][] dataConvertByteArrayValueTesting() {
        return new Object[][]{
                {"Tests that the method returns a byte[] value from a byte", Byte.MIN_VALUE, new byte[] {Byte.MIN_VALUE}},
                {"Tests that the method returns a byte[] value from a byte[]", TRUE_AS_BYTE_ARRAY, TRUE_AS_BYTE_ARRAY},
                {"Tests that the method returns a byte[] value from a short", Short.MIN_VALUE, new byte[] {Short.valueOf(Short.MIN_VALUE).byteValue()}},
                {"Tests that the method returns a byte[] value from an Integer", Integer.MIN_VALUE, new byte[] {Integer.valueOf(Integer.MIN_VALUE).byteValue()}},
                {"Tests that the method returns a byte[] value from a Long", Long.MIN_VALUE, new byte[] {Long.valueOf(Long.MIN_VALUE).byteValue()}},
                {"Tests that the method returns a byte[] value from a Float", Float.MIN_VALUE, new byte[] {Float.valueOf(Float.MIN_VALUE).byteValue()}},
                {"Tests that the method returns a byte[] value from a Double", Double.MIN_VALUE, new byte[] {Double.valueOf(Double.MIN_VALUE).byteValue()}},
                {"Tests that the method returns a byte[] value from a char", CHAR_VALUE, new byte[] {(byte) CHAR_VALUE}},
                {"Tests that the method returns a byte[] value from a Boolean", Boolean.TRUE, TRUE_AS_BYTE_ARRAY},
                {"Tests that the method returns a byte[] value from a String", ONE_AS_STRING, ONE_AS_STRING.getBytes()},
                {"Tests that the method returns a byte[] value from a BigInteger", BigInteger.ZERO, new byte[] {BigInteger.ZERO.byteValue()}},
                {"Tests that the method returns a byte[] value from a BigDecimal", BigDecimal.ZERO, new byte[] {BigDecimal.ZERO.byteValue()}}
        };
    }

    /**
     * Tests that the method {@code convertValue} raises a {@link TypeConversionException}.
     */
    @Test(expectedExceptions = TypeConversionException.class)
    public void testConvertValueRaisesExceptionInCaseNoConverterIsDefined() {
        // GIVEN

        // WHEN
        underTest.convertValue(ONE_AS_STRING, Void.class);
    }
}
