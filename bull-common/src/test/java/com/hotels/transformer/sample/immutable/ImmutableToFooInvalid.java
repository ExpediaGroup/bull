/**
 * Copyright (C) 2019 Expedia, Inc.
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

package com.hotels.transformer.sample.immutable;

import java.math.BigInteger;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Getter;

/**
 * Sample immutable object with constructor containing parameter in a different order than expected.
 */
@Getter
public class ImmutableToFooInvalid {
    @NotNull
    private final String name;
    @NotNull
    private final BigInteger id;

    private final List<String> list;
    @NotNull
    private final List<ImmutableToSubFoo> nestedObjectList;
    @NotNull
    private final ImmutableToSubFoo nestedObject;

    /**
     * Constructor.
     */
    public ImmutableToFooInvalid(@NotNull final String name, @NotNull final BigInteger id, @NotNull final List<ImmutableToSubFoo> nestedObjectList,
        @NotNull final ImmutableToSubFoo nestedObject) {
        this.name = name;
        this.id = id;
        this.list = null;
        this.nestedObjectList = nestedObjectList;
        this.nestedObject = nestedObject;
    }

    @Override
    public String toString() {
        return "ImmutableToFooInvalid";
    }
}
