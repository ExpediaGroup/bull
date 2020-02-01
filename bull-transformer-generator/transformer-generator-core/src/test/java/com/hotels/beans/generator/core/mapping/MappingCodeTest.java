/**
 * Copyright (C) 2019-2020 Expedia, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hotels.beans.generator.core.mapping;

import static org.testng.Assert.assertNotNull;

import org.apache.commons.lang3.NotImplementedException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.hotels.beans.generator.core.sample.immutable.ImmutableDestination;
import com.hotels.beans.generator.core.sample.javabean.Destination;
import com.hotels.beans.generator.core.sample.javabean.Source;
import com.hotels.beans.generator.core.sample.mixed.MixedDestination;

public class MappingCodeTest {

    @Test
    public void shouldReturnAnInstanceForMutableDestination() {
        // when
        var mappingCode = MappingCode.of(Source.class, Destination.class);

        // then
        assertNotNull(mappingCode);
    }

    /**
     * TODO remove this test after implementation of immutable mapping code.
     */
    @Test(expectedExceptions = NotImplementedException.class)
    public void shouldFailForImmutableDestination() {
        MappingCode.of(Source.class, ImmutableDestination.class);
    }

    /**
     * TODO remove this test after implementation of mixed mapping code.
     */
    @Test(expectedExceptions = NotImplementedException.class)
    public void shouldFailForMixedDestination() {
        MappingCode.of(Source.class, MixedDestination.class);
    }

    @Test(dataProvider = "typePairs", expectedExceptions = NullPointerException.class)
    public void shouldFailIfAnyTypeIsNull(final Class<?> source, final Class<?> destination) {
        MappingCode.of(source, destination);
    }

    @DataProvider
    private Object[][] typePairs() {
        return new Object[][]{
                {null, Object.class},
                {Object.class, null},
                {null, null}
        };
    }

}
