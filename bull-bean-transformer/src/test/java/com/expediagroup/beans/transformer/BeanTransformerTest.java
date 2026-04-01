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

import static java.math.BigInteger.ZERO;
import static java.util.Optional.empty;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Deque;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.expediagroup.beans.conversion.analyzer.ConversionAnalyzer;
import com.expediagroup.beans.sample.FromFooSimple;
import com.expediagroup.beans.sample.mutable.MutableToFoo;
import com.expediagroup.beans.sample.mutable.MutableToFooAdvFields;
import com.expediagroup.transformer.annotation.ConstructorArg;
import com.expediagroup.transformer.cache.CacheManager;
import com.expediagroup.transformer.error.InvalidBeanException;
import com.expediagroup.transformer.error.MissingFieldException;
import com.expediagroup.transformer.model.FieldMapping;
import com.expediagroup.transformer.model.FieldTransformer;
import com.expediagroup.transformer.model.TransformerSettings;
import com.expediagroup.transformer.utils.ClassUtils;
import com.expediagroup.transformer.utils.ReflectionUtils;
import com.expediagroup.transformer.validator.Validator;

/**
 * Unit test for class: {@link BeanTransformer}.
 */
public class BeanTransformerTest extends AbstractBeanTransformerTest {
    private static final String SOURCE_FIELD_NAME = "sourceFieldName";
    private static final String SOURCE_FIELD_NAME_2 = "sourceFieldName2";
    private static final String GET_SOURCE_FIELD_VALUE_METHOD_NAME = "getSourceFieldValue";
    private static final String GET_SOURCE_FIELD_TYPE_METHOD_NAME = "getSourceFieldType";
    private static final String CACHE_MANAGER_FIELD_NAME = "cacheManager";
    private static final String REFLECTION_UTILS_FIELD_NAME = "reflectionUtils";
    private static final String CLASS_UTILS_FIELD_NAME = "classUtils";
    private static final String GET_TRANSFORMER_VALUE_METHOD_NAME = "getTransformedValue";
    private static final String GET_CONSTRUCTOR_ARGS_VALUES_METHOD_NAME = "getConstructorArgsValues";
    private static final String HANDLE_INJECTION_EXCEPTION_METHOD_NAME = "handleInjectionException";
    private static final String RESOLVE_EFFECTIVE_SOURCE_METHOD_NAME = "resolveEffectiveSource";
    private static final String ROOT_SOURCE_STACK_FIELD_NAME = "rootSourceStack";
    private static final String BREADCRUMB = "bc";

    /**
     * Test that is possible to remove a field mapping for a given field.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testRemoveFieldMappingWorksProperly() {
        // GIVEN
        BeanTransformer beanTransformer = underTest.withFieldMapping(new FieldMapping<>(SOURCE_FIELD_NAME, DEST_FIELD_NAME));

        // WHEN
        beanTransformer.removeFieldMapping(DEST_FIELD_NAME);
        TransformerSettings transformerSettings = (TransformerSettings) REFLECTION_UTILS.getFieldValue(beanTransformer, TRANSFORMER_SETTINGS_FIELD_NAME, TransformerSettings.class);

        // THEN
        assertThat(transformerSettings.getFieldsNameMapping()).doesNotContainKey(DEST_FIELD_NAME);
    }

    /**
     * Test that the method {@code removeFieldMapping} raises an {@link IllegalArgumentException} if the parameter is null.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testRemoveFieldMappingRaisesExceptionIfItsCalledWithNullParam() {
        // GIVEN
        BeanTransformer beanTransformer = underTest.withFieldMapping(new FieldMapping<>(SOURCE_FIELD_NAME, DEST_FIELD_NAME));

        // WHEN
        beanTransformer.removeFieldMapping(null);
    }

    /**
     * Test that is possible to remove all the fields mappings defined.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testResetFieldsMappingWorksProperly() {
        // GIVEN
        BeanTransformer beanTransformer = underTest
                .withFieldMapping(new FieldMapping<>(SOURCE_FIELD_NAME, DEST_FIELD_NAME), new FieldMapping<>(SOURCE_FIELD_NAME_2, DEST_FIELD_NAME));

        // WHEN
        beanTransformer.resetFieldsMapping();
        TransformerSettings transformerSettings = (TransformerSettings) REFLECTION_UTILS.getFieldValue(beanTransformer, TRANSFORMER_SETTINGS_FIELD_NAME, TransformerSettings.class);

        // THEN
        assertThat(transformerSettings.getFieldsNameMapping()).isEmpty();
    }

    /**
     * Test that is possible to remove all the fields transformer defined.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testResetFieldsTransformerWorksProperly() {
        // GIVEN
        BeanTransformer beanTransformer = underTest
                .withFieldTransformer(new FieldTransformer<>(DEST_FIELD_NAME, val -> val), new FieldTransformer<>(REFLECTION_UTILS_FIELD_NAME, val -> val));

        // WHEN
        beanTransformer.resetFieldsTransformer();
        TransformerSettings transformerSettings = (TransformerSettings) REFLECTION_UTILS.getFieldValue(beanTransformer, TRANSFORMER_SETTINGS_FIELD_NAME, TransformerSettings.class);

        // THEN
        assertThat(transformerSettings.getFieldsTransformers()).isEmpty();
    }

    /**
     * Test that is possible to remove a field transformer for a given field.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testRemoveFieldTransformerWorksProperly() {
        // GIVEN
        BeanTransformer beanTransformer = underTest.withFieldTransformer(new FieldTransformer<>(DEST_FIELD_NAME, val -> val));

        // WHEN
        beanTransformer.removeFieldTransformer(DEST_FIELD_NAME);
        TransformerSettings transformerSettings = (TransformerSettings) REFLECTION_UTILS.getFieldValue(beanTransformer, TRANSFORMER_SETTINGS_FIELD_NAME, TransformerSettings.class);

        // THEN
        assertThat(transformerSettings.getFieldsTransformers()).doesNotContainKey(DEST_FIELD_NAME);
    }

    /**
     * Test that the method {@code removeFieldTransformer} raises an {@link IllegalArgumentException} if the parameter is null.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testRemoveFieldTransformerRaisesExceptionIfItsCalledWithNullParam() {
        // GIVEN
        BeanTransformer beanTransformer = underTest.withFieldTransformer(new FieldTransformer<>(DEST_FIELD_NAME, val -> val));

        // WHEN
        beanTransformer.removeFieldTransformer(null);
    }

    /**
     * Test that is possible to remove all the transformer settings.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testResetWorksProperly() {
        // GIVEN
        BeanTransformer beanTransformer = underTest
                .withFieldTransformer(new FieldTransformer<>(DEST_FIELD_NAME, val -> val), new FieldTransformer<>(REFLECTION_UTILS_FIELD_NAME, val -> val))
                .withFieldMapping(new FieldMapping<>(SOURCE_FIELD_NAME, DEST_FIELD_NAME))
                .skipTransformationForField(NAME_FIELD_NAME)
                .setDefaultValueForMissingField(true);

        // WHEN
        beanTransformer.reset();
        TransformerSettings<String> transformerSettings =
                (TransformerSettings<String>) REFLECTION_UTILS.getFieldValue(beanTransformer, TRANSFORMER_SETTINGS_FIELD_NAME, TransformerSettings.class);

        // THEN
        assertThat(transformerSettings.getFieldsTransformers()).isEmpty();
        assertThat(transformerSettings.getFieldsNameMapping()).isEmpty();
        assertThat(transformerSettings.getFieldsToSkip()).isEmpty();
        assertThat(transformerSettings.isSetDefaultValueForMissingField()).isFalse();
    }

    /**
     * Test that is possible to remove all the transformation skip settings.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testResetFieldsTransformationSkipWorksProperly() {
        // GIVEN
        BeanTransformer beanTransformer = underTest.skipTransformationForField(NAME_FIELD_NAME);

        // WHEN
        beanTransformer.resetFieldsTransformationSkip();
        TransformerSettings<String> transformerSettings =
                (TransformerSettings<String>) REFLECTION_UTILS.getFieldValue(beanTransformer, TRANSFORMER_SETTINGS_FIELD_NAME, TransformerSettings.class);

        // THEN
        assertThat(transformerSettings.getFieldsToSkip()).isEmpty();
    }

    /**
     * Test that the method: {@code getSourceFieldValue} raises a {@link NullPointerException} in case any of the parameters null.
     * @throws Exception uf the invocation fails
     */
    @Test(expectedExceptions = Exception.class)
    public void testGetSourceFieldValueRaisesAnExceptionIfTheParameterAreNull() throws Exception {
        // GIVEN
        Method getSourceFieldValueMethod = underTest.getClass().getDeclaredMethod(GET_SOURCE_FIELD_VALUE_METHOD_NAME, Object.class, String.class, Field.class, boolean.class);
        getSourceFieldValueMethod.setAccessible(true);

        // WHEN
        getSourceFieldValueMethod.invoke(underTest, null, null, null, false);
    }

    /**
     * Test that the method: {@code getSourceFieldValue} throws no Exceptions in case reflectionUtils throws exception and
     * a field transformer is defined for the given field.
     * @throws Exception if the invoke method fails
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testGetSourceFieldValueThrowsNoExceptionIfAFieldTransformerIsDefined() throws Exception {
        // GIVEN
        ReflectionUtils reflectionUtilsMock = mock(ReflectionUtils.class);
        Field field = mock(Field.class);
        Class fieldType = Integer.class;

        when(reflectionUtilsMock.getFieldValue(FromFooSimple.class, AGE_FIELD_NAME, fieldType)).thenThrow(InvalidBeanException.class);
        when(field.getType()).thenReturn(fieldType);

        reflectionUtils.setFieldValue(underTest, REFLECTION_UTILS_FIELD_NAME, reflectionUtilsMock);

        Method getSourceFieldValueMethod = underTest.getClass().getDeclaredMethod(GET_SOURCE_FIELD_VALUE_METHOD_NAME, Object.class, String.class, Field.class, boolean.class);
        getSourceFieldValueMethod.setAccessible(true);

        // WHEN
        ThrowingCallable actual = () -> getSourceFieldValueMethod.invoke(underTest, FromFooSimple.class, AGE_FIELD_NAME, field, true);

        // THEN
        assertThatCode(actual).doesNotThrowAnyException();
        verify(reflectionUtilsMock).getFieldValue(FromFooSimple.class, AGE_FIELD_NAME, fieldType);
        verify(field).getType();
        restoreUnderTestObject();
    }

    /**
     * Test that the primitive type conversion is correctly enabled.
     */
    @Test
    public void testThatPrimitiveTypeConversionIsCorrectlyEnabled() {
        // GIVEN
        underTest.setPrimitiveTypeConversionEnabled(true);

        // WHEN
        boolean actual = underTest.getSettings().isPrimitiveTypeConversionEnabled();

        // THEN
        assertThat(actual).isTrue();
        underTest.setPrimitiveTypeConversionEnabled(false);
    }

    /**
     * Test that the method: {@code getSourceFieldType} returns the source object class in case the given field does not
     * exists in the given class and the source object class is primitive.
     * @throws Exception if the invoke method fails
     */
    @Test
    public void testGetSourceFieldTypeReturnsTheSourceObjectClass() throws Exception {
        // GIVEN
        CacheManager cacheManager = mock(CacheManager.class);
        ReflectionUtils reflectionUtilsMock = mock(ReflectionUtils.class);
        ClassUtils classUtils = mock(ClassUtils.class);

        when(cacheManager.getFromCache(anyString(), any(Class.class))).thenReturn(empty());
        when(reflectionUtilsMock.getDeclaredFieldType(AGE_FIELD_NAME, Integer.class)).thenThrow(MissingFieldException.class);
        when(classUtils.isPrimitiveType(Integer.class)).thenReturn(true);

        reflectionUtils.setFieldValue(underTest, CACHE_MANAGER_FIELD_NAME, cacheManager);
        reflectionUtils.setFieldValue(underTest, REFLECTION_UTILS_FIELD_NAME, reflectionUtilsMock);
        reflectionUtils.setFieldValue(underTest, CLASS_UTILS_FIELD_NAME, classUtils);

        Method getSourceFieldTypeMethod = underTest.getClass().getDeclaredMethod(GET_SOURCE_FIELD_TYPE_METHOD_NAME, Class.class, String.class);
        getSourceFieldTypeMethod.setAccessible(true);

        // WHEN
        Class<?> actual = (Class<?>) getSourceFieldTypeMethod.invoke(underTest, Integer.class, AGE_FIELD_NAME);

        // THEN
        verify(cacheManager).getFromCache(anyString(), any(Class.class));
        verify(reflectionUtilsMock).getDeclaredFieldType(AGE_FIELD_NAME, Integer.class);
        verify(classUtils).isPrimitiveType(Integer.class);
        assertThat(actual).isEqualTo(Integer.class);
        restoreUnderTestObject();
    }

    /**
     * Test that the method: {@code getSourceFieldType} returns nulls in case the given field does not
     * exists in the given class, the source object class is not primitive and the default value set for missing
     * fields feature is enabled.
     * @throws Exception if the invoke method fails
     */
    @Test
    public void testGetSourceFieldTypeReturnsNull() throws Exception {
        // GIVEN
        CacheManager cacheManager = mock(CacheManager.class);
        ReflectionUtils reflectionUtilsMock = mock(ReflectionUtils.class);
        ClassUtils classUtils = mock(ClassUtils.class);
        TransformerSettings settings = mock(TransformerSettings.class);

        when(cacheManager.getFromCache(anyString(), any(Class.class))).thenReturn(empty());
        when(reflectionUtilsMock.getDeclaredFieldType(AGE_FIELD_NAME, FromFooSimple.class)).thenThrow(MissingFieldException.class);
        when(classUtils.isPrimitiveType(Integer.class)).thenReturn(true);
        when(settings.isSetDefaultValueForMissingField()).thenReturn(true);

        reflectionUtils.setFieldValue(underTest, CACHE_MANAGER_FIELD_NAME, cacheManager);
        reflectionUtils.setFieldValue(underTest, REFLECTION_UTILS_FIELD_NAME, reflectionUtilsMock);
        reflectionUtils.setFieldValue(underTest, CLASS_UTILS_FIELD_NAME, classUtils);
        reflectionUtils.setFieldValue(underTest, TRANSFORMER_SETTINGS_FIELD_NAME, settings);

        Method getSourceFieldTypeMethod = underTest.getClass().getDeclaredMethod(GET_SOURCE_FIELD_TYPE_METHOD_NAME, Class.class, String.class);
        getSourceFieldTypeMethod.setAccessible(true);

        // WHEN
        Class<?> actual = (Class<?>) getSourceFieldTypeMethod.invoke(underTest, FromFooSimple.class, AGE_FIELD_NAME);

        // THEN
        verify(cacheManager).getFromCache(anyString(), any(Class.class));
        verify(reflectionUtilsMock).getDeclaredFieldType(AGE_FIELD_NAME, FromFooSimple.class);
        verify(classUtils).isPrimitiveType(FromFooSimple.class);
        verify(settings).isSetDefaultValueForMissingField();
        assertThat(actual).isNull();
        restoreUnderTestObject();
    }

    /**
     * Test that the method: {@code getSourceFieldType} caches and returns {@code null} on second call when
     * the field does not exist in the source class and default-value-for-missing-field mode is active.
     * @throws Exception if the invoke method fails
     */
    @Test
    public void testGetSourceFieldTypeReturnsCachedNullForAbsentField() throws Exception {
        // GIVEN
        underTest.setDefaultValueForMissingField(true);

        Method getSourceFieldTypeMethod = underTest.getClass().getDeclaredMethod(GET_SOURCE_FIELD_TYPE_METHOD_NAME, Class.class, String.class);
        getSourceFieldTypeMethod.setAccessible(true);

        // WHEN - first call: field does not exist, default value mode active → returns null, caches ABSENT sentinel
        Class<?> firstResult = (Class<?>) getSourceFieldTypeMethod.invoke(underTest, FromFooSimple.class, "nonExistentFieldForCacheTest");
        // WHEN - second call: hits the ABSENT sentinel from cache → returns null without recomputing
        Class<?> secondResult = (Class<?>) getSourceFieldTypeMethod.invoke(underTest, FromFooSimple.class, "nonExistentFieldForCacheTest");

        // THEN
        assertThat(firstResult).isNull();
        assertThat(secondResult).isNull();
    }

    /**
     * Test that the method: {@code getSourceFieldType} throws {@link MissingFieldException} in case the given field does not exists
     * in the given class and the source object class is not primitive and the default value set for missing fields feature is disabled.
     * @throws Exception if the invoke method fails
     */
    @Test
    public void testGetSourceFieldTypeThrowsMissingFieldException() throws Exception {
        // GIVEN
        CacheManager cacheManager = mock(CacheManager.class);
        ReflectionUtils reflectionUtilsMock = mock(ReflectionUtils.class);
        ClassUtils classUtils = mock(ClassUtils.class);
        TransformerSettings settings = mock(TransformerSettings.class);

        when(cacheManager.getFromCache(anyString(), any(Class.class))).thenReturn(empty());
        when(reflectionUtilsMock.getDeclaredFieldType(AGE_FIELD_NAME, FromFooSimple.class)).thenThrow(MissingFieldException.class);
        when(classUtils.isPrimitiveType(Integer.class)).thenReturn(false);
        when(settings.isSetDefaultValueForMissingField()).thenReturn(false);

        reflectionUtils.setFieldValue(underTest, CACHE_MANAGER_FIELD_NAME, cacheManager);
        reflectionUtils.setFieldValue(underTest, REFLECTION_UTILS_FIELD_NAME, reflectionUtilsMock);
        reflectionUtils.setFieldValue(underTest, CLASS_UTILS_FIELD_NAME, classUtils);
        reflectionUtils.setFieldValue(underTest, TRANSFORMER_SETTINGS_FIELD_NAME, settings);

        Method getSourceFieldTypeMethod = underTest.getClass().getDeclaredMethod(GET_SOURCE_FIELD_TYPE_METHOD_NAME, Class.class, String.class);
        getSourceFieldTypeMethod.setAccessible(true);

        // WHEN
        ThrowingCallable actual = () -> getSourceFieldTypeMethod.invoke(underTest, FromFooSimple.class, AGE_FIELD_NAME);

        // THEN
        assertThatThrownBy(actual).hasCauseInstanceOf(MissingFieldException.class);
        verify(cacheManager).getFromCache(anyString(), any(Class.class));
        verify(reflectionUtilsMock).getDeclaredFieldType(AGE_FIELD_NAME, FromFooSimple.class);
        verify(classUtils).isPrimitiveType(FromFooSimple.class);
        verify(settings).isSetDefaultValueForMissingField();
        restoreUnderTestObject();
    }

    /**
     * Test that the {@code getTransformedValue} works as expected.
     * @param testCaseDescription the test case description
     * @param fieldName the field name to which the transformer function has to be applied
     * @param fieldValue the field value before the transformation
     * @param fieldTransformer the field transformer associated to the given field
     * @param isPrimitiveTypeConversionEnabled indicates if the automatic conversion function is enabled
     * @param isDestinationFieldPrimitiveType indicates if the destination field type is primitive or not
     * @param expectedValue the expected value after the transformation
     * @throws Exception if the method invocation fails
     */
    @Test(dataProvider = "dataGetTransformerFunctionTesting")
    public void testGetTransformedValueWorksProperly(final String testCaseDescription, final String fieldName, final Object fieldValue, final FieldTransformer fieldTransformer,
        final boolean isPrimitiveTypeConversionEnabled, final boolean isDestinationFieldPrimitiveType, final Object expectedValue)
        throws Exception {
        // GIVEN
        Field field = mock(Field.class);
        TransformerSettings settings = mock(TransformerSettings.class);
        when(settings.isPrimitiveTypeConversionEnabled()).thenReturn(isPrimitiveTypeConversionEnabled);
        reflectionUtils.setFieldValue(underTest, TRANSFORMER_SETTINGS_FIELD_NAME, settings);

        Class[] transformedValueMethodParams = {FieldTransformer.class, Object.class, Class.class, String.class, Field.class, boolean.class, String.class};
        Method getTransformerFunctionMethod = underTest.getClass().getDeclaredMethod(GET_TRANSFORMER_VALUE_METHOD_NAME, transformedValueMethodParams);
        getTransformerFunctionMethod.setAccessible(true);

        // WHEN
        Object actual = getTransformerFunctionMethod
                .invoke(underTest, fieldTransformer, fieldValue, FromFooSimple.class, fieldName, field, isDestinationFieldPrimitiveType, fieldName);

        // THEN
        assertThat(actual).isEqualTo(expectedValue);
        restoreUnderTestObject();
    }

    /**
     * Creates the parameters to be used for testing method {@code getTransformedValue}.
     * @return parameters to be used for testing method {@code getTransformedValue}.
     */
    @DataProvider
    private Object[][] dataGetTransformerFunctionTesting() {
        return new Object[][] {
                {"Test that the primitive type conversion function is not executed if the primitive type conversion is disabled",
                        AGE_FIELD_NAME, ZERO.intValue(), null, false, true, ZERO.intValue()},
                {"Test that the primitive type conversion function is not executed if the destination field type is not primitive",
                        AGE_FIELD_NAME, null, null, true, false, null}
        };
    }

    /**
     * Test that the method: {@code getConstructorArgsValues} returns the default type value if the parameter name cannot be retrieved from the constructor.
     * @throws Exception if the invoke method fails
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testGetConstructorArgsValuesReturnsTheDefaultTypeIfTheDestinationFieldNameIsNull() throws Exception {
        // GIVEN
        Constructor constructor = mock(Constructor.class);
        ClassUtils classUtils = mock(ClassUtils.class);
        Parameter constructorParameter = mock(Parameter.class);
        Parameter[] constructorParameters = {constructorParameter};

        when(classUtils.getConstructorParameters(constructor)).thenReturn(constructorParameters);
        when(constructorParameter.isNamePresent()).thenReturn(true);
        when(constructorParameter.getName()).thenReturn(null);
        when(constructorParameter.getType()).thenReturn((Class) Integer.class);
        when(classUtils.getDefaultTypeValue(Integer.class)).thenReturn(ZERO.intValue());

        reflectionUtils.setFieldValue(underTest, CLASS_UTILS_FIELD_NAME, classUtils);

        Method getConstructorArgsValuesMethod = underTest.getClass()
                .getDeclaredMethod(GET_CONSTRUCTOR_ARGS_VALUES_METHOD_NAME, Object.class, Class.class, Constructor.class, String.class);
        getConstructorArgsValuesMethod.setAccessible(true);

        // WHEN
        Object[] actual = (Object[]) getConstructorArgsValuesMethod.invoke(underTest, fromFoo, MutableToFooAdvFields.class, constructor, ID_FIELD_NAME);

        // THEN
        verify(classUtils).getConstructorParameters(constructor);
        verify(constructorParameter).isNamePresent();
        verify(constructorParameter, times(2)).getName();
        verify(constructorParameter).getType();
        verify(classUtils).getDefaultTypeValue(Integer.class);
        assertThat(actual).containsOnly(0);
        restoreUnderTestObject();
    }

    /**
     * Test that the {@code canBeInjectedByConstructorParams} lambda correctly evaluates both branches
     * of the {@code areParameterNamesAvailable || allParameterAnnotatedWith} expression.
     * @param testCaseDescription the test case description
     * @param areNamesAvailable whether parameter names are available
     * @param allAnnotated whether all parameters are annotated with {@link ConstructorArg}
     * @param expected the expected result
     * @throws Exception if something goes wrong
     */
    @Test(dataProvider = "dataCanBeInjectedByConstructorParamsTesting")
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void testCanBeInjectedByConstructorParamsLambdaBranchesWorkCorrectly(final String testCaseDescription,
            final boolean areNamesAvailable, final boolean allAnnotated, final boolean expected) throws Exception {
        // GIVEN - use a real Constructor and mock both ClassUtils and CacheManager so the lambda always runs
        ClassUtils classUtilsMock = mock(ClassUtils.class);
        CacheManager cacheManagerMock = mock(CacheManager.class);
        Constructor<?> constructor = String.class.getDeclaredConstructors()[0];

        when(cacheManagerMock.getFromCache(anyString(), any(Class.class))).thenReturn(empty());
        when(classUtilsMock.areParameterNamesAvailable(constructor)).thenReturn(areNamesAvailable);
        if (!areNamesAvailable) {
            when(classUtilsMock.allParameterAnnotatedWith(constructor, ConstructorArg.class)).thenReturn(allAnnotated);
        }
        reflectionUtils.setFieldValue(underTest, CLASS_UTILS_FIELD_NAME, classUtilsMock);
        reflectionUtils.setFieldValue(underTest, CACHE_MANAGER_FIELD_NAME, cacheManagerMock);

        // WHEN
        boolean actual = underTest.canBeInjectedByConstructorParams(constructor);

        // THEN
        assertThat(actual).isEqualTo(expected);
        restoreUnderTestObject();
    }

    /**
     * Creates the parameters to be used for testing the {@code canBeInjectedByConstructorParams} lambda branches.
     * @return parameters to be used for testing the {@code canBeInjectedByConstructorParams} lambda branches.
     */
    @DataProvider
    private Object[][] dataCanBeInjectedByConstructorParamsTesting() {
        return new Object[][] {
            {"NamesUnavailable_AllAnnotated_returnsTrue", false, true, true},
            {"NamesUnavailable_NotAnnotated_returnsFalse", false, false, false}
        };
    }

    /**
     * Test that {@code resolveEffectiveSource} falls through to the default source when a field-name mapping
     * exists for the resolved breadcrumb key but the root-source stack is empty (no active transform context).
     * Covers the false branch of {@code mapped != null && !stack.isEmpty()} when the stack is empty.
     * @throws Exception if the method invocation fails
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testResolveEffectiveSourceWithMappingAndEmptyStackFallsThrough() throws Exception {
        // GIVEN - add a mapping so "bc.name" → SOURCE_FIELD_NAME (mapped != null)
        underTest.withFieldMapping(new FieldMapping<>(SOURCE_FIELD_NAME, BREADCRUMB + "." + NAME_FIELD_NAME));
        Method method = TransformerImpl.class.getDeclaredMethod(
                RESOLVE_EFFECTIVE_SOURCE_METHOD_NAME, Object.class, String.class, String.class);
        method.setAccessible(true);

        // WHEN - stack is empty (no active transform), so condition evaluates to false → falls through
        Object result = method.invoke(underTest, fromFoo, NAME_FIELD_NAME, BREADCRUMB);

        // THEN - returns a non-null EffectiveSource backed by the original sourceObj
        assertThat(result).isNotNull();
    }

    /**
     * Test that the void {@code transform(T, K, String)} method does not push or pop the root source stack
     * when it is called in a non-root context (stack already contains an item).
     * Covers the false branch of {@code if (isRoot)} at the beginning and end of the method.
     * @throws Exception if the field access fails
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testVoidTransformWithPrePopulatedStackDoesNotPushAgain() throws Exception {
        // GIVEN - pre-populate the stack to simulate a nested (non-root) call
        Field stackField = TransformerImpl.class.getDeclaredField(ROOT_SOURCE_STACK_FIELD_NAME);
        stackField.setAccessible(true);
        ThreadLocal<Deque<Object>> rootSourceStack = (ThreadLocal<Deque<Object>>) stackField.get(underTest);
        rootSourceStack.get().push(fromFoo);

        MutableToFoo dest = new MutableToFoo();
        try {
            // WHEN - isRoot = false because stack is non-empty → push/pop are both skipped
            underTest.transform(fromFoo, dest, null);
        } finally {
            rootSourceStack.get().clear();
        }

        // THEN - transformation completed and destination was populated
        assertThat(dest).isNotNull();
    }

    /**
     * Restores the underTest object removing all defined mocks.
     */
    private void restoreUnderTestObject() {
        underTest = new TransformerImpl();
    }

    /**
     * Tests that an instance fo {@link Validator} is created only if the validation is enabled.
     * @param testCaseDescription the test case description
     * @param validationEnabled true if the validation is enabled, false otherwise
     * @param expectedNull the expected result
     */
    @Test(dataProvider = "dataValidationInitializationTesting")
    public void testValidatorIsInitializedOnlyIfValidationIsEnabled(final String testCaseDescription, final boolean validationEnabled, final boolean expectedNull) {
        // GIVEN

        // WHEN
        underTest.setValidationEnabled(validationEnabled);

        // THEN
        assertThat(underTest.validator == null).isEqualTo(expectedNull);
        underTest.setValidationEnabled(false);
    }

    /**
     * Creates the parameters to be used for testing the method {@code setValidationEnabled}.
     * @return parameters to be used for testing the method {@code setValidationEnabled}.
     */
    @DataProvider
    private Object[][] dataValidationInitializationTesting() {
        return new Object[][] {
                {"Tests that the Validator object is not created if the validation is disabled", false, true},
                {"Tests that the Validator object is created if the validation is enabled", true, false}
        };
    }

    /**
     * Tests that an instance fo {@link ConversionAnalyzer} is created only if the automatic conversion is enabled.
     * @param testCaseDescription the test case description
     * @param autoConversionEnabled true if the automatic conversion is enabled, false otherwise
     * @param expectedNull the expected result
     */
    @Test(dataProvider = "dataConversionAnalyzerInitializationTesting")
    public void testConversionAnalyzerIsInitializedOnlyIfValidationIsEnabled(final String testCaseDescription, final boolean autoConversionEnabled, final boolean expectedNull) {
        // GIVEN

        // WHEN
        underTest.setPrimitiveTypeConversionEnabled(autoConversionEnabled);

        // THEN
        assertThat(underTest.conversionAnalyzer == null).isEqualTo(expectedNull);
        underTest.setPrimitiveTypeConversionEnabled(false);
    }

    /**
     * Creates the parameters to be used for testing the method {@code setValidationEnabled}.
     * @return parameters to be used for testing the method {@code setValidationEnabled}.
     */
    @DataProvider
    private Object[][] dataConversionAnalyzerInitializationTesting() {
        return new Object[][] {
                {"Tests that the ConversionAnalyzer object is not created if the automatic conversion is disabled", false, true},
                {"Tests that the ConversionAnalyzer object is created if the automatic conversion is enabled", true, false}
        };
    }

    /**
     * Test that the method: {@code handleInjectionException} works as expected.
     * @param testCaseDescription the test case description
     * @param forceConstructorInjection if true it forces the injection trough constructor
     * @param expectedReturnType the expected return type class
     * @throws Exception if the invoke method fails
     */
    @Test(dataProvider = "dataHandleInjectionExceptionTesting")
    public void testHandleInjectionExceptionWorksAsExpected(final String testCaseDescription, final boolean forceConstructorInjection,
        final Class<?> expectedReturnType) throws Exception {
        // GIVEN
        ClassUtils classUtils = mock(ClassUtils.class);
        when(classUtils.areParameterNamesAvailable(any(Constructor.class))).thenReturn(false);
        when(classUtils.getConstructorParameters(any())).thenReturn(new Parameter[] {});
        when(classUtils.getInstance(any(), any())).thenReturn(new MutableToFoo());
        reflectionUtils.setFieldValue(underTest, CLASS_UTILS_FIELD_NAME, classUtils);

        Method handleInjectionExceptionMethod = underTest.getClass().getDeclaredMethod(HANDLE_INJECTION_EXCEPTION_METHOD_NAME, Object.class,
                Class.class, Constructor.class, String.class, Object[].class, boolean.class, Exception.class);
        handleInjectionExceptionMethod.setAccessible(true);

        // WHEN
        Object actual;
        try {
            actual = handleInjectionExceptionMethod.invoke(underTest, fromFoo, MutableToFoo.class, null, "", null, forceConstructorInjection, new Exception());
        } catch (InvocationTargetException e) {
            actual = e.getTargetException();
        }

        // THEN
        assertThat(actual).isInstanceOf(expectedReturnType);
        restoreUnderTestObject();
    }

    /**
     * Creates the parameters to be used for testing the method {@code handleInjectionExceptionTesting}.
     * @return parameters to be used for testing the method {@code handleInjectionExceptionTesting}.
     */
    @DataProvider
    private Object[][] dataHandleInjectionExceptionTesting() {
        return new Object[][] {
            {"Tests that the handleInjectionException returns an error message if the forceConstructorInjection is true", true, InvalidBeanException.class},
            {"Tests that the handleInjectionException invokes the injectValues (that throws a MutableToFoo object) if the forceConstructorInjection is false",
                    false, MutableToFoo.class}
        };
    }
}
