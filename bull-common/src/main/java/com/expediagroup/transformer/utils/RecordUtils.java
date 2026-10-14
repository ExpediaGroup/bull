/**
 * Copyright (C) 2019-2026 Expedia, Inc.
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
package com.expediagroup.transformer.utils;

import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Utility class for working with Java Records (Java 16+).
 * Provides optimized methods for record introspection and component access.
 */
public final class RecordUtils {
    /**
     * Private constructor to prevent instantiation.
     */
    private RecordUtils() {
    }

    /**
     * Gets all record components for a record class.
     * Records have immutable accessor methods corresponding to each component.
     * @param recordClass the record class
     * @return array of record components, or empty array if not a record
     */
    public static RecordComponent[] getRecordComponents(final Class<?> recordClass) {
        if (!recordClass.isRecord()) {
            return new RecordComponent[0];
        }
        return recordClass.getRecordComponents();
    }

    /**
     * Gets record component names in order.
     * @param recordClass the record class
     * @return list of component names
     */
    public static List<String> getRecordComponentNames(final Class<?> recordClass) {
        return Arrays.stream(getRecordComponents(recordClass))
                .map(RecordComponent::getName)
                .toList();
    }

    /**
     * Gets record component types in order.
     * @param recordClass the record class
     * @return list of component types
     */
    @SuppressWarnings("unchecked")
    public static List<Class<?>> getRecordComponentTypes(final Class<?> recordClass) {
        return (List<Class<?>>) (List<?>) Arrays.stream(getRecordComponents(recordClass))
                .map(RecordComponent::getType)
                .toList();
    }

    /**
     * Streams all record components.
     * @param recordClass the record class
     * @return stream of record components
     */
    public static Stream<RecordComponent> streamComponents(final Class<?> recordClass) {
        return Arrays.stream(getRecordComponents(recordClass));
    }

    /**
     * Checks if a class is a record.
     * @param recordClass the class to check
     * @return true if the class is a record
     */
    public static boolean isRecord(final Class<?> recordClass) {
        return recordClass.isRecord();
    }

    /**
     * Gets the canonical constructor of a record class.
     * Records have exactly one constructor matching all record components.
     * @param recordClass the record class
     * @return the canonical constructor or null if not a record
     */
    public static java.lang.reflect.Constructor<?> getCanonicalConstructor(final Class<?> recordClass) {
        if (!recordClass.isRecord()) {
            return null;
        }
        var components = getRecordComponents(recordClass);
        var parameterTypes = Arrays.stream(components)
                .map(RecordComponent::getType)
                .toArray(Class[]::new);
        return getConstructorOrNull(recordClass, parameterTypes);
    }

    /**
     * Safely gets a declared constructor or returns null.
     * Package-private to allow testing the not-found path, which records never hit via the canonical constructor.
     * @param recordClass the record class
     * @param parameterTypes the constructor parameter types
     * @return the constructor or null if not found
     */
    @SuppressWarnings("java:S1166")
    static java.lang.reflect.Constructor<?> getConstructorOrNull(final Class<?> recordClass,
        final Class<?>[] parameterTypes) {
        try {
            return recordClass.getDeclaredConstructor(parameterTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
