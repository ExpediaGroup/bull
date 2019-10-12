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

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import static com.hotels.transformer.constant.ClassType.IMMUTABLE;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import com.hotels.beans.sample.FromFoo;
import com.hotels.beans.sample.FromFooAdvFields;
import com.hotels.beans.sample.FromFooMap;
import com.hotels.beans.sample.FromFooOnlyPrimitiveTypes;
import com.hotels.beans.sample.FromFooSimple;
import com.hotels.beans.sample.FromFooSubClass;
import com.hotels.beans.sample.FromFooWithPrimitiveFields;
import com.hotels.beans.sample.FromSubFoo;

/**
 * Unit test for {@link com.hotels.transformer.Transformer}.
 */
public abstract class AbstractTransformerTest {
    public static FromFoo fromFoo;
    protected static final BigInteger ID = new BigInteger("1234");
    protected static final String NAME = "Goofy";
    protected static FromFoo fromFooWithNullProperties;
    protected static FromFooSimple fromFooSimple;
    protected static FromFooWithPrimitiveFields fromFooWithPrimitiveFields;
    protected static FromFooSubClass fromFooSubClass;
    protected static FromFooAdvFields fromFooAdvFields;
    protected static FromFooMap fromFooMap;
    protected static FromFooOnlyPrimitiveTypes fromFooPrimitiveTypes;
    protected static final int AGE = 34;
    protected static final String AGE_FIELD_NAME = "age";
    protected static final String DEST_FIELD_NAME = "destFieldName";
    protected static final String CONSTRUCTOR_PARAMETER_NAME = "constructorParameterName";
    protected static final String REFLECTION_UTILS_FIELD_NAME = "reflectionUtils";
    protected static final String ID_FIELD_NAME = "id";
    protected static final String IDENTIFIER_FIELD_NAME = "identifier";
    protected static final String LOCALE_FIELD_NAME = "locale";
    protected static final String PHONE_NUMBER_DEST_FIELD_NAME = "phoneNumbers";
    protected static final String PHONE_NUMBER_NESTED_OBJECT_FIELD_NAME = "nestedObject.phoneNumbers";
    protected static final String NAME_FIELD_NAME = "name";
    protected static final float PRICE = 10.0f;
    protected static final Map<String, String> SAMPLE_MAP = new HashMap<>();
    protected static final Map<String, List<String>> COMPLEX_MAP = new HashMap<>();
    protected static final Map<String, Map<String, String>> VERY_COMPLEX_MAP = new HashMap<>();
    protected static final Map<FromFooSimple, Map<String, String>> EXTREME_COMPLEX_MAP = new HashMap<>();
    protected static final String ITEM_1 = "donald";
    private static final String ITEM_2 = "duck";
    private static final String SURNAME = "surname";
    private static final boolean ACTIVE = true;
    private static final int PHONE = 123;
    private static final String INDEX_NUMBER = null;
    private static final boolean CHECK = true;
    private static final BigDecimal AMOUNT = new BigDecimal(10);
    private static final String SUB_FOO_NAME = "Smith";
    private static final int[] SUB_FOO_PHONE_NUMBERS = {12345, 6892, 10873};
    private static List<FromSubFoo> fromSubFooList;
    private static List<String> sourceFooSimpleList;
    private static FromSubFoo fromSubFoo;

    /**
     * Create an instance of two objects: one without custom annotation and another one with custom annotations then execute the copy into a specular immutable object.
     */
    protected void initObjects() {
        SAMPLE_MAP.put(ITEM_1, ITEM_2);
        COMPLEX_MAP.put(ITEM_1, singletonList(ITEM_2));
        VERY_COMPLEX_MAP.put(ITEM_1, SAMPLE_MAP);
        fromSubFoo = new FromSubFoo(SUB_FOO_NAME, SUB_FOO_PHONE_NUMBERS, SAMPLE_MAP, COMPLEX_MAP, VERY_COMPLEX_MAP);
        fromSubFooList = singletonList(fromSubFoo);
        sourceFooSimpleList = asList(ITEM_1, ITEM_2);
        fromFoo = createFromFoo(fromSubFoo);
        fromFooWithNullProperties = createFromFoo(null);
        fromFooSimple = createFromFooSimple();
        fromFooWithPrimitiveFields = createFromFooWithPrimitiveFields();
        fromFooSubClass = createFromFooSubClass();
        fromFooAdvFields = createFromFooAdvFields();
        fromFooPrimitiveTypes = createFromFooPrimitiveTypes();
        EXTREME_COMPLEX_MAP.put(fromFooSimple, SAMPLE_MAP);
        fromFooMap = new FromFooMap(SAMPLE_MAP, COMPLEX_MAP, VERY_COMPLEX_MAP, EXTREME_COMPLEX_MAP);
    }

    /**
     * Creates a {@link FromFoo} instance.
     * @param fromSubFoo the {@link FromSubFoo} instance
     * @return the {@link FromFoo} instance.
     */
    private FromFoo createFromFoo(final FromSubFoo fromSubFoo) {
        return new FromFoo(NAME, ID, fromSubFooList, sourceFooSimpleList, fromSubFoo);
    }

    /**
     * Creates a {@link FromFooSimple} instance.
     * @return the {@link FromFooSimple} instance.
     */
    private FromFooSimple createFromFooSimple() {
        return new FromFooSimple(NAME, ID, ACTIVE);
    }

    /**
     * Creates a {@link FromFooWithPrimitiveFields} instance.
     * @return the {@link FromFooWithPrimitiveFields} instance.
     */
    private FromFooWithPrimitiveFields createFromFooWithPrimitiveFields() {
        return new FromFooWithPrimitiveFields(NAME, ID.intValue(), AGE, fromSubFooList, sourceFooSimpleList, fromSubFoo);
    }

    /**
     * Creates a {@link FromFooSubClass} instance.
     * @return the {@link FromFooSubClass} instance.
     */
    private FromFooSubClass createFromFooSubClass() {
        return new FromFooSubClass(fromFoo.getName(), fromFoo.getId(), fromFoo.getNestedObjectList(), fromFoo.getList(), fromFoo.getNestedObject(), SURNAME, PHONE, CHECK, AMOUNT);
    }

    /**
     * Creates a {@link FromFooAdvFields} instance.
     * @return the {@link FromFooAdvFields} instance.
     */
    private FromFooAdvFields createFromFooAdvFields() {
        return new FromFooAdvFields(Optional.of(NAME), Optional.of(AGE), INDEX_NUMBER, IMMUTABLE, Locale.ENGLISH.getLanguage(), PRICE);
    }

    /**
     * Creates a {@link FromFooOnlyPrimitiveTypes} instance.
     * @return the {@link FromFooOnlyPrimitiveTypes} instance.
     */
    private FromFooOnlyPrimitiveTypes createFromFooPrimitiveTypes() {
        return new FromFooOnlyPrimitiveTypes(ID.toString(), ID.intValue(), PRICE, String.valueOf(ACTIVE));
    }
}
