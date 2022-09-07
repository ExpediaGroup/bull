/**
 * Copyright (C) 2019-2022 Expedia, Inc.
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
package com.expediagroup.beans.conversion

import com.expediagroup.beans.conversion.error.TypeConversionException
import com.expediagroup.beans.conversion.processor.ConversionProcessor
import com.expediagroup.beans.conversion.processor.impl.BigDecimalConversionProcessor
import com.expediagroup.beans.conversion.processor.impl.BigIntegerConversionProcessor
import com.expediagroup.beans.conversion.processor.impl.BooleanConversionProcessor
import com.expediagroup.beans.conversion.processor.impl.ByteConversionProcessor
import com.expediagroup.beans.conversion.processor.impl.CharacterConversionProcessor
import com.expediagroup.beans.conversion.processor.impl.DoubleConversionProcessor
import com.expediagroup.beans.conversion.processor.impl.FloatConversionProcessor
import com.expediagroup.beans.conversion.processor.impl.IntegerConversionProcessor
import com.expediagroup.beans.conversion.processor.impl.StringConversionProcessor
import org.apache.commons.lang3.tuple.Pair
import org.assertj.core.api.Assertions
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations
import org.testng.annotations.BeforeClass
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import java.math.BigDecimal
import java.math.BigInteger
import java.nio.ByteBuffer
import java.util.function.Function

/**
 * Unit test for [Converter].
 */
class ConverterTest : AbstractConversionTest() {
    /**
     * The class to be tested.
     */
    @InjectMocks
    private lateinit var underTest: ConverterImpl

    /**
     * Initializes mock.
     */
    @BeforeClass
    fun beforeClass() {
        MockitoAnnotations.openMocks(this)
    }

    /**
     * Tests that the method `getConversionFunction` returns an empty optional.
     * @param testCaseDescription the test case description
     * @param sourceFieldType source field class
     * @param destinationFieldType the destination field class
     */
    @Test(dataProvider = "dataGetConversionFunctionEmptyCaseTesting")
    fun testGetConversionFunctionReturnsAnEmptyOptional(
        testCaseDescription: String?,
        sourceFieldType: Class<*>?,
        destinationFieldType: Class<*>?
    ) {
        // GIVEN

        // WHEN
        val actual = underTest.getConversionFunction(sourceFieldType, destinationFieldType)

        // THEN
        Assertions.assertThat(actual).isEmpty
    }

    /**
     * Creates the parameters to be used for testing that the method `getConversionFunction` returns an empty Optional.
     * @return parameters to be used for testing that the method `getConversionFunction` returns an empty Optional.
     */
    @DataProvider
    private fun dataGetConversionFunctionEmptyCaseTesting(): Array<Array<Any?>> {
        return arrayOf(
            arrayOf(
                "Tests that the method returns an empty optional in case the source field type is not equal to the source field type but it's not primitive",
                Pair::class.java,
                Int::class.javaPrimitiveType
            ),
            arrayOf(
                "Tests that the method returns an empty optional in case the source field type is not equal to the source field type and the destination type is void",
                Int::class.javaPrimitiveType,
                Void::class.java
            )
        )
    }

    /**
     * Tests that the method `getConversionFunction` returns the expected conversion function.
     * @param testCaseDescription the test case description
     * @param sourceFieldType source field class
     * @param destinationFieldType the destination field class
     * @param expectedConversionFunction the expected [ConversionProcessor] instance
     */
    @Test(dataProvider = "dataGetConversionFunctionTesting")
    fun testGetConversionFunctionReturnsTheExpectedConversionFunction(
        testCaseDescription: String?,
        sourceFieldType: Class<*>?,
        destinationFieldType: Class<*>?,
        expectedConversionFunction: Function<*, *>
    ) {
        // GIVEN

        // WHEN
        val actual = underTest.getConversionFunction(sourceFieldType, destinationFieldType)

        // THEN
        Assertions.assertThat(actual).contains(expectedConversionFunction as Function<Any?, Any?>)
    }

    /**
     * Creates the parameters to be used for testing the method `getConversionFunction`.
     * @return parameters to be used for testing the method `getConversionFunction`.
     */
    @DataProvider
    private fun dataGetConversionFunctionTesting(): Array<Array<Any?>> {
        return arrayOf(
            arrayOf(
                "Tests that the method returns a IntegerConversionProcessor that converts from String to int",
                String::class.java,
                Int::class.javaPrimitiveType,
                IntegerConversionProcessor().convertString()
            ),
            arrayOf(
                "Tests that the method returns a StringConversionProcessor that converts from byte to String",
                Byte::class.javaPrimitiveType,
                String::class.java,
                StringConversionProcessor().convertByte()
            ),
            arrayOf(
                "Tests that the method returns a ShortConversionProcessor that converts from short to byte",
                Short::class.javaPrimitiveType,
                Byte::class.javaPrimitiveType,
                ByteConversionProcessor().convertShort()
            ),
            arrayOf(
                "Tests that the method returns a StringConversionProcessor that converts from int to String",
                Int::class.javaPrimitiveType,
                String::class.java,
                StringConversionProcessor().convertInteger()
            ),
            arrayOf(
                "Tests that the method returns a IntegerConversionProcessor that converts from long to int",
                Long::class.javaPrimitiveType,
                Int::class.javaPrimitiveType,
                IntegerConversionProcessor().convertLong()
            ),
            arrayOf(
                "Tests that the method returns a DoubleConversionProcessor that converts from float to double",
                Float::class.javaPrimitiveType,
                Double::class.javaPrimitiveType,
                DoubleConversionProcessor().convertFloat()
            ),
            arrayOf(
                "Tests that the method returns a FloatConversionProcessor that converts from double to float",
                Double::class.javaPrimitiveType,
                Float::class.javaPrimitiveType,
                FloatConversionProcessor().convertDouble()
            ),
            arrayOf(
                "Tests that the method returns a StringConversionProcessor that converts from char to String",
                Char::class.javaPrimitiveType,
                String::class.java,
                StringConversionProcessor().convertCharacter()
            ),
            arrayOf(
                "Tests that the method returns a BooleanConversionProcessor that converts from boolean to char",
                Char::class.javaPrimitiveType,
                Boolean::class.javaPrimitiveType,
                BooleanConversionProcessor().convertCharacter()
            ),
            arrayOf(
                "Tests that the method returns a CharacterConversionProcessor that converts from char to boolean",
                Boolean::class.javaPrimitiveType,
                Char::class.javaPrimitiveType,
                CharacterConversionProcessor().convertBoolean()
            ),
            arrayOf(
                "Tests that the method returns a CharacterConversionProcessor that converts from BigInteger to BigDecimal",
                BigInteger::class.java,
                BigDecimal::class.java,
                BigDecimalConversionProcessor().convertBigInteger()
            ),
            arrayOf(
                "Tests that the method returns a CharacterConversionProcessor that converts from BigDecimal to BigInteger",
                BigDecimal::class.java,
                BigInteger::class.java,
                BigIntegerConversionProcessor().convertBigDecimal()
            ),
            arrayOf(
                "Tests that the method returns a CharacterConversionProcessor that converts from byte[] to String",
                ByteArray::class.java,
                String::class.java,
                StringConversionProcessor().convertByteArray()
            )
        )
    }

    /**
     * Test that the method `convertValue` raises an [IllegalArgumentException] if the target class parameter is null.
     */
    @Test(expectedExceptions = [IllegalArgumentException::class])
    fun testConvertValueRaisesExceptionIfItsCalledWithNullTargetClassParam() {
        // GIVEN

        // WHEN
        underTest.convertValue<BigInteger, Any>(BigInteger.ZERO, null)
    }

    /**
     * Tests that the method `convertValue` returns the expected converted value.
     * @param testCaseDescription the test case description
     * @param valueToConvert the value to convert
     * @param targetClass the destination class
     * @param expectedValue the expected result
     * @param <T> the value to convert class type
     </T> */
    @Test(dataProvider = "dataConvertValueTesting")
    fun <T> testConvertValueWorksProperly(
        testCaseDescription: String?,
        valueToConvert: T,
        targetClass: Class<*>?,
        expectedValue: Any?
    ) {
        // GIVEN

        // WHEN
        val actual = underTest.convertValue(valueToConvert, targetClass)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expectedValue)
    }

    /**
     * Creates the parameters to be used for testing that the method `convertValue` returns the expected result.
     * @return parameters to be used for testing that the method `convertValue` returns the expected result.
     */
    @DataProvider
    private fun dataConvertValueTesting(): Iterator<Array<Any?>> {
        val testCases: MutableList<Array<Any?>> = ArrayList()
        testCases.add(
            arrayOf(
                "Tests that the method returns a null value in case the value to convert is null",
                null,
                BigInteger::class.java,
                null
            )
        )
        testCases.addAll(dataConvertBooleanValueTesting())
        testCases.addAll(dataConvertByteValueTesting())
        testCases.addAll(dataConvertCharValueTesting())
        testCases.addAll(dataConvertDoubleValueTesting())
        testCases.addAll(dataConvertFloatValueTesting())
        testCases.addAll(dataConvertIntValueTesting())
        testCases.addAll(dataConvertLongValueTesting())
        testCases.addAll(dataConvertShortValueTesting())
        testCases.addAll(dataConvertStringValueTesting())
        testCases.addAll(dataConvertBigIntegerValueTesting())
        testCases.addAll(dataConvertBigDecimalValueTesting())
        return testCases.iterator()
    }

    /**
     * Creates the parameters to be used for testing that the method `convertValue` returns the expected result for boolean type.
     * @return parameters to be used for testing that the method `convertValue` returns the expected result for boolean type.
     */
    private fun dataConvertBooleanValueTesting(): List<Array<Any?>> {
        return listOf(
            arrayOf(
                "Tests that the method returns a boolean value from a byte",
                BYTE_VALUE,
                Boolean::class.javaPrimitiveType,
                BOOLEAN_VALUE
            ),
            arrayOf(
                "Tests that the method returns a boolean value from a short",
                SHORT_VALUE,
                Boolean::class.javaPrimitiveType,
                BOOLEAN_VALUE
            ),
            arrayOf(
                "Tests that the method returns a boolean value from an Integer",
                INTEGER_VALUE,
                Boolean::class.javaPrimitiveType,
                BOOLEAN_VALUE
            ),
            arrayOf(
                "Tests that the method returns a boolean value from a Long",
                LONG_VALUE,
                Boolean::class.javaPrimitiveType,
                BOOLEAN_VALUE
            ),
            arrayOf(
                "Tests that the method returns a boolean value from a Float",
                FLOAT_VALUE,
                Boolean::class.javaPrimitiveType,
                BOOLEAN_VALUE
            ),
            arrayOf(
                "Tests that the method returns a boolean value from a Double",
                DOUBLE_VALUE,
                Boolean::class.javaPrimitiveType,
                BOOLEAN_VALUE
            ),
            arrayOf(
                "Tests that the method returns a boolean value from a char",
                TRUE_AS_CHAR_VALUE,
                Boolean::class.javaPrimitiveType,
                BOOLEAN_VALUE
            ),
            arrayOf(
                "Tests that the method returns a boolean value from a Boolean",
                BOOLEAN_VALUE,
                Boolean::class.javaPrimitiveType,
                BOOLEAN_VALUE
            ),
            arrayOf(
                "Tests that the method returns a boolean value from a String",
                TRUE_AS_STRING,
                Boolean::class.javaPrimitiveType,
                BOOLEAN_VALUE
            ),
            arrayOf(
                "Tests that the method returns a boolean value from a BigInteger",
                BigInteger.ZERO,
                Boolean::class.javaPrimitiveType,
                false
            ),
            arrayOf(
                "Tests that the method returns a boolean value from a BigDecimal",
                BigDecimal.ZERO,
                Boolean::class.javaPrimitiveType,
                false
            ),
            arrayOf(
                "Tests that the method returns a boolean value from a byte[]",
                EIGHT_BYTE_BYTE_ARRAY,
                Boolean::class.javaPrimitiveType,
                BOOLEAN_VALUE
            )
        )
    }

    /**
     * Creates the parameters to be used for testing that the method `convertValue` returns the expected result for byte type.
     * @return parameters to be used for testing that the method `convertValue` returns the expected result for byte type.
     */
    private fun dataConvertByteValueTesting(): List<Array<Any?>> {
        return listOf(
            arrayOf(
                "Tests that the method returns a byte value from a byte",
                BYTE_VALUE,
                Byte::class.javaPrimitiveType,
                BYTE_VALUE
            ),
            arrayOf(
                "Tests that the method returns a byte value from a short",
                SHORT_VALUE,
                Byte::class.javaPrimitiveType,
                SHORT_VALUE.toByte()
            ),
            arrayOf(
                "Tests that the method returns a byte value from an Integer",
                INTEGER_VALUE,
                Byte::class.javaPrimitiveType,
                INTEGER_VALUE.toByte()
            ),
            arrayOf(
                "Tests that the method returns a byte value from a Long",
                LONG_VALUE,
                Byte::class.javaPrimitiveType,
                LONG_VALUE.toByte()
            ),
            arrayOf(
                "Tests that the method returns a byte value from a Float",
                FLOAT_VALUE,
                Byte::class.javaPrimitiveType,
                FLOAT_VALUE.toInt()
            ),
            arrayOf(
                "Tests that the method returns a byte value from a Double",
                DOUBLE_VALUE,
                Byte::class.javaPrimitiveType,
                DOUBLE_VALUE.toInt()
            ),
            arrayOf(
                "Tests that the method returns a byte value from a char",
                TRUE_AS_CHAR_VALUE,
                Byte::class.javaPrimitiveType,
                TRUE_AS_CHAR_VALUE.code.toByte()
            ),
            arrayOf(
                "Tests that the method returns a byte value from a Boolean",
                BOOLEAN_VALUE,
                Byte::class.javaPrimitiveType,
                TRUE_AS_BYTE
            ),
            arrayOf(
                "Tests that the method returns a byte value from a String",
                ONE_AS_STRING,
                Byte::class.javaPrimitiveType,
                java.lang.Byte.valueOf(
                    ONE_AS_STRING
                )
            ),
            arrayOf(
                "Tests that the method returns a byte value from a BigInteger",
                BigInteger.ZERO,
                Byte::class.javaPrimitiveType,
                BigInteger.ZERO.toByte()
            ),
            arrayOf(
                "Tests that the method returns a byte value from a BigDecimal",
                BigDecimal.ZERO,
                Byte::class.javaPrimitiveType,
                BigDecimal.ZERO.toByte()
            ),
            arrayOf(
                "Tests that the method returns a byte value from a byte[]",
                ONE_BYTE_BYTE_ARRAY,
                Byte::class.javaPrimitiveType,
                TRUE_AS_BYTE
            )
        )
    }

    /**
     * Creates the parameters to be used for testing that the method `convertValue` returns the expected result for char type.
     * @return parameters to be used for testing that the method `convertValue` returns the expected result for char type.
     */
    private fun dataConvertCharValueTesting(): List<Array<Any?>> {
        return listOf(
            arrayOf(
                "Tests that the method returns a char value from a byte",
                BYTE_VALUE,
                Char::class.javaPrimitiveType,
                Char(BYTE_VALUE.toUShort())
            ),
            arrayOf(
                "Tests that the method returns a char value from a short",
                SHORT_VALUE,
                Char::class.javaPrimitiveType,
                Char(SHORT_VALUE.toByte().toUShort())
            ),
            arrayOf(
                "Tests that the method returns a char value from an Integer",
                INTEGER_VALUE,
                Char::class.javaPrimitiveType,
                Char(INTEGER_VALUE.toByte().toUShort())
            ),
            arrayOf(
                "Tests that the method returns a char value from a Long",
                LONG_VALUE,
                Char::class.javaPrimitiveType,
                Char(LONG_VALUE.toByte().toUShort())
            ),
            arrayOf(
                "Tests that the method returns a char value from a Float",
                FLOAT_VALUE,
                Char::class.javaPrimitiveType,
                Char(FLOAT_VALUE.toInt().toUShort())
            ),
            arrayOf(
                "Tests that the method returns a char value from a Double",
                DOUBLE_VALUE,
                Char::class.javaPrimitiveType,
                Char(DOUBLE_VALUE.toInt().toUShort())
            ),
            arrayOf(
                "Tests that the method returns a char value from a char",
                TRUE_AS_CHAR_VALUE,
                Char::class.javaPrimitiveType,
                TRUE_AS_CHAR_VALUE
            ),
            arrayOf(
                "Tests that the method returns a char value from a Boolean",
                BOOLEAN_VALUE,
                Char::class.javaPrimitiveType,
                TRUE_AS_CHAR_VALUE
            ),
            arrayOf(
                "Tests that the method returns a char value from a String",
                ONE_AS_STRING,
                Char::class.javaPrimitiveType,
                ONE_AS_STRING[0]
            ),
            arrayOf(
                "Tests that the method returns a char value from a BigInteger",
                BigInteger.ZERO,
                Char::class.javaPrimitiveType,
                BIG_INTEGER_ZERO_AS_CHAR
            ),
            arrayOf(
                "Tests that the method returns a char value from a BigDecimal",
                BigDecimal.ZERO,
                Char::class.javaPrimitiveType,
                BIG_DECIMAL_ZERO_AS_CHAR
            ),
            arrayOf(
                "Tests that the method returns a char value from a byte[]",
                EIGHT_BYTE_BYTE_ARRAY,
                Char::class.javaPrimitiveType,
                ByteBuffer.wrap(EIGHT_BYTE_BYTE_ARRAY).char
            )
        )
    }

    /**
     * Creates the parameters to be used for testing that the method `convertValue` returns the expected result for double type.
     * @return parameters to be used for testing that the method `convertValue` returns the expected result for double type.
     */
    private fun dataConvertDoubleValueTesting(): List<Array<Any?>> {
        return listOf(
            arrayOf(
                "Tests that the method returns a double value from a byte",
                BYTE_VALUE,
                Double::class.javaPrimitiveType,
                BYTE_VALUE.toDouble()
            ),
            arrayOf(
                "Tests that the method returns a double value from a short",
                SHORT_VALUE,
                Double::class.javaPrimitiveType,
                SHORT_VALUE.toDouble()
            ),
            arrayOf(
                "Tests that the method returns a double value from an Integer",
                INTEGER_VALUE,
                Double::class.javaPrimitiveType,
                INTEGER_VALUE.toDouble()
            ),
            arrayOf(
                "Tests that the method returns a double value from a Long",
                LONG_VALUE,
                Double::class.javaPrimitiveType,
                LONG_VALUE.toDouble()
            ),
            arrayOf(
                "Tests that the method returns a double value from a Float",
                FLOAT_VALUE,
                Double::class.javaPrimitiveType,
                FLOAT_VALUE.toDouble()
            ),
            arrayOf(
                "Tests that the method returns a double value from a Double",
                DOUBLE_VALUE,
                Double::class.javaPrimitiveType,
                DOUBLE_VALUE
            ),
            arrayOf(
                "Tests that the method returns a double value from a char",
                CHAR_VALUE,
                Double::class.javaPrimitiveType,
                java.lang.Double.valueOf(CHAR_VALUE.toString())
            ),
            arrayOf(
                "Tests that the method returns a double value from a Boolean",
                BOOLEAN_VALUE,
                Double::class.javaPrimitiveType,
                TRUE_AS_INT.toDouble()
            ),
            arrayOf(
                "Tests that the method returns a double value from a String",
                ONE_AS_STRING,
                Double::class.javaPrimitiveType,
                java.lang.Double.valueOf(
                    ONE_AS_STRING
                )
            ),
            arrayOf(
                "Tests that the method returns a double value from a BigInteger",
                BigInteger.ZERO,
                Double::class.javaPrimitiveType,
                BigInteger.ZERO.toDouble()
            ),
            arrayOf(
                "Tests that the method returns a double value from a BigDecimal",
                BigDecimal.ZERO,
                Double::class.javaPrimitiveType,
                BigDecimal.ZERO.toDouble()
            ),
            arrayOf(
                "Tests that the method returns a double value from a byte[]",
                EIGHT_BYTE_BYTE_ARRAY,
                Double::class.javaPrimitiveType,
                ByteBuffer.wrap(EIGHT_BYTE_BYTE_ARRAY).double
            )
        )
    }

    /**
     * Creates the parameters to be used for testing that the method `convertValue` returns the expected result for float type.
     * @return parameters to be used for testing that the method `convertValue` returns the expected result for float type.
     */
    private fun dataConvertFloatValueTesting(): List<Array<Any?>> {
        return listOf(
            arrayOf(
                "Tests that the method returns a float value from a byte",
                BYTE_VALUE,
                Float::class.javaPrimitiveType,
                BYTE_VALUE.toFloat()
            ),
            arrayOf(
                "Tests that the method returns a float value from a short",
                SHORT_VALUE,
                Float::class.javaPrimitiveType,
                SHORT_VALUE.toFloat()
            ),
            arrayOf(
                "Tests that the method returns a float value from an Integer",
                INTEGER_VALUE,
                Float::class.javaPrimitiveType,
                INTEGER_VALUE.toFloat()
            ),
            arrayOf(
                "Tests that the method returns a float value from a Long",
                LONG_VALUE,
                Float::class.javaPrimitiveType,
                LONG_VALUE.toFloat()
            ),
            arrayOf(
                "Tests that the method returns a float value from a Float",
                FLOAT_VALUE,
                Float::class.javaPrimitiveType,
                FLOAT_VALUE
            ),
            arrayOf(
                "Tests that the method returns a float value from a Double",
                DOUBLE_VALUE,
                Float::class.javaPrimitiveType,
                DOUBLE_VALUE.toFloat()
            ),
            arrayOf(
                "Tests that the method returns a float value from a char",
                CHAR_VALUE,
                Float::class.javaPrimitiveType,
                Character.getNumericValue(CHAR_VALUE).toFloat()
            ),
            arrayOf(
                "Tests that the method returns a float value from a Boolean",
                BOOLEAN_VALUE,
                Float::class.javaPrimitiveType,
                TRUE_AS_INT.toFloat()
            ),
            arrayOf(
                "Tests that the method returns a float value from a String",
                ONE_AS_STRING,
                Float::class.javaPrimitiveType,
                java.lang.Float.valueOf(
                    ONE_AS_STRING
                )
            ),
            arrayOf(
                "Tests that the method returns a float value from a BigInteger",
                BigInteger.ZERO,
                Float::class.javaPrimitiveType,
                BigInteger.ZERO.toFloat()
            ),
            arrayOf(
                "Tests that the method returns a float value from a BigDecimal",
                BigDecimal.ZERO,
                Float::class.javaPrimitiveType,
                BigDecimal.ZERO.toFloat()
            ),
            arrayOf(
                "Tests that the method returns a float value from a byte[]",
                EIGHT_BYTE_BYTE_ARRAY,
                Float::class.javaPrimitiveType,
                ByteBuffer.wrap(EIGHT_BYTE_BYTE_ARRAY).float
            )
        )
    }

    /**
     * Creates the parameters to be used for testing that the method `convertValue` returns the expected result for int type.
     * @return parameters to be used for testing that the method `convertValue` returns the expected result for int type.
     */
    private fun dataConvertIntValueTesting(): List<Array<Any?>> {
        return listOf(
            arrayOf(
                "Tests that the method returns an int value from a byte",
                BYTE_VALUE,
                Int::class.javaPrimitiveType,
                BYTE_VALUE.toInt()
            ),
            arrayOf(
                "Tests that the method returns an int value from a short",
                SHORT_VALUE,
                Int::class.javaPrimitiveType,
                SHORT_VALUE.toInt()
            ),
            arrayOf(
                "Tests that the method returns an int value from an Integer",
                INTEGER_VALUE,
                Int::class.javaPrimitiveType,
                INTEGER_VALUE
            ),
            arrayOf(
                "Tests that the method returns an int value from a Long",
                LONG_VALUE,
                Int::class.javaPrimitiveType,
                LONG_VALUE.toInt()
            ),
            arrayOf(
                "Tests that the method returns an int value from a Float",
                FLOAT_VALUE,
                Int::class.javaPrimitiveType,
                FLOAT_VALUE.toInt()
            ),
            arrayOf(
                "Tests that the method returns an int value from a Double",
                DOUBLE_VALUE,
                Int::class.javaPrimitiveType,
                DOUBLE_VALUE.toInt()
            ),
            arrayOf(
                "Tests that the method returns an int value from a char",
                CHAR_VALUE,
                Int::class.javaPrimitiveType,
                Character.getNumericValue(CHAR_VALUE)
            ),
            arrayOf(
                "Tests that the method returns an int value from a Boolean",
                BOOLEAN_VALUE,
                Int::class.javaPrimitiveType,
                TRUE_AS_INT
            ),
            arrayOf(
                "Tests that the method returns an int value from a String",
                ONE_AS_STRING,
                Int::class.javaPrimitiveType,
                Integer.valueOf(
                    ONE_AS_STRING
                )
            ),
            arrayOf(
                "Tests that the method returns an int value from a BigInteger",
                BigInteger.ZERO,
                Int::class.javaPrimitiveType,
                BigInteger.ZERO.toInt()
            ),
            arrayOf(
                "Tests that the method returns an int value from a BigDecimal",
                BigDecimal.ZERO,
                Int::class.javaPrimitiveType,
                BigDecimal.ZERO.toInt()
            ),
            arrayOf(
                "Tests that the method returns a int value from a byte[]",
                EIGHT_BYTE_BYTE_ARRAY,
                Int::class.javaPrimitiveType,
                ByteBuffer.wrap(EIGHT_BYTE_BYTE_ARRAY).int
            )
        )
    }

    /**
     * Creates the parameters to be used for testing that the method `convertValue` returns the expected result for long type.
     * @return parameters to be used for testing that the method `convertValue` returns the expected result for long type.
     */
    private fun dataConvertLongValueTesting(): List<Array<Any?>> {
        return listOf(
            arrayOf(
                "Tests that the method returns a long value from a byte",
                BYTE_VALUE,
                Long::class.javaPrimitiveType,
                BYTE_VALUE.toLong()
            ),
            arrayOf(
                "Tests that the method returns a long value from a short",
                SHORT_VALUE,
                Long::class.javaPrimitiveType,
                SHORT_VALUE.toLong()
            ),
            arrayOf(
                "Tests that the method returns a long value from an Integer",
                INTEGER_VALUE,
                Long::class.javaPrimitiveType,
                INTEGER_VALUE.toLong()
            ),
            arrayOf(
                "Tests that the method returns a long value from a Long",
                LONG_VALUE,
                Long::class.javaPrimitiveType,
                LONG_VALUE
            ),
            arrayOf(
                "Tests that the method returns a long value from a Float",
                FLOAT_VALUE,
                Long::class.javaPrimitiveType,
                FLOAT_VALUE.toLong()
            ),
            arrayOf(
                "Tests that the method returns a long value from a Double",
                DOUBLE_VALUE,
                Long::class.javaPrimitiveType,
                DOUBLE_VALUE.toLong()
            ),
            arrayOf(
                "Tests that the method returns a long value from a char",
                CHAR_VALUE,
                Long::class.javaPrimitiveType,
                Character.getNumericValue(CHAR_VALUE).toLong()
            ),
            arrayOf(
                "Tests that the method returns a long value from a Boolean",
                BOOLEAN_VALUE,
                Long::class.javaPrimitiveType,
                TRUE_AS_INT.toLong()
            ),
            arrayOf(
                "Tests that the method returns a long value from a String",
                ONE_AS_STRING,
                Long::class.javaPrimitiveType,
                java.lang.Long.valueOf(
                    ONE_AS_STRING
                )
            ),
            arrayOf(
                "Tests that the method returns a long value from a BigInteger",
                BigInteger.ZERO,
                Long::class.javaPrimitiveType,
                BigInteger.ZERO.toLong()
            ),
            arrayOf(
                "Tests that the method returns a long value from a BigDecimal",
                BigDecimal.ZERO,
                Long::class.javaPrimitiveType,
                BigDecimal.ZERO.toLong()
            ),
            arrayOf(
                "Tests that the method returns a long value from a byte[]",
                EIGHT_BYTE_BYTE_ARRAY,
                Long::class.javaPrimitiveType,
                ByteBuffer.wrap(EIGHT_BYTE_BYTE_ARRAY).long
            )
        )
    }

    /**
     * Creates the parameters to be used for testing that the method `convertValue` returns the expected result for short type.
     * @return parameters to be used for testing that the method `convertValue` returns the expected result for short type.
     */
    private fun dataConvertShortValueTesting(): List<Array<Any?>> {
        return listOf(
            arrayOf(
                "Tests that the method returns a short value from a byte",
                BYTE_VALUE,
                Short::class.javaPrimitiveType,
                BYTE_VALUE.toShort()
            ),
            arrayOf(
                "Tests that the method returns a short value from a short",
                SHORT_VALUE,
                Short::class.javaPrimitiveType,
                SHORT_VALUE
            ),
            arrayOf(
                "Tests that the method returns a short value from an Integer",
                INTEGER_VALUE,
                Short::class.javaPrimitiveType,
                INTEGER_VALUE.toShort()
            ),
            arrayOf(
                "Tests that the method returns a short value from a Long",
                LONG_VALUE,
                Short::class.javaPrimitiveType,
                LONG_VALUE.toShort()
            ),
            arrayOf(
                "Tests that the method returns a short value from a Float",
                FLOAT_VALUE,
                Short::class.javaPrimitiveType,
                FLOAT_VALUE.toInt()
            ),
            arrayOf(
                "Tests that the method returns a short value from a Double",
                DOUBLE_VALUE,
                Short::class.javaPrimitiveType,
                DOUBLE_VALUE.toInt()
            ),
            arrayOf(
                "Tests that the method returns a short value from a char",
                CHAR_VALUE,
                Short::class.javaPrimitiveType,
                Character.getNumericValue(CHAR_VALUE).toShort()
            ),
            arrayOf(
                "Tests that the method returns a short value from a Boolean",
                BOOLEAN_VALUE,
                Short::class.javaPrimitiveType,
                TRUE_AS_INT.toShort()
            ),
            arrayOf(
                "Tests that the method returns a short value from a String",
                ONE_AS_STRING,
                Short::class.javaPrimitiveType,
                ONE_AS_STRING.toShort()
            ),
            arrayOf(
                "Tests that the method returns a short value from a BigInteger",
                BigInteger.ZERO,
                Short::class.javaPrimitiveType,
                BigInteger.ZERO.toShort()
            ),
            arrayOf(
                "Tests that the method returns a short value from a BigDecimal",
                BigDecimal.ZERO,
                Short::class.javaPrimitiveType,
                BigDecimal.ZERO.toShort()
            ),
            arrayOf(
                "Tests that the method returns a short value from a byte[]",
                EIGHT_BYTE_BYTE_ARRAY,
                Short::class.javaPrimitiveType,
                ByteBuffer.wrap(EIGHT_BYTE_BYTE_ARRAY).short
            )
        )
    }

    /**
     * Creates the parameters to be used for testing that the method `convertValue` returns the expected result for String type.
     * @return parameters to be used for testing that the method `convertValue` returns the expected result for String type.
     */
    private fun dataConvertStringValueTesting(): List<Array<Any?>> {
        return listOf(
            arrayOf(
                "Tests that the method returns a String value from a byte",
                BYTE_VALUE,
                String::class.java,
                BYTE_VALUE.toString()
            ),
            arrayOf(
                "Tests that the method returns a String value from a short",
                SHORT_VALUE,
                String::class.java,
                SHORT_VALUE.toString()
            ),
            arrayOf(
                "Tests that the method returns a String value from an Integer",
                INTEGER_VALUE,
                String::class.java,
                INTEGER_VALUE.toString()
            ),
            arrayOf(
                "Tests that the method returns a String value from a Long",
                LONG_VALUE,
                String::class.java,
                LONG_VALUE.toString()
            ),
            arrayOf(
                "Tests that the method returns a String value from a Float",
                FLOAT_VALUE,
                String::class.java,
                FLOAT_VALUE.toString()
            ),
            arrayOf(
                "Tests that the method returns a String value from a Double",
                DOUBLE_VALUE,
                String::class.java,
                DOUBLE_VALUE.toString()
            ),
            arrayOf(
                "Tests that the method returns a String value from a char",
                CHAR_VALUE,
                String::class.java,
                CHAR_VALUE.toString()
            ),
            arrayOf(
                "Tests that the method returns a String value from a Boolean",
                BOOLEAN_VALUE,
                String::class.java,
                BOOLEAN_VALUE.toString()
            ),
            arrayOf(
                "Tests that the method returns a String value from a String",
                ONE_AS_STRING,
                String::class.java,
                ONE_AS_STRING
            ),
            arrayOf(
                "Tests that the method returns a String value from a BigInteger",
                BigInteger.ZERO,
                String::class.java,
                BigInteger.ZERO.toString()
            ),
            arrayOf(
                "Tests that the method returns a String value from a BigDecimal",
                BigDecimal.ZERO,
                String::class.java,
                BigDecimal.ZERO.toPlainString()
            ),
            arrayOf(
                "Tests that the method returns a String value from a byte[]",
                EIGHT_BYTE_BYTE_ARRAY,
                String::class.java,
                String(EIGHT_BYTE_BYTE_ARRAY)
            )
        )
    }

    /**
     * Creates the parameters to be used for testing that the method `convertValue` returns the expected result for BigInteger type.
     * @return parameters to be used for testing that the method `convertValue` returns the expected result for BigInteger type.
     */
    private fun dataConvertBigIntegerValueTesting(): List<Array<Any?>> {
        return listOf(
            arrayOf(
                "Tests that the method returns a BigInteger value from a byte",
                BYTE_VALUE,
                BigInteger::class.java,
                BigInteger.valueOf(BYTE_VALUE.toLong())
            ),
            arrayOf(
                "Tests that the method returns a BigInteger value from a short",
                SHORT_VALUE,
                BigInteger::class.java,
                BigInteger.valueOf(SHORT_VALUE.toLong())
            ),
            arrayOf(
                "Tests that the method returns a BigInteger value from an Integer",
                INTEGER_VALUE,
                BigInteger::class.java,
                BigInteger.valueOf(INTEGER_VALUE.toLong())
            ),
            arrayOf(
                "Tests that the method returns a BigInteger value from a Long",
                LONG_VALUE,
                BigInteger::class.java,
                BigInteger.valueOf(LONG_VALUE)
            ),
            arrayOf(
                "Tests that the method returns a BigInteger value from a Float",
                FLOAT_VALUE,
                BigInteger::class.java,
                BigInteger.valueOf(FLOAT_VALUE.toInt().toLong())
            ),
            arrayOf(
                "Tests that the method returns a BigInteger value from a Double",
                DOUBLE_VALUE,
                BigInteger::class.java,
                BigInteger.valueOf(DOUBLE_VALUE.toInt().toLong())
            ),
            arrayOf(
                "Tests that the method returns a BigInteger value from a char",
                CHAR_VALUE,
                BigInteger::class.java,
                BigInteger.valueOf(Character.getNumericValue(CHAR_VALUE).toLong())
            ),
            arrayOf(
                "Tests that the method returns a BigInteger value from a Boolean",
                BOOLEAN_VALUE,
                BigInteger::class.java,
                BigInteger.valueOf(
                    TRUE_AS_INT.toLong()
                )
            ),
            arrayOf(
                "Tests that the method returns a BigInteger value from a String",
                ONE_AS_STRING,
                BigInteger::class.java,
                BigInteger(
                    ONE_AS_STRING
                )
            ),
            arrayOf(
                "Tests that the method returns a BigInteger value from a BigInteger",
                BigInteger.ZERO,
                BigInteger::class.java,
                BigInteger.ZERO
            ),
            arrayOf(
                "Tests that the method returns a BigInteger value from a BigDecimal",
                BigDecimal.ZERO,
                BigInteger::class.java,
                BigDecimal.ZERO.toBigInteger()
            ),
            arrayOf(
                "Tests that the method returns a BigInteger value from a byte[]",
                EIGHT_BYTE_BYTE_ARRAY,
                BigInteger::class.java,
                BigInteger.valueOf(ByteBuffer.wrap(EIGHT_BYTE_BYTE_ARRAY).long)
            )
        )
    }

    /**
     * Creates the parameters to be used for testing that the method `convertValue` returns the expected result for BigDecimal type.
     * @return parameters to be used for testing that the method `convertValue` returns the expected result for BigDecimal type.
     */
    private fun dataConvertBigDecimalValueTesting(): List<Array<Any?>> {
        return listOf(
            arrayOf(
                "Tests that the method returns a BigDecimal value from a byte",
                BYTE_VALUE,
                BigDecimal::class.java,
                BigDecimal.valueOf(BYTE_VALUE.toDouble())
            ),
            arrayOf(
                "Tests that the method returns a BigDecimal value from a short",
                SHORT_VALUE,
                BigDecimal::class.java,
                BigDecimal.valueOf(SHORT_VALUE.toLong())
            ),
            arrayOf(
                "Tests that the method returns a BigDecimal value from an Integer",
                INTEGER_VALUE,
                BigDecimal::class.java,
                BigDecimal.valueOf(INTEGER_VALUE.toLong())
            ),
            arrayOf(
                "Tests that the method returns a BigDecimal value from a Long",
                LONG_VALUE,
                BigDecimal::class.java,
                BigDecimal.valueOf(LONG_VALUE)
            ),
            arrayOf(
                "Tests that the method returns a BigDecimal value from a Float",
                FLOAT_VALUE,
                BigDecimal::class.java,
                BigDecimal.valueOf(FLOAT_VALUE.toDouble())
            ),
            arrayOf(
                "Tests that the method returns a BigDecimal value from a Double",
                DOUBLE_VALUE,
                BigDecimal::class.java,
                BigDecimal.valueOf(DOUBLE_VALUE)
            ),
            arrayOf(
                "Tests that the method returns a BigDecimal value from a char",
                CHAR_VALUE,
                BigDecimal::class.java,
                BigDecimal.valueOf(Character.getNumericValue(CHAR_VALUE).toLong())
            ),
            arrayOf(
                "Tests that the method returns a BigDecimal value from a Boolean",
                BOOLEAN_VALUE,
                BigDecimal::class.java,
                BigDecimal.valueOf(
                    TRUE_AS_INT.toLong()
                )
            ),
            arrayOf(
                "Tests that the method returns a BigDecimal value from a String",
                ONE_AS_STRING,
                BigDecimal::class.java,
                BigDecimal(
                    ONE_AS_STRING
                )
            ),
            arrayOf(
                "Tests that the method returns a BigDecimal value from a BigInteger",
                BigInteger.ZERO,
                BigDecimal::class.java,
                BigDecimal.valueOf(BigInteger.ZERO.toInt().toLong())
            ),
            arrayOf(
                "Tests that the method returns a BigDecimal value from a BigDecimal",
                BigDecimal.ZERO,
                BigDecimal::class.java,
                BigDecimal.ZERO
            ),
            arrayOf(
                "Tests that the method returns a BigDecimal value from a byte[]",
                EIGHT_BYTE_BYTE_ARRAY,
                BigDecimal::class.java,
                BigDecimal.valueOf(ByteBuffer.wrap(EIGHT_BYTE_BYTE_ARRAY).double)
            )
        )
    }

    /**
     * Tests that the method `convertValue` returns the expected converted byte[] value.
     * @param testCaseDescription the test case description
     * @param valueToConvert the value to convert
     * @param expectedValue the expected result
     * @param <T> the value to convert class type
     */
    @Test(dataProvider = "dataConvertByteArrayValueTesting")
    fun <T> testConvertByteArrayValueWorksProperly(
        testCaseDescription: String?,
        valueToConvert: T,
        expectedValue: ByteArray?
    ) {
        // GIVEN

        // WHEN
        val actual = underTest.convertValue(valueToConvert, ByteArray::class.java)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expectedValue)
    }

    /**
     * Creates the parameters to be used for testing that the method `convertValue` returns the expected result for byte[] type.
     * @return parameters to be used for testing that the method `convertValue` returns the expected result for byte[] type.
     */
    @DataProvider
    private fun dataConvertByteArrayValueTesting(): Array<Array<Any>> {
        return arrayOf(
            arrayOf(
                "Tests that the method returns a byte[] value from a byte",
                BYTE_VALUE,
                byteArrayOf(BYTE_VALUE)
            ),
            arrayOf(
                "Tests that the method returns a byte[] value from a byte[]",
                EIGHT_BYTE_BYTE_ARRAY,
                EIGHT_BYTE_BYTE_ARRAY
            ),
            arrayOf(
                "Tests that the method returns a byte[] value from a short",
                SHORT_VALUE,
                byteArrayOf(SHORT_VALUE.toByte())
            ),
            arrayOf(
                "Tests that the method returns a byte[] value from an Integer",
                INTEGER_VALUE,
                byteArrayOf(INTEGER_VALUE.toByte())
            ),
            arrayOf(
                "Tests that the method returns a byte[] value from a Long",
                LONG_VALUE,
                byteArrayOf(LONG_VALUE.toByte())
            ),
            arrayOf(
                "Tests that the method returns a byte[] value from a Float",
                FLOAT_VALUE,
                byteArrayOf(
                    FLOAT_VALUE.toInt().toByte()
                )
            ),
            arrayOf(
                "Tests that the method returns a byte[] value from a Double",
                DOUBLE_VALUE,
                byteArrayOf(
                    DOUBLE_VALUE.toInt().toByte()
                )
            ),
            arrayOf(
                "Tests that the method returns a byte[] value from a char",
                TRUE_AS_CHAR_VALUE,
                byteArrayOf(
                    TRUE_AS_CHAR_VALUE.code.toByte()
                )
            ),
            arrayOf(
                "Tests that the method returns a byte[] value from a Boolean",
                BOOLEAN_VALUE,
                ONE_BYTE_BYTE_ARRAY
            ),
            arrayOf(
                "Tests that the method returns a byte[] value from a String",
                ONE_AS_STRING,
                ONE_AS_STRING.toByteArray()
            ),
            arrayOf(
                "Tests that the method returns a byte[] value from a BigInteger",
                BigInteger.ZERO,
                byteArrayOf(BigInteger.ZERO.toByte())
            ),
            arrayOf(
                "Tests that the method returns a byte[] value from a BigDecimal",
                BigDecimal.ZERO,
                byteArrayOf(BigDecimal.ZERO.toByte())
            )
        )
    }

    /**
     * Tests that the method `convertValue` raises a [TypeConversionException].
     */
    @Test(expectedExceptions = [TypeConversionException::class])
    fun testConvertValueRaisesExceptionInCaseNoConverterIsDefined() {
        // GIVEN

        // WHEN
        underTest.convertValue(ONE_AS_STRING, Void::class.java)
    }

    companion object {
        private const val ONE_AS_STRING = "1"
        private const val TRUE_AS_CHAR_VALUE = 'T'
        private const val TRUE_AS_BYTE: Byte = 1
        private const val TRUE_AS_INT = 1
        private val BIG_DECIMAL_ZERO_AS_CHAR = BigDecimal.ZERO.toDouble().toInt().toChar()
        private val BIG_INTEGER_ZERO_AS_CHAR = BigInteger.ZERO.toInt().toChar()
    }
}
