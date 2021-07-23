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
package com.expediagroup.transformer.constant;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

/**
 * Default method prefix.
 */
@NoArgsConstructor(access = PRIVATE)
public enum MethodPrefix {
    /**
     * Getter method prefix.
     */
    GET,
    /**
     * Setter method prefix.
     */
    SET,
    /**
     * Boolean method prefix.
     */
    IS;

    /**
     * Gets the method prefix.
     * @return the method prefix.
     */
    public final String getPrefix() {
        return name().toLowerCase();
    }
}
