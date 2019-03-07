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

package com.hotels.beans.transformer;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import com.hotels.beans.error.InvalidBeanException;
import com.hotels.beans.model.FieldTransformer;
import com.hotels.beans.sample.FromFooSimple;
import com.hotels.beans.sample.FromFooSimpleNoGetters;
import com.hotels.beans.sample.mutable.MutableToFoo;
import com.hotels.beans.sample.mutable.MutableToFooInvalid;
import com.hotels.beans.sample.mutable.MutableToFooNotExistingFields;
import com.hotels.beans.sample.mutable.MutableToFooSimpleNoSetters;
import com.hotels.beans.sample.mutable.MutableToFooSubClass;

/**
 * Unit test for all {@link Transformer} functions related to Mutable type Java Beans.
 */
public class MutableObjectTransformationTest extends AbstractTransformerTest {
    /**
     * The class to be tested.
     */
    @InjectMocks
    private TransformerImpl underTest;

    /**
     * Initialized mocks.
     */
    @Before
    public void beforeMethod() {
        initMocks(this);
    }

    /**
     * Test that an exception is thrown if there is no default constructor defined for the mutable bean object.
     */
    @Test(expected = InvalidBeanException.class)
    public void testTransformThrowsExceptionWhenMutableBeanHasNoDefaultConstructor() {
        //GIVEN

        //WHEN
        underTest.transform(fromFoo, MutableToFooInvalid.class);
    }

    /**
     * Test mutable beans are correctly copied.
     */
    @Test
    public void testMutableBeanIsCorrectlyCopied() {
        //GIVEN

        //WHEN
        MutableToFoo actual = underTest.transform(fromFoo, MutableToFoo.class);

        //THEN
        assertThat(actual, sameBeanAs(fromFoo));
    }

    /**
     * Test that a given field transformer function is applied only to a specific field even if there are other ones with same name.
     */
    @Test
    public void testFieldTransformationIsAppliedOnlyToASpecificField() {
        //GIVEN
        String namePrefix = "prefix-";
        FieldTransformer<String, String> nameTransformer = new FieldTransformer<>("nestedObject.name", val -> namePrefix + val);

        //WHEN
        MutableToFoo actual = underTest
                .withFieldTransformer(nameTransformer)
                .transform(fromFoo, MutableToFoo.class);

        //THEN
        assertEquals(fromFoo.getName(), actual.getName());
        assertEquals(namePrefix + fromFoo.getNestedObject().getName(), actual.getNestedObject().getName());
        underTest.resetFieldsTransformer();
    }

    /**
     * Test that a given field transformer function is applied to all field matching the given name when {@code flatFieldNameTransformation} is set to true.
     */
    @Test
    public void testFieldTransformationIsAppliedToAllMatchingFields() {
        //GIVEN
        String namePrefix = "prefix-";
        FieldTransformer<String, String> nameTransformer = new FieldTransformer<>("name", val -> namePrefix + val);

        //WHEN
        MutableToFoo actual = underTest
                .setFlatFieldNameTransformation(true)
                .withFieldTransformer(nameTransformer)
                .transform(fromFoo, MutableToFoo.class);

        //THEN
        assertEquals(namePrefix + fromFoo.getName(), actual.getName());
        assertEquals(namePrefix + fromFoo.getNestedObject().getName(), actual.getNestedObject().getName());
        underTest.resetFieldsTransformer();
    }

    /**
     * Test that the transformer is able to copy object even if the source object has no setter methods and the destination
     * object has no setter methods.
     */
    @Test
    public void testTransformerIsAbleToCopyObjectsWithoutRequiredMethods() {
        //GIVEN
        FromFooSimpleNoGetters fromFooSimpleNoGetters = new FromFooSimpleNoGetters(NAME, ID);
        //WHEN
        MutableToFooSimpleNoSetters actual = underTest.transform(fromFooSimpleNoGetters, MutableToFooSimpleNoSetters.class);

        //THEN
        assertThat(actual, sameBeanAs(fromFooSimpleNoGetters));
    }

    /**
     * Test that a bean containing a field not existing in the source object, but with a transformer function defined for such object is correctly copied.
     */
    @Test
    public void testThatAnyTypeOfBeanContainsANotExistingFieldInTheSourceObjectIsCorrectlyCopiedThroughTransformerFunctions() {
        //GIVEN
        FromFooSimple fromFooSimple = new FromFooSimple(NAME, ID);
        FieldTransformer<Object, Integer> ageFieldTransformer = new FieldTransformer<>(AGE_FIELD_NAME, val -> AGE);

        //WHEN
        underTest.withFieldTransformer(ageFieldTransformer);
        MutableToFooNotExistingFields mutableObjectBean = underTest.transform(fromFooSimple, MutableToFooNotExistingFields.class);

        //THEN
        assertThat(mutableObjectBean, hasProperty(AGE_FIELD_NAME, equalTo(AGE)));
    }

    /**
     * Test that, given a set of fields for which the transformation has to be skipped, they are actually skipped.
     */
    @Test
    public void testFieldTransformationSkipWorksProperly() {
        //GIVEN
        underTest.skipTransformationForField("name", "nestedObject.phoneNumbers");

        //WHEN
        MutableToFoo actual = underTest.transform(fromFoo, MutableToFoo.class);

        //THEN
        assertEquals(fromFoo.getId(), actual.getId());
        assertNull(actual.getName());
        assertNull(actual.getNestedObject().getPhoneNumbers());
        underTest.resetFieldsTransformationSkip();
    }

    /**
     * Test transformation on an existing bean is correctly copied.
     */
    @Test
    public void testTransformationOnAnExistingDestinationWorksProperly() {
        //GIVEN
        MutableToFooSubClass mutableToFoo = new MutableToFooSubClass();

        //WHEN
        underTest.transform(fromFooSubClass, mutableToFoo);

        //THEN
        assertThat(mutableToFoo, sameBeanAs(fromFooSubClass));
    }
}
