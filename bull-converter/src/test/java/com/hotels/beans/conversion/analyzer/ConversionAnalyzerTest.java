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

package com.hotels.beans.conversion.analyzer;

import static java.util.Optional.empty;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Optional;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.mockito.InjectMocks;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

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
        initMocks(this);
    }

    /**
     * Tests that the method {@code getConversionFunction} returns the expected .
     * @param testCaseDescription the test case description
     * @param sourceFieldType source field class
     * @param destinationFieldType the destination field class
     */
    @SuppressWarnings("unchecked")
    @Test(dataProvider = "dataGetConversionFunctionTesting")
    public void testGetConversionFunctionWorksAsExpected(final String testCaseDescription, final Class<?> sourceFieldType,
        final Class<?> destinationFieldType, final Optional<Function<Object, Object>> expectedResult) {
        // GIVEN

        // WHEN
        Optional<Function<Object, Object>> actual = underTest.getConversionFunction(sourceFieldType, destinationFieldType);

        // THEN
        assertEquals(expectedResult, actual);
    }

    /**
     * Creates the parameters to be used for testing the method {@code getConversionFunction}.
     * @return parameters to be used for testing the the method {@code getConversionFunction}.
     */
    @DataProvider
    private Object[][] dataGetConversionFunctionTesting() {
        return new Object[][] {
            {"Tests that the method returns an empty optional in case the source field type is equal to the destination field type",
                int.class, int.class, empty()},
            {"Tests that the method returns an empty optional in case the source field type is equal to the source field type is not primitive",
                ImmutablePair.class, int.class, empty()}
        };
    }
}
