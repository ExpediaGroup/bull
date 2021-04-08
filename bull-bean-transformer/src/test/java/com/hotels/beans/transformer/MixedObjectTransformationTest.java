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
package com.hotels.beans.transformer;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigInteger;

import org.testng.annotations.Test;

import com.hotels.beans.sample.FromFooSimple;
import com.hotels.beans.sample.mixed.MixedToFoo;
import com.hotels.beans.sample.mixed.MixedToFooDiffFields;
import com.hotels.beans.sample.mixed.MixedToFooMissingAllArgsConstructor;
import com.hotels.beans.sample.mixed.MixedToFooMissingField;
import com.hotels.beans.sample.mixed.MixedToFooNotExistingFields;
import com.hotels.transformer.error.MissingFieldException;
import com.hotels.transformer.model.FieldMapping;
import com.hotels.transformer.model.FieldTransformer;

/**
 * Unit test for all {@link BeanTransformer} functions related to Mixed type Java Beans.
 */
public class MixedObjectTransformationTest extends AbstractBeanTransformerTest {
    private static final boolean ACTIVE = true;

    /**
     * Test that a Mixed bean with a constructor containing the final field only is correctly copied.
     */
    @Test
    public void testMixedBeanWithoutAllArgsConstructorIsCorrectlyCopied() {
        //GIVEN

        //WHEN
        MixedToFooMissingAllArgsConstructor actual = underTest.transform(fromFoo, MixedToFooMissingAllArgsConstructor.class);

        //THEN
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(fromFoo);
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
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(fromFoo);
    }

    /**
     * Test that bean containing both final fields (with different names) and not are correctly copied.
     */
    @Test
    public void testMixedBeanWithDifferentFieldNamesIsCorrectlyCopied() {
        //GIVEN

        //WHEN
        BeanTransformer beanTransformer = underTest.withFieldMapping(new FieldMapping<>(ID_FIELD_NAME, IDENTIFIER_FIELD_NAME));
        MixedToFooDiffFields actual = beanTransformer.transform(fromFoo, MixedToFooDiffFields.class);

        //THEN
        assertThat(actual).hasFieldOrPropertyWithValue(IDENTIFIER_FIELD_NAME, fromFoo.getId())
                .usingRecursiveComparison()
                .ignoringFields(IDENTIFIER_FIELD_NAME)
                .isEqualTo(fromFoo);
    }

    /**
     * Test that bean containing both final fields (with different names) and not are correctly copied through field transformer.
     */
    @Test
    public void testMixedBeanWithDifferentFieldNamesIsCorrectlyCopiedThroughFieldTransformer() {
        //GIVEN
        /* Extended FieldTransformer function declaration.
         * Function<BigInteger, BigInteger> idTransformer = value -> value.negate();
         * FieldTransformer<BigInteger, BigInteger> fieldTransformer = new FieldTransformer<>("identifier", idTransformer);
         */
        FieldTransformer<BigInteger, BigInteger> fieldTransformer = new FieldTransformer<>(IDENTIFIER_FIELD_NAME, BigInteger::negate);

        //WHEN
        underTest.withFieldMapping(new FieldMapping<>(ID_FIELD_NAME, IDENTIFIER_FIELD_NAME)).withFieldTransformer(fieldTransformer);
        MixedToFooDiffFields actual = underTest.transform(fromFoo, MixedToFooDiffFields.class);

        //THEN
        assertThat(actual).hasFieldOrPropertyWithValue(IDENTIFIER_FIELD_NAME, fromFoo.getId().negate())
                .usingRecursiveComparison()
                .ignoringFields(IDENTIFIER_FIELD_NAME)
                .isEqualTo(fromFoo);
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

        assertThat(actual.getFooField()).isNull();
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
        assertThat(mixedObjectBean).hasFieldOrPropertyWithValue(AGE_FIELD_NAME, AGE);
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
        assertThat(actual).hasNoNullFieldsOrPropertiesExcept(NAME_FIELD_NAME, PHONE_NUMBER_NESTED_OBJECT_FIELD_NAME);
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
        assertThat(mixedToFoo).usingRecursiveComparison()
                .isEqualTo(fromFoo);
    }
}
