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
package com.expediagroup.beans.transformer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.math.BigInteger;
import java.util.Locale;
import java.util.Optional;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.expediagroup.beans.BeanUtils;
import com.expediagroup.beans.sample.FromFoo;
import com.expediagroup.beans.sample.FromFooAdvFields;
import com.expediagroup.beans.sample.FromFooSimple;
import com.expediagroup.beans.sample.FromFooSimpleBooleanField;
import com.expediagroup.beans.sample.immutable.ImmutableFlatToFoo;
import com.expediagroup.beans.sample.immutable.ImmutableToFoo;
import com.expediagroup.beans.sample.immutable.ImmutableToFooAdvFields;
import com.expediagroup.beans.sample.immutable.ImmutableToFooCustomAnnotation;
import com.expediagroup.beans.sample.immutable.ImmutableToFooDiffFields;
import com.expediagroup.beans.sample.immutable.ImmutableToFooDiffTypesFields;
import com.expediagroup.beans.sample.immutable.ImmutableToFooMap;
import com.expediagroup.beans.sample.immutable.ImmutableToFooMissingCustomAnnotation;
import com.expediagroup.beans.sample.immutable.ImmutableToFooNotExistingFields;
import com.expediagroup.beans.sample.immutable.ImmutableToFooSimple;
import com.expediagroup.beans.sample.immutable.ImmutableToFooSimpleBoolean;
import com.expediagroup.beans.sample.immutable.ImmutableToFooSimpleWrongTypes;
import com.expediagroup.beans.sample.immutable.ImmutableToFooSubClass;
import com.expediagroup.transformer.AbstractTransformerTest;
import com.expediagroup.transformer.annotation.ConstructorArg;
import com.expediagroup.transformer.cache.CacheManager;
import com.expediagroup.transformer.error.InvalidBeanException;
import com.expediagroup.transformer.error.InvalidFunctionException;
import com.expediagroup.transformer.model.FieldMapping;
import com.expediagroup.transformer.model.FieldTransformer;
import com.expediagroup.transformer.utils.ClassUtils;
import com.expediagroup.transformer.utils.ReflectionUtils;

/**
 * Unit test for all {@link BeanTransformer} functions related to Immutable Java Beans.
 */
public class ImmutableObjectTransformationTest extends AbstractBeanTransformerTest {
    private static final int TOTAL_ADV_CLASS_FIELDS = 11;
    private static final String GET_DEST_FIELD_NAME_METHOD_NAME = "getDestFieldName";
    private static final String GET_CONSTRUCTOR_VALUES_FROM_FIELDS_METHOD_NAME = "getConstructorValuesFromFields";
    private static final String PRICE_FIELD_NAME = "price";
    private static final String NET_PRICE_FIELD_NAME = "price.netPrice";
    private static final String GROSS_PRICE_FIELD_NAME = "price.grossPrice";
    private static final String WORK_FIELD_NAME = "work";
    private static final boolean ACTIVE = true;
    private static final String LOCALE_LANGUAGE_FIELD_NAME = "locale.language";

    /**
     * After method actions.
     */
    @AfterMethod
    public void afterMethod() {
        underTest.setValidationEnabled(false);
        underTest.resetFieldsTransformer();
    }

    /**
     * Test that default transformation of immutable beans works properly.
     * @param testCaseDescription the test case description
     * @param transformer the transform to use
     * @param sourceObject the object to transform
     * @param targetObjectClass the target object class
     */
    @Test(dataProvider = "dataDefaultTransformationTesting")
    public void testImmutableBeanIsCorrectlyCopied(final String testCaseDescription, final BeanTransformer transformer, final Object sourceObject,
        final Class<?> targetObjectClass) {
        // GIVEN

        // WHEN
        Object actual = transformer.transform(sourceObject, targetObjectClass);

        // THEN
        assertThat(actual).usingRecursiveComparison()
                .ignoringAllOverriddenEquals()
                .isEqualTo(sourceObject);
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
        // GIVEN
        ImmutableToFooSimple immutableToFoo = new ImmutableToFooSimple(null, null, false);

        // WHEN
        underTest.setValidationEnabled(true).transform(fromFooSimple, immutableToFoo);

        // THEN
        assertThat(immutableToFoo)
                .usingRecursiveComparison()
                .isEqualTo(fromFooSimple);
        underTest.setValidationEnabled(false);
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
        // GIVEN
        TransformerImpl underTestMock = spy(new TransformerImpl());
        Constructor<ImmutableFlatToFoo> beanAllArgsConstructor = new ClassUtils().getAllArgsConstructor(ImmutableFlatToFoo.class);
        when(underTestMock.canBeInjectedByConstructorParams(beanAllArgsConstructor)).thenReturn(false);
        FieldMapping<String, String> phoneNumbersMapping = new FieldMapping<>(PHONE_NUMBER_NESTED_OBJECT_FIELD_NAME, PHONE_NUMBER_DEST_FIELD_NAME);

        // WHEN
        ImmutableFlatToFoo actual = underTestMock.withFieldMapping(phoneNumbersMapping).transform(sourceObject, ImmutableFlatToFoo.class);

        // THEN
        assertThat(actual).extracting(NAME_FIELD_NAME, ID_FIELD_NAME, PHONE_NUMBER_DEST_FIELD_NAME)
                .containsExactly(expectedName, expectedId, expectedPhoneNumbers);
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
        // GIVEN

        // WHEN
        underTest.setValidationEnabled(false).transform(sourceObject, targetObjectClass);
    }

    /**
     * Creates the parameters to be used for testing the exception raised in case of error during the constructor invocation.
     * @return parameters to be used for testing the transformation with composite field name mapping.
     */
    @DataProvider(parallel = true)
    private Object[][] dataConstructorErrorTesting() {
        FromFoo actual = new FromFoo(NAME, ID, null, null, null);
        return new Object[][] {
                {"Test that an exception is thrown if the constructor is invoked with wrong type arguments", actual, ImmutableToFooDiffTypesFields.class}
        };
    }

    /**
     * Test that an {@link InvalidBeanException} is thrown if the bean is not valid.
     * @throws CloneNotSupportedException if the clone of an object fails
     */
    @Test(expectedExceptions = InvalidBeanException.class)
    public void testTransformThrowsExceptionWhenImmutableIsInvalid() throws CloneNotSupportedException {
        // GIVEN
        FromFoo fromFooNullId = AbstractTransformerTest.fromFoo.clone();
        fromFooNullId.setId(null);

        // WHEN
        underTest.setValidationEnabled(true).transform(fromFooNullId, ImmutableToFoo.class);
    }

    /**
     * Test that no exception is thrown if the destination object don't met the constraints and the validation is disabled.
     */
    @Test
    public void testTransformThrowsNoExceptionIfTheDestinationObjectValuesAreNotValidAndTheValidationIsDisabled() {
        // GIVEN
        fromFoo.setId(null);

        // WHEN
        ImmutableToFoo actual = underTest.transform(fromFoo, ImmutableToFoo.class);

        // THEN
        assertThat(actual).usingRecursiveComparison().isEqualTo(fromFoo);
        fromFoo.setId(ID);
    }

    /**
     * Test that bean containing final fields (with different field names) are correctly copied.
     */
    @Test
    public void testImmutableBeanWithDifferentFieldNamesIsCorrectlyCopied() {
        // GIVEN

        // WHEN
        final BeanTransformer beanTransformer = underTest.withFieldMapping(new FieldMapping<>(ID_FIELD_NAME, IDENTIFIER_FIELD_NAME));
        ImmutableToFooDiffFields actual = beanTransformer.transform(fromFoo, ImmutableToFooDiffFields.class);

        // THEN
        assertThat(actual).hasFieldOrPropertyWithValue(IDENTIFIER_FIELD_NAME, fromFoo.getId())
                .usingRecursiveComparison()
                .ignoringFields(IDENTIFIER_FIELD_NAME)
                .isEqualTo(fromFoo);
    }

    /**
     * Test that bean containing advanced final fields are correctly copied.
     * @param testCaseDescription the test case description
     * @param sourceObject the object to transform
     * @param targetObjectClass the target object class
     * @param isNameFieldEmpty true if the name field should contain a value, false otherwise
     */
    @Test(dataProvider = "dataAdvancedFieldsCopyTesting")
    public void testImmutableBeanWithAdvancedFieldsIsCorrectlyCopied(final String testCaseDescription, final FromFooAdvFields sourceObject,
        final Class<?> targetObjectClass, final boolean isNameFieldEmpty) {
        // GIVEN
        final BeanTransformer beanTransformer = underTest
                .withFieldMapping(new FieldMapping<>(ID_FIELD_NAME, IDENTIFIER_FIELD_NAME))
                .withFieldMapping(new FieldMapping<>(PRICE_FIELD_NAME, NET_PRICE_FIELD_NAME))
                .withFieldMapping(new FieldMapping<>(PRICE_FIELD_NAME, GROSS_PRICE_FIELD_NAME))
                .withFieldTransformer(new FieldTransformer<>(LOCALE_FIELD_NAME, Locale::forLanguageTag));

        // WHEN
        ImmutableToFooAdvFields actual = (ImmutableToFooAdvFields) beanTransformer.transform(sourceObject, targetObjectClass);

        // THEN
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields(AGE_FIELD_NAME, PRICE_FIELD_NAME, LOCALE_FIELD_NAME)
                .isEqualTo(sourceObject);
        assertThat(actual).extracting(AGE_FIELD_NAME, NET_PRICE_FIELD_NAME, GROSS_PRICE_FIELD_NAME, LOCALE_LANGUAGE_FIELD_NAME)
                .containsExactly(sourceObject.getAge().orElse(null), sourceObject.getPrice(), sourceObject.getPrice(), sourceObject.getLocale());
    }

    /**
     * Creates the parameters to be used for testing the transformation of advanced type fields.
     * @return parameters to be used for testing the transformation of advanced type fields.
     * @throws CloneNotSupportedException if the clone fails
     */
    @DataProvider(parallel = true)
    private Object[][] dataAdvancedFieldsCopyTesting() throws CloneNotSupportedException {
        FromFooAdvFields emptyOptionalSourceObject = fromFooAdvFields.clone();
        emptyOptionalSourceObject.setName(Optional.empty());
        return new Object[][] {
                {"Test that bean containing advanced final fields are correctly copied", fromFooAdvFields, ImmutableToFooAdvFields.class, true},
                {"Test that bean containing advanced final fields (with empty optional) are correctly copied", emptyOptionalSourceObject, ImmutableToFooAdvFields.class, false}
        };
    }

    /**
     * Test that immutable bean containing a constructor with some field not annotated with
     * {@link ConstructorArg} is correctly copied.
     */
    @Test
    public void testImmutableBeanWithMissingConstructorArgIsCorrectlyCopied() {
        // GIVEN

        // WHEN
        ImmutableToFooMissingCustomAnnotation actual = underTest.withFieldTransformer().transform(fromFooWithPrimitiveFields, ImmutableToFooMissingCustomAnnotation.class);

        // THEN
        assertThat(actual).isNotNull();
        assertThat(actual.getName()).isEqualTo(fromFooWithPrimitiveFields.getName());
    }

    /**
     * Test that method: {@code getDestFieldName} retrieves the param name from the {@link ConstructorArg} if it is not provided from jvm directly.
     * @throws Exception the thrown exception
     */
    @Test
    public void testGetDestFieldNameIsRetrievedFromConstructorArgIfTheParamNameIsNotProvidedFromJVM() throws Exception {
        // GIVEN
        String declaringClassName = ImmutableToFoo.class.getName();
        // Parameter mock setup
        Parameter constructorParameter = mock(Parameter.class);
        when(constructorParameter.getName()).thenReturn(CONSTRUCTOR_PARAMETER_NAME);

        // CacheManager mock setup
        initGetDestFieldNameTestMock(declaringClassName, constructorParameter);
        Method getDestFieldNameMethod = underTest.getClass().getDeclaredMethod(GET_DEST_FIELD_NAME_METHOD_NAME, Parameter.class, String.class);
        getDestFieldNameMethod.setAccessible(true);

        // WHEN
        String actual = (String) getDestFieldNameMethod.invoke(underTest, constructorParameter, declaringClassName);

        // THEN
        assertThat(actual).isEqualTo(DEST_FIELD_NAME);

        // restore modified objects
        restoreObjects(getDestFieldNameMethod);
    }

    /**
     * Test that the method: {@code getConstructorValuesFromFields} works properly.
     * @throws Exception the thrown exception
     */
    @Test
    public void testGetConstructorValuesFromFieldsWorksProperly() throws Exception {
        // GIVEN
        underTest.withFieldTransformer(new FieldTransformer<>(LOCALE_FIELD_NAME, Locale::forLanguageTag));

        // WHEN
        final Method getConstructorValuesFromFieldsMethod =
                underTest.getClass().getDeclaredMethod(GET_CONSTRUCTOR_VALUES_FROM_FIELDS_METHOD_NAME, Object.class, Class.class, String.class);
        getConstructorValuesFromFieldsMethod.setAccessible(true);
        Object[] actual = (Object[]) getConstructorValuesFromFieldsMethod.invoke(underTest, fromFooAdvFields, ImmutableToFooAdvFields.class, "");

        // THEN
        assertThat(actual)
                .isNotNull()
                .hasSize(TOTAL_ADV_CLASS_FIELDS);

        // restore modified objects
        restoreObjects(getConstructorValuesFromFieldsMethod);
    }

    /**
     * Test that method: {@code getDestFieldName} returns null if the constructor's parameter name is not provided from jvm directly and the {@link ConstructorArg} is not defined.
     * @throws Exception the thrown exception
     */
    @Test
    public void testGetDestFieldNameReturnsNullIfConstructorParamHasNoNameProvidedFromJVMAndNoConstructorArgIsDefined() throws Exception {
        // GIVEN
        String declaringClassName = ImmutableToFoo.class.getName();
        // Parameter mock setup
        Parameter constructorParameter = mock(Parameter.class);
        when(constructorParameter.getName()).thenReturn(null);

        // CacheManager mock setup
        initGetDestFieldNameTestMock(declaringClassName, constructorParameter);
        Method getDestFieldNameMethod = underTest.getClass().getDeclaredMethod(GET_DEST_FIELD_NAME_METHOD_NAME, Parameter.class, String.class);
        getDestFieldNameMethod.setAccessible(true);

        // WHEN
        String actual = (String) getDestFieldNameMethod.invoke(underTest, constructorParameter, declaringClassName);

        // THEN
        assertThat(actual).isEqualTo(DEST_FIELD_NAME);

        // restore modified objects
        restoreObjects(getDestFieldNameMethod);
    }

    /**
     * Test that a meaningful exception is returned when a constructor is invoked with wrong arguments.
     */
    @Test
    public void testTransformationReturnsAMeaningfulException() {
        // GIVEN
        Class<ImmutableToFooSimpleWrongTypes> targetClass = ImmutableToFooSimpleWrongTypes.class;
        final String expectedExceptionMessageFormat =
                "Constructor invoked with wrong arguments. Expected: public %s(java.lang.Integer,java.lang.String); Found: %s(java.math.BigInteger,java.lang.String). "
                        + "Double check that each %s's field have the same type and name than the source object: %s otherwise specify a transformer configuration. "
                        + "Error message: argument type mismatch";
        String targetClassName = targetClass.getName();
        String expectedExceptionMessage =
                String.format(expectedExceptionMessageFormat, targetClassName, targetClassName, targetClass.getSimpleName(), fromFooSimple.getClass().getName());

        // WHEN
        ThrowingCallable actual = () -> underTest.transform(fromFooSimple, targetClass);

        // THEN
        assertThatThrownBy(actual).isInstanceOf(InvalidBeanException.class)
                .hasMessage(expectedExceptionMessage);
    }

    /**
     * Test that a bean containing a field not existing in the source object, but with a transformer function defined for such object is correctly copied.
     */
    @Test
    public void testThatAnyTypeOfBeanContainsANotExistingFieldInTheSourceObjectIsCorrectlyCopiedThroughTransformerFunctions() {
        // GIVEN
        FromFooSimple fromFooSimple = new FromFooSimple(NAME, ID, ACTIVE);
        FieldTransformer<Object, Integer> ageFieldTransformer = new FieldTransformer<>(AGE_FIELD_NAME, () -> AGE);

        // WHEN
        underTest.withFieldTransformer(ageFieldTransformer);
        ImmutableToFooNotExistingFields immutableObjectBean = underTest.transform(fromFooSimple, ImmutableToFooNotExistingFields.class);

        // THEN
        assertThat(immutableObjectBean).hasFieldOrPropertyWithValue(AGE_FIELD_NAME, AGE);
    }

    /**
     * Test that, given a set of fields for which the transformation has to be skipped, they are actually skipped.
     */
    @Test
    public void testFieldTransformationSkipWorksProperly() {
        // GIVEN
        underTest.skipTransformationForField("name", "nestedObject.phoneNumbers");

        // WHEN
        ImmutableToFoo actual = underTest.transform(fromFoo, ImmutableToFoo.class);

        // THEN
        assertThat(actual).hasNoNullFieldsOrPropertiesExcept(NAME_FIELD_NAME, PHONE_NUMBER_NESTED_OBJECT_FIELD_NAME);
        underTest.resetFieldsTransformationSkip();
    }

    /**
     * Test that the transformer function is applied earlier than the default value.
     */
    @Test
    public void testTransformerFunctionHasHigherPriorityThanDefaultValue() {
        // GIVEN
        FromFooSimpleBooleanField fromFooSimpleNullFields = new FromFooSimpleBooleanField();
        FieldTransformer<Boolean, Boolean> nullToTrue =
            new FieldTransformer<>(WORK_FIELD_NAME, aBoolean -> aBoolean == null || aBoolean);

        // WHEN
        ImmutableToFooSimpleBoolean actual = underTest
            .withFieldTransformer(nullToTrue)
            .transform(fromFooSimpleNullFields, ImmutableToFooSimpleBoolean.class);

        // THEN
        assertThat(actual.getWork()).isTrue();
        underTest.resetFieldsTransformer();
    }

    /**
     * Test that an {@link InvalidFunctionException} is raised if the transformer function defined is not valid.
     */
    @Test(expectedExceptions = InvalidFunctionException.class)
    public void testTransformRaiseAnExceptionIfTheTransformerFunctionIsNotValid() {
        // GIVEN
        FromFooSimpleBooleanField fromFooSimpleNullFields = new FromFooSimpleBooleanField();
        FieldTransformer<String, String> upperCase =
                new FieldTransformer<>(WORK_FIELD_NAME, String::toUpperCase);

        // WHEN
        underTest.withFieldTransformer(upperCase)
                .transform(fromFooSimpleNullFields, ImmutableToFooSimpleBoolean.class);

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
        ReflectionUtils reflectionUtilsMock = mock(ReflectionUtils.class);
        when(reflectionUtilsMock.getParameterAnnotation(constructorParameter, ConstructorArg.class, declaringClassName)).thenReturn(constructorArg);
        reflectionUtils.setFieldValue(underTest, REFLECTION_UTILS_FIELD_NAME, reflectionUtilsMock);
    }

    /**
     * Restores the initial object status before testing method: {@code getDestFieldName}.
     * @param getDestFieldNameMethod the destination field name method
     */
    private void restoreObjects(final Method getDestFieldNameMethod) {
        getDestFieldNameMethod.setAccessible(false);
        reflectionUtils.setFieldValue(underTest, REFLECTION_UTILS_FIELD_NAME, new ReflectionUtils());
    }

}
