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
package com.expediagroup.transformer.utils;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;
import static java.util.Objects.isNull;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.expediagroup.beans.sample.FromFoo;
import com.expediagroup.beans.sample.FromFooMap;
import com.expediagroup.beans.sample.FromFooSimple;
import com.expediagroup.beans.sample.FromFooSimpleNoGetters;
import com.expediagroup.beans.sample.FromFooSubClass;
import com.expediagroup.beans.sample.FromSubFoo;
import com.expediagroup.beans.sample.immutable.ImmutableToFoo;
import com.expediagroup.beans.sample.immutable.ImmutableToFooAdvFields;
import com.expediagroup.beans.sample.immutable.ImmutableToSubFoo;
import com.expediagroup.beans.sample.mutable.MutableToFoo;
import com.expediagroup.beans.sample.mutable.MutableToFooAdvFields;
import com.expediagroup.beans.sample.mutable.MutableToFooSimple;
import com.expediagroup.transformer.constant.MethodPrefix;
import com.expediagroup.transformer.error.MissingFieldException;
import com.expediagroup.transformer.error.MissingMethodException;
import com.expediagroup.transformer.model.ItemType;
import com.expediagroup.transformer.model.MapElemType;
import com.expediagroup.transformer.model.MapType;

/**
 * Unit tests fro class: {@link ReflectionUtils}.
 */
public class ReflectionUtilsTest {
    private static final String ID_FIELD_NAME = "id";
    private static final String NOT_EXISTING_FIELD_NAME = "notExistingField";
    private static final String NESTED_OBJECT_NAME_FIELD_NAME = "nestedObject.name";
    private static final String LIST_FIELD_NAME = "list";
    private static final String PHONE_NUMBERS_FIELD_NAME = "phoneNumbers";
    private static final String GETTER_METHOD_PREFIX_METHOD_NAME = "getGetterMethodPrefix";
    private static final String EXPECTED_SETTER_METHOD_NAME = "setId";
    private static final String GET_FIELD_VALUE_DIRECT_ACCESS_METHOD_NAME = "getFieldValueDirectAccess";
    private static final String CHECK_FIELD_NAME = "check";
    private static final String DECLARING_CLASS_NAME = "declaringClassName";
    private static final String INVOKE_METHOD_NAME = "invokeMethod";
    private static final String VERY_COMPLEX_MAP_FIELD_NAME = "veryComplexMap";
    private static final String UNPARAMETRIZED_MAP_FIELD_NAME = "unparametrizedMap";
    private static final String GET_GETTER_METHOD_NAME = "getGetterMethod";
    private static final String SET_NAME_METHOD_NAME = "setName";
    private static final String SET_INDEX_METHOD_NAME = "setIndex";
    private static final String INDEX_NUMBER = "123";
    private static final String GET_REAL_TARGET_METHOD_NAME = "getRealTarget";
    private static final String GET_CLASS_DECLARED_FIELD_METHOD_NAME = "getClassDeclaredField";
    private static final String NAME_FIELD_NAME = "name";

    /**
     * The class to be tested.
     */
    @InjectMocks
    private ReflectionUtils underTest;

    /**
     * Initializes mock.
     */
    @BeforeClass
    public void beforeClass() {
        openMocks(this);
    }

    /**
     * Tests that the method {@code getFieldValue} returns the expected value.
     * @param testCaseDescription the test case description
     * @param beanObject the java bean from which the value has to be retrieved
     * @param fieldName the name of the field to retrieve
     * @param fieldType the type of the field to retrieve
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "dataGetFieldValueTesting")
    public void testGetFieldValueWorksAsExpected(final String testCaseDescription, final Object beanObject,
        final String fieldName, final Class<?> fieldType, final Object expectedResult) {
        // GIVEN

        // WHEN
        Object actual = underTest.getFieldValue(beanObject, fieldName, fieldType);

        // THEN
        assertThat(actual).isEqualTo(expectedResult);
    }

    /**
     * Tests that the method {@code getFieldValue} with a a given field returns the expected value.
     * @throws Exception in case of issues
     */
    @Test
    public void testGetFieldValueWithAGivenFieldWorksAsExpected() throws Exception {
        // GIVEN
        FromFooSimpleNoGetters fromFooSimpleNoGetters = createFromFooSimpleNoGetters();
        Field field = FromFooSimpleNoGetters.class.getDeclaredField(ID_FIELD_NAME);

        // WHEN
        Object actual = underTest.getFieldValue(fromFooSimpleNoGetters, field);

        // THEN
        assertThat(actual).isEqualTo(ZERO);
    }

    /**
     * Creates the parameters to be used for testing the method {@code getFieldValue}.
     * @return parameters to be used for testing the the method {@code getFieldValue}.
     */
    @DataProvider
    private Object[][] dataGetFieldValueTesting() {
        MutableToFoo mutableToFoo = createMutableToFoo(ZERO);
        return new Object[][] {
                {"Tests that the method returns the field value", mutableToFoo, ID_FIELD_NAME, BigInteger.class, ZERO},
                {"Tests that the method returns null if the required field is inside a null object", mutableToFoo, NESTED_OBJECT_NAME_FIELD_NAME,
                    String.class, null},
                {"Tests that the method returns the field value even if there is no getter method defined", createFromFooSimpleNoGetters(),
                    ID_FIELD_NAME, BigInteger.class, ZERO}
        };
    }

    /**
     * Tests that the method {@link ReflectionUtils#getFieldValue(Object, String, Class) getFieldValue} catches a runtime exception.
     */
    @Test
    public void testGetFieldValueCatchesRuntimeException() {
        // GIVEN
        MutableToFoo mutableToFoo = createMutableToFoo(null);
        ReflectionUtils underTestMock = Mockito.spy(ReflectionUtils.class);
        when(underTestMock.getDeclaredFieldType(LIST_FIELD_NAME, MutableToFoo.class)).thenThrow(new RuntimeException());

        // WHEN
        Object actual = underTestMock.getFieldValue(mutableToFoo, LIST_FIELD_NAME, FromFooSubClass.class);

        // THEN
        assertThat(actual).isNull();
        verify(underTestMock, times(1)).getDeclaredField(LIST_FIELD_NAME, MutableToFoo.class);
    }

    /**
     * Tests that the method {@code getFieldValue} throws Exception if the field does not exists.
     */
    @Test(expectedExceptions = MissingFieldException.class)
    public void testGetFieldValueThrowsExceptionIfTheFieldDoesNotExists() {
        // GIVEN
        MutableToFoo mutableToFoo = createMutableToFoo(null);

        // WHEN
        underTest.getFieldValue(mutableToFoo, NOT_EXISTING_FIELD_NAME, Object.class);
    }

    /**
     * Tests that the method {@code getFieldValueDirectAccess} returns the expected value.
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetFieldValueDirectAccessWorksAsExpected() throws Exception {
        // GIVEN
        MutableToFoo mutableToFoo = createMutableToFoo(ZERO);
        Method getFieldValueDirectAccessMethod = ReflectionUtils.class.getDeclaredMethod(GET_FIELD_VALUE_DIRECT_ACCESS_METHOD_NAME, Object.class, String.class);
        getFieldValueDirectAccessMethod.setAccessible(true);

        // WHEN
        Object actual = getFieldValueDirectAccessMethod.invoke(underTest, mutableToFoo, ID_FIELD_NAME);

        // THEN
        assertThat(actual).isEqualTo(ZERO);
    }

    /**
     * Tests that the method {@code getFieldValueDirectAccess} throws Exception in specific cases.
     * @param testCaseDescription the test case description
     * @param beanObject the java bean from which the value has to be retrieved
     * @param fieldName the name of the field to retrieve
     * @throws Exception if an error occurs
     */
    @Test(dataProvider = "dataGetFieldValueDirectAccessTesting", expectedExceptions = java.lang.reflect.InvocationTargetException.class)
    public void testGetFieldValueDirectAccessThrowsExceptionIfTheFieldDoesNotExists(final String testCaseDescription,
        final Object beanObject, final String fieldName) throws Exception {
        // GIVEN
        Method getFieldValueDirectAccessMethod = ReflectionUtils.class.getDeclaredMethod(GET_FIELD_VALUE_DIRECT_ACCESS_METHOD_NAME, Object.class, String.class);
        getFieldValueDirectAccessMethod.setAccessible(true);

        // WHEN
       getFieldValueDirectAccessMethod.invoke(underTest, beanObject, fieldName);
    }

    /**
     * Creates the parameters to be used for testing the method {@code getFieldValueDirectAccess}.
     * @return parameters to be used for testing the the method {@code getFieldValueDirectAccess}.
     */
    @DataProvider
    private Object[][] dataGetFieldValueDirectAccessTesting() {
        MutableToFoo mutableToFoo = createMutableToFoo(null);
        return new Object[][] {
                {"Tests that the method throws Exception if the field does not exists", mutableToFoo, NOT_EXISTING_FIELD_NAME},
                {"Tests that the method throws Exception if the bean is null", null, NESTED_OBJECT_NAME_FIELD_NAME}
        };
    }

    /**
     * Tests that the method {@code handleReflectionException} raises the expected exception.
     */
    @Test
    public void testHandleReflectionExceptionThrowsMissingMethodExceptionWhenGivenExceptionIsNoSuchMethodException() {
        // GIVEN
        NoSuchMethodException noSuchMethodException = new NoSuchMethodException();

        // WHEN
        RuntimeException actual = underTest.handleReflectionException(noSuchMethodException);

        // THEN
        assertThat(actual).isInstanceOf(MissingMethodException.class);
    }

    /**
     * Tests that the method {@code handleReflectionException} returns the expected exception.
     */
    @Test
    public void testHandleReflectionExceptionThrowsIllegalStateExceptionWhenGivenExceptionIsIllegalAccessException() {
        // GIVEN
        IllegalAccessException illegalAccessException = new IllegalAccessException();

        // WHEN
        RuntimeException actual = underTest.handleReflectionException(illegalAccessException);

        // THEN
        assertThat(actual).isInstanceOf(IllegalStateException.class);
    }

    /**
     * Tests that the method {@code handleReflectionException} returns the expected exception.
     */
    @Test
    public void testHandleReflectionExceptionThrowsRuntimeExceptionWhenGivenExceptionIsRuntimeException() {
        // GIVEN
        RuntimeException runtimeException = new RuntimeException();

        // WHEN
        RuntimeException actual = underTest.handleReflectionException(runtimeException);

        // THEN
        assertThat(actual).isInstanceOf(RuntimeException.class);
    }

    /**
     * Tests that the method {@code handleReflectionException} returns the expected exception.
     */
    @Test
    public void testHandleReflectionExceptionThrowsUndeclaredThrowableExceptionWhenGivenExceptionIsInvalidBeanException() {
        // GIVEN
        Exception genericException = new Exception();

        // WHEN
        RuntimeException actual = underTest.handleReflectionException(genericException);

        // THEN
        assertThat(actual).isInstanceOf(UndeclaredThrowableException.class);
    }

    /**
     * Tests that the method {@code getMapGenericType} throws Exception when the given type is not a map.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetMapGenericTypeThrowsIllegalArgumentExceptionWhenTheGivenTypeIsNotAMap() {
        // GIVEN

        // WHEN
        underTest.getMapGenericType(List.class, null, LIST_FIELD_NAME);
    }

    /**
     * Tests that the method {@code getGetterMethodPrefix} returns the expected value.
     * @param testCaseDescription the test case description
     * @param testClass the class to test
     * @param expectedResult the expected result
     * @throws Exception if something goes wrong
     */
    @Test(dataProvider = "dataGetGetterMethodPrefixTesting")
    public void testGetGetterMethodPrefixWorksAsExpected(final String testCaseDescription, final Class<?> testClass, final String expectedResult) throws Exception {
        // GIVEN
        Method method = underTest.getClass().getDeclaredMethod(GETTER_METHOD_PREFIX_METHOD_NAME, Class.class);
        method.setAccessible(true);

        // WHEN
        String actual = (String) method.invoke(underTest, testClass);

        // THEN
        assertThat(actual).isEqualTo(expectedResult);
    }

    /**
     * Creates the parameters to be used for testing the method {@code getGetterMethodPrefix}.
     * @return parameters to be used for testing the the method {@code getGetterMethodPrefix}.
     * @throws Exception if the method does not exists or the invoke fails.
     */
    @DataProvider
    private Object[][] dataGetGetterMethodPrefixTesting() throws Exception {
        return new Object[][]{
                {"Tests that the method returns the prefix: 'get' in case the returned class is a String ", String.class, MethodPrefix.GET.getPrefix()},
                {"Tests that the method returns the prefix: 'is' in case the returned class is a Boolean", Boolean.class, MethodPrefix.IS.getPrefix()},
                {"Tests that the method returns the prefix: 'is' in case the returned class is a primitive boolean",
                        FromFooSubClass.class.getDeclaredField(CHECK_FIELD_NAME).getType(), MethodPrefix.IS.getPrefix()}
        };
    }

    /**
     * Tests that the method {@code getFieldAnnotation} returns the proper annotation.
     * @param testCaseDescription the test case description
     * @param annotationToGet the annotation to retrieve
     * @param expectNull true if it's expected to find it null, false otherwise
     */
    @Test(dataProvider = "dataGetFieldAnnotationTesting")
    public void testGetFieldAnnotationWorksProperly(final String testCaseDescription, final Class<? extends Annotation> annotationToGet,
        final boolean expectNull) {
        // GIVEN
        Field nameField = underTest.getDeclaredField(ID_FIELD_NAME, ImmutableToFoo.class);

        // WHEN
        final Annotation actual = underTest.getFieldAnnotation(nameField, annotationToGet);

        // THEN
        assertThat(isNull(actual)).isEqualTo(expectNull);
    }

    /**
     * Creates the parameters to be used for testing the method {@code getFieldAnnotation}.
     * @return parameters to be used for testing the the method {@code getFieldAnnotation}.
     */
    @DataProvider
    private Object[][] dataGetFieldAnnotationTesting() {
        return new Object[][] {
                {"Tests that the method returns the field's annotation: 'NotNull' for the given field", NotNull.class, false},
                {"Tests that the method returns: null in case the searched annotation does not exists on the given field", NotBlank.class, true}
        };
    }

    /**
     * Tests that the method {@code getSetterMethodForField} returns the set method.
     */
    @Test
    public void testGetSetterMethodForFieldWorksProperly() {
        // GIVEN

        // WHEN
        final Method actual = underTest.getSetterMethodForField(MutableToFoo.class, ID_FIELD_NAME, BigInteger.class);

        // THEN
        assertThat(actual).hasFieldOrPropertyWithValue(NAME_FIELD_NAME, EXPECTED_SETTER_METHOD_NAME);
    }

    /**
     * Tests that the method {@code getSetterMethodForField} raises a MissingMethodException if the setter method for the given field does not exists.
     */
    @Test(expectedExceptions = MissingMethodException.class)
    public void testGetSetterMethodForFieldThrowsExceptionIfTheMethodDoesNotExists() {
        // GIVEN

        // WHEN
        underTest.getSetterMethodForField(MutableToFoo.class, NOT_EXISTING_FIELD_NAME, BigInteger.class);
    }

    /**
     * Tests that the method {@code getParameterAnnotations} returns the annotation when the parameter has an annotation with the given type.
     */
    @Test
    public void testGetParameterAnnotationReturnsTheAnnotationIfExists() {
        // GIVEN
        NotNull notNullAnnotation = mock(NotNull.class);
        Parameter parameter = mock(Parameter.class);
        when(parameter.isAnnotationPresent(NotNull.class)).thenReturn(true);
        when(parameter.getAnnotation(NotNull.class)).thenReturn(notNullAnnotation);

        // WHEN
        final Annotation actual = underTest.getParameterAnnotation(parameter, NotNull.class, DECLARING_CLASS_NAME);

        // THEN
        assertThat(actual).isEqualTo(notNullAnnotation);
    }

    /**
     * Tests that the method {@code getParameterAnnotations} returns null when the parameter has no annotation with the given type.
     */
    @Test
    public void testGetParameterAnnotationReturnsNullIfTheAnnotationDoesNotExists() {
        // GIVEN
        Parameter parameter = mock(Parameter.class);
        when(parameter.isAnnotationPresent(NotNull.class)).thenReturn(false);

        // WHEN
        final Annotation actual = underTest.getParameterAnnotation(parameter, NotNull.class, DECLARING_CLASS_NAME);

        // THEN
        assertThat(actual).isNull();
    }

    /**
     * Tests that the method {@code getSetterMethodForField} works properly.
     * @throws Exception if something goes wrong.
     */
    @Test
    public void testInvokeMethodWorksProperly() throws Exception {
        // GIVEN
        MutableToFoo mutableToFoo = createMutableToFoo(null);
        Method idSetterMethod = underTest.getSetterMethodForField(MutableToFoo.class, ID_FIELD_NAME, BigInteger.class);

        // WHEN
        getMethod(underTest.getClass(), INVOKE_METHOD_NAME, true, Method.class, Object.class, Object[].class)
                .invoke(underTest, idSetterMethod, mutableToFoo, new Object[] {ONE});

        // THEN
        assertThat(mutableToFoo).hasFieldOrPropertyWithValue(ID_FIELD_NAME, ONE);
    }

    /**
     * Tests that the method {@code getSetterMethodForField} raises an {@link InvocationTargetException} if the argument is wrong.
     */
    @Test
    public void testInvokeMethodRaisesAnIllegalArgumentExceptionIfTheArgumentIsWrong() {
        // GIVEN
        MutableToFoo mutableToFoo = createMutableToFoo(null);
        Method idSetterMethod = underTest.getSetterMethodForField(MutableToFoo.class, LIST_FIELD_NAME, List.class);


        // WHEN
        ThrowingCallable actual = () ->
                getMethod(underTest.getClass(), INVOKE_METHOD_NAME, true, Method.class, Object.class, Object[].class)
                        .invoke(underTest, idSetterMethod, mutableToFoo, new Object[]{ONE});

        // THEN
        assertThatThrownBy(actual).hasCauseInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Tests that the method {@code getDeclaredField} works properly.
     * @param testCaseDescription the test case description
     * @param fieldName the field to retrieve
     * @param targetClass the class where the field has to be searched
     */
    @Test(dataProvider = "dataGetDeclaredFieldTesting")
    public void testGetDeclaredFieldWorksProperly(final String testCaseDescription, final String fieldName, final Class<?> targetClass) {
        // GIVEN

        // WHEN
        Field actual = underTest.getDeclaredField(fieldName, targetClass);

        // THEN
        assertThat(actual).isNotNull();
    }

    /**
     * Test that the method: {@code getClassDeclaredField} throws the right exception.
     */
    @Test
    public void testGetClassDeclaredFieldThrowsTheRightException() {
        // GIVEN

        // WHEN
        Throwable actualException = catchThrowable(() -> {
            Method getClassDeclaredFieldMethod = underTest.getClass().getDeclaredMethod(GET_CLASS_DECLARED_FIELD_METHOD_NAME, String.class, Class.class);
            getClassDeclaredFieldMethod.setAccessible(true);
            getClassDeclaredFieldMethod.invoke(underTest, null, FromFoo.class);
        });

        // THEN
        assertThat(actualException).hasCauseInstanceOf(NullPointerException.class);
    }

    /**
     * Creates the parameters to be used for testing the method {@code getDeclaredField}.
     * @return parameters to be used for testing the the method {@code getDeclaredField}.
     */
    @DataProvider
    private Object[][] dataGetDeclaredFieldTesting() {
        return new Object[][] {
                {"Tests that the method returns the class field from first object", ID_FIELD_NAME, FromFooSubClass.class},
                {"Tests that the method returns the class field from a nested object", NESTED_OBJECT_NAME_FIELD_NAME, MutableToFoo.class}
        };
    }

    /**
     * Tests that the method {@code getDeclaredField} throws a {@link MissingFieldException} if the field does not exists.
     */
    @Test(expectedExceptions = MissingFieldException.class)
    public void testGetDeclaredFieldRaisesAnExceptionIfTheFieldDoesNotExists() {
        // GIVEN

        // WHEN
        Field actual = underTest.getDeclaredField(NOT_EXISTING_FIELD_NAME, FromFooSubClass.class);

        // THEN
        assertThat(actual).isNotNull();
    }

    /**
     * Tests that the method {@code getDeclaredFieldType} works properly.
     */
    @Test
    public void testGetDeclaredFieldTypeWorksProperly() {
        // GIVEN

        // WHEN
        Class<?> actual = underTest.getDeclaredFieldType(ID_FIELD_NAME, FromFooSubClass.class);

        // THEN
        assertThat(actual).isNotNull();
    }


    /**
     * Tests that the method {@code getGenericFieldType} works properly.
     * @param testCaseDescription the test case description
     * @param fieldName the field name for which the generic type has to be retrieved
     * @param fieldClass the class owning the field
     * @param expectedType the expected generic class type
     */
    @Test(dataProvider = "dataGetGenericFieldTypeTesting")
    public void testGetGenericFieldTypeWorksProperly(final String testCaseDescription, final String fieldName, final Class<?> fieldClass, final Class<?> expectedType) {
        // GIVEN
        Field field = underTest.getDeclaredField(fieldName, fieldClass);

        // WHEN
        Class<?> actual = underTest.getGenericFieldType(field);

        // THEN
        assertThat(actual).isEqualTo(expectedType);
    }

    /**
     * Creates the parameters to be used for testing the method {@code getGenericFieldType}.
     * @return parameters to be used for testing the the method {@code getGenericFieldType}.
     */
    @DataProvider
    private Object[][] dataGetGenericFieldTypeTesting() {
        return new Object[][] {
                {"Tests that the method returns a type String.", LIST_FIELD_NAME, ImmutableToFoo.class, String.class},
                {"Tests that the method returns a type Object in case of wildcard types.", LIST_FIELD_NAME, ImmutableToFooAdvFields.class, Object.class}
        };
    }

    /**
     * Tests that the method {@code getArrayType} works properly.
     */
    @Test
    public void testGetArrayTypeWorksProperly() {
        // GIVEN
        Field phoneNumbersField = underTest.getDeclaredField(PHONE_NUMBERS_FIELD_NAME, FromSubFoo.class);

        // WHEN
        Class<?> actual = underTest.getArrayType(phoneNumbersField);

        // THEN
        assertThat(actual).isEqualTo(int.class);
    }

    /**
     * Tests that the method {@code getGenericFieldType} works properly.
     */
    @Test
    public void testMapGenericFieldTypeWorksProperly() {
        // GIVEN
        MapElemType keyType = ItemType.builder()
                .objectClass(String.class)
                .build();
        final MapType expectedElemType = new MapType(keyType, keyType);
        final MapType expectedMapType = new MapType(keyType, expectedElemType);
        Field field = underTest.getDeclaredField(VERY_COMPLEX_MAP_FIELD_NAME, ImmutableToSubFoo.class);

        // WHEN
        MapType actual = underTest.getMapGenericType(field.getGenericType(), field.getDeclaringClass().getName(), field.getName());

        // THEN
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expectedMapType);
    }

    @Test
    public void testMapGenericFieldTypeWorksProperlyForUnparametrizedMap() {
        // GIVEN
        MapElemType keyType = ItemType.builder()
                .objectClass(Map.class)
                .build();
        final MapType expectedMapType = new MapType(keyType, keyType);
        Field field = underTest.getDeclaredField(UNPARAMETRIZED_MAP_FIELD_NAME, FromFooMap.class);

        // WHEN
        MapType actual = underTest.getMapGenericType(field.getGenericType(), field.getDeclaringClass().getName(), field.getName());

        // THEN
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expectedMapType);
    }

    /**
     * Tests that the method {@code getGetterMethod} works properly.
     * @throws Exception in case an error occurs
     */
    @Test
    public void testGetGetterMethodWorksProperly() throws Exception {
        // GIVEN
        Method methodUnderTest = getMethod(underTest.getClass(), GET_GETTER_METHOD_NAME, true, Class.class, String.class, Class.class);

        // WHEN
        Object actual = methodUnderTest.invoke(underTest, FromFooSimple.class, ID_FIELD_NAME, BigInteger.class);

        // THEN
        assertThat(actual).isNotNull();
    }

    /**
     * Tests that the method returned by {@code getGetterMethod} returns the expected value once invoked.
     * @throws Exception in case an error occurs
     */
    @Test
    public void testThatTheReturnedMethodFromGetGetterMethodReturnsTheExpectedValue() throws Exception {
        // GIVEN
        MutableToFoo mutableToFoo = createMutableToFoo(ONE);
        Method getGetterMethod = getMethod(underTest.getClass(), GET_GETTER_METHOD_NAME, true, Class.class, String.class, Class.class);
        Method methodUnderTest = (Method) getGetterMethod.invoke(underTest, MutableToFoo.class, ID_FIELD_NAME, BigInteger.class);

        // WHEN
        BigInteger actual = (BigInteger) methodUnderTest.invoke(mutableToFoo);

        // THEN
        assertThat(actual).isEqualTo(ONE);
    }

    /**
     * Tests that the method {@code getGetterMethod} raises an exception in case the given class does not have the method.
     * @throws Exception in case an error occurs
     */
    @Test
    public void testGetGetterMethodThrowsExceptionIfTheMethodDoesNotExists() throws Exception {
        // GIVEN
        Method methodUnderTest = getMethod(underTest.getClass(), GET_GETTER_METHOD_NAME, true, Class.class, String.class, Class.class);

        // WHEN
        ThrowingCallable actual = () ->
                methodUnderTest.invoke(underTest, FromFooSimpleNoGetters.class, ID_FIELD_NAME, BigInteger.class);

        // THEN
        assertThatThrownBy(actual).hasCauseInstanceOf(MissingFieldException.class);
    }

    /**
     * Tests that the method {@code setFieldValue} tries to inject the value through the setter method if the direct access fails.
     */
    @Test(expectedExceptions = NullPointerException.class)
    public void testSetFieldValueTriesToInjectThroughSetterMethodInCaseOfErrors() {
        // GIVEN
        MutableToFooSimple mutableToFoo = new MutableToFooSimple();
        Field idField = underTest.getDeclaredField(ID_FIELD_NAME, mutableToFoo.getClass());

        // WHEN
        underTest.setFieldValue(null, idField, ONE);
    }

    /**
     * Tests that the method {@code setFieldValue} throws an {@link IllegalArgumentException} if the field value is not valid.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetFieldValueRaiseAnExceptionIfTheValueToSetIsNotValid() {
        // GIVEN
        MutableToFooSimple mutableToFoo = new MutableToFooSimple();
        Field idField = underTest.getDeclaredField(ID_FIELD_NAME, mutableToFoo.getClass());

        // WHEN
        underTest.setFieldValue(mutableToFoo, idField, Boolean.TRUE);
    }

    @Test
    public void testSetFieldValueWorksProperly() {
        // GIVEN
        MutableToFooSimple mutableToFoo = new MutableToFooSimple();
        Field idField = underTest.getDeclaredField(ID_FIELD_NAME, mutableToFoo.getClass());

        // WHEN
        underTest.setFieldValue(mutableToFoo, idField, ONE);

        // THEN
        assertThat(mutableToFoo).hasFieldOrPropertyWithValue(ID_FIELD_NAME, ONE);
    }

    @Test
    public void testSetFieldValueInvokesTheSetterMethodInCaseAnExceptionIsRaised() {
        // GIVEN
        ReflectionUtils underTestMock = spy(ReflectionUtils.class);
        MutableToFooSimple mutableToFoo = new MutableToFooSimple();
        Field idField = underTest.getDeclaredField(ID_FIELD_NAME, mutableToFoo.getClass());
        doThrow(RuntimeException.class).when(underTestMock).setFieldValueWithoutSetterMethod(mutableToFoo, idField, ONE);

        // WHEN
        underTestMock.setFieldValue(mutableToFoo, idField, ONE);

        // THEN
        assertThat(mutableToFoo).hasFieldOrPropertyWithValue(ID_FIELD_NAME, ONE);
    }

    /**
     * Tests that the method {@code invokeMethod} manages the exception properly.
     * @param testCaseDescription the test case description
     * @param targetObject the object on which the method has to be invoked
     * @param methodNameToInvoke the name of the method to invoke
     * @param methodArg the argument to pass to the invoke method
     * @param isAccessible the accessibility that the {@code invokeMethod} must have
     * @param expectedException the expected exception class
     * @throws Exception if something goes wrong
     */
    @Test(dataProvider = "dataInvokeMethodTesting")
    public void testInvokeMethodCorrectlyHandlesExceptions(final String testCaseDescription, final Object targetObject, final String methodNameToInvoke,
        final Object methodArg, final boolean isAccessible, final Class<?> expectedException) throws Exception {
        // GIVEN
        Method setMethodToInvoke = getMethod(targetObject.getClass(), methodNameToInvoke, isAccessible, String.class);

        // WHEN
        ThrowingCallable actual = () ->
                underTest.invokeMethod(setMethodToInvoke, targetObject, methodArg);

        // THEN
        assertThatThrownBy(actual).isInstanceOf(expectedException);
    }

    /**
     * Creates the parameters to be used for testing the method {@code invokeMethod}.
     * @return parameters to be used for testing the the method {@code invokeMethod}.
     */
    @DataProvider
    private Object[][] dataInvokeMethodTesting() {
        return new Object[][] {
                {"Tests that the method raises an IllegalArgumentException in case the given argument is wrong", createMutableToFoo(ONE), SET_NAME_METHOD_NAME, ZERO,
                    true, IllegalArgumentException.class},
                {"Tests that the method raises an IllegalAccessException in case the method is not accessible", new MutableToFooAdvFields(), SET_INDEX_METHOD_NAME,
                    INDEX_NUMBER, false, IllegalStateException.class}
        };
    }

    /**
     * Test that the method: {@code getRealTarget} returns the object contained in the Optional.
     * @throws Exception if something goes wrong
     */
    @Test
    public void testGetSourceFieldValueRaisesAnExceptionIfTheParameterAreNull() throws Exception {
        // GIVEN
        Method getRealTargetMethod = getMethod(underTest.getClass(), GET_REAL_TARGET_METHOD_NAME, true, Object.class);
        Optional<BigInteger> optionalBigInteger = Optional.of(ZERO);

        // WHEN
        Object actual = getRealTargetMethod.invoke(underTest, optionalBigInteger);

        // THEN
        assertThat(actual).isEqualTo(ZERO);
    }

    /**
     * Retrieves a method.
     * @param targetClass the class on which look for the method
     * @param methodName the method to retrieve
     * @param makeAccessible sets the method visibility
     * @param parameterTypes the parameter types
     * @return the method
     * @throws NoSuchMethodException if the method does not exists
     */
    private Method getMethod(final Class<?> targetClass, final String methodName, final boolean makeAccessible, final Class<?>... parameterTypes)
            throws NoSuchMethodException {
        Method invokeMethod = targetClass.getDeclaredMethod(methodName, parameterTypes);
        invokeMethod.setAccessible(makeAccessible);
        return invokeMethod;
    }

    /**
     * Creates an instance of of {@link FromFooSimpleNoGetters}.
     * @return an instance of {@link FromFooSimpleNoGetters}
     */
    private FromFooSimpleNoGetters createFromFooSimpleNoGetters() {
        FromFooSimpleNoGetters fromFooSimpleNoGetters = new FromFooSimpleNoGetters();
        fromFooSimpleNoGetters.setId(ZERO);
        return fromFooSimpleNoGetters;
    }

    /**
     * Creates an instance of of {@link MutableToFoo} with the given id.
     * @param id the id to assign
     * @return an instance of {@link MutableToFoo}
     */
    private MutableToFoo createMutableToFoo(final BigInteger id) {
        MutableToFoo mutableToFoo = new MutableToFoo();
        mutableToFoo.setId(id);
        return mutableToFoo;
    }
}
