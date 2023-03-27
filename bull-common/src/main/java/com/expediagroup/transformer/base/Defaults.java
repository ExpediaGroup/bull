/**
 * Copyright (C) 2018-2023 Expedia, Inc.
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
package com.expediagroup.transformer.base;

import static java.lang.Boolean.FALSE;

import static com.expediagroup.transformer.utils.ClassUtils.isBoolean;
import static com.expediagroup.transformer.utils.ClassUtils.isByte;
import static com.expediagroup.transformer.utils.ClassUtils.isChar;
import static com.expediagroup.transformer.utils.ClassUtils.isDouble;
import static com.expediagroup.transformer.utils.ClassUtils.isFloat;
import static com.expediagroup.transformer.utils.ClassUtils.isInt;
import static com.expediagroup.transformer.utils.ClassUtils.isLong;
import static com.expediagroup.transformer.utils.ClassUtils.isShort;

import lombok.experimental.UtilityClass;

/**
 * Default values for primitive types.
 */
@UtilityClass
public class Defaults {
    /**
     * Gets the default value of a primitive type.
     * @param type the class type
     * @return the default value of a primitive type.
     */
    public static Object defaultValue(final Class<?> type) {
        Object res = null;
        if (isBoolean(type)) {
            res = FALSE;
        } else if (isChar(type)) {
            res = '\u0000';
        } else if (isByte(type) || isInt(type) || isShort(type)) {
            res = 0;
        } else if (isLong(type)) {
            res = 0L;
        } else if (isFloat(type)) {
            res = 0.0F;
        } else if (isDouble(type)) {
            res = 0.0D;
        }
        return res;
    }
}
