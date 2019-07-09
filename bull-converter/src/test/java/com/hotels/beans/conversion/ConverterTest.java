package com.hotels.beans.conversion;

import static java.math.BigInteger.ZERO;
import static java.math.BigInteger.valueOf;
import static java.util.Optional.empty;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;
import org.mockito.InjectMocks;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.hotels.beans.conversion.error.NoConverterAvailableException;
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
    private static final char CHAR_VALUE = 'x';
    private static final byte TRUE_AS_BYTE = 1;
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
                {"Tests that the method returns a CharacterConversionProcessor that converts from char to boolean",
                        boolean.class, char.class, new CharacterConversionProcessor().convertBoolean()}
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


                {"Tests that the method returns an int value from a String", ONE_AS_STRING, int.class, Integer.parseInt(ONE_AS_STRING)},
                {"Tests that the method returns a byte value from a String", ONE_AS_STRING, byte.class, Byte.parseByte(ONE_AS_STRING)}
        };
    }

    /**
     * Tests that the method {@code convertValue} raises a {@link NoConverterAvailableException}.
     */
    @Test(expectedExceptions = NoConverterAvailableException.class)
    public void testConvertValueRaisesExpectionInCaseNoConverterIsDefined() {
        // GIVEN

        // WHEN
        underTest.convertValue(ONE_AS_STRING, Void.class);
    }
}
