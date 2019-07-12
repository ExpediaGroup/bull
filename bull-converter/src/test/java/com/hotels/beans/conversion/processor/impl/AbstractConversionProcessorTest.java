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

package com.hotels.beans.conversion.processor.impl;

/**
 * Abstract class containing all methods/fields common to all Conversion Processor test classes.
 */
public abstract class AbstractConversionProcessorTest {
    static final Byte BYTE_VALUE = 10;
    static final Byte BYTE_VALUE_ZERO = 0;
    static final Short SHORT_VALUE = 10;
    static final Short SHORT_VALUE_ZERO = 0;
    static final Integer INTEGER_VALUE = 10;
    static final Integer INTEGER_VALUE_ZERO = 0;
    static final Long LONG_VALUE = 10L;
    static final Long LONG_VALUE_ZERO = 0L;
    static final Float FLOAT_VALUE = 10f;
    static final Float FLOAT_VALUE_ZERO = 0f;
    static final Double DOUBLE_VALUE = 10d;
    static final Double DOUBLE_VALUE_ZERO = 0d;
    static final char CHAR_VALUE = '1';
    static final Boolean BOOLEAN_VALUE = Boolean.TRUE;
    static final String STRING_VALUE = "10";
    static final String TRUE_AS_STRING = "true";
    static final char TRUE_AS_CHAR = 'T';
    static final char FALSE_AS_CHAR = 'F';
}
