/**
 * Copyright (C) 2019-2023 Expedia, Inc.
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
package com.expediagroup.transformer.model;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.expediagroup.transformer.error.MissingFieldException;

import lombok.Getter;
import lombok.Setter;

/**
 * Transformer object configuration.
 * It contains:
 *  1) The field name mapping
 *  2) The lambda function to apply on a field.
 *  3) Other configurations.
 * @param <T> source element type
 */
@Getter
public class TransformerSettings<T> {
    /**
     * Contains the mapping between fields's name in the source object and the destination one.
     */
    private final Map<T, T> fieldsNameMapping = new ConcurrentHashMap<>();

    /**
     * Contains the lambda functions to be applied on a given fields.
     * This feature allows applying transformation on the destination object value.
     * e.g. The following instructions allows to negate the destination value of the identifier field.
     * {@code
     *      FieldTransformer<BigInteger, BigInteger> fieldTransformer = new FieldTransformer<>("identifier", BigInteger::negate);
     * }
     */
    private final Map<T, FieldTransformer> fieldsTransformers = new ConcurrentHashMap<>();

    /**
     * Contains the list of fields that don't need to be transformed.
     */
    private final Set<String> fieldsToSkip = new HashSet<>();

    /**
     * It allows to configure the transformer in order to set a default value in case some field is missing in the source object.
     * If set to true the default value is set, if false if it raises a: {@link MissingFieldException} in case of missing fields.
     */
    @Setter
    private boolean setDefaultValueForMissingField;

    /**
     * It allows to enable/disable the set of the default value for primitive types in case they are null.
     * If set to true the default value is set.
     */
    @Setter
    private boolean defaultValueForMissingPrimitiveField = true;

    /**
     * It allows to apply a transformation to all fields matching with the provided name without using their whole path.
     * If set to true the transformation function is applied to all fields that have a name matching with the given one without evaluating their full path.
     */
    @Setter
    private boolean flatFieldNameTransformation;

    /**
     * It allows to enable the object validation.
     * If set to true the validation is performed.
     */
    @Setter
    private boolean validationEnabled;

    /**
     * It allows to enable/disable the automatic conversion of primitive types.
     * If set to true primitive types are transformed automatically.
     */
    @Setter
    private boolean primitiveTypeConversionEnabled;

    /**
     * It allows to enable/disable the transformation of Java Bean with a custom Builder pattern.
     * if true Java Beans with a custom Builder pattern are transformed automatically.
     */
    @Setter
    private boolean customBuilderTransformationEnabled;
}
