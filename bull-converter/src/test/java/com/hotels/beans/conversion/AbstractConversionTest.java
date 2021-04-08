/**
 * Copyright (C) 2019-2021 Expedia, Inc.
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
package com.hotels.beans.conversion;

/**
 * Abstract class containing all methods/fields common to all Conversion test classes.
 */
public abstract class AbstractConversionTest {
    protected static final Byte BYTE_VALUE = 10;
    protected static final Short SHORT_VALUE = 10;
    protected static final Integer INTEGER_VALUE = 10;
    protected static final Long LONG_VALUE = 10L;
    protected static final Float FLOAT_VALUE = 10f;
    protected static final Double DOUBLE_VALUE = 10d;
    protected static final char CHAR_VALUE = '1';
    protected static final Boolean BOOLEAN_VALUE = Boolean.TRUE;
    protected static final String STRING_VALUE = "10";
    protected static final byte[] ONE_BYTE_BYTE_ARRAY = new byte[] {1};
    protected static final byte[] EIGHT_BYTE_BYTE_ARRAY = new byte[] {1, 2, 3, 4, 5, 6, 7, 8};
    protected static final Byte BYTE_VALUE_ZERO = 0;
    protected static final Short SHORT_VALUE_ZERO = 0;
    protected static final Integer INTEGER_VALUE_ZERO = 0;
    protected static final Long LONG_VALUE_ZERO = 0L;
    protected static final Float FLOAT_VALUE_ZERO = 0f;
    protected static final Double DOUBLE_VALUE_ZERO = 0d;
    protected static final String TRUE_AS_STRING = "true";
}
