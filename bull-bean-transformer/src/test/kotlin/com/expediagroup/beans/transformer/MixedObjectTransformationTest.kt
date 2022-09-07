/**
 * Copyright (C) 2019-2022 Expedia, Inc.
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
package com.expediagroup.beans.transformer

import com.expediagroup.beans.sample.FromFooSimple
import com.expediagroup.beans.sample.mixed.MixedToFoo
import com.expediagroup.beans.sample.mixed.MixedToFooDiffFields
import com.expediagroup.beans.sample.mixed.MixedToFooMissingAllArgsConstructor
import com.expediagroup.beans.sample.mixed.MixedToFooMissingField
import com.expediagroup.beans.sample.mixed.MixedToFooNotExistingFields
import com.expediagroup.beans.sample.mixed.MixedToFooSimpleNotExistingFields
import com.expediagroup.transformer.error.MissingFieldException
import com.expediagroup.transformer.model.FieldMapping
import com.expediagroup.transformer.model.FieldTransformer
import org.assertj.core.api.Assertions
import org.testng.annotations.Test
import java.math.BigInteger

/**
 * Unit test for all [BeanTransformer] functions related to Mixed type Java Beans.
 */
class MixedObjectTransformationTest : AbstractBeanTransformerTest() {
    /**
     * Test that a Mixed bean with a constructor containing the final field only is correctly copied.
     */
    @Test
    fun testMixedBeanWithoutAllArgsConstructorIsCorrectlyCopied() {
        // GIVEN

        // WHEN
        val actual = underTest.transform(fromFoo, MixedToFooMissingAllArgsConstructor::class.java)

        // THEN
        Assertions.assertThat(actual).usingRecursiveComparison()
            .isEqualTo(fromFoo)
    }

    /**
     * Test that bean containing both final fields and not are correctly copied.
     */
    @Test
    fun testMixedBeanIsCorrectlyCopied() {
        // GIVEN

        // WHEN
        val actual = underTest.transform(fromFoo, MixedToFoo::class.java)

        // THEN
        Assertions.assertThat(actual).usingRecursiveComparison()
            .isEqualTo(fromFoo)
    }

    /**
     * Test that bean containing both final fields (with different names) and not are correctly copied.
     */
    @Test
    fun testMixedBeanWithDifferentFieldNamesIsCorrectlyCopied() {
        // GIVEN

        // WHEN
        val beanTransformer = underTest.withFieldMapping(FieldMapping(ID_FIELD_NAME, IDENTIFIER_FIELD_NAME))
        val actual = beanTransformer.transform(fromFoo, MixedToFooDiffFields::class.java)

        // THEN
        Assertions.assertThat(actual).hasFieldOrPropertyWithValue(IDENTIFIER_FIELD_NAME, fromFoo.id)
            .usingRecursiveComparison()
            .ignoringFields(IDENTIFIER_FIELD_NAME)
            .isEqualTo(fromFoo)
    }

    /**
     * Test that bean containing both final fields (with different names) and not are correctly copied through field transformer.
     */
    @Test
    fun testMixedBeanWithDifferentFieldNamesIsCorrectlyCopiedThroughFieldTransformer() {
        // GIVEN
        /* Extended FieldTransformer function declaration.
         * Function<BigInteger, BigInteger> idTransformer = value -> value.negate();
         * FieldTransformer<BigInteger, BigInteger> fieldTransformer = new FieldTransformer<>("identifier", idTransformer);
         */
        val fieldTransformer = FieldTransformer(IDENTIFIER_FIELD_NAME) { obj: BigInteger -> obj.negate() }

        // WHEN
        underTest.withFieldMapping(FieldMapping(ID_FIELD_NAME, IDENTIFIER_FIELD_NAME))
            .withFieldTransformer(fieldTransformer)
        val actual = underTest.transform(fromFoo, MixedToFooDiffFields::class.java)

        // THEN
        Assertions.assertThat(actual).hasFieldOrPropertyWithValue(IDENTIFIER_FIELD_NAME, fromFoo.id!!.negate())
            .usingRecursiveComparison()
            .ignoringFields(IDENTIFIER_FIELD_NAME)
            .isEqualTo(fromFoo)
    }

    /**
     * Test that the copy method sets the default value when the source object does not contain a required field.
     */
    @Test
    fun testMixedBeanWithMissingFieldsReturnsTheDefaultValueWhenTheSourceObjectDoesNotContainARequiredField() {
        // GIVEN
        underTest.setDefaultValueForMissingField(true)

        // WHEN
        val actual = underTest.transform(fromFoo, MixedToFooMissingField::class.java)
        Assertions.assertThat(actual.fooField).isNull()
        underTest.setDefaultValueForMissingField(false)
    }

    /**
     * Test that the copy method raises an exception when the source object does not contain a required field.
     */
    @Test(expectedExceptions = [MissingFieldException::class])
    fun testMixedBeanWithMissingFieldsThrowsMissingFieldExceptionWhenTheSourceObjectDoesNotContainARequiredField() {
        // GIVEN
        underTest.setDefaultValueForMissingField(false)

        // WHEN
        underTest.transform(fromFoo, MixedToFooMissingField::class.java)
    }

    /**
     * Test that a bean containing a field not existing in the source object, but with a transformer function defined for such object is correctly copied.
     */
    @Test
    fun testThatAnyTypeOfBeanContainsANotExistingFieldInTheSourceObjectIsCorrectlyCopiedThroughTransformerFunctions() {
        // GIVEN
        val fromFooSimple = FromFooSimple(NAME, ID, ACTIVE)
        val ageFieldTransformer = FieldTransformer(AGE_FIELD_NAME) { `val`: Any? -> AGE }

        // WHEN
        underTest.withFieldTransformer(ageFieldTransformer)
        val mixedObjectBean = underTest.transform(fromFooSimple, MixedToFooNotExistingFields::class.java)

        // THEN
        Assertions.assertThat(mixedObjectBean).hasFieldOrPropertyWithValue(AGE_FIELD_NAME, AGE)
    }

    /**
     * Test that, given a set of fields for which the transformation has to be skipped, they are actually skipped.
     */
    @Test
    fun testFieldTransformationSkipWorksProperly() {
        // GIVEN
        underTest.skipTransformationForField(NAME_FIELD_NAME, PHONE_NUMBER_NESTED_OBJECT_FIELD_NAME)

        // WHEN
        val actual = underTest.transform(fromFoo, MixedToFoo::class.java)

        // THEN
        Assertions.assertThat(actual)
            .hasNoNullFieldsOrPropertiesExcept(NAME_FIELD_NAME, PHONE_NUMBER_NESTED_OBJECT_FIELD_NAME)
    }

    /**
     * Test transformation on an existing bean is correctly copied.
     */
    @Test
    fun testTransformationOnAnExistingDestinationWorksProperly() {
        // GIVEN
        val presetFieldId = BigInteger.valueOf(777)
        val fieldNameToIgnore = "id"
        val mixedToFoo = MixedToFoo(presetFieldId, null, null, null, null)

        // WHEN
        underTest.skipTransformationForField(fieldNameToIgnore).transform(fromFoo, mixedToFoo)

        // THEN
        Assertions.assertThat(mixedToFoo).usingRecursiveComparison()
            .ignoringFields(fieldNameToIgnore)
            .isEqualTo(fromFoo)
        Assertions.assertThat(mixedToFoo.id).isEqualTo(presetFieldId)
    }

    /**
     * Test that bean containing both final fields (with different names) and not with a source field mapped into
     * multiple destination fields is correctly copied.
     */
    @Test
    fun testMixedBeanWithASourceFieldMappedIntoMultipleDestinationFieldsIsCorrectlyCopied() {
        // GIVEN
        val multipleDestinationFieldMapping = FieldMapping(
            ID_FIELD_NAME,
            ID_FIELD_NAME,
            INDEX_FIELD_NAME
        )

        // WHEN
        val actual = underTest
            .withFieldMapping(multipleDestinationFieldMapping)
            .setDefaultValueForMissingField(true)
            .transform(fromFooSimple, MixedToFooSimpleNotExistingFields::class.java)

        // THEN
        Assertions.assertThat(actual)
            .hasFieldOrPropertyWithValue(ID_FIELD_NAME, fromFoo.id)
            .hasFieldOrPropertyWithValue(INDEX_FIELD_NAME, fromFoo.id)
            .usingRecursiveComparison()
            .ignoringFields(INDEX_FIELD_NAME, AGE_FIELD_NAME, ACTIVE_FIELD_NAME)
            .isEqualTo(fromFoo)
        underTest.reset()
    }

    /**
     * Test that bean containing both final fields (with different names) and not with a source field mapped into
     * multiple destination fields and field transformeration applies on mutliple fields is correctly copied.
     */
    @Test
    fun testMixedBeanWithASourceFieldMappedIntoMultipleDestinationFieldsAndMultipleFieldTransformerIsCorrectlyCopied() {
        // GIVEN
        val valueToAdd = BigInteger.ONE
        val expectedIdValue = fromFooSimple.id!!.add(valueToAdd)
        val multipleDestinationFieldMapping = FieldMapping(
            ID_FIELD_NAME,
            ID_FIELD_NAME,
            INDEX_FIELD_NAME
        )
        val multipleDestinationFieldTransformer = FieldTransformer(
            listOf(ID_FIELD_NAME, INDEX_FIELD_NAME)
        ) { `val`: BigInteger -> `val`.add(valueToAdd) }

        // WHEN
        val actual = underTest
            .withFieldMapping(multipleDestinationFieldMapping)
            .withFieldTransformer(multipleDestinationFieldTransformer)
            .setDefaultValueForMissingField(true)
            .transform(fromFooSimple, MixedToFooSimpleNotExistingFields::class.java)

        // THEN
        Assertions.assertThat(actual)
            .hasFieldOrPropertyWithValue(ID_FIELD_NAME, expectedIdValue)
            .hasFieldOrPropertyWithValue(INDEX_FIELD_NAME, expectedIdValue)
            .usingRecursiveComparison()
            .ignoringFields(ID_FIELD_NAME, INDEX_FIELD_NAME, AGE_FIELD_NAME, ACTIVE_FIELD_NAME)
            .isEqualTo(fromFoo)
        underTest.reset()
    }

    companion object {
        private const val ACTIVE = true
        private const val INDEX_FIELD_NAME = "index"
    }
}
