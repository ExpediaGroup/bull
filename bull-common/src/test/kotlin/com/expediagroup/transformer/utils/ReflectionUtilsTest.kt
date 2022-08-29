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

import com.expediagroup.beans.sample.FromFoo
import com.expediagroup.beans.sample.FromFooMap
import com.expediagroup.beans.sample.FromFooSimple
import com.expediagroup.beans.sample.FromFooSimpleNoGetters
import com.expediagroup.beans.sample.FromFooSubClass
import com.expediagroup.beans.sample.FromSubFoo
import com.expediagroup.beans.sample.immutable.ImmutableToFoo
import com.expediagroup.beans.sample.immutable.ImmutableToFooAdvFields
import com.expediagroup.beans.sample.immutable.ImmutableToSubFoo
import com.expediagroup.beans.sample.mutable.MutableToFoo
import com.expediagroup.beans.sample.mutable.MutableToFooAdvFields
import com.expediagroup.beans.sample.mutable.MutableToFooSimple
import com.expediagroup.transformer.constant.MethodPrefix
import com.expediagroup.transformer.error.MissingFieldException
import com.expediagroup.transformer.error.MissingMethodException
import com.expediagroup.transformer.model.ItemType
import com.expediagroup.transformer.model.MapElemType
import com.expediagroup.transformer.model.MapType
import org.assertj.core.api.Assertions
import org.assertj.core.api.ThrowableAssert.ThrowingCallable
import org.mockito.InjectMocks
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.testng.annotations.BeforeClass
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.lang.reflect.Parameter
import java.lang.reflect.UndeclaredThrowableException
import java.math.BigInteger
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

/**
 * Unit tests fro class: [ReflectionUtils].
 */
class ReflectionUtilsTest {
    /**
     * The class to be tested.
     */
    @InjectMocks
    private val underTest: ReflectionUtils? = null

    /**
     * Initializes mock.
     */
    @BeforeClass
    fun beforeClass() {
        MockitoAnnotations.openMocks(this)
    }

    /**
     * Tests that the method `getFieldValue` returns the expected value.
     * @param testCaseDescription the test case description
     * @param beanObject the java bean from which the value has to be retrieved
     * @param fieldName the name of the field to retrieve
     * @param fieldType the type of the field to retrieve
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "dataGetFieldValueTesting")
    fun testGetFieldValueWorksAsExpected(
        testCaseDescription: String?, beanObject: Any?,
        fieldName: String?, fieldType: Class<*>?, expectedResult: Any?
    ) {
        // GIVEN

        // WHEN
        val actual = underTest!!.getFieldValue(beanObject, fieldName, fieldType)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expectedResult)
    }

    /**
     * Tests that the method `getFieldValue` with a a given field returns the expected value.
     * @throws Exception in case of issues
     */
    @Test
    @Throws(Exception::class)
    fun testGetFieldValueWithAGivenFieldWorksAsExpected() {
        // GIVEN
        val fromFooSimpleNoGetters = createFromFooSimpleNoGetters()
        val field = FromFooSimpleNoGetters::class.java.getDeclaredField(ID_FIELD_NAME)

        // WHEN
        val actual = underTest!!.getFieldValue(fromFooSimpleNoGetters, field)

        // THEN
        Assertions.assertThat(actual).isEqualTo(BigInteger.ZERO)
    }

    /**
     * Creates the parameters to be used for testing the method `getFieldValue`.
     * @return parameters to be used for testing the method `getFieldValue`.
     */
    @DataProvider
    private fun dataGetFieldValueTesting(): Array<Array<Any?>> {
        val mutableToFoo = createMutableToFoo(BigInteger.ZERO)
        return arrayOf(
            arrayOf(
                "Tests that the method returns the field value",
                mutableToFoo,
                ID_FIELD_NAME,
                BigInteger::class.java,
                BigInteger.ZERO
            ), arrayOf(
                "Tests that the method returns null if the required field is inside a null object",
                mutableToFoo,
                NESTED_OBJECT_NAME_FIELD_NAME,
                String::class.java,
                null
            ), arrayOf(
                "Tests that the method returns the field value even if there is no getter method defined",
                createFromFooSimpleNoGetters(),
                ID_FIELD_NAME,
                BigInteger::class.java,
                BigInteger.ZERO
            )
        )
    }

    /**
     * Tests that the method [getFieldValue][ReflectionUtils.getFieldValue] catches a runtime exception.
     */
    @Test
    fun testGetFieldValueCatchesRuntimeException() {
        // GIVEN
        val mutableToFoo = createMutableToFoo(null)
        val underTestMock = Mockito.spy(ReflectionUtils::class.java)
        Mockito.`when`(underTestMock.getDeclaredFieldType(LIST_FIELD_NAME, MutableToFoo::class.java))
            .thenThrow(RuntimeException())

        // WHEN
        val actual = underTestMock.getFieldValue(mutableToFoo, LIST_FIELD_NAME, FromFooSubClass::class.java)

        // THEN
        Assertions.assertThat(actual).isNull()
        Mockito.verify(underTestMock, Mockito.times(1)).getDeclaredField(LIST_FIELD_NAME, MutableToFoo::class.java)
    }

    /**
     * Tests that the method `getFieldValue` throws Exception if the field does not exists.
     */
    @Test(expectedExceptions = [MissingFieldException::class])
    fun testGetFieldValueThrowsExceptionIfTheFieldDoesNotExists() {
        // GIVEN
        val mutableToFoo = createMutableToFoo(null)

        // WHEN
        underTest!!.getFieldValue(mutableToFoo, NOT_EXISTING_FIELD_NAME, Any::class.java)
    }

    /**
     * Tests that the method `getFieldValueDirectAccess` returns the expected value.
     * @throws Exception if an error occurs
     */
    @Test
    @Throws(Exception::class)
    fun testGetFieldValueDirectAccessWorksAsExpected() {
        // GIVEN
        val mutableToFoo = createMutableToFoo(BigInteger.ZERO)
        val getFieldValueDirectAccessMethod = ReflectionUtils::class.java.getDeclaredMethod(
            GET_FIELD_VALUE_DIRECT_ACCESS_METHOD_NAME, Any::class.java, String::class.java
        )
        getFieldValueDirectAccessMethod.isAccessible = true

        // WHEN
        val actual = getFieldValueDirectAccessMethod.invoke(underTest, mutableToFoo, ID_FIELD_NAME)

        // THEN
        Assertions.assertThat(actual).isEqualTo(BigInteger.ZERO)
    }

    /**
     * Tests that the method `getFieldValueDirectAccess` throws Exception in specific cases.
     * @param testCaseDescription the test case description
     * @param beanObject the java bean from which the value has to be retrieved
     * @param fieldName the name of the field to retrieve
     * @throws Exception if an error occurs
     */
    @Test(
        dataProvider = "dataGetFieldValueDirectAccessTesting",
        expectedExceptions = [InvocationTargetException::class]
    )
    @Throws(
        Exception::class
    )
    fun testGetFieldValueDirectAccessThrowsExceptionIfTheFieldDoesNotExists(
        testCaseDescription: String?,
        beanObject: Any?, fieldName: String?
    ) {
        // GIVEN
        val getFieldValueDirectAccessMethod = ReflectionUtils::class.java.getDeclaredMethod(
            GET_FIELD_VALUE_DIRECT_ACCESS_METHOD_NAME, Any::class.java, String::class.java
        )
        getFieldValueDirectAccessMethod.isAccessible = true

        // WHEN
        getFieldValueDirectAccessMethod.invoke(underTest, beanObject, fieldName)
    }

    /**
     * Creates the parameters to be used for testing the method `getFieldValueDirectAccess`.
     * @return parameters to be used for testing the method `getFieldValueDirectAccess`.
     */
    @DataProvider
    private fun dataGetFieldValueDirectAccessTesting(): Array<Array<Any?>> {
        val mutableToFoo = createMutableToFoo(null)
        return arrayOf(
            arrayOf(
                "Tests that the method throws Exception if the field does not exists",
                mutableToFoo,
                NOT_EXISTING_FIELD_NAME
            ),
            arrayOf("Tests that the method throws Exception if the bean is null", null, NESTED_OBJECT_NAME_FIELD_NAME)
        )
    }

    /**
     * Tests that the method `handleReflectionException` raises the expected exception.
     */
    @Test
    fun testHandleReflectionExceptionThrowsMissingMethodExceptionWhenGivenExceptionIsNoSuchMethodException() {
        // GIVEN
        val noSuchMethodException = NoSuchMethodException()

        // WHEN
        val actual = underTest!!.handleReflectionException(noSuchMethodException)

        // THEN
        Assertions.assertThat(actual).isInstanceOf(MissingMethodException::class.java)
    }

    /**
     * Tests that the method `handleReflectionException` returns the expected exception.
     */
    @Test
    fun testHandleReflectionExceptionThrowsIllegalStateExceptionWhenGivenExceptionIsIllegalAccessException() {
        // GIVEN
        val illegalAccessException = IllegalAccessException()

        // WHEN
        val actual = underTest!!.handleReflectionException(illegalAccessException)

        // THEN
        Assertions.assertThat(actual).isInstanceOf(IllegalStateException::class.java)
    }

    /**
     * Tests that the method `handleReflectionException` returns the expected exception.
     */
    @Test
    fun testHandleReflectionExceptionThrowsRuntimeExceptionWhenGivenExceptionIsRuntimeException() {
        // GIVEN
        val runtimeException = RuntimeException()

        // WHEN
        val actual = underTest!!.handleReflectionException(runtimeException)

        // THEN
        Assertions.assertThat(actual).isInstanceOf(RuntimeException::class.java)
    }

    /**
     * Tests that the method `handleReflectionException` returns the expected exception.
     */
    @Test
    fun testHandleReflectionExceptionThrowsUndeclaredThrowableExceptionWhenGivenExceptionIsInvalidBeanException() {
        // GIVEN
        val genericException = Exception()

        // WHEN
        val actual = underTest!!.handleReflectionException(genericException)

        // THEN
        Assertions.assertThat(actual).isInstanceOf(UndeclaredThrowableException::class.java)
    }

    /**
     * Tests that the method `getMapGenericType` throws Exception when the given type is not a map.
     */
    @Test(expectedExceptions = [IllegalArgumentException::class])
    fun testGetMapGenericTypeThrowsIllegalArgumentExceptionWhenTheGivenTypeIsNotAMap() {
        // GIVEN

        // WHEN
        underTest!!.getMapGenericType(MutableList::class.java, null, LIST_FIELD_NAME)
    }

    /**
     * Tests that the method `getGetterMethodPrefix` returns the expected value.
     * @param testCaseDescription the test case description
     * @param testClass the class to test
     * @param expectedResult the expected result
     * @throws Exception if something goes wrong
     */
    @Test(dataProvider = "dataGetGetterMethodPrefixTesting")
    @Throws(Exception::class)
    fun testGetGetterMethodPrefixWorksAsExpected(
        testCaseDescription: String?,
        testClass: Class<*>?,
        expectedResult: String?
    ) {
        // GIVEN
        val method = underTest!!.javaClass.getDeclaredMethod(GETTER_METHOD_PREFIX_METHOD_NAME, Class::class.java)
        method.isAccessible = true

        // WHEN
        val actual = method.invoke(underTest, testClass) as String

        // THEN
        Assertions.assertThat(actual).isEqualTo(expectedResult)
    }

    /**
     * Creates the parameters to be used for testing the method `getGetterMethodPrefix`.
     * @return parameters to be used for testing the method `getGetterMethodPrefix`.
     * @throws Exception if the method does not exists or the invoke fails.
     */
    @DataProvider
    @Throws(Exception::class)
    private fun dataGetGetterMethodPrefixTesting(): Array<Array<Any>> {
        return arrayOf(
            arrayOf(
                "Tests that the method returns the prefix: 'get' in case the returned class is a String ",
                String::class.java,
                MethodPrefix.GET.prefix
            ),
            arrayOf(
                "Tests that the method returns the prefix: 'is' in case the returned class is a Boolean",
                Boolean::class.java,
                MethodPrefix.IS.prefix
            ),
            arrayOf(
                "Tests that the method returns the prefix: 'is' in case the returned class is a primitive boolean",
                FromFooSubClass::class.java.getDeclaredField(CHECK_FIELD_NAME).type, MethodPrefix.IS.prefix
            )
        )
    }

    /**
     * Tests that the method `getFieldAnnotation` returns the proper annotation.
     * @param testCaseDescription the test case description
     * @param annotationToGet the annotation to retrieve
     * @param expectNull true if it's expected to find it null, false otherwise
     */
    @Test(dataProvider = "dataGetFieldAnnotationTesting")
    fun testGetFieldAnnotationWorksProperly(
        testCaseDescription: String?, annotationToGet: Class<out Annotation?>?,
        expectNull: Boolean
    ) {
        // GIVEN
        val nameField = underTest!!.getDeclaredField(ID_FIELD_NAME, ImmutableToFoo::class.java)

        // WHEN
        val actual = underTest.getFieldAnnotation(nameField, annotationToGet)!!

        // THEN
        Assertions.assertThat(Objects.isNull(actual)).isEqualTo(expectNull)
    }

    /**
     * Creates the parameters to be used for testing the method `getFieldAnnotation`.
     * @return parameters to be used for testing the method `getFieldAnnotation`.
     */
    @DataProvider
    private fun dataGetFieldAnnotationTesting(): Array<Array<Any>> {
        return arrayOf(
            arrayOf(
                "Tests that the method returns the field's annotation: 'NotNull' for the given field",
                NotNull::class.java,
                false
            ),
            arrayOf(
                "Tests that the method returns: null in case the searched annotation does not exists on the given field",
                NotBlank::class.java,
                true
            )
        )
    }

    /**
     * Tests that the method `getSetterMethodForField` returns the set method.
     */
    @Test
    fun testGetSetterMethodForFieldWorksProperly() {
        // GIVEN

        // WHEN
        val actual =
            underTest!!.getSetterMethodForField(MutableToFoo::class.java, ID_FIELD_NAME, BigInteger::class.java)

        // THEN
        Assertions.assertThat(actual).hasFieldOrPropertyWithValue(NAME_FIELD_NAME, EXPECTED_SETTER_METHOD_NAME)
    }

    /**
     * Tests that the method `getSetterMethodForField` raises a MissingMethodException if the setter method for the given field does not exists.
     */
    @Test(expectedExceptions = [MissingMethodException::class])
    fun testGetSetterMethodForFieldThrowsExceptionIfTheMethodDoesNotExists() {
        // GIVEN

        // WHEN
        underTest!!.getSetterMethodForField(MutableToFoo::class.java, NOT_EXISTING_FIELD_NAME, BigInteger::class.java)
    }

    /**
     * Tests that the method `getParameterAnnotations` returns the annotation when the parameter has an annotation with the given type.
     */
    @Test
    fun testGetParameterAnnotationReturnsTheAnnotationIfExists() {
        // GIVEN
        val notNullAnnotation = Mockito.mock(
            NotNull::class.java
        )
        val parameter = Mockito.mock(Parameter::class.java)
        Mockito.`when`(parameter.isAnnotationPresent(NotNull::class.java)).thenReturn(true)
        Mockito.`when`(
            parameter.getAnnotation(
                NotNull::class.java
            )
        ).thenReturn(notNullAnnotation)

        // WHEN
        val actual: Annotation =
            underTest!!.getParameterAnnotation(parameter, NotNull::class.java, DECLARING_CLASS_NAME)

        // THEN
        Assertions.assertThat(actual).isEqualTo(notNullAnnotation)
    }

    /**
     * Tests that the method `getParameterAnnotations` returns null when the parameter has no annotation with the given type.
     */
    @Test
    fun testGetParameterAnnotationReturnsNullIfTheAnnotationDoesNotExists() {
        // GIVEN
        val parameter = Mockito.mock(Parameter::class.java)
        Mockito.`when`(parameter.isAnnotationPresent(NotNull::class.java)).thenReturn(false)

        // WHEN
        val actual: Annotation =
            underTest!!.getParameterAnnotation(parameter, NotNull::class.java, DECLARING_CLASS_NAME)

        // THEN
        Assertions.assertThat(actual).isNull()
    }

    /**
     * Tests that the method `getSetterMethodForField` works properly.
     * @throws Exception if something goes wrong.
     */
    @Test
    @Throws(Exception::class)
    fun testInvokeMethodWorksProperly() {
        // GIVEN
        val mutableToFoo = createMutableToFoo(null)
        val idSetterMethod =
            underTest!!.getSetterMethodForField(MutableToFoo::class.java, ID_FIELD_NAME, BigInteger::class.java)

        // WHEN
        getMethod(
            underTest.javaClass,
            INVOKE_METHOD_NAME,
            true,
            Method::class.java,
            Any::class.java,
            Array<Any>::class.java
        )
            .invoke(underTest, idSetterMethod, mutableToFoo, arrayOf<Any>(BigInteger.ONE))

        // THEN
        Assertions.assertThat(mutableToFoo).hasFieldOrPropertyWithValue(ID_FIELD_NAME, BigInteger.ONE)
    }

    /**
     * Tests that the method `getSetterMethodForField` raises an [InvocationTargetException] if the argument is wrong.
     */
    @Test
    fun testInvokeMethodRaisesAnIllegalArgumentExceptionIfTheArgumentIsWrong() {
        // GIVEN
        val mutableToFoo = createMutableToFoo(null)
        val idSetterMethod =
            underTest!!.getSetterMethodForField(MutableToFoo::class.java, LIST_FIELD_NAME, MutableList::class.java)

        // WHEN
        val actual = ThrowingCallable {
            getMethod(
                underTest.javaClass,
                INVOKE_METHOD_NAME,
                true,
                Method::class.java,
                Any::class.java,
                Array<Any>::class.java
            )
                .invoke(underTest, idSetterMethod, mutableToFoo, arrayOf<Any>(BigInteger.ONE))
        }

        // THEN
        Assertions.assertThatThrownBy(actual).hasCauseInstanceOf(IllegalArgumentException::class.java)
    }

    /**
     * Tests that the method `getDeclaredField` works properly.
     * @param testCaseDescription the test case description
     * @param fieldName the field to retrieve
     * @param targetClass the class where the field has to be searched
     */
    @Test(dataProvider = "dataGetDeclaredFieldTesting")
    fun testGetDeclaredFieldWorksProperly(testCaseDescription: String?, fieldName: String?, targetClass: Class<*>?) {
        // GIVEN

        // WHEN
        val actual = underTest!!.getDeclaredField(fieldName, targetClass)

        // THEN
        Assertions.assertThat(actual).isNotNull
    }

    /**
     * Test that the method: `getClassDeclaredField` throws the right exception.
     */
    @Test
    fun testGetClassDeclaredFieldThrowsTheRightException() {
        // GIVEN

        // WHEN
        val actualException = Assertions.catchThrowable {
            val getClassDeclaredFieldMethod = underTest!!.javaClass.getDeclaredMethod(
                GET_CLASS_DECLARED_FIELD_METHOD_NAME,
                String::class.java,
                Class::class.java
            )
            getClassDeclaredFieldMethod.isAccessible = true
            getClassDeclaredFieldMethod.invoke(underTest, null, FromFoo::class.java)
        }

        // THEN
        Assertions.assertThat(actualException).hasCauseInstanceOf(NullPointerException::class.java)
    }

    /**
     * Creates the parameters to be used for testing the method `getDeclaredField`.
     * @return parameters to be used for testing the method `getDeclaredField`.
     */
    @DataProvider
    private fun dataGetDeclaredFieldTesting(): Array<Array<Any>> {
        return arrayOf(
            arrayOf(
                "Tests that the method returns the class field from first object",
                ID_FIELD_NAME,
                FromFooSubClass::class.java
            ),
            arrayOf(
                "Tests that the method returns the class field from a nested object",
                NESTED_OBJECT_NAME_FIELD_NAME,
                MutableToFoo::class.java
            )
        )
    }

    /**
     * Tests that the method `getDeclaredField` throws a [MissingFieldException] if the field does not exists.
     */
    @Test(expectedExceptions = [MissingFieldException::class])
    fun testGetDeclaredFieldRaisesAnExceptionIfTheFieldDoesNotExists() {
        // GIVEN

        // WHEN
        val actual = underTest!!.getDeclaredField(NOT_EXISTING_FIELD_NAME, FromFooSubClass::class.java)

        // THEN
        Assertions.assertThat(actual).isNotNull
    }

    /**
     * Tests that the method `getDeclaredFieldType` works properly.
     */
    @Test
    fun testGetDeclaredFieldTypeWorksProperly() {
        // GIVEN

        // WHEN
        val actual = underTest!!.getDeclaredFieldType(ID_FIELD_NAME, FromFooSubClass::class.java)

        // THEN
        Assertions.assertThat(actual).isNotNull
    }

    /**
     * Tests that the method `getGenericFieldType` works properly.
     * @param testCaseDescription the test case description
     * @param fieldName the field name for which the generic type has to be retrieved
     * @param fieldClass the class owning the field
     * @param expectedType the expected generic class type
     */
    @Test(dataProvider = "dataGetGenericFieldTypeTesting")
    fun testGetGenericFieldTypeWorksProperly(
        testCaseDescription: String?,
        fieldName: String?,
        fieldClass: Class<*>?,
        expectedType: Class<*>?
    ) {
        // GIVEN
        val field = underTest!!.getDeclaredField(fieldName, fieldClass)

        // WHEN
        val actual = underTest.getGenericFieldType(field)

        // THEN
        Assertions.assertThat(actual).isEqualTo(expectedType)
    }

    /**
     * Creates the parameters to be used for testing the method `getGenericFieldType`.
     * @return parameters to be used for testing the method `getGenericFieldType`.
     */
    @DataProvider
    private fun dataGetGenericFieldTypeTesting(): Array<Array<Any>> {
        return arrayOf(
            arrayOf(
                "Tests that the method returns a type String.",
                LIST_FIELD_NAME,
                ImmutableToFoo::class.java,
                String::class.java
            ),
            arrayOf(
                "Tests that the method returns a type Object in case of wildcard types.",
                LIST_FIELD_NAME,
                ImmutableToFooAdvFields::class.java,
                Any::class.java
            )
        )
    }

    /**
     * Tests that the method `getArrayType` works properly.
     */
    @Test
    fun testGetArrayTypeWorksProperly() {
        // GIVEN
        val phoneNumbersField = underTest!!.getDeclaredField(PHONE_NUMBERS_FIELD_NAME, FromSubFoo::class.java)

        // WHEN
        val actual = underTest.getArrayType(phoneNumbersField)

        // THEN
        Assertions.assertThat(actual).isEqualTo(Int::class.javaPrimitiveType)
    }

    /**
     * Tests that the method `getGenericFieldType` works properly.
     */
    @Test
    fun testMapGenericFieldTypeWorksProperly() {
        // GIVEN
        val keyType: MapElemType = ItemType.builder()
            .objectClass(String::class.java)
            .build()
        val expectedElemType = MapType(keyType, keyType)
        val expectedMapType = MapType(keyType, expectedElemType)
        val field = underTest!!.getDeclaredField(VERY_COMPLEX_MAP_FIELD_NAME, ImmutableToSubFoo::class.java)

        // WHEN
        val actual = underTest.getMapGenericType(field.genericType, field.declaringClass.name, field.name)

        // THEN
        Assertions.assertThat(actual).usingRecursiveComparison()
            .isEqualTo(expectedMapType)
    }

    @Test
    fun testMapGenericFieldTypeWorksProperlyForUnparametrizedMap() {
        // GIVEN
        val keyType: MapElemType = ItemType.builder()
            .objectClass(MutableMap::class.java)
            .build()
        val expectedMapType = MapType(keyType, keyType)
        val field = underTest!!.getDeclaredField(UNPARAMETRIZED_MAP_FIELD_NAME, FromFooMap::class.java)

        // WHEN
        val actual = underTest.getMapGenericType(field.genericType, field.declaringClass.name, field.name)

        // THEN
        Assertions.assertThat(actual).usingRecursiveComparison()
            .isEqualTo(expectedMapType)
    }

    /**
     * Tests that the method `getGetterMethod` works properly.
     * @throws Exception in case an error occurs
     */
    @Test
    @Throws(Exception::class)
    fun testGetGetterMethodWorksProperly() {
        // GIVEN
        val methodUnderTest = getMethod(
            underTest!!.javaClass,
            GET_GETTER_METHOD_NAME,
            true,
            Class::class.java,
            String::class.java,
            Class::class.java
        )

        // WHEN
        val actual = methodUnderTest.invoke(underTest, FromFooSimple::class.java, ID_FIELD_NAME, BigInteger::class.java)

        // THEN
        Assertions.assertThat(actual).isNotNull
    }

    /**
     * Tests that the method returned by `getGetterMethod` returns the expected value once invoked.
     * @throws Exception in case an error occurs
     */
    @Test
    @Throws(Exception::class)
    fun testThatTheReturnedMethodFromGetGetterMethodReturnsTheExpectedValue() {
        // GIVEN
        val mutableToFoo = createMutableToFoo(BigInteger.ONE)
        val getGetterMethod = getMethod(
            underTest!!.javaClass,
            GET_GETTER_METHOD_NAME,
            true,
            Class::class.java,
            String::class.java,
            Class::class.java
        )
        val methodUnderTest =
            getGetterMethod.invoke(underTest, MutableToFoo::class.java, ID_FIELD_NAME, BigInteger::class.java) as Method

        // WHEN
        val actual = methodUnderTest.invoke(mutableToFoo) as BigInteger

        // THEN
        Assertions.assertThat(actual).isEqualTo(BigInteger.ONE)
    }

    /**
     * Tests that the method `getGetterMethod` raises an exception in case the given class does not have the method.
     * @throws Exception in case an error occurs
     */
    @Test
    @Throws(Exception::class)
    fun testGetGetterMethodThrowsExceptionIfTheMethodDoesNotExists() {
        // GIVEN
        val methodUnderTest = getMethod(
            underTest!!.javaClass,
            GET_GETTER_METHOD_NAME,
            true,
            Class::class.java,
            String::class.java,
            Class::class.java
        )

        // WHEN
        val actual = ThrowingCallable {
            methodUnderTest.invoke(
                underTest,
                FromFooSimpleNoGetters::class.java,
                ID_FIELD_NAME,
                BigInteger::class.java
            )
        }

        // THEN
        Assertions.assertThatThrownBy(actual).hasCauseInstanceOf(MissingFieldException::class.java)
    }

    /**
     * Tests that the method `setFieldValue` tries to inject the value through the setter method if the direct access fails.
     */
    @Test(expectedExceptions = [NullPointerException::class])
    fun testSetFieldValueTriesToInjectThroughSetterMethodInCaseOfErrors() {
        // GIVEN
        val mutableToFoo = MutableToFooSimple()
        val idField = underTest!!.getDeclaredField(ID_FIELD_NAME, mutableToFoo.javaClass)

        // WHEN
        underTest.setFieldValue(null, idField, BigInteger.ONE)
    }

    /**
     * Tests that the method `setFieldValue` throws an [IllegalArgumentException] if the field value is not valid.
     */
    @Test(expectedExceptions = [IllegalArgumentException::class])
    fun testSetFieldValueRaiseAnExceptionIfTheValueToSetIsNotValid() {
        // GIVEN
        val mutableToFoo = MutableToFooSimple()
        val idField = underTest!!.getDeclaredField(ID_FIELD_NAME, mutableToFoo.javaClass)

        // WHEN
        underTest.setFieldValue(mutableToFoo, idField, java.lang.Boolean.TRUE)
    }

    @Test
    fun testSetFieldValueWorksProperly() {
        // GIVEN
        val mutableToFoo = MutableToFooSimple()
        val idField = underTest!!.getDeclaredField(ID_FIELD_NAME, mutableToFoo.javaClass)

        // WHEN
        underTest.setFieldValue(mutableToFoo, idField, BigInteger.ONE)

        // THEN
        Assertions.assertThat(mutableToFoo).hasFieldOrPropertyWithValue(ID_FIELD_NAME, BigInteger.ONE)
    }

    @Test
    fun testSetFieldValueInvokesTheSetterMethodInCaseAnExceptionIsRaised() {
        // GIVEN
        val underTestMock = Mockito.spy(ReflectionUtils::class.java)
        val mutableToFoo = MutableToFooSimple()
        val idField = underTest!!.getDeclaredField(ID_FIELD_NAME, mutableToFoo.javaClass)
        Mockito.doThrow(RuntimeException::class.java).`when`(underTestMock)
            .setFieldValueWithoutSetterMethod(mutableToFoo, idField, BigInteger.ONE)

        // WHEN
        underTestMock.setFieldValue(mutableToFoo, idField, BigInteger.ONE)

        // THEN
        Assertions.assertThat(mutableToFoo).hasFieldOrPropertyWithValue(ID_FIELD_NAME, BigInteger.ONE)
    }

    /**
     * Tests that the method `invokeMethod` manages the exception properly.
     * @param testCaseDescription the test case description
     * @param targetObject the object on which the method has to be invoked
     * @param methodNameToInvoke the name of the method to invoke
     * @param methodArg the argument to pass to the invoke method
     * @param isAccessible the accessibility that the `invokeMethod` must have
     * @param expectedException the expected exception class
     * @throws Exception if something goes wrong
     */
    @Test(dataProvider = "dataInvokeMethodTesting")
    @Throws(Exception::class)
    fun testInvokeMethodCorrectlyHandlesExceptions(
        testCaseDescription: String?,
        targetObject: Any,
        methodNameToInvoke: String,
        methodArg: Any?,
        isAccessible: Boolean,
        expectedException: Class<*>?
    ) {
        // GIVEN
        val setMethodToInvoke = getMethod(targetObject.javaClass, methodNameToInvoke, isAccessible, String::class.java)

        // WHEN
        val actual = ThrowingCallable { underTest!!.invokeMethod(setMethodToInvoke, targetObject, methodArg) }

        // THEN
        Assertions.assertThatThrownBy(actual).isInstanceOf(expectedException)
    }

    /**
     * Creates the parameters to be used for testing the method `invokeMethod`.
     * @return parameters to be used for testing the method `invokeMethod`.
     */
    @DataProvider
    private fun dataInvokeMethodTesting(): Array<Array<Any>> {
        return arrayOf(
            arrayOf(
                "Tests that the method raises an IllegalArgumentException in case the given argument is wrong",
                createMutableToFoo(BigInteger.ONE),
                SET_NAME_METHOD_NAME,
                BigInteger.ZERO,
                true,
                IllegalArgumentException::class.java
            ), arrayOf(
                "Tests that the method raises an IllegalAccessException in case the method is not accessible",
                MutableToFooAdvFields(),
                SET_INDEX_METHOD_NAME,
                INDEX_NUMBER,
                false,
                IllegalStateException::class.java
            )
        )
    }

    /**
     * Test that the method: `getRealTarget` returns the object contained in the Optional.
     * @throws Exception if something goes wrong
     */
    @Test
    @Throws(Exception::class)
    fun testGetSourceFieldValueRaisesAnExceptionIfTheParameterAreNull() {
        // GIVEN
        val getRealTargetMethod = getMethod(underTest!!.javaClass, GET_REAL_TARGET_METHOD_NAME, true, Any::class.java)
        val optionalBigInteger = Optional.of(BigInteger.ZERO)

        // WHEN
        val actual = getRealTargetMethod.invoke(underTest, optionalBigInteger)

        // THEN
        Assertions.assertThat(actual).isEqualTo(BigInteger.ZERO)
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
    @Throws(NoSuchMethodException::class)
    private fun getMethod(
        targetClass: Class<*>,
        methodName: String,
        makeAccessible: Boolean,
        vararg parameterTypes: Class<*>
    ): Method {
        val invokeMethod = targetClass.getDeclaredMethod(methodName, *parameterTypes)
        invokeMethod.isAccessible = makeAccessible
        return invokeMethod
    }

    /**
     * Creates an instance of of [FromFooSimpleNoGetters].
     * @return an instance of [FromFooSimpleNoGetters]
     */
    private fun createFromFooSimpleNoGetters(): FromFooSimpleNoGetters {
        val fromFooSimpleNoGetters = FromFooSimpleNoGetters()
        fromFooSimpleNoGetters.setId(BigInteger.ZERO)
        return fromFooSimpleNoGetters
    }

    /**
     * Creates an instance of of [MutableToFoo] with the given id.
     * @param id the id to assign
     * @return an instance of [MutableToFoo]
     */
    private fun createMutableToFoo(id: BigInteger?): MutableToFoo {
        val mutableToFoo = MutableToFoo()
        mutableToFoo.id = id
        return mutableToFoo
    }

    companion object {
        private const val ID_FIELD_NAME = "id"
        private const val NOT_EXISTING_FIELD_NAME = "notExistingField"
        private const val NESTED_OBJECT_NAME_FIELD_NAME = "nestedObject.name"
        private const val LIST_FIELD_NAME = "list"
        private const val PHONE_NUMBERS_FIELD_NAME = "phoneNumbers"
        private const val GETTER_METHOD_PREFIX_METHOD_NAME = "getGetterMethodPrefix"
        private const val EXPECTED_SETTER_METHOD_NAME = "setId"
        private const val GET_FIELD_VALUE_DIRECT_ACCESS_METHOD_NAME = "getFieldValueDirectAccess"
        private const val CHECK_FIELD_NAME = "check"
        private const val DECLARING_CLASS_NAME = "declaringClassName"
        private const val INVOKE_METHOD_NAME = "invokeMethod"
        private const val VERY_COMPLEX_MAP_FIELD_NAME = "veryComplexMap"
        private const val UNPARAMETRIZED_MAP_FIELD_NAME = "unparametrizedMap"
        private const val GET_GETTER_METHOD_NAME = "getGetterMethod"
        private const val SET_NAME_METHOD_NAME = "setName"
        private const val SET_INDEX_METHOD_NAME = "setIndex"
        private const val INDEX_NUMBER = "123"
        private const val GET_REAL_TARGET_METHOD_NAME = "getRealTarget"
        private const val GET_CLASS_DECLARED_FIELD_METHOD_NAME = "getClassDeclaredField"
        private const val NAME_FIELD_NAME = "name"
    }
}