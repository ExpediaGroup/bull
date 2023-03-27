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
package com.expediagroup.transformer.constant;

/**
 * Class type definition.
 */
public enum ClassType {
    /**
     * The class is mutable.
     */
    MUTABLE,
    /**
     * The class is immutable.
     */
    IMMUTABLE,
    /**
     * The class contains both final and not final fields.
     */
    MIXED;

    /**
     * Checks if a the class type  instance is equal to the given one.
     * @param classType the {@link ClassType} to which compare
     * @return true if this is equals to the given class type
     */
    public boolean is(final ClassType classType) {
        return this == classType;
    }
}
