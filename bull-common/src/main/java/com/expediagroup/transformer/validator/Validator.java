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
package com.expediagroup.transformer.validator;

import static java.util.Objects.isNull;

import java.util.List;
import java.util.Set;

import jakarta.validation.ConstraintViolation;

import com.expediagroup.transformer.error.InvalidBeanException;

/**
 * Java Bean validation class.
 * It offers the possibility to validate a given Java Bean against a set of defined constraints.
 */
public interface Validator {
    /**
     * Checks if an object is valid and returns the list of the constraints not met.
     * @param <K> the object class
     * @param k the object to check
     * @return the list of violated constraints.
     */
    <K> Set<ConstraintViolation<Object>> getConstraintViolations(K k);

    /**
     * Checks if an object is valid and returns the list of the constraints messages not met.
     * @param <K> the object class
     * @param k the object to check
     * @return the list of violated constraints messages.
     */
    <K> List<String> getConstraintViolationsMessages(K k);

    /**
     * Checks if an object is valid.
     * @param <K> the object class
     * @param k the object to check
     * @throws InvalidBeanException {@link InvalidBeanException} if the validation fails
     */
    <K> void validate(K k);

    /**
     * Validate that the specified argument is not {@code null};
     * otherwise throws an {@link IllegalArgumentException}.
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
