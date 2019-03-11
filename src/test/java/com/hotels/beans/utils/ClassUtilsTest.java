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

import static java.lang.reflect.Modifier.isFinal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

import javax.validation.constraints.NotNull;

import org.mockito.InjectMocks;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.hotels.beans.annotation.ConstructorArg;
import com.hotels.beans.constant.ClassType;
import com.hotels.beans.error.InvalidBeanException;
import com.hotels.beans.sample.FromFoo;
import com.hotels.beans.sample.FromFooWithBuilder;
import com.hotels.beans.sample.immutable.ImmutableToFoo;
import com.hotels.beans.sample.immutable.ImmutableToFooCustomAnnotation;
import com.hotels.beans.sample.immutable.ImmutableToFooSubClass;
import com.hotels.beans.sample.mixed.MixedToFoo;
import com.hotels.beans.sample.mixed.MixedToFooMissingConstructor;
import com.hotels.beans.sample.mixed.MixedToFooStaticField;
import com.hotels.beans.sample.mutable.MutableToFoo;

/**
 * Unit test for {@link ClassUtils}.
 */
public class ClassUtilsTest {
    private static final Class<BigDecimal> PRIMITIVE_CLASS = BigDecimal.class;
    private static final Class<FromFoo> NOT_PRIMITIVE_CLASS = FromFoo.class;
    private static final Class<MutableToFoo> CLASS_WITHOUT_PRIVATE_FINAL_FIELDS = MutableToFoo.class;
    private static final Class<ImmutableToFoo> CLASS_WITH_PRIVATE_FINAL_FIELDS = ImmutableToFoo.class;
    private static final int EXPECTED_PRIVATE_FINAL_FIELDS = 5;
    private static final int EXPECTED_MIXED_CLASS_TOTAL_PRIVATE_FIELDS = 4;
    private static final Class<MixedToFoo> CLASS_WITH_PRIVATE_AND_PUBLIC_FIELDS = MixedToFoo.class;
    private static final int EXPECTED_MIXED_CLASS_TOTAL_PRIVATE_NOT_FINAL_FIELDS = 1;
    private static final Class<MixedToFooStaticField> CLASS_WITH_STATIC_FIELDS = MixedToFooStaticField.class;
    private static final Class<MixedToFooMissingConstructor> CLASS_WITHOUT_CONSTRUCTOR = MixedToFooMissingConstructor.class;
    private static final int EXPECTED_NOT_STATIC_FIELDS = 1;
    private static final String NAME = "name";
    private static final String NORMAL_FIELD = "normalField";
    private static final int EXPECTED_CLASS_PARAMETERS = 5;
    private static final String NOT_EXISTING_FIELD_NAME = "notExistingFieldName";
    private static final Class<ImmutableToFooSubClass> CLASS_WITH_PRIVATE_FINAL_FIELDS_AND_SUB_CLASS = ImmutableToFooSubClass.class;
    private static final int EXPECTED_SUB_CLASS_PRIVATE_FIELDS = 9;
    private static final int EXPECTED_DEFAULT_VALUE = 0;
    private static final Predicate<Field> IS_FINAL_FIELD_PREDICATE = field -> isFinal(field.getModifiers());
    private static final int ZERO = 0;
    private static final int[] PRIMITIVE_INT_ARRAY = {};
    private static final short[] PRIMITIVE_SHORT_ARRAY = {};
    private static final char[] PRIMITIVE_CHAR_ARRAY = {};
    private static final byte[] PRIMITIVE_BYTE_ARRAY = {};
    private static final double[] PRIMITIVE_DOUBLE_ARRAY = {};
    private static final float[] PRIMITIVE_FLOAT_ARRAY = {};
    private static final long[] PRIMITIVE_LONG_ARRAY = {};

    /**
     * The class to be tested.
     */
    @InjectMocks
    private ClassUtils underTest;

    /**
     * Initializes mock.
     */
    @BeforeMethod
    public void beforeMethod() {
        initMocks(this);
    }

    /**
     * Tests that the method {@code isPrimitiveType} returns the expected value.
     */
    @Test
    public void testIsPrimitiveTypeWorksAsExpected() {
        // GIVEN

        // WHEN
        boolean actualPrimitiveClass = underTest.isPrimitiveType(PRIMITIVE_CLASS);
        boolean actualNotPrimitiveClass = underTest.isPrimitiveType(NOT_PRIMITIVE_CLASS);

        // THEN
        assertTrue(actualPrimitiveClass);
        assertFalse(actualNotPrimitiveClass);
    }

    /**
     * Tests that the method {@code isSpecialType} returns the expected value.
     */
    @Test
    public void testIsSpecialTypeWorksAsExpected() {
        // GIVEN

        // WHEN
        boolean actualSpecialClass = underTest.isSpecialType(Locale.class);
        boolean actualNotSpecialClass = underTest.isSpecialType(PRIMITIVE_CLASS);

        // THEN
        assertTrue(actualSpecialClass);
        assertFalse(actualNotSpecialClass);
    }

    /**
     * Tests that the method {@code isPrimitiveOrSpecialType} returns the expected value.
     */
    @Test
    public void testIsPrimitiveOrSpecialTypeWorksAsExpected() {
        // GIVEN

        // WHEN
        boolean actualPrimitiveOrSpecialClass = underTest.isPrimitiveOrSpecialType(Locale.class);
        boolean actualPrimitiveOrSpecialClass2 = underTest.isPrimitiveOrSpecialType(BigDecimal.class);
        boolean actualNotPrimitiveOrSpecialClass = underTest.isPrimitiveOrSpecialType(NOT_PRIMITIVE_CLASS);

        // THEN
        assertTrue(actualPrimitiveOrSpecialClass);
        assertTrue(actualPrimitiveOrSpecialClass2);
        assertFalse(actualNotPrimitiveOrSpecialClass);
    }

    /**
     * Tests that the method {@code isPrimitiveArrayType} returns the expected value.
     */
    @Test
    public void testIsPrimitiveArrayTypeWorksAsExpected() {
        // GIVEN

        // WHEN
        boolean actualPrimitiveIntArrayClass = underTest.isPrimitiveTypeArray(PRIMITIVE_INT_ARRAY);
        boolean actualPrimitiveShortArrayClass = underTest.isPrimitiveTypeArray(PRIMITIVE_SHORT_ARRAY);
        boolean actualPrimitiveCharArrayClass = underTest.isPrimitiveTypeArray(PRIMITIVE_CHAR_ARRAY);
        boolean actualPrimitiveByteArrayClass = underTest.isPrimitiveTypeArray(PRIMITIVE_BYTE_ARRAY);
        boolean actualPrimitiveDoubleArrayClass = underTest.isPrimitiveTypeArray(PRIMITIVE_DOUBLE_ARRAY);
        boolean actualPrimitiveFloatArrayClass = underTest.isPrimitiveTypeArray(PRIMITIVE_FLOAT_ARRAY);
        boolean actualPrimitiveLongArrayClass = underTest.isPrimitiveTypeArray(PRIMITIVE_LONG_ARRAY);

        // THEN
        assertTrue(actualPrimitiveIntArrayClass);
        assertTrue(actualPrimitiveShortArrayClass);
        assertTrue(actualPrimitiveCharArrayClass);
        assertTrue(actualPrimitiveByteArrayClass);
        assertTrue(actualPrimitiveDoubleArrayClass);
        assertTrue(actualPrimitiveFloatArrayClass);
        assertTrue(actualPrimitiveLongArrayClass);
    }

    /**
     * Tests that the method {@code getPrivateFinalFields} works as expected.
     */
    @Test
    public void testGetPrivateFinalFieldsWorksAsExpected() {
        // GIVEN

        // WHEN
        List<Field> mutableClassPrivateFinalFields = underTest.getPrivateFinalFields(CLASS_WITHOUT_PRIVATE_FINAL_FIELDS);
        List<Field> immutableClassPrivateFinalFields = underTest.getPrivateFinalFields(CLASS_WITH_PRIVATE_FINAL_FIELDS);

        // THEN
        assertTrue(mutableClassPrivateFinalFields.isEmpty());
        assertEquals(EXPECTED_PRIVATE_FINAL_FIELDS, immutableClassPrivateFinalFields.size());
    }

    /**
     * Tests that the method {@code getTotalFields} works as expected.
     */
    @Test
    public void testGetTotalFieldsWorksAsExpected() {
        // GIVEN

        // WHEN
        long mutableClassTotPrivateFinalFields = underTest.getTotalFields(CLASS_WITHOUT_PRIVATE_FINAL_FIELDS, IS_FINAL_FIELD_PREDICATE);
        long immutableClassPrivateFinalFields = underTest.getTotalFields(CLASS_WITH_PRIVATE_FINAL_FIELDS, IS_FINAL_FIELD_PREDICATE);

        // THEN
        assertEquals(ZERO, mutableClassTotPrivateFinalFields);
        assertEquals(EXPECTED_PRIVATE_FINAL_FIELDS, immutableClassPrivateFinalFields);
    }

    /**
     * Tests that the method {@code getPrivateFields} works as expected.
     */
    @Test
    public void testGetPrivateFieldsWorksAsExpected() {
        // GIVEN

        // WHEN
        List<Field> mixedToFooPrivateFields = underTest.getPrivateFields(CLASS_WITH_PRIVATE_AND_PUBLIC_FIELDS);
        List<Field> immutableClassPrivateFields = underTest.getPrivateFields(CLASS_WITH_PRIVATE_FINAL_FIELDS);

        // THEN
        assertEquals(EXPECTED_MIXED_CLASS_TOTAL_PRIVATE_FIELDS, mixedToFooPrivateFields.size());
        assertEquals(EXPECTED_PRIVATE_FINAL_FIELDS, immutableClassPrivateFields.size());
    }

    /**
     * Tests that the method {@code getPrivateFields} works as expected with sub classes.
     */
    @Test
    public void testGetPrivateFieldsWorksAsExpectedOnSubLasses() {
        // GIVEN

        // WHEN
        List<Field> immutableClassPrivateFinalFields = underTest.getPrivateFields(ImmutableToFooSubClass.class);

        // THEN
        assertEquals(EXPECTED_SUB_CLASS_PRIVATE_FIELDS, immutableClassPrivateFinalFields.size());
    }


    /**
     * Tests that the method {@code getPrivateFields} with the skipFinal option one works as expected.
     */
    @Test
    public void testGetPrivateFieldsSkippingFinalWorksAsExpected() {
        // GIVEN

        // WHEN
        List<Field> mixedToFooPrivateFieldsNotFinal = underTest.getPrivateFields(CLASS_WITH_PRIVATE_AND_PUBLIC_FIELDS, true);
        List<Field> immutableClassPrivateOrFinalFields = underTest.getPrivateFields(CLASS_WITH_PRIVATE_AND_PUBLIC_FIELDS, false);

        // THEN
        assertEquals(EXPECTED_MIXED_CLASS_TOTAL_PRIVATE_NOT_FINAL_FIELDS, mixedToFooPrivateFieldsNotFinal.size());
        assertEquals(EXPECTED_MIXED_CLASS_TOTAL_PRIVATE_FIELDS, immutableClassPrivateOrFinalFields.size());
    }

    /**
     * Tests that the method {@code getDeclaredFields} works as expected.
     */
    @Test
    public void testGetDeclaredFieldsWorksAsExpected() {
        // GIVEN
        final int totalClassFields = CLASS_WITH_STATIC_FIELDS.getDeclaredFields().length;

        // WHEN
        List<Field> notStaticFields = underTest.getDeclaredFields(CLASS_WITH_STATIC_FIELDS, true);
        List<Field> allFields = underTest.getDeclaredFields(CLASS_WITH_STATIC_FIELDS, false);

        // THEN
        assertEquals(EXPECTED_NOT_STATIC_FIELDS, notStaticFields.size());
        assertEquals(totalClassFields, allFields.size());
    }

    /**
     * Tests that the method {@code getDeclaredFields} returns the expected field.
     */
    @Test
    public void testGetDeclaredFieldReturnsTheExpectedField() {
        // GIVEN

        // WHEN
        Field actual = underTest.getDeclaredField(CLASS_WITH_STATIC_FIELDS, NORMAL_FIELD);

        // THEN
        assertNotNull(actual);
        assertEquals(NORMAL_FIELD, actual.getName());
    }

    /**
     * Tests that the method {@code getAllArgsConstructor} returns the class constructor.
     */
    @Test
    public void testGetAllArgsConstructorWorksAsExpected() {
        // GIVEN

        // WHEN
        Constructor actual = underTest.getAllArgsConstructor(CLASS_WITH_PRIVATE_FINAL_FIELDS.getConstructors());

        // THEN
        assertNotNull(actual);
    }

    /**
     * Tests that the method {@code getAllArgsConstructor} throws exception if the class has no all args constructor.
     */
    @Test(expectedExceptions = InvalidBeanException.class)
    public void testGetAllArgsConstructorThrowsExceptionIfTheConstructorIsMissing() {
        // GIVEN

        // WHEN
        underTest.getAllArgsConstructor(CLASS_WITHOUT_CONSTRUCTOR.getConstructors());
    }

    /**
     * Tests that the method {@code getConstructorParameters} returns the constructor parameter.
     */
    @Test
    public void testGetConstructorParameters() {
        // GIVEN
        Constructor classConstructor = underTest.getAllArgsConstructor(CLASS_WITH_PRIVATE_FINAL_FIELDS.getConstructors());

        // WHEN
        Parameter[] constructorParameters = underTest.getConstructorParameters(classConstructor);

        // THEN
        assertNotNull(constructorParameters);
        assertEquals(EXPECTED_CLASS_PARAMETERS, constructorParameters.length);
    }

    /**
     * Tests that the method {@code hasField} works as expected.
     */
    @Test
    public void testHasFieldWorksAsExpected() {
        // GIVEN
        final ImmutableToFooSubClass immutableToFooSubClass =
                new ImmutableToFooSubClass(null, null, null, null, null, null, 0, false, null);

        // WHEN
        boolean classContainsNotExistingField = underTest.hasField(immutableToFooSubClass, NOT_EXISTING_FIELD_NAME);
        boolean classContainsExistingField = underTest.hasField(immutableToFooSubClass, NAME);

        // THEN
        assertFalse(classContainsNotExistingField);
        assertTrue(classContainsExistingField);
    }

    /**
     * Tests that the method {@code hasSetterMethods} works as expected.
     */
    @Test
    public void testHasSetterMethodsWorksAsExpected() {
        // GIVEN

        // WHEN
        boolean mixedFooStaticFieldHasSetterMethods = underTest.hasSetterMethods(CLASS_WITH_PRIVATE_AND_PUBLIC_FIELDS);
        boolean immutableClassHasSetterMethods = underTest.hasSetterMethods(CLASS_WITH_PRIVATE_FINAL_FIELDS);

        // THEN
        assertTrue(mixedFooStaticFieldHasSetterMethods);
        assertFalse(immutableClassHasSetterMethods);
    }

    /**
     * Tests that the method {@code hasFinalFields} works as expected.
     */
    @Test
    public void testHasPrivateFinalFieldsWorksAsExpected() {
        // GIVEN


        // WHEN
        boolean immutableClassHasPrivateFinalFields = underTest.hasFinalFields(CLASS_WITH_PRIVATE_FINAL_FIELDS_AND_SUB_CLASS);
        boolean mutableClassHasPrivateFinalFields = underTest.hasFinalFields(CLASS_WITHOUT_PRIVATE_FINAL_FIELDS);

        // THEN
        assertTrue(immutableClassHasPrivateFinalFields);
        assertFalse(mutableClassHasPrivateFinalFields);
    }

    /**
     * Tests that the method {@code containsAnnotation} works as expected.
     */
    @Test
    public void testContainsAnnotationWorksAsExpected() {
        // GIVEN
        Constructor<?> constructor = ImmutableToFooCustomAnnotation.class.getConstructors()[0];


        // WHEN
        boolean containsExistingAnnotation = underTest.containsAnnotation(constructor, ConstructorArg.class);
        boolean containsNotExistingAnnotation = underTest.containsAnnotation(constructor, NotNull.class);

        // THEN
        assertTrue(containsExistingAnnotation);
        assertFalse(containsNotExistingAnnotation);
    }

    /**
     * Tests that the method {@code notAllParameterAnnotatedWith} works as expected.
     */
    @Test
    public void testAllParameterAnnotatedWithWorksAsExpected() {
        // GIVEN
        Constructor<?> constructor = ImmutableToFooCustomAnnotation.class.getConstructors()[0];


        // WHEN
        boolean allParamContainsExistingAnnotation = underTest.allParameterAnnotatedWith(constructor, ConstructorArg.class);
        boolean allParamContainsNotExistingAnnotation = underTest.allParameterAnnotatedWith(constructor, NotNull.class);

        // THEN
        assertTrue(allParamContainsExistingAnnotation);
        assertFalse(allParamContainsNotExistingAnnotation);
    }

    /**
     * Tests that the method {@code getClassType} works as expected.
     */
    @Test
    public void testGetClassTypeWorksAsExpected() {
        // GIVEN

        // WHEN
        final ClassType immutableToFooClassType = underTest.getClassType(ImmutableToFoo.class);
        final ClassType mutableToFooClassType = underTest.getClassType(MutableToFoo.class);
        final ClassType mixedToFooClassType = underTest.getClassType(MixedToFoo.class);

        // THEN
        assertEquals(ClassType.IMMUTABLE, immutableToFooClassType);
        assertEquals(ClassType.MUTABLE, mutableToFooClassType);
        assertEquals(ClassType.MIXED, mixedToFooClassType);
    }

    /**
     * Tests that the method {@code getSetterMethods} works as expected.
     */
    @Test
    public void testGetSetterMethodsWorksAsExpected() {
        // GIVEN

        // WHEN
        final List<Method> immutableClassSetterMethods = underTest.getSetterMethods(ImmutableToFooSubClass.class);
        final List<Method> mutableClassSetterMethods = underTest.getSetterMethods(MutableToFoo.class);

        // THEN
        assertTrue(immutableClassSetterMethods.isEmpty());
        assertFalse(mutableClassSetterMethods.isEmpty());
    }

    /**
     * Tests that the method {@code getDefaultTypeValue} works as expected.
     */
    @Test
    public void testGetDefaultTypeValueWorksAsExpected() {
        // GIVEN

        // WHEN
        Object actual = underTest.getDefaultTypeValue(Integer.class);

        // THEN
        assertEquals(EXPECTED_DEFAULT_VALUE, actual);
    }

    /**
     * Tests that the method {@code getDefaultTypeValue} works as expected.
     */
    @Test
    public void testUsesBuilderPatternWorksAsExpected() {
        // GIVEN
        final Constructor constructorWithBuilder = underTest.getAllArgsConstructor(FromFooWithBuilder.class);
        final Constructor constructorWithoutBuilder = underTest.getAllArgsConstructor(FromFoo.class);

        // WHEN
        final boolean usesBuilderPattern = underTest.usesBuilderPattern(constructorWithBuilder, FromFooWithBuilder.class);
        final boolean notUseBuilderPattern = underTest.usesBuilderPattern(constructorWithoutBuilder, FromFoo.class);

        // THEN
        assertTrue(usesBuilderPattern);
        assertFalse(notUseBuilderPattern);
    }

    /**
     * Tests that the method {@code hasAccessibleConstructors} works as expected.
     */
    @Test
    public void testHasAccessibleConstructorsWorksAsExpected() {
        // GIVEN

        // WHEN
        final boolean notAccessibleConstructors = underTest.hasAccessibleConstructors(FromFooWithBuilder.class);
        final boolean accessibleConstructors = underTest.hasAccessibleConstructors(FromFoo.class);

        // THEN
        assertFalse(notAccessibleConstructors);
        assertTrue(accessibleConstructors);
    }
}
