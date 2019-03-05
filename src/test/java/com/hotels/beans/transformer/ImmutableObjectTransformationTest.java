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

import static java.lang.String.format;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Locale;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import com.hotels.beans.annotation.ConstructorArg;
import com.hotels.beans.cache.CacheManager;
import com.hotels.beans.error.InvalidBeanException;
import com.hotels.beans.model.FieldMapping;
import com.hotels.beans.model.FieldTransformer;
import com.hotels.beans.sample.FromFoo;
import com.hotels.beans.sample.FromFooSimple;
import com.hotels.beans.sample.immutable.ImmutableFlatToFoo;
import com.hotels.beans.sample.immutable.ImmutableToFoo;
import com.hotels.beans.sample.immutable.ImmutableToFooAdvFields;
import com.hotels.beans.sample.immutable.ImmutableToFooCustomAnnotation;
import com.hotels.beans.sample.immutable.ImmutableToFooDiffFields;
import com.hotels.beans.sample.immutable.ImmutableToFooInvalid;
import com.hotels.beans.sample.immutable.ImmutableToFooMissingCustomAnnotation;
import com.hotels.beans.sample.immutable.ImmutableToFooNoConstructors;
import com.hotels.beans.sample.immutable.ImmutableToFooNotExistingFields;
import com.hotels.beans.sample.immutable.ImmutableToFooSimple;
import com.hotels.beans.sample.immutable.ImmutableToFooSimpleWrongTypes;
import com.hotels.beans.sample.immutable.ImmutableToFooSubClass;
import com.hotels.beans.utils.ReflectionUtils;

/**
 * Unit test for all {@link Transformer} functions related to Immutable Java Beans.
 */
public class ImmutableObjectTransformationTest extends AbstractTransformerTest {
    private static final int TOTAL_ADV_CLASS_FIELDS = 5;
    private static final String GET_DEST_FIELD_NAME_METHOD_NAME = "getDestFieldName";
    private static final String GET_CONSTRUCTOR_VALUES_FROM_FIELDS_METHOD_NAME = "getConstructorValuesFromFields";

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
     * Test that immutable beans without constructor arguments parameter annotated with: @ConstructorArg {@link com.hotels.beans.annotation.ConstructorArg} are correctly copied.
     */
    @Test
    public void testBeanWithoutCustomAnnotationIsCorrectlyCopied() {
        //GIVEN

        //WHEN
        ImmutableToFoo actual = underTest.transform(fromFoo, ImmutableToFoo.class);

        //THEN
        assertThat(actual, sameBeanAs(fromFoo));
    }

    /**
     * Test that, in case a destination object field is contained into a nested object of the source field, defining a composite {@link FieldMapping} the field is correctly
     * valorized.
     */
    @Test
    public void testTransformationWithCompositeFieldNameMappingIsWorkingAsExpected() {
        //GIVEN
        FieldMapping phoneNumbersMapping = new FieldMapping("nestedObject.phoneNumbers", "phoneNumbers");

        //WHEN
        ImmutableFlatToFoo actual = underTest.withFieldMapping(phoneNumbersMapping).transform(fromFoo, ImmutableFlatToFoo.class);

        //THEN
        assertEquals(fromFoo.getName(), actual.getName());
        assertEquals(fromFoo.getId(), actual.getId());
        assertEquals(fromFoo.getNestedObject().getPhoneNumbers(), actual.getPhoneNumbers());
        underTest.resetFieldsMapping();
    }

    /**
     * Test that, in case a destination object field is contained into a nested object of the source field, defining a composite {@link FieldMapping} the field is correctly
     * valorized even if some of them are null.
     */
    @Test
    public void testTransformationWithCompositeFieldNameWorksEvenWithNullObjects() {
        //GIVEN
        FieldMapping phoneNumbersMapping = new FieldMapping("nestedObject.phoneNumbers", "phoneNumbers");

        //WHEN
        ImmutableFlatToFoo actual = underTest.withFieldMapping(phoneNumbersMapping).transform(fromFooWithNullProperties, ImmutableFlatToFoo.class);

        //THEN
        assertEquals(fromFooWithNullProperties.getName(), actual.getName());
        assertEquals(fromFooWithNullProperties.getId(), actual.getId());
        assertNull(actual.getPhoneNumbers());
        underTest.resetFieldsMapping();
    }

    /**
     * Test that immutable beans without custom field mapping.
     */
    @Test
    public void testBeanWithEmptyFieldMappingIsCorrectlyCopied() {
        //GIVEN

        //WHEN
        ImmutableToFoo actual = underTest.withFieldMapping().transform(fromFoo, ImmutableToFoo.class);

        //THEN
        assertThat(actual, sameBeanAs(fromFoo));
    }

    /**
     * Test that immutable beans without custom field mapping.
     */
    @Test
    public void testBeanWithEmptyFieldTransformerIsCorrectlyCopied() {
        //GIVEN

        //WHEN
        ImmutableToFoo actual = underTest.withFieldMapping().transform(fromFoo, ImmutableToFoo.class);

        //THEN
        assertThat(actual, sameBeanAs(fromFoo));
    }

    /**
     * Test that immutable beans with constructor arguments parameter annotated with: @ConstructorArg {@link com.hotels.beans.annotation.ConstructorArg} are correctly copied.
     */
    @Test
    public void testBeanWithCustomAnnotationIsCorrectlyCopied() {
        //GIVEN

        //WHEN
        ImmutableToFooCustomAnnotation actual = underTest.transform(fromFoo, ImmutableToFooCustomAnnotation.class);

        //THEN
        assertThat(actual, sameBeanAs(fromFoo));
    }

    /**
     * Test that an exception is thrown if the constructor invocation throws exception.
     */
    @Test(expected = InvalidBeanException.class)
    public void testTransformThrowsExceptionIfTheConstructorInvocationThrowsException() {
        //GIVEN
        FromFoo actual = new FromFoo(NAME, ID, null, null, null);

        //WHEN
        underTest.transform(actual, ImmutableToFooCustomAnnotation.class);
    }

    /**
     * Test that an exception is thrown if no constructors are defined.
     */
    @Test(expected = InvalidBeanException.class)
    public void testTransformThrowsExceptionWhenParameterAreNull() {
        //GIVEN
        FromFoo actual = new FromFoo(NAME, ID, null, null, null);

        //WHEN
        underTest.transform(actual, ImmutableToFooNoConstructors.class);
    }


    /**
     * Test that an exception is thrown if there the constructor args parameters have a different order for the mutable bean object.
     */
    @Test(expected = InvalidBeanException.class)
    public void testTransformThrowsExceptionWhenImmutableBeanHasAWrongConstructor() {
        //GIVEN

        //WHEN
        underTest.transform(fromFoo, ImmutableToFooInvalid.class);
    }

    /**
     * Test that an exception is thrown if the destination object don't met the constraints.
     */
    @Test(expected = InvalidBeanException.class)
    public void testTransformThrowsExceptionIfTheDestinationObjectValuesAreNotValid() {
        //GIVEN
        fromFoo.setId(null);

        //WHEN
        underTest.transform(fromFoo, ImmutableToFoo.class);

        // THEN
        fromFoo.setId(ID);
    }

    /**
     * Test that no exception is thrown if the destination object don't met the constraints and the validation is disabled.
     */
    @Test
    public void testTransformThrowsNoExceptionIfTheDestinationObjectValuesAreNotValidAndTheValidationIsDisabled() {
        //GIVEN
        fromFoo.setId(null);
        underTest.setValidationDisabled(true);

        //WHEN
        ImmutableToFoo actual = underTest.transform(fromFoo, ImmutableToFoo.class);

        // THEN
        assertThat(actual, sameBeanAs(fromFoo));
        fromFoo.setId(ID);
        underTest.setValidationDisabled(false);
    }

    /**
     * Test that bean that extends another class are correctly copied.
     */
    @Test
    public void testBeanWithoutCustomAnnotationAndWithSuperclassIsCorrectlyCopied() {
        //GIVEN

        //WHEN
        ImmutableToFooSubClass actual = underTest.transform(fromFooSubClass, ImmutableToFooSubClass.class);

        //THEN
        assertThat(actual, sameBeanAs(fromFooSubClass));
    }

    /**
     * Test that bean containing final fields (with different field names) are correctly copied.
     */
    @Test
    public void testImmutableBeanWithDifferentFieldNamesIsCorrectlyCopied() {
        //GIVEN

        //WHEN
        final Transformer beanTransformer = underTest.withFieldMapping(new FieldMapping("id", "identifier"));
        ImmutableToFooDiffFields actual = beanTransformer.transform(fromFoo, ImmutableToFooDiffFields.class);

        //THEN
        assertThat(actual, hasProperty("name", equalTo(actual.getName())));
        assertThat(actual, hasProperty("identifier", equalTo(fromFoo.getId())));
        assertEquals(actual.getList(), fromFoo.getList());
        IntStream.range(0, actual.getNestedObjectList().size())
                .forEach(i -> assertThat(actual.getNestedObjectList().get(i), sameBeanAs(fromFoo.getNestedObjectList().get(i))));
        assertThat(actual.getNestedObject(), sameBeanAs(fromFoo.getNestedObject()));
    }

    /**
     * Test that bean containing advanced final fields are correctly copied.
     */
    @Test
    public void testImmutableBeanWithAdvancedFieldsIsCorrectlyCopied() {
        //GIVEN

        //WHEN
        final Transformer beanTransformer = underTest
                .withFieldMapping(new FieldMapping("id", "identifier"))
                .withFieldTransformer(new FieldTransformer<>("locale", Locale::forLanguageTag));
        ImmutableToFooAdvFields actual = beanTransformer.transform(fromFooAdvFields, ImmutableToFooAdvFields.class);

        //THEN
        assertNotNull(actual.getName());
        assertTrue(actual.getName().isPresent());
        assertEquals(fromFooAdvFields.getName().get(), actual.getName().get());
        assertEquals(fromFooAdvFields.getAge().get(), actual.getAge());
        assertEquals(fromFooAdvFields.getClassType(), actual.getClassType());
        assertEquals(fromFooAdvFields.getLocale(), actual.getLocale().getLanguage());
    }

    /**
     * Test that immutable bean containing a constructor with some field not annotated with
     * {@link com.hotels.beans.annotation.ConstructorArg} is correctly copied.
     */
    @Test
    public void testImmutableBeanWithMissingConstructorArgIsCorrectlyCopied() {
        //GIVEN

        //WHEN
        ImmutableToFooMissingCustomAnnotation actual = underTest.withFieldTransformer().transform(fromFooWithPrimitiveFields, ImmutableToFooMissingCustomAnnotation.class);

        //THEN
        assertNotNull(actual);
        assertEquals(fromFooWithPrimitiveFields.getName(), actual.getName());
    }

    /**
     * Test that method: {@code getDestFieldName} retrieves the param name from the {@link ConstructorArg} if it is not provided from jvm directly.
     */
    @Test
    public void testGetDestFieldNameIsRetrievedFromConstructorArgIfTheParamNameIsNotProvidedFromJVM() throws Exception {
        //GIVEN
        String declaringClassName = ImmutableToFoo.class.getName();
        // Parameter mock setup
        Parameter constructorParameter = mock(Parameter.class);
        when(constructorParameter.getName()).thenReturn(CONSTRUCTOR_PARAMETER_NAME);

        // CacheManager mock setup
        initGetDestFieldNameTestMock(declaringClassName, constructorParameter);
        Method getDestFieldNameMethod = underTest.getClass().getDeclaredMethod(GET_DEST_FIELD_NAME_METHOD_NAME, Parameter.class, String.class);
        getDestFieldNameMethod.setAccessible(true);

        //WHEN
        String actual = (String) getDestFieldNameMethod.invoke(underTest, constructorParameter, declaringClassName);

        //THEN
        assertEquals(DEST_FIELD_NAME, actual);

        // restore modified objects
        restoreObjects(getDestFieldNameMethod);
    }

    /**
     * Test that the method: {@code getConstructorValuesFromFields} works properly.
     */
    @Test
    public void testGetConstructorValuesFromFieldsWorksProperly() throws Exception {
        //GIVEN
        underTest.withFieldTransformer(new FieldTransformer<>("locale", Locale::forLanguageTag));

        //WHEN
        final Method getConstructorValuesFromFieldsMethod =
                underTest.getClass().getDeclaredMethod(GET_CONSTRUCTOR_VALUES_FROM_FIELDS_METHOD_NAME, Object.class, Class.class, String.class);
        getConstructorValuesFromFieldsMethod.setAccessible(true);
        Object[] actual = (Object[]) getConstructorValuesFromFieldsMethod.invoke(underTest, fromFooAdvFields, ImmutableToFooAdvFields.class, "");

        //THEN
        assertNotNull(actual);
        assertEquals(TOTAL_ADV_CLASS_FIELDS, actual.length);

        // restore modified objects
        restoreObjects(getConstructorValuesFromFieldsMethod);
    }

    /**
     * Test that method: {@code getDestFieldName} returns null if the constructor's parameter name is not provided from jvm directly and the {@link ConstructorArg} is not defined.
     */
    @Test
    public void testGetDestFieldNameReturnsNullIfConstructorParamHasNoNameProvidedFromJVMAndNoConstructorArgIsDefined() throws Exception {
        //GIVEN
        String declaringClassName = ImmutableToFoo.class.getName();
        // Parameter mock setup
        Parameter constructorParameter = mock(Parameter.class);
        when(constructorParameter.getName()).thenReturn(null);

        // CacheManager mock setup
        initGetDestFieldNameTestMock(declaringClassName, constructorParameter);
        Method getDestFieldNameMethod = underTest.getClass().getDeclaredMethod(GET_DEST_FIELD_NAME_METHOD_NAME, Parameter.class, String.class);
        getDestFieldNameMethod.setAccessible(true);

        //WHEN
        String actual = (String) getDestFieldNameMethod.invoke(underTest, constructorParameter, declaringClassName);

        //THEN
        assertEquals(DEST_FIELD_NAME, actual);

        // restore modified objects
        restoreObjects(getDestFieldNameMethod);
    }

    /**
     * Test that a meaningful exception is returned when a constructor is invoked with wrong arguments.
     */
    @Test
    public void testTransformationReturnsAMeaningfulException() {
        //GIVEN
        Class<ImmutableToFooSimpleWrongTypes> targetClass = ImmutableToFooSimpleWrongTypes.class;
        final String expectedExceptionMessageFormat =
                "Constructor invoked with arguments. Expected: public %s(java.lang.Integer,java.lang.String); Found: %s(java.math.BigInteger,java.lang.String). "
                        +  "Double check that each %s's field have the same type and name than the source object: %s otherwise specify a transformer configuration.";
        String targetClassName = targetClass.getCanonicalName();
        String expectedExceptionMessage =
                format(expectedExceptionMessageFormat, targetClassName, targetClassName, targetClass.getSimpleName(), fromFooSimple.getClass().getCanonicalName());

        //WHEN
        Exception raisedException = null;
        try {
            underTest.transform(fromFooSimple, targetClass);
        } catch (final Exception e) {
            raisedException = e;
        }

        //THEN
        assertNotNull(raisedException);
        assertEquals(InvalidBeanException.class, raisedException.getClass());
        assertEquals(expectedExceptionMessage, raisedException.getMessage());
    }

    /**
     * Test transformation on an existing bean is correctly copied.
     */
    @Test
    public void testTransformationOnAnExistingDestinationWorksProperly() {
        //GIVEN
        ImmutableToFooSimple immutableToFoo = new ImmutableToFooSimple(null, null);

        //WHEN
        underTest.transform(fromFooSimple, immutableToFoo);

        //THEN
        assertThat(immutableToFoo, sameBeanAs(fromFooSimple));
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
        ImmutableToFooNotExistingFields immutableObjectBean = underTest.transform(fromFooSimple, ImmutableToFooNotExistingFields.class);

        //THEN
        assertThat(immutableObjectBean, hasProperty(AGE_FIELD_NAME, equalTo(AGE)));
    }

    /**
     * Initializes the mocks required for testing method: {@code getDestFieldName}.
     * @param declaringClassName the declaring class name
     * @param constructorParameter the constructor parameter
     */
    private void initGetDestFieldNameTestMock(final String declaringClassName, final Parameter constructorParameter) {
        CacheManager cacheManager = mock(CacheManager.class);
        String cacheKey = "DestFieldName-" + declaringClassName + "-" + CONSTRUCTOR_PARAMETER_NAME;
        when(cacheManager.getFromCache(cacheKey, String.class)).thenReturn(null);
        // ConstructorArg mock setup
        ConstructorArg constructorArg = mock(ConstructorArg.class);
        when(constructorArg.value()).thenReturn(DEST_FIELD_NAME);
        // ReflectionUtils mock setup
        ReflectionUtils reflectionUtils = mock(ReflectionUtils.class);
        when(reflectionUtils.getParameterAnnotation(constructorParameter, ConstructorArg.class, declaringClassName)).thenReturn(constructorArg);
        setField(underTest, REFLECTION_UTILS_FIELD_NAME, reflectionUtils);
    }

    /**
     * Restored the initial object status before testing method: {@code getDestFieldName}.
     */
    private void restoreObjects(final Method getDestFieldNameMethod) {
        getDestFieldNameMethod.setAccessible(false);
        setField(underTest, REFLECTION_UTILS_FIELD_NAME, new ReflectionUtils());
    }

}
