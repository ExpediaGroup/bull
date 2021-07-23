/**
 * Copyright (C) 2019-2021 Expedia, Inc.
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
package com.expediagroup.beans.populator;

import static java.util.Objects.nonNull;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.expediagroup.beans.sample.FromFoo;
import com.expediagroup.beans.transformer.BeanTransformer;

/**
 * Unit test for class: {@link PopulatorFactory}.
 */
public class PopulatorFactoryTest {
    /**
     * The class to be tested.
     */
    @InjectMocks
    private PopulatorFactory underTest;

    /**
     * Transformer object.
     */
    @Mock
    private BeanTransformer transformer;

    /**
     * Initializes mock.
     */
    @BeforeClass
    public void beforeClass() {
        openMocks(this);
    }

    /**
     * Data provider for the required test cases.
     * @return the test cases.
     */
    @DataProvider
    public Object[][] dataProvider() {
        return new Object[][]{
                {String[].class, ArrayPopulator.class},
                {List.class, CollectionPopulator.class},
                {Map.class, MapPopulator.class},
                {FromFoo.class, null},
                {Optional.class, OptionalPopulator.class}
        };
    }

    /**
     * Tests that the method: {@code defaultValue} returns the expected result for the given type.
     * @param type the type for which a populator needs to be found
     * @param expectedResult the expected populator
     */
    @Test(dataProvider = "dataProvider")
    @SuppressWarnings({"unchecked"})
    public void testGetPopulatorReturnsTheExpectedResult(final Class type, final Class<? extends Populator> expectedResult) {
        // GIVEN

        // WHEN
        Optional<Populator> populator = underTest.getPopulator(type, type, transformer);

        // THEN
        final boolean isNonNullObjectExpected = nonNull(expectedResult);
        assertThat(populator.isPresent()).isEqualTo(isNonNullObjectExpected);
        if (isNonNullObjectExpected) {
            assertThat(populator).containsInstanceOf(expectedResult);
        }
    }
}
