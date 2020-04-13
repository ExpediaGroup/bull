/**
 * Copyright (C) 2019-2020 Expedia, Inc.
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

package com.hotels.transformer.constant;

import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isStatic;

import static com.hotels.transformer.utils.ClassUtils.BUILD_METHOD_NAME;

import static lombok.AccessLevel.PRIVATE;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import lombok.NoArgsConstructor;

/**
 * Filters conditions.
 */
@NoArgsConstructor(access = PRIVATE)
public final class Filters {
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
     * Returns true if the method is the Builder "build" one and it returns the given class.
     */
    public static final BiPredicate<Method, Class> IS_BUILDER_CLASS =
            (method, targetClass) -> method.getName().equals(BUILD_METHOD_NAME) && method.getReturnType().equals(targetClass);

    /**
     * Returns only the final field.
     */
    private static final Predicate<Field> IS_FINAL_FIELD = field -> isFinal(field.getModifiers());

    /**
     * Returns only the not final field.
     */
    public static final Predicate<Field> IS_NOT_FINAL_FIELD = IS_FINAL_FIELD.negate();

}
