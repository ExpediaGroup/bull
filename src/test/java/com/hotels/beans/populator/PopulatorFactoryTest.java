/**
 * Copyright (C) 2019 Expedia Inc.
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

package com.hotels.beans.populator;

import static java.util.Arrays.asList;
import static java.util.Objects.nonNull;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.hotels.beans.sample.FromFoo;
import com.hotels.beans.transformer.Transformer;

/**
 * Unit test for class: {@link PopulatorFactory}.
 */
@RunWith(value = Parameterized.class)
public class PopulatorFactoryTest {
    /**
     * The class type.
     */
    private final Class<?> type;

    /**
     * The expected result.
     */
    private final Class<? extends Populator> expectedResult;

    /**
     * The class to be tested.
     */
    @InjectMocks
    private PopulatorFactory underTest;

    /**
     * Transformer object.
     */
    @Mock
    private Transformer transformer;

    /**
     * All args constructor.
     * @param type the type for wich a populator needs to be found
     * @param expectedResult the expected populator
     */
    public PopulatorFactoryTest(final Class type, final Class<? extends Populator> expectedResult) {
        this.type = type;
        this.expectedResult = expectedResult;
    }

    /**
     * Initializes mock.
     */
    @Before
    public void beforeMethod() {
        initMocks(this);
    }

    /**
     * Tests that the method: {@code defaultValue} returns the expected result for the given type.
     */
    @Test
    public void testGetPopulatorReturnsTheExpectedResult() {
        // GIVEN

        // WHEN
        Optional<Populator> populator = underTest.getPopulator(type, type, transformer);

        // THEN
        final boolean isNonNullObjectExpected = nonNull(expectedResult);
        assertEquals(populator.isPresent(), isNonNullObjectExpected);
        if (isNonNullObjectExpected) {
            assertEquals(expectedResult, populator.get().getClass());
        }
    }

    /**
     * Data provider for the required test cases.
     * @return the test cases.
     */
    @Parameterized.Parameters(name = "{index}. Type: {0}, Expected populator: {1}.")
    public static Collection<Object[]> dataProvider() {
        return asList(new Object[][]{
                {String[].class, ArrayPopulator.class},
                {List.class, CollectionPopulator.class},
                {Map.class, MapPopulator.class},
                {FromFoo.class, null},
                {Optional.class, OptionalPopulator.class}
        });
    }
}
