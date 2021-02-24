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

package com.hotels.beans.sample.immutable;

import jakarta.validation.constraints.NotNull;

import com.hotels.transformer.annotation.ConstructorArg;

import lombok.Getter;
import lombok.ToString;

/**
 * Sample immutable object without all constructor's parameter annotated with {@link ConstructorArg}.
 */
@Getter
@ToString
public class ImmutableToFooMissingCustomAnnotation {
    @NotNull
    private final String name;

    private final int id;

    /**
     * Constructor.
     */
    public ImmutableToFooMissingCustomAnnotation(@ConstructorArg("name") final String name, final int id) {
        this.name = name;
        this.id = id;
    }
}
