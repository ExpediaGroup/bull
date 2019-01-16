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

package com.hotels.beans.model;

import java.util.function.Function;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Specifies the field mapping between the source object and destination one.
 *
 * @param <T> input field type.
 * @param <K> field value on with apply the function.
 */
@AllArgsConstructor
@Getter
@ToString
public class FieldTransformer<T, K> {
    /**
     * The field name in the destination object.
     */
    private final String destFieldName;

    /**
     * The field transformer function.
     */
    private final Function<T, K> transformerFunction;
}
