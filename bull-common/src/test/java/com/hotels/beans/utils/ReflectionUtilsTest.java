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

package com.hotels.beans.utils;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;
import static java.util.Objects.isNull;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import static com.hotels.beans.constant.MethodPrefix.GET;
import static com.hotels.beans.constant.MethodPrefix.IS;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigInteger;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.mockito.InjectMocks;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.hotels.beans.error.MissingFieldException;
import com.hotels.beans.error.MissingMethodException;
import com.hotels.beans.model.ItemType;
import com.hotels.beans.model.MapElemType;
import com.hotels.beans.model.MapType;
import com.hotels.beans.sample.FromFooSimpleNoGetters;
import com.hotels.beans.sample.FromFooSubClass;
import com.hotels.beans.sample.FromSubFoo;
import com.hotels.beans.sample.immutable.ImmutableToFoo;
import com.hotels.beans.sample.immutable.ImmutableToSubFoo;
import com.hotels.beans.sample.mutable.MutableToFoo;
import com.hotels.beans.sample.mutable.MutableToFooSimple;

/**
 * Unit tests fro class: {@link ReflectionUtils}.
 */
public class ReflectionUtilsTest {
    private static final String ID_FIELD_NAME = "id";
    private static final String NOT_EXISTING_FIELD_NAME = "notExistingField";
    private static final String NESTED_OBJECT_FIELD_NAME = "nestedObject.name";
    private static final String LIST_FIELD_NAME = "list";
    private static final String PHONE_NUMBERS_FIELD_NAME = "phoneNumbers";
    private static final String GETTER_METHOD_PREFIX_METHOD_NAME = "getGetterMethodPrefix";
    private static final String EXPECTED_SETTER_METHOD_NAME = "setId";
    private static final String GET_FIELD_VALUE_DIRECT_ACCESS_METHOD_NAME = "getFieldValueDirectAccess";
    private static final String CHECK_FIELD_NAME = "check";
    private static final String DECLARING_CLASS_NAME = "declaringClassName";
    private static final String INVOKE_METHOD_NAME = "invokeMethod";
    private static final String VERY_COMPLEX_MAP_FIELD_NAME = "veryComplexMap";

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
        initMocks(this);
    }

    /**
     * Tests that the method {@code getFieldValue} returns the expected value.
     * @param testCaseDescription the test case description
     * @param beanObject the java bean from which the value has to be retrieved
     * @param fieldName the name of the field to retrieve
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "dataGetFieldValueTesting")
    public void testGetFieldValueWorksAsExpected(final String testCaseDescription, final Object beanObject, final String fieldName, final Object expectedResult) {
        // GIVEN

        // WHEN
        Object actual = underTest.getFieldValue(beanObject, fieldName);

        // THEN
        assertEquals(expectedResult, actual);
    }

    /**
     * Creates the parameters to be used for testing the method {@code getFieldValue}.
     * @return parameters to be used for testing the the method {@code getFieldValue}.
     */
    @DataProvider
    private Object[][] dataGetFieldValueTesting() {
        MutableToFoo mutableToFoo = createMutableToFoo(ZERO);
        return new Object[][] {
                {"Tests that the method returns the field value", mutableToFoo, ID_FIELD_NAME, ZERO},
                {"Tests that the method returns null if the required field is inside a null object", mutableToFoo, NESTED_OBJECT_FIELD_NAME, null},
                {"Tests that the method returns the field value even if there is no getter method defined", createFromFooSimpleNoGetters(), ID_FIELD_NAME, ZERO}
        };
    }

    /**
     * Tests that the method {@code getFieldValue} throws Exception if the field does not exists.
     */
    @Test(expectedExceptions = MissingFieldException.class)
    public void testGetFieldValueThrowsExceptionIfTheFieldDoesNotExists() {
        // GIVEN
        MutableToFoo mutableToFoo = createMutableToFoo(null);

        // WHEN
        underTest.getFieldValue(mutableToFoo, NOT_EXISTING_FIELD_NAME);
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
        assertEquals(ZERO, actual);
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
                {"Tests that the method throws Exception if the bean is null", null, NESTED_OBJECT_FIELD_NAME}
        };
    }

    /**
     * Tests that the method {@code handleReflectionException} raises the expected exception.
     */
    @Test(expectedExceptions = IllegalStateException.class)
    public void testHandleReflectionExceptionThrowsIllegalStateExceptionWhenGivenExceptionIsNoSuchMethodException() {
        // GIVEN
        NoSuchMethodException noSuchMethodException = new NoSuchMethodException();

        // WHEN
        underTest.handleReflectionException(noSuchMethodException);
    }

    /**
     * Tests that the method {@code handleReflectionException} raises the expected exception.
     */
    @Test(expectedExceptions = IllegalStateException.class)
    public void testHandleReflectionExceptionThrowsIllegalStateExceptionWhenGivenExceptionIsIllegalAccessException() {
        // GIVEN
        IllegalAccessException illegalAccessException = new IllegalAccessException();

        // WHEN
        underTest.handleReflectionException(illegalAccessException);
    }

    /**
     * Tests that the method {@code handleReflectionException} raises the expected exception.
     */
    @Test(expectedExceptions = RuntimeException.class)
    public void testHandleReflectionExceptionThrowsRuntimeExceptionWhenGivenExceptionIsRuntimeException() {
        // GIVEN
        RuntimeException runtimeException = new RuntimeException();

        // WHEN
        underTest.handleReflectionException(runtimeException);
    }

    /**
     * Tests that the method {@code handleReflectionException} raises the expected exception.
     */
    @Test(expectedExceptions = UndeclaredThrowableException.class)
    public void testHandleReflectionExceptionThrowsUndeclaredThrowableExceptionWhenGivenExceptionIsInvalidBeanException() {
        // GIVEN
        Exception exception = new Exception();

        // WHEN
        underTest.handleReflectionException(exception);
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
        assertEquals(expectedResult, actual);
    }

    /**
     * Creates the parameters to be used for testing the method {@code getGetterMethodPrefix}.
     * @return parameters to be used for testing the the method {@code getGetterMethodPrefix}.
     * @throws Exception if the method does not exists or the invoke fails.
     */
    @DataProvider
    private Object[][] dataGetGetterMethodPrefixTesting() throws Exception {
        return new Object[][] {
                {"Tests that the method returns the prefix: 'get' in case the returned class is a String ", String.class, GET.getPrefix()},
                {"Tests that the method returns the prefix: 'is' in case the returned class is a Boolean", Boolean.class, IS.getPrefix()},
                {"Tests that the method returns the prefix: 'is' in case the returned class is a primitive boolean",
                        FromFooSubClass.class.getDeclaredField(CHECK_FIELD_NAME).getType(), IS.getPrefix()}
        };
    }

    /**
     * Tests that the method {@code getFieldAnnotation} returns the proper annotation.
     * @param testCaseDescription the test case description
     * @param annotationToGet the annotation to retrieve
     * @param expectNull true if it's expected to find it null, false otherwise
     * @throws NoSuchFieldException if the field does not exists.
     */
    @Test(dataProvider = "dataGetFieldAnnotationTesting")
    public void testGetFieldAnnotationWorksProperly(final String testCaseDescription, final Class<? extends Annotation> annotationToGet,
        final boolean expectNull) throws NoSuchFieldException {
        // GIVEN
        Field nameField = underTest.getDeclaredField(ID_FIELD_NAME, ImmutableToFoo.class);

        // WHEN
        final Annotation actual = underTest.getFieldAnnotation(nameField, annotationToGet);

        // THEN
        assertEquals(expectNull, isNull(actual));
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
        assertNotNull(actual);
        assertEquals(EXPECTED_SETTER_METHOD_NAME, actual.getName());
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
        assertNotNull(actual);
        assertEquals(notNullAnnotation, actual);
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
        assertNull(actual);
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
        getMethod(INVOKE_METHOD_NAME).invoke(underTest, idSetterMethod, mutableToFoo, new Object[] {ONE});

        // THEN
        assertEquals(ONE, mutableToFoo.getId());
    }

    /**
     * Tests that the method {@code getSetterMethodForField} raises an {@link InvocationTargetException} if the argument is wrong.
     * @throws Exception if something goes wrong.
     */
    @Test(expectedExceptions = InvocationTargetException.class)
    public void testInvokeMethodRaisesAnIllegalArgumentExceptionIfTheArgumentIsWrong() throws Exception {
        // GIVEN
        MutableToFoo mutableToFoo = createMutableToFoo(null);
        Method idSetterMethod = underTest.getSetterMethodForField(MutableToFoo.class, LIST_FIELD_NAME, List.class);

        // WHEN
        getMethod(INVOKE_METHOD_NAME).invoke(underTest, idSetterMethod, mutableToFoo, new Object[] {ONE});
    }

    /**
     * Tests that the method {@code getDeclaredField} works properly.
     */
    @Test
    public void testGetDeclaredFieldWorksProperly() {
        // GIVEN

        // WHEN
        Field actual = underTest.getDeclaredField(ID_FIELD_NAME, FromFooSubClass.class);

        // THEN
        assertNotNull(actual);
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
        assertNotNull(actual);
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
        assertNotNull(actual);
    }

    /**
     * Tests that the method {@code getGenericFieldType} works properly.
     */
    @Test
    public void testGetGenericFieldTypeWorksProperly() {
        // GIVEN
        Field field = underTest.getDeclaredField(LIST_FIELD_NAME, ImmutableToFoo.class);

        // WHEN
        Class<?> actual = underTest.getGenericFieldType(field);

        // THEN
        assertEquals(String.class, actual);
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
        assertEquals(int.class, actual);
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
        ItemType expectedMapKeyType = (ItemType) expectedMapType.getKeyType();
        ItemType actualMapKeyType = (ItemType) actual.getKeyType();
        ItemType expectedNestedMapKeyType = (ItemType) ((MapType) expectedMapType.getElemType()).getKeyType();
        ItemType actualNestedMapKeyType = (ItemType) ((MapType) actual.getElemType()).getKeyType();
        ItemType expectedNestedMapElemType = (ItemType) ((MapType) expectedMapType.getElemType()).getElemType();
        ItemType actualNestedMapElemType = (ItemType) ((MapType) actual.getElemType()).getElemType();

        // THEN
        assertEquals(expectedMapKeyType.getObjectClass(), actualMapKeyType.getObjectClass());
        assertEquals(expectedMapKeyType.getGenericClass(), actualMapKeyType.getGenericClass());
        assertEquals(expectedNestedMapKeyType.getObjectClass(), actualNestedMapKeyType.getObjectClass());
        assertEquals(expectedNestedMapKeyType.getGenericClass(), actualNestedMapKeyType.getGenericClass());
        assertEquals(expectedNestedMapElemType.getObjectClass(), actualNestedMapElemType.getObjectClass());
        assertEquals(expectedNestedMapElemType.getGenericClass(), actualNestedMapElemType.getGenericClass());
    }

    /**
     * Tests that the method {@code setFieldValue} works properly.
     */
    @Test
    public void testSetFieldValueWorksProperly() {
        // GIVEN
        MutableToFooSimple mutableToFoo = new MutableToFooSimple();
        Field idField = underTest.getDeclaredField(ID_FIELD_NAME, mutableToFoo.getClass());

        // WHEN
        underTest.setFieldValue(mutableToFoo, idField, ONE);

        // THEN
        assertEquals(ONE, mutableToFoo.getId());
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

    /**
     * Retrieves a method.
     * @param methodName the method to retrieve
     * @return the method
     * @throws NoSuchMethodException if the method does not exists
     */
    private Method getMethod(final String methodName) throws NoSuchMethodException {
        Method invokeMethod = underTest.getClass().getDeclaredMethod(methodName, Method.class, Object.class, Object[].class);
        invokeMethod.setAccessible(true);
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
