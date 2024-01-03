/**
 * Copyright (C) 2019-2023 Expedia, Inc.
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
package com.expediagroup.beans.sample.mixed;

import java.math.BigInteger;
import java.util.List;

import com.expediagroup.beans.sample.immutable.ImmutableToSubFoo;

import lombok.Builder;
import lombok.Getter;

/**
 * Mixed bean instantiable only through a Builder.
 */
@Getter
@Builder
public class MixedToFooWithBuilder {
    private final String name;
    private BigInteger id;
    private final List<ImmutableToSubFoo> nestedObjectList;
    private final List<String> list;
    private final ImmutableToSubFoo nestedObject;

    public MixedToFooWithBuilder(final String name, final BigInteger id, final List<ImmutableToSubFoo> nestedObjectList,
        final List<String> list, final ImmutableToSubFoo nestedObject) {
        this.name = name;
        this.id = id;
        this.nestedObjectList = nestedObjectList;
        this.list = list;
        this.nestedObject = nestedObject;
    }
}
