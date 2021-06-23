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
package com.hotels.transformer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Specifies the field's name mapping between the source object and destination one.
 * @param <T> source element type
 * @param <K> target element type
 */
@AllArgsConstructor
@Getter
@ToString
public class FieldMapping<T, K> {
    /**
     * The field name in the source object.
     */
    private final T sourceFieldName;

    /**
     * The field name in the destination object.
     */
    private final K destFieldName;
}
