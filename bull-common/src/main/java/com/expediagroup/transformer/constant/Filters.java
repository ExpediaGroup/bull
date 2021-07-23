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
package com.expediagroup.transformer.constant;

import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isStatic;

import java.lang.reflect.Field;
import java.util.function.Predicate;

import lombok.experimental.UtilityClass;

/**
 * Filters conditions.
 */
@UtilityClass
public class Filters {
    /**
     * Returns only the final not static field.
     */
    public static final Predicate<Field> IS_FINAL_AND_NOT_STATIC_FIELD = field -> {
        int modifiers = field.getModifiers();
        return isFinal(modifiers) && !isStatic(modifiers);
    };

    /**
     * Returns only the not final not static field.
     */
    public static final Predicate<Field> IS_NOT_FINAL_AND_NOT_STATIC_FIELD = field -> {
        int modifiers = field.getModifiers();
        return !isFinal(modifiers) && !isStatic(modifiers);
    };

    /**
     * Returns only the final field.
     */
    private static final Predicate<Field> IS_FINAL_FIELD = field -> isFinal(field.getModifiers());

    /**
     * Returns only the not final field.
     */
    public static final Predicate<Field> IS_NOT_FINAL_FIELD = IS_FINAL_FIELD.negate();

}
