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

package com.hotels.beans.transformer;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

import java.math.BigInteger;
import java.util.stream.IntStream;

import org.mockito.InjectMocks;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.hotels.beans.error.MissingFieldException;
import com.hotels.beans.model.FieldMapping;
import com.hotels.beans.model.FieldTransformer;
import com.hotels.beans.sample.FromFooSimple;
import com.hotels.beans.sample.mixed.MixedToFoo;
import com.hotels.beans.sample.mixed.MixedToFooDiffFields;
import com.hotels.beans.sample.mixed.MixedToFooMissingAllArgsConstructor;
import com.hotels.beans.sample.mixed.MixedToFooMissingField;
import com.hotels.beans.sample.mixed.MixedToFooNotExistingFields;

/**
 * Unit test for all {@link Transformer} functions related to Mixed type Java Beans.
 */
public class MixedObjectTransformationTest extends AbstractTransformerTest {
    private static final boolean ACTIVE = true;
    /**
     * The class to be tested.
     */
    @InjectMocks
    private TransformerImpl underTest;

    /**
     * Initialized mocks.
     */
    @BeforeMethod
    public void beforeMethod() {
        initMocks(this);
    }

    /**
     * Test that a Mixed bean with a constructor containing the final field only is correctly copied.
     */
    @Test
    public void testMixedBeanWithoutAllArgsConstructorIsCorrectlyCopied() {
        //GIVEN

        //WHEN
        MixedToFooMissingAllArgsConstructor actual = underTest.transform(fromFoo, MixedToFooMissingAllArgsConstructor.class);

        //THEN
        assertThat(actual, sameBeanAs(fromFoo));
    }

    /**
     * Test that bean containing both final fields and not are correctly copied.
     */
    @Test
    public void testMixedBeanIsCorrectlyCopied() {
        //GIVEN

        //WHEN
        MixedToFoo actual = underTest.transform(fromFoo, MixedToFoo.class);

        //THEN
        assertThat(actual, sameBeanAs(fromFoo));
    }

    /**
     * Test that bean containing both final fields (with different names) and not are correctly copied.
     */
    @Test
    public void testMixedBeanWithDifferentFieldNamesIsCorrectlyCopied() {
        //GIVEN

        //WHEN
        final Transformer beanTransformer = underTest.withFieldMapping(new FieldMapping(ID_FIELD_NAME, IDENTIFIER_FIELD_NAME));
        MixedToFooDiffFields actual = beanTransformer.transform(fromFoo, MixedToFooDiffFields.class);

        //THEN
        assertThat(actual, hasProperty(NAME_FIELD_NAME, equalTo(actual.getName())));
        assertThat(actual, hasProperty(IDENTIFIER_FIELD_NAME, equalTo(fromFoo.getId())));
        assertEquals(actual.getList(), fromFoo.getList());
        IntStream.range(0, actual.getNestedObjectList().size())
                .forEach(i -> assertThat(actual.getNestedObjectList().get(i), sameBeanAs(fromFoo.getNestedObjectList().get(i))));
        assertThat(actual.getNestedObject(), sameBeanAs(fromFoo.getNestedObject()));
    }

    /**
     * Test that bean containing both final fields (with different names) and not are correctly copied through field transformer.
     */
    @Test
    public void testMixedBeanWithDifferentFieldNamesIsCorrectlyCopiedThroughFieldTransformer() {
        //GIVEN
        /* Extended declaration.
         * Function<BigInteger, BigInteger> idTransformer = value -> value.negate();
         * FieldTransformer<BigInteger, BigInteger> fieldTransformer = new FieldTransformer<>("identifier", idTransformer);
         */
        FieldTransformer<BigInteger, BigInteger> fieldTransformer = new FieldTransformer<>(IDENTIFIER_FIELD_NAME, BigInteger::negate);

        //WHEN
        underTest.withFieldMapping(new FieldMapping(ID_FIELD_NAME, IDENTIFIER_FIELD_NAME)).withFieldTransformer(fieldTransformer);
        MixedToFooDiffFields actual = underTest.transform(fromFoo, MixedToFooDiffFields.class);

        //THEN
        assertThat(actual, hasProperty(NAME_FIELD_NAME, equalTo(actual.getName())));
        assertThat(actual, hasProperty(IDENTIFIER_FIELD_NAME, equalTo(fromFoo.getId().negate())));
        assertEquals(actual.getList(), fromFoo.getList());
        IntStream.range(0, actual.getNestedObjectList().size())
                .forEach(i -> assertThat(actual.getNestedObjectList().get(i), sameBeanAs(fromFoo.getNestedObjectList().get(i))));
        assertThat(actual.getNestedObject(), sameBeanAs(fromFoo.getNestedObject()));
    }

    /**
     * Test that the copy method sets the default value when the source object does not contain a required field.
     */
    @Test
    public void testMixedBeanWithMissingFieldsReturnsTheDefaultValueWhenTheSourceObjectDoesNotContainARequiredField() {
        //GIVEN
        underTest.setDefaultValueForMissingField(true);

        //WHEN
        MixedToFooMissingField actual = underTest.transform(fromFoo, MixedToFooMissingField.class);

        assertNull(actual.getFooField());
        underTest.setDefaultValueForMissingField(false);
    }

    /**
     * Test that the copy method raises an exception when the source object does not contain a required field.
     */
    @Test(expectedExceptions = MissingFieldException.class)
    public void testMixedBeanWithMissingFieldsThrowsMissingFieldExceptionWhenTheSourceObjectDoesNotContainARequiredField() {
        //GIVEN
        underTest.setDefaultValueForMissingField(false);

        //WHEN
        underTest.transform(fromFoo, MixedToFooMissingField.class);
    }

    /**
     * Test that a bean containing a field not existing in the source object, but with a transformer function defined for such object is correctly copied.
     */
    @Test
    public void testThatAnyTypeOfBeanContainsANotExistingFieldInTheSourceObjectIsCorrectlyCopiedThroughTransformerFunctions() {
        //GIVEN
        FromFooSimple fromFooSimple = new FromFooSimple(NAME, ID, ACTIVE);
        FieldTransformer<Object, Integer> ageFieldTransformer = new FieldTransformer<>(AGE_FIELD_NAME, val -> AGE);

        //WHEN
        underTest.withFieldTransformer(ageFieldTransformer);
        MixedToFooNotExistingFields mixedObjectBean = underTest.transform(fromFooSimple, MixedToFooNotExistingFields.class);

        //THEN
        assertThat(mixedObjectBean, hasProperty(AGE_FIELD_NAME, equalTo(AGE)));
    }

    /**
     * Test that, given a set of fields for which the transformation has to be skipped, they are actually skipped.
     */
    @Test
    public void testFieldTransformationSkipWorksProperly() {
        //GIVEN
        underTest.skipTransformationForField(NAME_FIELD_NAME, PHONE_NUMBER_NESTED_OBJECT_FIELD_NAME);

        //WHEN
        MixedToFoo actual = underTest.transform(fromFoo, MixedToFoo.class);

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
        MixedToFoo mixedToFoo = new MixedToFoo(null, null, null, null, null);

        //WHEN
        underTest.skipTransformationForField().transform(fromFoo, mixedToFoo);

        //THEN
        assertThat(mixedToFoo, sameBeanAs(fromFoo));
    }
}
