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

package com.hotels.beans.validator;

import static java.util.Objects.isNull;

/**
 * Java Bean validation class.
 * It offers the possibility to validate a given Java Bean against a set of defined constraints.
 */
public interface Validator {
    /**
     * Checks if an object is valid.
     * @param k the object to check
     * @param <K> the object class
     * @throws com.hotels.beans.error.InvalidBeanException {@link com.hotels.beans.error.InvalidBeanException} if the validation fails
     */
    <K> void validate(final K k);

    /**
     * Validate that the specified argument is not {@code null};
     * otherwise throws an {@link IllegalArgumentException} with the specified message.
     * @param object  the object to check
     * @param <T> the object type
     * @throws IllegalArgumentException if the object is {@code null}
     */
    static <T> void notNull(final T object) {
        if (isNull(object)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Validate that the specified argument is not {@code null};
     * otherwise throws an {@link IllegalArgumentException} with the specified message.
     * @param object  the object to check
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param <T> the object type
     * @throws IllegalArgumentException if the object is {@code null}
     */
    static <T> void notNull(final T object, final String message) {
        if (isNull(object)) {
            throw new IllegalArgumentException(message);
        }
    }
}
