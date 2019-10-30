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

import static java.lang.Integer.parseInt;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.hotels.beans.sample.FromFooNoField;
import com.hotels.beans.sample.FromFooSimple;
import com.hotels.beans.sample.FromFooSimpleNoGetters;
import com.hotels.beans.sample.mixed.MutableToFooOnlyPrimitiveTypes;
import com.hotels.beans.sample.mutable.MutableToFoo;
import com.hotels.beans.sample.mutable.MutableToFooInvalid;
import com.hotels.beans.sample.mutable.MutableToFooNotExistingFields;
import com.hotels.beans.sample.mutable.MutableToFooSimple;
import com.hotels.beans.sample.mutable.MutableToFooSimpleNoSetters;
import com.hotels.beans.sample.mutable.MutableToFooSubClass;
import com.hotels.transformer.error.InvalidBeanException;
import com.hotels.transformer.error.MissingFieldException;
import com.hotels.transformer.model.FieldTransformer;

/**
 * Unit test for all {@link BeanTransformer} functions related to Mutable type Java Beans.
 */
public class MutableObjectTransformationTest extends AbstractBeanTransformerTest {
    private static final boolean ACTIVE = true;
    private static final String PRICE_FIELD_NAME = "price";

    /**
     * Test that an exception is thrown if there is no default constructor defined for the mutable bean object.
     */
    @Test(expectedExceptions = InvalidBeanException.class)
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

    /**
     * Test that no exception is thrown if the destination object don't met the constraints and the validation is disabled.
     */
    @Test
    public void testTransformThrowsNoExceptionIfTheDestinationObjectValuesAreNotValidAndTheValidationIsDisabled() {
        //GIVEN
        MutableToFooSubClass mutableToFoo = new MutableToFooSubClass();
        fromFooSubClass.setId(null);

        //WHEN
        underTest.transform(fromFooSubClass, mutableToFoo);

        //THEN
        assertThat(mutableToFoo, sameBeanAs(fromFooSubClass));
        fromFooSubClass.setId(ID);
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
        FieldTransformer<String, String> nameTransformer = new FieldTransformer<>(NAME_FIELD_NAME, val -> namePrefix + val);

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
        FromFooSimpleNoGetters fromFooSimpleNoGetters = new FromFooSimpleNoGetters(NAME, ID, ACTIVE);

        //WHEN
        MutableToFooSimpleNoSetters actual = underTest.transform(fromFooSimpleNoGetters, MutableToFooSimpleNoSetters.class);

        //THEN
        assertThat(actual, sameBeanAs(fromFooSimpleNoGetters));
    }

    /**
     * Test that the transformer does not sets the default value for primitive type field in case it's disabled.
     */
    @Test
    public void testTransformerDoesNotSetsTheDefaultValueForPrimitiveTypeField() {
        //GIVEN
        FromFooSimpleNoGetters fromFooSimpleNoGetters = new FromFooSimpleNoGetters(NAME, null, ACTIVE);
        underTest.setDefaultValueSetEnabled(false);

        //WHEN
        MutableToFooSimpleNoSetters actual = underTest.transform(fromFooSimpleNoGetters, MutableToFooSimpleNoSetters.class);

        //THEN
        assertThat(actual, sameBeanAs(fromFooSimpleNoGetters));
        underTest.setDefaultValueSetEnabled(true);
    }

    /**
     * Test that the transformer is able to copy object even if the source object has no fields but has getter methods.
     */
    @Test
    public void testTransformerIsAbleToCopyObjectsWithoutFieldButWithGetterMethods() {
        //GIVEN
        FromFooNoField fromFooNoField = new FromFooNoField();

        //WHEN
        MutableToFooSimpleNoSetters actual = underTest.transform(fromFooNoField, MutableToFooSimpleNoSetters.class);

        //THEN
        assertEquals(actual.getId(), fromFooNoField.getId());
        assertEquals(actual.getName(), fromFooNoField.getName());
        assertEquals(actual.isActive(), fromFooNoField.isActive());
    }

    /**
     * Test that a bean containing a field not existing in the source object, but with a transformer function defined for such object is correctly copied.
     */
    @Test
    public void testThatAnyTypeOfBeanContainsANotExistingFieldInTheSourceObjectIsCorrectlyCopiedThroughTransformerFunctions() {
        //GIVEN
        FromFooSimple fromFooSimple = new FromFooSimple(NAME, ID, ACTIVE);
        FieldTransformer<Object, Integer> ageFieldTransformer = new FieldTransformer<>(AGE_FIELD_NAME, () -> AGE);

        //WHEN
        underTest.withFieldTransformer(ageFieldTransformer);
        MutableToFooNotExistingFields mutableObjectBean = underTest.transform(fromFooSimple, MutableToFooNotExistingFields.class);

        //THEN
        assertThat(mutableObjectBean, hasProperty(AGE_FIELD_NAME, equalTo(AGE)));
        underTest.resetFieldsTransformer();
    }

    /**
     * Test that a bean containing a field not existing in the source object, and without a transformer function defined throws
     * MissingFieldException even if the primitiveTypeConversionFunction is enabled.
     */
    @Test
    public void testTransformerThrowsExceptionIfAFieldIsMissingAndThePrimitiveTypeConversionIsEnabled() {
        //GIVEN
        FromFooSimple fromFooSimple = new FromFooSimple(NAME, ID, ACTIVE);
        underTest.setPrimitiveTypeConversionEnabled(true);

        //WHEN
        Exception raisedException = null;
        try {
            underTest.transform(fromFooSimple, MutableToFooNotExistingFields.class);
        } catch (final Exception e) {
            raisedException = e;
        }

        //THEN
        assertNotNull(raisedException);
        assertEquals(MissingFieldException.class, raisedException.getCause().getClass());
        underTest.setPrimitiveTypeConversionEnabled(false);
    }

    /**
     * Test that a bean containing a field not existing in the source object, and without a transformer function defined,
     * does not throws MissingFieldException and the primitiveTypeConversionFunction is enabled.
     */
    @Test
    public void testTransformerDoesNotThrowExceptionIfAFieldIsMissingAndTheDefaultValueSetIsEnabled() {
        //GIVEN
        FromFooSimple fromFooSimple = new FromFooSimple(NAME, ID, ACTIVE);
        underTest.setPrimitiveTypeConversionEnabled(true).setDefaultValueForMissingField(true);

        //WHEN
        MutableToFooNotExistingFields actual = underTest.transform(fromFooSimple, MutableToFooNotExistingFields.class);

        //THEN
        assertEquals(actual.getId(), fromFooSimple.getId());
        assertEquals(actual.getName(), fromFooSimple.getName());
        underTest.setPrimitiveTypeConversionEnabled(false).setDefaultValueForMissingField(false);
    }

    /**
     * Test transformation field with field transformer.
     * @param testCaseDescription the test case description
     * @param fieldToTransform the name of the field on which apply the transformation
     * @param transformationResult the value to return after the transformation
     */
    @Test(dataProvider = "dataTransformationTesting")
    public void testTransformationWithFieldTransformationWorksProperly(final String testCaseDescription, final String fieldToTransform, final Object transformationResult) {
        //GIVEN
        FromFooSimple fromFooSimple = new FromFooSimple(NAME, ID, ACTIVE);
        FieldTransformer<Object, Object> fieldTransformer = new FieldTransformer<>(fieldToTransform, val -> transformationResult);

        //WHEN
        underTest.withFieldTransformer(fieldTransformer);
        MutableToFooSimple actual = underTest.transform(fromFooSimple, MutableToFooSimple.class);

        //THEN
        assertThat(actual, hasProperty(fieldToTransform, equalTo(transformationResult)));
        underTest.removeFieldTransformer(fieldToTransform);
    }

    /**
     * Creates the parameters to be used for testing the transformation with field transformer.
     * @return parameters to be used for testing the transformation with field transformer.
     */
    @DataProvider
    private Object[][] dataTransformationTesting() {
        return new Object[][] {
                {"Test that the field transformation returns the expected values.",
                        NAME_FIELD_NAME, NAME.toLowerCase()},
                {"Test that the field transformation returns the expected values even if null.",
                        NAME_FIELD_NAME, null}
        };
    }

    /**
     * Test that, given a set of fields for which the transformation has to be skipped, they are actually skipped.
     */
    @Test
    public void testFieldTransformationSkipWorksProperly() {
        //GIVEN
        underTest.skipTransformationForField(NAME_FIELD_NAME, PHONE_NUMBER_NESTED_OBJECT_FIELD_NAME);

        //WHEN
        MutableToFoo actual = underTest.transform(fromFoo, MutableToFoo.class);

        //THEN
        assertEquals(fromFoo.getId(), actual.getId());
        assertNull(actual.getName());
        assertNull(actual.getNestedObject().getPhoneNumbers());
        underTest.resetFieldsTransformationSkip();
    }

    /**
     * Test that the automatic primitive type conversion works properly.
     */
    @Test
    public void testAutomaticPrimitiveTypeTransformationWorksProperly() {
        //GIVEN
        double delta = 0d;
        underTest.setPrimitiveTypeConversionEnabled(true);

        //WHEN
        MutableToFooOnlyPrimitiveTypes actual = underTest.transform(fromFooPrimitiveTypes, MutableToFooOnlyPrimitiveTypes.class);

        //THEN
        assertEquals(parseInt(fromFooPrimitiveTypes.getCode()), actual.getCode());
        assertEquals(String.valueOf(fromFooPrimitiveTypes.getId()), actual.getId());
        assertEquals(Float.valueOf(fromFooPrimitiveTypes.getPrice()).doubleValue(), actual.getPrice(), delta);
        assertEquals(ACTIVE, actual.isActive());
        underTest.setPrimitiveTypeConversionEnabled(false);
    }

    /**
     * Test that both primitive type transformation and custom transformation are executed.
     */
    @Test
    public void testThatBothPrimitiveTypeTransformationAndCustomTransformationAreExecuted() {
        //GIVEN
        double delta = 0d;
        double newPrice = PRICE * PRICE;
        FieldTransformer<Void, Double> priceTransformer = new FieldTransformer<>(PRICE_FIELD_NAME, () -> newPrice);
        underTest.setPrimitiveTypeConversionEnabled(true).withFieldTransformer(priceTransformer);

        //WHEN
        MutableToFooOnlyPrimitiveTypes actual = underTest.transform(fromFooPrimitiveTypes, MutableToFooOnlyPrimitiveTypes.class);

        //THEN
        assertEquals(parseInt(fromFooPrimitiveTypes.getCode()), actual.getCode());
        assertEquals(String.valueOf(fromFooPrimitiveTypes.getId()), actual.getId());
        assertEquals(newPrice, actual.getPrice(), delta);
        assertEquals(ACTIVE, actual.isActive());
        underTest.setPrimitiveTypeConversionEnabled(false);
        underTest.removeFieldTransformer(PRICE_FIELD_NAME);
    }
}
