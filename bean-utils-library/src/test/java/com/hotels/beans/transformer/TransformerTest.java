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

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;
import static java.util.Collections.emptyMap;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

import org.mockito.InjectMocks;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.hotels.beans.cache.CacheManager;
import com.hotels.beans.conversion.analyzer.ConversionAnalyzer;
import com.hotels.beans.error.InvalidBeanException;
import com.hotels.beans.error.MissingFieldException;
import com.hotels.beans.model.FieldMapping;
import com.hotels.beans.model.FieldTransformer;
import com.hotels.beans.sample.FromFooSimple;
import com.hotels.beans.sample.mutable.MutableToFooAdvFields;
import com.hotels.beans.utils.ClassUtils;
import com.hotels.beans.utils.ReflectionUtils;

/**
 * Unit test for class: {@link Transformer}.
 */
public class TransformerTest extends AbstractTransformerTest {
    private static final String SOURCE_FIELD_NAME = "sourceFieldName";
    private static final String SOURCE_FIELD_NAME_2 = "sourceFieldName2";
    private static final String TRANSFORMER_SETTINGS_FIELD_NAME = "settings";
    private static final String GET_SOURCE_FIELD_VALUE_METHOD_NAME = "getSourceFieldValue";
    private static final String GET_SOURCE_FIELD_TYPE_METHOD_NAME = "getSourceFieldType";
    private static final ReflectionUtils REFLECTION_UTILS = new ReflectionUtils();
    private static final String CACHE_MANAGER_FIELD_NAME = "cacheManager";
    private static final String REFLECTION_UTILS_FIELD_NAME = "reflectionUtils";
    private static final String CLASS_UTILS_FIELD_NAME = "classUtils";
    private static final String GET_TRANSFORMER_FUNCTION_METHOD_NAME = "getTransformerFunction";
    private static final String CONVERSION_ANALYZER_FIELD_NAME = "conversionAnalyzer";
    private static final String GET_CONSTRUCTOR_ARGS_VALUES_METHOD_NAME = "getConstructorArgsValues";

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
     * Test that is possible to remove a field mapping for a given field.
     */
    @Test
    public void testRemoveFieldMappingWorksProperly() {
        //GIVEN
        Transformer beanTransformer = underTest.withFieldMapping(new FieldMapping(SOURCE_FIELD_NAME, DEST_FIELD_NAME));

        //WHEN
        beanTransformer.removeFieldMapping(DEST_FIELD_NAME);
        TransformerSettings transformerSettings = (TransformerSettings) REFLECTION_UTILS.getFieldValue(beanTransformer, TRANSFORMER_SETTINGS_FIELD_NAME, TransformerSettings.class);

        //THEN
        assertFalse(transformerSettings.getFieldsNameMapping().containsKey(DEST_FIELD_NAME));
    }

    /**
     * Test that the method {@code removeFieldMapping} raises an {@link IllegalArgumentException} if the parameter is null.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testRemoveFieldMappingRaisesExceptionIfItsCalledWithNullParam() {
        //GIVEN
        Transformer beanTransformer = underTest.withFieldMapping(new FieldMapping(SOURCE_FIELD_NAME, DEST_FIELD_NAME));

        //WHEN
        beanTransformer.removeFieldMapping(null);
    }

    /**
     * Test that is possible to remove all the fields mappings defined.
     */
    @Test
    public void testResetFieldsMappingWorksProperly() {
        //GIVEN
        Transformer beanTransformer = underTest
                .withFieldMapping(new FieldMapping(SOURCE_FIELD_NAME, DEST_FIELD_NAME), new FieldMapping(SOURCE_FIELD_NAME_2, DEST_FIELD_NAME));

        //WHEN
        beanTransformer.resetFieldsMapping();
        TransformerSettings transformerSettings = (TransformerSettings) REFLECTION_UTILS.getFieldValue(beanTransformer, TRANSFORMER_SETTINGS_FIELD_NAME, TransformerSettings.class);

        //THEN
        assertTrue(transformerSettings.getFieldsNameMapping().isEmpty());
    }

    /**
     * Test that is possible to remove all the fields transformer defined.
     */
    @Test
    public void testResetFieldsTransformerWorksProperly() {
        //GIVEN
        Transformer beanTransformer = underTest
                .withFieldTransformer(new FieldTransformer<>(DEST_FIELD_NAME, val -> val), new FieldTransformer<>(REFLECTION_UTILS_FIELD_NAME, val -> val));

        //WHEN
        beanTransformer.resetFieldsTransformer();
        TransformerSettings transformerSettings = (TransformerSettings) REFLECTION_UTILS.getFieldValue(beanTransformer, TRANSFORMER_SETTINGS_FIELD_NAME, TransformerSettings.class);

        //THEN
        assertTrue(transformerSettings.getFieldsTransformers().isEmpty());
    }

    /**
     * Test that is possible to remove a field transformer for a given field.
     */
    @Test
    public void testRemoveFieldTransformerWorksProperly() {
        //GIVEN
        Transformer beanTransformer = underTest.withFieldTransformer(new FieldTransformer<>(DEST_FIELD_NAME, val -> val));

        //WHEN
        beanTransformer.removeFieldTransformer(DEST_FIELD_NAME);
        TransformerSettings transformerSettings = (TransformerSettings) REFLECTION_UTILS.getFieldValue(beanTransformer, TRANSFORMER_SETTINGS_FIELD_NAME, TransformerSettings.class);

        //THEN
        assertFalse(transformerSettings.getFieldsTransformers().containsKey(DEST_FIELD_NAME));
    }

    /**
     * Test that the method {@code removeFieldTransformer} raises an {@link IllegalArgumentException} if the parameter is null.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testRemoveFieldTransformerRaisesExceptionIfItsCalledWithNullParam() {
        //GIVEN
        Transformer beanTransformer = underTest.withFieldTransformer(new FieldTransformer<>(DEST_FIELD_NAME, val -> val));

        //WHEN
        beanTransformer.removeFieldTransformer(null);
    }

    /**
     * Test that the method: {@code getSourceFieldValue} raises a {@link NullPointerException} in case any of the parameters null.
     */
    @Test(expectedExceptions = Exception.class)
    public void testGetSourceFieldValueRaisesAnExceptionIfTheParameterAreNull() throws Exception {
        //GIVEN
        Method getSourceFieldValueMethod = underTest.getClass().getDeclaredMethod(GET_SOURCE_FIELD_VALUE_METHOD_NAME, Object.class, String.class, Field.class, boolean.class);
        getSourceFieldValueMethod.setAccessible(true);

        //WHEN
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
        //GIVEN
        ReflectionUtils reflectionUtils = mock(ReflectionUtils.class);
        Field field = mock(Field.class);
        Class fieldType = Integer.class;

        when(reflectionUtils.getFieldValue(FromFooSimple.class, AGE_FIELD_NAME, fieldType)).thenThrow(InvalidBeanException.class);
        when(field.getType()).thenReturn(fieldType);

        setField(underTest, REFLECTION_UTILS_FIELD_NAME, reflectionUtils);

        Method getSourceFieldValueMethod = underTest.getClass().getDeclaredMethod(GET_SOURCE_FIELD_VALUE_METHOD_NAME, Object.class, String.class, Field.class, boolean.class);
        getSourceFieldValueMethod.setAccessible(true);

        //WHEN
        Exception raisedException = null;
        Object actual = null;
        try {
            actual = getSourceFieldValueMethod.invoke(underTest, FromFooSimple.class, AGE_FIELD_NAME, field, true);
        } catch (final Exception e) {
            raisedException = e;
        }

        //THEN
        verify(reflectionUtils).getFieldValue(FromFooSimple.class, AGE_FIELD_NAME, fieldType);
        verify(field).getType();
        assertNull(raisedException);
        assertNull(actual);
        restoreUnderTestObject();
    }

    /**
     * Test that by default the primitive type conversion is disabled.
     */
    @Test
    public void testThatDefaultPrimitiveTypeConversionIsDisabledByDefault() {
        //GIVEN

        //WHEN
        boolean actual = underTest.settings.isDefaultPrimitiveTypeConversionEnabled();

        //THEN
        assertFalse(actual);
    }

    /**
     * Test that the primitive type conversion is correctly enabled.
     */
    @Test
    public void testThatDefaultPrimitiveTypeConversionIsCorrectlyEnabled() {
        //GIVEN
        underTest.setDefaultPrimitiveTypeConversionEnabled(true);

        //WHEN
        boolean actual = underTest.settings.isDefaultPrimitiveTypeConversionEnabled();

        //THEN
        assertTrue(actual);
        underTest.setDefaultPrimitiveTypeConversionEnabled(false);
    }

    /**
     * Test that the method: {@code getSourceFieldType} returns the source object class in case the given field does not
     * exists in the given class and the source object class is primitive.
     * @throws Exception if the invoke method fails
     */
    @Test
    public void testGetSourceFieldTypeReturnsTheSourceObjectClass() throws Exception {
        //GIVEN
        CacheManager cacheManager = mock(CacheManager.class);
        ReflectionUtils reflectionUtils = mock(ReflectionUtils.class);
        ClassUtils classUtils = mock(ClassUtils.class);

        when(cacheManager.getFromCache(anyString(), any(Class.class))).thenReturn(empty());
        when(reflectionUtils.getDeclaredFieldType(AGE_FIELD_NAME, Integer.class)).thenThrow(MissingFieldException.class);
        when(classUtils.isPrimitiveType(Integer.class)).thenReturn(true);

        setField(underTest, CACHE_MANAGER_FIELD_NAME, cacheManager);
        setField(underTest, REFLECTION_UTILS_FIELD_NAME, reflectionUtils);
        setField(underTest, CLASS_UTILS_FIELD_NAME, classUtils);

        Method getSourceFieldTypeMethod = underTest.getClass().getDeclaredMethod(GET_SOURCE_FIELD_TYPE_METHOD_NAME, Class.class, String.class);
        getSourceFieldTypeMethod.setAccessible(true);

        //WHEN
        Class<?> actual = (Class<?>) getSourceFieldTypeMethod.invoke(underTest, Integer.class, AGE_FIELD_NAME);

        //THEN
        verify(cacheManager).getFromCache(anyString(), any(Class.class));
        verify(reflectionUtils).getDeclaredFieldType(AGE_FIELD_NAME, Integer.class);
        verify(classUtils).isPrimitiveType(Integer.class);
        assertEquals(Integer.class, actual);
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
        //GIVEN
        CacheManager cacheManager = mock(CacheManager.class);
        ReflectionUtils reflectionUtils = mock(ReflectionUtils.class);
        ClassUtils classUtils = mock(ClassUtils.class);
        TransformerSettings settings = mock(TransformerSettings.class);

        when(cacheManager.getFromCache(anyString(), any(Class.class))).thenReturn(empty());
        when(reflectionUtils.getDeclaredFieldType(AGE_FIELD_NAME, FromFooSimple.class)).thenThrow(MissingFieldException.class);
        when(classUtils.isPrimitiveType(Integer.class)).thenReturn(true);
        when(settings.isSetDefaultValueForMissingField()).thenReturn(true);

        setField(underTest, CACHE_MANAGER_FIELD_NAME, cacheManager);
        setField(underTest, REFLECTION_UTILS_FIELD_NAME, reflectionUtils);
        setField(underTest, CLASS_UTILS_FIELD_NAME, classUtils);
        setField(underTest, TRANSFORMER_SETTINGS_FIELD_NAME, settings);

        Method getSourceFieldTypeMethod = underTest.getClass().getDeclaredMethod(GET_SOURCE_FIELD_TYPE_METHOD_NAME, Class.class, String.class);
        getSourceFieldTypeMethod.setAccessible(true);

        //WHEN
        Class<?> actual = (Class<?>) getSourceFieldTypeMethod.invoke(underTest, FromFooSimple.class, AGE_FIELD_NAME);

        //THEN
        verify(cacheManager).getFromCache(anyString(), any(Class.class));
        verify(reflectionUtils).getDeclaredFieldType(AGE_FIELD_NAME, FromFooSimple.class);
        verify(classUtils).isPrimitiveType(FromFooSimple.class);
        verify(settings).isSetDefaultValueForMissingField();
        assertNull(actual);
        restoreUnderTestObject();
    }

    /**
     * Test that the method: {@code getSourceFieldType} throws {@link MissingFieldException} in case the given field does not exists
     * in the given class and the source object class is not primitive and the default value set for missing fields feature is disabled.
     * @throws Exception if the invoke method fails
     */
    @Test
    public void testGetSourceFieldTypeThrowsMissingFieldException() throws Exception {
        //GIVEN
        CacheManager cacheManager = mock(CacheManager.class);
        ReflectionUtils reflectionUtils = mock(ReflectionUtils.class);
        ClassUtils classUtils = mock(ClassUtils.class);
        TransformerSettings settings = mock(TransformerSettings.class);

        when(cacheManager.getFromCache(anyString(), any(Class.class))).thenReturn(empty());
        when(reflectionUtils.getDeclaredFieldType(AGE_FIELD_NAME, FromFooSimple.class)).thenThrow(MissingFieldException.class);
        when(classUtils.isPrimitiveType(Integer.class)).thenReturn(false);
        when(settings.isSetDefaultValueForMissingField()).thenReturn(false);

        setField(underTest, CACHE_MANAGER_FIELD_NAME, cacheManager);
        setField(underTest, REFLECTION_UTILS_FIELD_NAME, reflectionUtils);
        setField(underTest, CLASS_UTILS_FIELD_NAME, classUtils);
        setField(underTest, TRANSFORMER_SETTINGS_FIELD_NAME, settings);

        Method getSourceFieldTypeMethod = underTest.getClass().getDeclaredMethod(GET_SOURCE_FIELD_TYPE_METHOD_NAME, Class.class, String.class);
        getSourceFieldTypeMethod.setAccessible(true);

        //WHEN
        Exception raisedException = null;
        try {
            getSourceFieldTypeMethod.invoke(underTest, FromFooSimple.class, AGE_FIELD_NAME);
        } catch (final Exception e) {
            raisedException = e;
        }

        //THEN
        verify(cacheManager).getFromCache(anyString(), any(Class.class));
        verify(reflectionUtils).getDeclaredFieldType(AGE_FIELD_NAME, FromFooSimple.class);
        verify(classUtils).isPrimitiveType(FromFooSimple.class);
        verify(settings).isSetDefaultValueForMissingField();
        assertNotNull(raisedException);
        assertEquals(MissingFieldException.class, raisedException.getCause().getClass());
        restoreUnderTestObject();
    }

    /**
     * Test that the {@code getTransformerFunction} works as expected.
     * @param testCaseDescription the test case description
     * @param fieldName the field name to which the transformer function has to be applied
     * @param isDefaultPrimitiveTypeConversionEnabled indicates if the automatic conversion function is enabled
     * @param isDestinationFieldPrimitiveType indicates if the destination field type is primitive or not
     * @param fieldTransformers the field transformer associated to the given field
     * @param sourceFieldType the source field type
     * @param expectedFieldTransformerSize the total number of transformation function expected for the given field
     * @throws Exception if the method invocation fails
     */
    @Test(dataProvider = "dataGetTransformerFunctionTesting")
    @SuppressWarnings("unchecked")
    public void testGetTransformerFunctionWorksProperly(final String testCaseDescription, final String fieldName, final boolean isDefaultPrimitiveTypeConversionEnabled,
        final boolean isDestinationFieldPrimitiveType, final Map<String, FieldTransformer> fieldTransformers, final Class sourceFieldType, final int expectedFieldTransformerSize)
        throws Exception {
        //GIVEN
        Field field = mock(Field.class);
        CacheManager cacheManager = mock(CacheManager.class);
        TransformerSettings settings = mock(TransformerSettings.class);
        ConversionAnalyzer conversionAnalyzer = mock(ConversionAnalyzer.class);
        ReflectionUtils reflectionUtils = mock(ReflectionUtils.class);

        when(cacheManager.getFromCache(anyString(), any(Class.class))).thenReturn(ofNullable(sourceFieldType));
        when(settings.isFlatFieldNameTransformation()).thenReturn(false);
        when(settings.isDefaultPrimitiveTypeConversionEnabled()).thenReturn(isDefaultPrimitiveTypeConversionEnabled);
        when(settings.getFieldsTransformers()).thenReturn(fieldTransformers);
        when(conversionAnalyzer.getConversionFunction(any(Class.class), any(Class.class))).thenReturn(of(Object::toString));
        when(field.getType()).thenReturn(sourceFieldType);
        when(reflectionUtils.getDeclaredFieldType(fieldName, FromFooSimple.class)).thenReturn(sourceFieldType);

        setField(underTest, CACHE_MANAGER_FIELD_NAME, cacheManager);
        setField(underTest, TRANSFORMER_SETTINGS_FIELD_NAME, settings);
        setField(underTest, CONVERSION_ANALYZER_FIELD_NAME, conversionAnalyzer);
        setField(underTest, REFLECTION_UTILS_FIELD_NAME, reflectionUtils);

        Method getTransformerFunctionMethod = underTest.getClass()
                .getDeclaredMethod(GET_TRANSFORMER_FUNCTION_METHOD_NAME, Class.class, String.class, Field.class, boolean.class, String.class);
        getTransformerFunctionMethod.setAccessible(true);

        //WHEN
        List<FieldTransformer> actual =
                (List<FieldTransformer>) getTransformerFunctionMethod.invoke(underTest, FromFooSimple.class, fieldName, field, isDestinationFieldPrimitiveType, fieldName);

        //THEN
        assertEquals(expectedFieldTransformerSize, actual.size());
        restoreUnderTestObject();
    }

    /**
     * Creates the parameters to be used for testing method {@code getTransformerFunction}.
     * @return parameters to be used for testing method {@code getTransformerFunction}.
     */
    @DataProvider
    private Object[][] dataGetTransformerFunctionTesting() {
        return new Object[][] {
                {"Test that a type conversion function is added to the existing one if all the conditions are met",
                        AGE_FIELD_NAME, true, true, emptyMap(), int.class, ONE.intValue()},
                {"Test that no type conversion function is added to the existing one if the source type field is null",
                        AGE_FIELD_NAME, true, true, emptyMap(), null, ZERO.intValue()},
                {"Test that no conversion function is added to the existing one if the primitive type conversion is disabled",
                        AGE_FIELD_NAME, false, true, emptyMap(), null, ZERO.intValue()},
                {"Test that no conversion function is added to the existing one if the destination field type is not primitive",
                        AGE_FIELD_NAME, true, false, emptyMap(), null, ZERO.intValue()},
                {"Test that no conversion function is added to the existing one if there is a field transformer function defined for the existing field",
                        AGE_FIELD_NAME, true, true, Map.of(AGE_FIELD_NAME, new FieldTransformer<>(AGE_FIELD_NAME, () -> null)), null, ONE.intValue()}
        };
    }

    /**
     * Test that the method: {@code getConstructorArgsValues} returns the default type value if the parameter name cannot be retrieved from the constructor.
     * @throws Exception if the invoke method fails
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testGetConstructorArgsValuesReturnsTheDefaultTypeIfTheDestinationFieldNameIsNull() throws Exception {
        //GIVEN
        Constructor constructor = mock(Constructor.class);
        ClassUtils classUtils = mock(ClassUtils.class);
        Parameter constructorParameter = mock(Parameter.class);
        Parameter[] constructorParameters = {constructorParameter};

        when(classUtils.getConstructorParameters(constructor)).thenReturn(constructorParameters);
        when(constructorParameter.isNamePresent()).thenReturn(true);
        when(constructorParameter.getName()).thenReturn(null);
        when(constructorParameter.getType()).thenReturn((Class) Integer.class);
        when(classUtils.getDefaultTypeValue(Integer.class)).thenReturn(ZERO.intValue());

        setField(underTest, CLASS_UTILS_FIELD_NAME, classUtils);

        Method getConstructorArgsValuesMethod = underTest.getClass()
                .getDeclaredMethod(GET_CONSTRUCTOR_ARGS_VALUES_METHOD_NAME, Object.class, Class.class, Constructor.class, String.class);
        getConstructorArgsValuesMethod.setAccessible(true);

        //WHEN
        Object[] actual = (Object[]) getConstructorArgsValuesMethod.invoke(underTest, fromFoo, MutableToFooAdvFields.class, constructor, ID_FIELD_NAME);

        //THEN
        verify(classUtils).getConstructorParameters(constructor);
        verify(constructorParameter).isNamePresent();
        verify(constructorParameter, times(2)).getName();
        verify(constructorParameter).getType();
        verify(classUtils).getDefaultTypeValue(Integer.class);
        assertEquals(ONE.intValue(), actual.length);
        assertEquals(ZERO.intValue(), actual[ZERO.intValue()]);
        restoreUnderTestObject();
    }

    /**
     * Restores the underTest object removing all defined mocks.
     */
    private void restoreUnderTestObject() {
        underTest = new TransformerImpl();
    }

    /**
     * Tests that an instance fo {@link com.hotels.beans.validator.Validator} is created only if the validation is enabled.
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
        assertEquals(expectedNull, underTest.validator == null);
        underTest.setValidationEnabled(false);
    }

    /**
     * Creates the parameters to be used for testing the method {@code setValidationEnabled}.
     * @return parameters to be used for testing the the method {@code setValidationEnabled}.
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
        underTest.setDefaultPrimitiveTypeConversionEnabled(autoConversionEnabled);

        // THEN
        assertEquals(expectedNull, underTest.conversionAnalyzer == null);
        underTest.setDefaultPrimitiveTypeConversionEnabled(false);
    }

    /**
     * Creates the parameters to be used for testing the method {@code setValidationEnabled}.
     * @return parameters to be used for testing the the method {@code setValidationEnabled}.
     */
    @DataProvider
    private Object[][] dataConversionAnalyzerInitializationTesting() {
        return new Object[][] {
                {"Tests that the ConversionAnalyzer object is not created if the automatic conversion is disabled", false, true},
                {"Tests that the ConversionAnalyzer object is created if the automatic conversion is enabled", true, false}
        };
    }
}
