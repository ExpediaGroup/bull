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
import static java.util.Objects.nonNull;

import static org.apache.commons.lang3.ArrayUtils.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

import javax.validation.constraints.NotNull;

import org.mockito.InjectMocks;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.hotels.beans.annotation.ConstructorArg;
import com.hotels.beans.constant.ClassType;
import com.hotels.beans.error.InvalidBeanException;
import com.hotels.beans.sample.FromFoo;
import com.hotels.beans.sample.immutable.ImmutableToFoo;
import com.hotels.beans.sample.immutable.ImmutableToFooCustomAnnotation;
import com.hotels.beans.sample.immutable.ImmutableToFooSubClass;
import com.hotels.beans.sample.mixed.MixedToFoo;
import com.hotels.beans.sample.mixed.MixedToFooMissingConstructor;
import com.hotels.beans.sample.mixed.MixedToFooWithBuilder;
import com.hotels.beans.sample.mixed.MixedToFooStaticField;
import com.hotels.beans.sample.mutable.MutableToFoo;
import com.hotels.beans.sample.mutable.MutableToFooWithBuilder;

/**
 * Unit test for {@link ClassUtils}.
 */
public class ClassUtilsTest {
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
    private static final FromFoo[] NOT_PRIMITIVE_ARRAY = {};

    /**
     * The class to be tested.
     */
    @InjectMocks
    private ClassUtils underTest;

    /**
     * Initializes mock.
     */
    @BeforeClass
    public void beforeClass() {
        initMocks(this);
    }

    /**
     * Tests that the method {@code isPrimitiveType} returns the expected value.
     * @param testCaseDescription the test case description
     * @param testClass the class to test
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "dataIsPrimitiveTypeObjectTesting")
    public void testIsPrimitiveTypeWorksAsExpected(final String testCaseDescription, final Class<?> testClass, final boolean expectedResult) {
        // GIVEN

        // WHEN
        boolean actual = underTest.isPrimitiveType(testClass);

        // THEN
        assertEquals(expectedResult, actual);
    }

    /**
     * Creates the parameters to be used for testing the method {@code isPrimitiveType}.
     * @return parameters to be used for testing the the method {@code isPrimitiveType}.
     */
    @DataProvider
    private Object[][] dataIsPrimitiveTypeObjectTesting() {
        return new Object[][] {
                {"Tests that the method returns true if the class is a primitive type object", BigDecimal.class, true},
                {"Tests that the method returns false if the class is not a primitive type object", FromFoo.class, false}
        };
    }

    /**
     * Tests that the method {@code isSpecialType} returns the expected value.
     * @param testCaseDescription the test case description
     * @param testClass the class to test
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "dataSpecialTypeObjectTesting")
    public void testIsSpecialTypeWorksAsExpected(final String testCaseDescription, final Class<?> testClass, final boolean expectedResult) {
        // GIVEN

        // WHEN
        boolean actual = underTest.isSpecialType(testClass);

        // THEN
        assertEquals(expectedResult, actual);
    }

    /**
     * Creates the parameters to be used for testing the method {@code isSpecialType}.
     * @return parameters to be used for testing the the method {@code isSpecialType}.
     */
    @DataProvider
    private Object[][] dataSpecialTypeObjectTesting() {
        return new Object[][] {
                {"Tests that the method returns true if the class is a special type object", Locale.class, true},
                {"Tests that the method returns false if the class is not a special type object", BigDecimal.class, false},
                {"Tests that the method returns true if the class is an instance of Temporal interface", Instant.class, true}
        };
    }

    /**
     * Tests that the method {@code isPrimitiveOrSpecialType} returns the expected value.
     * @param testCaseDescription the test case description
     * @param testClass the class to test
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "dataPrimitiveOrSpecialTypeObjectTesting")
    public void testIsPrimitiveOrSpecialTypeWorksAsExpected(final String testCaseDescription, final Class<?> testClass, final boolean expectedResult) {
        // GIVEN

        // WHEN
        boolean actual = underTest.isPrimitiveOrSpecialType(testClass);

        // THEN
        assertEquals(expectedResult, actual);
    }

    /**
     * Creates the parameters to be used for testing the method {@code isPrimitiveOrSpecialType}.
     * @return parameters to be used for testing the the method {@code isPrimitiveOrSpecialType}.
     */
    @DataProvider
    private Object[][] dataPrimitiveOrSpecialTypeObjectTesting() {
        return new Object[][] {
                {"Tests that the method returns true if the class is a primitive or special type object", Locale.class, true},
                {"Tests that the method returns false if the class is a primitive nor a special type object", BigDecimal.class, true},
                {"Tests that the method returns false if the class is not a primitive nor a special type object", FromFoo.class, false}
        };
    }

    /**
     * Tests that the method {@code isPrimitiveArrayType} returns the expected value.
     * @param testCaseDescription the test case description
     * @param testArray the array to test
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "dataPrimitiveArrayTypeTesting")
    public void testIsPrimitiveArrayTypeWorksAsExpected(final String testCaseDescription, final Object testArray, final boolean expectedResult) {
        // GIVEN

        // WHEN
        boolean actual = underTest.isPrimitiveTypeArray(testArray);

        // THEN
        assertEquals(expectedResult, actual);
    }

    /**
     * Creates the parameters to be used for testing the method {@code isPrimitiveArrayType}.
     * @return parameters to be used for testing the the method {@code isPrimitiveArrayType}.
     */
    @DataProvider
    private Object[][] dataPrimitiveArrayTypeTesting() {
        return new Object[][] {
                {"Tests that the method returns true if the array is of type int[]", PRIMITIVE_INT_ARRAY, true},
                {"Tests that the method returns true if the array is of type short[]", PRIMITIVE_SHORT_ARRAY, true},
                {"Tests that the method returns true if the array is of type char[]", PRIMITIVE_CHAR_ARRAY, true},
                {"Tests that the method returns true if the array is of type byte[]", PRIMITIVE_BYTE_ARRAY, true},
                {"Tests that the method returns true if the array is of type double[]", PRIMITIVE_DOUBLE_ARRAY, true},
                {"Tests that the method returns true if the array is of type float[]", PRIMITIVE_FLOAT_ARRAY, true},
                {"Tests that the method returns true if the array is of type long[]", PRIMITIVE_LONG_ARRAY, true},
                {"Tests that the method returns false if the array is of type FromFoo[]", NOT_PRIMITIVE_ARRAY, false}
        };
    }

    /**
     * Tests that the method {@code getPrivateFinalFields} works as expected.
     * @param testCaseDescription the test case description
     * @param testClass the class to test
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "dataGetPrivateFinalFieldsTesting")
    public void testGetPrivateFinalFieldsWorksAsExpected(final String testCaseDescription, final Class<?> testClass, final int expectedResult) {
        // GIVEN

        // WHEN
        List<Field> actual = underTest.getPrivateFinalFields(testClass);

        // THEN
        assertEquals(expectedResult, actual.size());
    }

    /**
     * Creates the parameters to be used for testing the method {@code getPrivateFinalFields}.
     * @return parameters to be used for testing the the method {@code getPrivateFinalFields}.
     */
    @DataProvider
    private Object[][] dataGetPrivateFinalFieldsTesting() {
        return new Object[][] {
                {"Tests that the method returns 0 if the given class has no private final fields", CLASS_WITHOUT_PRIVATE_FINAL_FIELDS, ZERO},
                {"Tests that the method returns the expected value if the class has private final fields", CLASS_WITH_PRIVATE_FINAL_FIELDS, EXPECTED_PRIVATE_FINAL_FIELDS}
        };
    }

    /**
     * Tests that the method {@code getTotalFields} works as expected.
     * @param testCaseDescription the test case description
     * @param testClass the class to test
     * @param fieldPredicate the  predicate to apply
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "dataGetTotalFieldsTesting")
    public void testGetTotalFieldsWorksAsExpected(final String testCaseDescription, final Class<?> testClass, final Predicate<Field> fieldPredicate, final int expectedResult) {
        // GIVEN

        // WHEN
        long actual = underTest.getTotalFields(testClass, fieldPredicate);

        // THEN
        assertEquals(expectedResult, actual);
    }

    /**
     * Creates the parameters to be used for testing the method {@code getTotalFields}.
     * @return parameters to be used for testing the the method {@code getTotalFields}.
     */
    @DataProvider
    private Object[][] dataGetTotalFieldsTesting() {
        return new Object[][] {
                {"Tests that the method returns 0 if the given class has no private final fields", CLASS_WITHOUT_PRIVATE_FINAL_FIELDS, IS_FINAL_FIELD_PREDICATE, ZERO},
                {"Tests that the method returns the expected value if the class has private final fields", CLASS_WITH_PRIVATE_FINAL_FIELDS, IS_FINAL_FIELD_PREDICATE,
                        EXPECTED_PRIVATE_FINAL_FIELDS}
        };
    }

    /**
     * Tests that the method {@code getPrivateFields} works as expected.
     * @param testCaseDescription the test case description
     * @param testClass the class to test
     * @param skipFinal if true the final fields are skipped
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "dataGetPrivateFieldsTesting")
    public void testGetPrivateFieldsWorksAsExpected(final String testCaseDescription, final Class<?> testClass, final Boolean skipFinal, final int expectedResult) {
        // GIVEN

        // WHEN
        List<Field> actual = nonNull(skipFinal) ? underTest.getPrivateFields(testClass, skipFinal) : underTest.getPrivateFields(testClass);

        // THEN
        assertEquals(expectedResult, actual.size());
    }

    /**
     * Creates the parameters to be used for testing the method {@code getPrivateFields}.
     * @return parameters to be used for testing the the method {@code getPrivateFields}.
     */
    @DataProvider
    private Object[][] dataGetPrivateFieldsTesting() {
        return new Object[][] {
                {"Tests that the method returns the expected value if the class has private and public fields", CLASS_WITH_PRIVATE_AND_PUBLIC_FIELDS, false,
                        EXPECTED_MIXED_CLASS_TOTAL_PRIVATE_FIELDS},
                {"Tests that the method returns the expected value if the class has private and public fields and skipFinal is not passed as param",
                        CLASS_WITH_PRIVATE_AND_PUBLIC_FIELDS, null, EXPECTED_MIXED_CLASS_TOTAL_PRIVATE_FIELDS},
                {"Tests that the method returns the expected value if the class has private final fields only", CLASS_WITH_PRIVATE_FINAL_FIELDS, false,
                        EXPECTED_PRIVATE_FINAL_FIELDS},
                {"Tests that the method returns the expected value if the class has private final fields only and skipFinal is not passed as param",
                        CLASS_WITH_PRIVATE_FINAL_FIELDS, null, EXPECTED_PRIVATE_FINAL_FIELDS},
                {"Tests that the method returns the expected value if the class extends another class", ImmutableToFooSubClass.class, false,
                        EXPECTED_SUB_CLASS_PRIVATE_FIELDS},
                {"Tests that the method returns the expected value if the class extends another class and skipFinal is not passed as param",
                        ImmutableToFooSubClass.class, null, EXPECTED_SUB_CLASS_PRIVATE_FIELDS},
                {"Tests that the method returns the expected value if the skipFinal is enabled", CLASS_WITH_PRIVATE_AND_PUBLIC_FIELDS, true,
                        EXPECTED_MIXED_CLASS_TOTAL_PRIVATE_NOT_FINAL_FIELDS}
        };
    }

    /**
     * Tests that the method {@code getDeclaredFields} works as expected.
     * @param testCaseDescription the test case description
     * @param testClass the class to test
     * @param skipStatic if true the static fields are skipped
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "dataGetDeclaredFieldsTesting")
    public void testGetDeclaredFieldsWorksAsExpected(final String testCaseDescription, final Class<?> testClass, final boolean skipStatic, final int expectedResult) {
        // GIVEN

        // WHEN
        List<Field> actual = underTest.getDeclaredFields(testClass, skipStatic);

        // THEN
        assertEquals(expectedResult, actual.size());
    }

    /**
     * Creates the parameters to be used for testing the method {@code getDeclaredFields}.
     * @return parameters to be used for testing the the method {@code getDeclaredFields}.
     */
    @DataProvider
    private Object[][] dataGetDeclaredFieldsTesting() {
        return new Object[][] {
                {"Tests that the method returns the expected total number of fields when the skipStatic param is true", CLASS_WITH_STATIC_FIELDS, true,
                        EXPECTED_NOT_STATIC_FIELDS},
                {"Tests that the method returns the expected value if the class has private final fields only", CLASS_WITH_STATIC_FIELDS, false,
                        CLASS_WITH_STATIC_FIELDS.getDeclaredFields().length}
        };
    }

    /**
     * Test that the a manual declared Builder is returned by method: {@code getDeclaredClasses}.
     * @param testCaseDescription the test case description
     * @param testClass the class from which extract the nested classes
     * @param expectedClass the class expected as nested
     */
    @Test(dataProvider = "dataGetDeclaredClassesTesting")
    public void testGetDeclaredClassesWorksAsExpected(final String testCaseDescription, final Class<?> testClass, final Class<?> expectedClass) {
        // GIVEN

        // WHEN
        Class[] actual = underTest.getDeclaredClasses(testClass);

        // THEN
        assertTrue(contains(actual, expectedClass));
    }

    /**
     * Creates the parameters to be used for testing the method {@code getDeclaredClasses}.
     * @return parameters to be used for testing the the method {@code getDeclaredClasses}.
     */
    @DataProvider
    private Object[][] dataGetDeclaredClassesTesting() {
        return new Object[][] {
                {"Test that the a manual declared Builder is returned by method: {@code getDeclaredClasses}", MutableToFooWithBuilder.class,
                        MutableToFooWithBuilder.Builder.class},
                {"Test that the a Builder created by lombok is returned by method: {@code getDeclaredClasses}", MixedToFooWithBuilder.class,
                        MixedToFooWithBuilder.builder().getClass()}
        };
    }

    /**
     * Tests that the method {@code getAllArgsConstructor} returns the class constructor.
     */
    @Test
    public void testGetAllArgsConstructorWorksAsExpected() {
        // GIVEN

        // WHEN
        Constructor actual = underTest.getAllArgsConstructor(CLASS_WITH_PRIVATE_FINAL_FIELDS);

        // THEN
        assertNotNull(actual);
    }

    /**
     * Tests that the method {@code getNoArgsConstructor} throws exception if the class has no all args constructor.
     */
    @Test(expectedExceptions = InvalidBeanException.class)
    public void testGetNoArgsConstructorThrowsExceptionIfTheConstructorIsMissing() {
        // GIVEN

        // WHEN
        underTest.getNoArgsConstructor(CLASS_WITHOUT_CONSTRUCTOR);
    }

    /**
     * Tests that the method {@code getConstructorParameters} returns the constructor parameter.
     */
    @Test
    public void testGetConstructorParameters() {
        // GIVEN
        Constructor classConstructor = underTest.getAllArgsConstructor(CLASS_WITH_PRIVATE_FINAL_FIELDS);

        // WHEN
        Parameter[] constructorParameters = underTest.getConstructorParameters(classConstructor);

        // THEN
        assertNotNull(constructorParameters);
        assertEquals(EXPECTED_CLASS_PARAMETERS, constructorParameters.length);
    }

    /**
     * Tests that the method {@code hasField} works as expected.
     * @param testCaseDescription the test case description
     * @param fieldName the field's name to retrieve
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "dataHasFieldTesting")
    public void testHasFieldWorksAsExpected(final String testCaseDescription, final String fieldName, final boolean expectedResult) {
        // GIVEN
        final ImmutableToFooSubClass immutableToFooSubClass =
                new ImmutableToFooSubClass(null, null, null, null, null, null, 0, false, null);

        // WHEN
        boolean actual = underTest.hasField(immutableToFooSubClass, fieldName);

        // THEN
        assertEquals(expectedResult, actual);
    }

    /**
     * Creates the parameters to be used for testing the method {@code hasField}.
     * @return parameters to be used for testing the the method {@code hasField}.
     */
    @DataProvider
    private Object[][] dataHasFieldTesting() {
        return new Object[][] {
                {"Tests that the method returns false if the given field does not exists", NOT_EXISTING_FIELD_NAME, false},
                {"Tests that the method returns true if the given field exists", NAME, true}
        };
    }

    /**
     * Tests that the method {@code hasFinalFields} works as expected.
     * @param testCaseDescription the test case description
     * @param testClass the class to test
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "dataHasFinalFieldTesting")
    public void testHasPrivateFinalFieldsWorksAsExpected(final String testCaseDescription, final Class<?> testClass, final boolean expectedResult) {
        // GIVEN

        // WHEN
        boolean actual = underTest.hasFinalFields(testClass);

        // THEN
        assertEquals(expectedResult, actual);
    }

    /**
     * Creates the parameters to be used for testing the method {@code hasFinalFields}.
     * @return parameters to be used for testing the the method {@code hasFinalFields}.
     */
    @DataProvider
    private Object[][] dataHasFinalFieldTesting() {
        return new Object[][] {
                {"Tests that the method returns true if the given class has private final fields", CLASS_WITH_PRIVATE_FINAL_FIELDS_AND_SUB_CLASS, true},
                {"Tests that the method returns true if the given class has no private final fields", CLASS_WITHOUT_PRIVATE_FINAL_FIELDS, false}
        };
    }

    /**
     * Tests that the method {@code hasSetterMethods} works as expected.
     * @param testCaseDescription the test case description
     * @param testClass the class to test
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "dataHasSetterMethodsTesting")
    public void testHasSetterMethodsWorksAsExpected(final String testCaseDescription, final Class<?> testClass, final boolean expectedResult) {
        // GIVEN

        // WHEN
        boolean actual = underTest.hasSetterMethods(testClass);

        // THEN
        assertEquals(expectedResult, actual);
    }

    /**
     * Creates the parameters to be used for testing the method {@code hasSetterMethods}.
     * @return parameters to be used for testing the the method {@code hasSetterMethods}.
     */
    @DataProvider
    private Object[][] dataHasSetterMethodsTesting() {
        return new Object[][] {
                {"Tests that the method returns true if the given class has private final fields", CLASS_WITH_PRIVATE_AND_PUBLIC_FIELDS, true},
                {"Tests that the method returns true if the given class has no private final fields", CLASS_WITH_PRIVATE_FINAL_FIELDS, false},
        };
    }

    /**
     * Tests that the method {@code containsAnnotation} works as expected.
     * @param testCaseDescription the test case description
     * @param annotationClass the annotation to search
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "dataContainsAnnotationTesting")
    public void testContainsAnnotationWorksAsExpected(final String testCaseDescription, final Class<? extends Annotation> annotationClass, final boolean expectedResult) {
        // GIVEN
        Constructor<?> constructor = ImmutableToFooCustomAnnotation.class.getConstructors()[0];


        // WHEN
        boolean actual = underTest.containsAnnotation(constructor, annotationClass);

        // THEN
        assertEquals(expectedResult, actual);
    }

    /**
     * Creates the parameters to be used for testing the method {@code containsAnnotation}.
     * @return parameters to be used for testing the the method {@code containsAnnotation}.
     */
    @DataProvider
    private Object[][] dataContainsAnnotationTesting() {
        return new Object[][] {
                {"Tests that the method returns true if the constructor contains parameters annotated with @ConstructorArg", ConstructorArg.class, true},
                {"Tests that the method returns false if the constructor does not contain parameters annotated with @NotNull", NotNull.class, false},
        };
    }

    /**
     * Tests that the method {@code notAllParameterAnnotatedWith} works as expected.
     * @param testCaseDescription the test case description
     * @param annotationClass the annotation to search
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "dataNotAllParameterAnnotatedWithTesting")
    public void testAllParameterAnnotatedWithWorksAsExpected(final String testCaseDescription, final Class<? extends Annotation> annotationClass, final boolean expectedResult) {
        // GIVEN
        Constructor<?> constructor = ImmutableToFooCustomAnnotation.class.getConstructors()[0];


        // WHEN
        boolean actual = underTest.allParameterAnnotatedWith(constructor, annotationClass);

        // THEN
        assertEquals(expectedResult, actual);
    }

    /**
     * Creates the parameters to be used for testing the method {@code notAllParameterAnnotatedWith}.
     * @return parameters to be used for testing the the method {@code notAllParameterAnnotatedWith}.
     */
    @DataProvider
    private Object[][] dataNotAllParameterAnnotatedWithTesting() {
        return new Object[][] {
                {"Tests that the method returns true if all constructor's parameter are annotated with @ConstructorArg", ConstructorArg.class, true},
                {"Tests that the method returns false if not all constructor's parameter are annotated with @NotNull", NotNull.class, false},
        };
    }

    /**
     * Tests that the method {@code getClassType} works as expected.
     * @param testCaseDescription the test case description
     * @param testClass the class to test
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "dataGetClassTypeTesting")
    public void testGetClassTypeWorksAsExpected(final String testCaseDescription, final Class<?> testClass, final ClassType expectedResult) {
        // GIVEN

        // WHEN
        final ClassType actual = underTest.getClassType(testClass);

        // THEN
        assertEquals(expectedResult, actual);
    }

    /**
     * Creates the parameters to be used for testing the method {@code getClassType}.
     * @return parameters to be used for testing the the method {@code getClassType}.
     */
    @DataProvider
    private Object[][] dataGetClassTypeTesting() {
        return new Object[][] {
                {"Tests that the method returns immutable if the given class is immutable", ImmutableToFoo.class, ClassType.IMMUTABLE},
                {"Tests that the method returns mutable if the given class is mutable", MutableToFoo.class, ClassType.MUTABLE},
                {"Tests that the method returns mixed if the given class contains both final and not fields", MixedToFoo.class, ClassType.MIXED}
        };
    }

    /**
     * Tests that the method {@code getSetterMethods} works as expected.
     * @param testCaseDescription the test case description
     * @param testClass the class to test
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "dataGetSetterMethodsTesting")
    public void testGetSetterMethodsWorksAsExpected(final String testCaseDescription, final Class<?> testClass, final boolean expectedResult) {
        // GIVEN

        // WHEN
        final List<Method> actual = underTest.getSetterMethods(testClass);

        // THEN
        assertEquals(expectedResult, actual.isEmpty());
    }

    /**
     * Creates the parameters to be used for testing the method {@code getSetterMethods}.
     * @return parameters to be used for testing the the method {@code getSetterMethods}.
     */
    @DataProvider
    private Object[][] dataGetSetterMethodsTesting() {
        return new Object[][] {
                {"Tests that the method returns an empty list if the class has no setter methods", ImmutableToFooSubClass.class, true},
                {"Tests that the method returns a not empty list if the class has setter methods", MutableToFoo.class, false}
        };
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
     * Creates the parameters to be used for testing the method {@code usesBuilderPattern}.
     * @return parameters to be used for testing the the method {@code usesBuilderPattern}.
     */
    @DataProvider
    private Object[][] dataUsesBuilderPatternTesting() {
        return new Object[][] {
                {"Tests that the method returns true if the class has a builder", MutableToFooWithBuilder.class, true},
                {"Tests that the method returns false if the class hasn't a builder", FromFoo.class, false}
        };
    }

    /**
     * Tests that the method {@code hasAccessibleConstructors} works as expected.
     * @param testCaseDescription the test case description
     * @param testClass the class to test
     * @param expectedResult the expected result
     */
    @Test(dataProvider = "dataHasAccessibleConstructorsTesting")
    public void testHasAccessibleConstructorsWorksAsExpected(final String testCaseDescription, final Class<?> testClass, final boolean expectedResult) {
        // GIVEN

        // WHEN
        final boolean actual = underTest.hasAccessibleConstructors(testClass);

        // THEN
        assertEquals(expectedResult, actual);
    }

    /**
     * Creates the parameters to be used for testing the method {@code hasAccessibleConstructors}.
     * @return parameters to be used for testing the the method {@code hasAccessibleConstructors}.
     */
    @DataProvider
    private Object[][] dataHasAccessibleConstructorsTesting() {
        return new Object[][] {
                {"Tests that the method returns false if the constructor is public", FromFoo.class, true},
                {"Tests that the method returns false if the constructor is private", MutableToFooWithBuilder.class, false}
        };
    }
}
