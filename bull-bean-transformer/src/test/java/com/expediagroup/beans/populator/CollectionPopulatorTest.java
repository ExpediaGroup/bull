/**
 * Copyright (C) 2019-2026 Expedia, Inc.
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.List;
import java.util.Set;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.expediagroup.beans.sample.mixed.MixedToFooStaticField;
import com.expediagroup.beans.transformer.BeanTransformer;

/**
 * Unit test for class: {@link CollectionPopulator}.
 */
public class CollectionPopulatorTest {
    private static final MixedToFooStaticField ELEM = new MixedToFooStaticField();

    @Mock
    private BeanTransformer transformer;

    /**
     * The class to be tested.
     */
    @InjectMocks
    private CollectionPopulator<MixedToFooStaticField> underTest;

    /**
     * Initializes mock.
     */
    @BeforeClass
    public void beforeClass() {
        openMocks(this);
    }

    /**
     * Tests that {@code getPopulatedObject} collects into a {@link Set} when {@code fieldType} is {@code Set}.
     * Covers the true branch of {@code Set.class.isAssignableFrom(fieldType) ? toSet() : toList()}.
     */
    @Test
    public void testGetPopulatedObjectCollectsIntoSetWhenFieldTypeIsSet() {
        // GIVEN - a source list of non-primitive elements, destination type is Set
        List<MixedToFooStaticField> sourceList = List.of(ELEM);
        when(transformer.transform(any(), eq(MixedToFooStaticField.class))).thenReturn(ELEM);

        // WHEN
        var result = underTest.getPopulatedObject(Set.class, MixedToFooStaticField.class, sourceList, null);

        // THEN
        assertThat(result).isInstanceOf(Set.class).contains(ELEM);
    }
}
