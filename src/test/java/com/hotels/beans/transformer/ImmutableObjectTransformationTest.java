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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.math.BigInteger;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.IntStream;

import org.mockito.InjectMocks;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.hotels.beans.BeanUtils;
import com.hotels.beans.annotation.ConstructorArg;
import com.hotels.beans.cache.CacheManager;
import com.hotels.beans.error.InvalidBeanException;
import com.hotels.beans.model.FieldMapping;
import com.hotels.beans.model.FieldTransformer;
import com.hotels.beans.sample.FromFoo;
import com.hotels.beans.sample.FromFooAdvFields;
import com.hotels.beans.sample.FromFooSimple;
import com.hotels.beans.sample.immutable.ImmutableFlatToFoo;
import com.hotels.beans.sample.immutable.ImmutableToFoo;
import com.hotels.beans.sample.immutable.ImmutableToFooAdvFields;
import com.hotels.beans.sample.immutable.ImmutableToFooCustomAnnotation;
import com.hotels.beans.sample.immutable.ImmutableToFooDiffFields;
import com.hotels.beans.sample.immutable.ImmutableToFooInvalid;
import com.hotels.beans.sample.immutable.ImmutableToFooMap;
import com.hotels.beans.sample.immutable.ImmutableToFooMissingCustomAnnotation;
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
    @BeforeMethod
    public void beforeMethod() {
        initMocks(this);
    }

    /**
     * Test that default transformation of immutable beans works properly.
     * @param testCaseDescription the test case description
     * @param transformer the transform to use
     * @param sourceObject the object to transform
     * @param targetObjectClass the target object class
     */
    @Test(dataProvider = "dataDefaultTransformationTesting")
    public void testImmutableBeanIsCorrectlyCopied(final String testCaseDescription, final Transformer transformer, final Object sourceObject,
        final Class<?> targetObjectClass) {
        //GIVEN

        //WHEN
        Object actual = transformer.transform(sourceObject, targetObjectClass);

        //THEN
        assertThat(actual, sameBeanAs(sourceObject));
    }

    /**
     * Creates the parameters to be used for testing the default transformation operations.
     * @return parameters to be used for testing the default transformation operations.
     */
    @DataProvider(parallel = true)
    private Object[][] dataDefaultTransformationTesting() {
        BeanUtils beanUtils = new BeanUtils();
        return new Object[][] {
                {"Test that immutable beans without constructor arguments parameter annotated with: @ConstructorArg are correctly copied.",
                        beanUtils.getTransformer(), fromFoo, ImmutableToFoo.class},
                {"Test that immutable beans without custom field mapping are correctly transformed.",
                        beanUtils.getTransformer().withFieldMapping(), fromFoo, ImmutableToFoo.class},
                {"Test that immutable beans with constructor arguments parameter annotated with: @ConstructorArg are correctly copied.",
                        beanUtils.getTransformer(), fromFoo, ImmutableToFooCustomAnnotation.class},
                {"Test that bean that extends another class are correctly copied", beanUtils.getTransformer(), fromFooSubClass, ImmutableToFooSubClass.class},
                {"Test that immutable beans with extremely complex map are correctly transformed.",
                        beanUtils.getTransformer(), fromFooMap, ImmutableToFooMap.class},
        };
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
     * Test transformation field with composite field name mapping.
     * @param testCaseDescription the test case description
     * @param sourceObject the object to transform
     * @param expectedName the expected name
     * @param expectedId the expected id
     * @param expectedPhoneNumbers the expected phone number
     */
    @Test(dataProvider = "dataCompositeFieldNameTesting")
    public void testTransformationWithCompositeFieldNameMappingIsWorkingAsExpected(final String testCaseDescription, final Object sourceObject, final String expectedName,
        final BigInteger expectedId, final int[] expectedPhoneNumbers) {
        //GIVEN
        FieldMapping phoneNumbersMapping = new FieldMapping(PHONE_NUMBER_NESTED_OBJECT_FIELD_NAME, PHONE_NUMBER_DEST_FIELD_NAME);

        //WHEN
        ImmutableFlatToFoo actual = underTest.withFieldMapping(phoneNumbersMapping).transform(sourceObject, ImmutableFlatToFoo.class);

        //THEN
        assertEquals(expectedName, actual.getName());
        assertEquals(expectedId, actual.getId());
        assertEquals(expectedPhoneNumbers, actual.getPhoneNumbers());
        underTest.resetFieldsMapping();
    }

    /**
     * Creates the parameters to be used for testing the transformation with composite field name mapping.
     * @return parameters to be used for testing the transformation with composite field name mapping.
     */
    @DataProvider(parallel = true)
    private Object[][] dataCompositeFieldNameTesting() {
        return new Object[][] {
                {"Test that, in case a destination object field is contained into a nested object of the source field, defining a composite FieldMapping"
                        + "the field is correctly set.", fromFoo, fromFoo.getName(), fromFoo.getId(), fromFoo.getNestedObject().getPhoneNumbers()},
                {"Test that, in case a destination object field is contained into a nested object of the source field, defining a composite {@link FieldMapping}"
                        + " the field is correctly set even if some of them are null.", fromFooWithNullProperties, fromFooWithNullProperties.getName(),
                        fromFooWithNullProperties.getId(), null}
        };
    }

    /**
     * Test that an exception is thrown if the constructor invocation throws exception.
     * @param testCaseDescription the test case description
     * @param sourceObject the object to transform
     * @param targetObjectClass the target object class
     */
    @Test(dataProvider = "dataConstructorErrorTesting", expectedExceptions = InvalidBeanException.class)
    public void testTransformThrowsExceptionIfTheConstructorInvocationThrowsException(final String testCaseDescription, final Object sourceObject,
        final Class<?> targetObjectClass) {
        //GIVEN

        //WHEN
        underTest.transform(sourceObject, targetObjectClass);
    }

    /**
     * Creates the parameters to be used for testing the exception raised in case of error during the constructor invocation.
     * @return parameters to be used for testing the transformation with composite field name mapping.
     */
    @DataProvider(parallel = true)
    private Object[][] dataConstructorErrorTesting() {
        FromFoo actual = new FromFoo(NAME, ID, null, null, null);
        return new Object[][] {
                {"Test that an exception is thrown if the constructor invocation throws exception", actual, ImmutableToFooCustomAnnotation.class}
        };
    }

    /**
     * Test that an {@link InvalidBeanException} is thrown if the bean is not valid.
     * @param testCaseDescription the test case description
     * @param sourceObject the object to transform
     * @param targetObjectClass the target object class
     */
    @Test(dataProvider = "dataInvalidBeanExceptionTesting", expectedExceptions = InvalidBeanException.class)
    public void testTransformThrowsExceptionWhenImmutableIsInvalid(final String testCaseDescription, final FromFoo sourceObject,
        final Class<?> targetObjectClass) {
        //GIVEN

        //WHEN
        underTest.transform(sourceObject, targetObjectClass);
    }

    /**
     * Creates the parameters to be used for testing the exception raised in case of error during the constructor invocation.
     * @return parameters to be used for testing the exception raised in case of error during the constructor invocation.
     * @throws CloneNotSupportedException if the clone fails
     */
    @DataProvider(parallel = true)
    private Object[][] dataInvalidBeanExceptionTesting() throws CloneNotSupportedException {
        FromFoo fromFooNullId = AbstractTransformerTest.fromFoo.clone();
        fromFooNullId.setId(null);
        return new Object[][] {
                {"Test that an exception is thrown if there the constructor args parameters have a different order for the mutable bean object.",
                        fromFoo, ImmutableToFooInvalid.class},
                {"Test that an exception is thrown if the destination object don't met the constraints.", fromFooNullId, ImmutableToFoo.class},
        };
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
     * Test that bean containing final fields (with different field names) are correctly copied.
     */
    @Test
    public void testImmutableBeanWithDifferentFieldNamesIsCorrectlyCopied() {
        //GIVEN

        //WHEN
        final Transformer beanTransformer = underTest.withFieldMapping(new FieldMapping(ID_FIELD_NAME, IDENTIFIER_FIELD_NAME));
        ImmutableToFooDiffFields actual = beanTransformer.transform(fromFoo, ImmutableToFooDiffFields.class);

        //THEN
        assertThat(actual, hasProperty(NAME_FIELD_NAME, equalTo(actual.getName())));
        assertThat(actual, hasProperty(IDENTIFIER_FIELD_NAME, equalTo(fromFoo.getId())));
        assertEquals(actual.getList(), fromFoo.getList());
        IntStream.range(0, actual.getNestedObjectList().size())
                .forEach(i -> assertThat(actual.getNestedObjectList().get(i), sameBeanAs(fromFoo.getNestedObjectList().get(i))));
        assertThat(actual.getNestedObject(), sameBeanAs(fromFoo.getNestedObject()));
    }

    /**
     * Test that bean containing advanced final fields are correctly copied.
     * @param testCaseDescription the test case description
     * @param sourceObject the object to transform
     * @param targetObjectClass the target object class
     * @param isNameFieldEmpty true if the name field should contain a value, false otherwise
     */
    @Test(dataProvider = "dataCAdvancedFieldsCopyTesting")
    public void testImmutableBeanWithAdvancedFieldsIsCorrectlyCopied(final String testCaseDescription, final FromFooAdvFields sourceObject,
        final Class<?> targetObjectClass, final boolean isNameFieldEmpty) {
        //GIVEN

        //WHEN
        final Transformer beanTransformer = underTest
                .withFieldMapping(new FieldMapping(ID_FIELD_NAME, IDENTIFIER_FIELD_NAME))
                .withFieldTransformer(new FieldTransformer<>(LOCALE_FIELD_NAME, Locale::forLanguageTag));
        ImmutableToFooAdvFields actual = (ImmutableToFooAdvFields) beanTransformer.transform(sourceObject, targetObjectClass);

        //THEN
        assertNotNull(actual.getName());
        assertEquals(isNameFieldEmpty, actual.getName().isPresent());
        sourceObject.getName().ifPresent(name -> assertEquals(name, actual.getName().get()));
        assertEquals(sourceObject.getAge().get(), actual.getAge());
        assertEquals(sourceObject.getClassType(), actual.getClassType());
        assertEquals(sourceObject.getLocale(), actual.getLocale().getLanguage());
    }

    /**
     * Creates the parameters to be used for testing the transformation of advanced type fields.
     * @return parameters to be used for testing the transformation of advanced type fields.
     * @throws CloneNotSupportedException if the clone fails
     */
    @DataProvider(parallel = true)
    private Object[][] dataCAdvancedFieldsCopyTesting() throws CloneNotSupportedException {
        FromFooAdvFields emptyOptionalSourceObject = fromFooAdvFields.clone();
        emptyOptionalSourceObject.setName(Optional.empty());
        return new Object[][] {
                {"Test that bean containing advanced final fields are correctly copied", fromFooAdvFields, ImmutableToFooAdvFields.class, true},
                {"Test that bean containing advanced final fields (with empty optional) are correctly copied", emptyOptionalSourceObject, ImmutableToFooAdvFields.class, false}
        };
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
     * @throws Exception the thrown exception
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
     * @throws Exception the thrown exception
     */
    @Test
    public void testGetConstructorValuesFromFieldsWorksProperly() throws Exception {
        //GIVEN
        underTest.withFieldTransformer(new FieldTransformer<>(LOCALE_FIELD_NAME, Locale::forLanguageTag));

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
     * @throws Exception the thrown exception
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
                        + "Double check that each %s's field have the same type and name than the source object: %s otherwise specify a transformer configuration. "
                        + "Error message: argument type mismatch";
        String targetClassName = targetClass.getName();
        String expectedExceptionMessage =
                format(expectedExceptionMessageFormat, targetClassName, targetClassName, targetClass.getSimpleName(), fromFooSimple.getClass().getName());

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
     * Test that, given a set of fields for which the transformation has to be skipped, they are actually skipped.
     */
    @Test
    public void testFieldTransformationSkipWorksProperly() {
        //GIVEN
        underTest.skipTransformationForField("name", "nestedObject.phoneNumbers");

        //WHEN
        ImmutableToFoo actual = underTest.transform(fromFoo, ImmutableToFoo.class);

        //THEN
        assertEquals(fromFoo.getId(), actual.getId());
        assertNull(actual.getName());
        assertNull(actual.getNestedObject().getPhoneNumbers());
        underTest.resetFieldsTransformationSkip();
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
     * Restores the initial object status before testing method: {@code getDestFieldName}.
     */
    private void restoreObjects(final Method getDestFieldNameMethod) {
        getDestFieldNameMethod.setAccessible(false);
        setField(underTest, REFLECTION_UTILS_FIELD_NAME, new ReflectionUtils());
    }

}
