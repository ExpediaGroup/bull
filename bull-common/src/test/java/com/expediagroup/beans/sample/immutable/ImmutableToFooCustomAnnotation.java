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
package com.expediagroup.beans.sample.immutable;

import java.math.BigInteger;
import java.util.List;

import jakarta.validation.constraints.NotNull;

import com.expediagroup.transformer.annotation.ConstructorArg;

import lombok.ToString;

/**
 * Sample immutable object.
 */
@ToString
public class ImmutableToFooCustomAnnotation {
    @NotNull
    private final String name;
    @NotNull
    private final BigInteger id;
    @NotNull
    private final List<String> list;
    @NotNull
    private final List<ImmutableToSubFooCustomAnnotation> nestedObjectList;
    @NotNull
    private final ImmutableToSubFooCustomAnnotation nestedObject;

    public ImmutableToFooCustomAnnotation(@ConstructorArg("name") final String name,
        @ConstructorArg("id") final BigInteger id,
        @ConstructorArg("list") final List<String> list,
        @ConstructorArg("nestedObjectList") final List<ImmutableToSubFooCustomAnnotation> nestedObjectList,
        @ConstructorArg("nestedObject") final ImmutableToSubFooCustomAnnotation nestedObject) {
        this.name = name;
        this.id = id;
        this.list = list;
        this.nestedObjectList = nestedObjectList;
        this.nestedObject = nestedObject;
    }

    public String getName() {
        return name;
    }

    public BigInteger getId() {
        return id;
    }

    public List<String> getList() {
        return list;
    }

    public List<ImmutableToSubFooCustomAnnotation> getNestedObjectList() {
        return nestedObjectList;
    }

    public ImmutableToSubFooCustomAnnotation getNestedObject() {
        return nestedObject;
    }
}
