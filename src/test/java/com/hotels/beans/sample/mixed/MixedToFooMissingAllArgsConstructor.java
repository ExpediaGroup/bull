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

package com.hotels.beans.sample.mixed;

import java.math.BigInteger;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.hotels.beans.sample.immutable.ImmutableToSubFoo;

import lombok.Getter;
import lombok.Setter;

/**
 * Sample mixed object with a constructor containing the final fields only.
 */
@Getter
@Setter
public class MixedToFooMissingAllArgsConstructor {
    @NotNull
    public BigInteger id;
    private final String name;
    private final List<String> list;
    private final List<ImmutableToSubFoo> nestedObjectList;
    private ImmutableToSubFoo nestedObject;

    /**
     * Constructor for final fields only.
     */
    public MixedToFooMissingAllArgsConstructor(final String name, final List<String> list, final List<ImmutableToSubFoo> nestedObjectList) {
        this.name = name;
        this.list = list;
        this.nestedObjectList = nestedObjectList;
    }

    @Override
    public String toString() {
        return "MixedToFooMissingAllArgsConstructor";
    }
}
