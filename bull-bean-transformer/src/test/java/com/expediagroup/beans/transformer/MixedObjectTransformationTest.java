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
package com.expediagroup.beans.transformer;

import static java.math.BigInteger.ONE;

import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigInteger;
import java.util.List;

import org.testng.annotations.Test;

import com.expediagroup.beans.sample.FromFooContainingList;
import com.expediagroup.beans.sample.FromFooSimple;
import com.expediagroup.beans.sample.FromFooSimpleNested;
import com.expediagroup.beans.sample.FromFooWithFullName;
import com.expediagroup.beans.sample.FromFooWithPrimitiveAndNestedObject;
import com.expediagroup.beans.sample.ToFooNestedWithDiffFieldName;
import com.expediagroup.beans.sample.ToFooWithNestedFieldMapping;
import com.expediagroup.beans.sample.immutable.ImmutableToFooContainingList;
import com.expediagroup.beans.sample.immutable.ImmutableToFooWithSplitName;
import com.expediagroup.beans.sample.mixed.MixedToFoo;
import com.expediagroup.beans.sample.mixed.MixedToFooDiffFields;
import com.expediagroup.beans.sample.mixed.MixedToFooMissingAllArgsConstructor;
import com.expediagroup.beans.sample.mixed.MixedToFooMissingField;
import com.expediagroup.beans.sample.mixed.MixedToFooNotExistingFields;
import com.expediagroup.beans.sample.mixed.MixedToFooSimpleNotExistingFields;
import com.expediagroup.beans.sample.mutable.MutableToFooWithNestedFieldMapping;
import com.expediagroup.transformer.error.MissingFieldException;
import com.expediagroup.transformer.model.FieldMapping;
import com.expediagroup.transformer.model.FieldTransformer;

/**
 * Unit test for all {@link BeanTransformer} functions related to Mixed type Java Beans.
 */
public class MixedObjectTransformationTest extends AbstractBeanTransformerTest {
    private static final boolean ACTIVE = true;
    private static final String INDEX_FIELD_NAME = "index";
    private static final int EXPECTED_X_VALUE = 20;

    /**
     * Test that a Mixed bean with a constructor containing the final field only is correctly copied.
     */
    @Test
    public void testMixedBeanWithoutAllArgsConstructorIsCorrectlyCopied() {
        // GIVEN

        // WHEN
        MixedToFooMissingAllArgsConstructor actual = underTest.transform(fromFoo, MixedToFooMissingAllArgsConstructor.class);

        // THEN
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(fromFoo);
    }

    /**
     * Test that bean containing both final fields and not are correctly copied.
     */
    @Test
    public void testMixedBeanIsCorrectlyCopied() {
        // GIVEN

        // WHEN
        MixedToFoo actual = underTest.transform(fromFoo, MixedToFoo.class);

        // THEN
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(fromFoo);
    }

    /**
     * Test that bean containing both final fields (with different names) and not are correctly copied.
     */
    @Test
    public void testMixedBeanWithDifferentFieldNamesIsCorrectlyCopied() {
        // GIVEN

        // WHEN
        BeanTransformer beanTransformer = underTest.withFieldMapping(new FieldMapping<>(ID_FIELD_NAME, IDENTIFIER_FIELD_NAME));
        MixedToFooDiffFields actual = beanTransformer.transform(fromFoo, MixedToFooDiffFields.class);

        // THEN
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
        // GIVEN
        /* Extended FieldTransformer function declaration.
         * Function<BigInteger, BigInteger> idTransformer = value -> value.negate();
         * FieldTransformer<BigInteger, BigInteger> fieldTransformer = new FieldTransformer<>("identifier", idTransformer);
         */
        FieldTransformer<BigInteger, BigInteger> fieldTransformer = new FieldTransformer<>(IDENTIFIER_FIELD_NAME, BigInteger::negate);

        // WHEN
        underTest.withFieldMapping(new FieldMapping<>(ID_FIELD_NAME, IDENTIFIER_FIELD_NAME)).withFieldTransformer(fieldTransformer);
        MixedToFooDiffFields actual = underTest.transform(fromFoo, MixedToFooDiffFields.class);

        // THEN
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
        // GIVEN
        underTest.setDefaultValueForMissingField(true);

        // WHEN
        MixedToFooMissingField actual = underTest.transform(fromFoo, MixedToFooMissingField.class);

        assertThat(actual.getFooField()).isNull();
        underTest.setDefaultValueForMissingField(false);
    }

    /**
     * Test that the copy method raises an exception when the source object does not contain a required field.
     */
    @Test(expectedExceptions = MissingFieldException.class)
    public void testMixedBeanWithMissingFieldsThrowsMissingFieldExceptionWhenTheSourceObjectDoesNotContainARequiredField() {
        // GIVEN
        underTest.setDefaultValueForMissingField(false);

        // WHEN
        underTest.transform(fromFoo, MixedToFooMissingField.class);
    }

    /**
     * Test that a bean containing a field not existing in the source object, but with a transformer function defined for such object is correctly copied.
     */
    @Test
    public void testThatAnyTypeOfBeanContainsANotExistingFieldInTheSourceObjectIsCorrectlyCopiedThroughTransformerFunctions() {
        // GIVEN
        FromFooSimple fromFooSimple = new FromFooSimple(NAME, ID, ACTIVE);
        FieldTransformer<Object, Integer> ageFieldTransformer = new FieldTransformer<>(AGE_FIELD_NAME, val -> AGE);

        // WHEN
        underTest.withFieldTransformer(ageFieldTransformer);
        MixedToFooNotExistingFields mixedObjectBean = underTest.transform(fromFooSimple, MixedToFooNotExistingFields.class);

        // THEN
        assertThat(mixedObjectBean).hasFieldOrPropertyWithValue(AGE_FIELD_NAME, AGE);
    }

    /**
     * Test that, given a set of fields for which the transformation has to be skipped, they are actually skipped.
     */
    @Test
    public void testFieldTransformationSkipWorksProperly() {
        // GIVEN
        underTest.skipTransformationForField(NAME_FIELD_NAME, PHONE_NUMBER_NESTED_OBJECT_FIELD_NAME);

        // WHEN
        MixedToFoo actual = underTest.transform(fromFoo, MixedToFoo.class);

        // THEN
        assertThat(actual).hasNoNullFieldsOrPropertiesExcept(NAME_FIELD_NAME, PHONE_NUMBER_NESTED_OBJECT_FIELD_NAME);
    }

    /**
     * Test transformation on an existing bean is correctly copied.
     */
    @Test
    public void testTransformationOnAnExistingDestinationWorksProperly() {
        // GIVEN
        final BigInteger presetFieldId = BigInteger.valueOf(777);
        final String fieldNameToIgnore = "id";
        MixedToFoo mixedToFoo = new MixedToFoo(presetFieldId, null, null, null, null);

        // WHEN
        underTest.skipTransformationForField(fieldNameToIgnore).transform(fromFoo, mixedToFoo);

        // THEN
        assertThat(mixedToFoo).usingRecursiveComparison()
                .ignoringFields(fieldNameToIgnore)
                .isEqualTo(fromFoo);
        assertThat(mixedToFoo.getId()).isEqualTo(presetFieldId);
    }

    /**
     * Test that bean containing both final fields (with different names) and not with a source field mapped into
     * multiple destination fields is correctly copied.
     */
    @Test
    public void testMixedBeanWithASourceFieldMappedIntoMultipleDestinationFieldsIsCorrectlyCopied() {
        // GIVEN
        var multipleDestinationFieldMapping = new FieldMapping<>(ID_FIELD_NAME,
                ID_FIELD_NAME, INDEX_FIELD_NAME);

        // WHEN
        var actual = underTest
                .withFieldMapping(multipleDestinationFieldMapping)
                .setDefaultValueForMissingField(true)
                .transform(fromFooSimple, MixedToFooSimpleNotExistingFields.class);

        // THEN
        assertThat(actual)
                .hasFieldOrPropertyWithValue(ID_FIELD_NAME, fromFoo.getId())
                .hasFieldOrPropertyWithValue(INDEX_FIELD_NAME, fromFoo.getId())
                .usingRecursiveComparison()
                .ignoringFields(INDEX_FIELD_NAME, AGE_FIELD_NAME, ACTIVE_FIELD_NAME)
                .isEqualTo(fromFoo);
        underTest.reset();
    }

    /**
     * Test that bean containing both final fields (with different names) and not with a source field mapped into
     * multiple destination fields and field transformeration applies on mutliple fields is correctly copied.
     */
    @Test
    public void testMixedBeanWithASourceFieldMappedIntoMultipleDestinationFieldsAndMultipleFieldTransformerIsCorrectlyCopied() {
        // GIVEN
        var valueToAdd = ONE;
        var expectedIdValue = fromFooSimple.getId().add(valueToAdd);
        var multipleDestinationFieldMapping = new FieldMapping<>(ID_FIELD_NAME,
                ID_FIELD_NAME, INDEX_FIELD_NAME);
        var multipleDestinationFieldTransformer =
                new FieldTransformer<BigInteger, BigInteger>(List.of(ID_FIELD_NAME, INDEX_FIELD_NAME),
                        val -> val.add(valueToAdd));

        // WHEN
        var actual = underTest
                .withFieldMapping(multipleDestinationFieldMapping)
                .withFieldTransformer(multipleDestinationFieldTransformer)
                .setDefaultValueForMissingField(true)
                .transform(fromFooSimple, MixedToFooSimpleNotExistingFields.class);

        // THEN
        assertThat(actual)
                .hasFieldOrPropertyWithValue(ID_FIELD_NAME, expectedIdValue)
                .hasFieldOrPropertyWithValue(INDEX_FIELD_NAME, expectedIdValue)
                .usingRecursiveComparison()
                .ignoringFields(ID_FIELD_NAME, INDEX_FIELD_NAME, AGE_FIELD_NAME, ACTIVE_FIELD_NAME)
                .isEqualTo(fromFoo);
        underTest.reset();
    }

    /**
     * Test that a primitive field from the source root can be mapped into a nested destination field.
     * This verifies the fix for issue #559.
     */
    @Test
    public void testPrimitiveFieldMappedIntoNestedObject() {
        // GIVEN
        FromFooWithPrimitiveAndNestedObject source = new FromFooWithPrimitiveAndNestedObject(
                "Lucas", new FromFooSimpleNested("Mendes", 1L), EXPECTED_X_VALUE);

        // WHEN
        ToFooWithNestedFieldMapping actual = underTest
                .withFieldMapping(new FieldMapping<>("x", "nestedObject.x"))
                .transform(source, ToFooWithNestedFieldMapping.class);

        // THEN
        assertThat(actual.getName()).isEqualTo("Lucas");
        assertThat(actual.getNestedObject().getName()).isEqualTo("Mendes");
        assertThat(actual.getNestedObject().getIndex()).isEqualTo(1L);
        assertThat(actual.getNestedObject().getX()).isEqualTo(EXPECTED_X_VALUE);
        underTest.reset();
    }

    /**
     * Test that a primitive field mapped into a nested destination works via the setter-injection path
     * (mutable destination bean). This exercises the transform(T, K, String) overload and
     * the getFieldValue overload with breadcrumb-aware root resolution.
     */
    @Test
    public void testPrimitiveFieldMappedIntoNestedMutableObject() {
        // GIVEN
        FromFooWithPrimitiveAndNestedObject source = new FromFooWithPrimitiveAndNestedObject(
                "Lucas", new FromFooSimpleNested("Mendes", 1L), EXPECTED_X_VALUE);

        // WHEN
        MutableToFooWithNestedFieldMapping actual = underTest
                .withFieldMapping(new FieldMapping<>("x", "nestedObject.x"))
                .transform(source, MutableToFooWithNestedFieldMapping.class);

        // THEN
        assertThat(actual.getName()).isEqualTo("Lucas");
        assertThat(actual.getNestedObject().getName()).isEqualTo("Mendes");
        assertThat(actual.getNestedObject().getIndex()).isEqualTo(1L);
        assertThat(actual.getNestedObject().getX()).isEqualTo(EXPECTED_X_VALUE);
        underTest.reset();
    }

    /**
     * Test that breadcrumb-based mapping works when source and destination field names differ.
     * Maps "x" from root source into "nestedObject.y" in the destination, verifying the lookup
     * does not accidentally fall through to matching by field name alone.
     */
    @Test
    public void testPrimitiveFieldMappedIntoNestedObjectWithDifferentFieldName() {
        // GIVEN
        FromFooWithPrimitiveAndNestedObject source = new FromFooWithPrimitiveAndNestedObject(
                "Lucas", new FromFooSimpleNested("Mendes", 1L), EXPECTED_X_VALUE);

        // WHEN
        ToFooNestedWithDiffFieldName actual = underTest
                .withFieldMapping(new FieldMapping<>("x", "nestedObject.y"))
                .transform(source, ToFooNestedWithDiffFieldName.class);

        // THEN
        assertThat(actual.getName()).isEqualTo("Lucas");
        assertThat(actual.getNestedObject().getName()).isEqualTo("Mendes");
        assertThat(actual.getNestedObject().getIndex()).isEqualTo(1L);
        assertThat(actual.getNestedObject().getY()).isEqualTo(EXPECTED_X_VALUE);
        underTest.reset();
    }

    /**
     * Test that a zero (default int) value is correctly mapped through breadcrumb-based resolution.
     * Ensures there is no confusion between "field not found" and "field value is default".
     */
    @Test
    public void testPrimitiveFieldMappedIntoNestedObjectWithZeroValue() {
        // GIVEN
        int zeroValue = 0;
        FromFooWithPrimitiveAndNestedObject source = new FromFooWithPrimitiveAndNestedObject(
                "Lucas", new FromFooSimpleNested("Mendes", 1L), zeroValue);

        // WHEN
        ToFooWithNestedFieldMapping actual = underTest
                .withFieldMapping(new FieldMapping<>("x", "nestedObject.x"))
                .transform(source, ToFooWithNestedFieldMapping.class);

        // THEN
        assertThat(actual.getName()).isEqualTo("Lucas");
        assertThat(actual.getNestedObject().getX()).isEqualTo(zeroValue);
        underTest.reset();
    }

    /**
     * Test that when no FieldMapping is provided and the nested destination has a field
     * that does not exist in the nested source, MissingFieldException is thrown.
     * This confirms the transformer does not silently resolve unmapped fields from the root.
     */
    @Test(expectedExceptions = MissingFieldException.class)
    public void testNestedFieldWithoutMappingThrowsMissingFieldException() {
        // GIVEN
        FromFooWithPrimitiveAndNestedObject source = new FromFooWithPrimitiveAndNestedObject(
                "Lucas", new FromFooSimpleNested("Mendes", 1L), EXPECTED_X_VALUE);

        // WHEN - no field mapping defined, nested destination has 'x' but nested source does not
        underTest.transform(source, ToFooWithNestedFieldMapping.class);
    }

    /**
     * Test that multiple root-level primitive fields can be mapped into the same nested
     * destination object simultaneously.
     */
    @Test
    public void testMultipleRootFieldsMappedIntoSameNestedObject() {
        // GIVEN
        FromFooWithPrimitiveAndNestedObject source = new FromFooWithPrimitiveAndNestedObject(
                "Lucas", new FromFooSimpleNested("Mendes", 1L), EXPECTED_X_VALUE);

        // WHEN
        ToFooWithNestedFieldMapping actual = underTest
                .withFieldMapping(new FieldMapping<>("x", "nestedObject.x"))
                .withFieldMapping(new FieldMapping<>("name", "nestedObject.name"))
                .transform(source, ToFooWithNestedFieldMapping.class);

        // THEN
        assertThat(actual.getNestedObject().getX()).isEqualTo(EXPECTED_X_VALUE);
        assertThat(actual.getNestedObject().getName()).isEqualTo("Lucas");
        underTest.reset();
    }

    /**
     * Test that a field transformer applied with flat field name transformation correctly uses
     * the collection element as the source, not the root object.
     * Regression test: in 3.0.1, resolveEffectiveSource matched the flat mapping ("name" -> "fullName")
     * when the breadcrumb was null (collection element path), redirecting the source lookup to the
     * root object which had no "fullName" field. The silenced MissingFieldException left the value
     * as null, causing an NPE in the transformer function.
     */
    @Test
    public void testFieldTransformerOnCollectionElementsUsesElementNotRootAsSource() {
        // GIVEN
        FromFooContainingList source = new FromFooContainingList("root-id",
                List.of(new FromFooWithFullName("Donald Duck"), new FromFooWithFullName("Daisy Duck")));

        // WHEN
        ImmutableToFooContainingList actual = underTest
                .setFlatFieldNameTransformation(true)
                .withFieldMapping(new FieldMapping<>("fullName", "name"))
                .withFieldMapping(new FieldMapping<>("fullName", "surname"))
                .withFieldTransformer(new FieldTransformer<String, String>("name", fullName -> fullName.split(SPACE)[0]))
                .withFieldTransformer(new FieldTransformer<String, String>("surname", fullName -> fullName.split(SPACE)[1]))
                .transform(source, ImmutableToFooContainingList.class);

        // THEN
        assertThat(actual.getId()).isEqualTo("root-id");
        assertThat(actual.getItems()).hasSize(2);
        assertThat(actual.getItems().get(0).getName()).isEqualTo("Donald");
        assertThat(actual.getItems().get(0).getSurname()).isEqualTo("Duck");
        assertThat(actual.getItems().get(1).getName()).isEqualTo("Daisy");
        assertThat(actual.getItems().get(1).getSurname()).isEqualTo("Duck");
        underTest.reset();
    }

    /**
     * Test that multiple items in a collection are all correctly transformed using their own
     * element as the source, not the root. Verifies no cross-contamination between elements.
     */
    @Test
    public void testAllCollectionElementsAreIndependentlyTransformedFromTheirOwnSource() {
        // GIVEN
        FromFooContainingList source = new FromFooContainingList("root-id",
                List.of(new FromFooWithFullName("Tony Stark"), new FromFooWithFullName("Bruce Banner"),
                        new FromFooWithFullName("Steve Rogers")));

        // WHEN
        ImmutableToFooContainingList actual = underTest
                .setFlatFieldNameTransformation(true)
                .withFieldMapping(new FieldMapping<>("fullName", "name"))
                .withFieldMapping(new FieldMapping<>("fullName", "surname"))
                .withFieldTransformer(new FieldTransformer<String, String>("name", fullName -> fullName.split(SPACE)[0]))
                .withFieldTransformer(new FieldTransformer<String, String>("surname", fullName -> fullName.split(SPACE)[1]))
                .transform(source, ImmutableToFooContainingList.class);

        // THEN
        assertThat(actual.getItems()).extracting(ImmutableToFooWithSplitName::getName)
                .containsExactly("Tony", "Bruce", "Steve");
        assertThat(actual.getItems()).extracting(ImmutableToFooWithSplitName::getSurname)
                .containsExactly("Stark", "Banner", "Rogers");
        underTest.reset();
    }
}
