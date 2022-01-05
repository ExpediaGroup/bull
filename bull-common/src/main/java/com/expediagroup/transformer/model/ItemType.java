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
package com.expediagroup.transformer.model;

import lombok.Builder;
import lombok.Getter;

/**
 * Bean class for mapping the "Generic" object type and its nested generic (if any).
 */
@Getter
@Builder
public class ItemType implements MapElemType {
    /**
     * The object class.
     * i.e. In case of: {@code Map<List<String>, Integer>} the value wil be {@code List}
     */
    private final Class<?> objectClass;

    /**
     * The generic object class in the key object (if any).
     * i.e. In case of: {@code Map<List<String>, Integer>} the value wil be {@code String}
     */
    private final Class<?> genericClass;
}
