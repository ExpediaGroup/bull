/**
 * Copyright (C) 2018-2023 Expedia, Inc.
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
package com.expediagroup.beans.generator.core.mapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import static com.expediagroup.transformer.constant.ClassType.UNSUPPORTED;

import org.apache.commons.lang3.NotImplementedException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.expediagroup.beans.generator.core.sample.immutable.ImmutableDestination;
import com.expediagroup.beans.generator.core.sample.javabean.Destination;
import com.expediagroup.beans.generator.core.sample.javabean.Source;
import com.expediagroup.beans.generator.core.sample.mixed.MixedDestination;
import com.expediagroup.transformer.utils.ClassUtils;

/**
 * Tests for {@link MappingCodeFactory}.
 */
public class MappingCodeFactoryTest {
    /**
     * The class to be tested.
     */
    private final MappingCodeFactory underTest = MappingCodeFactory.newInstance();

    @Test
    public void shouldReturnAnInstanceForMutableDestination() {
        // WHEN
        var mappingCode = underTest.of(Source.class, Destination.class);

        // THEN
        assertThat(mappingCode).isNotNull();
    }

    /**
     * TODO: remove this test after implementation of immutable mapping code.
     */
    @Test(expectedExceptions = NotImplementedException.class)
    public void shouldFailForImmutableDestination() {
        underTest.of(Source.class, ImmutableDestination.class);
    }

    /**
     * TODO: remove this test after implementation of mixed mapping code.
     */
    @Test(expectedExceptions = NotImplementedException.class)
    public void shouldFailForMixedDestination() {
        underTest.of(Source.class, MixedDestination.class);
    }

    @Test(expectedExceptions = AssertionError.class, expectedExceptionsMessageRegExp = ".*UNSUPPORTED.*")
    public void shouldFailForUnsupportedDestinationType() {
        // GIVEN
        ClassUtils classUtils = given(mock(ClassUtils.class).getClassType(any()))
                .willReturn(UNSUPPORTED)
                .getMock();
        MappingCodeFactory underTest = MappingCodeFactory.newInstance(classUtils);

        // WHEN
        underTest.of(Source.class, Destination.class);
    }

    @Test(dataProvider = "typePairs", expectedExceptions = IllegalArgumentException.class)
    public void shouldFailIfAnyTypeIsNull(final Class<?> source, final Class<?> destination) {
        underTest.of(source, destination);
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
