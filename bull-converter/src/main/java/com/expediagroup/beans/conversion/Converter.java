/**
 * Copyright (C) 2019-2022 Expedia, Inc.
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
package com.expediagroup.beans.conversion;

import java.util.Optional;
import java.util.function.Function;

import com.expediagroup.beans.conversion.error.TypeConversionException;

/**
 * It allows to convert any primitive type into another.
 */
public interface Converter {
    /**
     * It provides a conversion function for the given primitive type into the other.
     * @param sourceClass source field class
     * @param targetClass the destination field class
     * @return an {@link Optional} containing the conversion function (if exists)
     */
    Optional<Function<Object, Object>> getConversionFunction(Class<?> sourceClass, Class<?> targetClass);

    /**
     * Converts a given primitive value into the given primitive type.
     * @param valueToConvert the value to be converted
     * @param targetClass the destination field class
     * @param <T> the value to convert type
     * @param <K> the target object type
     * @return the converted value
     * @throws TypeConversionException in case there is no converter for the given class
     * @throws IllegalArgumentException in case the destination field type is null.
     */
    <T, K> K convertValue(T valueToConvert, Class<K> targetClass);
}
