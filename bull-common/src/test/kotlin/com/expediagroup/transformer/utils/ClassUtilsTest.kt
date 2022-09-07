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
package com.expediagroup.transformer.utils

import com.expediagroup.beans.sample.AbstractClass
import com.expediagroup.beans.sample.FromFoo
import com.expediagroup.beans.sample.FromFooAdvFields
import com.expediagroup.beans.sample.FromFooSimple
import com.expediagroup.beans.sample.FromFooSimpleNoGetters
import com.expediagroup.beans.sample.FromFooSubClass
import com.expediagroup.beans.sample.immutable.ImmutableToFoo
import com.expediagroup.beans.sample.immutable.ImmutableToFooCustomAnnotation
import com.expediagroup.beans.sample.immutable.ImmutableToFooSubClass
import com.expediagroup.beans.sample.mixed.MixedToFoo
import com.expediagroup.beans.sample.mixed.MixedToFooMissingConstructor
import com.expediagroup.beans.sample.mixed.MixedToFooStaticField
import com.expediagroup.beans.sample.mixed.MixedToFooWithBuilder
import com.expediagroup.beans.sample.mutable.MutableToFoo
import com.expediagroup.beans.sample.mutable.MutableToFooSubClass
import com.expediagroup.beans.sample.mutable.MutableToFooWithBuilder
import com.expediagroup.beans.sample.mutable.MutableToFooWithWrongBuilder
import com.expediagroup.transformer.annotation.ConstructorArg
import com.expediagroup.transformer.constant.ClassType
import com.expediagroup.transformer.error.InstanceCreationException
import com.expediagroup.transformer.error.InvalidBeanException
import com.expediagroup.transformer.error.MissingMethodException
import org.assertj.core.api.Assertions
import org.mockito.InjectMocks
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.testng.annotations.BeforeClass
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.lang.reflect.Parameter
import java.math.BigDecimal
import java.math.BigInteger
import java.time.Instant
import java.util.*
import java.util.function.Predicate
import javax.validation.constraints.NotNull

/**
 * Unit test for [ClassUtils].
 */
class ClassUtilsTest {
    /**
     * The class to be tested.
     */
    @InjectMocks
    private lateinit var underTest: ClassUtils

    /**
     * Initializes mock.
     */
    @BeforeClass
    fun beforeClass() {
        MockitoAnnotations.openMocks(this)
    }

    /**
     * Tests that the method `isPrimitiveType` returns the expected value.
     * @param testCaseDescription the test case description
     * @param testClass the class to test
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "dataIsPrimitiveTypeObjectTesting")
    fun testIsPrimitiveTypeWorksAsExpected(
        testCaseDescription: String?,
        testClass: Class<*>?,
        expectedResult: Boolean
    ) {
        // GIVEN

        // WHEN
        val actual = underTest.isPrimitiveType(testClass)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expectedResult)
    }

    /**
     * Creates the parameters to be used for testing the method `isPrimitiveType`.
     * @return parameters to be used for testing the method `isPrimitiveType`.
     */
    @DataProvider
    private fun dataIsPrimitiveTypeObjectTesting(): Array<Array<Any>> {
        return arrayOf(
            arrayOf(
                "Tests that the method returns true if the class is a primitive type object",
                BigDecimal::class.java,
                true
            ),
            arrayOf(
                "Tests that the method returns false if the class is not a primitive type object",
                FromFoo::class.java,
                false
            ),
            arrayOf("Tests that the method returns true if the class is a Boolean", Boolean::class.java, true),
            arrayOf("Tests that the method returns true if the class is a Character", Char::class.java, true),
            arrayOf("Tests that the method returns true if the class is a Byte", Byte::class.java, true),
            arrayOf("Tests that the method returns true if the class is a Void", Void::class.java, true),
            arrayOf("Tests that the method returns true if the class is a String", String::class.java, true)
        )
    }

    /**
     * Tests that the method `isSpecialType` returns the expected value.
     * @param testCaseDescription the test case description
     * @param testClass the class to test
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "dataSpecialTypeObjectTesting")
    fun testIsSpecialTypeWorksAsExpected(testCaseDescription: String?, testClass: Class<*>?, expectedResult: Boolean) {
        // GIVEN

        // WHEN
        val actual = underTest.isSpecialType(testClass)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expectedResult)
    }

    /**
     * Creates the parameters to be used for testing the method `isSpecialType`.
     * @return parameters to be used for testing the method `isSpecialType`.
     */
    @DataProvider
    private fun dataSpecialTypeObjectTesting(): Array<Array<Any>> {
        return arrayOf(
            arrayOf(
                "Tests that the method returns true if the class is a special type object",
                Locale::class.java,
                true
            ),
            arrayOf(
                "Tests that the method returns false if the class is not a special type object",
                BigDecimal::class.java,
                false
            ),
            arrayOf(
                "Tests that the method returns true if the class is an instance of Temporal interface",
                Instant::class.java,
                true
            ),
            arrayOf(
                "Tests that the method returns true if the class is an instance of Properties class",
                Properties::class.java,
                true
            ),
            arrayOf(
                "Tests that the method returns true if the class is an instance of Currency class",
                Currency::class.java,
                true
            ),
            arrayOf(
                "Tests that the method returns true if the class is an instance of Date class",
                Date::class.java,
                true
            )
        )
    }

    /**
     * Tests that the method `isPrimitiveOrSpecialType` returns the expected value.
     * @param testCaseDescription the test case description
     * @param testClass the class to test
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "dataPrimitiveOrSpecialTypeObjectTesting")
    fun testIsPrimitiveOrSpecialTypeWorksAsExpected(
        testCaseDescription: String?,
        testClass: Class<*>?,
        expectedResult: Boolean
    ) {
        // GIVEN

        // WHEN
        val actual = underTest.isPrimitiveOrSpecialType(testClass)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expectedResult)
    }

    /**
     * Creates the parameters to be used for testing the method `isPrimitiveOrSpecialType`.
     * @return parameters to be used for testing the method `isPrimitiveOrSpecialType`.
     */
    @DataProvider
    private fun dataPrimitiveOrSpecialTypeObjectTesting(): Array<Array<Any?>> {
        return arrayOf(
            arrayOf(
                "Tests that the method returns true if the class is a primitive or special type object",
                Locale::class.java,
                true
            ),
            arrayOf(
                "Tests that the method returns false if the class is a primitive nor a special type object",
                BigDecimal::class.java,
                true
            ),
            arrayOf(
                "Tests that the method returns false if the class is not a primitive nor a special type object",
                FromFoo::class.java,
                false
            ),
            arrayOf("Tests that the method returns false if the class is null", null, false)
        )
    }

    /**
     * Tests that the method `isPrimitiveArrayType` returns the expected value.
     * @param testCaseDescription the test case description
     * @param testArray the array to test
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "dataPrimitiveArrayTypeTesting")
    fun testIsPrimitiveArrayTypeWorksAsExpected(testCaseDescription: String?, testArray: Any, expectedResult: Boolean) {
        // GIVEN

        // WHEN
        val actual = underTest.isPrimitiveTypeArray(testArray.javaClass)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expectedResult)
    }

    /**
     * Creates the parameters to be used for testing the method `isPrimitiveArrayType`.
     * @return parameters to be used for testing the method `isPrimitiveArrayType`.
     */
    @DataProvider
    private fun dataPrimitiveArrayTypeTesting(): Array<Array<Any>> {
        return arrayOf(
            arrayOf(
                "Tests that the method returns true if the array is of type int[]",
                PRIMITIVE_INT_ARRAY,
                true
            ),
            arrayOf("Tests that the method returns true if the array is of type short[]", PRIMITIVE_SHORT_ARRAY, true),
            arrayOf("Tests that the method returns true if the array is of type char[]", PRIMITIVE_CHAR_ARRAY, true),
            arrayOf("Tests that the method returns true if the array is of type byte[]", PRIMITIVE_BYTE_ARRAY, true),
            arrayOf(
                "Tests that the method returns true if the array is of type double[]",
                PRIMITIVE_DOUBLE_ARRAY,
                true
            ),
            arrayOf("Tests that the method returns true if the array is of type float[]", PRIMITIVE_FLOAT_ARRAY, true),
            arrayOf("Tests that the method returns true if the array is of type long[]", PRIMITIVE_LONG_ARRAY, true),
            arrayOf(
                "Tests that the method returns true if the array is of type Integer[]",
                PRIMITIVE_INTEGER_ARRAY,
                true
            ),
            arrayOf("Tests that the method returns false if the array is of type FromFoo[]", NOT_PRIMITIVE_ARRAY, false)
        )
    }

    /**
     * Tests that the method `getPrivateFinalFields` works as expected.
     * @param testCaseDescription the test case description
     * @param testClass the class to test
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "dataGetPrivateFinalFieldsTesting")
    fun testGetPrivateFinalFieldsWorksAsExpected(
        testCaseDescription: String?,
        testClass: Class<*>?,
        expectedResult: Int
    ) {
        // GIVEN

        // WHEN
        val actual = underTest.getPrivateFinalFields(testClass)

        // THEN
        Assertions.assertThat(actual).hasSize(expectedResult)
    }

    /**
     * Creates the parameters to be used for testing the method `getPrivateFinalFields`.
     * @return parameters to be used for testing the method `getPrivateFinalFields`.
     */
    @DataProvider
    private fun dataGetPrivateFinalFieldsTesting(): Array<Array<Any>> {
        return arrayOf(
            arrayOf(
                "Tests that the method returns 0 if the given class has no private final fields",
                CLASS_WITHOUT_PRIVATE_FINAL_FIELDS,
                ZERO
            ),
            arrayOf(
                "Tests that the method returns the expected value if the class has private final fields",
                CLASS_WITH_PRIVATE_FINAL_FIELDS,
                EXPECTED_PRIVATE_FINAL_FIELDS
            ),
            arrayOf(
                "Tests that the method returns the expected value if the class has private final fields and extends another class",
                CLASS_WITH_PRIVATE_FINAL_FIELDS_AND_SUB_CLASS, EXPECTED_SUB_CLASS_PRIVATE_FIELDS
            )
        )
    }

    /**
     * Tests that the method `getNotFinalFields` works as expected.
     * @param testCaseDescription the test case description
     * @param testClass the class to test
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "dataGetNotFinalFieldsTesting")
    fun testGetNotFinalFieldsWorksAsExpected(testCaseDescription: String?, testClass: Class<*>?, expectedResult: Int) {
        // GIVEN

        // WHEN
        val actual = underTest.getNotFinalFields(testClass, true)

        // THEN
        Assertions.assertThat(actual).hasSize(expectedResult)
    }

    /**
     * Creates the parameters to be used for testing the method `getNotFinalFields`.
     * @return parameters to be used for testing the method `getNotFinalFields`.
     */
    @DataProvider
    private fun dataGetNotFinalFieldsTesting(): Array<Array<Any>> {
        return arrayOf(
            arrayOf(
                "Tests that the method returns 0 if the given class has only private fields",
                CLASS_WITH_PRIVATE_FINAL_FIELDS,
                ZERO
            ),
            arrayOf(
                "Tests that the method returns the expected value if the class has private final fields",
                CLASS_WITH_PRIVATE_AND_PUBLIC_FIELDS, EXPECTED_MIXED_CLASS_TOTAL_NOT_FINAL_FIELDS
            )
        )
    }

    /**
     * Tests that the method `getTotalFields` works as expected.
     * @param testCaseDescription the test case description
     * @param testClass the class to test
     * @param fieldPredicate the predicate to apply
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "dataGetTotalFieldsTesting")
    fun testGetTotalFieldsWorksAsExpected(
        testCaseDescription: String?,
        testClass: Class<*>?,
        fieldPredicate: Predicate<Field?>?,
        expectedResult: Int
    ) {
        // GIVEN

        // WHEN
        val actual = underTest.getTotalFields(testClass, fieldPredicate).toLong()

        // THEN
        Assertions.assertThat(actual).isEqualTo(expectedResult.toLong())
    }

    /**
     * Creates the parameters to be used for testing the method `getTotalFields`.
     * @return parameters to be used for testing the method `getTotalFields`.
     */
    @DataProvider
    private fun dataGetTotalFieldsTesting(): Array<Array<Any>> {
        return arrayOf(
            arrayOf(
                "Tests that the method returns 0 if the given class has no private final fields",
                CLASS_WITHOUT_PRIVATE_FINAL_FIELDS,
                IS_FINAL_FIELD_PREDICATE,
                ZERO
            ),
            arrayOf(
                "Tests that the method returns the expected value if the class has private final fields",
                CLASS_WITH_PRIVATE_FINAL_FIELDS,
                IS_FINAL_FIELD_PREDICATE,
                EXPECTED_PRIVATE_FINAL_FIELDS
            )
        )
    }

    /**
     * Tests that the method `getPrivateFields` works as expected.
     * @param testCaseDescription the test case description
     * @param testClass the class to test
     * @param skipFinal if true the final fields are skipped
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "dataGetPrivateFieldsTesting")
    fun testGetPrivateFieldsWorksAsExpected(
        testCaseDescription: String?,
        testClass: Class<*>?,
        skipFinal: Boolean,
        expectedResult: Int
    ) {
        // GIVEN

        // WHEN
        val actual = if (Objects.nonNull(skipFinal)) underTest.getPrivateFields(
            testClass,
            skipFinal
        ) else underTest.getPrivateFields(testClass)

        // THEN
        Assertions.assertThat(actual).hasSize(expectedResult)
    }

    /**
     * Creates the parameters to be used for testing the method `getPrivateFields`.
     * @return parameters to be used for testing the method `getPrivateFields`.
     */
    @DataProvider
    private fun dataGetPrivateFieldsTesting(): Array<Array<Any?>> {
        return arrayOf(
            arrayOf(
                "Tests that the method returns the expected value if the class has private and public fields",
                CLASS_WITH_PRIVATE_AND_PUBLIC_FIELDS,
                false,
                EXPECTED_MIXED_CLASS_TOTAL_PRIVATE_FIELDS
            ),
            arrayOf(
                "Tests that the method returns the expected value if the class has private and public fields and skipFinal is not passed as param",
                CLASS_WITH_PRIVATE_AND_PUBLIC_FIELDS, null, EXPECTED_MIXED_CLASS_TOTAL_PRIVATE_FIELDS
            ),
            arrayOf(
                "Tests that the method returns the expected value if the class has private final fields only",
                CLASS_WITH_PRIVATE_FINAL_FIELDS,
                false,
                EXPECTED_PRIVATE_FINAL_FIELDS
            ),
            arrayOf(
                "Tests that the method returns the expected value if the class has private final fields only and skipFinal is not passed as param",
                CLASS_WITH_PRIVATE_FINAL_FIELDS, null, EXPECTED_PRIVATE_FINAL_FIELDS
            ),
            arrayOf(
                "Tests that the method returns the expected value if the class extends another class",
                ImmutableToFooSubClass::class.java,
                false,
                EXPECTED_SUB_CLASS_PRIVATE_FIELDS
            ),
            arrayOf(
                "Tests that the method returns the expected value if the class extends another class and skipFinal is not passed as param",
                ImmutableToFooSubClass::class.java, null, EXPECTED_SUB_CLASS_PRIVATE_FIELDS
            ),
            arrayOf(
                "Tests that the method returns the expected value if the skipFinal is enabled",
                CLASS_WITH_PRIVATE_AND_PUBLIC_FIELDS,
                true,
                EXPECTED_MIXED_CLASS_TOTAL_PRIVATE_NOT_FINAL_FIELDS
            )
        )
    }

    /**
     * Tests that the method `getDeclaredFields` works as expected.
     * @param testCaseDescription the test case description
     * @param testClass the class to test
     * @param skipStatic if true the static fields are skipped
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "dataGetDeclaredFieldsTesting")
    fun testGetDeclaredFieldsWorksAsExpected(
        testCaseDescription: String?,
        testClass: Class<*>?,
        skipStatic: Boolean,
        expectedResult: Int
    ) {
        // GIVEN

        // WHEN
        val actual = underTest.getDeclaredFields(testClass, skipStatic)

        // THEN
        Assertions.assertThat(actual).hasSize(expectedResult)
    }

    /**
     * Creates the parameters to be used for testing the method `getDeclaredFields`.
     * @return parameters to be used for testing the method `getDeclaredFields`.
     */
    @DataProvider
    private fun dataGetDeclaredFieldsTesting(): Array<Array<Any>> {
        return arrayOf(
            arrayOf(
                "Tests that the method returns the expected total number of fields when the skipStatic param is true",
                CLASS_WITH_STATIC_FIELDS,
                true,
                EXPECTED_NOT_STATIC_FIELDS
            ),
            arrayOf(
                "Tests that the method returns the expected value if the class has private final fields only",
                CLASS_WITH_STATIC_FIELDS,
                false,
                CLASS_WITH_STATIC_FIELDS.declaredFields.size
            ),
            arrayOf(
                "Tests that the method returns the expected total number of fields when the skipStatic param is true and the class extends another class",
                CLASS_WITH_PRIVATE_FINAL_FIELDS_AND_SUB_CLASS, true, EXPECTED_SUB_CLASS_PRIVATE_FIELDS
            )
        )
    }

    /**
     * Test that the a manual declared Builder is returned by method: `getDeclaredClasses`.
     * @param testCaseDescription the test case description
     * @param testClass the class from which extract the nested classes
     * @param expectedClass the class expected as nested
     */
    @Test(dataProvider = "dataGetDeclaredClassesTesting")
    fun testGetDeclaredClassesWorksAsExpected(
        testCaseDescription: String?,
        testClass: Class<*>?,
        expectedClass: Class<*>?
    ) {
        // GIVEN

        // WHEN
        val actual = underTest.getDeclaredClasses(testClass)

        // THEN
        Assertions.assertThat(actual).contains(expectedClass)
    }

    /**
     * Creates the parameters to be used for testing the method `getDeclaredClasses`.
     * @return parameters to be used for testing the method `getDeclaredClasses`.
     */
    @DataProvider
    private fun dataGetDeclaredClassesTesting(): Array<Array<Any>> {
        return arrayOf(
            arrayOf(
                "Test that the a manual declared Builder is returned by method: {@code getDeclaredClasses}",
                MutableToFooWithBuilder::class.java,
                MutableToFooWithBuilder.Builder::class.java
            ),
            arrayOf(
                "Test that the a Builder created by lombok is returned by method: {@code getDeclaredClasses}",
                MixedToFooWithBuilder::class.java,
                MixedToFooWithBuilder.builder().javaClass
            )
        )
    }

    /**
     * Tests that the method `getAllArgsConstructor` returns the class constructor.
     */
    @Test
    fun testGetAllArgsConstructorWorksAsExpected() {
        // GIVEN

        // WHEN
        val actual: Constructor<*> = underTest.getAllArgsConstructor(CLASS_WITH_PRIVATE_FINAL_FIELDS)

        // THEN
        Assertions.assertThat(actual).isNotNull
    }

    /**
     * Tests that the method `getNoArgsConstructor` works as expected.
     */
    @Test
    fun testGetNoArgsConstructorWorksAsExpected() {
        // GIVEN

        // WHEN
        val actual = underTest.getNoArgsConstructor(CLASS_WITHOUT_PRIVATE_FINAL_FIELDS)

        // THEN
        Assertions.assertThat(actual).isNotNull
    }

    /**
     * Tests that the method `areParameterNamesAvailable` works as expected.
     * @param testCaseDescription the test case description
     * @param constructor the test constructor to check
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "dataAreParameterNamesAvailableTesting")
    fun testAreParameterNamesAvailableWorksAsExpected(
        testCaseDescription: String?,
        constructor: Constructor<*>?,
        expectedResult: Boolean
    ) {
        // GIVEN

        // WHEN
        val actual = underTest.areParameterNamesAvailable(constructor)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expectedResult)
    }

    /**
     * Creates the parameters to be used for testing the method `areParameterNamesAvailable`.
     * @return parameters to be used for testing the method `areParameterNamesAvailable`.
     */
    @DataProvider
    private fun dataAreParameterNamesAvailableTesting(): Array<Array<Any>> {
        return arrayOf(
            arrayOf(
                "Tests that the method returns false if the constructor parameter names are not available",
                createMockedConstructor(),
                false
            ),
            arrayOf(
                "Tests that the method returns false if the constructor parameter names are available",
                underTest.getAllArgsConstructor(
                    MixedToFoo::class.java
                ),
                true
            )
        )
    }

    /**
     * Creates a mocked constructor for testing method `areParameterNamesAvailable`.
     * @return a mocked [Constructor] instance
     */
    private fun createMockedConstructor(): Constructor<*> {
        val parameter = Mockito.mock(Parameter::class.java)
        val reflectionUtils = ReflectionUtils()
        reflectionUtils.setFieldValue(parameter, "name", "paramName")
        Mockito.`when`(parameter.isNamePresent).thenReturn(false)
        val constructor = Mockito.mock(Constructor::class.java)
        Mockito.`when`(constructor.declaringClass).thenReturn(ImmutableToFoo::class.java)
        Mockito.`when`(constructor.parameters).thenReturn(arrayOf(parameter))
        return constructor
    }

    /**
     * Tests that the method `getNoArgsConstructor` throws exception if the class has no all args constructor.
     */
    @Test(expectedExceptions = [InvalidBeanException::class])
    fun testGetNoArgsConstructorThrowsExceptionIfTheConstructorIsMissing() {
        // GIVEN

        // WHEN
        underTest.getNoArgsConstructor(CLASS_WITHOUT_CONSTRUCTOR)
    }

    /**
     * Tests that the method `getConstructorParameters` returns the constructor parameter.
     */
    @Test
    fun testGetConstructorParameters() {
        // GIVEN
        val classConstructor: Constructor<*> = underTest.getAllArgsConstructor(CLASS_WITH_PRIVATE_FINAL_FIELDS)

        // WHEN
        val constructorParameters = underTest.getConstructorParameters(classConstructor)

        // THEN
        Assertions.assertThat(constructorParameters).hasSize(EXPECTED_CLASS_PARAMETERS)
    }

    /**
     * Tests that the method `getInstance` raises an [InstanceCreationException] if an error occurs.
     */
    @Test(expectedExceptions = [InstanceCreationException::class])
    fun testGetInstanceRaisesAnInstanceCreationExceptionIfAnErrorOccurs() {
        // GIVEN
        val classConstructor: Constructor<*> = underTest.getAllArgsConstructor(
            AbstractClass::class.java
        )

        // WHEN
        underTest.getInstance<Any>(classConstructor, NAME_FIELD_NAME, ZERO)
    }

    /**
     * Tests that the method `hasField` works as expected.
     * @param testCaseDescription the test case description
     * @param fieldName the field's name to retrieve
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "dataHasFieldTesting")
    fun testHasFieldWorksAsExpected(testCaseDescription: String?, fieldName: String?, expectedResult: Boolean) {
        // GIVEN
        val immutableToFooSubClass = ImmutableToFooSubClass(null, null, null, null, null, null, 0, false, null)

        // WHEN
        val actual = underTest.hasField(immutableToFooSubClass, fieldName)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expectedResult)
    }

    /**
     * Creates the parameters to be used for testing the method `hasField`.
     * @return parameters to be used for testing the method `hasField`.
     */
    @DataProvider
    private fun dataHasFieldTesting(): Array<Array<Any>> {
        return arrayOf(
            arrayOf(
                "Tests that the method returns false if the given field does not exists",
                NOT_EXISTING_FIELD_NAME,
                false
            ),
            arrayOf("Tests that the method returns true if the given field exists", NAME_FIELD_NAME, true)
        )
    }

    /**
     * Tests that the method `hasFinalFields` works as expected.
     * @param testCaseDescription the test case description
     * @param testClass the class to test
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "dataHasFinalFieldTesting")
    fun testHasPrivateFinalFieldsWorksAsExpected(
        testCaseDescription: String?,
        testClass: Class<*>?,
        expectedResult: Boolean
    ) {
        // GIVEN

        // WHEN
        val actual = underTest.hasFinalFields(testClass)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expectedResult)
    }

    /**
     * Creates the parameters to be used for testing the method `hasFinalFields`.
     * @return parameters to be used for testing the method `hasFinalFields`.
     */
    @DataProvider
    private fun dataHasFinalFieldTesting(): Array<Array<Any>> {
        return arrayOf(
            arrayOf(
                "Tests that the method returns true if the given class has private final fields",
                CLASS_WITH_PRIVATE_FINAL_FIELDS_AND_SUB_CLASS,
                true
            ),
            arrayOf(
                "Tests that the method returns true if the given class has no private final fields",
                CLASS_WITHOUT_PRIVATE_FINAL_FIELDS,
                false
            )
        )
    }

    /**
     * Tests that the method `hasSetterMethods` works as expected.
     * @param testCaseDescription the test case description
     * @param testClass the class to test
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "dataHasSetterMethodsTesting")
    fun testHasSetterMethodsWorksAsExpected(
        testCaseDescription: String?,
        testClass: Class<*>?,
        expectedResult: Boolean
    ) {
        // GIVEN

        // WHEN
        val actual = underTest.hasSetterMethods(testClass)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expectedResult)
    }

    /**
     * Creates the parameters to be used for testing the method `hasSetterMethods`.
     * @return parameters to be used for testing the method `hasSetterMethods`.
     */
    @DataProvider
    private fun dataHasSetterMethodsTesting(): Array<Array<Any>> {
        return arrayOf(
            arrayOf(
                "Tests that the method returns true if the given class has private final fields",
                CLASS_WITH_PRIVATE_AND_PUBLIC_FIELDS,
                true
            ),
            arrayOf(
                "Tests that the method returns true if the given class has no private final fields",
                CLASS_WITH_PRIVATE_FINAL_FIELDS,
                false
            )
        )
    }

    /**
     * Tests that the method `notAllParameterAnnotatedWith` works as expected.
     * @param testCaseDescription the test case description
     * @param annotationClass the annotation to search
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "dataNotAllParameterAnnotatedWithTesting")
    fun testAllParameterAnnotatedWithWorksAsExpected(
        testCaseDescription: String?,
        annotationClass: Class<out Annotation?>?,
        expectedResult: Boolean
    ) {
        // GIVEN
        val constructor = ImmutableToFooCustomAnnotation::class.java.constructors[0]

        // WHEN
        val actual = underTest.allParameterAnnotatedWith(constructor, annotationClass)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expectedResult)
    }

    /**
     * Creates the parameters to be used for testing the method `notAllParameterAnnotatedWith`.
     * @return parameters to be used for testing the method `notAllParameterAnnotatedWith`.
     */
    @DataProvider
    private fun dataNotAllParameterAnnotatedWithTesting(): Array<Array<Any>> {
        return arrayOf(
            arrayOf(
                "Tests that the method returns true if all constructor's parameter are annotated with @ConstructorArg",
                ConstructorArg::class.java,
                true
            ),
            arrayOf(
                "Tests that the method returns false if not all constructor's parameter are annotated with @NotNull",
                NotNull::class.java,
                false
            )
        )
    }

    /**
     * Tests that the method `getClassType` works as expected.
     * @param testCaseDescription the test case description
     * @param testClass the class to test
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "dataGetClassTypeTesting")
    fun testGetClassTypeWorksAsExpected(
        testCaseDescription: String?,
        testClass: Class<*>?,
        expectedResult: ClassType?
    ) {
        // GIVEN

        // WHEN
        val actual = underTest.getClassType(testClass)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expectedResult)
    }

    /**
     * Creates the parameters to be used for testing the method `getClassType`.
     * @return parameters to be used for testing the method `getClassType`.
     */
    @DataProvider
    private fun dataGetClassTypeTesting(): Array<Array<Any>> {
        return arrayOf(
            arrayOf(
                "Tests that the method returns immutable if the given class is immutable",
                ImmutableToFoo::class.java,
                ClassType.IMMUTABLE
            ),
            arrayOf(
                "Tests that the method returns mutable if the given class is mutable",
                MutableToFoo::class.java,
                ClassType.MUTABLE
            ),
            arrayOf(
                "Tests that the method returns mixed if the given class contains both final and not fields",
                MixedToFoo::class.java,
                ClassType.MIXED
            )
        )
    }

    /**
     * Tests that the method `getSetterMethods` works as expected.
     * @param testCaseDescription the test case description
     * @param testClass the class to test
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "dataGetSetterMethodsTesting")
    fun testGetSetterMethodsWorksAsExpected(
        testCaseDescription: String?,
        testClass: Class<*>?,
        expectedResult: Boolean
    ) {
        // GIVEN

        // WHEN
        val actual = underTest.getSetterMethods(testClass)

        // THEN
        Assertions.assertThat(actual.isEmpty()).isEqualTo(expectedResult)
    }

    /**
     * Creates the parameters to be used for testing the method `getSetterMethods`.
     * @return parameters to be used for testing the method `getSetterMethods`.
     */
    @DataProvider
    private fun dataGetSetterMethodsTesting(): Array<Array<Any>> {
        return arrayOf(
            arrayOf(
                "Tests that the method returns an empty list if the class has no setter methods",
                ImmutableToFooSubClass::class.java,
                true
            ),
            arrayOf(
                "Tests that the method returns a not empty list if the class has setter methods",
                MutableToFoo::class.java,
                false
            )
        )
    }

    /**
     * Tests that the method `getGetterMethods` works as expected.
     * @param testCaseDescription the test case description
     * @param testClass the class to test
     * @param expectedGetterMethods the total expected getter method
     */
    @Test(dataProvider = "dataGetGetterMethodsTesting")
    fun testGetGetterMethodsWorksAsExpected(
        testCaseDescription: String?,
        testClass: Class<*>?,
        expectedGetterMethods: Int
    ) {
        // GIVEN

        // WHEN
        val actual = underTest.getGetterMethods(testClass)

        // THEN
        Assertions.assertThat(actual).hasSize(expectedGetterMethods)
    }

    /**
     * Creates the parameters to be used for testing the method `getGetterMethods`.
     * @return parameters to be used for testing the method `getGetterMethods`.
     */
    @DataProvider
    private fun dataGetGetterMethodsTesting(): Array<Array<Any>> {
        return arrayOf(
            arrayOf(
                "Tests that the method returns an empty list if the class has no getter methods",
                FromFooSimpleNoGetters::class.java,
                ZERO
            ),
            arrayOf(
                "Tests that the method returns only the getter methods discarding the not valid one",
                FromFooAdvFields::class.java,
                FROM_FOO_ADV_FIELD_EXPECTED_GETTER_METHODS
            ),
            arrayOf(
                "Tests that the method returns the boolean getter method too",
                FromFooSimple::class.java,
                FROM_FOO_SIMPLE_EXPECTED_GETTER_METHODS
            ),
            arrayOf(
                "Tests that the method returns the getter methods from parent class too",
                FromFooSubClass::class.java,
                FROM_FOO_SUB_CLASS_EXPECTED_GETTER_METHODS
            )
        )
    }

    /**
     * Tests that the method `getDefaultTypeValue` works as expected.
     */
    @Test
    fun testGetDefaultTypeValueWorksAsExpected() {
        // GIVEN

        // WHEN
        val actual = underTest.getDefaultTypeValue(Int::class.java)

        // THEN
        Assertions.assertThat(actual).isEqualTo(EXPECTED_DEFAULT_VALUE)
    }

    /**
     * Creates the parameters to be used for testing the method `usesBuilderPattern`.
     * @return parameters to be used for testing the method `usesBuilderPattern`.
     */
    @DataProvider
    private fun dataUsesBuilderPatternTesting(): Array<Array<Any>> {
        return arrayOf(
            arrayOf(
                "Tests that the method returns true if the class has a builder",
                MutableToFooWithBuilder::class.java,
                true
            ),
            arrayOf("Tests that the method returns false if the class hasn't a builder", FromFoo::class.java, false)
        )
    }

    /**
     * Tests that the method `hasAccessibleConstructors` works as expected.
     * @param testCaseDescription the test case description
     * @param testClass the class to test
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "dataHasAccessibleConstructorsTesting")
    fun testHasAccessibleConstructorsWorksAsExpected(
        testCaseDescription: String?,
        testClass: Class<*>?,
        expectedResult: Boolean
    ) {
        // GIVEN

        // WHEN
        val actual = underTest.hasAccessibleConstructors(testClass)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expectedResult)
    }

    /**
     * Creates the parameters to be used for testing the method `hasAccessibleConstructors`.
     * @return parameters to be used for testing the method `hasAccessibleConstructors`.
     */
    @DataProvider
    private fun dataHasAccessibleConstructorsTesting(): Array<Array<Any>> {
        return arrayOf(
            arrayOf(
                "Tests that the method returns false if the constructor is public",
                FromFoo::class.java,
                true
            ),
            arrayOf(
                "Tests that the method returns false if the constructor is private",
                MutableToFooWithBuilder::class.java,
                false
            )
        )
    }

    /**
     * Tests that the method `isString` returns the expected value.
     * @param testCaseDescription the test case description
     * @param testClass the class to test
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "dataIsStringTesting")
    fun testIsStringWorksAsExpected(testCaseDescription: String?, testClass: Class<*>?, expectedResult: Boolean) {
        // GIVEN

        // WHEN
        val actual = ClassUtils.isString(testClass)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expectedResult)
    }

    /**
     * Creates the parameters to be used for testing the method `isString`.
     * @return parameters to be used for testing the method `isString`.
     */
    @DataProvider
    private fun dataIsStringTesting(): Array<Array<Any>> {
        return arrayOf(
            arrayOf("Tests that the method returns true if the class is a String", String::class.java, true),
            arrayOf("Tests that the method returns false if the class is not a String", BigDecimal::class.java, false)
        )
    }

    /**
     * Tests that the method `isBigInteger` returns the expected value.
     * @param testCaseDescription the test case description
     * @param testClass the class to test
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "dataIsBigIntegerTesting")
    fun testIsBigIntegerWorksAsExpected(testCaseDescription: String?, testClass: Class<*>?, expectedResult: Boolean) {
        // GIVEN

        // WHEN
        val actual = ClassUtils.isBigInteger(testClass)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expectedResult)
    }

    /**
     * Creates the parameters to be used for testing the method `isBigInteger`.
     * @return parameters to be used for testing the method `isBigInteger`.
     */
    @DataProvider
    private fun dataIsBigIntegerTesting(): Array<Array<Any>> {
        return arrayOf(
            arrayOf(
                "Tests that the method returns true if the class is a BigInteger",
                BigInteger::class.java,
                true
            ),
            arrayOf(
                "Tests that the method returns false if the class is not a BigInteger",
                BigDecimal::class.java,
                false
            )
        )
    }

    /**
     * Tests that the method `isBigDecimal` returns the expected value.
     * @param testCaseDescription the test case description
     * @param testClass the class to test
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "dataIsBigDecimalTesting")
    fun testIsBigDecimalWorksAsExpected(testCaseDescription: String?, testClass: Class<*>?, expectedResult: Boolean) {
        // GIVEN

        // WHEN
        val actual = ClassUtils.isBigDecimal(testClass)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expectedResult)
    }

    /**
     * Creates the parameters to be used for testing the method `isBigDecimal`.
     * @return parameters to be used for testing the method `isBigDecimal`.
     */
    @DataProvider
    private fun dataIsBigDecimalTesting(): Array<Array<Any>> {
        return arrayOf(
            arrayOf(
                "Tests that the method returns true if the class is a BigDecimal",
                BigDecimal::class.java,
                true
            ),
            arrayOf(
                "Tests that the method returns false if the class is not a BigDecimal",
                BigInteger::class.java,
                false
            )
        )
    }

    /**
     * Tests that the method `isByteArray` returns the expected value.
     * @param testCaseDescription the test case description
     * @param testClass the class to test
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "dataIsByteArrayTesting")
    fun testIsBiteArrayWorksAsExpected(testCaseDescription: String?, testClass: Class<*>?, expectedResult: Boolean) {
        // GIVEN

        // WHEN
        val actual = ClassUtils.isByteArray(testClass)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expectedResult)
    }

    /**
     * Creates the parameters to be used for testing the method `isByteArray`.
     * @return parameters to be used for testing the method `isByteArray`.
     */
    @DataProvider
    private fun dataIsByteArrayTesting(): Array<Array<Any?>> {
        return arrayOf(
            arrayOf(
                "Tests that the method returns true if the class is a byte[]",
                ByteArray::class.java,
                true
            ),
            arrayOf(
                "Tests that the method returns false if the class is not a byte[]",
                Byte::class.javaPrimitiveType,
                false
            )
        )
    }

    /**
     * Tests that the method `getConcreteClass` works as expected.
     * @throws Exception if the field is not retrieved
     */
    @Test
    @Throws(Exception::class)
    fun testGetFieldClassWorksAsExpected() {
        // GIVEN
        val fromFoo = createFromFoo()
        val listField = getField(fromFoo, LIST_FIELD_NAME)

        // WHEN
        val actual = underTest.getFieldClass(listField, fromFoo)

        // THEN
        Assertions.assertThat(actual).isEqualTo(LinkedList::class.java)
    }

    /**
     * Tests that the method `getConcreteClass` returns the expected value.
     * @param testCaseDescription the test case description
     * @param field the class field for which the concrete class has to be retrieved
     * @param fieldValue the field value
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "dataGetConcreteClassTesting")
    fun testGetConcreteClassWorksAsExpected(
        testCaseDescription: String?,
        field: Field?,
        fieldValue: Any?,
        expectedResult: Class<*>?
    ) {
        // GIVEN

        // WHEN
        val actual = underTest.getConcreteClass(field, fieldValue)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expectedResult)
    }

    /**
     * Creates the parameters to be used for testing the method `getConcreteClass`.
     * @return parameters to be used for testing the method `getConcreteClass`.
     * @throws Exception if something goes wrong
     */
    @DataProvider
    @Throws(Exception::class)
    private fun dataGetConcreteClassTesting(): Array<Array<Any?>> {
        val listField = getField(createFromFoo(), LIST_FIELD_NAME)
        return arrayOf(
            arrayOf(
                "Tests that the method returns Object if the field value is null",
                listField,
                null,
                Any::class.java
            ),
            arrayOf(
                "Tests that the method returns LinkedList if the concrete field class is a LinkedList",
                listField,
                LINKED_LIST,
                LINKED_LIST.javaClass
            )
        )
    }

    /**
     * Test that the a [MissingMethodException] is raised if the Builder class has no `build()` method.
     * @param testCaseDescription the test case description
     * @param containerTestClass the test class containing the Builder
     * @param builderClass the Builder nested class
     */
    @Test(dataProvider = "dataGetBuilderMethodExceptionTesting", expectedExceptions = [MissingMethodException::class])
    fun testGetBuildMethodThrowsExceptionIfMethodIsMissing(
        testCaseDescription: String?,
        containerTestClass: Class<*>?,
        builderClass: Class<*>?
    ) {
        // GIVEN

        // WHEN
        underTest.getBuildMethod(containerTestClass, builderClass)
    }

    /**
     * Creates the parameters to be used for testing the method `getBuildMethod`.
     * @return parameters to be used for testing the method `getBuildMethod`.
     */
    @DataProvider
    private fun dataGetBuilderMethodExceptionTesting(): Array<Array<Any>> {
        return arrayOf(
            arrayOf(
                "Tests that the method raises a MissingMethodException if the class has no builder build method",
                ImmutableToFoo::class.java,
                ImmutableToFoo::class.java
            ),
            arrayOf(
                "Tests that the method raises a MissingMethodException if the class has a builder build method that does not return the parent class",
                MutableToFooWithWrongBuilder::class.java, MutableToFooWithWrongBuilder.Builder::class.java
            )
        )
    }

    /**
     * Test that the [getBuildMethod][ClassUtils.getBuildMethod] returns the Builder build method.
     */
    @Test
    fun testGetBuildMethodReturnsTheBuildMethod() {
        // GIVEN

        // WHEN
        val actual =
            underTest.getBuildMethod(MutableToFooWithBuilder::class.java, MutableToFooWithBuilder.Builder::class.java)

        // THEN
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual.name).isEqualTo(ClassUtils.BUILD_METHOD_NAME)
        Assertions.assertThat(actual).hasFieldOrPropertyWithValue(NAME_FIELD_NAME, ClassUtils.BUILD_METHOD_NAME)
    }

    /**
     * Tests that the method [getBuilderClass][ClassUtils.getBuilderClass] returns the expected value.
     * @param testCaseDescription the test case description
     * @param testClass the class for which the builder class has to be retrieved
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "dataGetBuilderClassTesting")
    fun testGetConcreteClassWorksAsExpected(
        testCaseDescription: String?,
        testClass: Class<*>?,
        expectedResult: Optional<Class<*>?>?
    ) {
        // GIVEN

        // WHEN
        val actual = underTest.getBuilderClass(testClass)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expectedResult)
    }

    /**
     * Creates the parameters to be used for testing the method `getBuilderClass`.
     * @return parameters to be used for testing the method `getBuilderClass`.
     */
    @DataProvider
    private fun dataGetBuilderClassTesting(): Array<Array<Any>> {
        return arrayOf(
            arrayOf(
                "Tests that the method returns the builder class", MutableToFooWithBuilder::class.java, Optional.of(
                    MutableToFooWithBuilder.Builder::class.java
                )
            ),
            arrayOf(
                "Tests that the method returns an empty optional if the class has no builder",
                ImmutableToFoo::class.java,
                Optional.empty<Any>()
            ),
            arrayOf(
                "Tests that the method returns an empty optional if the class has a wrong builder",
                MutableToFooWithWrongBuilder::class.java,
                Optional.empty<Any>()
            )
        )
    }

    /**
     * Returns the class field with the given name.
     * @param objectInstance the class containing the field
     * @param fieldName the name of the field to retrieve
     * @param <T> the object instance type
     * @return the class field with the given name
     * @throws NoSuchFieldException if the field is missing
    </T> */
    @Throws(NoSuchFieldException::class)
    private fun <T> getField(objectInstance: T, fieldName: String): Field {
        val field: Field = objectInstance!!::class.java.javaClass.getDeclaredField(fieldName)
        field.isAccessible = true
        return field
    }

    /**
     * Creates a [FromFoo].
     * @return a [FromFoo] instance
     */
    private fun createFromFoo(): FromFoo {
        return FromFoo(NAME_FIELD_NAME, null, null, LINKED_LIST, null)
    }

    companion object {
        private val CLASS_WITHOUT_PRIVATE_FINAL_FIELDS = MutableToFooSubClass::class.java
        private val CLASS_WITH_PRIVATE_FINAL_FIELDS = ImmutableToFoo::class.java
        private const val EXPECTED_PRIVATE_FINAL_FIELDS = 5
        private const val EXPECTED_MIXED_CLASS_TOTAL_PRIVATE_FIELDS = 4
        private val CLASS_WITH_PRIVATE_AND_PUBLIC_FIELDS = MixedToFoo::class.java
        private const val EXPECTED_MIXED_CLASS_TOTAL_NOT_FINAL_FIELDS = 2
        private const val EXPECTED_MIXED_CLASS_TOTAL_PRIVATE_NOT_FINAL_FIELDS = 1
        private val CLASS_WITH_STATIC_FIELDS = MixedToFooStaticField::class.java
        private val CLASS_WITHOUT_CONSTRUCTOR = MixedToFooMissingConstructor::class.java
        private const val EXPECTED_NOT_STATIC_FIELDS = 1
        private const val NAME_FIELD_NAME = "name"
        private const val EXPECTED_CLASS_PARAMETERS = 5
        private const val NOT_EXISTING_FIELD_NAME = "notExistingFieldName"
        private val CLASS_WITH_PRIVATE_FINAL_FIELDS_AND_SUB_CLASS = ImmutableToFooSubClass::class.java
        private const val EXPECTED_SUB_CLASS_PRIVATE_FIELDS = 9
        private const val EXPECTED_DEFAULT_VALUE = 0
        private val IS_FINAL_FIELD_PREDICATE = Predicate { field: Field -> Modifier.isFinal(field.modifiers) }
        private const val ZERO = 0
        private val PRIMITIVE_INT_ARRAY = intArrayOf()
        private val PRIMITIVE_SHORT_ARRAY = shortArrayOf()
        private val PRIMITIVE_CHAR_ARRAY = charArrayOf()
        private val PRIMITIVE_BYTE_ARRAY = byteArrayOf()
        private val PRIMITIVE_DOUBLE_ARRAY = doubleArrayOf()
        private val PRIMITIVE_FLOAT_ARRAY = floatArrayOf()
        private val PRIMITIVE_LONG_ARRAY = longArrayOf()
        private val PRIMITIVE_INTEGER_ARRAY = arrayOf<Int>()
        private val NOT_PRIMITIVE_ARRAY = arrayOf<FromFoo>()
        private const val FROM_FOO_ADV_FIELD_EXPECTED_GETTER_METHODS = 11
        private const val FROM_FOO_SIMPLE_EXPECTED_GETTER_METHODS = 3
        private const val FROM_FOO_SUB_CLASS_EXPECTED_GETTER_METHODS = 9
        private val LINKED_LIST = LinkedList<String?>()
        private const val LIST_FIELD_NAME = "list"
    }
}