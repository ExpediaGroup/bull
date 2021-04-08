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
package com.hotels.beans.sample.mixed;

import java.math.BigInteger;
import java.util.List;

import com.hotels.beans.sample.immutable.ImmutableToSubFoo;
import com.hotels.transformer.annotation.ConstructorArg;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Sample mixed object with different column names.
 */
@Getter
@Setter
@ToString
public class MixedToFooDiffFields {
    private String name;
    private final BigInteger identifier;
    private final List<String> list;
    private final List<ImmutableToSubFoo> nestedObjectList;
    private ImmutableToSubFoo nestedObject;

    public MixedToFooDiffFields(@ConstructorArg("name") final String name,
        @ConstructorArg("identifier") final BigInteger identifier,
        @ConstructorArg("list") final List<String> list, @ConstructorArg("nestedObjectList") final List<ImmutableToSubFoo> nestedObjectList,
        @ConstructorArg("nestedObject") final ImmutableToSubFoo nestedObject) {
        this.name = name;
        this.identifier = identifier;
        this.list = list;
        this.nestedObjectList = nestedObjectList;
        this.nestedObject = nestedObject;
    }
}
