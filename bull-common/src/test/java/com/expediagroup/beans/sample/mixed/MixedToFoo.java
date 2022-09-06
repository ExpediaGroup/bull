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

import javax.validation.constraints.NotNull;

import com.expediagroup.beans.sample.immutable.ImmutableToSubFoo;

/**
 * Sample mixed object.
 */
public class MixedToFoo {
    private final String name;
    private final List<String> list;
    private final List<ImmutableToSubFoo> nestedObjectList;
    @NotNull
    public BigInteger id;
    private ImmutableToSubFoo nestedObject;

    public MixedToFoo(final BigInteger id, final String name, final List<String> list, final List<ImmutableToSubFoo> nestedObjectList, final ImmutableToSubFoo nestedObject) {
        this.id = id;
        this.name = name;
        this.list = list;
        this.nestedObjectList = nestedObjectList;
        this.nestedObject = nestedObject;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(final BigInteger id) {
        this.id = id;
    }

    public String getName() {
        return name;
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
        return "MixedToFoo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", list=" + list +
                ", nestedObjectList=" + nestedObjectList +
                ", nestedObject=" + nestedObject +
                '}';
    }
}
