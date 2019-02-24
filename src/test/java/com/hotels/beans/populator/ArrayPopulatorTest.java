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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Collection;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.hotels.beans.sample.mixed.MixedToFooStaticField;
import com.hotels.beans.transformer.Transformer;

/**
 * Unit test for class: {@link ArrayPopulator}.
 */
@RunWith(value = Parameterized.class)
public class ArrayPopulatorTest {
    private static final String VAL_1 = "val1";
    private static final String VAL_2 = "val2";
    private static final String[] STRING_ARRAY = new String[] {VAL_1, VAL_2};
    private static final char CHAR = '\u0000';
    private static final char[] CHAR_ARRAY = new char[] {CHAR};
    private static final int ZERO = 0;
    private static final int[] INT_ARRAY = new int[] {ZERO};
    private static final MixedToFooStaticField MIXED_TO_FOO_STATIC_FIELDS_OBJECTS = new MixedToFooStaticField();
    private static final String FIELD_NAME = "testField";

    private final Class<?> genericFieldType;
    private final Class<?> nestedGenericClass;
    private final Object array;

    @Mock
    private Transformer transformer;

    /**
     * The class to be tested.
     */
    @InjectMocks
    private ArrayPopulator underTest;

    /**
     * All args constructor.
     * @param genericFieldType the field to be populated class
     * @param array the source object from which extract the values
     * @param nestedGenericClass the nested generic object class.
     */
    public ArrayPopulatorTest(final Class<?> genericFieldType, final Object array, final Class<?> nestedGenericClass) {
        this.genericFieldType = genericFieldType;
        this.array = array;
        this.nestedGenericClass = nestedGenericClass;
        MIXED_TO_FOO_STATIC_FIELDS_OBJECTS.setNormalField(VAL_1);
    }

    /**
     * Initializes mock.
     */
    @Before
    public void beforeMethod() {
        initMocks(this);
    }

    /**
     * Tests that the method {@code getPopulatedObject} works as expected.
     */
    @Test
    public void testGetPopulatedObjectWorksProperly() {
        // GIVEN
        when(transformer.transform(any(), eq(MixedToFooStaticField.class))).thenReturn(MIXED_TO_FOO_STATIC_FIELDS_OBJECTS);

        // WHEN
        Object actual = underTest.getPopulatedObject(null, genericFieldType, array, nestedGenericClass, FIELD_NAME);

        // THEN
        if (genericFieldType == Character.class) {
            assertArrayEquals((char[]) array, (char[]) actual);
        } else if (genericFieldType == Integer.class) {
            assertArrayEquals((int[]) array, (int[]) actual);
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
    @Parameterized.Parameters(name = "{index}. Type: {0}, Expected value: {1}.")
    public static Collection<Object[]> dataProvider() {
        return asList(new Object[][]{
                {String.class, STRING_ARRAY, null},
                {Character.class, CHAR_ARRAY, null},
                {Integer.class, INT_ARRAY, null},
                {MixedToFooStaticField.class, createMixedToFooArray(), null}
        });
    }

    /**
     * Creates an array containing an instance of {@link MixedToFooStaticField}.
     * @return an array containing an instance of {@link MixedToFooStaticField}.
     */
    private static MixedToFooStaticField[] createMixedToFooArray() {
        return new MixedToFooStaticField[] {MIXED_TO_FOO_STATIC_FIELDS_OBJECTS};

    }
}
