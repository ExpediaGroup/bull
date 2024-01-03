/**
 * Copyright (C) 2019-2023 Expedia, Inc.
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
package com.expediagroup.beans.conversion.analyzer;

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

import com.expediagroup.beans.conversion.processor.ConversionProcessor;
import com.expediagroup.beans.conversion.processor.impl.BigDecimalConversionProcessor;
import com.expediagroup.beans.conversion.processor.impl.BigIntegerConversionProcessor;
import com.expediagroup.beans.conversion.processor.impl.BooleanConversionProcessor;
import com.expediagroup.beans.conversion.processor.impl.ByteArrayConversionProcessor;
import com.expediagroup.beans.conversion.processor.impl.ByteConversionProcessor;
import com.expediagroup.beans.conversion.processor.impl.CharacterConversionProcessor;
import com.expediagroup.beans.conversion.processor.impl.DoubleConversionProcessor;
import com.expediagroup.beans.conversion.processor.impl.FloatConversionProcessor;
import com.expediagroup.beans.conversion.processor.impl.IntegerConversionProcessor;
import com.expediagroup.beans.conversion.processor.impl.StringConversionProcessor;

/**
 * Unit test for {@link ConversionAnalyzer}.
 */
public class ConversionAnalyzerTest {

    /**
     * The class to be tested.
     */
    @InjectMocks
    private ConversionAnalyzer underTest;

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
        return new Object[][] {
            {"Tests that the method returns an empty optional in case the source field type is equal to the destination field type",
                int.class, int.class},
            {"Tests that the method returns an empty optional in case the source field type is not equal to the source field type but it's not primitive",
                Pair.class, int.class},
            {"Tests that the method returns an empty optional in case the source field type is not equal to the source field type and the destination type is void",
                int.class, Void.class},
            {"Tests that the method returns an empty optional in case the source field type is not equal to the source field type and the source type is void",
                Void.class, int.class}
        };
    }

    /**
     * Tests that the method {@code getConversionFunction} returns the expected conversion function.
     * @param testCaseDescription the test case description
     * @param sourceFieldType source field class
     * @param destinationFieldType the destination field class
     * @param expectedConversionFunction the expected {@link ConversionProcessor} instance
     */
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
            {"Tests that the method returns a CharacterConversionProcessor that converts from char to boolean",
                boolean.class, char.class, new CharacterConversionProcessor().convertBoolean()},
            {"Tests that the method returns a BooleanConversionProcessor that converts from String to boolean",
                String.class, boolean.class, new BooleanConversionProcessor().convertString()},
            {"Tests that the method returns a BigIntegerConversionProcessor that converts from BigDecimal to BigInteger",
                BigDecimal.class, BigInteger.class, new BigIntegerConversionProcessor().convertBigDecimal()},
            {"Tests that the method returns a BigDecimalConversionProcessor that converts from String to BigDecimal",
                String.class, BigDecimal.class, new BigDecimalConversionProcessor().convertString()},
            {"Tests that the method returns a ByteConversionProcessor that converts from String to byte[]",
                String.class, byte[].class, new ByteArrayConversionProcessor().convertString()}
        };
    }
}
