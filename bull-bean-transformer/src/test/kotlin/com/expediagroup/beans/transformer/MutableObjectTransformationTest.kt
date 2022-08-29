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

import com.expediagroup.beans.sample.FromFooNoField
import com.expediagroup.beans.sample.FromFooSimple
import com.expediagroup.beans.sample.FromFooSimpleNoGetters
import com.expediagroup.beans.sample.mixed.MutableToFooOnlyPrimitiveTypes
import com.expediagroup.beans.sample.mutable.MutableToFoo
import com.expediagroup.beans.sample.mutable.MutableToFooInvalid
import com.expediagroup.beans.sample.mutable.MutableToFooNotExistingFields
import com.expediagroup.beans.sample.mutable.MutableToFooSimple
import com.expediagroup.beans.sample.mutable.MutableToFooSimpleNoSetters
import com.expediagroup.beans.sample.mutable.MutableToFooSubClass
import com.expediagroup.transformer.error.InvalidBeanException
import com.expediagroup.transformer.error.MissingFieldException
import com.expediagroup.transformer.model.FieldTransformer
import com.expediagroup.transformer.utils.ClassUtils
import org.assertj.core.api.Assertions
import org.assertj.core.api.ThrowableAssert.ThrowingCallable
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import java.lang.reflect.Constructor
import java.util.*
import java.util.function.Supplier

/**
 * Unit test for all [BeanTransformer] functions related to Mutable type Java Beans.
 */
class MutableObjectTransformationTest : AbstractBeanTransformerTest() {
    /**
     * Test that an exception is thrown if there is no default constructor defined for the mutable bean object.
     */
    @Test(expectedExceptions = [InvalidBeanException::class])
    fun testTransformThrowsExceptionWhenMutableBeanHasNoDefaultConstructor() {
        // GIVEN

        // WHEN
        underTest!!.transform(fromFoo, MutableToFooInvalid::class.java)
    }

    /**
     * Test mutable beans are correctly copied.
     */
    @Test
    fun testMutableBeanIsCorrectlyCopied() {
        // GIVEN

        // WHEN
        val actual = underTest!!.transform(fromFoo, MutableToFoo::class.java)

        // THEN
        Assertions.assertThat(actual).usingRecursiveComparison()
            .isEqualTo(fromFoo)
    }

    /**
     * Test transformation on an existing bean is correctly copied.
     */
    @Test
    fun testTransformationOnAnExistingDestinationWorksProperly() {
        // GIVEN
        val mutableToFoo = MutableToFooSubClass()

        // WHEN
        underTest!!.transform(fromFooSubClass, mutableToFoo)

        // THEN
        Assertions.assertThat(mutableToFoo).usingRecursiveComparison()
            .isEqualTo(fromFooSubClass)
    }

    /**
     * Test that no exception is thrown if the destination object don't met the constraints and the validation is disabled.
     */
    @Test
    fun testTransformThrowsNoExceptionIfTheDestinationObjectValuesAreNotValidAndTheValidationIsDisabled() {
        // GIVEN
        val mutableToFoo = MutableToFooSubClass()
        fromFooSubClass.id = null

        // WHEN
        underTest!!.transform(fromFooSubClass, mutableToFoo)

        // THEN
        Assertions.assertThat(mutableToFoo).usingRecursiveComparison()
            .isEqualTo(fromFooSubClass)
        fromFooSubClass.id = ID
    }

    /**
     * Test that a given field transformer function is applied only to a specific field even if there are other ones with same name.
     */
    @Test
    fun testFieldTransformationIsAppliedOnlyToASpecificField() {
        // GIVEN
        val namePrefix = "prefix-"
        val nameTransformer = FieldTransformer(NESTED_OBJECT_NAME_FIELD_NAME) { `val`: String -> namePrefix + `val` }

        // WHEN
        val actual = underTest
            .withFieldTransformer(nameTransformer)
            .transform(fromFoo, MutableToFoo::class.java)

        // THEN
        Assertions.assertThat(actual)
            .extracting(NAME_FIELD_NAME, NESTED_OBJECT_NAME_FIELD_NAME)
            .containsExactly(fromFoo.name, namePrefix + fromFoo.nestedObject.name)
    }

    /**
     * Test that a given field transformer function is applied to all field matching the given name when `flatFieldNameTransformation` is set to true.
     */
    @Test
    fun testFieldTransformationIsAppliedToAllMatchingFields() {
        // GIVEN
        val namePrefix = "prefix-"
        val nameTransformer = FieldTransformer(NAME_FIELD_NAME) { `val`: String -> namePrefix + `val` }

        // WHEN
        val actual = underTest
            .setFlatFieldNameTransformation(true)
            .withFieldTransformer(nameTransformer)
            .transform(fromFoo, MutableToFoo::class.java)

        // THEN
        Assertions.assertThat(actual.name).isEqualTo(namePrefix + fromFoo.name)
        Assertions.assertThat(actual.nestedObject.name)
            .isEqualTo(namePrefix + fromFoo.nestedObject.name)
    }

    /**
     * Test that the transformer is able to copy object even if the source object has no setter methods and the destination
     * object has no setter methods.
     */
    @Test
    fun testTransformerIsAbleToCopyObjectsWithoutRequiredMethods() {
        // GIVEN
        val fromFooSimpleNoGetters = FromFooSimpleNoGetters(NAME, ID, ACTIVE)

        // WHEN
        val actual = underTest!!.transform(fromFooSimpleNoGetters, MutableToFooSimpleNoSetters::class.java)

        // THEN
        Assertions.assertThat(actual).usingRecursiveComparison()
            .isEqualTo(fromFooSimpleNoGetters)
    }

    /**
     * Test that the transformer does not sets the default value for primitive type field in case it's disabled.
     */
    @Test
    fun testTransformerDoesNotSetsTheDefaultValueForPrimitiveTypeField() {
        // GIVEN
        val fromFooSimpleNoGetters = FromFooSimpleNoGetters(NAME, null, ACTIVE)
        underTest!!.setDefaultValueForMissingPrimitiveField(false)

        // WHEN
        val actual = underTest!!.transform(fromFooSimpleNoGetters, MutableToFooSimpleNoSetters::class.java)

        // THEN
        Assertions.assertThat(actual).usingRecursiveComparison()
            .isEqualTo(fromFooSimpleNoGetters)
        underTest!!.setDefaultValueForMissingPrimitiveField(true)
    }

    /**
     * Test that the transformer is able to copy object even if the source object has no fields but has getter methods.
     */
    @Test
    fun testTransformerIsAbleToCopyObjectsWithoutFieldButWithGetterMethods() {
        // GIVEN
        val fromFooNoField = FromFooNoField()

        // WHEN
        val actual = underTest!!.transform(fromFooNoField, MutableToFooSimpleNoSetters::class.java)

        // THEN
        Assertions.assertThat(actual).extracting(ID_FIELD_NAME, NAME_FIELD_NAME, ACTIVE_FIELD_NAME)
            .containsExactly(fromFooNoField.id, fromFooNoField.name, fromFooNoField.isActive)
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
        underTest!!.withFieldTransformer(ageFieldTransformer)
        val mutableObjectBean = underTest!!.transform(fromFooSimple, MutableToFooNotExistingFields::class.java)

        // THEN
        Assertions.assertThat(mutableObjectBean).hasFieldOrPropertyWithValue(AGE_FIELD_NAME, AGE)
    }

    /**
     * Test that a bean containing a field not existing in the source object, and without a transformer function defined throws
     * MissingFieldException even if the primitiveTypeConversionFunction is enabled.
     */
    @Test
    fun testTransformerThrowsExceptionIfAFieldIsMissingAndThePrimitiveTypeConversionIsEnabled() {
        // GIVEN
        val fromFooSimple = FromFooSimple(NAME, ID, ACTIVE)
        underTest!!.setPrimitiveTypeConversionEnabled(true)

        // WHEN
        val actual =
            ThrowingCallable { underTest!!.transform(fromFooSimple, MutableToFooNotExistingFields::class.java) }

        // THEN
        Assertions.assertThatThrownBy(actual).hasCauseInstanceOf(MissingFieldException::class.java)
        underTest!!.setPrimitiveTypeConversionEnabled(false)
    }

    /**
     * Test that a bean containing a field not existing in the source object, and without a transformer function defined,
     * does not throws MissingFieldException and the primitiveTypeConversionFunction is enabled.
     */
    @Test
    fun testTransformerDoesNotThrowExceptionIfAFieldIsMissingAndTheDefaultValueSetIsEnabled() {
        // GIVEN
        val fromFooSimple = FromFooSimple(NAME, ID, ACTIVE)
        underTest!!.setPrimitiveTypeConversionEnabled(true).setDefaultValueForMissingField(true)

        // WHEN
        val actual = underTest!!.transform(fromFooSimple, MutableToFooNotExistingFields::class.java)

        // THEN
        Assertions.assertThat(actual).extracting(ID_FIELD_NAME, NAME_FIELD_NAME)
            .containsExactly(fromFooSimple.getId(), fromFooSimple.getName())
        underTest!!.setPrimitiveTypeConversionEnabled(false).setDefaultValueForMissingField(false)
    }

    /**
     * Test transformation field with field transformer.
     * @param testCaseDescription the test case description
     * @param fieldToTransform the name of the field on which apply the transformation
     * @param transformationResult the value to return after the transformation
     */
    @Test(dataProvider = "dataTransformationTesting")
    fun testTransformationWithFieldTransformationWorksProperly(
        testCaseDescription: String?,
        fieldToTransform: String?,
        transformationResult: Any?
    ) {
        // GIVEN
        val fromFooSimple = FromFooSimple(NAME, ID, ACTIVE)
        val fieldTransformer = FieldTransformer(fieldToTransform) { `val`: Any? -> transformationResult }

        // WHEN
        underTest!!.withFieldTransformer(fieldTransformer)
        val actual = underTest!!.transform(fromFooSimple, MutableToFooSimple::class.java)

        // THEN
        Assertions.assertThat(actual).hasFieldOrPropertyWithValue(fieldToTransform, transformationResult)
        underTest!!.removeFieldTransformer(fieldToTransform)
    }

    /**
     * Creates the parameters to be used for testing the transformation with field transformer.
     * @return parameters to be used for testing the transformation with field transformer.
     */
    @DataProvider
    private fun dataTransformationTesting(): Array<Array<Any?>> {
        return arrayOf(
            arrayOf(
                "Test that the field transformation returns the expected values.",
                NAME_FIELD_NAME, NAME.lowercase(Locale.getDefault())
            ), arrayOf(
                "Test that the field transformation returns the expected values even if null.",
                NAME_FIELD_NAME, null
            )
        )
    }

    /**
     * Test that, given a set of fields for which the transformation has to be skipped, they are actually skipped.
     */
    @Test
    fun testFieldTransformationSkipWorksProperly() {
        // GIVEN
        underTest!!.skipTransformationForField(NAME_FIELD_NAME, PHONE_NUMBER_NESTED_OBJECT_FIELD_NAME)

        // WHEN
        val actual = underTest!!.transform(fromFoo, MutableToFoo::class.java)

        // THEN
        Assertions.assertThat(actual)
            .hasNoNullFieldsOrPropertiesExcept(NAME_FIELD_NAME, PHONE_NUMBER_NESTED_OBJECT_FIELD_NAME)
    }

    /**
     * Test that the automatic primitive type conversion works properly.
     */
    @Test
    fun testAutomaticPrimitiveTypeTransformationWorksProperly() {
        // GIVEN
        underTest!!.setPrimitiveTypeConversionEnabled(true)

        // WHEN
        val actual = underTest!!.transform(fromFooPrimitiveTypes, MutableToFooOnlyPrimitiveTypes::class.java)

        // THEN
        Assertions.assertThat(actual)
            .extracting(CODE_FIELD_NAME, ID_FIELD_NAME, PRICE_FIELD_NAME, ACTIVE_FIELD_NAME, UUID_FIELD_NAME)
            .containsExactly(
                fromFooPrimitiveTypes.code.toInt(),
                fromFooPrimitiveTypes.id.toString(),
                fromFooPrimitiveTypes.price.toDouble(),
                ACTIVE,
                fromFooPrimitiveTypes.uuid
            )
        underTest!!.setPrimitiveTypeConversionEnabled(false)
    }

    /**
     * Test that both primitive type transformation and custom transformation are executed.
     */
    @Test
    fun testThatBothPrimitiveTypeTransformationAndCustomTransformationAreExecuted() {
        // GIVEN
        val newPrice = (PRICE * PRICE).toDouble()
        val priceTransformer = FieldTransformer<Void, Double>(PRICE_FIELD_NAME, Supplier { newPrice })
        underTest!!.setPrimitiveTypeConversionEnabled(true).withFieldTransformer(priceTransformer)

        // WHEN
        val actual = underTest!!.transform(fromFooPrimitiveTypes, MutableToFooOnlyPrimitiveTypes::class.java)

        // THEN
        Assertions.assertThat(actual)
            .extracting(CODE_FIELD_NAME, ID_FIELD_NAME, ACTIVE_FIELD_NAME, PRICE_FIELD_NAME, UUID_FIELD_NAME)
            .containsExactly(
                fromFooPrimitiveTypes.code.toInt(), fromFooPrimitiveTypes.id.toString(),
                ACTIVE,
                newPrice,
                fromFooPrimitiveTypes.uuid
            )
        underTest!!.setPrimitiveTypeConversionEnabled(false)
        underTest!!.removeFieldTransformer(PRICE_FIELD_NAME)
    }

    /**
     * Test that an [InvalidBeanException] is raised if the class instantiation fails when the method `injectValues()` is called.
     * @throws Exception if something goes wrong
     */
    @Test
    @Throws(Exception::class)
    fun testInjectValuesThrowsException() {
        // GIVEN
        val expectedException = InvalidBeanException("Dummy exception")
        val underTestMock = Mockito.spy(
            TransformerImpl::class.java
        )
        val classUtils = Mockito.mock(
            ClassUtils::class.java
        )
        val constructor = classUtils.getAllArgsConstructor(
            MutableToFooSimple::class.java
        )
        Mockito.`when`(classUtils.getInstance<Any>(constructor)).thenThrow(InvalidBeanException::class.java)
        Mockito.`when`(classUtils.getConstructorParameters(constructor)).thenReturn(arrayOf())
        Mockito.`when`(classUtils.areParameterNamesAvailable(constructor)).thenReturn(true)
        Mockito.doReturn(expectedException).`when`(underTestMock).handleInjectionException(
            ArgumentMatchers.any<Any>(),
            ArgumentMatchers.any<Class<Any>>(),
            ArgumentMatchers.any(),
            ArgumentMatchers.any(),
            ArgumentMatchers.any(),
            ArgumentMatchers.anyBoolean(),
            ArgumentMatchers.any()
        )
        reflectionUtils.setFieldValue(underTestMock, CLASS_UTILS_FIELD_NAME, classUtils)
        val injectValuesMethod = TransformerImpl::class.java.getDeclaredMethod(
            INJECT_VALUES_METHOD_NAME,
            Any::class.java,
            Class::class.java,
            Constructor::class.java,
            String::class.java,
            Boolean::class.javaPrimitiveType
        )
        injectValuesMethod.isAccessible = true

        // WHEN
        val actual = injectValuesMethod.invoke(
            underTestMock,
            fromFooSimple,
            MutableToFooSimple::class.java,
            constructor,
            null,
            true
        )

        // THEN
        Assertions.assertThat(actual).isSameAs(expectedException)
    }

    companion object {
        private const val ACTIVE = true
        private const val PRICE_FIELD_NAME = "price"
        private const val UUID_FIELD_NAME = "uuid"
        private const val CLASS_UTILS_FIELD_NAME = "classUtils"
        private const val INJECT_VALUES_METHOD_NAME = "injectValues"
        private const val NESTED_OBJECT_NAME_FIELD_NAME = "nestedObject.name"
        private const val CODE_FIELD_NAME = "code"
    }
}