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
package com.expediagroup.transformer

import com.expediagroup.beans.sample.FromFoo
import com.expediagroup.beans.sample.FromFooAdvFields
import com.expediagroup.beans.sample.FromFooMap
import com.expediagroup.beans.sample.FromFooOnlyPrimitiveTypes
import com.expediagroup.beans.sample.FromFooSimple
import com.expediagroup.beans.sample.FromFooSubClass
import com.expediagroup.beans.sample.FromFooWithPrimitiveFields
import com.expediagroup.beans.sample.FromSubFoo
import com.expediagroup.transformer.constant.ClassType
import com.expediagroup.transformer.utils.ReflectionUtils
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

/**
 * Unit test for [Transformer].
 */
abstract class AbstractTransformerTest {
    /**
     * Create an instance of two objects: one without custom annotation and another one with custom annotations then execute the copy into a specular immutable object.
     */
    protected fun initObjects() {
        SAMPLE_MAP[ITEM_1] =
            ITEM_2
        COMPLEX_MAP[ITEM_1] =
            listOf(ITEM_2)
        VERY_COMPLEX_MAP[ITEM_1] = SAMPLE_MAP
        fromSubFoo = FromSubFoo(SUB_FOO_NAME, SUB_FOO_PHONE_NUMBERS, SAMPLE_MAP, COMPLEX_MAP, VERY_COMPLEX_MAP)
        fromSubFooList = listOf(fromSubFoo)
        sourceFooSimpleList = listOf(ITEM_1, ITEM_2)
        fromFoo = createFromFoo(fromSubFoo)
        fromFooWithNullProperties = createFromFoo(null)
        fromFooSimple = createFromFooSimple()
        fromFooWithPrimitiveFields = createFromFooWithPrimitiveFields()
        fromFooSubClass = createFromFooSubClass()
        fromFooAdvFields = createFromFooAdvFields()
        fromFooPrimitiveTypes = createFromFooPrimitiveTypes()
        EXTREME_COMPLEX_MAP[fromFooSimple] =
            SAMPLE_MAP
        fromFooMap = FromFooMap(SAMPLE_MAP, COMPLEX_MAP, VERY_COMPLEX_MAP, EXTREME_COMPLEX_MAP, UNPARAMETRIZED_MAP)
    }

    /**
     * Creates a [FromFoo] instance.
     * @param fromSubFoo the [FromSubFoo] instance
     * @return the [FromFoo] instance.
     */
    private fun createFromFoo(fromSubFoo: FromSubFoo?): FromFoo {
        return FromFoo(NAME, ID, fromSubFooList, sourceFooSimpleList, fromSubFoo)
    }

    /**
     * Creates a [FromFooSimple] instance.
     * @return the [FromFooSimple] instance.
     */
    private fun createFromFooSimple(): FromFooSimple {
        return FromFooSimple(NAME, ID, ACTIVE)
    }

    /**
     * Creates a [FromFooWithPrimitiveFields] instance.
     * @return the [FromFooWithPrimitiveFields] instance.
     */
    private fun createFromFooWithPrimitiveFields(): FromFooWithPrimitiveFields {
        return FromFooWithPrimitiveFields(NAME, ID.toInt(), AGE, fromSubFooList, sourceFooSimpleList, fromSubFoo)
    }

    /**
     * Creates a [SubClass] instance.
     * @return the [SubClass] instance.
     */
    private fun createFromFooSubClass(): FromFooSubClass {
        return FromFooSubClass(
            fromFoo.name,
            fromFoo.id,
            fromFoo.nestedObjectList,
            fromFoo.list,
            fromFoo.nestedObject,
            SURNAME,
            PHONE,
            CHECK,
            AMOUNT
        )
    }

    /**
     * Creates a [FromFooAdvFields] instance.
     * @return the [FromFooAdvFields] instance.
     */
    private fun createFromFooAdvFields(): FromFooAdvFields {
        return FromFooAdvFields(
            Optional.of(NAME),
            Optional.of(AGE),
            INDEX_NUMBER,
            ClassType.IMMUTABLE,
            Locale.ENGLISH.language,
            PRICE,
            sourceFooSimpleList,
            sourceFooSimpleList as Collection<Any>?,
            SAMPLE_MAP,
            SAMPLE_MAP as Map<Any, Any>,
            fromSubFoo
        )
    }

    /**
     * Creates a [FromFooOnlyPrimitiveTypes] instance.
     * @return the [FromFooOnlyPrimitiveTypes] instance.
     */
    private fun createFromFooPrimitiveTypes(): FromFooOnlyPrimitiveTypes {
        return FromFooOnlyPrimitiveTypes(ID.toString(), ID.toInt(), PRICE, ACTIVE.toString(), UUID.randomUUID())
    }

    companion object {
        val ID = BigInteger("1234")
        const val NAME = "Goofy"
        lateinit var fromFoo: FromFoo
        lateinit var fromFooWithNullProperties: FromFoo
        lateinit var fromFooSimple: FromFooSimple
        lateinit var fromFooWithPrimitiveFields: FromFooWithPrimitiveFields
        lateinit var fromFooSubClass: FromFooSubClass
        lateinit var fromFooAdvFields: FromFooAdvFields
        lateinit var fromFooMap: FromFooMap
        lateinit var fromFooPrimitiveTypes: FromFooOnlyPrimitiveTypes
        const val AGE = 34
        const val AGE_FIELD_NAME = "age"
        const val DEST_FIELD_NAME = "destFieldName"
        const val CONSTRUCTOR_PARAMETER_NAME = "constructorParameterName"
        const val REFLECTION_UTILS_FIELD_NAME = "reflectionUtils"
        const val ID_FIELD_NAME = "id"
        const val IDENTIFIER_FIELD_NAME = "identifier"
        const val LOCALE_FIELD_NAME = "locale"
        const val PHONE_NUMBER_DEST_FIELD_NAME = "phoneNumbers"
        const val PHONE_NUMBER_NESTED_OBJECT_FIELD_NAME = "nestedObject.phoneNumbers"
        const val NAME_FIELD_NAME = "name"
        const val ACTIVE_FIELD_NAME = "active"
        const val PRICE = 10.0f
        val SAMPLE_MAP: MutableMap<String, String> = HashMap()
        val COMPLEX_MAP: MutableMap<String, List<String>> = HashMap()
        val VERY_COMPLEX_MAP: MutableMap<String, Map<String, String>> = HashMap()
        val EXTREME_COMPLEX_MAP: MutableMap<FromFooSimple?, Map<String, String>> = HashMap()
        val UNPARAMETRIZED_MAP: Map<*, *> = HashMap<Any, Any>()
        const val TRANSFORMER_SETTINGS_FIELD_NAME = "settings"
        val REFLECTION_UTILS = ReflectionUtils()
        const val ITEM_1 = "donald"
        private const val ITEM_2 = "duck"
        private const val SURNAME = "surname"
        private const val ACTIVE = true
        private const val PHONE = 123
        private lateinit var INDEX_NUMBER: String
        private const val CHECK = true
        private val AMOUNT = BigDecimal(10)
        private const val SUB_FOO_NAME = "Smith"
        private val SUB_FOO_PHONE_NUMBERS = intArrayOf(12345, 6892, 10873)
        private lateinit var fromSubFooList: List<FromSubFoo?>
        private lateinit var sourceFooSimpleList: List<String?>
        private lateinit var fromSubFoo: FromSubFoo
    }
}
