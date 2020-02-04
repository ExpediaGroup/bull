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

package com.hotels.beans.generator.core.mapping;

import static com.hotels.transformer.constant.MethodPrefix.IS;

import java.lang.reflect.Method;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

/**
 * Represents a property underlying a method according to JavaBean conventions.
 * The {@code toString()} implementation returns the property name.
 */
final class BeanProperty {
    /**
     * The length of a short prefix, usually "is".
     */
    private static final int SHORT_PREFIX_LENGTH = 2;
    /**
     * The length of a regular prefix, such as "get" or "set".
     */
    private static final int REGULAR_PREFIX_LENGTH = 3;
    /**
     * The property name. Retains the same case as in the method name,
     * minus the prefix.
     */
    private final String name;

    /**
     * Instantiates a new Bean property for the given {@code accessor}.
     * @param accessor a JavaBean accessor method
     */
    BeanProperty(final Method accessor) {
        var methodName = accessor.getName();
        this.name = methodName.startsWith(IS.getPrefix())
                ? methodName.substring(SHORT_PREFIX_LENGTH)
                : methodName.substring(REGULAR_PREFIX_LENGTH);
    }

    @Override
    public String toString() {
        return StringUtils.uncapitalize(name);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BeanProperty property = (BeanProperty) o;
        return name.equals(property.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
