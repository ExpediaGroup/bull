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
package com.expediagroup.beans.populator;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.expediagroup.beans.sample.mixed.MixedToFooStaticField;
import com.expediagroup.beans.transformer.BeanTransformer;

/**
 * Unit test for class: {@link ArrayPopulator}.
 */
public class ArrayPopulatorTest {
    private static final String VAL_1 = "val1";
    private static final String VAL_2 = "val2";
    private static final String[] STRING_ARRAY = new String[] {VAL_1, VAL_2};
    private static final char CHAR = '\u0000';
    private static final char[] CHAR_ARRAY = new char[] {CHAR};
    private static final int ZERO = 0;
    private static final int[] INT_ARRAY = new int[] {ZERO};
    private static final MixedToFooStaticField MIXED_TO_FOO_STATIC_FIELDS_OBJECTS = new MixedToFooStaticField();
    private static final String NORMAL_FIELD = "normalField";

    @Mock
    private BeanTransformer transformer;

    /**
     * The class to be tested.
     */
    @InjectMocks
    private ArrayPopulator underTest;

    /**
     * Initializes mock.
     */
    @BeforeClass
    public void beforeClass() {
        openMocks(this);
    }

    /**
     * Tests that the method {@code getPopulatedObject} works as expected.
     * @param genericFieldType the field to be populated class
     * @param array the source object from which extract the values
     */
    @Test(dataProvider = "dataProvider")
    public void testGetPopulatedObjectWorksProperly(final Class<?> genericFieldType, final Object array) {
        // GIVEN
        when(transformer.transform(any(), eq(MixedToFooStaticField.class))).thenReturn(MIXED_TO_FOO_STATIC_FIELDS_OBJECTS);

        // WHEN
        Object actual = underTest.getPopulatedObject(null, genericFieldType, array, null);

        // THEN
        if (genericFieldType == Character.class) {
            assertThat((char[]) actual).isEqualTo(array);
        } else if (genericFieldType == Integer.class) {
            assertThat((int[]) actual).isEqualTo(array);
        } else if (genericFieldType == Object.class) {
            assertThat((Object[]) actual).isEqualTo(array);
        } else if (genericFieldType == MixedToFooStaticField.class) {
            assertThat((Object[]) actual)
                    .usingRecursiveFieldByFieldElementComparatorOnFields(NORMAL_FIELD)
                    .isEqualTo(array);
        } else {
            assertThat(actual).isEqualTo(array);
        }
    }

    /**
     * Data provider for the required test cases.
     * @return the test cases.
     */
    @DataProvider
    public Object[][] dataProvider() {
        return new Object[][]{
                {String.class, STRING_ARRAY},
                {Character.class, CHAR_ARRAY},
                {Integer.class, INT_ARRAY},
                {MixedToFooStaticField.class, createMixedToFooArray()},
                {Object.class, createBooleanArray()}
        };
    }

    /**
     * Creates an array containing an instance of {@link MixedToFooStaticField}.
     * @return an array containing an instance of {@link MixedToFooStaticField}.
     */
    private static MixedToFooStaticField[] createMixedToFooArray() {
        MIXED_TO_FOO_STATIC_FIELDS_OBJECTS.setNormalField(VAL_1);
        return new MixedToFooStaticField[] {MIXED_TO_FOO_STATIC_FIELDS_OBJECTS};
    }

    /**
     * Creates an array containing an instance of {@link Boolean}.
     * @return an array containing an instance of {@link Boolean}.
     */
    private static Boolean[] createBooleanArray() {
        return new Boolean[] {TRUE, FALSE};
    }
}
