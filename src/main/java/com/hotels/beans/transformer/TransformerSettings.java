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

package com.hotels.beans.transformer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import lombok.Getter;

/**
 * Transformer object configuration.
 * It contains:
 *  1) The field name mapping
 *  2) The lambda function to apply on a field.
 */
@Getter
final class TransformerSettings {
    /**
     * Contains the mapping between fields's name in the source object and the destination one.
     */
    private final Map<String, String> fieldsNameMapping = new ConcurrentHashMap<>();

    /**
     * Contains the lambda functions to be applied on a given fields.
     * This features allows to apply transformation on the destination object value.
     * e.g. The following instructions allows to negate the destination value of the identifier field.
     * {@code
     *      FieldTransformer<BigInteger, BigInteger> fieldTransformer = new FieldTransformer<>("identifier", BigInteger::negate);
     * }
     */
    private final Map<String, Function<Object, Object>> fieldsTransformers = new ConcurrentHashMap<>();
}
