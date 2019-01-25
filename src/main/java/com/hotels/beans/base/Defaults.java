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

package com.hotels.beans.base;

import static java.lang.Boolean.FALSE;

import static org.springframework.util.Assert.notNull;

import static com.hotels.beans.utils.ClassUtils.isBoolean;
import static com.hotels.beans.utils.ClassUtils.isByte;
import static com.hotels.beans.utils.ClassUtils.isChar;
import static com.hotels.beans.utils.ClassUtils.isDouble;
import static com.hotels.beans.utils.ClassUtils.isFloat;
import static com.hotels.beans.utils.ClassUtils.isInt;
import static com.hotels.beans.utils.ClassUtils.isLong;
import static com.hotels.beans.utils.ClassUtils.isShort;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

/**
 * Default values for primitive types.
 */
@NoArgsConstructor(access = PRIVATE)
public final class Defaults {
    /**
     * Gets the default value of a primitive type.
     * @param type the class type
     * @return the default value of a primitive type.
     */
    public static Object defaultValue(final Class<?> type) {
        notNull(type, "type cannot be null!");
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
