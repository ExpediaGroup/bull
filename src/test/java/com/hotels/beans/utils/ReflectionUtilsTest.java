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

package com.hotels.beans.utils;

import static java.math.BigInteger.ZERO;

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
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigInteger;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.testng.annotations.Test;
import org.mockito.InjectMocks;
import org.testng.annotations.BeforeMethod;

import com.hotels.beans.error.MissingMethodException;
import com.hotels.beans.error.MissingFieldException;
import com.hotels.beans.sample.FromFooSubClass;
import com.hotels.beans.sample.immutable.ImmutableToFoo;
import com.hotels.beans.sample.mutable.MutableToFoo;

/**
 * Unit tests fro class: {@link ReflectionUtils}.
 */
public class ReflectionUtilsTest {
    private static final String ID_FIELD_NAME = "id";
    private static final String NOT_EXISTING_FIELD_NAME = "notExistingField";
    private static final String LIST_FIELD_NAME = "list";
    private static final String GETTER_METHOD_PREFIX_METHOD_NAME = "getGetterMethodPrefix";
    private static final String CHECK_FIELD_NAME = "check";
    private static final String EXPECTED_SETTER_METHOD_NAME = "setId";

    /**
     * The class to be tested.
     */
    @InjectMocks
    private ReflectionUtils underTest;

    /**
     * Initializes mock.
     */
    @BeforeMethod
    public void beforeMethod() {
        initMocks(this);
    }

    /**
     * Tests that the method {@code getFieldValue} returns the expected value.
     *
     * @throws NoSuchFieldException if the field does not exists.
     */
    @Test
    public void testGetFieldValueWorksAsExpected() throws NoSuchFieldException {
        // GIVEN
        MutableToFoo mutableToFoo = new MutableToFoo();
        mutableToFoo.setId(ZERO);
        Field idField = mutableToFoo.getClass().getDeclaredField(ID_FIELD_NAME);

        // WHEN
        Object actual = underTest.getFieldValue(mutableToFoo, idField);

        // THEN
        assertEquals(ZERO, actual);
    }

    /**
     * Tests that the method {@code getFieldValue} throws Exception if the field does not exists.
     */
    @Test(expectedExceptions = MissingFieldException.class)
    public void testGetFieldValueThrowsExceptionIfTheFieldDoesNotExists() {
        // GIVEN
        MutableToFoo mutableToFoo = new MutableToFoo();
        mutableToFoo.setId(ZERO);

        // WHEN
        Object actual = underTest.getFieldValue(mutableToFoo, NOT_EXISTING_FIELD_NAME, Integer.class);

        // THEN
        assertEquals(ZERO, actual);
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
     *
     * @throws Exception if the method does not exists or the invoke fails.
     */
    @Test
    public void testGetGetterMethodPrefixWorksAsExpected() throws Exception {
        // GIVEN
        Method method = underTest.getClass().getDeclaredMethod(GETTER_METHOD_PREFIX_METHOD_NAME, Class.class);
        method.setAccessible(true);
        Field booleanField = FromFooSubClass.class.getDeclaredField(CHECK_FIELD_NAME);

        // WHEN
        String stringGetterMethodPrefix = (String) method.invoke(underTest, String.class);
        String booleanGetterMethodPrefix = (String) method.invoke(underTest, Boolean.class);
        String primitiveBooleanGetterMethodPrefix = (String) method.invoke(underTest, booleanField.getType());

        // THEN
        assertEquals(GET.getPrefix(), stringGetterMethodPrefix);
        assertEquals(IS.getPrefix(), booleanGetterMethodPrefix);
        assertEquals(IS.getPrefix(), primitiveBooleanGetterMethodPrefix);
    }

    /**
     * Tests that the method {@code getFieldAnnotation} returns the proper annotation.
     */
    @Test
    public void testGetFieldAnnotationWorksProperly() throws NoSuchFieldException {
        // GIVEN
        Field nameField = ImmutableToFoo.class.getDeclaredField(ID_FIELD_NAME);

        // WHEN
        final NotNull notNullFieldAnnotation = underTest.getFieldAnnotation(nameField, NotNull.class);
        final NotBlank notBlankFieldAnnotation = underTest.getFieldAnnotation(nameField, NotBlank.class);

        // THEN
        assertNotNull(notNullFieldAnnotation);
        assertNull(notBlankFieldAnnotation);
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
        final Annotation actual = underTest.getParameterAnnotation(parameter, NotNull.class, "declaringClassName");

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
        final Annotation actual = underTest.getParameterAnnotation(parameter, NotNull.class, "declaringClassName");

        // THEN
        assertNull(actual);
    }
}
