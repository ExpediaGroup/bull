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
package com.expediagroup.beans.sample.immutable;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Sample immutable class that also declares a static (non-final) field.
 * Used to verify that static non-final fields are correctly excluded when
 * determining the class type (IMMUTABLE: has final instance fields, no
 * non-final instance fields).
 */
@AllArgsConstructor
@Getter
public class ImmutableToFooWithStaticField {
    /**
     * Static non-final field — must be ignored by the IS_NOT_FINAL_AND_NOT_STATIC_FIELD filter.
     */
    private static int instanceCount = 0;

    /**
     * Non-static final field — makes this class IMMUTABLE.
     */
    private final String name;
}
