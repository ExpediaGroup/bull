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
package com.expediagroup.beans.conversion

import java.lang.Boolean
import kotlin.Byte
import kotlin.Short
import kotlin.byteArrayOf

/**
 * Abstract class containing all methods/fields common to all Conversion test classes.
 */
abstract class AbstractConversionTest {
    companion object {
        protected const val BYTE_VALUE: Byte = 10
        protected const val SHORT_VALUE: Short = 10
        protected const val INTEGER_VALUE = 10
        protected const val LONG_VALUE = 10L
        protected const val FLOAT_VALUE = 10f
        protected const val DOUBLE_VALUE = 10.0
        protected const val CHAR_VALUE = '1'
        protected val BOOLEAN_VALUE = Boolean.TRUE!!
        protected const val STRING_VALUE = "10"
        protected val ONE_BYTE_BYTE_ARRAY = byteArrayOf(1)
        protected val EIGHT_BYTE_BYTE_ARRAY = byteArrayOf(1, 2, 3, 4, 5, 6, 7, 8)
        protected const val BYTE_VALUE_ZERO: Byte = 0
        protected const val SHORT_VALUE_ZERO: Short = 0
        protected const val INTEGER_VALUE_ZERO = 0
        protected const val LONG_VALUE_ZERO = 0L
        protected const val FLOAT_VALUE_ZERO = 0f
        protected const val DOUBLE_VALUE_ZERO = 0.0
        protected const val TRUE_AS_STRING = "true"
    }
}