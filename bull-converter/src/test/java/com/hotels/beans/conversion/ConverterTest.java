/**
 * Copyright (C) 2019-2020 Expedia, Inc.
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.openMocks;

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
public class ConverterTest extends AbstractConversionTest {
    private static final String ONE_AS_STRING = "1";
    private static final Character TRUE_AS_CHAR_VALUE = 'T';
    private static final Byte TRUE_AS_BYTE = 1;
    private static final int TRUE_AS_INT = 1;
    private static final char BIG_DECIMAL_ZERO_AS_CHAR = (char) BigDecimal.ZERO.doubleValue();
    private static final char BIG_INTEGER_ZERO_AS_CHAR = (char) BigInteger.ZERO.intValue();

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
        openMocks(this);
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
        assertThat(actual).isEmpty();
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
    @SuppressWarnings("unchecked")
    @Test(dataProvider = "dataGetConversionFunctionTesting")
    public void testGetConversionFunctionReturnsTheExpectedConversionFunction(final String testCaseDescription, final Class<?> sourceFieldType,
        final Class<?> destinationFieldType, final Function<?, ?> expectedConversionFunction) {
        // GIVEN

        // WHEN
        Optional<Function<Object, Object>> actual = underTest.getConversionFunction(sourceFieldType, destinationFieldType);

        // THEN
        assertThat(actual).contains((Function<Object, Object>) expectedConversionFunction);
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
        assertThat(actual).isEqualTo(expectedValue);
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
            {"Tests that the method returns a boolean value from a byte", BYTE_VALUE, boolean.class, BOOLEAN_VALUE},
            {"Tests that the method returns a boolean value from a short", SHORT_VALUE, boolean.class, BOOLEAN_VALUE},
            {"Tests that the method returns a boolean value from an Integer", INTEGER_VALUE, boolean.class, BOOLEAN_VALUE},
            {"Tests that the method returns a boolean value from a Long", LONG_VALUE, boolean.class, BOOLEAN_VALUE},
            {"Tests that the method returns a boolean value from a Float", FLOAT_VALUE, boolean.class, BOOLEAN_VALUE},
            {"Tests that the method returns a boolean value from a Double", DOUBLE_VALUE, boolean.class, BOOLEAN_VALUE},
            {"Tests that the method returns a boolean value from a char", TRUE_AS_CHAR_VALUE, boolean.class, BOOLEAN_VALUE},
            {"Tests that the method returns a boolean value from a Boolean", BOOLEAN_VALUE, boolean.class, BOOLEAN_VALUE},
            {"Tests that the method returns a boolean value from a String", TRUE_AS_STRING, boolean.class, BOOLEAN_VALUE},
            {"Tests that the method returns a boolean value from a BigInteger", BigInteger.ZERO, boolean.class, Boolean.FALSE},
            {"Tests that the method returns a boolean value from a BigDecimal", BigDecimal.ZERO, boolean.class, Boolean.FALSE},
            {"Tests that the method returns a boolean value from a byte[]", EIGHT_BYTE_BYTE_ARRAY, boolean.class, BOOLEAN_VALUE},
            // byte conversion test cases
            {"Tests that the method returns a byte value from a byte", BYTE_VALUE, byte.class, BYTE_VALUE},
            {"Tests that the method returns a byte value from a short", SHORT_VALUE, byte.class, SHORT_VALUE.byteValue()},
            {"Tests that the method returns a byte value from an Integer", INTEGER_VALUE, byte.class, INTEGER_VALUE.byteValue()},
            {"Tests that the method returns a byte value from a Long", LONG_VALUE, byte.class, LONG_VALUE.byteValue()},
            {"Tests that the method returns a byte value from a Float", FLOAT_VALUE, byte.class, FLOAT_VALUE.byteValue()},
            {"Tests that the method returns a byte value from a Double", DOUBLE_VALUE, byte.class, DOUBLE_VALUE.byteValue()},
            {"Tests that the method returns a byte value from a char", TRUE_AS_CHAR_VALUE, byte.class, (byte) TRUE_AS_CHAR_VALUE.charValue()},
            {"Tests that the method returns a byte value from a Boolean", BOOLEAN_VALUE, byte.class, TRUE_AS_BYTE},
            {"Tests that the method returns a byte value from a String", ONE_AS_STRING, byte.class, Byte.valueOf(ONE_AS_STRING)},
            {"Tests that the method returns a byte value from a BigInteger", BigInteger.ZERO, byte.class, BigInteger.ZERO.byteValue()},
            {"Tests that the method returns a byte value from a BigDecimal", BigDecimal.ZERO, byte.class, BigDecimal.ZERO.byteValue()},
            {"Tests that the method returns a byte value from a byte[]", ONE_BYTE_BYTE_ARRAY, byte.class, TRUE_AS_BYTE},
            // char conversion test cases
            {"Tests that the method returns a char value from a byte", BYTE_VALUE, char.class, (char) BYTE_VALUE.byteValue()},
            {"Tests that the method returns a char value from a short", SHORT_VALUE, char.class, (char) SHORT_VALUE.byteValue()},
            {"Tests that the method returns a char value from an Integer", INTEGER_VALUE, char.class, (char) INTEGER_VALUE.byteValue()},
            {"Tests that the method returns a char value from a Long", LONG_VALUE, char.class, (char) LONG_VALUE.byteValue()},
            {"Tests that the method returns a char value from a Float", FLOAT_VALUE, char.class, (char) FLOAT_VALUE.byteValue()},
            {"Tests that the method returns a char value from a Double", DOUBLE_VALUE, char.class, (char) DOUBLE_VALUE.byteValue()},
            {"Tests that the method returns a char value from a char", TRUE_AS_CHAR_VALUE, char.class, TRUE_AS_CHAR_VALUE},
            {"Tests that the method returns a char value from a Boolean", BOOLEAN_VALUE, char.class, TRUE_AS_CHAR_VALUE},
            {"Tests that the method returns a char value from a String", ONE_AS_STRING, char.class, ONE_AS_STRING.charAt(0)},
            {"Tests that the method returns a char value from a BigInteger", BigInteger.ZERO, char.class, BIG_INTEGER_ZERO_AS_CHAR},
            {"Tests that the method returns a char value from a BigDecimal", BigDecimal.ZERO, char.class, BIG_DECIMAL_ZERO_AS_CHAR},
            {"Tests that the method returns a char value from a byte[]", EIGHT_BYTE_BYTE_ARRAY, char.class, wrap(EIGHT_BYTE_BYTE_ARRAY).getChar()},
            // double conversion test cases
            {"Tests that the method returns a double value from a byte", BYTE_VALUE, double.class, BYTE_VALUE.doubleValue()},
            {"Tests that the method returns a double value from a short", SHORT_VALUE, double.class, SHORT_VALUE.doubleValue()},
            {"Tests that the method returns a double value from an Integer", INTEGER_VALUE, double.class, INTEGER_VALUE.doubleValue()},
            {"Tests that the method returns a double value from a Long", LONG_VALUE, double.class, LONG_VALUE.doubleValue()},
            {"Tests that the method returns a double value from a Float", FLOAT_VALUE, double.class, FLOAT_VALUE.doubleValue()},
            {"Tests that the method returns a double value from a Double", DOUBLE_VALUE, double.class, DOUBLE_VALUE},
            {"Tests that the method returns a double value from a char", CHAR_VALUE, double.class, Double.valueOf(String.valueOf(CHAR_VALUE))},
            {"Tests that the method returns a double value from a Boolean", BOOLEAN_VALUE, double.class, (double) TRUE_AS_INT},
            {"Tests that the method returns a double value from a String", ONE_AS_STRING, double.class, Double.valueOf(ONE_AS_STRING)},
            {"Tests that the method returns a double value from a BigInteger", BigInteger.ZERO, double.class, BigInteger.ZERO.doubleValue()},
            {"Tests that the method returns a double value from a BigDecimal", BigDecimal.ZERO, double.class, BigDecimal.ZERO.doubleValue()},
            {"Tests that the method returns a double value from a byte[]", EIGHT_BYTE_BYTE_ARRAY, double.class, wrap(EIGHT_BYTE_BYTE_ARRAY).getDouble()},
            // float conversion test cases
            {"Tests that the method returns a float value from a byte", BYTE_VALUE, float.class, BYTE_VALUE.floatValue()},
            {"Tests that the method returns a float value from a short", SHORT_VALUE, float.class, SHORT_VALUE.floatValue()},
            {"Tests that the method returns a float value from an Integer", INTEGER_VALUE, float.class, INTEGER_VALUE.floatValue()},
            {"Tests that the method returns a float value from a Long", LONG_VALUE, float.class, LONG_VALUE.floatValue()},
            {"Tests that the method returns a float value from a Float", FLOAT_VALUE, float.class, FLOAT_VALUE},
            {"Tests that the method returns a float value from a Double", DOUBLE_VALUE, float.class, DOUBLE_VALUE.floatValue()},
            {"Tests that the method returns a float value from a char", CHAR_VALUE, float.class, (float) getNumericValue(CHAR_VALUE)},
            {"Tests that the method returns a float value from a Boolean", BOOLEAN_VALUE, float.class, (float) TRUE_AS_INT},
            {"Tests that the method returns a float value from a String", ONE_AS_STRING, float.class, Float.valueOf(ONE_AS_STRING)},
            {"Tests that the method returns a float value from a BigInteger", BigInteger.ZERO, float.class, BigInteger.ZERO.floatValue()},
            {"Tests that the method returns a float value from a BigDecimal", BigDecimal.ZERO, float.class, BigDecimal.ZERO.floatValue()},
            {"Tests that the method returns a float value from a byte[]", EIGHT_BYTE_BYTE_ARRAY, float.class, wrap(EIGHT_BYTE_BYTE_ARRAY).getFloat()},
            // integer conversion test cases
            {"Tests that the method returns an int value from a byte", BYTE_VALUE, int.class, BYTE_VALUE.intValue()},
            {"Tests that the method returns an int value from a short", SHORT_VALUE, int.class, SHORT_VALUE.intValue()},
            {"Tests that the method returns an int value from an Integer", INTEGER_VALUE, int.class, INTEGER_VALUE},
            {"Tests that the method returns an int value from a Long", LONG_VALUE, int.class, LONG_VALUE.intValue()},
            {"Tests that the method returns an int value from a Float", FLOAT_VALUE, int.class, FLOAT_VALUE.intValue()},
            {"Tests that the method returns an int value from a Double", DOUBLE_VALUE, int.class, DOUBLE_VALUE.intValue()},
            {"Tests that the method returns an int value from a char", CHAR_VALUE, int.class, getNumericValue(CHAR_VALUE)},
            {"Tests that the method returns an int value from a Boolean", BOOLEAN_VALUE, int.class, TRUE_AS_INT},
            {"Tests that the method returns an int value from a String", ONE_AS_STRING, int.class, Integer.valueOf(ONE_AS_STRING)},
            {"Tests that the method returns an int value from a BigInteger", BigInteger.ZERO, int.class, BigInteger.ZERO.intValue()},
            {"Tests that the method returns an int value from a BigDecimal", BigDecimal.ZERO, int.class, BigDecimal.ZERO.intValue()},
            {"Tests that the method returns a int value from a byte[]", EIGHT_BYTE_BYTE_ARRAY, int.class, wrap(EIGHT_BYTE_BYTE_ARRAY).getInt()},
            // long conversion test cases
            {"Tests that the method returns a long value from a byte", BYTE_VALUE, long.class, BYTE_VALUE.longValue()},
            {"Tests that the method returns a long value from a short", SHORT_VALUE, long.class, SHORT_VALUE.longValue()},
            {"Tests that the method returns a long value from an Integer", INTEGER_VALUE, long.class, INTEGER_VALUE.longValue()},
            {"Tests that the method returns a long value from a Long", LONG_VALUE, long.class, LONG_VALUE},
            {"Tests that the method returns a long value from a Float", FLOAT_VALUE, long.class, FLOAT_VALUE.longValue()},
            {"Tests that the method returns a long value from a Double", DOUBLE_VALUE, long.class, DOUBLE_VALUE.longValue()},
            {"Tests that the method returns a long value from a char", CHAR_VALUE, long.class, (long) getNumericValue(CHAR_VALUE)},
            {"Tests that the method returns a long value from a Boolean", BOOLEAN_VALUE, long.class, (long) TRUE_AS_INT},
            {"Tests that the method returns a long value from a String", ONE_AS_STRING, long.class, Long.valueOf(ONE_AS_STRING)},
            {"Tests that the method returns a long value from a BigInteger", BigInteger.ZERO, long.class, BigInteger.ZERO.longValue()},
            {"Tests that the method returns a long value from a BigDecimal", BigDecimal.ZERO, long.class, BigDecimal.ZERO.longValue()},
            {"Tests that the method returns a long value from a byte[]", EIGHT_BYTE_BYTE_ARRAY, long.class, wrap(EIGHT_BYTE_BYTE_ARRAY).getLong()},
            // short conversion test cases
            {"Tests that the method returns a short value from a byte", BYTE_VALUE, short.class, BYTE_VALUE.shortValue()},
            {"Tests that the method returns a short value from a short", SHORT_VALUE, short.class, SHORT_VALUE},
            {"Tests that the method returns a short value from an Integer", INTEGER_VALUE, short.class, INTEGER_VALUE.shortValue()},
            {"Tests that the method returns a short value from a Long", LONG_VALUE, short.class, LONG_VALUE.shortValue()},
            {"Tests that the method returns a short value from a Float", FLOAT_VALUE, short.class, FLOAT_VALUE.shortValue()},
            {"Tests that the method returns a short value from a Double", DOUBLE_VALUE, short.class, DOUBLE_VALUE.shortValue()},
            {"Tests that the method returns a short value from a char", CHAR_VALUE, short.class, (short) getNumericValue(CHAR_VALUE)},
            {"Tests that the method returns a short value from a Boolean", BOOLEAN_VALUE, short.class, (short) TRUE_AS_INT},
            {"Tests that the method returns a short value from a String", ONE_AS_STRING, short.class, Short.valueOf(ONE_AS_STRING)},
            {"Tests that the method returns a short value from a BigInteger", BigInteger.ZERO, short.class, BigInteger.ZERO.shortValue()},
            {"Tests that the method returns a short value from a BigDecimal", BigDecimal.ZERO, short.class, BigDecimal.ZERO.shortValue()},
            {"Tests that the method returns a short value from a byte[]", EIGHT_BYTE_BYTE_ARRAY, short.class, wrap(EIGHT_BYTE_BYTE_ARRAY).getShort()},
            // string conversion test cases
            {"Tests that the method returns a String value from a byte", BYTE_VALUE, String.class, BYTE_VALUE.toString()},
            {"Tests that the method returns a String value from a short", SHORT_VALUE, String.class, SHORT_VALUE.toString()},
            {"Tests that the method returns a String value from an Integer", INTEGER_VALUE, String.class, String.valueOf(INTEGER_VALUE)},
            {"Tests that the method returns a String value from a Long", LONG_VALUE, String.class, String.valueOf(LONG_VALUE)},
            {"Tests that the method returns a String value from a Float", FLOAT_VALUE, String.class, String.valueOf(FLOAT_VALUE)},
            {"Tests that the method returns a String value from a Double", DOUBLE_VALUE, String.class, String.valueOf(DOUBLE_VALUE)},
            {"Tests that the method returns a String value from a char", CHAR_VALUE, String.class, String.valueOf(CHAR_VALUE)},
            {"Tests that the method returns a String value from a Boolean", BOOLEAN_VALUE, String.class, String.valueOf(BOOLEAN_VALUE)},
            {"Tests that the method returns a String value from a String", ONE_AS_STRING, String.class, ONE_AS_STRING},
            {"Tests that the method returns a String value from a BigInteger", BigInteger.ZERO, String.class, BigInteger.ZERO.toString()},
            {"Tests that the method returns a String value from a BigDecimal", BigDecimal.ZERO, String.class, BigDecimal.ZERO.toPlainString()},
            {"Tests that the method returns a String value from a byte[]", EIGHT_BYTE_BYTE_ARRAY, String.class, new String(EIGHT_BYTE_BYTE_ARRAY)},
            // BigInteger conversion test cases
            {"Tests that the method returns a BigInteger value from a byte", BYTE_VALUE, BigInteger.class, BigInteger.valueOf(BYTE_VALUE.longValue())},
            {"Tests that the method returns a BigInteger value from a short", SHORT_VALUE, BigInteger.class, BigInteger.valueOf(SHORT_VALUE)},
            {"Tests that the method returns a BigInteger value from an Integer", INTEGER_VALUE, BigInteger.class, BigInteger.valueOf(INTEGER_VALUE)},
            {"Tests that the method returns a BigInteger value from a Long", LONG_VALUE, BigInteger.class, BigInteger.valueOf(LONG_VALUE)},
            {"Tests that the method returns a BigInteger value from a Float", FLOAT_VALUE, BigInteger.class, BigInteger.valueOf(FLOAT_VALUE.intValue())},
            {"Tests that the method returns a BigInteger value from a Double", DOUBLE_VALUE, BigInteger.class, BigInteger.valueOf(DOUBLE_VALUE.intValue())},
            {"Tests that the method returns a BigInteger value from a char", CHAR_VALUE, BigInteger.class, BigInteger.valueOf(getNumericValue(CHAR_VALUE))},
            {"Tests that the method returns a BigInteger value from a Boolean", BOOLEAN_VALUE, BigInteger.class, BigInteger.valueOf(TRUE_AS_INT)},
            {"Tests that the method returns a BigInteger value from a String", ONE_AS_STRING, BigInteger.class, new BigInteger(ONE_AS_STRING)},
            {"Tests that the method returns a BigInteger value from a BigInteger", BigInteger.ZERO, BigInteger.class, BigInteger.ZERO},
            {"Tests that the method returns a BigInteger value from a BigDecimal", BigDecimal.ZERO, BigInteger.class, BigDecimal.ZERO.toBigInteger()},
            {"Tests that the method returns a BigInteger value from a byte[]", EIGHT_BYTE_BYTE_ARRAY, BigInteger.class, BigInteger.valueOf(wrap(EIGHT_BYTE_BYTE_ARRAY).getLong())},
            // BigDecimal conversion test cases
            {"Tests that the method returns a BigDecimal value from a byte", BYTE_VALUE, BigDecimal.class, BigDecimal.valueOf(BYTE_VALUE.doubleValue())},
            {"Tests that the method returns a BigDecimal value from a short", SHORT_VALUE, BigDecimal.class, BigDecimal.valueOf(SHORT_VALUE)},
            {"Tests that the method returns a BigDecimal value from an Integer", INTEGER_VALUE, BigDecimal.class, BigDecimal.valueOf(INTEGER_VALUE)},
            {"Tests that the method returns a BigDecimal value from a Long", LONG_VALUE, BigDecimal.class, BigDecimal.valueOf(LONG_VALUE)},
            {"Tests that the method returns a BigDecimal value from a Float", FLOAT_VALUE, BigDecimal.class, BigDecimal.valueOf(FLOAT_VALUE)},
            {"Tests that the method returns a BigDecimal value from a Double", DOUBLE_VALUE, BigDecimal.class, BigDecimal.valueOf(DOUBLE_VALUE)},
            {"Tests that the method returns a BigDecimal value from a char", CHAR_VALUE, BigDecimal.class, BigDecimal.valueOf(getNumericValue(CHAR_VALUE))},
            {"Tests that the method returns a BigDecimal value from a Boolean", BOOLEAN_VALUE, BigDecimal.class, BigDecimal.valueOf(TRUE_AS_INT)},
            {"Tests that the method returns a BigDecimal value from a String", ONE_AS_STRING, BigDecimal.class, new BigDecimal(ONE_AS_STRING)},
            {"Tests that the method returns a BigDecimal value from a BigInteger", BigInteger.ZERO, BigDecimal.class, BigDecimal.valueOf(BigInteger.ZERO.intValue())},
            {"Tests that the method returns a BigDecimal value from a BigDecimal", BigDecimal.ZERO, BigDecimal.class, BigDecimal.ZERO},
            {"Tests that the method returns a BigDecimal value from a byte[]", EIGHT_BYTE_BYTE_ARRAY, BigDecimal.class, BigDecimal.valueOf(wrap(EIGHT_BYTE_BYTE_ARRAY).getDouble())}
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
        byte[] actual = underTest.convertValue(valueToConvert, byte[].class);

        // THEN
        assertThat(actual).isEqualTo(expectedValue);
    }

    /**
     * Creates the parameters to be used for testing that the method {@code convertValue} returns the expected result for byte[] type.
     * @return parameters to be used for testing that the method {@code convertValue} returns the expected result for byte[] type.
     */
    @DataProvider
    private Object[][] dataConvertByteArrayValueTesting() {
        return new Object[][]{
                {"Tests that the method returns a byte[] value from a byte", BYTE_VALUE, new byte[] {BYTE_VALUE}},
                {"Tests that the method returns a byte[] value from a byte[]", EIGHT_BYTE_BYTE_ARRAY, EIGHT_BYTE_BYTE_ARRAY},
                {"Tests that the method returns a byte[] value from a short", SHORT_VALUE, new byte[] {SHORT_VALUE.byteValue()}},
                {"Tests that the method returns a byte[] value from an Integer", INTEGER_VALUE, new byte[] {INTEGER_VALUE.byteValue()}},
                {"Tests that the method returns a byte[] value from a Long", LONG_VALUE, new byte[] {LONG_VALUE.byteValue()}},
                {"Tests that the method returns a byte[] value from a Float", FLOAT_VALUE, new byte[] {FLOAT_VALUE.byteValue()}},
                {"Tests that the method returns a byte[] value from a Double", DOUBLE_VALUE, new byte[] {DOUBLE_VALUE.byteValue()}},
                {"Tests that the method returns a byte[] value from a char", TRUE_AS_CHAR_VALUE, new byte[] {(byte) TRUE_AS_CHAR_VALUE.charValue()}},
                {"Tests that the method returns a byte[] value from a Boolean", BOOLEAN_VALUE, ONE_BYTE_BYTE_ARRAY},
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
