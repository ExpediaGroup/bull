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

package com.hotels.beans.populator;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.stream.IntStream;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.hotels.beans.sample.mixed.MixedToFooStaticField;
import com.hotels.beans.transformer.BeanTransformer;

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
        initMocks(this);
    }

    /**
     * Tests that the method {@code getPopulatedObject} works as expected.
     * @param genericFieldType the field to be populated class
     * @param array the source object from which extract the values
     * @param nestedGenericClass the nested generic object class.
     */
    @Test(dataProvider = "dataProvider")
    public void testGetPopulatedObjectWorksProperly(final Class<?> genericFieldType, final Object array, final Class<?> nestedGenericClass) {
        // GIVEN
        when(transformer.transform(any(), eq(MixedToFooStaticField.class))).thenReturn(MIXED_TO_FOO_STATIC_FIELDS_OBJECTS);

        // WHEN
        Object actual = underTest.getPopulatedObject(null, genericFieldType, array, nestedGenericClass);

        // THEN
        if (genericFieldType == Character.class) {
            assertArrayEquals((char[]) array, (char[]) actual);
        } else if (genericFieldType == Integer.class) {
            assertArrayEquals((int[]) array, (int[]) actual);
        } else if (genericFieldType == Object.class) {
            assertArrayEquals((Object[]) array, (Object[]) actual);
        } else if (genericFieldType == MixedToFooStaticField.class) {
            final MixedToFooStaticField[] expectedArray = (MixedToFooStaticField[]) array;
            final Object[] actualArray = (Object[]) actual;
            IntStream.range(0, expectedArray.length)
                    .forEach(i -> assertEquals(expectedArray[i].getNormalField(), ((MixedToFooStaticField) actualArray[i]).getNormalField()));
        } else {
            assertArrayEquals((Object[]) array, (Object[]) actual);
        }
    }

    /**
     * Data provider for the required test cases.
     * @return the test cases.
     */
    @DataProvider
    public Object[][] dataProvider() {
        return new Object[][]{
                {String.class, STRING_ARRAY, null},
                {Character.class, CHAR_ARRAY, null},
                {Integer.class, INT_ARRAY, null},
                {MixedToFooStaticField.class, createMixedToFooArray(), null},
                {Object.class, createBooleanArray(), null}
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
    private static Object[] createBooleanArray() {
        return new Object[] {TRUE, FALSE};
    }
}
