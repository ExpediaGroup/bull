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
package com.expediagroup.beans.transformer

import com.expediagroup.beans.sample.FromFooSimple
import com.expediagroup.beans.sample.mutable.MutableToFoo
import com.expediagroup.beans.sample.mutable.MutableToFooAdvFields
import com.expediagroup.transformer.cache.CacheManager
import com.expediagroup.transformer.error.InvalidBeanException
import com.expediagroup.transformer.error.MissingFieldException
import com.expediagroup.transformer.model.FieldMapping
import com.expediagroup.transformer.model.FieldTransformer
import com.expediagroup.transformer.model.TransformerSettings
import com.expediagroup.transformer.utils.ClassUtils
import com.expediagroup.transformer.utils.ReflectionUtils
import org.assertj.core.api.Assertions
import org.assertj.core.api.ThrowableAssert.ThrowingCallable
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Parameter
import java.math.BigInteger
import java.util.*

/**
 * Unit test for class: [BeanTransformer].
 */
class BeanTransformerTest : AbstractBeanTransformerTest() {
    /**
     * Test that is possible to remove a field mapping for a given field.
     */
    @Test
    fun testRemoveFieldMappingWorksProperly() {
        // GIVEN
        val beanTransformer = underTest.withFieldMapping(FieldMapping(SOURCE_FIELD_NAME, DEST_FIELD_NAME))

        // WHEN
        beanTransformer.removeFieldMapping(DEST_FIELD_NAME)
        val transformerSettings = REFLECTION_UTILS.getFieldValue(
            beanTransformer,
            TRANSFORMER_SETTINGS_FIELD_NAME,
            TransformerSettings::class.java
        ) as TransformerSettings<*>

        // THEN
        Assertions.assertThat(transformerSettings.fieldsNameMapping[DEST_FIELD_NAME]).isNull()
    }

    /**
     * Test that the method `removeFieldMapping` raises an [IllegalArgumentException] if the parameter is null.
     */
    @Test(expectedExceptions = [IllegalArgumentException::class])
    fun testRemoveFieldMappingRaisesExceptionIfItsCalledWithNullParam() {
        // GIVEN
        val beanTransformer = underTest.withFieldMapping(FieldMapping(SOURCE_FIELD_NAME, DEST_FIELD_NAME))

        // WHEN
        beanTransformer.removeFieldMapping(null)
    }

    /**
     * Test that is possible to remove all the fields mappings defined.
     */
    @Test
    fun testResetFieldsMappingWorksProperly() {
        // GIVEN
        val beanTransformer = underTest
            .withFieldMapping(
                FieldMapping(SOURCE_FIELD_NAME, DEST_FIELD_NAME),
                FieldMapping(SOURCE_FIELD_NAME_2, DEST_FIELD_NAME)
            )

        // WHEN
        beanTransformer.resetFieldsMapping()
        val transformerSettings = REFLECTION_UTILS.getFieldValue(
            beanTransformer,
            TRANSFORMER_SETTINGS_FIELD_NAME,
            TransformerSettings::class.java
        ) as TransformerSettings<*>

        // THEN
        Assertions.assertThat(transformerSettings.fieldsNameMapping).isEmpty()
    }

    /**
     * Test that is possible to remove all the fields transformer defined.
     */
    @Test
    fun testResetFieldsTransformerWorksProperly() {
        // GIVEN
        val beanTransformer = underTest
            .withFieldTransformer(
                FieldTransformer(DEST_FIELD_NAME) { `val`: Any? -> `val` },
                FieldTransformer(
                    REFLECTION_UTILS_FIELD_NAME
                ) { `val`: Any? -> `val` }
            )

        // WHEN
        beanTransformer.resetFieldsTransformer()
        val transformerSettings = REFLECTION_UTILS.getFieldValue(
            beanTransformer,
            TRANSFORMER_SETTINGS_FIELD_NAME,
            TransformerSettings::class.java
        ) as TransformerSettings<*>

        // THEN
        Assertions.assertThat(transformerSettings.fieldsTransformers).isEmpty()
    }

    /**
     * Test that is possible to remove a field transformer for a given field.
     */
    @Test
    fun testRemoveFieldTransformerWorksProperly() {
        // GIVEN
        val beanTransformer =
            underTest.withFieldTransformer(FieldTransformer(DEST_FIELD_NAME) { `val`: Any? -> `val` })

        // WHEN
        beanTransformer.removeFieldTransformer(DEST_FIELD_NAME)
        val transformerSettings = REFLECTION_UTILS.getFieldValue(
            beanTransformer,
            TRANSFORMER_SETTINGS_FIELD_NAME,
            TransformerSettings::class.java
        ) as TransformerSettings<*>

        // THEN
        Assertions.assertThat(transformerSettings.fieldsTransformers[DEST_FIELD_NAME]).isNull()
    }

    /**
     * Test that the method `removeFieldTransformer` raises an [IllegalArgumentException] if the parameter is null.
     */
    @Test(expectedExceptions = [IllegalArgumentException::class])
    fun testRemoveFieldTransformerRaisesExceptionIfItsCalledWithNullParam() {
        // GIVEN
        val beanTransformer =
            underTest.withFieldTransformer(FieldTransformer(DEST_FIELD_NAME) { `val`: Any? -> `val` })

        // WHEN
        beanTransformer.removeFieldTransformer(null)
    }

    /**
     * Test that is possible to remove all the transformer settings.
     */
    @Test
    fun testResetWorksProperly() {
        // GIVEN
        val beanTransformer = underTest
            .withFieldTransformer(
                FieldTransformer(DEST_FIELD_NAME) { `val`: Any? -> `val` },
                FieldTransformer(
                    REFLECTION_UTILS_FIELD_NAME
                ) { `val`: Any? -> `val` }
            )
            .withFieldMapping(FieldMapping(SOURCE_FIELD_NAME, DEST_FIELD_NAME))
            .skipTransformationForField(NAME_FIELD_NAME)
            .setDefaultValueForMissingField(true)

        // WHEN
        beanTransformer.reset()
        val transformerSettings = REFLECTION_UTILS.getFieldValue(
            beanTransformer,
            TRANSFORMER_SETTINGS_FIELD_NAME,
            TransformerSettings::class.java
        ) as TransformerSettings<String>

        // THEN
        Assertions.assertThat(transformerSettings.fieldsTransformers).isEmpty()
        Assertions.assertThat(transformerSettings.fieldsNameMapping).isEmpty()
        Assertions.assertThat(transformerSettings.fieldsToSkip).isEmpty()
        Assertions.assertThat(transformerSettings.isSetDefaultValueForMissingField).isFalse
    }

    /**
     * Test that the method: `getSourceFieldValue` raises a [NullPointerException] in case any of the parameters null.
     * @throws Exception uf the invocation fails
     */
    @Test(expectedExceptions = [Exception::class])
    @Throws(Exception::class)
    fun testGetSourceFieldValueRaisesAnExceptionIfTheParameterAreNull() {
        // GIVEN
        val getSourceFieldValueMethod = underTest.javaClass.getDeclaredMethod(
            GET_SOURCE_FIELD_VALUE_METHOD_NAME,
            Any::class.java,
            String::class.java,
            Field::class.java,
            Boolean::class.javaPrimitiveType
        )
        getSourceFieldValueMethod.isAccessible = true

        // WHEN
        getSourceFieldValueMethod.invoke(underTest, null, null, null, false)
    }

    /**
     * Test that the method: `getSourceFieldValue` throws no Exceptions in case reflectionUtils throws exception and
     * a field transformer is defined for the given field.
     * @throws Exception if the invoke method fails
     */
    @Test
    @Throws(Exception::class)
    fun testGetSourceFieldValueThrowsNoExceptionIfAFieldTransformerIsDefined() {
        // GIVEN
        val reflectionUtilsMock = Mockito.mock(ReflectionUtils::class.java)
        val field = Mockito.mock(Field::class.java)
        val fieldType: Class<*> = Int::class.java
        Mockito.`when`(reflectionUtilsMock.getFieldValue(FromFooSimple::class.java, AGE_FIELD_NAME, fieldType))
            .thenThrow(
                InvalidBeanException::class.java
            )
        Mockito.`when`(field.type).thenReturn(fieldType)
        reflectionUtils.setFieldValue(underTest, REFLECTION_UTILS_FIELD_NAME, reflectionUtilsMock)
        val getSourceFieldValueMethod = underTest.javaClass.getDeclaredMethod(
            GET_SOURCE_FIELD_VALUE_METHOD_NAME,
            Any::class.java,
            String::class.java,
            Field::class.java,
            Boolean::class.javaPrimitiveType
        )
        getSourceFieldValueMethod.isAccessible = true

        // WHEN
        val actual = ThrowingCallable {
            getSourceFieldValueMethod.invoke(
                underTest,
                FromFooSimple::class.java,
                AGE_FIELD_NAME,
                field,
                true
            )
        }

        // THEN
        Assertions.assertThatCode(actual).doesNotThrowAnyException()
        Mockito.verify(reflectionUtilsMock).getFieldValue(FromFooSimple::class.java, AGE_FIELD_NAME, fieldType)
        Mockito.verify(field).type
        restoreUnderTestObject()
    }

    /**
     * Test that the primitive type conversion is correctly enabled.
     */
    @Test
    fun testThatPrimitiveTypeConversionIsCorrectlyEnabled() {
        // GIVEN
        underTest.setPrimitiveTypeConversionEnabled(true)

        // WHEN
        val actual = underTest.settings.isPrimitiveTypeConversionEnabled

        // THEN
        Assertions.assertThat(actual).isTrue
        underTest.setPrimitiveTypeConversionEnabled(false)
    }

    /**
     * Test that the method: `getSourceFieldType` returns the source object class in case the given field does not
     * exists in the given class and the source object class is primitive.
     * @throws Exception if the invoke method fails
     */
    @Test
    @Throws(Exception::class)
    fun testGetSourceFieldTypeReturnsTheSourceObjectClass() {
        // GIVEN
        val cacheManager = Mockito.mock(CacheManager::class.java)
        val reflectionUtilsMock = Mockito.mock(ReflectionUtils::class.java)
        val classUtils = Mockito.mock(
            ClassUtils::class.java
        )
        Mockito.`when`<Optional<*>>(
            cacheManager.getFromCache(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(
                    Class::class.java
                )
            )
        ).thenReturn(Optional.empty<Any>())
        Mockito.`when`(reflectionUtilsMock.getDeclaredFieldType(AGE_FIELD_NAME, Int::class.java)).thenThrow(
            MissingFieldException::class.java
        )
        Mockito.`when`(classUtils.isPrimitiveType(Int::class.java)).thenReturn(true)
        reflectionUtils.setFieldValue(underTest, CACHE_MANAGER_FIELD_NAME, cacheManager)
        reflectionUtils.setFieldValue(underTest, REFLECTION_UTILS_FIELD_NAME, reflectionUtilsMock)
        reflectionUtils.setFieldValue(underTest, CLASS_UTILS_FIELD_NAME, classUtils)
        val getSourceFieldTypeMethod = underTest.javaClass.getDeclaredMethod(
            GET_SOURCE_FIELD_TYPE_METHOD_NAME,
            Class::class.java,
            String::class.java
        )
        getSourceFieldTypeMethod.isAccessible = true

        // WHEN
        val actual = getSourceFieldTypeMethod.invoke(underTest, Int::class.java, AGE_FIELD_NAME) as Class<*>

        // THEN
        Mockito.verify(cacheManager).getFromCache(
            ArgumentMatchers.anyString(),
            ArgumentMatchers.any(
                Class::class.java
            )
        )
        Mockito.verify(reflectionUtilsMock).getDeclaredFieldType(AGE_FIELD_NAME, Int::class.java)
        Mockito.verify(classUtils).isPrimitiveType(Int::class.java)
        Assertions.assertThat(actual).isEqualTo(Int::class.java)
        restoreUnderTestObject()
    }

    /**
     * Test that the method: `getSourceFieldType` returns nulls in case the given field does not
     * exists in the given class, the source object class is not primitive and the default value set for missing
     * fields feature is enabled.
     * @throws Exception if the invoke method fails
     */
    @Test
    @Throws(Exception::class)
    fun testGetSourceFieldTypeReturnsNull() {
        // GIVEN
        val cacheManager = Mockito.mock(CacheManager::class.java)
        val reflectionUtilsMock = Mockito.mock(ReflectionUtils::class.java)
        val classUtils = Mockito.mock(
            ClassUtils::class.java
        )
        val settings = Mockito.mock(TransformerSettings::class.java)
        Mockito.`when`<Optional<*>>(
            cacheManager.getFromCache(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(
                    Class::class.java
                )
            )
        ).thenReturn(Optional.empty<Any>())
        Mockito.`when`(reflectionUtilsMock.getDeclaredFieldType(AGE_FIELD_NAME, FromFooSimple::class.java)).thenThrow(
            MissingFieldException::class.java
        )
        Mockito.`when`(classUtils.isPrimitiveType(Int::class.java)).thenReturn(true)
        Mockito.`when`(settings.isSetDefaultValueForMissingField).thenReturn(true)
        reflectionUtils.setFieldValue(underTest, CACHE_MANAGER_FIELD_NAME, cacheManager)
        reflectionUtils.setFieldValue(underTest, REFLECTION_UTILS_FIELD_NAME, reflectionUtilsMock)
        reflectionUtils.setFieldValue(underTest, CLASS_UTILS_FIELD_NAME, classUtils)
        reflectionUtils.setFieldValue(underTest, TRANSFORMER_SETTINGS_FIELD_NAME, settings)
        val getSourceFieldTypeMethod = underTest.javaClass.getDeclaredMethod(
            GET_SOURCE_FIELD_TYPE_METHOD_NAME,
            Class::class.java,
            String::class.java
        )
        getSourceFieldTypeMethod.isAccessible = true

        // WHEN
        val actual = getSourceFieldTypeMethod.invoke(underTest, FromFooSimple::class.java, AGE_FIELD_NAME) as Class<*>

        // THEN
        Mockito.verify(cacheManager).getFromCache(
            ArgumentMatchers.anyString(),
            ArgumentMatchers.any(
                Class::class.java
            )
        )
        Mockito.verify(reflectionUtilsMock).getDeclaredFieldType(AGE_FIELD_NAME, FromFooSimple::class.java)
        Mockito.verify(classUtils).isPrimitiveType(FromFooSimple::class.java)
        Mockito.verify(settings).isSetDefaultValueForMissingField
        Assertions.assertThat(actual).isNull()
        restoreUnderTestObject()
    }

    /**
     * Test that the method: `getSourceFieldType` throws [MissingFieldException] in case the given field does not exists
     * in the given class and the source object class is not primitive and the default value set for missing fields feature is disabled.
     * @throws Exception if the invoke method fails
     */
    @Test
    @Throws(Exception::class)
    fun testGetSourceFieldTypeThrowsMissingFieldException() {
        // GIVEN
        val cacheManager = Mockito.mock(CacheManager::class.java)
        val reflectionUtilsMock = Mockito.mock(ReflectionUtils::class.java)
        val classUtils = Mockito.mock(
            ClassUtils::class.java
        )
        val settings = Mockito.mock(TransformerSettings::class.java)
        Mockito.`when`<Optional<*>>(
            cacheManager.getFromCache(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(
                    Class::class.java
                )
            )
        ).thenReturn(Optional.empty<Any>())
        Mockito.`when`(reflectionUtilsMock.getDeclaredFieldType(AGE_FIELD_NAME, FromFooSimple::class.java)).thenThrow(
            MissingFieldException::class.java
        )
        Mockito.`when`(classUtils.isPrimitiveType(Int::class.java)).thenReturn(false)
        Mockito.`when`(settings.isSetDefaultValueForMissingField).thenReturn(false)
        reflectionUtils.setFieldValue(underTest, CACHE_MANAGER_FIELD_NAME, cacheManager)
        reflectionUtils.setFieldValue(underTest, REFLECTION_UTILS_FIELD_NAME, reflectionUtilsMock)
        reflectionUtils.setFieldValue(underTest, CLASS_UTILS_FIELD_NAME, classUtils)
        reflectionUtils.setFieldValue(underTest, TRANSFORMER_SETTINGS_FIELD_NAME, settings)
        val getSourceFieldTypeMethod = underTest.javaClass.getDeclaredMethod(
            GET_SOURCE_FIELD_TYPE_METHOD_NAME,
            Class::class.java,
            String::class.java
        )
        getSourceFieldTypeMethod.isAccessible = true

        // WHEN
        val actual =
            ThrowingCallable { getSourceFieldTypeMethod.invoke(underTest, FromFooSimple::class.java, AGE_FIELD_NAME) }

        // THEN
        Assertions.assertThatThrownBy(actual).hasCauseInstanceOf(MissingFieldException::class.java)
        Mockito.verify(cacheManager).getFromCache(
            ArgumentMatchers.anyString(),
            ArgumentMatchers.any(
                Class::class.java
            )
        )
        Mockito.verify(reflectionUtilsMock).getDeclaredFieldType(AGE_FIELD_NAME, FromFooSimple::class.java)
        Mockito.verify(classUtils).isPrimitiveType(FromFooSimple::class.java)
        Mockito.verify(settings).isSetDefaultValueForMissingField
        restoreUnderTestObject()
    }

    /**
     * Test that the `getTransformedValue` works as expected.
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
    @Throws(Exception::class)
    fun testGetTransformedValueWorksProperly(
        testCaseDescription: String?,
        fieldName: String?,
        fieldValue: Any?,
        fieldTransformer: FieldTransformer<*, *>?,
        isPrimitiveTypeConversionEnabled: Boolean,
        isDestinationFieldPrimitiveType: Boolean,
        expectedValue: Any?
    ) {
        // GIVEN
        val field = Mockito.mock(Field::class.java)
        val settings = Mockito.mock(TransformerSettings::class.java)
        Mockito.`when`(settings.isPrimitiveTypeConversionEnabled).thenReturn(isPrimitiveTypeConversionEnabled)
        reflectionUtils.setFieldValue(underTest, TRANSFORMER_SETTINGS_FIELD_NAME, settings)
        val transformedValueMethodParams = arrayOf(
            FieldTransformer::class.java,
            Any::class.java,
            Class::class.java,
            String::class.java,
            Field::class.java,
            Boolean::class.javaPrimitiveType,
            String::class.java
        )
        val getTransformerFunctionMethod =
            underTest.javaClass.getDeclaredMethod(GET_TRANSFORMER_VALUE_METHOD_NAME, *transformedValueMethodParams)
        getTransformerFunctionMethod.isAccessible = true

        // WHEN
        val actual = getTransformerFunctionMethod
            .invoke(
                underTest,
                fieldTransformer,
                fieldValue,
                FromFooSimple::class.java,
                fieldName,
                field,
                isDestinationFieldPrimitiveType,
                fieldName
            )

        // THEN
        Assertions.assertThat(actual).isEqualTo(expectedValue)
        restoreUnderTestObject()
    }

    /**
     * Creates the parameters to be used for testing method `getTransformedValue`.
     * @return parameters to be used for testing method `getTransformedValue`.
     */
    @DataProvider
    private fun dataGetTransformerFunctionTesting(): Array<Array<Any?>> {
        return arrayOf(
            arrayOf(
                "Test that the primitive type conversion function is not executed if the primitive type conversion is disabled",
                AGE_FIELD_NAME,
                BigInteger.ZERO.toInt(),
                null,
                false,
                true,
                BigInteger.ZERO.toInt()
            ),
            arrayOf(
                "Test that the primitive type conversion function is not executed if the destination field type is not primitive",
                AGE_FIELD_NAME,
                null,
                null,
                true,
                false,
                null
            )
        )
    }

    /**
     * Test that the method: `getConstructorArgsValues` returns the default type value if the parameter name cannot be retrieved from the constructor.
     * @throws Exception if the invoke method fails
     */
    @Test
    @Throws(Exception::class)
    fun testGetConstructorArgsValuesReturnsTheDefaultTypeIfTheDestinationFieldNameIsNull() {
        // GIVEN
        val constructor = Mockito.mock(Constructor::class.java)
        val classUtils = Mockito.mock(
            ClassUtils::class.java
        )
        val constructorParameter = Mockito.mock(Parameter::class.java)
        val constructorParameters = arrayOf(constructorParameter)
        Mockito.`when`(classUtils.getConstructorParameters(constructor)).thenReturn(constructorParameters)
        Mockito.`when`(constructorParameter.isNamePresent).thenReturn(true)
        Mockito.`when`(constructorParameter.name).thenReturn(null)
        Mockito.`when`(constructorParameter.type).thenReturn(Int::class.java as Class<*>)
        Mockito.`when`(classUtils.getDefaultTypeValue(Int::class.java)).thenReturn(BigInteger.ZERO.toInt())
        reflectionUtils.setFieldValue(underTest, CLASS_UTILS_FIELD_NAME, classUtils)
        val getConstructorArgsValuesMethod = underTest.javaClass
            .getDeclaredMethod(
                GET_CONSTRUCTOR_ARGS_VALUES_METHOD_NAME,
                Any::class.java,
                Class::class.java,
                Constructor::class.java,
                String::class.java
            )
        getConstructorArgsValuesMethod.isAccessible = true

        // WHEN
        val actual = getConstructorArgsValuesMethod.invoke(
            underTest,
            fromFoo,
            MutableToFooAdvFields::class.java,
            constructor,
            ID_FIELD_NAME
        ) as Array<Any>

        // THEN
        Mockito.verify(classUtils).getConstructorParameters(constructor)
        Mockito.verify(constructorParameter).isNamePresent
        Mockito.verify(constructorParameter, Mockito.times(2)).name
        Mockito.verify(constructorParameter).type
        Mockito.verify(classUtils).getDefaultTypeValue(Int::class.java)
        Assertions.assertThat(actual).containsOnly(0)
        restoreUnderTestObject()
    }

    /**
     * Restores the underTest object removing all defined mocks.
     */
    private fun restoreUnderTestObject() {
        underTest = TransformerImpl()
    }

    /**
     * Tests that an instance fo [Validator] is created only if the validation is enabled.
     * @param testCaseDescription the test case description
     * @param validationEnabled true if the validation is enabled, false otherwise
     * @param expectedNull the expected result
     */
    @Test(dataProvider = "dataValidationInitializationTesting")
    fun testValidatorIsInitializedOnlyIfValidationIsEnabled(
        testCaseDescription: String?,
        validationEnabled: Boolean,
        expectedNull: Boolean
    ) {
        // GIVEN

        // WHEN
        underTest.setValidationEnabled(validationEnabled)

        // THEN
        Assertions.assertThat(underTest.validator == null).isEqualTo(expectedNull)
        underTest.setValidationEnabled(false)
    }

    /**
     * Creates the parameters to be used for testing the method `setValidationEnabled`.
     * @return parameters to be used for testing the method `setValidationEnabled`.
     */
    @DataProvider
    private fun dataValidationInitializationTesting(): Array<Array<Any>> {
        return arrayOf(
            arrayOf(
                "Tests that the Validator object is not created if the validation is disabled",
                false,
                true
            ),
            arrayOf("Tests that the Validator object is created if the validation is enabled", true, false)
        )
    }

    /**
     * Tests that an instance fo [ConversionAnalyzer] is created only if the automatic conversion is enabled.
     * @param testCaseDescription the test case description
     * @param autoConversionEnabled true if the automatic conversion is enabled, false otherwise
     * @param expectedNull the expected result
     */
    @Test(dataProvider = "dataConversionAnalyzerInitializationTesting")
    fun testConversionAnalyzerIsInitializedOnlyIfValidationIsEnabled(
        testCaseDescription: String?,
        autoConversionEnabled: Boolean,
        expectedNull: Boolean
    ) {
        // GIVEN

        // WHEN
        underTest.setPrimitiveTypeConversionEnabled(autoConversionEnabled)

        // THEN
        Assertions.assertThat(underTest.conversionAnalyzer == null).isEqualTo(expectedNull)
        underTest.setPrimitiveTypeConversionEnabled(false)
    }

    /**
     * Creates the parameters to be used for testing the method `setValidationEnabled`.
     * @return parameters to be used for testing the method `setValidationEnabled`.
     */
    @DataProvider
    private fun dataConversionAnalyzerInitializationTesting(): Array<Array<Any>> {
        return arrayOf(
            arrayOf(
                "Tests that the ConversionAnalyzer object is not created if the automatic conversion is disabled",
                false,
                true
            ),
            arrayOf(
                "Tests that the ConversionAnalyzer object is created if the automatic conversion is enabled",
                true,
                false
            )
        )
    }

    /**
     * Test that the method: `handleInjectionException` works as expected.
     * @param testCaseDescription the test case description
     * @param forceConstructorInjection if true it forces the injection trough constructor
     * @param expectedReturnType the expected return type class
     * @throws Exception if the invoke method fails
     */
    @Test(dataProvider = "dataHandleInjectionExceptionTesting")
    @Throws(Exception::class)
    fun testHandleInjectionExceptionWorksAsExpected(
        testCaseDescription: String?,
        forceConstructorInjection: Boolean,
        expectedReturnType: Class<*>?
    ) {
        // GIVEN
        val classUtils = Mockito.mock(
            ClassUtils::class.java
        )
        Mockito.`when`(
            classUtils.areParameterNamesAvailable(
                ArgumentMatchers.any(
                    Constructor::class.java
                )
            )
        ).thenReturn(false)
        Mockito.`when`(classUtils.getConstructorParameters(ArgumentMatchers.any())).thenReturn(arrayOf())
        Mockito.`when`(classUtils.getInstance<Any>(ArgumentMatchers.any(), *ArgumentMatchers.any()))
            .thenReturn(MutableToFoo())
        reflectionUtils.setFieldValue(underTest, CLASS_UTILS_FIELD_NAME, classUtils)
        val handleInjectionExceptionMethod = underTest.javaClass.getDeclaredMethod(
            HANDLE_INJECTION_EXCEPTION_METHOD_NAME,
            Any::class.java,
            Class::class.java,
            Constructor::class.java,
            String::class.java,
            Array<Any>::class.java,
            Boolean::class.javaPrimitiveType,
            Exception::class.java
        )
        handleInjectionExceptionMethod.isAccessible = true

        // WHEN
        val actual: Any = try {
            handleInjectionExceptionMethod.invoke(
                underTest,
                fromFoo,
                MutableToFoo::class.java,
                null,
                "",
                null,
                forceConstructorInjection,
                Exception()
            )
        } catch (e: InvocationTargetException) {
            e.targetException
        }

        // THEN
        Assertions.assertThat(actual).isInstanceOf(expectedReturnType)
        restoreUnderTestObject()
    }

    /**
     * Creates the parameters to be used for testing the method `handleInjectionExceptionTesting`.
     * @return parameters to be used for testing the method `handleInjectionExceptionTesting`.
     */
    @DataProvider
    private fun dataHandleInjectionExceptionTesting(): Array<Array<Any>> {
        return arrayOf(
            arrayOf(
                "Tests that the handleInjectionException returns an error message if the forceConstructorInjection is true",
                true,
                InvalidBeanException::class.java
            ),
            arrayOf(
                "Tests that the handleInjectionException invokes the injectValues (that throws a MutableToFoo object) if the forceConstructorInjection is false",
                false,
                MutableToFoo::class.java
            )
        )
    }

    companion object {
        private const val SOURCE_FIELD_NAME = "sourceFieldName"
        private const val SOURCE_FIELD_NAME_2 = "sourceFieldName2"
        private const val GET_SOURCE_FIELD_VALUE_METHOD_NAME = "getSourceFieldValue"
        private const val GET_SOURCE_FIELD_TYPE_METHOD_NAME = "getSourceFieldType"
        private const val CACHE_MANAGER_FIELD_NAME = "cacheManager"
        private const val REFLECTION_UTILS_FIELD_NAME = "reflectionUtils"
        private const val CLASS_UTILS_FIELD_NAME = "classUtils"
        private const val GET_TRANSFORMER_VALUE_METHOD_NAME = "getTransformedValue"
        private const val GET_CONSTRUCTOR_ARGS_VALUES_METHOD_NAME = "getConstructorArgsValues"
        private const val HANDLE_INJECTION_EXCEPTION_METHOD_NAME = "handleInjectionException"
    }
}
