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

import com.expediagroup.beans.BeanUtils
import com.expediagroup.beans.sample.FromFoo
import com.expediagroup.beans.sample.FromFooAdvFields
import com.expediagroup.beans.sample.FromFooSimple
import com.expediagroup.beans.sample.FromFooSimpleBooleanField
import com.expediagroup.beans.sample.immutable.ImmutableFlatToFoo
import com.expediagroup.beans.sample.immutable.ImmutableToFoo
import com.expediagroup.beans.sample.immutable.ImmutableToFooAdvFields
import com.expediagroup.beans.sample.immutable.ImmutableToFooCustomAnnotation
import com.expediagroup.beans.sample.immutable.ImmutableToFooDiffFields
import com.expediagroup.beans.sample.immutable.ImmutableToFooDiffTypesFields
import com.expediagroup.beans.sample.immutable.ImmutableToFooMap
import com.expediagroup.beans.sample.immutable.ImmutableToFooMissingCustomAnnotation
import com.expediagroup.beans.sample.immutable.ImmutableToFooNotExistingFields
import com.expediagroup.beans.sample.immutable.ImmutableToFooSimple
import com.expediagroup.beans.sample.immutable.ImmutableToFooSimpleBoolean
import com.expediagroup.beans.sample.immutable.ImmutableToFooSimpleWrongTypes
import com.expediagroup.beans.sample.immutable.ImmutableToFooSubClass
import com.expediagroup.transformer.annotation.ConstructorArg
import com.expediagroup.transformer.cache.CacheManager
import com.expediagroup.transformer.error.InvalidBeanException
import com.expediagroup.transformer.error.InvalidFunctionException
import com.expediagroup.transformer.model.FieldMapping
import com.expediagroup.transformer.model.FieldTransformer
import com.expediagroup.transformer.utils.ClassUtils
import com.expediagroup.transformer.utils.ReflectionUtils
import org.assertj.core.api.Assertions
import org.assertj.core.api.ThrowableAssert.ThrowingCallable
import org.mockito.Mockito
import org.testng.annotations.AfterMethod
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import java.lang.reflect.Method
import java.lang.reflect.Parameter
import java.math.BigInteger
import java.util.*
import java.util.function.Supplier
import java.util.stream.IntStream

/**
 * Unit test for all [BeanTransformer] functions related to Immutable Java Beans.
 */
class ImmutableObjectTransformationTest : AbstractBeanTransformerTest() {
    /**
     * After method actions.
     */
    @AfterMethod
    fun afterMethod() {
        underTest.setValidationEnabled(false)
    }

    /**
     * Test that default transformation of immutable beans works properly.
     * @param testCaseDescription the test case description
     * @param transformer the transform to use
     * @param sourceObject the object to transform
     * @param targetObjectClass the target object class
     */
    @Test(dataProvider = "dataDefaultTransformationTesting")
    fun testImmutableBeanIsCorrectlyCopied(
        testCaseDescription: String?, transformer: BeanTransformer, sourceObject: Any?,
        targetObjectClass: Class<*>?
    ) {
        // GIVEN

        // WHEN
        val actual = transformer.transform(sourceObject, targetObjectClass)

        // THEN
        Assertions.assertThat(actual).usingRecursiveComparison()
            .ignoringAllOverriddenEquals()
            .isEqualTo(sourceObject)
    }

    /**
     * Creates the parameters to be used for testing the default transformation operations.
     * @return parameters to be used for testing the default transformation operations.
     */
    @DataProvider(parallel = true)
    private fun dataDefaultTransformationTesting(): Array<Array<Any>> {
        val beanUtils = BeanUtils()
        return arrayOf(
            arrayOf(
                "Test that immutable beans without constructor arguments parameter annotated with: @ConstructorArg are correctly copied.",
                beanUtils.transformer, fromFoo, ImmutableToFoo::class.java
            ),
            arrayOf(
                "Test that immutable beans without custom field mapping are correctly transformed.",
                beanUtils.transformer.withFieldMapping(), fromFoo, ImmutableToFoo::class.java
            ),
            arrayOf(
                "Test that immutable beans with constructor arguments parameter annotated with: @ConstructorArg are correctly copied.",
                beanUtils.transformer, fromFoo, ImmutableToFooCustomAnnotation::class.java
            ),
            arrayOf(
                "Test that bean that extends another class are correctly copied",
                beanUtils.transformer,
                fromFooSubClass,
                ImmutableToFooSubClass::class.java
            )
        )
    }

    /**
     * Test that immutable beans with extremely complex map are correctly transformed.
     */
    @Test
    fun testImmutableBeanIsCorrectlyCopied() {
        // GIVEN

        // WHEN
        val actual = BeanUtils().transformer.transform(fromFooMap, ImmutableToFooMap::class.java)

        // THEN
        Assertions.assertThat(actual).usingRecursiveComparison()
            .ignoringAllOverriddenEquals()
            .ignoringFields("extremeComplexMap")
            .isEqualTo(fromFooMap)
        val fromFooMapKeys = ArrayList(fromFooMap.extremeComplexMap.keys)
        val actualMapKeys = ArrayList(actual.extremeComplexMap.keys)
        IntStream.range(0, fromFooMapKeys.size)
            .forEach { i: Int ->
                val actualKey = actualMapKeys[i]
                val sourceKey = fromFooMapKeys[i]
                Assertions.assertThat(actualKey).usingRecursiveComparison()
                    .ignoringAllOverriddenEquals()
                    .isEqualTo(sourceKey)
                Assertions.assertThat(actual.extremeComplexMap[actualKey])
                    .usingRecursiveComparison()
                    .isEqualTo(fromFooMap.extremeComplexMap[sourceKey])
            }
    }

    /**
     * Test transformation on an existing bean is correctly copied.
     */
    @Test
    fun testTransformationOnAnExistingDestinationWorksProperly() {
        // GIVEN
        val immutableToFoo = ImmutableToFooSimple(null, null, false)

        // WHEN
        underTest.setValidationEnabled(true).transform(fromFooSimple, immutableToFoo)

        // THEN
        Assertions.assertThat(immutableToFoo)
            .usingRecursiveComparison()
            .isEqualTo(fromFooSimple)
        underTest.setValidationEnabled(false)
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
    fun testTransformationWithCompositeFieldNameMappingIsWorkingAsExpected(
        testCaseDescription: String?,
        sourceObject: Any,
        expectedName: String?,
        expectedId: BigInteger?,
        expectedPhoneNumbers: IntArray?
    ) {
        // GIVEN
        val underTestMock = Mockito.spy(TransformerImpl())
        val beanAllArgsConstructor = ClassUtils().getAllArgsConstructor(
            ImmutableFlatToFoo::class.java
        )
        Mockito.`when`(underTestMock.canBeInjectedByConstructorParams(beanAllArgsConstructor)).thenReturn(false)
        val phoneNumbersMapping = FieldMapping(PHONE_NUMBER_NESTED_OBJECT_FIELD_NAME, PHONE_NUMBER_DEST_FIELD_NAME)

        // WHEN
        val actual =
            underTestMock.withFieldMapping(phoneNumbersMapping).transform(sourceObject, ImmutableFlatToFoo::class.java)

        // THEN
        Assertions.assertThat(actual).extracting(NAME_FIELD_NAME, ID_FIELD_NAME, PHONE_NUMBER_DEST_FIELD_NAME)
            .containsExactly(expectedName, expectedId, expectedPhoneNumbers)
    }

    /**
     * Creates the parameters to be used for testing the transformation with composite field name mapping.
     * @return parameters to be used for testing the transformation with composite field name mapping.
     */
    @DataProvider(parallel = true)
    private fun dataCompositeFieldNameTesting(): Array<Array<Any?>> {
        return arrayOf(
            arrayOf(
                "Test that, in case a destination object field is contained into a nested object of the source field, defining a composite FieldMapping"
                        + "the field is correctly set.",
                fromFoo,
                fromFoo.name,
                fromFoo.id,
                fromFoo.nestedObject!!.phoneNumbers
            ), arrayOf(
                "Test that, in case a destination object field is contained into a nested object of the source field, defining a composite {@link FieldMapping}"
                        + " the field is correctly set even if some of them are null.",
                fromFooWithNullProperties,
                fromFooWithNullProperties.name,
                fromFooWithNullProperties.id,
                null
            )
        )
    }

    /**
     * Test that an exception is thrown if the constructor invocation throws exception.
     * @param testCaseDescription the test case description
     * @param sourceObject the object to transform
     * @param targetObjectClass the target object class
     */
    @Test(dataProvider = "dataConstructorErrorTesting", expectedExceptions = [InvalidBeanException::class])
    fun testTransformThrowsExceptionIfTheConstructorInvocationThrowsException(
        testCaseDescription: String?,
        sourceObject: Any?,
        targetObjectClass: Class<*>?
    ) {
        // GIVEN

        // WHEN
        underTest.setValidationEnabled(false).transform(sourceObject, targetObjectClass)
    }

    /**
     * Creates the parameters to be used for testing the exception raised in case of error during the constructor invocation.
     * @return parameters to be used for testing the transformation with composite field name mapping.
     */
    @DataProvider(parallel = true)
    private fun dataConstructorErrorTesting(): Array<Array<Any>> {
        val actual = FromFoo(NAME, ID, null, null, null)
        return arrayOf(
            arrayOf(
                "Test that an exception is thrown if the constructor is invoked with wrong type arguments",
                actual,
                ImmutableToFooDiffTypesFields::class.java
            )
        )
    }

    /**
     * Test that an [InvalidBeanException] is thrown if the bean is not valid.
     * @throws CloneNotSupportedException if the clone of an object fails
     */
    @Test(expectedExceptions = [InvalidBeanException::class])
    @Throws(CloneNotSupportedException::class)
    fun testTransformThrowsExceptionWhenImmutableIsInvalid() {
        // GIVEN
        val fromFooNullId = fromFoo.clone()
        fromFooNullId.id = null

        // WHEN
        underTest.setValidationEnabled(true).transform(fromFooNullId, ImmutableToFoo::class.java)
    }

    /**
     * Test that no exception is thrown if the destination object don't met the constraints and the validation is disabled.
     */
    @Test
    fun testTransformThrowsNoExceptionIfTheDestinationObjectValuesAreNotValidAndTheValidationIsDisabled() {
        // GIVEN
        fromFoo.id = null

        // WHEN
        val actual = underTest.transform(fromFoo, ImmutableToFoo::class.java)

        // THEN
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(fromFoo)
        fromFoo.id = ID
    }

    /**
     * Test that bean containing final fields (with different field names) are correctly copied.
     */
    @Test
    fun testImmutableBeanWithDifferentFieldNamesIsCorrectlyCopied() {
        // GIVEN

        // WHEN
        val beanTransformer = underTest.withFieldMapping(FieldMapping(ID_FIELD_NAME, IDENTIFIER_FIELD_NAME))
        val actual = beanTransformer.transform(fromFoo, ImmutableToFooDiffFields::class.java)

        // THEN
        Assertions.assertThat(actual).hasFieldOrPropertyWithValue(IDENTIFIER_FIELD_NAME, fromFoo.id)
            .usingRecursiveComparison()
            .ignoringFields(IDENTIFIER_FIELD_NAME)
            .isEqualTo(fromFoo)
    }

    /**
     * Test that bean containing advanced final fields are correctly copied.
     * @param testCaseDescription the test case description
     * @param sourceObject the object to transform
     * @param targetObjectClass the target object class
     * @param isNameFieldEmpty true if the name field should contain a value, false otherwise
     */
    @Test(dataProvider = "dataAdvancedFieldsCopyTesting")
    fun testImmutableBeanWithAdvancedFieldsIsCorrectlyCopied(
        testCaseDescription: String?,
        sourceObject: FromFooAdvFields,
        targetObjectClass: Class<*>?,
        isNameFieldEmpty: Boolean
    ) {
        // GIVEN
        val beanTransformer = underTest
            .withFieldMapping(FieldMapping(ID_FIELD_NAME, IDENTIFIER_FIELD_NAME))
            .withFieldMapping(FieldMapping(PRICE_FIELD_NAME, NET_PRICE_FIELD_NAME))
            .withFieldMapping(FieldMapping(PRICE_FIELD_NAME, GROSS_PRICE_FIELD_NAME))
            .withFieldTransformer(FieldTransformer(LOCALE_FIELD_NAME) { languageTag: String? ->
                Locale.forLanguageTag(
                    languageTag
                )
            })

        // WHEN
        val actual = beanTransformer.transform(sourceObject, targetObjectClass) as ImmutableToFooAdvFields

        // THEN
        Assertions.assertThat(actual).usingRecursiveComparison()
            .ignoringFields(AGE_FIELD_NAME, PRICE_FIELD_NAME, LOCALE_FIELD_NAME)
            .isEqualTo(sourceObject)
        Assertions.assertThat(actual)
            .extracting(AGE_FIELD_NAME, NET_PRICE_FIELD_NAME, GROSS_PRICE_FIELD_NAME, LOCALE_LANGUAGE_FIELD_NAME)
            .containsExactly(sourceObject.age.orElse(null), sourceObject.price, sourceObject.price, sourceObject.locale)
    }

    /**
     * Creates the parameters to be used for testing the transformation of advanced type fields.
     * @return parameters to be used for testing the transformation of advanced type fields.
     * @throws CloneNotSupportedException if the clone fails
     */
    @DataProvider(parallel = true)
    @Throws(CloneNotSupportedException::class)
    private fun dataAdvancedFieldsCopyTesting(): Array<Array<Any>> {
        val emptyOptionalSourceObject = fromFooAdvFields.clone()
        emptyOptionalSourceObject.name = Optional.empty()
        return arrayOf(
            arrayOf(
                "Test that bean containing advanced final fields are correctly copied",
                fromFooAdvFields,
                ImmutableToFooAdvFields::class.java,
                true
            ),
            arrayOf(
                "Test that bean containing advanced final fields (with empty optional) are correctly copied",
                emptyOptionalSourceObject,
                ImmutableToFooAdvFields::class.java,
                false
            )
        )
    }

    /**
     * Test that immutable bean containing a constructor with some field not annotated with
     * [ConstructorArg] is correctly copied.
     */
    @Test
    fun testImmutableBeanWithMissingConstructorArgIsCorrectlyCopied() {
        // GIVEN

        // WHEN
        val actual = underTest.withFieldTransformer()
            .transform(fromFooWithPrimitiveFields, ImmutableToFooMissingCustomAnnotation::class.java)

        // THEN
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual.name).isEqualTo(fromFooWithPrimitiveFields.name)
    }

    /**
     * Test that method: `getDestFieldName` retrieves the param name from the [ConstructorArg] if it is not provided from jvm directly.
     * @throws Exception the thrown exception
     */
    @Test
    @Throws(Exception::class)
    fun testGetDestFieldNameIsRetrievedFromConstructorArgIfTheParamNameIsNotProvidedFromJVM() {
        // GIVEN
        val declaringClassName = ImmutableToFoo::class.java.name
        // Parameter mock setup
        val constructorParameter = Mockito.mock(Parameter::class.java)
        Mockito.`when`(constructorParameter.name).thenReturn(CONSTRUCTOR_PARAMETER_NAME)

        // CacheManager mock setup
        initGetDestFieldNameTestMock(declaringClassName, constructorParameter)
        val getDestFieldNameMethod = underTest.javaClass.getDeclaredMethod(
            GET_DEST_FIELD_NAME_METHOD_NAME,
            Parameter::class.java,
            String::class.java
        )
        getDestFieldNameMethod.isAccessible = true

        // WHEN
        val actual = getDestFieldNameMethod.invoke(underTest, constructorParameter, declaringClassName) as String

        // THEN
        Assertions.assertThat(actual).isEqualTo(DEST_FIELD_NAME)

        // restore modified objects
        restoreObjects(getDestFieldNameMethod)
    }

    /**
     * Test that the method: `getConstructorValuesFromFields` works properly.
     * @throws Exception the thrown exception
     */
    @Test
    @Throws(Exception::class)
    fun testGetConstructorValuesFromFieldsWorksProperly() {
        // GIVEN
        underTest.withFieldTransformer(FieldTransformer(LOCALE_FIELD_NAME) { languageTag: String? ->
            Locale.forLanguageTag(
                languageTag
            )
        })

        // WHEN
        val getConstructorValuesFromFieldsMethod = underTest.javaClass.getDeclaredMethod(
            GET_CONSTRUCTOR_VALUES_FROM_FIELDS_METHOD_NAME, Any::class.java, Class::class.java, String::class.java
        )
        getConstructorValuesFromFieldsMethod.isAccessible = true
        val actual = getConstructorValuesFromFieldsMethod.invoke(
            underTest,
            fromFooAdvFields,
            ImmutableToFooAdvFields::class.java,
            ""
        ) as Array<Any>

        // THEN
        Assertions.assertThat(actual)
            .isNotNull
            .hasSize(TOTAL_ADV_CLASS_FIELDS)

        // restore modified objects
        restoreObjects(getConstructorValuesFromFieldsMethod)
    }

    /**
     * Test that method: `getDestFieldName` returns null if the constructor's parameter name is not provided from jvm directly and the [ConstructorArg] is not defined.
     * @throws Exception the thrown exception
     */
    @Test
    @Throws(Exception::class)
    fun testGetDestFieldNameReturnsNullIfConstructorParamHasNoNameProvidedFromJVMAndNoConstructorArgIsDefined() {
        // GIVEN
        val declaringClassName = ImmutableToFoo::class.java.name
        // Parameter mock setup
        val constructorParameter = Mockito.mock(Parameter::class.java)
        Mockito.`when`(constructorParameter.name).thenReturn(null)

        // CacheManager mock setup
        initGetDestFieldNameTestMock(declaringClassName, constructorParameter)
        val getDestFieldNameMethod = underTest.javaClass.getDeclaredMethod(
            GET_DEST_FIELD_NAME_METHOD_NAME,
            Parameter::class.java,
            String::class.java
        )
        getDestFieldNameMethod.isAccessible = true

        // WHEN
        val actual = getDestFieldNameMethod.invoke(underTest, constructorParameter, declaringClassName) as String

        // THEN
        Assertions.assertThat(actual).isEqualTo(DEST_FIELD_NAME)

        // restore modified objects
        restoreObjects(getDestFieldNameMethod)
    }

    /**
     * Test that a meaningful exception is returned when a constructor is invoked with wrong arguments.
     */
    @Test
    fun testTransformationReturnsAMeaningfulException() {
        // GIVEN
        val targetClass = ImmutableToFooSimpleWrongTypes::class.java
        val expectedExceptionMessageFormat =
            ("Constructor invoked with wrong arguments. Expected: public %s(java.lang.Integer,java.lang.String); Found: %s(java.math.BigInteger,java.lang.String). "
                    + "Double check that each %s's field have the same type and name than the source object: %s otherwise specify a transformer configuration. "
                    + "Error message: argument type mismatch")
        val targetClassName = targetClass.name
        val expectedExceptionMessage = String.format(
            expectedExceptionMessageFormat,
            targetClassName,
            targetClassName,
            targetClass.simpleName,
            fromFooSimple.javaClass.name
        )

        // WHEN
        val actual = ThrowingCallable { underTest.transform(fromFooSimple, targetClass) }

        // THEN
        Assertions.assertThatThrownBy(actual).isInstanceOf(InvalidBeanException::class.java)
            .hasMessage(expectedExceptionMessage)
    }

    /**
     * Test that a bean containing a field not existing in the source object, but with a transformer function defined for such object is correctly copied.
     */
    @Test
    fun testThatAnyTypeOfBeanContainsANotExistingFieldInTheSourceObjectIsCorrectlyCopiedThroughTransformerFunctions() {
        // GIVEN
        val fromFooSimple = FromFooSimple(NAME, ID, ACTIVE)
        val ageFieldTransformer = FieldTransformer<Any, Int>(AGE_FIELD_NAME, Supplier { AGE })

        // WHEN
        underTest.withFieldTransformer(ageFieldTransformer)
        val immutableObjectBean = underTest.transform(fromFooSimple, ImmutableToFooNotExistingFields::class.java)

        // THEN
        Assertions.assertThat(immutableObjectBean).hasFieldOrPropertyWithValue(AGE_FIELD_NAME, AGE)
    }

    /**
     * Test that, given a set of fields for which the transformation has to be skipped, they are actually skipped.
     */
    @Test
    fun testFieldTransformationSkipWorksProperly() {
        // GIVEN
        underTest.skipTransformationForField("name", "nestedObject.phoneNumbers")

        // WHEN
        val actual = underTest.transform(fromFoo, ImmutableToFoo::class.java)

        // THEN
        Assertions.assertThat(actual)
            .hasNoNullFieldsOrPropertiesExcept(NAME_FIELD_NAME, PHONE_NUMBER_NESTED_OBJECT_FIELD_NAME)
    }

    /**
     * Test that the transformer function is applied earlier than the default value.
     */
    @Test
    fun testTransformerFunctionHasHigherPriorityThanDefaultValue() {
        // GIVEN
        val fromFooSimpleNullFields = FromFooSimpleBooleanField()
        val nullToTrue = FieldTransformer(WORK_FIELD_NAME) { aBoolean: Boolean? -> aBoolean == null || aBoolean }

        // WHEN
        val actual = underTest
            .withFieldTransformer(nullToTrue)
            .transform(fromFooSimpleNullFields, ImmutableToFooSimpleBoolean::class.java)

        // THEN
        Assertions.assertThat(actual.work).isTrue
    }

    /**
     * Test that an [InvalidFunctionException] is raised if the transformer function defined is not valid.
     */
    @Test(expectedExceptions = [InvalidFunctionException::class])
    fun testTransformRaiseAnExceptionIfTheTransformerFunctionIsNotValid() {
        // GIVEN
        val fromFooSimpleNullFields = FromFooSimpleBooleanField()
        val upperCase = FieldTransformer(WORK_FIELD_NAME) { obj: String -> obj.uppercase(Locale.getDefault()) }

        // WHEN
        underTest.withFieldTransformer(upperCase)
            .transform(fromFooSimpleNullFields, ImmutableToFooSimpleBoolean::class.java)
    }

    /**
     * Initializes the mocks required for testing method: `getDestFieldName`.
     * @param declaringClassName the declaring class name
     * @param constructorParameter the constructor parameter
     */
    private fun initGetDestFieldNameTestMock(declaringClassName: String, constructorParameter: Parameter) {
        val cacheManager = Mockito.mock(CacheManager::class.java)
        val cacheKey = "DestFieldName-$declaringClassName-$CONSTRUCTOR_PARAMETER_NAME"
        Mockito.`when`(cacheManager.getFromCache(cacheKey, String::class.java)).thenReturn(null)
        // ConstructorArg mock setup
        val constructorArg = Mockito.mock(ConstructorArg::class.java)
        Mockito.`when`(constructorArg.value).thenReturn(DEST_FIELD_NAME)
        // ReflectionUtils mock setup
        val reflectionUtilsMock = Mockito.mock(ReflectionUtils::class.java)
        Mockito.`when`(
            reflectionUtilsMock.getParameterAnnotation(
                constructorParameter,
                ConstructorArg::class.java,
                declaringClassName
            )
        ).thenReturn(constructorArg)
        reflectionUtils.setFieldValue(underTest, REFLECTION_UTILS_FIELD_NAME, reflectionUtilsMock)
    }

    /**
     * Restores the initial object status before testing method: `getDestFieldName`.
     * @param getDestFieldNameMethod the destination field name method
     */
    private fun restoreObjects(getDestFieldNameMethod: Method) {
        getDestFieldNameMethod.isAccessible = false
        reflectionUtils.setFieldValue(underTest, REFLECTION_UTILS_FIELD_NAME, ReflectionUtils())
    }

    companion object {
        private const val TOTAL_ADV_CLASS_FIELDS = 11
        private const val GET_DEST_FIELD_NAME_METHOD_NAME = "getDestFieldName"
        private const val GET_CONSTRUCTOR_VALUES_FROM_FIELDS_METHOD_NAME = "getConstructorValuesFromFields"
        private const val PRICE_FIELD_NAME = "price"
        private const val NET_PRICE_FIELD_NAME = "price.netPrice"
        private const val GROSS_PRICE_FIELD_NAME = "price.grossPrice"
        private const val WORK_FIELD_NAME = "work"
        private const val ACTIVE = true
        private const val LOCALE_LANGUAGE_FIELD_NAME = "locale.language"
    }
}