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
package com.expediagroup.beans.sample.mixed;

import java.math.BigInteger;
import java.util.List;

import com.expediagroup.beans.sample.immutable.ImmutableToSubFoo;
import com.expediagroup.transformer.annotation.ConstructorArg;

/**
 * Sample mixed object with different column names.
 */
public class MixedToFooDiffFields {
    private final BigInteger identifier;
    private final List<String> list;
    private final List<ImmutableToSubFoo> nestedObjectList;
    private String name;
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

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigInteger getIdentifier() {
        return identifier;
    }

    public List<String> getList() {
        return list;
    }

    public List<ImmutableToSubFoo> getNestedObjectList() {
        return nestedObjectList;
    }

    public ImmutableToSubFoo getNestedObject() {
        return nestedObject;
    }

    public void setNestedObject(final ImmutableToSubFoo nestedObject) {
        this.nestedObject = nestedObject;
    }

    @Override
    public String toString() {
        return "MixedToFooDiffFields{" +
                "name='" + name + '\'' +
                ", identifier=" + identifier +
                ", list=" + list +
                ", nestedObjectList=" + nestedObjectList +
                ", nestedObject=" + nestedObject +
                '}';
    }
}
