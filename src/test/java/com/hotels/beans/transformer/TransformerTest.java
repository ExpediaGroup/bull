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

package com.hotels.beans.transformer;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static com.hotels.beans.constant.ClassType.IMMUTABLE;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;

import com.hotels.beans.annotation.ConstructorArg;
import com.hotels.beans.cache.CacheManager;
import com.hotels.beans.error.InvalidBeanException;
import com.hotels.beans.error.MissingFieldException;
import com.hotels.beans.model.FieldMapping;
import com.hotels.beans.model.FieldTransformer;
import com.hotels.beans.sample.FromFoo;
import com.hotels.beans.sample.FromFooAdvFields;
import com.hotels.beans.sample.FromFooSimple;
import com.hotels.beans.sample.FromFooSimpleNoGetters;
import com.hotels.beans.sample.FromFooSubClass;
import com.hotels.beans.sample.FromFooWithPrimitiveFields;
import com.hotels.beans.sample.FromSubFoo;
import com.hotels.beans.sample.immutable.ImmutableToFoo;
import com.hotels.beans.sample.immutable.ImmutableToFooAdvFields;
import com.hotels.beans.sample.immutable.ImmutableToFooCustomAnnotation;
import com.hotels.beans.sample.immutable.ImmutableToFooDiffFields;
import com.hotels.beans.sample.immutable.ImmutableToFooInvalid;
import com.hotels.beans.sample.immutable.ImmutableToFooMissingCustomAnnotation;
import com.hotels.beans.sample.immutable.ImmutableToFooNoConstructors;
import com.hotels.beans.sample.immutable.ImmutableToFooNotExistingFields;
import com.hotels.beans.sample.immutable.ImmutableToFooSubClass;
import com.hotels.beans.sample.mixed.MixedToFoo;
import com.hotels.beans.sample.mixed.MixedToFooDiffFields;
import com.hotels.beans.sample.mixed.MixedToFooMissingAllArgsConstructor;
import com.hotels.beans.sample.mixed.MixedToFooMissingField;
import com.hotels.beans.sample.mixed.MixedToFooNotExistingFields;
import com.hotels.beans.sample.mutable.MutableToFoo;
import com.hotels.beans.sample.mutable.MutableToFooInvalid;
import com.hotels.beans.sample.mutable.MutableToFooNotExistingFields;
import com.hotels.beans.sample.mutable.MutableToFooSimpleNoSetters;
import com.hotels.beans.utils.ReflectionUtils;

/**
 * Unit test for {@link Transformer}.
 */
public class TransformerTest {
    private static final BigInteger ID = new BigInteger("1234");
    private static final String ITEM_1 = "donald";
    private static final String ITEM_2 = "duck";
    private static final String NAME = "Goofy";
    private static final String SURNAME = "surname";
    private static final int PHONE = 123;
    private static final String INDEX_NUMBER = null;
    private static final boolean CHECK = true;
    private static final BigDecimal AMOUNT = new BigDecimal(10);
    private static final String SUB_FOO_NAME = "Smith";
    private static final int[] SUB_FOO_PHONE_NUMBERS = {12345, 6892, 10873};
    private static final Map<String, String> SUB_FOO_SAMPLE_MAP = new HashMap<>();
    private static final Map<String, List<String>> SUB_FOO_COMPLEX_MAP = new HashMap<>();
    private static final Map<String, Map<String, String>> SUB_FOO_VERY_COMPLEX_MAP = new HashMap<>();
    private static final int AGE = 34;
    private static final int TOTAL_ADV_CLASS_FIELDS = 5;
    private static final String AGE_FIELD_NAME = "age";
    private static final String GET_DEST_FIELD_NAME_METHOD_NAME = "getDestFieldName";
    private static final String GET_CONSTRUCTOR_VALUES_FROM_FIELDS_METHOD_NAME = "getConstructorValuesFromFields";
    private static final String GET_SOURCE_FIELD_VALUE_METHOD_NAME = "getSourceFieldValue";
    private static final String DEST_FIELD_NAME = "destFieldName";
    private static final String REFLECTION_UTILS_FIELD_NAME = "reflectionUtils";
    private static final String TRANSFORMER_SETTINGS_FIELD_NAME = "transformerSettings";
    private static final String CONSTRUCTOR_PARAMETER_NAME = "constructorParameterName";
    private static final ReflectionUtils REFLECTION_UTILS = new ReflectionUtils();
    private static FromFoo fromFoo;
    private static FromFooWithPrimitiveFields fromFooWithPrimitiveFields;
    private static List<FromSubFoo> fromSubFooList;
    private static List<String> sourceFooSimpleList;
    private static FromSubFoo fromSubFoo;
    private static FromFooSubClass fromFooSubClass;
    private static FromFooAdvFields fromFooAdvFields;

    /**
     * The class to be tested.
     */
    @InjectMocks
    private TransformerImpl underTest;

    /**
     * Initializes the arguments and objects.
     */
    @BeforeClass
    public static void beforeClass() {
        initObjects();
    }

    /**
     * Initialized mocks.
     */
    @Before
    public void beforeMethod() {
        initMocks(this);
    }


    /**
     * Test that is possible to remove a field mapping for a given field.
     */
    @Test
    public void testRemoveFieldMappingWorksProperly() {
        //GIVEN
        Transformer beanTransformer = underTest.withFieldMapping(new FieldMapping("sourceFieldName", DEST_FIELD_NAME));

        //WHEN
        beanTransformer.removeFieldMapping(DEST_FIELD_NAME);
        TransformerSettings transformerSettings = (TransformerSettings) REFLECTION_UTILS.getFieldValue(beanTransformer, TRANSFORMER_SETTINGS_FIELD_NAME, TransformerSettings.class);

        //THEN
        assertFalse(transformerSettings.getFieldsNameMapping().containsKey(DEST_FIELD_NAME));
    }

    /**
     * Test that the method {@code removeFieldMapping} raises an {@link IllegalArgumentException} if the parameter is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemoveFieldMappingRaisesExceptionIfItsCalledWithNullParam() {
        //GIVEN
        Transformer beanTransformer = underTest.withFieldMapping(new FieldMapping("sourceFieldName", DEST_FIELD_NAME));

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
                .withFieldMapping(new FieldMapping("sourceFieldName", DEST_FIELD_NAME), new FieldMapping("sourceFieldName2", DEST_FIELD_NAME));

        //WHEN
        beanTransformer.resetFieldsMapping();
        TransformerSettings transformerSettings = (TransformerSettings) REFLECTION_UTILS.getFieldValue(beanTransformer, TRANSFORMER_SETTINGS_FIELD_NAME, TransformerSettings.class);

        //THEN
        assertTrue(transformerSettings.getFieldsNameMapping().isEmpty());
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
    @Test(expected = IllegalArgumentException.class)
    public void testRemoveFieldTransformerRaisesExceptionIfItsCalledWithNullParam() {
        //GIVEN
        Transformer beanTransformer = underTest.withFieldTransformer(new FieldTransformer<>(DEST_FIELD_NAME, val -> val));

        //WHEN
        beanTransformer.removeFieldTransformer(null);
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
     * Test that immutable beans without constructor arguments parameter annotated with: @ConstructorArg {@link com.hotels.beans.annotation.ConstructorArg} are correctly copied.
     */
    @Test
    public void testBeanWithoutCustomAnnotationIsCorrectlyCopied() {
        //GIVEN

        //WHEN
        ImmutableToFoo actual = underTest.transform(fromFoo, ImmutableToFoo.class);

        //THEN
        assertThat(actual, sameBeanAs(fromFoo));
    }

    /**
     * Test that immutable beans without custom field mapping.
     */
    @Test
    public void testBeanWithEmptyFieldMappingIsCorrectlyCopied() {
        //GIVEN

        //WHEN
        ImmutableToFoo actual = underTest.withFieldMapping().transform(fromFoo, ImmutableToFoo.class);

        //THEN
        assertThat(actual, sameBeanAs(fromFoo));
    }

    /**
     * Test that immutable beans without custom field mapping.
     */
    @Test
    public void testBeanWithEmptyFieldTransformerIsCorrectlyCopied() {
        //GIVEN

        //WHEN
        ImmutableToFoo actual = underTest.withFieldMapping().transform(fromFoo, ImmutableToFoo.class);

        //THEN
        assertThat(actual, sameBeanAs(fromFoo));
    }

    /**
     * Test that immutable beans with constructor arguments parameter annotated with: @ConstructorArg {@link com.hotels.beans.annotation.ConstructorArg} are correctly copied.
     */
    @Test
    public void testBeanWithCustomAnnotationIsCorrectlyCopied() {
        //GIVEN

        //WHEN
        ImmutableToFooCustomAnnotation actual = underTest.transform(fromFoo, ImmutableToFooCustomAnnotation.class);

        //THEN
        assertThat(actual, sameBeanAs(fromFoo));
    }

    /**
     * Test that an exception is thrown if the constructor invocation throws exception.
     */
    @Test(expected = InvalidBeanException.class)
    public void testTransformThrowsExceptionIfTheConstructorInvocationThrowsException() {
        //GIVEN
        FromFoo actual = new FromFoo(NAME, ID, null, null, null);

        //WHEN
        underTest.transform(actual, ImmutableToFooCustomAnnotation.class);
    }

    /**
     * Test that an exception is thrown if no constructors are defined.
     */
    @Test(expected = InvalidBeanException.class)
    public void testTransformThrowsExceptionWhenParameterAreNull() {
        //GIVEN
        FromFoo actual = new FromFoo(NAME, ID, null, null, null);

        //WHEN
        underTest.transform(actual, ImmutableToFooNoConstructors.class);
    }

    /**
     * Test that an exception is thrown if there is no default constructor defined for the mutable bean object.
     */
    @Test(expected = InvalidBeanException.class)
    public void testTransformThrowsExceptionWhenMutableBeanHasNoDefaultConstructor() {
        //GIVEN

        //WHEN
        underTest.transform(fromFoo, MutableToFooInvalid.class);
    }

    /**
     * Test that an exception is thrown if there the constructor args parameters have a different order for the mutable bean object.
     */
    @Test(expected = InvalidBeanException.class)
    public void testTransformThrowsExceptionWhenImmutableBeanHasAWrongConstructor() {
        //GIVEN

        //WHEN
        underTest.transform(fromFoo, ImmutableToFooInvalid.class);
    }

    /**
     * Test that a Mixed bean with a constructor containing the final field only is correctly copied.
     */
    @Test
    public void testMixedBeanWithoutAllArgsConstructorIsCorrectlyCopied() {
        //GIVEN

        //WHEN
        MixedToFooMissingAllArgsConstructor actual = underTest.transform(fromFoo, MixedToFooMissingAllArgsConstructor.class);

        //THEN
        assertThat(actual, sameBeanAs(fromFoo));
    }

     /**
     * Test mutable beans are correctly copied.
     */
    @Test
    public void testMutableBeanIsCorrectlyCopied() {
        //GIVEN

        //WHEN
        MutableToFoo actual = underTest.transform(fromFoo, MutableToFoo.class);

        //THEN
        assertThat(actual, sameBeanAs(fromFoo));
    }

    /**
     * Test that bean that extends another class are correctly copied.
     */
    @Test
    public void testBeanWithoutCustomAnnotationAndWithSuperclassIsCorrectlyCopied() {
        //GIVEN

        //WHEN
        ImmutableToFooSubClass actual = underTest.transform(fromFooSubClass, ImmutableToFooSubClass.class);

        //THEN
        assertThat(actual, sameBeanAs(fromFooSubClass));
    }

    /**
     * Test that bean containing both final fields and not are correctly copied.
     */
    @Test
    public void testMixedBeanIsCorrectlyCopied() {
        //GIVEN

        //WHEN
        MixedToFoo actual = underTest.transform(fromFoo, MixedToFoo.class);

        //THEN
        assertThat(actual, sameBeanAs(fromFoo));
    }

    /**
     * Test that bean containing both final fields (with different names) and not are correctly copied.
     */
    @Test
    public void testMixedBeanWithDifferentFieldNamesIsCorrectlyCopied() {
        //GIVEN

        //WHEN
        final Transformer beanTransformer = underTest.withFieldMapping(new FieldMapping("id", "identifier"));
        MixedToFooDiffFields actual = beanTransformer.transform(fromFoo, MixedToFooDiffFields.class);

        //THEN
        assertThat(actual, hasProperty("name", equalTo(actual.getName())));
        assertThat(actual, hasProperty("identifier", equalTo(fromFoo.getId())));
        assertEquals(actual.getList(), fromFoo.getList());
        IntStream.range(0, actual.getNestedObjectList().size())
                .forEach(i -> assertThat(actual.getNestedObjectList().get(i), sameBeanAs(fromFoo.getNestedObjectList().get(i))));
        assertThat(actual.getNestedObject(), sameBeanAs(fromFoo.getNestedObject()));
    }

    /**
     * Test that bean containing both final fields (with different names) and not are correctly copied through field transformer.
     */
    @Test
    public void testMixedBeanWithDifferentFieldNamesIsCorrectlyCopiedThroughFieldTransformer() {
        //GIVEN
        /* Extended declaration.
         * Function<BigInteger, BigInteger> idTransformer = value -> value.negate();
         * FieldTransformer<BigInteger, BigInteger> fieldTransformer = new FieldTransformer<>("identifier", idTransformer);
         */
        FieldTransformer<BigInteger, BigInteger> fieldTransformer = new FieldTransformer<>("identifier", BigInteger::negate);

        //WHEN
        underTest.withFieldMapping(new FieldMapping("id", "identifier")).withFieldTransformer(fieldTransformer);
        MixedToFooDiffFields actual = underTest.transform(fromFoo, MixedToFooDiffFields.class);

        //THEN
        assertThat(actual, hasProperty("name", equalTo(actual.getName())));
        assertThat(actual, hasProperty("identifier", equalTo(fromFoo.getId().negate())));
        assertEquals(actual.getList(), fromFoo.getList());
        IntStream.range(0, actual.getNestedObjectList().size())
                .forEach(i -> assertThat(actual.getNestedObjectList().get(i), sameBeanAs(fromFoo.getNestedObjectList().get(i))));
        assertThat(actual.getNestedObject(), sameBeanAs(fromFoo.getNestedObject()));
    }

    /**
     * Test that a bean containing a field not existing in the source object, but with a transformer function defined for such object is correctly copied.
     */
    @Test
    public void testThatAnyTypeOfBeanContainsANotExistingFieldInTheSourceObjectIsCorrectlyCopyedThroughTransformerFunctions() {
        //GIVEN
        FromFooSimple fromFooSimple = new FromFooSimple(NAME, ID);
        FieldTransformer<Object, Integer> ageFieldTransformer = new FieldTransformer<>(AGE_FIELD_NAME, val -> AGE);

        //WHEN
        underTest.withFieldTransformer(ageFieldTransformer);
        MixedToFooNotExistingFields mixedObjectBean = underTest.transform(fromFooSimple, MixedToFooNotExistingFields.class);
        ImmutableToFooNotExistingFields immutableObjectBean = underTest.transform(fromFooSimple, ImmutableToFooNotExistingFields.class);
        MutableToFooNotExistingFields mutableObjectBean = underTest.transform(fromFooSimple, MutableToFooNotExistingFields.class);

        //THEN
        assertThat(mixedObjectBean, hasProperty(AGE_FIELD_NAME, equalTo(AGE)));
        assertThat(immutableObjectBean, hasProperty(AGE_FIELD_NAME, equalTo(AGE)));
        assertThat(mutableObjectBean, hasProperty(AGE_FIELD_NAME, equalTo(AGE)));
    }

    /**
     * Test that the copy method sets the default value when the source object does not contain a required field.
     */
    @Test
    public void testMixedBeanWithMissingFieldsReturnsTheDefaultValueWhenTheSourceObjectDoesNotContainARequiredField() {
        //GIVEN
        underTest.setDefaultValueForMissingField(true);

        //WHEN
        MixedToFooMissingField actual = underTest.transform(fromFoo, MixedToFooMissingField.class);

        assertNull(actual.getFooField());
        underTest.setDefaultValueForMissingField(false);
    }

    /**
     * Test that the copy method raises an exception when the source object does not contain a required field.
     */
    @Test(expected = MissingFieldException.class)
    public void testMixedBeanWithMissingFieldsThrowsMissingFieldExceptionWhenTheSourceObjectDoesNotContainARequiredField() {
        //GIVEN
        underTest.setDefaultValueForMissingField(false);

        //WHEN
        underTest.transform(fromFoo, MixedToFooMissingField.class);
    }

    /**
     * Test that bean containing final fields (with different field names) are correctly copied.
     */
    @Test
    public void testImmutableBeanWithDifferentFieldNamesIsCorrectlyCopied() {
        //GIVEN

        //WHEN
        final Transformer beanTransformer = underTest.withFieldMapping(new FieldMapping("id", "identifier"));
        ImmutableToFooDiffFields actual = beanTransformer.transform(fromFoo, ImmutableToFooDiffFields.class);

        //THEN
        assertThat(actual, hasProperty("name", equalTo(actual.getName())));
        assertThat(actual, hasProperty("identifier", equalTo(fromFoo.getId())));
        assertEquals(actual.getList(), fromFoo.getList());
        IntStream.range(0, actual.getNestedObjectList().size())
                .forEach(i -> assertThat(actual.getNestedObjectList().get(i), sameBeanAs(fromFoo.getNestedObjectList().get(i))));
        assertThat(actual.getNestedObject(), sameBeanAs(fromFoo.getNestedObject()));
    }

    /**
     * Test that bean containing advanced final fields are correctly copied.
     */
    @Test
    public void testImmutableBeanWithAdvancedFieldsIsCorrectlyCopied() {
        //GIVEN

        //WHEN
        final Transformer beanTransformer = underTest
                .withFieldMapping(new FieldMapping("id", "identifier"))
                .withFieldTransformer(new FieldTransformer<>("locale", Locale::forLanguageTag));
        ImmutableToFooAdvFields actual = beanTransformer.transform(fromFooAdvFields, ImmutableToFooAdvFields.class);

        //THEN
        assertNotNull(actual.getName());
        assertTrue(actual.getName().isPresent());
        assertEquals(fromFooAdvFields.getName().get(), actual.getName().get());
        assertEquals(fromFooAdvFields.getAge().get(), actual.getAge());
        assertEquals(fromFooAdvFields.getClassType(), actual.getClassType());
        assertEquals(fromFooAdvFields.getLocale(), actual.getLocale().getLanguage());
    }

    /**
     * Test that immutable bean containing a constructor with some field not annotated with
     * {@link com.hotels.beans.annotation.ConstructorArg} is correctly copied.
     */
    @Test
    public void testImmutableBeanWithMissingConstructorArgIsCorrectlyCopied() {
        //GIVEN

        //WHEN
        ImmutableToFooMissingCustomAnnotation actual = underTest.withFieldTransformer().transform(fromFooWithPrimitiveFields, ImmutableToFooMissingCustomAnnotation.class);

        //THEN
        assertNotNull(actual);
        assertEquals(fromFooWithPrimitiveFields.getName(), actual.getName());
    }

    /**
     * Test that method: {@code getDestFieldName} retrieves the param name from the {@link ConstructorArg} if it is not provided from jvm directly.
     */
    @Test
    public void testGetDestFieldNameIsRetrievedFromConstructorArgIfTheParamNameIsNotProvidedFromJVM() throws Exception {
        //GIVEN
        String declaringClassName = ImmutableToFoo.class.getName();
        // Parameter mock setup
        Parameter constructorParameter = mock(Parameter.class);
        when(constructorParameter.getName()).thenReturn(CONSTRUCTOR_PARAMETER_NAME);

        // CacheManager mock setup
        initGetDestFieldNameTestMock(declaringClassName, constructorParameter);
        Method getDestFieldNameMethod = underTest.getClass().getDeclaredMethod(GET_DEST_FIELD_NAME_METHOD_NAME, Parameter.class, String.class);
        getDestFieldNameMethod.setAccessible(true);

        //WHEN
        String actual = (String) getDestFieldNameMethod.invoke(underTest, constructorParameter, declaringClassName);

        //THEN
        assertEquals(DEST_FIELD_NAME, actual);

        // restore modified objects
        restoreObjects(getDestFieldNameMethod);
    }

    /**
     * Test that the method: {@code getConstructorValuesFromFields} works properly.
     */
    @Test
    public void testGetConstructorValuesFromFieldsWorksProperly() throws Exception {
        //GIVEN
        underTest.withFieldTransformer(new FieldTransformer<>("locale", Locale::forLanguageTag));

        //WHEN
        final Method getConstructorValuesFromFieldsMethod = underTest.getClass().getDeclaredMethod(GET_CONSTRUCTOR_VALUES_FROM_FIELDS_METHOD_NAME, Object.class, Class.class);
        getConstructorValuesFromFieldsMethod.setAccessible(true);
        Object[] actual = (Object[]) getConstructorValuesFromFieldsMethod.invoke(underTest, fromFooAdvFields, ImmutableToFooAdvFields.class);

        //THEN
        assertNotNull(actual);
        assertEquals(TOTAL_ADV_CLASS_FIELDS, actual.length);

        // restore modified objects
        restoreObjects(getConstructorValuesFromFieldsMethod);
    }

    /**
     * Test that method: {@code getDestFieldName} returns null if the constructor's parameter name is not provided from jvm directly and the {@link ConstructorArg} is not defined.
     */
    @Test
    public void testGetDestFieldNameReturnsNullIfConstructorParamHasNoNameProvidedFromJVMAndNoConstructorArgIsDefined() throws Exception {
        //GIVEN
        String declaringClassName = ImmutableToFoo.class.getName();
        // Parameter mock setup
        Parameter constructorParameter = mock(Parameter.class);
        when(constructorParameter.getName()).thenReturn(null);

        // CacheManager mock setup
        initGetDestFieldNameTestMock(declaringClassName, constructorParameter);
        Method getDestFieldNameMethod = underTest.getClass().getDeclaredMethod(GET_DEST_FIELD_NAME_METHOD_NAME, Parameter.class, String.class);
        getDestFieldNameMethod.setAccessible(true);

        //WHEN
        String actual = (String) getDestFieldNameMethod.invoke(underTest, constructorParameter, declaringClassName);

        //THEN
        assertEquals(DEST_FIELD_NAME, actual);

        // restore modified objects
        restoreObjects(getDestFieldNameMethod);
    }

    /**
     * Test that the transformer is able to copy object even if the source object has no setter methods and the destination
     * object has no setter methods.
     */
    @Test
    public void testTransformerIsAbleToCopyObjectsWithoutRequiredMethods() {
        //GIVEN
        FromFooSimpleNoGetters fromFooSimpleNoGetters = new FromFooSimpleNoGetters(NAME, ID);
        //WHEN
        MutableToFooSimpleNoSetters actual = underTest.transform(fromFooSimpleNoGetters, MutableToFooSimpleNoSetters.class);

        //THEN
        assertThat(actual, sameBeanAs(fromFooSimpleNoGetters));
    }

    /**
     * Test that the method: {@code getSourceFieldValue} raises a {@link NullPointerException} in case any of the parameters null.
     */
    @Test(expected = Exception.class)
    public void testGetSourceFieldValueRaisesAnExceptionIfTheParameterAreNull() throws Exception {
        //GIVEN
        Method getSourceFieldValueMethod = underTest.getClass().getDeclaredMethod(GET_SOURCE_FIELD_VALUE_METHOD_NAME, Object.class, String.class, Field.class, boolean.class);
        getSourceFieldValueMethod.setAccessible(true);

        //WHEN
        getSourceFieldValueMethod.invoke(underTest, null, null, null, false);
    }

    /**
     * Initializes the mocks required for testing method: {@code getDestFieldName}.
     * @param declaringClassName the declaring class name
     * @param constructorParameter the constructor parameter
     */
    private void initGetDestFieldNameTestMock(final String declaringClassName, final Parameter constructorParameter) {
        CacheManager cacheManager = mock(CacheManager.class);
        String cacheKey = "DestFieldName-" + declaringClassName + "-" + CONSTRUCTOR_PARAMETER_NAME;
        when(cacheManager.getFromCache(cacheKey, String.class)).thenReturn(null);
        // ConstructorArg mock setup
        ConstructorArg constructorArg = mock(ConstructorArg.class);
        when(constructorArg.value()).thenReturn(DEST_FIELD_NAME);
        // ReflectionUtils mock setup
        ReflectionUtils reflectionUtils = mock(ReflectionUtils.class);
        when(reflectionUtils.getParameterAnnotation(constructorParameter, ConstructorArg.class, declaringClassName)).thenReturn(constructorArg);
        setField(underTest, REFLECTION_UTILS_FIELD_NAME, reflectionUtils);
    }

    /**
     * Restored the initail object status before testing method: {@code getDestFieldName}.
     */
    private void restoreObjects(final Method getDestFieldNameMethod) {
        getDestFieldNameMethod.setAccessible(false);
        setField(underTest, REFLECTION_UTILS_FIELD_NAME, new ReflectionUtils());
    }

    /**
     * Create an instance of two objects: one without custom annotation and another one with custom annotations then execute the copy into a specular immutable object.
     */
    private static void initObjects() {
        SUB_FOO_SAMPLE_MAP.put(ITEM_1, ITEM_2);
        SUB_FOO_COMPLEX_MAP.put(ITEM_1, singletonList(ITEM_2));
        SUB_FOO_VERY_COMPLEX_MAP.put(ITEM_1, SUB_FOO_SAMPLE_MAP);
        fromSubFoo = new FromSubFoo(SUB_FOO_NAME, SUB_FOO_PHONE_NUMBERS, SUB_FOO_SAMPLE_MAP, SUB_FOO_COMPLEX_MAP, SUB_FOO_VERY_COMPLEX_MAP);
        fromSubFooList = singletonList(fromSubFoo);
        sourceFooSimpleList = asList(ITEM_1, ITEM_2);
        fromFoo = createFromFoo();
        fromFooWithPrimitiveFields = createFromFooWithPrimitiveFields();
        fromFooSubClass = createFromFooSubClass();
        fromFooAdvFields = createFromFooAdvFields();
    }

    /**
     * Creates a {@link FromFoo} instance.
     * @return the {@link FromFoo} instance.
     */
    private static FromFoo createFromFoo() {
        return new FromFoo(NAME, ID, fromSubFooList, sourceFooSimpleList, fromSubFoo);
    }

    /**
     * Creates a {@link FromFooWithPrimitiveFields} instance.
     * @return the {@link FromFooWithPrimitiveFields} instance.
     */
    private static FromFooWithPrimitiveFields createFromFooWithPrimitiveFields() {
        return new FromFooWithPrimitiveFields(NAME, ID.intValue(), AGE, fromSubFooList, sourceFooSimpleList, fromSubFoo);
    }

    /**
     * Creates a {@link FromFooSubClass} instance.
     * @return the {@link FromFooSubClass} instance.
     */
    private static FromFooSubClass createFromFooSubClass() {
        return new FromFooSubClass(fromFoo.getName(), fromFoo.getId(), fromFoo.getNestedObjectList(), fromFoo.getList(), fromFoo.getNestedObject(), SURNAME, PHONE, CHECK, AMOUNT);
    }

    /**
     * Creates a {@link FromFooAdvFields} instance.
     * @return the {@link FromFooAdvFields} instance.
     */
    private static FromFooAdvFields createFromFooAdvFields() {
        return new FromFooAdvFields(Optional.of(NAME), Optional.of(AGE), INDEX_NUMBER, IMMUTABLE, Locale.ENGLISH.getLanguage());
    }

}
